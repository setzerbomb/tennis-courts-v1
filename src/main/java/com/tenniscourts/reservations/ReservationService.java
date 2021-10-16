package com.tenniscourts.reservations;

import com.tenniscourts.exceptions.AlreadyExistsEntityException;
import com.tenniscourts.exceptions.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
@Log4j2
public class ReservationService {

    private final ReservationRepository reservationRepository;

    private final ReservationMapper reservationMapper;

    private final ReservationDTOResolver reservationDTOResolver;

    public ReservationDTO bookReservation(CreateReservationRequestDTO createReservationRequestDTO) {
        scheduleValidation(createReservationRequestDTO.getScheduleId());

        Reservation reservation = reservationMapper.map(
                reservationDTOResolver.resolve(ReservationDTO.builder()
                        .guestId(createReservationRequestDTO.getGuestId())
                        .scheduledId(createReservationRequestDTO.getScheduleId())
                        .build())
        );

        reservation.setValue(BigDecimal.valueOf(10));

        if (Objects.nonNull(createReservationRequestDTO.getPreviousReservation()))
            reservation.setPreviousReservationId(createReservationRequestDTO.getPreviousReservation());

        return reservationMapper.map(reservationRepository.save(reservation));
    }

    public ReservationDTO findReservation(Long reservationId) {
        return reservationRepository.findById(reservationId).map(reservationMapper::map).orElseThrow(() -> {
            throw new EntityNotFoundException("Reservation not found.");
        });
    }

    public ReservationDTO cancelReservation(Long reservationId) {
        return reservationMapper.map(this.cancel(reservationId));
    }

    private Reservation cancel(Long reservationId) {
        return reservationRepository.findById(reservationId).map(reservation -> {

            this.validateCancellation(reservation);

            BigDecimal refundValue = getRefundValue(reservation);
            return this.updateReservation(reservation, refundValue, ReservationStatus.CANCELLED);

        }).orElseThrow(() -> {
            throw new EntityNotFoundException("Reservation not found.");
        });
    }

    private Reservation updateReservation(Reservation reservation, BigDecimal refundValue, ReservationStatus status) {
        reservation.setReservationStatus(status);
        reservation.setValue(reservation.getValue().subtract(refundValue));
        reservation.setRefundValue(refundValue);

        return reservationRepository.save(reservation);
    }

    private void validateCancellation(Reservation reservation) {
        if (!ReservationStatus.READY_TO_PLAY.equals(reservation.getReservationStatus())) {
            throw new IllegalArgumentException("Cannot cancel/reschedule because it's not in ready to play status.");
        }

        if (reservation.getSchedule().getStartDateTime().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Can cancel/reschedule only future dates.");
        }
    }

    public BigDecimal getRefundValue(Reservation reservation) {
        long minutes = ChronoUnit.MINUTES.between(LocalDateTime.now(), reservation.getSchedule().getStartDateTime());

        if (minutes <= 0) {
            return BigDecimal.ZERO;
        }

        BigDecimal hours = BigDecimal.valueOf(minutes).divide(BigDecimal.valueOf(60),2, RoundingMode.HALF_UP);

        return hours.compareTo(BigDecimal.valueOf(24)) == 1 ? reservation.getValue() : (
                    hours.compareTo(BigDecimal.valueOf(12)) == 1 ? reservation.getValue().multiply(BigDecimal.valueOf(0.75)) : (
                            hours.compareTo(BigDecimal.valueOf(2)) == 1 ? reservation.getValue().multiply(BigDecimal.valueOf(0.50)) :
                                        reservation.getValue().multiply(BigDecimal.valueOf(0.25))
                            )
                );
    }

    public ReservationDTO rescheduleReservation(Long previousReservationId, Long scheduleId) {
        scheduleValidation(previousReservationId, scheduleId);

        Reservation previousReservation = cancel(previousReservationId);

        previousReservation.setReservationStatus(ReservationStatus.RESCHEDULED);
        reservationRepository.save(previousReservation);

        ReservationDTO newReservation = bookReservation(CreateReservationRequestDTO.builder()
                .guestId(previousReservation.getGuest().getId())
                .scheduleId(scheduleId)
                .previousReservation(previousReservationId)
                .build());
        newReservation.setPreviousReservation(reservationMapper.map(previousReservation));
        return newReservation;
    }

    private void scheduleValidation(Long reservationId, Long scheduleId) {

        List<Reservation> reservations = reservationRepository.findByIdIsNotAndSchedule_IdAndReservationStatus(
                reservationId,scheduleId, ReservationStatus.READY_TO_PLAY);

        if (reservations.size() > 0) throw new AlreadyExistsEntityException("The selected schedule is not available");
    }

    private void scheduleValidation(Long scheduleId) {

        List<Reservation> reservations = reservationRepository.findBySchedule_IdAndReservationStatus(
                scheduleId, ReservationStatus.READY_TO_PLAY);

        if (reservations.size() > 0) throw new AlreadyExistsEntityException("The selected schedule is not available");
    }
}
