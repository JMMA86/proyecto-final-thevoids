package org.thevoids.oncologic.controller.api;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.thevoids.oncologic.dto.ClinicAssignmentDTO;
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
    }

    @Test
    void getAllClinicAssignments_Success() {
        when(clinicAssigmentService.getAllClinicAssignments()).thenReturn(Arrays.asList(assignment));
        when(clinicAssignmentMapper.toClinicAssignmentDTO(assignment)).thenReturn(assignmentDTO);

        List<ClinicAssignmentDTO> result = controller.getAllClinicAssignments();

        assertEquals(1, result.size());
        assertEquals(assignmentDTO.getId(), result.get(0).getId());
    }

    @Test
    void getClinicAssignmentById_Success() {
        when(clinicAssigmentService.getClinicAssignmentById(1L)).thenReturn(assignment);
        when(clinicAssignmentMapper.toClinicAssignmentDTO(assignment)).thenReturn(assignmentDTO);

        ResponseEntity<ClinicAssignmentDTO> response = controller.getClinicAssignmentById(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(assignmentDTO, response.getBody());
    }

    @Test
    void getClinicAssignmentById_NotFound() {
        when(clinicAssigmentService.getClinicAssignmentById(1L)).thenReturn(null);

        ResponseEntity<ClinicAssignmentDTO> response = controller.getClinicAssignmentById(1L);

        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void createClinicAssignment_Success() {
        when(clinicAssignmentMapper.toClinicAssignment(assignmentDTO)).thenReturn(assignment);
        when(clinicAssigmentService.updateClinicAssignment(assignment)).thenReturn(assignment);
        when(clinicAssignmentMapper.toClinicAssignmentDTO(assignment)).thenReturn(assignmentDTO);

        ResponseEntity<ClinicAssignmentDTO> response = controller.createClinicAssignment(assignmentDTO);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(assignmentDTO, response.getBody());
    }

    @Test
    void createClinicAssignment_BadRequest() {
        when(clinicAssignmentMapper.toClinicAssignment(assignmentDTO)).thenReturn(assignment);
        when(clinicAssigmentService.updateClinicAssignment(assignment)).thenThrow(new IllegalArgumentException());

        ResponseEntity<ClinicAssignmentDTO> response = controller.createClinicAssignment(assignmentDTO);

        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    void updateClinicAssignment_Success() {
        when(clinicAssignmentMapper.toClinicAssignment(assignmentDTO)).thenReturn(assignment);
        when(clinicAssigmentService.updateClinicAssignment(assignment)).thenReturn(assignment);
        when(clinicAssignmentMapper.toClinicAssignmentDTO(assignment)).thenReturn(assignmentDTO);

        ResponseEntity<ClinicAssignmentDTO> response = controller.updateClinicAssignment(1L, assignmentDTO);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(assignmentDTO, response.getBody());
    }

    @Test
    void updateClinicAssignment_BadRequest() {
        when(clinicAssignmentMapper.toClinicAssignment(assignmentDTO)).thenReturn(assignment);
        when(clinicAssigmentService.updateClinicAssignment(assignment)).thenThrow(new IllegalArgumentException());

        ResponseEntity<ClinicAssignmentDTO> response = controller.updateClinicAssignment(1L, assignmentDTO);

        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    void deleteClinicAssignment_Success() {
        ResponseEntity<Void> response = controller.deleteClinicAssignment(1L);
        assertEquals(200, response.getStatusCodeValue());
        verify(clinicAssigmentService, times(1)).deleteClinicAssigment(1L);
    }

    @Test
    void deleteClinicAssignment_NotFound() {
        doThrow(new IllegalArgumentException()).when(clinicAssigmentService).deleteClinicAssigment(1L);

        ResponseEntity<Void> response = controller.deleteClinicAssignment(1L);

        assertEquals(404, response.getStatusCodeValue());
    }
}
