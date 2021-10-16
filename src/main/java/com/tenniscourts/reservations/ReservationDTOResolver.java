package com.tenniscourts.reservations;

import com.tenniscourts.exceptions.EntityNotFoundException;
import com.tenniscourts.guests.GuestDTO;
import com.tenniscourts.guests.GuestMapper;
import com.tenniscourts.guests.GuestRepository;
import com.tenniscourts.schedules.ScheduleDTO;
import com.tenniscourts.schedules.ScheduleMapper;
import com.tenniscourts.schedules.ScheduleRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ReservationDTOResolver {

    private ScheduleRepository scheduleRepository;
    private ScheduleMapper scheduleMapper;

    private GuestRepository guestRepository;
    private GuestMapper guestMapper;

    public ReservationDTO resolve(ReservationDTO reservationDTO){
        ScheduleDTO scheduleDTO = scheduleRepository.findById(reservationDTO.getScheduledId()).map(scheduleMapper::map).orElseThrow(() -> {
            throw new EntityNotFoundException("Schedule not found.");
        });
        GuestDTO guestDTO = guestRepository.findById(reservationDTO.getGuestId()).map(guestMapper::map).orElseThrow(() -> {
            throw new EntityNotFoundException("Guest not found.");
        });

        reservationDTO.setSchedule(scheduleDTO);
        reservationDTO.setGuest(guestDTO);

        return reservationDTO;
    }
}
