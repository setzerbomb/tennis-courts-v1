package com.tenniscourts.schedules;

import com.tenniscourts.exceptions.EntityNotFoundException;
import com.tenniscourts.tenniscourts.TennisCourt;
import com.tenniscourts.tenniscourts.TennisCourtDTO;
import com.tenniscourts.tenniscourts.TennisCourtMapper;
import com.tenniscourts.tenniscourts.TennisCourtRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ScheduleDTOResolver {

    private TennisCourtRepository tennisCourtRepository;

    private TennisCourtMapper tennisCourtMapper;

    public ScheduleDTO resolve(ScheduleDTO scheduleDTO){
        TennisCourtDTO tennisCourtDTO = tennisCourtRepository.findById(scheduleDTO.getTennisCourtId()).map(tennisCourtMapper::map).orElseThrow(() -> {
            throw new EntityNotFoundException("Tennis Court not found.");
        });
        scheduleDTO.setTennisCourt(tennisCourtDTO);

        return scheduleDTO;
    }
}
