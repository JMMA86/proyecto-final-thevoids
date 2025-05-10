package org.thevoids.oncologic.service;

import org.thevoids.oncologic.dto.ScheduleDTO;

import java.util.List;

public interface ScheduleService {
    List<ScheduleDTO> getAllSchedules();

    ScheduleDTO getScheduleById(Long id);

    ScheduleDTO createSchedule(ScheduleDTO scheduleDTO);

    ScheduleDTO updateSchedule(ScheduleDTO scheduleDTO);

    void deleteSchedule(Long id);
}
