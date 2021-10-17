package com.tenniscourts.reservations;

import com.tenniscourts.guests.GuestDTO;
import com.tenniscourts.schedules.ScheduleDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ReservationDTO {

    private Long id;

    private ScheduleDTO schedule;

    private GuestDTO guest;

    private String reservationStatus;

    private ReservationDTO previousReservation;

    private Long previousReservationId;

    private BigDecimal refundValue;

    private BigDecimal value;

    private BigDecimal tax;

    @NotNull
    private Long scheduledId;

    @NotNull
    private Long guestId;
}
