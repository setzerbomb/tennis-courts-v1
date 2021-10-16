package com.tenniscourts.reservations;

import com.tenniscourts.tenniscourts.TennisCourt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findBySchedule_Id(Long scheduleId);

    List<Reservation> findByReservationStatusAndSchedule_StartDateTimeGreaterThanEqualAndSchedule_EndDateTimeLessThanEqual(ReservationStatus reservationStatus, LocalDateTime startDateTime, LocalDateTime endDateTime);

    List<Reservation> findBySchedule_IdAndReservationStatus(Long scheduleId, ReservationStatus reservationStatus);

    List<Reservation> findByIdIsNotAndSchedule_IdAndReservationStatus(Long reservationId, Long scheduleId, ReservationStatus reservationStatus);

    // List<Reservation> findByStartDateTimeGreaterThanEqualAndEndDateTimeLessThanEqualAndTennisCourt(LocalDateTime startDateTime, LocalDateTime endDateTime, TennisCourt tennisCourt);
}
