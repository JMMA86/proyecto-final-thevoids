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
import org.thevoids.oncologic.dto.entity.PatientDTO;
import org.thevoids.oncologic.service.PatientService;
import org.thevoids.oncologic.exception.ResourceNotFoundException;
import org.thevoids.oncologic.exception.InvalidOperationException;

class RestPatientControllerUnitTest {

    @InjectMocks
    private RestPatientController patientController;

    @Mock
    private PatientService patientService;

    private PatientDTO testPatient1;
    private PatientDTO testPatient2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        testPatient1 = new PatientDTO();
        testPatient1.setPatientId(1L);
        testPatient1.setUserId(1L);
        testPatient1.setBloodGroup("A+");
        testPatient1.setAllergies("None");
        testPatient1.setFamilyHistory("Cancer");

        testPatient2 = new PatientDTO();
        testPatient2.setPatientId(2L);
        testPatient2.setUserId(2L);
        testPatient2.setBloodGroup("O-");
        testPatient2.setAllergies("Penicillin");
        testPatient2.setFamilyHistory("Diabetes");
    }

    @Test
    void testGetAllPatients_Success() {
        when(patientService.getAllPatients()).thenReturn(Arrays.asList(testPatient1, testPatient2));
        ResponseEntity<List<PatientDTO>> response = patientController.getAllPatients();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        List<PatientDTO> patients = response.getBody();
        assertEquals(2, patients.size());
        assertEquals("A+", patients.get(0).getBloodGroup());
        assertEquals("O-", patients.get(1).getBloodGroup());
    }

    @Test
    void testGetAllPatients_Failure() {
        when(patientService.getAllPatients()).thenThrow(new RuntimeException("Database error"));
        ResponseEntity<List<PatientDTO>> response = patientController.getAllPatients();
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void testGetPatientById_Success() {
        when(patientService.getPatientById(1L)).thenReturn(testPatient1);
        ResponseEntity<PatientDTO> response = patientController.getPatientById(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        PatientDTO patient = response.getBody();
        assertNotNull(patient);
        assertEquals(1L, patient.getPatientId());
        assertEquals("A+", patient.getBloodGroup());
    }

    @Test
    void testGetPatientById_NotFound() {
        when(patientService.getPatientById(1L)).thenThrow(new ResourceNotFoundException("Patient", "id", 1L));
        ResponseEntity<PatientDTO> response = patientController.getPatientById(1L);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void testCreatePatient_Success() {
        when(patientService.createPatient(any(PatientDTO.class))).thenReturn(testPatient1);
        ResponseEntity<PatientDTO> response = patientController.createPatient(testPatient1);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        PatientDTO patient = response.getBody();
        assertNotNull(patient);
        assertEquals(1L, patient.getPatientId());
        assertEquals("A+", patient.getBloodGroup());
    }

    @Test
    void testCreatePatient_BadRequest() {
        when(patientService.createPatient(any(PatientDTO.class)))
                .thenThrow(new InvalidOperationException("Invalid data"));
        ResponseEntity<PatientDTO> response = patientController.createPatient(testPatient1);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void testUpdatePatient_Success() {
        when(patientService.updatePatient(any(PatientDTO.class))).thenReturn(testPatient1);
        ResponseEntity<PatientDTO> response = patientController.updatePatient(1L, testPatient1);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        PatientDTO patient = response.getBody();
        assertNotNull(patient);
        assertEquals(1L, patient.getPatientId());
        assertEquals("A+", patient.getBloodGroup());
        verify(patientService, times(1)).updatePatient(any(PatientDTO.class));
    }

    @Test
    void testUpdatePatient_BadRequest() {
        when(patientService.updatePatient(any(PatientDTO.class)))
                .thenThrow(new InvalidOperationException("Invalid data"));
        ResponseEntity<PatientDTO> response = patientController.updatePatient(1L, testPatient1);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void testDeletePatient_Success() {
        ResponseEntity<Void> response = patientController.deletePatient(1L);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(patientService, times(1)).deletePatient(1L);
    }

    @Test
    void testDeletePatient_NotFound() {
        doThrow(new ResourceNotFoundException("Patient", "id", 1L)).when(patientService).deletePatient(anyLong());
        ResponseEntity<Void> response = patientController.deletePatient(1L);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }
}
