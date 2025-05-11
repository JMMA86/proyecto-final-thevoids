package org.thevoids.oncologic.controller.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.thevoids.oncologic.dto.MedicalHistoryDTO;
import org.thevoids.oncologic.service.MedicalHistoryService;
import org.thevoids.oncologic.exception.ResourceNotFoundException;
import org.thevoids.oncologic.exception.InvalidOperationException;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RestMedicalHistoryControllerUnitTest {

    @InjectMocks
    private RestMedicalHistoryController medicalHistoryController;

    @Mock
    private MedicalHistoryService medicalHistoryService;

    private MedicalHistoryDTO testMedicalHistory1;
    private MedicalHistoryDTO testMedicalHistory2;
    private Timestamp testTimestamp;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Initialize test timestamp
        testTimestamp = new Timestamp(System.currentTimeMillis());

        // Initialize test medical histories
        testMedicalHistory1 = new MedicalHistoryDTO();
        testMedicalHistory1.setHistoryId(1L);
        testMedicalHistory1.setPatientId(1L);
        testMedicalHistory1.setDiagnosis("Lung Cancer");
        testMedicalHistory1.setTreatment("Chemotherapy");
        testMedicalHistory1.setMedications("Paclitaxel, Carboplatin");
        testMedicalHistory1.setRecordDate(testTimestamp);

        testMedicalHistory2 = new MedicalHistoryDTO();
        testMedicalHistory2.setHistoryId(2L);
        testMedicalHistory2.setPatientId(2L);
        testMedicalHistory2.setDiagnosis("Breast Cancer");
        testMedicalHistory2.setTreatment("Surgery");
        testMedicalHistory2.setMedications("Tamoxifen");
        testMedicalHistory2.setRecordDate(testTimestamp);
    }

    @Test
    void testGetAllMedicalHistories_Success() {
        // Arrange
        when(medicalHistoryService.getAllMedicalHistories())
                .thenReturn(Arrays.asList(testMedicalHistory1, testMedicalHistory2));
        // Act
        ResponseEntity<?> response = medicalHistoryController.getAllMedicalHistories();
        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof List);
        List<?> medicalHistories = (List<?>) response.getBody();
        assertEquals(2, medicalHistories.size());
        assertTrue(medicalHistories.get(0) instanceof MedicalHistoryDTO);
        assertEquals("Lung Cancer", ((MedicalHistoryDTO) medicalHistories.get(0)).getDiagnosis());
        assertEquals("Breast Cancer", ((MedicalHistoryDTO) medicalHistories.get(1)).getDiagnosis());
    }

    @Test
    void testGetAllMedicalHistories_Failure() {
        // Arrange
        when(medicalHistoryService.getAllMedicalHistories()).thenThrow(new RuntimeException("Database error"));
        // Act
        ResponseEntity<?> response = medicalHistoryController.getAllMedicalHistories();
        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        @SuppressWarnings("unchecked")
        Map<String, String> errorResponse = (Map<String, String>) response.getBody();
        assertNotNull(errorResponse);
        assertEquals("Database error", errorResponse.get("error"));
    }

    @Test
    void testGetMedicalHistoryById_Success() {
        // Arrange
        when(medicalHistoryService.getMedicalHistoryById(1L)).thenReturn(testMedicalHistory1);
        // Act
        ResponseEntity<?> response = medicalHistoryController.getMedicalHistoryById(1L);
        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        MedicalHistoryDTO medicalHistory = (MedicalHistoryDTO) response.getBody();
        assertNotNull(medicalHistory);
        assertEquals(1L, medicalHistory.getHistoryId());
        assertEquals("Lung Cancer", medicalHistory.getDiagnosis());
    }

    @Test
    void testGetMedicalHistoryById_NotFound() {
        // Arrange
        when(medicalHistoryService.getMedicalHistoryById(1L))
                .thenThrow(new ResourceNotFoundException("MedicalHistory", "id", 1L));
        // Act
        ResponseEntity<?> response = medicalHistoryController.getMedicalHistoryById(1L);
        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        @SuppressWarnings("unchecked")
        Map<String, String> errorResponse = (Map<String, String>) response.getBody();
        assertNotNull(errorResponse);
        assertEquals("MedicalHistory no encontrado con id : '1'", errorResponse.get("error"));
    }

    @Test
    void testCreateMedicalHistory_Success() {
        // Arrange
        when(medicalHistoryService.createMedicalHistory(any(MedicalHistoryDTO.class))).thenReturn(testMedicalHistory1);
        // Act
        ResponseEntity<?> response = medicalHistoryController.createMedicalHistory(testMedicalHistory1);
        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        MedicalHistoryDTO medicalHistory = (MedicalHistoryDTO) response.getBody();
        assertNotNull(medicalHistory);
        assertEquals(1L, medicalHistory.getHistoryId());
        assertEquals("Lung Cancer", medicalHistory.getDiagnosis());
    }

    @Test
    void testCreateMedicalHistory_BadRequest() {
        // Arrange
        when(medicalHistoryService.createMedicalHistory(any(MedicalHistoryDTO.class)))
                .thenThrow(new InvalidOperationException("Invalid data"));
        // Act
        ResponseEntity<?> response = medicalHistoryController.createMedicalHistory(testMedicalHistory1);
        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        @SuppressWarnings("unchecked")
        Map<String, String> errorResponse = (Map<String, String>) response.getBody();
        assertNotNull(errorResponse);
        assertEquals("Invalid data", errorResponse.get("error"));
    }

    @Test
    void testUpdateMedicalHistory_Success() {
        // Arrange
        when(medicalHistoryService.updateMedicalHistory(any(MedicalHistoryDTO.class))).thenReturn(testMedicalHistory1);
        // Act
        ResponseEntity<?> response = medicalHistoryController.updateMedicalHistory(1L, testMedicalHistory1);
        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        MedicalHistoryDTO medicalHistory = (MedicalHistoryDTO) response.getBody();
        assertNotNull(medicalHistory);
        assertEquals(1L, medicalHistory.getHistoryId());
        assertEquals("Lung Cancer", medicalHistory.getDiagnosis());
        verify(medicalHistoryService, times(1)).updateMedicalHistory(any(MedicalHistoryDTO.class));
    }

    @Test
    void testUpdateMedicalHistory_BadRequest() {
        // Arrange
        when(medicalHistoryService.updateMedicalHistory(any(MedicalHistoryDTO.class)))
                .thenThrow(new InvalidOperationException("Invalid data"));
        // Act
        ResponseEntity<?> response = medicalHistoryController.updateMedicalHistory(1L, testMedicalHistory1);
        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        @SuppressWarnings("unchecked")
        Map<String, String> errorResponse = (Map<String, String>) response.getBody();
        assertNotNull(errorResponse);
        assertEquals("Invalid data", errorResponse.get("error"));
    }

    @Test
    void testDeleteMedicalHistory_Success() {
        // Act
        ResponseEntity<?> response = medicalHistoryController.deleteMedicalHistory(1L);
        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(medicalHistoryService, times(1)).deleteMedicalHistory(1L);
    }

    @Test
    void testDeleteMedicalHistory_NotFound() {
        // Arrange
        doThrow(new ResourceNotFoundException("MedicalHistory", "id", 1L)).when(medicalHistoryService)
                .deleteMedicalHistory(anyLong());
        // Act
        ResponseEntity<?> response = medicalHistoryController.deleteMedicalHistory(1L);
        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        @SuppressWarnings("unchecked")
        Map<String, String> errorResponse = (Map<String, String>) response.getBody();
        assertNotNull(errorResponse);
        assertEquals("MedicalHistory no encontrado con id : '1'", errorResponse.get("error"));
    }
}