package org.thevoids.oncologic.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.thevoids.oncologic.entity.Schedule;
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

    @InjectMocks
    private ScheduleServiceImpl scheduleService;

    @Test
    void getAllSchedulesReturnsAllSchedules() {
        List<Schedule> expectedSchedules = List.of(
                createSchedule(1L),
                createSchedule(2L)
        );

        when(scheduleRepository.findAll()).thenReturn(expectedSchedules);

        List<Schedule> result = scheduleService.getAllSchedules();

        assertEquals(2, result.size());
        assertEquals(expectedSchedules, result);
    }

    @Test
    void getScheduleByIdReturnsScheduleWhenExists() {
        Long id = 1L;
        Schedule expectedSchedule = createSchedule(id);

        when(scheduleRepository.findById(id)).thenReturn(Optional.of(expectedSchedule));

        Schedule result = scheduleService.getScheduleById(id);

        assertNotNull(result);
        assertEquals(id, result.getScheduleId());
    }

    @Test
    void getScheduleByIdThrowsExceptionWhenNotExists() {
        Long id = 1L;

        when(scheduleRepository.findById(id)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                scheduleService.getScheduleById(id));

        assertEquals("Schedule with id 1 does not exist", exception.getMessage());
    }

    @Test
    void createScheduleSuccessfullyCreatesSchedule() {
        Schedule schedule = createSchedule(null);
        Schedule savedSchedule = createSchedule(1L);

        when(scheduleRepository.save(schedule)).thenReturn(savedSchedule);

        Schedule result = scheduleService.createSchedule(schedule);

        assertNotNull(result);
        assertEquals(savedSchedule, result);
        verify(scheduleRepository).save(schedule);
    }

    @Test
    void createScheduleThrowsExceptionWhenScheduleIsNull() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                scheduleService.createSchedule(null));

        assertEquals("Schedule cannot be null", exception.getMessage());
        verify(scheduleRepository, never()).save(any());
    }

    @Test
    void updateScheduleSuccessfullyUpdatesSchedule() {
        Long id = 1L;
        Schedule schedule = createSchedule(id);

        when(scheduleRepository.existsById(id)).thenReturn(true);
        when(scheduleRepository.save(schedule)).thenReturn(schedule);

        Schedule result = scheduleService.updateSchedule(schedule);

        assertNotNull(result);
        assertEquals(schedule, result);
        verify(scheduleRepository).save(schedule);
    }

    @Test
    void updateScheduleThrowsExceptionWhenScheduleIsNull() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                scheduleService.updateSchedule(null));

        assertEquals("Schedule cannot be null", exception.getMessage());
        verify(scheduleRepository, never()).save(any());
    }

    @Test
    void updateScheduleThrowsExceptionWhenIdIsNull() {
        Schedule schedule = createSchedule(null);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                scheduleService.updateSchedule(schedule));

        assertEquals("Schedule ID cannot be null", exception.getMessage());
        verify(scheduleRepository, never()).save(any());
    }

    @Test
    void updateScheduleThrowsExceptionWhenScheduleDoesNotExist() {
        Long id = 1L;
        Schedule schedule = createSchedule(id);

        when(scheduleRepository.existsById(id)).thenReturn(false);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                scheduleService.updateSchedule(schedule));

        assertEquals("Schedule with id 1 does not exist", exception.getMessage());
        verify(scheduleRepository, never()).save(any());
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

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                scheduleService.deleteSchedule(id));

        assertEquals("Schedule with id 1 does not exist", exception.getMessage());
        verify(scheduleRepository, never()).deleteById(any());
    }

    private Schedule createSchedule(Long id) {
        Schedule schedule = new Schedule();
        schedule.setScheduleId(id);
        return schedule;
    }
}