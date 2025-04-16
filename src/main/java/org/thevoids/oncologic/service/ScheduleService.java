package org.thevoids.oncologic.service;

import org.thevoids.oncologic.entity.Schedule;

import java.util.List;

public interface ScheduleService {
    List<Schedule> getAllSchedules();

    Schedule getScheduleById(Long id);

    Schedule createSchedule(Schedule schedule);

    Schedule updateSchedule(Schedule schedule);

    void deleteSchedule(Long id);
}
