package org.thevoids.oncologic.controller.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.thevoids.oncologic.dto.LabDTO;
import org.thevoids.oncologic.service.LabService;

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
        ResponseEntity<List<LabDTO>> response = labController.getAllLabs();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<LabDTO> labs = response.getBody();
        assertNotNull(labs);
        assertEquals(2, labs.size());
        assertEquals("Blood Test", labs.get(0).getTestType());
        assertEquals("X-Ray", labs.get(1).getTestType());
    }

    @Test
    void testGetAllLabs_Failure() {
        // Arrange
        when(labService.getAllLabs()).thenThrow(new RuntimeException("Database error"));

        // Act
        ResponseEntity<List<LabDTO>> response = labController.getAllLabs();

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
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
        when(labService.getLabById(1L)).thenThrow(new IllegalArgumentException("Lab not found"));

        // Act
        ResponseEntity<?> response = labController.getLabById(1L);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        Map<String, String> errorResponse = (Map<String, String>) response.getBody();
        assertNotNull(errorResponse);
        assertEquals("Lab not found", errorResponse.get("error"));
    }

    @Test
    void testAssignLab_Success() {
        // Arrange
        when(labService.assignLab(1L, 1L, testDate)).thenReturn(testLab1);

        // Act
        ResponseEntity<?> response = labController.assignLab(1L, 1L, testDate);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        LabDTO lab = (LabDTO) response.getBody();
        assertNotNull(lab);
        assertEquals(1L, lab.getLabId());
        assertEquals("Blood Test", lab.getTestType());
    }

    @Test
    void testAssignLab_BadRequest() {
        // Arrange
        when(labService.assignLab(1L, 1L, testDate)).thenThrow(new IllegalArgumentException("Invalid data"));

        // Act
        ResponseEntity<?> response = labController.assignLab(1L, 1L, testDate);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Map<String, String> errorResponse = (Map<String, String>) response.getBody();
        assertNotNull(errorResponse);
        assertEquals("Invalid data", errorResponse.get("error"));
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
        when(labService.updateLab(any(LabDTO.class))).thenThrow(new IllegalArgumentException("Invalid data"));

        // Act
        ResponseEntity<?> response = labController.updateLab(1L, testLab1);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Map<String, String> errorResponse = (Map<String, String>) response.getBody();
        assertNotNull(errorResponse);
        assertEquals("Invalid data", errorResponse.get("error"));
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
        doThrow(new IllegalArgumentException("Lab not found")).when(labService).deleteLab(anyLong());

        // Act
        ResponseEntity<?> response = labController.deleteLab(1L);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        Map<String, String> errorResponse = (Map<String, String>) response.getBody();
        assertNotNull(errorResponse);
        assertEquals("Lab not found", errorResponse.get("error"));
    }
}