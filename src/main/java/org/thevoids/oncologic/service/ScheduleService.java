package org.thevoids.oncologic.service;

import java.util.List;

import org.thevoids.oncologic.dto.entity.ScheduleDTO;

public interface ScheduleService {
    List<ScheduleDTO> getAllSchedules();

    ScheduleDTO getScheduleById(Long id);

    ScheduleDTO createSchedule(ScheduleDTO scheduleDTO);

    ScheduleDTO updateSchedule(ScheduleDTO scheduleDTO);

    void deleteSchedule(Long id);
}
