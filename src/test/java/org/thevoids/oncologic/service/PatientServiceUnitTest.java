package org.thevoids.oncologic.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.thevoids.oncologic.entity.Patient;
import org.thevoids.oncologic.repository.PatientRepository;
import org.thevoids.oncologic.repository.UserRepository;
import org.thevoids.oncologic.service.impl.PatientServiceImpl;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PatientServiceUnitTest {

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private PatientServiceImpl patientService;

    @Test
    void getPatientByIdReturnsPatientWhenExists() {
        Long id = 1L;
        Patient expectedPatient = new Patient();
        expectedPatient.setPatientId(id);

        when(patientRepository.findById(id)).thenReturn(Optional.of(expectedPatient));

        Patient result = patientService.getPatientById(id);

        assertNotNull(result);
        assertEquals(id, result.getPatientId());
    }

    @Test
    void getPatientByIdThrowsExceptionWhenPatientDoesNotExist() {
        Long id = 1L;

        when(patientRepository.findById(id)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                patientService.getPatientById(id));

        assertEquals("Patient with id 1 does not exist", exception.getMessage());
    }

    @Test
    void createPatientSuccessfullyCreatesPatient() {
        Patient patient = new Patient();
        Patient savedPatient = new Patient();
        savedPatient.setPatientId(1L);

        when(patientRepository.save(patient)).thenReturn(savedPatient);

        Patient result = patientService.createPatient(patient);

        assertNotNull(result);
        assertEquals(savedPatient, result);
        verify(patientRepository).save(patient);
    }

    @Test
    void createPatientThrowsExceptionWhenPatientIsNull() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                patientService.createPatient(null));

        assertEquals("Patient cannot be null", exception.getMessage());
        verify(patientRepository, never()).save(any());
    }

    @Test
    void updatePatientSuccessfullyUpdatesPatient() {
        Long id = 1L;
        Patient patient = new Patient();
        patient.setPatientId(id);

        when(patientRepository.existsById(id)).thenReturn(true);
        when(patientRepository.save(patient)).thenReturn(patient);

        Patient result = patientService.updatePatient(patient);

        assertNotNull(result);
        assertEquals(patient, result);
        verify(patientRepository).save(patient);
    }

    @Test
    void updatePatientThrowsExceptionWhenPatientIsNull() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                patientService.updatePatient(null));

        assertEquals("Patient cannot be null", exception.getMessage());
        verify(patientRepository, never()).save(any());
    }

    @Test
    void updatePatientThrowsExceptionWhenIdIsNull() {
        Patient patient = new Patient();

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                patientService.updatePatient(patient));

        assertEquals("Patient ID cannot be null", exception.getMessage());
        verify(patientRepository, never()).save(any());
    }

    @Test
    void updatePatientThrowsExceptionWhenPatientDoesNotExist() {
        Long id = 1L;
        Patient patient = new Patient();
        patient.setPatientId(id);

        when(patientRepository.existsById(id)).thenReturn(false);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                patientService.updatePatient(patient));

        assertEquals("Patient with id 1 does not exist", exception.getMessage());
        verify(patientRepository, never()).save(any());
    }

    @Test
    void deletePatientSuccessfullyDeletesPatient() {
        Long id = 1L;

        when(patientRepository.existsById(id)).thenReturn(true);

        patientService.deletePatient(id);

        verify(patientRepository).deleteById(id);
    }

    @Test
    void deletePatientThrowsExceptionWhenPatientDoesNotExist() {
        Long id = 1L;

        when(patientRepository.existsById(id)).thenReturn(false);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                patientService.deletePatient(id));

        assertEquals("Patient with id 1 does not exist", exception.getMessage());
        verify(patientRepository, never()).deleteById(any());
    }

    @Test
    void getAllPatientsReturnsAllPatients() {
        List<Patient> expectedPatients = List.of(
                createPatient(1L),
                createPatient(2L)
        );

        when(patientRepository.findAll()).thenReturn(expectedPatients);

        List<Patient> result = patientService.getAllPatients();

        assertEquals(2, result.size());
        assertEquals(expectedPatients, result);
    }

    private Patient createPatient(Long id) {
        Patient patient = new Patient();
        patient.setPatientId(id);
        return patient;
    }
}