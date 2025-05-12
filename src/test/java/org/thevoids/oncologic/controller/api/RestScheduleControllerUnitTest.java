package org.thevoids.oncologic.controller.api;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.thevoids.oncologic.dto.entity.ScheduleDTO;
import org.thevoids.oncologic.service.ScheduleService;
import org.thevoids.oncologic.exception.ResourceNotFoundException;
import org.thevoids.oncologic.exception.InvalidOperationException;

class RestScheduleControllerUnitTest {

    @InjectMocks
    private RestScheduleController scheduleController;

    @Mock
    private ScheduleService scheduleService;

    private ScheduleDTO testSchedule1;
    private ScheduleDTO testSchedule2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        testSchedule1 = new ScheduleDTO();
        testSchedule1.setScheduleId(1L);
        testSchedule2 = new ScheduleDTO();
        testSchedule2.setScheduleId(2L);
    }

    @Test
    void testGetAllSchedules_Success() {
        when(scheduleService.getAllSchedules()).thenReturn(Arrays.asList(testSchedule1, testSchedule2));
        ResponseEntity<List<ScheduleDTO>> response = scheduleController.getAllSchedules();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        List<ScheduleDTO> schedules = response.getBody();
        assertEquals(2, schedules.size());
        assertEquals(1L, schedules.get(0).getScheduleId());
        assertEquals(2L, schedules.get(1).getScheduleId());
    }

    @Test
    void testGetAllSchedules_Failure() {
        when(scheduleService.getAllSchedules()).thenThrow(new RuntimeException("Database error"));
        ResponseEntity<List<ScheduleDTO>> response = scheduleController.getAllSchedules();
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void testGetScheduleById_Success() {
        when(scheduleService.getScheduleById(1L)).thenReturn(testSchedule1);
        ResponseEntity<ScheduleDTO> response = scheduleController.getScheduleById(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        ScheduleDTO schedule = response.getBody();
        assertNotNull(schedule);
        assertEquals(1L, schedule.getScheduleId());
    }

    @Test
    void testGetScheduleById_NotFound() {
        when(scheduleService.getScheduleById(1L)).thenThrow(new ResourceNotFoundException("Schedule", "id", 1L));
        ResponseEntity<ScheduleDTO> response = scheduleController.getScheduleById(1L);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void testCreateSchedule_Success() {
        when(scheduleService.createSchedule(any(ScheduleDTO.class))).thenReturn(testSchedule1);
        ResponseEntity<ScheduleDTO> response = scheduleController.createSchedule(testSchedule1);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        ScheduleDTO schedule = response.getBody();
        assertNotNull(schedule);
        assertEquals(1L, schedule.getScheduleId());
    }

    @Test
    void testCreateSchedule_BadRequest() {
        when(scheduleService.createSchedule(any(ScheduleDTO.class)))
                .thenThrow(new InvalidOperationException("Invalid data"));
        ResponseEntity<ScheduleDTO> response = scheduleController.createSchedule(testSchedule1);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void testUpdateSchedule_Success() {
        when(scheduleService.updateSchedule(any(ScheduleDTO.class))).thenReturn(testSchedule1);
        ResponseEntity<ScheduleDTO> response = scheduleController.updateSchedule(1L, testSchedule1);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        ScheduleDTO schedule = response.getBody();
        assertNotNull(schedule);
        assertEquals(1L, schedule.getScheduleId());
        verify(scheduleService, times(1)).updateSchedule(any(ScheduleDTO.class));
    }

    @Test
    void testUpdateSchedule_BadRequest() {
        when(scheduleService.updateSchedule(any(ScheduleDTO.class)))
                .thenThrow(new InvalidOperationException("Invalid data"));
        ResponseEntity<ScheduleDTO> response = scheduleController.updateSchedule(1L, testSchedule1);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void testDeleteSchedule_Success() {
        ResponseEntity<Void> response = scheduleController.deleteSchedule(1L);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(scheduleService, times(1)).deleteSchedule(1L);
    }

    @Test
    void testDeleteSchedule_NotFound() {
        doThrow(new ResourceNotFoundException("Schedule", "id", 1L)).when(scheduleService).deleteSchedule(anyLong());
        ResponseEntity<Void> response = scheduleController.deleteSchedule(1L);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }
}
