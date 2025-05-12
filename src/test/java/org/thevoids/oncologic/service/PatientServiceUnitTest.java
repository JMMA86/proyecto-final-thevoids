package org.thevoids.oncologic.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.thevoids.oncologic.dto.entity.PatientDTO;
import org.thevoids.oncologic.entity.Patient;
import org.thevoids.oncologic.exception.InvalidOperationException;
import org.thevoids.oncologic.exception.ResourceNotFoundException;
import org.thevoids.oncologic.mapper.PatientMapper;
import org.thevoids.oncologic.repository.PatientRepository;
import org.thevoids.oncologic.repository.UserRepository;
import org.thevoids.oncologic.service.impl.PatientServiceImpl;

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
                PatientDTO patientDTO = new PatientDTO();
                patientDTO.setPatientId(id);

                when(patientRepository.findById(id)).thenReturn(Optional.of(patient));
                when(patientMapper.toPatientDTO(patient)).thenReturn(patientDTO);

                PatientDTO result = patientService.getPatientById(id);

                assertNotNull(result);
                assertEquals(id, result.getPatientId());
                verify(patientRepository).findById(id);
                verify(patientMapper).toPatientDTO(patient);
        }

        @Test
        void getPatientByIdThrowsExceptionWhenPatientDoesNotExist() {
                Long id = 1L;
                when(patientRepository.findById(id)).thenReturn(Optional.empty());
                assertThrows(ResourceNotFoundException.class, () -> patientService.getPatientById(id));
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
                assertThrows(InvalidOperationException.class, () -> patientService.createPatient(null));
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
                assertEquals(id, result.getPatientId());
                verify(patientRepository).existsById(id);
                verify(patientMapper).toPatient(patientDTO);
                verify(patientRepository).save(patient);
                verify(patientMapper).toPatientDTO(updatedPatient);
        }

        @Test
        void updatePatientThrowsExceptionWhenPatientIsNull() {
                assertThrows(InvalidOperationException.class, () -> patientService.updatePatient(null));
                verify(patientRepository, never()).save(any());
        }

        @Test
        void updatePatientThrowsExceptionWhenIdIsNull() {
                PatientDTO patientDTO = new PatientDTO();
                assertThrows(InvalidOperationException.class, () -> patientService.updatePatient(patientDTO));
                verify(patientRepository, never()).save(any());
        }

        @Test
        void updatePatientThrowsExceptionWhenPatientDoesNotExist() {
                Long id = 1L;
                PatientDTO patientDTO = new PatientDTO();
                patientDTO.setPatientId(id);
                when(patientRepository.existsById(id)).thenReturn(false);
                assertThrows(ResourceNotFoundException.class, () -> patientService.updatePatient(patientDTO));
                verify(patientRepository, never()).save(any());
        }

        @Test
        void deletePatientSuccessfullyDeletesPatient() {
                Long id = 1L;
                Patient patient = new Patient();
                patient.setPatientId(id);
                when(patientRepository.findById(id)).thenReturn(Optional.of(patient));
                patientService.deletePatient(id);
                verify(patientRepository).delete(patient);
        }

        @Test
        void deletePatientThrowsExceptionWhenPatientDoesNotExist() {
                Long id = 1L;
                when(patientRepository.findById(id)).thenReturn(Optional.empty());
                assertThrows(ResourceNotFoundException.class, () -> patientService.deletePatient(id));
                verify(patientRepository, never()).delete(any());
        }
}
