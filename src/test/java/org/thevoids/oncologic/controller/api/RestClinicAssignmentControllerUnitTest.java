package org.thevoids.oncologic.controller.api;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.thevoids.oncologic.dto.entity.ClinicAssignmentDTO;
import org.thevoids.oncologic.entity.ClinicAssignment;
import org.thevoids.oncologic.mapper.ClinicAssignmentMapper;
import org.thevoids.oncologic.service.ClinicAssigmentService;

class RestClinicAssignmentControllerUnitTest {

    @InjectMocks
    private RestClinicAssignmentController controller;

    @Mock
    private ClinicAssigmentService clinicAssigmentService;

    @Mock
    private ClinicAssignmentMapper clinicAssignmentMapper;

    private ClinicAssignment assignment;
    private ClinicAssignmentDTO assignmentDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        assignment = new ClinicAssignment();
        assignment.setId(1L);
        assignment.setStartTime(new Date());
        assignment.setEndTime(new Date());

        assignmentDTO = new ClinicAssignmentDTO();
        assignmentDTO.setId(1L);
        assignmentDTO.setStartTime(assignment.getStartTime());
        assignmentDTO.setEndTime(assignment.getEndTime());
        assignmentDTO.setUserId(2L);
        assignmentDTO.setClinicId(3L);
    }

    @Test
    void getAllClinicAssignments_Success() {
        when(clinicAssigmentService.getAllClinicAssignments()).thenReturn(Arrays.asList(assignment));
        when(clinicAssignmentMapper.toClinicAssignmentDTO(assignment)).thenReturn(assignmentDTO);

        ResponseEntity<List<ClinicAssignmentDTO>> response = controller.getAllClinicAssignments();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals(assignmentDTO.getId(), response.getBody().get(0).getId());
    }

    @Test
    void getAllClinicAssignments_InternalServerError() {
        when(clinicAssigmentService.getAllClinicAssignments()).thenThrow(new RuntimeException());
        ResponseEntity<List<ClinicAssignmentDTO>> response = controller.getAllClinicAssignments();
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void getClinicAssignmentById_Success() {
        when(clinicAssigmentService.getClinicAssignmentById(1L)).thenReturn(assignment);
        when(clinicAssignmentMapper.toClinicAssignmentDTO(assignment)).thenReturn(assignmentDTO);

        ResponseEntity<ClinicAssignmentDTO> response = controller.getClinicAssignmentById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(assignmentDTO, response.getBody());
    }

    @Test
    void getClinicAssignmentById_NotFound() {
        when(clinicAssigmentService.getClinicAssignmentById(1L)).thenReturn(null);

        ResponseEntity<ClinicAssignmentDTO> response = controller.getClinicAssignmentById(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void getClinicAssignmentById_InternalServerError() {
        when(clinicAssigmentService.getClinicAssignmentById(1L)).thenThrow(new RuntimeException());
        ResponseEntity<ClinicAssignmentDTO> response = controller.getClinicAssignmentById(1L);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void createClinicAssignment_Success() {
        when(clinicAssignmentMapper.toClinicAssignment(assignmentDTO)).thenReturn(assignment);
        when(clinicAssigmentService.assignClinic(assignment)).thenReturn(assignment);
        when(clinicAssignmentMapper.toClinicAssignmentDTO(assignment)).thenReturn(assignmentDTO);

        ResponseEntity<ClinicAssignmentDTO> response = controller.createClinicAssignment(assignmentDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(assignmentDTO, response.getBody());
        verify(clinicAssigmentService, times(1)).assignClinic(assignment);
    }

    @Test
    void createClinicAssignment_BadRequest() {
        when(clinicAssignmentMapper.toClinicAssignment(assignmentDTO)).thenReturn(assignment);
        doThrow(new IllegalArgumentException()).when(clinicAssigmentService).assignClinic(assignment);

        ResponseEntity<ClinicAssignmentDTO> response = controller.createClinicAssignment(assignmentDTO);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void createClinicAssignment_InternalServerError() {
        when(clinicAssignmentMapper.toClinicAssignment(assignmentDTO)).thenReturn(assignment);
        doThrow(new RuntimeException()).when(clinicAssigmentService).assignClinic(assignment);

        ResponseEntity<ClinicAssignmentDTO> response = controller.createClinicAssignment(assignmentDTO);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void updateClinicAssignment_Success() {
        when(clinicAssignmentMapper.toClinicAssignment(assignmentDTO)).thenReturn(assignment);
        when(clinicAssigmentService.updateClinicAssignment(assignment)).thenReturn(assignment);
        when(clinicAssignmentMapper.toClinicAssignmentDTO(assignment)).thenReturn(assignmentDTO);

        ResponseEntity<ClinicAssignmentDTO> response = controller.updateClinicAssignment(1L, assignmentDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(assignmentDTO, response.getBody());
    }

    @Test
    void updateClinicAssignment_BadRequest() {
        when(clinicAssignmentMapper.toClinicAssignment(assignmentDTO)).thenReturn(assignment);
        when(clinicAssigmentService.updateClinicAssignment(assignment)).thenThrow(new IllegalArgumentException());

        ResponseEntity<ClinicAssignmentDTO> response = controller.updateClinicAssignment(1L, assignmentDTO);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void updateClinicAssignment_InternalServerError() {
        when(clinicAssignmentMapper.toClinicAssignment(assignmentDTO)).thenReturn(assignment);
        when(clinicAssigmentService.updateClinicAssignment(assignment)).thenThrow(new RuntimeException());
        ResponseEntity<ClinicAssignmentDTO> response = controller.updateClinicAssignment(1L, assignmentDTO);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void deleteClinicAssignment_Success() {
        ResponseEntity<Void> response = controller.deleteClinicAssignment(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(clinicAssigmentService, times(1)).deleteClinicAssigment(1L);
    }

    @Test
    void deleteClinicAssignment_NotFound() {
        doThrow(new IllegalArgumentException()).when(clinicAssigmentService).deleteClinicAssigment(1L);

        ResponseEntity<Void> response = controller.deleteClinicAssignment(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void deleteClinicAssignment_InternalServerError() {
        doThrow(new RuntimeException()).when(clinicAssigmentService).deleteClinicAssigment(1L);
        ResponseEntity<Void> response = controller.deleteClinicAssignment(1L);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
}
