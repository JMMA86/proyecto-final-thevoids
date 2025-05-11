package org.thevoids.oncologic.controller.api;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
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

        ResponseEntity<List<AppointmentDTO>> response = controller.getAllAppointments();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals(appointmentDTO.getAppointmentId(), response.getBody().get(0).getAppointmentId());
    }

    @Test
    void getAllAppointments_InternalServerError() {
        when(appointmentService.getAllAppointments()).thenThrow(new RuntimeException("DB error"));
        ResponseEntity<List<AppointmentDTO>> response = controller.getAllAppointments();
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void getAppointmentById_Success() {
        when(appointmentService.getAppointmentById(1L)).thenReturn(appointment);
        when(appointmentMapper.toAppointmentDTO(appointment)).thenReturn(appointmentDTO);

        ResponseEntity<AppointmentDTO> response = controller.getAppointmentById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(appointmentDTO, response.getBody());
    }

    @Test
    void getAppointmentById_NotFound() {
        when(appointmentService.getAppointmentById(1L)).thenThrow(new IllegalArgumentException());
        ResponseEntity<AppointmentDTO> response = controller.getAppointmentById(1L);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void getAppointmentById_InternalServerError() {
        when(appointmentService.getAppointmentById(1L)).thenThrow(new RuntimeException());
        ResponseEntity<AppointmentDTO> response = controller.getAppointmentById(1L);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void createAppointment_Success() {
        when(appointmentService.createAppointment(any(), any(), any(), any())).thenReturn(appointment);
        when(appointmentMapper.toAppointmentDTO(appointment)).thenReturn(appointmentDTO);

        ResponseEntity<AppointmentDTO> response = controller.createAppointment(appointmentDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(appointmentDTO, response.getBody());
    }

    @Test
    void createAppointment_BadRequest() {
        when(appointmentService.createAppointment(any(), any(), any(), any()))
                .thenThrow(new IllegalArgumentException());

        ResponseEntity<AppointmentDTO> response = controller.createAppointment(appointmentDTO);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void createAppointment_InternalServerError() {
        when(appointmentService.createAppointment(any(), any(), any(), any()))
                .thenThrow(new RuntimeException());
        ResponseEntity<AppointmentDTO> response = controller.createAppointment(appointmentDTO);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void updateAppointment_Success() {
        when(appointmentMapper.toAppointment(appointmentDTO)).thenReturn(appointment);
        when(appointmentService.updateAppointment(any())).thenReturn(appointment);
        when(appointmentMapper.toAppointmentDTO(appointment)).thenReturn(appointmentDTO);

        ResponseEntity<AppointmentDTO> response = controller.updateAppointment(1L, appointmentDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(appointmentDTO, response.getBody());
    }

    @Test
    void updateAppointment_BadRequest() {
        when(appointmentMapper.toAppointment(appointmentDTO)).thenReturn(appointment);
        when(appointmentService.updateAppointment(any())).thenThrow(new IllegalArgumentException());

        ResponseEntity<AppointmentDTO> response = controller.updateAppointment(1L, appointmentDTO);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void updateAppointment_InternalServerError() {
        when(appointmentMapper.toAppointment(appointmentDTO)).thenReturn(appointment);
        when(appointmentService.updateAppointment(any())).thenThrow(new RuntimeException());
        ResponseEntity<AppointmentDTO> response = controller.updateAppointment(1L, appointmentDTO);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void deleteAppointment_Success() {
        ResponseEntity<Void> response = controller.deleteAppointment(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(appointmentService, times(1)).deleteAppointment(1L);
    }

    @Test
    void deleteAppointment_NotFound() {
        doThrow(new IllegalArgumentException()).when(appointmentService).deleteAppointment(1L);

        ResponseEntity<Void> response = controller.deleteAppointment(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void deleteAppointment_InternalServerError() {
        doThrow(new RuntimeException()).when(appointmentService).deleteAppointment(1L);
        ResponseEntity<Void> response = controller.deleteAppointment(1L);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
}
