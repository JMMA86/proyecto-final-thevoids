package org.thevoids.oncologic.controller.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.thevoids.oncologic.dto.entity.LabDTO;
import org.thevoids.oncologic.service.LabService;
import org.thevoids.oncologic.exception.ResourceNotFoundException;
import org.thevoids.oncologic.exception.InvalidOperationException;

class RestLabControllerUnitTest {

    @InjectMocks
    private RestLabController labController;

    @Mock
    private LabService labService;

    private LabDTO testLab1;
    private LabDTO testLab2;
    private Date testDate;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Initialize test date
        testDate = new Date();

        // Initialize test labs
        testLab1 = new LabDTO();
        testLab1.setLabId(1L);
        testLab1.setPatientId(1L);
        testLab1.setLabTechnicianId(1L);
        testLab1.setTestType("Blood Test");
        testLab1.setRequestDate(testDate);
        testLab1.setCompletionDate(null);
        testLab1.setResult(null);
        testLab1.setAttachment(null);

        testLab2 = new LabDTO();
        testLab2.setLabId(2L);
        testLab2.setPatientId(2L);
        testLab2.setLabTechnicianId(2L);
        testLab2.setTestType("X-Ray");
        testLab2.setRequestDate(testDate);
        testLab2.setCompletionDate(null);
        testLab2.setResult(null);
        testLab2.setAttachment(null);
    }

    @Test
    void testGetAllLabs_Success() {
        // Arrange
        when(labService.getAllLabs()).thenReturn(Arrays.asList(testLab1, testLab2));

        // Act
        ResponseEntity<?> response = labController.getAllLabs();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof List);
        List<?> labsRaw = (List<?>) response.getBody();
        assertEquals(2, labsRaw.size());
        assertTrue(labsRaw.get(0) instanceof LabDTO);
        assertEquals("Blood Test", ((LabDTO) labsRaw.get(0)).getTestType());
        assertEquals("X-Ray", ((LabDTO) labsRaw.get(1)).getTestType());
    }

    @Test
    void testGetAllLabs_Failure() {
        // Arrange
        when(labService.getAllLabs()).thenThrow(new RuntimeException("Database error"));

        // Act
        ResponseEntity<?> response = labController.getAllLabs();

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        // For INTERNAL_SERVER_ERROR, body is expected to be null (controller returns
        // .body(null))
        assertNull(response.getBody());
    }

    @Test
    void testGetLabById_Success() {
        // Arrange
        when(labService.getLabById(1L)).thenReturn(testLab1);

        // Act
        ResponseEntity<?> response = labController.getLabById(1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        LabDTO lab = (LabDTO) response.getBody();
        assertNotNull(lab);
        assertEquals(1L, lab.getLabId());
        assertEquals("Blood Test", lab.getTestType());
    }

    @Test
    void testGetLabById_NotFound() {
        // Arrange
        when(labService.getLabById(1L)).thenThrow(new ResourceNotFoundException("Lab", "id", 1L));

        // Act
        ResponseEntity<?> response = labController.getLabById(1L);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        // For NOT_FOUND, body is expected to be null (controller returns .body(null))
        assertNull(response.getBody());
    }

    @Test
    void testGetLabById_NullId() {
        // Act
        ResponseEntity<?> response = labController.getLabById(null);
        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testAssignLab_Success() {
        // Arrange
        when(labService.assignLab(1L, 1L, testDate)).thenReturn(testLab1);

        // Act
        LabDTO request = new LabDTO();
        request.setPatientId(1L);
        request.setLabTechnicianId(1L);
        request.setRequestDate(testDate);
        ResponseEntity<LabDTO> response = labController.assignLab(request);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        LabDTO lab = response.getBody();
        assertNotNull(lab);
        assertEquals(1L, lab.getLabId());
        assertEquals("Blood Test", lab.getTestType());
    }

    @Test
    void testAssignLab_BadRequest() {
        // Arrange
        when(labService.assignLab(1L, 1L, testDate)).thenThrow(new InvalidOperationException("Invalid data"));

        // Act
        LabDTO request = new LabDTO();
        request.setPatientId(1L);
        request.setLabTechnicianId(1L);
        request.setRequestDate(testDate);
        ResponseEntity<LabDTO> response = labController.assignLab(request);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        // For BAD_REQUEST, body is expected to be null (controller returns .body(null))
        assertNull(response.getBody());
    }

    @Test
    void testAssignLab_NullRequest() {
        // Act
        ResponseEntity<LabDTO> response = labController.assignLab(null);
        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testUpdateLab_Success() {
        // Arrange
        when(labService.updateLab(any(LabDTO.class))).thenReturn(testLab1);

        // Act
        ResponseEntity<?> response = labController.updateLab(1L, testLab1);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        LabDTO lab = (LabDTO) response.getBody();
        assertNotNull(lab);
        assertEquals(1L, lab.getLabId());
        assertEquals("Blood Test", lab.getTestType());
        verify(labService, times(1)).updateLab(any(LabDTO.class));
    }

    @Test
    void testUpdateLab_BadRequest() {
        // Arrange
        when(labService.updateLab(any(LabDTO.class))).thenThrow(new InvalidOperationException("Invalid data"));

        // Act
        ResponseEntity<?> response = labController.updateLab(1L, testLab1);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        // For BAD_REQUEST, body is expected to be null (controller returns .body(null))
        assertNull(response.getBody());
    }

    @Test
    void testUpdateLab_NullBody() {
        // Act
        ResponseEntity<?> response = labController.updateLab(1L, null);
        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testDeleteLab_Success() {
        // Act
        ResponseEntity<?> response = labController.deleteLab(1L);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(labService, times(1)).deleteLab(1L);
    }

    @Test
    void testDeleteLab_NotFound() {
        // Arrange
        doThrow(new ResourceNotFoundException("Lab", "id", 1L)).when(labService).deleteLab(anyLong());

        // Act
        ResponseEntity<?> response = labController.deleteLab(1L);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        // For NOT_FOUND, body is expected to be null (controller returns .build())
        assertNull(response.getBody());
    }

    @Test
    void testDeleteLab_NullId() {
        // Act
        ResponseEntity<?> response = labController.deleteLab(null);
        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testGetLabById_RuntimeException() {
        when(labService.getLabById(1L)).thenThrow(new RuntimeException("Unexpected error"));
        ResponseEntity<?> response = labController.getLabById(1L);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void testGetAllLabs_RuntimeException() {
        when(labService.getAllLabs()).thenThrow(new RuntimeException("Unexpected error"));
        ResponseEntity<?> response = labController.getAllLabs();
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void testAssignLab_RuntimeException() {
        when(labService.assignLab(anyLong(), anyLong(), any())).thenThrow(new RuntimeException("Unexpected error"));
        LabDTO request = new LabDTO();
        request.setPatientId(1L);
        request.setLabTechnicianId(1L);
        request.setRequestDate(new java.util.Date());
        ResponseEntity<LabDTO> response = labController.assignLab(request);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void testUpdateLab_RuntimeException() {
        when(labService.updateLab(any(LabDTO.class))).thenThrow(new RuntimeException("Unexpected error"));
        ResponseEntity<?> response = labController.updateLab(1L, testLab1);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void testDeleteLab_RuntimeException() {
        doThrow(new RuntimeException("Unexpected error")).when(labService).deleteLab(anyLong());
        ResponseEntity<?> response = labController.deleteLab(1L);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());
    }
}