package com.tenniscourts.schedules;

import com.tenniscourts.exceptions.AlreadyExistsEntityException;
import com.tenniscourts.exceptions.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;

    private final ScheduleMapper scheduleMapper;

    private final ScheduleDTOResolver scheduleDTOResolver;

    public ScheduleDTO addSchedule(Long tennisCourtId, CreateScheduleRequestDTO createScheduleRequestDTO) {
        List<Schedule> schedules = scheduleRepository.findIfCourtIsBeingUsedInThisTimeSpan(
                createScheduleRequestDTO.getTennisCourtId(),
                createScheduleRequestDTO.getStartDateTime(),
                createScheduleRequestDTO.getEndDateTime()
        );

        if (schedules.size() > 0) throw new AlreadyExistsEntityException("There's already an schedule created in this time span");

        Schedule schedule = scheduleRepository.save(
                scheduleMapper.map(
                        scheduleDTOResolver.resolve(ScheduleDTO.builder()
                                .tennisCourtId(tennisCourtId)
                                .startDateTime(createScheduleRequestDTO.getStartDateTime())
                                .endDateTime(createScheduleRequestDTO.getEndDateTime())
                                .build())
                )
        );
        return scheduleMapper.map(schedule);
    }

    public List<ScheduleDTO> findSchedulesByDates(LocalDateTime startDate, LocalDateTime endDate) {
        return scheduleMapper.map(scheduleRepository.findAvailableSchedulesByStartDateAndEndDate(startDate,endDate));
    }

    public ScheduleDTO findSchedule(Long scheduleId) {
        Optional<Schedule> schedule = scheduleRepository.findById(scheduleId);
        if (!schedule.isPresent())
            throw new EntityNotFoundException("Schedule not found");

        return scheduleMapper.map(schedule.get());
    }

    public List<ScheduleDTO> findSchedulesByTennisCourtId(Long tennisCourtId) {
        return scheduleMapper.map(scheduleRepository.findByTennisCourt_IdOrderByStartDateTime(tennisCourtId));
    }
}
