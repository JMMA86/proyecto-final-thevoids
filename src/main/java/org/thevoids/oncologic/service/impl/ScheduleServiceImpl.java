package org.thevoids.oncologic.service.impl;

import org.springframework.stereotype.Service;
import org.thevoids.oncologic.dto.ScheduleDTO;
import org.thevoids.oncologic.entity.Schedule;
import org.thevoids.oncologic.exception.ResourceNotFoundException;
import org.thevoids.oncologic.exception.InvalidOperationException;
import org.thevoids.oncologic.mapper.ScheduleMapper;
import org.thevoids.oncologic.repository.ScheduleRepository;
import org.thevoids.oncologic.service.ScheduleService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ScheduleServiceImpl implements ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final ScheduleMapper scheduleMapper;

    public ScheduleServiceImpl(ScheduleRepository scheduleRepository, ScheduleMapper scheduleMapper) {
        this.scheduleRepository = scheduleRepository;
        this.scheduleMapper = scheduleMapper;
    }

    @Override
    public List<ScheduleDTO> getAllSchedules() {
        List<Schedule> schedules = scheduleRepository.findAll();
        return schedules.stream()
                .map(scheduleMapper::toScheduleDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ScheduleDTO getScheduleById(Long id) {
        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Schedule", "id", id));
        return scheduleMapper.toScheduleDTO(schedule);
    }

    @Override
    public ScheduleDTO createSchedule(ScheduleDTO scheduleDTO) {
        if (scheduleDTO == null) {
            throw new InvalidOperationException("Schedule cannot be null");
        }

        Schedule schedule = scheduleMapper.toSchedule(scheduleDTO);
        Schedule savedSchedule = scheduleRepository.save(schedule);
        return scheduleMapper.toScheduleDTO(savedSchedule);
    }

    @Override
    public ScheduleDTO updateSchedule(ScheduleDTO scheduleDTO) {
        if (scheduleDTO == null) {
            throw new InvalidOperationException("Schedule cannot be null");
        }

        if (scheduleDTO.getScheduleId() == null) {
            throw new InvalidOperationException("Schedule ID cannot be null");
        }

        if (!scheduleRepository.existsById(scheduleDTO.getScheduleId())) {
            throw new ResourceNotFoundException("Schedule", "id", scheduleDTO.getScheduleId());
        }

        Schedule schedule = scheduleMapper.toSchedule(scheduleDTO);
        Schedule updatedSchedule = scheduleRepository.save(schedule);
        return scheduleMapper.toScheduleDTO(updatedSchedule);
    }

    @Override
    public void deleteSchedule(Long id) {
        if (!scheduleRepository.existsById(id)) {
            throw new ResourceNotFoundException("Schedule", "id", id);
        }

        scheduleRepository.deleteById(id);
    }
}
