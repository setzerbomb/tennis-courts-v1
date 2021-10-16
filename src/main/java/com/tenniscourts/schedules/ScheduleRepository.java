package com.tenniscourts.schedules;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    List<Schedule> findByTennisCourt_IdOrderByStartDateTime(Long id);

    @Query(value = "select s from Schedule as s where s.tennisCourt.id = ?1 and " +
            "s.startDateTime >= ?2 and s.startDateTime <= ?3 or " +
            "s.endDateTime >= ?2 and s.endDateTime <= ?3")
    List<Schedule> findIfCourtIsBeingUsedInThisTimeSpan(Long tennisCourtId, LocalDateTime startDate, LocalDateTime endDate);

    @Query("select distinct s from Schedule as s left join Reservation as r on s.id=r.schedule.id " +
            "where s.startDateTime >= ?1 and s.endDateTime <= ?2 and (r.reservationStatus > 0 or r.reservationStatus is null)")
    List<Schedule> findAvailableSchedulesByStartDateAndEndDate(LocalDateTime startDate, LocalDateTime endDate);
}
