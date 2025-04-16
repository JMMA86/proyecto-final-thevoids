package org.thevoids.oncologic.service.impl;

import org.springframework.stereotype.Service;
import org.thevoids.oncologic.entity.Schedule;
import org.thevoids.oncologic.repository.ScheduleRepository;
import org.thevoids.oncologic.service.ScheduleService;

import java.util.List;

@Service
public class ScheduleServiceImpl implements ScheduleService {
    private final ScheduleRepository scheduleRepository;

    public ScheduleServiceImpl(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    @Override
    public List<Schedule> getAllSchedules() {
        return scheduleRepository.findAll();
    }

    @Override
    public Schedule getScheduleById(Long id) {
        return scheduleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Schedule with id " + id + " does not exist"));
    }

    @Override
    public Schedule createSchedule(Schedule schedule) {
        if (schedule == null) {
            throw new IllegalArgumentException("Schedule cannot be null");
        }

        return scheduleRepository.save(schedule);
    }

    @Override
    public Schedule updateSchedule(Schedule schedule) {
        if (schedule == null) {
            throw new IllegalArgumentException("Schedule cannot be null");
        }

        if (schedule.getScheduleId() == null) {
            throw new IllegalArgumentException("Schedule ID cannot be null");
        }

        if (!scheduleRepository.existsById(schedule.getScheduleId())) {
            throw new IllegalArgumentException("Schedule with id " + schedule.getScheduleId() + " does not exist");
        }

        return scheduleRepository.save(schedule);
    }

    @Override
    public void deleteSchedule(Long id) {
        if (!scheduleRepository.existsById(id)) {
            throw new IllegalArgumentException("Schedule with id " + id + " does not exist");
        }

        scheduleRepository.deleteById(id);
    }
}