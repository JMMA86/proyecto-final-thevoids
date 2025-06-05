package org.thevoids.oncologic.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;
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

        @Mock
        private FileService fileService;

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
                Lab lab = new Lab();
                lab.setLabId(id);

                when(labRepository.findById(id)).thenReturn(Optional.of(lab));

                labService.deleteLab(id);

                verify(labRepository).deleteById(id);
        }

        @Test
        void deleteLabThrowsExceptionWhenLabDoesNotExist() {
                Long id = 1L;

                when(labRepository.findById(id)).thenReturn(Optional.empty());

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

        // NEW TESTS FOR MISSING METHOD COVERAGE

        @Test
        void updateLabWithFileSuccessfullyUpdatesLabWithoutFile() {
                Long labId = 1L;
                LabDTO labDTO = new LabDTO();
                labDTO.setLabId(labId);
                labDTO.setTestType("Updated Test");

                Lab existingLab = new Lab();
                existingLab.setLabId(labId);
                existingLab.setAttachment(null);

                Lab updatedLab = new Lab();
                updatedLab.setLabId(labId);
                updatedLab.setTestType("Updated Test");

                LabDTO expectedLabDTO = new LabDTO();
                expectedLabDTO.setLabId(labId);
                expectedLabDTO.setTestType("Updated Test");

                when(labRepository.findById(labId)).thenReturn(Optional.of(existingLab));
                when(labMapper.toLab(any(LabDTO.class))).thenReturn(updatedLab);
                when(labRepository.save(updatedLab)).thenReturn(updatedLab);
                when(labMapper.toLabDTO(updatedLab)).thenReturn(expectedLabDTO);

                LabDTO result = labService.updateLabWithFile(labDTO, null);

                assertNotNull(result);
                assertEquals(labId, result.getLabId());
                assertEquals("Updated Test", result.getTestType());
                verify(fileService, never()).isValidFile(any());
                verify(fileService, never()).storeFile(any(), any());
                verify(fileService, never()).deleteFile(any());
                verify(labRepository).save(updatedLab);
        }

        @Test
        void updateLabWithFileThrowsExceptionWhenLabIsNull() {
                InvalidOperationException exception = assertThrows(InvalidOperationException.class,
                                () -> labService.updateLabWithFile(null, null));

                assertEquals("Lab cannot be null", exception.getMessage());
                verify(labRepository, never()).save(any(Lab.class));
        }

        @Test
        void updateLabWithFileThrowsExceptionWhenLabIdIsNull() {
                LabDTO labDTO = new LabDTO();
                // labId is null

                InvalidOperationException exception = assertThrows(InvalidOperationException.class,
                                () -> labService.updateLabWithFile(labDTO, null));

                assertEquals("Lab ID cannot be null", exception.getMessage());
                verify(labRepository, never()).save(any(Lab.class));
        }

        @Test
        void updateLabWithFileThrowsExceptionWhenLabDoesNotExist() {
                Long labId = 1L;
                LabDTO labDTO = new LabDTO();
                labDTO.setLabId(labId);

                when(labRepository.findById(labId)).thenReturn(Optional.empty());

                ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                                () -> labService.updateLabWithFile(labDTO, null));

                assertEquals("Lab no encontrado con id : '1'", exception.getMessage());
                verify(labRepository, never()).save(any(Lab.class));
        }

        @Test
        void updateLabWithFileThrowsExceptionWhenFileIsInvalid() {
                Long labId = 1L;
                LabDTO labDTO = new LabDTO();
                labDTO.setLabId(labId);

                Lab existingLab = new Lab();
                existingLab.setLabId(labId);

                MultipartFile mockFile = mock(MultipartFile.class);
                when(mockFile.isEmpty()).thenReturn(false);
                when(fileService.isValidFile(mockFile)).thenReturn(false);
                when(labRepository.findById(labId)).thenReturn(Optional.of(existingLab));

                InvalidOperationException exception = assertThrows(InvalidOperationException.class,
                                () -> labService.updateLabWithFile(labDTO, mockFile));

                assertEquals("Invalid file type or size", exception.getMessage());
                verify(fileService).isValidFile(mockFile);
                verify(fileService, never()).storeFile(any(), any());
                verify(labRepository, never()).save(any(Lab.class));
        }

        @Test
        void updateLabWithFileDeletesOldFileBeforeStoringNew() {
                Long labId = 1L;
                LabDTO labDTO = new LabDTO();
                labDTO.setLabId(labId);

                Lab existingLab = new Lab();
                existingLab.setLabId(labId);
                existingLab.setAttachment("labs/old-file.pdf");

                Lab updatedLab = new Lab();
                updatedLab.setLabId(labId);
                updatedLab.setAttachment("labs/new-file.pdf");

                LabDTO expectedLabDTO = new LabDTO();
                expectedLabDTO.setLabId(labId);
                expectedLabDTO.setAttachment("labs/new-file.pdf");

                MultipartFile mockFile = mock(MultipartFile.class);
                when(mockFile.isEmpty()).thenReturn(false);
                when(fileService.isValidFile(mockFile)).thenReturn(true);
                when(fileService.storeFile(mockFile, "labs")).thenReturn("labs/new-file.pdf");
                when(labRepository.findById(labId)).thenReturn(Optional.of(existingLab));
                when(labMapper.toLab(any(LabDTO.class))).thenReturn(updatedLab);
                when(labRepository.save(updatedLab)).thenReturn(updatedLab);
                when(labMapper.toLabDTO(updatedLab)).thenReturn(expectedLabDTO);

                LabDTO result = labService.updateLabWithFile(labDTO, mockFile);

                assertNotNull(result);
                assertEquals("labs/new-file.pdf", result.getAttachment());
                verify(fileService).deleteFile("labs/old-file.pdf");
                verify(fileService).isValidFile(mockFile);
                verify(fileService).storeFile(mockFile, "labs");
                verify(labRepository).save(updatedLab);
        }

        @Test
        void assignLabWithFileSuccessfullyCreatesLabWithoutFile() {
                Long patientId = 1L;
                Long userId = 1L;
                Date requestDate = new Date();

                User user = new User();
                user.setUserId(userId);

                Patient patient = new Patient();
                patient.setPatientId(patientId);

                Lab savedLab = new Lab();
                savedLab.setLabId(1L);
                savedLab.setRequestDate(requestDate);
                savedLab.setLabTechnician(user);
                savedLab.setPatient(patient);
                savedLab.setAttachment(null);

                LabDTO expectedLabDTO = new LabDTO();
                expectedLabDTO.setLabId(1L);
                expectedLabDTO.setAttachment(null);

                when(userRepository.findById(userId)).thenReturn(Optional.of(user));
                when(patientRepository.findById(patientId)).thenReturn(Optional.of(patient));
                when(labRepository.save(any(Lab.class))).thenReturn(savedLab);
                when(labMapper.toLabDTO(savedLab)).thenReturn(expectedLabDTO);

                LabDTO result = labService.assignLabWithFile(patientId, userId, requestDate, null, null, null, null);

                assertNotNull(result);
                assertNull(result.getAttachment());
                verify(fileService, never()).isValidFile(any());
                verify(fileService, never()).storeFile(any(), any());
                verify(labRepository).save(any(Lab.class));
        }

        @Test
        void assignLabWithFileThrowsExceptionWhenPatientIdIsNull() {
                Long userId = 1L;
                Date requestDate = new Date();
                MultipartFile mockFile = mock(MultipartFile.class);
                InvalidOperationException exception = assertThrows(InvalidOperationException.class,
                                () -> labService.assignLabWithFile(null, userId, requestDate, "Test Type", null, null,
                                                mockFile));

                assertEquals("Patient ID, User ID, and Request Date cannot be null", exception.getMessage());
                verify(labRepository, never()).save(any(Lab.class));
        }

        @Test
        void assignLabWithFileThrowsExceptionWhenUserIdIsNull() {
                Long patientId = 1L;
                Date requestDate = new Date();
                MultipartFile mockFile = mock(MultipartFile.class);
                InvalidOperationException exception = assertThrows(InvalidOperationException.class,
                                () -> labService.assignLabWithFile(patientId, null, requestDate, "Test Type", null,
                                                null, mockFile));

                assertEquals("Patient ID, User ID, and Request Date cannot be null", exception.getMessage());
                verify(labRepository, never()).save(any(Lab.class));
        }

        @Test
        void assignLabWithFileThrowsExceptionWhenRequestDateIsNull() {
                Long patientId = 1L;
                Long userId = 1L;
                MultipartFile mockFile = mock(MultipartFile.class);
                InvalidOperationException exception = assertThrows(InvalidOperationException.class,
                                () -> labService.assignLabWithFile(patientId, userId, null, "Test Type", null, null,
                                                mockFile));

                assertEquals("Patient ID, User ID, and Request Date cannot be null", exception.getMessage());
                verify(labRepository, never()).save(any(Lab.class));
        }

        @Test
        void assignLabWithFileThrowsExceptionWhenUserDoesNotExist() {
                Long patientId = 1L;
                Long userId = 1L;
                Date requestDate = new Date();
                MultipartFile mockFile = mock(MultipartFile.class);

                when(userRepository.findById(userId)).thenReturn(Optional.empty());

                ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                                () -> labService.assignLabWithFile(patientId, userId, requestDate, "Test Type", null,
                                                null, mockFile));

                assertEquals("User no encontrado con id : '1'", exception.getMessage());
                verify(labRepository, never()).save(any(Lab.class));
        }

        @Test
        void assignLabWithFileThrowsExceptionWhenPatientDoesNotExist() {
                Long patientId = 1L;
                Long userId = 1L;
                Date requestDate = new Date();
                MultipartFile mockFile = mock(MultipartFile.class);

                User user = new User();
                user.setUserId(userId);

                when(userRepository.findById(userId)).thenReturn(Optional.of(user));
                when(patientRepository.findById(patientId)).thenReturn(Optional.empty());

                ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                                () -> labService.assignLabWithFile(patientId, userId, requestDate, "Test Type", null,
                                                null, mockFile));

                assertEquals("Patient no encontrado con id : '1'", exception.getMessage());
                verify(labRepository, never()).save(any(Lab.class));
        }

        @Test
        void assignLabWithFileHandlesEmptyFile() {
                Long patientId = 1L;
                Long userId = 1L;
                Date requestDate = new Date();

                User user = new User();
                user.setUserId(userId);

                Patient patient = new Patient();
                patient.setPatientId(patientId);

                Lab savedLab = new Lab();
                savedLab.setLabId(1L);
                savedLab.setRequestDate(requestDate);
                savedLab.setLabTechnician(user);
                savedLab.setPatient(patient);
                savedLab.setAttachment(null);

                LabDTO expectedLabDTO = new LabDTO();
                expectedLabDTO.setLabId(1L);
                expectedLabDTO.setAttachment(null);

                MultipartFile mockFile = mock(MultipartFile.class);
                when(mockFile.isEmpty()).thenReturn(true);
                when(userRepository.findById(userId)).thenReturn(Optional.of(user));
                when(patientRepository.findById(patientId)).thenReturn(Optional.of(patient));
                when(labRepository.save(any(Lab.class))).thenReturn(savedLab);
                when(labMapper.toLabDTO(savedLab)).thenReturn(expectedLabDTO);

                LabDTO result = labService.assignLabWithFile(patientId, userId, requestDate, "Test Type", null, null,
                                mockFile);

                assertNotNull(result);
                assertNull(result.getAttachment());
                verify(fileService, never()).isValidFile(any());
                verify(fileService, never()).storeFile(any(), any());
                verify(labRepository).save(any(Lab.class));
        }

        @Test
        void deleteLabDoesNotCallFileServiceWhenNoAttachment() {
                Long labId = 1L;
                Lab lab = new Lab();
                lab.setLabId(labId);
                lab.setAttachment(null);

                when(labRepository.findById(labId)).thenReturn(Optional.of(lab));

                labService.deleteLab(labId);

                verify(fileService, never()).deleteFile(any());
                verify(labRepository).deleteById(labId);
        }

        @Test
        void deleteLabCallsFileServiceWhenAttachmentExists() {
                Long labId = 1L;
                Lab lab = new Lab();
                lab.setLabId(labId);
                lab.setAttachment("labs/test-file.pdf");

                when(labRepository.findById(labId)).thenReturn(Optional.of(lab));

                labService.deleteLab(labId);

                verify(fileService).deleteFile("labs/test-file.pdf");
                verify(labRepository).deleteById(labId);
        }

        @Test
        void deleteLabCallsFileServiceWhenAttachmentIsEmpty() {
                Long labId = 1L;
                Lab lab = new Lab();
                lab.setLabId(labId);
                lab.setAttachment("");

                when(labRepository.findById(labId)).thenReturn(Optional.of(lab));

                labService.deleteLab(labId);

                verify(fileService, never()).deleteFile(any());
                verify(labRepository).deleteById(labId);
        }

        @Test
        void updateLabWithFileUpdatesPatientAndTechnicianReferences() {
                Long labId = 1L;
                Long patientId = 2L;
                Long technicianId = 3L;

                LabDTO labDTO = new LabDTO();
                labDTO.setLabId(labId);
                labDTO.setPatientId(patientId);
                labDTO.setLabTechnicianId(technicianId);

                Lab existingLab = new Lab();
                existingLab.setLabId(labId);

                Patient patient = new Patient();
                patient.setPatientId(patientId);

                User technician = new User();
                technician.setUserId(technicianId);

                Lab mappedLab = new Lab();
                mappedLab.setLabId(labId);
                Patient mappedLabPatient = new Patient();
                mappedLabPatient.setPatientId(patientId);
                mappedLab.setPatient(mappedLabPatient);
                User mappedLabTechnician = new User();
                mappedLabTechnician.setUserId(technicianId);
                mappedLab.setLabTechnician(mappedLabTechnician);

                Lab updatedLab = new Lab();
                updatedLab.setLabId(labId);
                updatedLab.setPatient(patient);
                updatedLab.setLabTechnician(technician);

                LabDTO expectedLabDTO = new LabDTO();
                expectedLabDTO.setLabId(labId);

                when(labRepository.findById(labId)).thenReturn(Optional.of(existingLab));
                when(labMapper.toLab(any(LabDTO.class))).thenReturn(mappedLab);
                when(patientRepository.findById(patientId)).thenReturn(Optional.of(patient));
                when(userRepository.findById(technicianId)).thenReturn(Optional.of(technician));
                when(labRepository.save(any(Lab.class))).thenReturn(updatedLab);
                when(labMapper.toLabDTO(updatedLab)).thenReturn(expectedLabDTO);

                LabDTO result = labService.updateLabWithFile(labDTO, null);

                assertNotNull(result);
                verify(patientRepository).findById(patientId);
                verify(userRepository).findById(technicianId);
                verify(labRepository).save(any(Lab.class));
        }

        @Test
        void updateLabWithFileThrowsExceptionWhenPatientNotFound() {
                Long labId = 1L;
                Long patientId = 2L;

                LabDTO labDTO = new LabDTO();
                labDTO.setLabId(labId);
                labDTO.setPatientId(patientId);

                Lab existingLab = new Lab();
                existingLab.setLabId(labId);

                Lab mappedLab = new Lab();
                mappedLab.setLabId(labId);
                Patient mappedLabPatient = new Patient();
                mappedLabPatient.setPatientId(patientId);
                mappedLab.setPatient(mappedLabPatient);

                when(labRepository.findById(labId)).thenReturn(Optional.of(existingLab));
                when(labMapper.toLab(any(LabDTO.class))).thenReturn(mappedLab);
                when(patientRepository.findById(patientId)).thenReturn(Optional.empty());

                ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                                () -> labService.updateLabWithFile(labDTO, null));

                assertEquals("Patient no encontrado con id : '2'", exception.getMessage());
                verify(labRepository, never()).save(any(Lab.class));
        }

        @Test
        void updateLabWithFileThrowsExceptionWhenTechnicianNotFound() {
                Long labId = 1L;
                Long patientId = 2L;
                Long technicianId = 3L;

                LabDTO labDTO = new LabDTO();
                labDTO.setLabId(labId);
                labDTO.setPatientId(patientId);
                labDTO.setLabTechnicianId(technicianId);

                Lab existingLab = new Lab();
                existingLab.setLabId(labId);

                Patient patient = new Patient();
                patient.setPatientId(patientId);

                Lab mappedLab = new Lab();
                mappedLab.setLabId(labId);
                Patient mappedLabPatient = new Patient();
                mappedLabPatient.setPatientId(patientId);
                mappedLab.setPatient(mappedLabPatient);
                User mappedLabTechnician = new User();
                mappedLabTechnician.setUserId(technicianId);
                mappedLab.setLabTechnician(mappedLabTechnician);

                when(labRepository.findById(labId)).thenReturn(Optional.of(existingLab));
                when(labMapper.toLab(any(LabDTO.class))).thenReturn(mappedLab);
                when(patientRepository.findById(patientId)).thenReturn(Optional.of(patient));
                when(userRepository.findById(technicianId)).thenReturn(Optional.empty());

                ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                                () -> labService.updateLabWithFile(labDTO, null));

                assertEquals("User no encontrado con id : '3'", exception.getMessage());
                verify(labRepository, never()).save(any(Lab.class));
        }

        private Lab createLab(Long id, Date requestDate) {
                Lab lab = new Lab();
                lab.setLabId(id);
                lab.setRequestDate(requestDate);
                return lab;
        }
}
