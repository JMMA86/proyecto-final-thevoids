package org.thevoids.oncologic.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.thevoids.oncologic.dto.entity.ScheduleDTO;
import org.thevoids.oncologic.entity.Schedule;
import org.thevoids.oncologic.mapper.ScheduleMapper;
import org.thevoids.oncologic.repository.ScheduleRepository;
import org.thevoids.oncologic.service.impl.ScheduleServiceImpl;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ScheduleServiceUnitTest {

    @Mock
    private ScheduleRepository scheduleRepository;

    @Mock
    private ScheduleMapper scheduleMapper;

    @InjectMocks
    private ScheduleServiceImpl scheduleService;

    @Test
    void getAllSchedulesReturnsAllSchedules() {
        List<Schedule> schedules = List.of(
                createSchedule(1L),
                createSchedule(2L));

        List<ScheduleDTO> expectedScheduleDTOs = List.of(
                createScheduleDTO(1L),
                createScheduleDTO(2L));

        when(scheduleRepository.findAll()).thenReturn(schedules);
        when(scheduleMapper.toScheduleDTO(schedules.get(0))).thenReturn(expectedScheduleDTOs.get(0));
        when(scheduleMapper.toScheduleDTO(schedules.get(1))).thenReturn(expectedScheduleDTOs.get(1));

        List<ScheduleDTO> result = scheduleService.getAllSchedules();

        assertEquals(2, result.size());
        assertEquals(expectedScheduleDTOs, result);
        verify(scheduleRepository).findAll();
        verify(scheduleMapper).toScheduleDTO(schedules.get(0));
        verify(scheduleMapper).toScheduleDTO(schedules.get(1));
    }

    @Test
    void getScheduleByIdReturnsScheduleWhenExists() {
        Long id = 1L;
        Schedule schedule = createSchedule(id);

        ScheduleDTO expectedScheduleDTO = createScheduleDTO(id);

        when(scheduleRepository.findById(id)).thenReturn(Optional.of(schedule));
        when(scheduleMapper.toScheduleDTO(schedule)).thenReturn(expectedScheduleDTO);

        ScheduleDTO result = scheduleService.getScheduleById(id);

        assertNotNull(result);
        assertEquals(id, result.getScheduleId());
        verify(scheduleMapper).toScheduleDTO(schedule);
    }

    @Test
    void getScheduleByIdThrowsExceptionWhenNotExists() {
        Long id = 1L;

        when(scheduleRepository.findById(id)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> scheduleService.getScheduleById(id));

        assertEquals("Schedule with id 1 does not exist", exception.getMessage());
    }

    @Test
    void createScheduleSuccessfullyCreatesSchedule() {
        ScheduleDTO scheduleDTO = createScheduleDTO(null);

        Schedule schedule = createSchedule(null);
        Schedule savedSchedule = createSchedule(1L);

        ScheduleDTO savedScheduleDTO = createScheduleDTO(1L);

        when(scheduleMapper.toSchedule(scheduleDTO)).thenReturn(schedule);
        when(scheduleRepository.save(schedule)).thenReturn(savedSchedule);
        when(scheduleMapper.toScheduleDTO(savedSchedule)).thenReturn(savedScheduleDTO);

        ScheduleDTO result = scheduleService.createSchedule(scheduleDTO);

        assertNotNull(result);
        assertEquals(1L, result.getScheduleId());
        verify(scheduleMapper).toSchedule(scheduleDTO);
        verify(scheduleRepository).save(schedule);
        verify(scheduleMapper).toScheduleDTO(savedSchedule);
    }

    @Test
    void createScheduleThrowsExceptionWhenScheduleIsNull() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> scheduleService.createSchedule(null));

        assertEquals("Schedule cannot be null", exception.getMessage());
        verify(scheduleRepository, never()).save(any());
    }

    @Test
    void updateScheduleSuccessfullyUpdatesSchedule() {
        Long id = 1L;
        ScheduleDTO scheduleDTO = createScheduleDTO(id);

        Schedule schedule = createSchedule(id);
        Schedule updatedSchedule = createSchedule(id);

        ScheduleDTO updatedScheduleDTO = createScheduleDTO(id);

        when(scheduleRepository.existsById(id)).thenReturn(true);
        when(scheduleMapper.toSchedule(scheduleDTO)).thenReturn(schedule);
        when(scheduleRepository.save(schedule)).thenReturn(updatedSchedule);
        when(scheduleMapper.toScheduleDTO(updatedSchedule)).thenReturn(updatedScheduleDTO);

        ScheduleDTO result = scheduleService.updateSchedule(scheduleDTO);

        assertNotNull(result);
        assertEquals(id, result.getScheduleId());
        verify(scheduleMapper).toSchedule(scheduleDTO);
        verify(scheduleRepository).save(schedule);
        verify(scheduleMapper).toScheduleDTO(updatedSchedule);
    }

    @Test
    void updateScheduleThrowsExceptionWhenScheduleIsNull() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> scheduleService.updateSchedule(null));

        assertEquals("Schedule cannot be null", exception.getMessage());
        verify(scheduleRepository, never()).save(any());
    }

    @Test
    void updateScheduleThrowsExceptionWhenIdIsNull() {
        ScheduleDTO scheduleDTO = createScheduleDTO(null);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> scheduleService.updateSchedule(scheduleDTO));

        assertEquals("Schedule ID cannot be null", exception.getMessage());
        verify(scheduleRepository, never()).save(any());
        verify(scheduleMapper, never()).toSchedule(any(ScheduleDTO.class));
    }

    @Test
    void updateScheduleThrowsExceptionWhenScheduleDoesNotExist() {
        Long id = 1L;
        ScheduleDTO scheduleDTO = createScheduleDTO(id);

        when(scheduleRepository.existsById(id)).thenReturn(false);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> scheduleService.updateSchedule(scheduleDTO));

        assertEquals("Schedule with id 1 does not exist", exception.getMessage());
        verify(scheduleRepository, never()).save(any());
        verify(scheduleMapper, never()).toSchedule(any(ScheduleDTO.class));
    }

    @Test
    void deleteScheduleSuccessfullyDeletesSchedule() {
        Long id = 1L;

        when(scheduleRepository.existsById(id)).thenReturn(true);

        scheduleService.deleteSchedule(id);

        verify(scheduleRepository).deleteById(id);
    }

    @Test
    void deleteScheduleThrowsExceptionWhenScheduleDoesNotExist() {
        Long id = 1L;

        when(scheduleRepository.existsById(id)).thenReturn(false);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> scheduleService.deleteSchedule(id));

        assertEquals("Schedule with id 1 does not exist", exception.getMessage());
        verify(scheduleRepository, never()).deleteById(any());
    }

    private Schedule createSchedule(Long id) {
        Schedule schedule = new Schedule();
        schedule.setScheduleId(id);
        return schedule;
    }

    private ScheduleDTO createScheduleDTO(Long id) {
        ScheduleDTO scheduleDTO = new ScheduleDTO();
        scheduleDTO.setScheduleId(id);
        return scheduleDTO;
    }
}
