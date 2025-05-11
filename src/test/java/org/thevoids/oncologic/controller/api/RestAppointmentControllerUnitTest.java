package org.thevoids.oncologic.controller.api;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.util.ReflectionTestUtils.setField;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.thevoids.oncologic.dto.AppointmentDTO;
import org.thevoids.oncologic.entity.Appointment;
import org.thevoids.oncologic.mapper.AppointmentMapper;
import org.thevoids.oncologic.service.AppointmentService;

class RestAppointmentControllerUnitTest {

    @InjectMocks
    private RestAppointmentController controller;

    @Mock
    private AppointmentService appointmentService;

    @Mock
    private AppointmentMapper appointmentMapper;

    private Appointment appointment;
    private AppointmentDTO appointmentDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        appointment = new Appointment();
        appointment.setAppointmentId(1L);
        appointment.setDateTime(new Date());
        appointment.setStatus("pending");

        appointmentDTO = new AppointmentDTO();
        appointmentDTO.setAppointmentId(1L);
        appointmentDTO.setDateTime(appointment.getDateTime());
        appointmentDTO.setStatus("pending");
    }

    @Test
    void getAllAppointments_Success() {
        when(appointmentService.getAllAppointments()).thenReturn(Arrays.asList(appointment));
        when(appointmentMapper.toAppointmentDTO(appointment)).thenReturn(appointmentDTO);

        List<AppointmentDTO> result = controller.getAllAppointments();

        assertEquals(1, result.size());
        assertEquals(appointmentDTO.getAppointmentId(), result.get(0).getAppointmentId());
    }

    @Test
    void getAppointmentById_Success() {
        when(appointmentService.getAppointmentById(1L)).thenReturn(appointment);
        when(appointmentMapper.toAppointmentDTO(appointment)).thenReturn(appointmentDTO);

        ResponseEntity<AppointmentDTO> response = controller.getAppointmentById(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(appointmentDTO, response.getBody());
    }

    @Test
    void getAppointmentById_NotFound() {
        when(appointmentService.getAppointmentById(1L)).thenThrow(new IllegalArgumentException());

        ResponseEntity<AppointmentDTO> response = controller.getAppointmentById(1L);

        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void createAppointment_Success() {
        when(appointmentService.createAppointment(any(), any(), any(), any())).thenReturn(appointment);
        when(appointmentMapper.toAppointmentDTO(appointment)).thenReturn(appointmentDTO);

        ResponseEntity<AppointmentDTO> response = controller.createAppointment(appointmentDTO);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(appointmentDTO, response.getBody());
    }

    @Test
    void createAppointment_BadRequest() {
        when(appointmentService.createAppointment(any(), any(), any(), any()))
                .thenThrow(new IllegalArgumentException());

        ResponseEntity<AppointmentDTO> response = controller.createAppointment(appointmentDTO);

        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    void updateAppointment_Success() {
        when(appointmentMapper.toAppointment(appointmentDTO)).thenReturn(appointment);
        when(appointmentService.updateAppointment(any())).thenReturn(appointment);
        when(appointmentMapper.toAppointmentDTO(appointment)).thenReturn(appointmentDTO);

        ResponseEntity<AppointmentDTO> response = controller.updateAppointment(1L, appointmentDTO);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(appointmentDTO, response.getBody());
    }

    @Test
    void updateAppointment_BadRequest() {
        when(appointmentMapper.toAppointment(appointmentDTO)).thenReturn(appointment);
        when(appointmentService.updateAppointment(any())).thenThrow(new IllegalArgumentException());

        ResponseEntity<AppointmentDTO> response = controller.updateAppointment(1L, appointmentDTO);

        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    void deleteAppointment_Success() {
        ResponseEntity<Void> response = controller.deleteAppointment(1L);
        assertEquals(200, response.getStatusCodeValue());
        verify(appointmentService, times(1)).deleteAppointment(1L);
    }

    @Test
    void deleteAppointment_NotFound() {
        doThrow(new IllegalArgumentException()).when(appointmentService).deleteAppointment(1L);

        ResponseEntity<Void> response = controller.deleteAppointment(1L);

        assertEquals(404, response.getStatusCodeValue());
    }
}
