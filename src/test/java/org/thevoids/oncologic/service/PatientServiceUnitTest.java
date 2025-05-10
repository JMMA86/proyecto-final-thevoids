package org.thevoids.oncologic.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.thevoids.oncologic.dto.PatientDTO;
import org.thevoids.oncologic.entity.Patient;
import org.thevoids.oncologic.mapper.PatientMapper;
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

    @Mock
    private PatientMapper patientMapper;

    @InjectMocks
    private PatientServiceImpl patientService;

    @Test
    void getPatientByIdReturnsPatientWhenExists() {
        Long id = 1L;
        Patient patient = new Patient();
        patient.setPatientId(id);

        PatientDTO expectedPatientDTO = new PatientDTO();
        expectedPatientDTO.setPatientId(id);

        when(patientRepository.findById(id)).thenReturn(Optional.of(patient));
        when(patientMapper.toPatientDTO(patient)).thenReturn(expectedPatientDTO);

        PatientDTO result = patientService.getPatientById(id);

        assertNotNull(result);
        assertEquals(id, result.getPatientId());
        verify(patientMapper).toPatientDTO(patient);
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
        PatientDTO patientDTO = new PatientDTO();
        Patient patient = new Patient();
        Patient savedPatient = new Patient();
        savedPatient.setPatientId(1L);

        PatientDTO savedPatientDTO = new PatientDTO();
        savedPatientDTO.setPatientId(1L);

        when(patientMapper.toPatient(patientDTO)).thenReturn(patient);
        when(patientRepository.save(patient)).thenReturn(savedPatient);
        when(patientMapper.toPatientDTO(savedPatient)).thenReturn(savedPatientDTO);

        PatientDTO result = patientService.createPatient(patientDTO);

        assertNotNull(result);
        assertEquals(savedPatientDTO.getPatientId(), result.getPatientId());
        verify(patientMapper).toPatient(patientDTO);
        verify(patientRepository).save(patient);
        verify(patientMapper).toPatientDTO(savedPatient);
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
        PatientDTO patientDTO = new PatientDTO();
        patientDTO.setPatientId(id);

        Patient patient = new Patient();
        patient.setPatientId(id);

        Patient updatedPatient = new Patient();
        updatedPatient.setPatientId(id);

        PatientDTO updatedPatientDTO = new PatientDTO();
        updatedPatientDTO.setPatientId(id);

        when(patientRepository.existsById(id)).thenReturn(true);
        when(patientMapper.toPatient(patientDTO)).thenReturn(patient);
        when(patientRepository.save(patient)).thenReturn(updatedPatient);
        when(patientMapper.toPatientDTO(updatedPatient)).thenReturn(updatedPatientDTO);

        PatientDTO result = patientService.updatePatient(patientDTO);

        assertNotNull(result);
        assertEquals(updatedPatientDTO.getPatientId(), result.getPatientId());
        verify(patientMapper).toPatient(patientDTO);
        verify(patientRepository).save(patient);
        verify(patientMapper).toPatientDTO(updatedPatient);
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
        PatientDTO patientDTO = new PatientDTO();

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                patientService.updatePatient(patientDTO));

        assertEquals("Patient ID cannot be null", exception.getMessage());
        verify(patientRepository, never()).save(any());
        verify(patientMapper, never()).toPatient(any());
    }

    @Test
    void updatePatientThrowsExceptionWhenPatientDoesNotExist() {
        Long id = 1L;
        PatientDTO patientDTO = new PatientDTO();
        patientDTO.setPatientId(id);

        when(patientRepository.existsById(id)).thenReturn(false);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                patientService.updatePatient(patientDTO));

        assertEquals("Patient with id 1 does not exist", exception.getMessage());
        verify(patientRepository, never()).save(any());
        verify(patientMapper, never()).toPatient(any());
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
        List<Patient> patients = List.of(
                createPatient(1L),
                createPatient(2L)
        );

        List<PatientDTO> expectedPatientDTOs = List.of(
                createPatientDTO(1L),
                createPatientDTO(2L)
        );

        when(patientRepository.findAll()).thenReturn(patients);
        when(patientMapper.toPatientDTOs(patients)).thenReturn(expectedPatientDTOs);

        List<PatientDTO> result = patientService.getAllPatients();

        assertEquals(2, result.size());
        assertEquals(expectedPatientDTOs, result);
        verify(patientRepository).findAll();
        verify(patientMapper).toPatientDTOs(patients);
    }

    private Patient createPatient(Long id) {
        Patient patient = new Patient();
        patient.setPatientId(id);
        return patient;
    }

    private PatientDTO createPatientDTO(Long id) {
        PatientDTO patientDTO = new PatientDTO();
        patientDTO.setPatientId(id);
        return patientDTO;
    }
}
