package org.thevoids.oncologic.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.thevoids.oncologic.entity.Lab;
import org.thevoids.oncologic.entity.Patient;
import org.thevoids.oncologic.entity.User;
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

    @InjectMocks
    private LabServiceImpl labService;

    @Test
    void getLabByIdReturnsLabWhenExists() {
        Long id = 1L;
        Lab expectedLab = new Lab();
        expectedLab.setLabId(id);

        when(labRepository.findById(id)).thenReturn(Optional.of(expectedLab));

        Lab result = labService.getLabById(id);

        assertNotNull(result);
        assertEquals(id, result.getLabId());
    }

    @Test
    void getLabByIdThrowsExceptionWhenLabDoesNotExist() {
        Long id = 1L;

        when(labRepository.findById(id)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                labService.getLabById(id));

        assertEquals("Lab with id 1 does not exist", exception.getMessage());
    }

    @Test
    void updateLabSuccessfullyUpdatesLab() {
        Long id = 1L;
        Lab lab = new Lab();
        lab.setLabId(id);

        when(labRepository.existsById(id)).thenReturn(true);
        when(labRepository.save(lab)).thenReturn(lab);

        Lab result = labService.updateLab(lab);

        assertNotNull(result);
        assertEquals(lab, result);
        verify(labRepository).save(lab);
    }

    @Test
    void updateLabThrowsExceptionWhenLabIsNull() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                labService.updateLab(null));

        assertEquals("Lab cannot be null", exception.getMessage());
        verify(labRepository, never()).save(any(Lab.class));
    }

    @Test
    void updateLabThrowsExceptionWhenLabIdIsNull() {
        Lab lab = new Lab();

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                labService.updateLab(lab));

        assertEquals("Lab ID cannot be null", exception.getMessage());
        verify(labRepository, never()).save(any(Lab.class));
    }

    @Test
    void updateLabThrowsExceptionWhenLabDoesNotExist() {
        Long id = 1L;
        Lab lab = new Lab();
        lab.setLabId(id);

        when(labRepository.existsById(id)).thenReturn(false);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                labService.updateLab(lab));

        assertEquals("Lab with id 1 does not exist", exception.getMessage());
        verify(labRepository, never()).save(any(Lab.class));
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

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                labService.deleteLab(id));

        assertEquals("Lab with id 1 does not exist", exception.getMessage());
        verify(labRepository, never()).deleteById(any(Long.class));
    }

    @Test
    void getAllLabsReturnsAllLabs() {
        List<Lab> expectedLabs = List.of(
                createLab(1L, new Date()),
                createLab(2L, new Date())
        );

        when(labRepository.findAll()).thenReturn(expectedLabs);

        List<Lab> result = labService.getAllLabs();

        assertEquals(2, result.size());
        assertEquals(expectedLabs, result);
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

        when(patientRepository.findById(patientId)).thenReturn(Optional.of(patient));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        Lab savedLab = labService.assignLab(patientId, userId, requestDate);

        assertEquals(patient, savedLab.getPatient());
        assertEquals(user, savedLab.getLabTechnician());
        assertEquals(requestDate, savedLab.getRequestDate());
    }

    @Test
    void assignLabThrowsExceptionWhenPatientIdIsNull() {
        Long userId = 2L;
        Date requestDate = new Date();

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                labService.assignLab(null, userId, requestDate));

        assertEquals("Patient ID, User ID, and Request Date cannot be null", exception.getMessage());
        verify(labRepository, never()).save(any(Lab.class));
    }

    @Test
    void assignLabThrowsExceptionWhenUserIdIsNull() {
        Long patientId = 1L;
        Date requestDate = new Date();

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                labService.assignLab(patientId, null, requestDate));

        assertEquals("Patient ID, User ID, and Request Date cannot be null", exception.getMessage());
        verify(labRepository, never()).save(any(Lab.class));
    }

    @Test
    void assignLabThrowsExceptionWhenRequestDateIsNull() {
        Long patientId = 1L;
        Long userId = 2L;

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                labService.assignLab(patientId, userId, null));

        assertEquals("Patient ID, User ID, and Request Date cannot be null", exception.getMessage());
        verify(labRepository, never()).save(any(Lab.class));
    }

    @Test
    void assignLabThrowsExceptionWhenUserDoesNotExist() {
        Long patientId = 1L;
        Long userId = 2L;
        Date requestDate = new Date();

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                labService.assignLab(patientId, userId, requestDate));

        assertEquals("User with id 2 does not exist", exception.getMessage());
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

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                labService.assignLab(patientId, userId, requestDate));

        assertEquals("Patient with id 1 does not exist", exception.getMessage());
        verify(labRepository, never()).save(any(Lab.class));
    }

    private Lab createLab(Long id, Date requestDate) {
        Lab lab = new Lab();
        lab.setLabId(id);
        lab.setRequestDate(requestDate);
        return lab;
    }
}