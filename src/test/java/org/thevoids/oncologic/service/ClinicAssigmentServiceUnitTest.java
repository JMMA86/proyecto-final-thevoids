package org.thevoids.oncologic.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.thevoids.oncologic.entity.Clinic;
import org.thevoids.oncologic.entity.ClinicAssignment;
import org.thevoids.oncologic.entity.User;
import org.thevoids.oncologic.repository.ClinicAssignmentRepository;
import org.thevoids.oncologic.repository.ClinicRepository;
import org.thevoids.oncologic.repository.UserRepository;
import org.thevoids.oncologic.service.impl.ClinicAssigmentImpl;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

    @Test
    void getAllClinicAssignmentsReturnsAllAssignments() {
        List<ClinicAssignment> expectedAssignments = List.of(
                new ClinicAssignment(),
                new ClinicAssignment()
        );

        when(clinicAssignmentRepository.findAll()).thenReturn(expectedAssignments);

        List<ClinicAssignment> result = clinicAssigmentService.getAllClinicAssignments();

        assertEquals(expectedAssignments, result);
        assertEquals(2, result.size());
    }

    @Test
    void getClinicAssignmentByIdReturnsAssignmentWhenExists() {
        Long id = 1L;
        ClinicAssignment expectedAssignment = new ClinicAssignment();
        expectedAssignment.setId(id);

        when(clinicAssignmentRepository.findById(id)).thenReturn(Optional.of(expectedAssignment));

        ClinicAssignment result = clinicAssigmentService.getClinicAssignmentById(id);

        assertNotNull(result);
        assertEquals(id, result.getId());
    }

    @Test
    void getClinicAssignmentByIdReturnsNullWhenNotExists() {
        Long id = 1L;

        when(clinicAssignmentRepository.findById(id)).thenReturn(Optional.empty());

        ClinicAssignment result = clinicAssigmentService.getClinicAssignmentById(id);

        assertNull(result);
    }

    @Test
    void updateClinicAssignmentSuccessfullyUpdatesAssignment() {
        Long id = 1L;
        ClinicAssignment assignment = new ClinicAssignment();
        assignment.setId(id);

        when(clinicAssignmentRepository.existsById(id)).thenReturn(true);
        when(clinicAssignmentRepository.save(assignment)).thenReturn(assignment);

        ClinicAssignment result = clinicAssigmentService.updateClinicAssignment(assignment);

        assertNotNull(result);
        assertEquals(assignment, result);
    }

    @Test
    void updateClinicAssignmentThrowsExceptionWhenAssignmentIsNull() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                clinicAssigmentService.updateClinicAssignment(null));

        assertEquals("ClinicAssigment cannot be null", exception.getMessage());
    }

    @Test
    void updateClinicAssignmentThrowsExceptionWhenIdIsNull() {
        ClinicAssignment assignment = new ClinicAssignment();

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                clinicAssigmentService.updateClinicAssignment(assignment));

        assertEquals("ClinicAssigment ID cannot be null", exception.getMessage());
    }

    @Test
    void updateClinicAssignmentThrowsExceptionWhenAssignmentDoesNotExist() {
        Long id = 1L;
        ClinicAssignment assignment = new ClinicAssignment();
        assignment.setId(id);

        when(clinicAssignmentRepository.existsById(id)).thenReturn(false);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                clinicAssigmentService.updateClinicAssignment(assignment));

        assertEquals("ClinicAssigment with id 1 does not exist", exception.getMessage());
    }

    @Test
    void deleteClinicAssigmentSuccessfullyDeletesAssignment() {
        Long id = 1L;

        when(clinicAssignmentRepository.existsById(id)).thenReturn(true);

        clinicAssigmentService.deleteClinicAssigment(id);

        verify(clinicAssignmentRepository).deleteById(id);
    }

    @Test
    void deleteClinicAssigmentThrowsExceptionWhenAssignmentDoesNotExist() {
        Long id = 1L;

        when(clinicAssignmentRepository.existsById(id)).thenReturn(false);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                clinicAssigmentService.deleteClinicAssigment(id));

        assertEquals("ClinicAssigment with id 1 does not exist", exception.getMessage());
    }

    @Test
    void assignClinicSuccessfullyAssignsClinicToUser() {
        Long userId = 1L;
        Long clinicId = 2L;

        User user = new User();
        user.setUserId(userId);

        Clinic clinic = new Clinic();
        clinic.setId(clinicId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(clinicRepository.findById(clinicId)).thenReturn(Optional.of(clinic));

        clinicAssigmentService.assignClinic(userId, clinicId);

        ArgumentCaptor<ClinicAssignment> captor = ArgumentCaptor.forClass(ClinicAssignment.class);
        verify(clinicAssignmentRepository).save(captor.capture());

        ClinicAssignment savedAssignment = captor.getValue();
        assertEquals(user, savedAssignment.getUser());
        assertEquals(clinic, savedAssignment.getClinic());
    }

    @Test
    void assignClinicThrowsExceptionWhenUserIdIsNull() {
        Long clinicId = 2L;

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                clinicAssigmentService.assignClinic(null, clinicId));

        assertEquals("User ID cannot be null", exception.getMessage());
    }

    @Test
    void assignClinicThrowsExceptionWhenClinicIdIsNull() {
        Long userId = 1L;

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                clinicAssigmentService.assignClinic(userId, null));

        assertEquals("Clinic ID cannot be null", exception.getMessage());
    }

    @Test
    void assignClinicThrowsExceptionWhenUserDoesNotExist() {
        Long userId = 1L;
        Long clinicId = 2L;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        when(clinicRepository.findById(clinicId)).thenReturn(Optional.of(new Clinic()));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                clinicAssigmentService.assignClinic(userId, clinicId));

        assertEquals("User with id 1 does not exist", exception.getMessage());
    }

    @Test
    void assignClinicThrowsExceptionWhenClinicDoesNotExist() {
        Long userId = 1L;
        Long clinicId = 2L;

        when(clinicRepository.findById(clinicId)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                clinicAssigmentService.assignClinic(userId, clinicId));

        assertEquals("Clinic with id 2 does not exist", exception.getMessage());
    }
}
