package org.thevoids.oncologic.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.thevoids.oncologic.dto.entity.LabDTO;
import org.thevoids.oncologic.entity.Lab;
import org.thevoids.oncologic.entity.Patient;
import org.thevoids.oncologic.entity.User;
import org.thevoids.oncologic.exception.InvalidOperationException;
import org.thevoids.oncologic.exception.ResourceNotFoundException;
import org.thevoids.oncologic.mapper.LabMapper;
import org.thevoids.oncologic.repository.LabRepository;
import org.thevoids.oncologic.repository.PatientRepository;
import org.thevoids.oncologic.repository.UserRepository;
import org.thevoids.oncologic.service.impl.LabServiceImpl;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LabServiceUnitTest {

        @Mock
        private LabRepository labRepository;

        @Mock
        private UserRepository userRepository;

        @Mock
        private PatientRepository patientRepository;

        @Mock
        private LabMapper labMapper;

        @InjectMocks
        private LabServiceImpl labService;

        @Test
        void getLabByIdReturnsLabWhenExists() {
                Long id = 1L;
                Lab lab = new Lab();
                lab.setLabId(id);

                LabDTO expectedLabDTO = new LabDTO();
                expectedLabDTO.setLabId(id);

                when(labRepository.findById(id)).thenReturn(Optional.of(lab));
                when(labMapper.toLabDTO(lab)).thenReturn(expectedLabDTO);

                LabDTO result = labService.getLabById(id);

                assertNotNull(result);
                assertEquals(id, result.getLabId());
                verify(labMapper).toLabDTO(lab);
        }

        @Test
        void getLabByIdThrowsExceptionWhenLabDoesNotExist() {
                Long id = 1L;

                when(labRepository.findById(id)).thenReturn(Optional.empty());

                ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                                () -> labService.getLabById(id));

                assertEquals("Lab no encontrado con id : '1'", exception.getMessage());
        }

        @Test
        void updateLabSuccessfullyUpdatesLab() {
                Long id = 1L;
                LabDTO labDTO = new LabDTO();
                labDTO.setLabId(id);

                Lab lab = new Lab();
                lab.setLabId(id);

                Lab savedLab = new Lab();
                savedLab.setLabId(id);

                LabDTO savedLabDTO = new LabDTO();
                savedLabDTO.setLabId(id);

                when(labRepository.existsById(id)).thenReturn(true);
                when(labMapper.toLab(labDTO)).thenReturn(lab);
                when(labRepository.save(lab)).thenReturn(savedLab);
                when(labMapper.toLabDTO(savedLab)).thenReturn(savedLabDTO);

                LabDTO result = labService.updateLab(labDTO);

                assertNotNull(result);
                assertEquals(id, result.getLabId());
                verify(labMapper).toLab(labDTO);
                verify(labRepository).save(lab);
                verify(labMapper).toLabDTO(savedLab);
        }

        @Test
        void updateLabThrowsExceptionWhenLabIsNull() {
                InvalidOperationException exception = assertThrows(InvalidOperationException.class,
                                () -> labService.updateLab(null));

                assertEquals("Lab cannot be null", exception.getMessage());
                verify(labRepository, never()).save(any(Lab.class));
        }

        @Test
        void updateLabThrowsExceptionWhenLabIdIsNull() {
                LabDTO labDTO = new LabDTO();

                InvalidOperationException exception = assertThrows(InvalidOperationException.class,
                                () -> labService.updateLab(labDTO));

                assertEquals("Lab ID cannot be null", exception.getMessage());
                verify(labRepository, never()).save(any(Lab.class));
                verify(labMapper, never()).toLab(any(LabDTO.class));
        }

        @Test
        void updateLabThrowsExceptionWhenLabDoesNotExist() {
                Long id = 1L;
                LabDTO labDTO = new LabDTO();
                labDTO.setLabId(id);

                when(labRepository.existsById(id)).thenReturn(false);

                ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                                () -> labService.updateLab(labDTO));

                assertEquals("Lab no encontrado con id : '1'", exception.getMessage());
                verify(labRepository, never()).save(any(Lab.class));
                verify(labMapper, never()).toLab(any(LabDTO.class));
        }

        @Test
        void deleteLabSuccessfullyDeletesLab() {
                Long id = 1L;

                when(labRepository.existsById(id)).thenReturn(true);

                labService.deleteLab(id);

                verify(labRepository).deleteById(id);
        }

        @Test
        void deleteLabThrowsExceptionWhenLabDoesNotExist() {
                Long id = 1L;

                when(labRepository.existsById(id)).thenReturn(false);

                ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                                () -> labService.deleteLab(id));

                assertEquals("Lab no encontrado con id : '1'", exception.getMessage());
                verify(labRepository, never()).deleteById(any(Long.class));
        }

        @Test
        void getAllLabsReturnsAllLabs() {
                List<Lab> labs = List.of(
                                createLab(1L, new Date()),
                                createLab(2L, new Date()));

                List<LabDTO> expectedLabDTOs = List.of(
                                createLabDTO(1L, new Date()),
                                createLabDTO(2L, new Date()));

                when(labRepository.findAll()).thenReturn(labs);
                when(labMapper.toLabDTO(labs.get(0))).thenReturn(expectedLabDTOs.get(0));
                when(labMapper.toLabDTO(labs.get(1))).thenReturn(expectedLabDTOs.get(1));

                List<LabDTO> result = labService.getAllLabs();

                assertEquals(2, result.size());
                assertEquals(expectedLabDTOs, result);
                verify(labRepository).findAll();
                verify(labMapper).toLabDTO(labs.get(0));
                verify(labMapper).toLabDTO(labs.get(1));
        }

        private LabDTO createLabDTO(Long id, Date requestDate) {
                LabDTO labDTO = new LabDTO();
                labDTO.setLabId(id);
                labDTO.setRequestDate(requestDate);
                return labDTO;
        }

        @Test
        void assignLabSuccessfullyAssignsLabToPatientAndUser() {
                Long patientId = 1L;
                Long userId = 2L;
                Date requestDate = new Date();

                Patient patient = new Patient();
                patient.setPatientId(patientId);

                User user = new User();
                user.setUserId(userId);

                Lab lab = new Lab();
                lab.setPatient(patient);
                lab.setLabTechnician(user);
                lab.setRequestDate(requestDate);

                Lab savedLab = new Lab();
                savedLab.setLabId(1L);
                savedLab.setPatient(patient);
                savedLab.setLabTechnician(user);
                savedLab.setRequestDate(requestDate);

                LabDTO savedLabDTO = new LabDTO();
                savedLabDTO.setLabId(1L);
                savedLabDTO.setPatientId(patientId);
                savedLabDTO.setLabTechnicianId(userId);
                savedLabDTO.setRequestDate(requestDate);

                when(patientRepository.findById(patientId)).thenReturn(Optional.of(patient));
                when(userRepository.findById(userId)).thenReturn(Optional.of(user));
                when(labRepository.save(any(Lab.class))).thenReturn(savedLab);
                when(labMapper.toLabDTO(savedLab)).thenReturn(savedLabDTO);

                LabDTO result = labService.assignLab(patientId, userId, requestDate);

                assertNotNull(result);
                assertEquals(patientId, result.getPatientId());
                assertEquals(userId, result.getLabTechnicianId());
                assertEquals(requestDate, result.getRequestDate());
                verify(patientRepository).findById(patientId);
                verify(userRepository).findById(userId);
                verify(labRepository).save(any(Lab.class));
                verify(labMapper).toLabDTO(savedLab);
        }

        @Test
        void assignLabThrowsExceptionWhenPatientIdIsNull() {
                Long userId = 2L;
                Date requestDate = new Date();

                InvalidOperationException exception = assertThrows(InvalidOperationException.class,
                                () -> labService.assignLab(null, userId, requestDate));

                assertEquals("Patient ID, User ID, and Request Date cannot be null", exception.getMessage());
                verify(labRepository, never()).save(any(Lab.class));
        }

        @Test
        void assignLabThrowsExceptionWhenUserIdIsNull() {
                Long patientId = 1L;
                Date requestDate = new Date();

                InvalidOperationException exception = assertThrows(InvalidOperationException.class,
                                () -> labService.assignLab(patientId, null, requestDate));

                assertEquals("Patient ID, User ID, and Request Date cannot be null", exception.getMessage());
                verify(labRepository, never()).save(any(Lab.class));
        }

        @Test
        void assignLabThrowsExceptionWhenRequestDateIsNull() {
                Long patientId = 1L;
                Long userId = 2L;

                InvalidOperationException exception = assertThrows(InvalidOperationException.class,
                                () -> labService.assignLab(patientId, userId, null));

                assertEquals("Patient ID, User ID, and Request Date cannot be null", exception.getMessage());
                verify(labRepository, never()).save(any(Lab.class));
        }

        @Test
        void assignLabThrowsExceptionWhenUserDoesNotExist() {
                Long patientId = 1L;
                Long userId = 2L;
                Date requestDate = new Date();

                when(userRepository.findById(userId)).thenReturn(Optional.empty());

                ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                                () -> labService.assignLab(patientId, userId, requestDate));

                assertEquals("User no encontrado con id : '2'", exception.getMessage());
                verify(labRepository, never()).save(any(Lab.class));
        }

        @Test
        void assignLabThrowsExceptionWhenPatientDoesNotExist() {
                Long patientId = 1L;
                Long userId = 2L;
                Date requestDate = new Date();

                User user = new User();
                user.setUserId(userId);

                when(userRepository.findById(userId)).thenReturn(Optional.of(user));
                when(patientRepository.findById(patientId)).thenReturn(Optional.empty());

                ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                                () -> labService.assignLab(patientId, userId, requestDate));

                assertEquals("Patient no encontrado con id : '1'", exception.getMessage());
                verify(labRepository, never()).save(any(Lab.class));
        }

        private Lab createLab(Long id, Date requestDate) {
                Lab lab = new Lab();
                lab.setLabId(id);
                lab.setRequestDate(requestDate);
                return lab;
        }
}
