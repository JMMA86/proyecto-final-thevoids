package org.thevoids.oncologic.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.thevoids.oncologic.entity.Clinic;
import org.thevoids.oncologic.entity.ClinicAssignment;
import org.thevoids.oncologic.entity.User;
import org.thevoids.oncologic.repository.ClinicAssignmentRepository;
import org.thevoids.oncologic.repository.ClinicRepository;
import org.thevoids.oncologic.repository.UserRepository;
import org.thevoids.oncologic.service.impl.ClinicAssigmentImpl;

@ExtendWith(MockitoExtension.class)
class ClinicAssigmentServiceUnitTest {

    @Mock
    private ClinicAssignmentRepository clinicAssignmentRepository;
    @Mock
    private ClinicRepository clinicRepository;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ClinicAssigmentImpl clinicAssigmentService;

    private ClinicAssignment validAssignment;
    private Clinic clinic;
    private User user;

    @BeforeEach
    void setUp() {
        clinic = new Clinic();
        clinic.setId(2L);

        user = new User();
        user.setUserId(1L);

        validAssignment = new ClinicAssignment();
        validAssignment.setId(10L);
        validAssignment.setStartTime(new Date());
        validAssignment.setEndTime(new Date());
        validAssignment.setClinic(clinic);
        validAssignment.setUser(user);
    }

    @Test
    void getAllClinicAssignments_ReturnsList() {
        when(clinicAssignmentRepository.findAll()).thenReturn(List.of(validAssignment));
        List<ClinicAssignment> result = clinicAssigmentService.getAllClinicAssignments();
        assertEquals(1, result.size());
        assertEquals(validAssignment, result.get(0));
    }

    @Test
    void getClinicAssignmentById_ReturnsAssignment() {
        when(clinicAssignmentRepository.findById(10L)).thenReturn(Optional.of(validAssignment));
        ClinicAssignment result = clinicAssigmentService.getClinicAssignmentById(10L);
        assertEquals(validAssignment, result);
    }

    @Test
    void getClinicAssignmentById_ReturnsNullIfNotFound() {
        when(clinicAssignmentRepository.findById(99L)).thenReturn(Optional.empty());
        ClinicAssignment result = clinicAssigmentService.getClinicAssignmentById(99L);
        assertNull(result);
    }

    @Test
    void updateClinicAssignment_Success() {
        when(clinicAssignmentRepository.existsById(validAssignment.getId())).thenReturn(true);
        when(clinicAssignmentRepository.save(validAssignment)).thenReturn(validAssignment);
        ClinicAssignment result = clinicAssigmentService.updateClinicAssignment(validAssignment);
        assertEquals(validAssignment, result);
    }

    @Test
    void updateClinicAssignment_ThrowsIfNull() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> clinicAssigmentService.updateClinicAssignment(null));
        assertEquals("ClinicAssigment cannot be null", ex.getMessage());
    }

    @Test
    void updateClinicAssignment_ThrowsIfIdNull() {
        validAssignment.setId(null);
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> clinicAssigmentService.updateClinicAssignment(validAssignment));
        assertEquals("ClinicAssigment ID cannot be null", ex.getMessage());
    }

    @Test
    void updateClinicAssignment_ThrowsIfNotExists() {
        when(clinicAssignmentRepository.existsById(validAssignment.getId())).thenReturn(false);
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> clinicAssigmentService.updateClinicAssignment(validAssignment));
        assertEquals("ClinicAssigment with id 10 does not exist", ex.getMessage());
    }

    @Test
    void deleteClinicAssigment_Success() {
        when(clinicAssignmentRepository.existsById(10L)).thenReturn(true);
        clinicAssigmentService.deleteClinicAssigment(10L);
        verify(clinicAssignmentRepository).deleteById(10L);
    }

    @Test
    void deleteClinicAssigment_ThrowsIfNotExists() {
        when(clinicAssignmentRepository.existsById(10L)).thenReturn(false);
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> clinicAssigmentService.deleteClinicAssigment(10L));
        assertEquals("ClinicAssigment with id 10 does not exist", ex.getMessage());
    }

    @Test
    void assignClinic_Success() {
        when(clinicRepository.findById(2L)).thenReturn(Optional.of(clinic));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(clinicAssignmentRepository.save(validAssignment)).thenReturn(validAssignment);

        ClinicAssignment result = clinicAssigmentService.assignClinic(validAssignment);
        assertNotNull(result);
        assertEquals(clinic, result.getClinic());
        assertEquals(user, result.getUser());
        verify(clinicAssignmentRepository).save(validAssignment);
    }

    @Test
    void assignClinic_ThrowsIfNull() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> clinicAssigmentService.assignClinic(null));
        assertEquals("ClinicAssignment cannot be null", ex.getMessage());
    }

    @Test
    void assignClinic_ThrowsIfUserNull() {
        validAssignment.setUser(null);
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> clinicAssigmentService.assignClinic(validAssignment));
        assertEquals("User ID cannot be null", ex.getMessage());
    }

    @Test
    void assignClinic_ThrowsIfUserIdNull() {
        validAssignment.setUser(new User());
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> clinicAssigmentService.assignClinic(validAssignment));
        assertEquals("User ID cannot be null", ex.getMessage());
    }

    @Test
    void assignClinic_ThrowsIfClinicNull() {
        validAssignment.setClinic(null);
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> clinicAssigmentService.assignClinic(validAssignment));
        assertEquals("Clinic ID cannot be null", ex.getMessage());
    }

    @Test
    void assignClinic_ThrowsIfClinicIdNull() {
        validAssignment.setClinic(new Clinic());
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> clinicAssigmentService.assignClinic(validAssignment));
        assertEquals("Clinic ID cannot be null", ex.getMessage());
    }

    @Test
    void assignClinic_ThrowsIfStartTimeNull() {
        validAssignment.setStartTime(null);
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> clinicAssigmentService.assignClinic(validAssignment));
        assertEquals("Start time and end time cannot be null", ex.getMessage());
    }

    @Test
    void assignClinic_ThrowsIfEndTimeNull() {
        validAssignment.setEndTime(null);
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> clinicAssigmentService.assignClinic(validAssignment));
        assertEquals("Start time and end time cannot be null", ex.getMessage());
    }

    @Test
    void assignClinic_ThrowsIfClinicNotExists() {
        when(clinicRepository.findById(2L)).thenReturn(Optional.empty());
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> clinicAssigmentService.assignClinic(validAssignment));
        assertEquals("Clinic with id 2 does not exist", ex.getMessage());
    }

    @Test
    void assignClinic_ThrowsIfUserNotExists() {
        when(clinicRepository.findById(2L)).thenReturn(Optional.of(clinic));
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> clinicAssigmentService.assignClinic(validAssignment));
        assertEquals("User with id 1 does not exist", ex.getMessage());
    }
}
