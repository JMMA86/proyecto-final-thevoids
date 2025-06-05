package org.thevoids.oncologic.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.thevoids.oncologic.entity.Specialty;
import org.thevoids.oncologic.entity.User;
import org.thevoids.oncologic.entity.UserSpecialty;
import org.thevoids.oncologic.repository.SpecialtyRepository;
import org.thevoids.oncologic.repository.UserRepository;
import org.thevoids.oncologic.repository.UserSpecialtyRepository;
import org.thevoids.oncologic.service.impl.UserSpecialtyServiceImpl;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserSpecialtyServiceUnitTest {

    @Mock
    private UserSpecialtyRepository userSpecialtyRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private SpecialtyRepository specialtyRepository;

    @InjectMocks
    private UserSpecialtyServiceImpl userSpecialtyService;

    @Test
    void getAllUserSpecialtiesReturnsAllUserSpecialties() {
        List<UserSpecialty> expectedUserSpecialties = List.of(
                createUserSpecialty(1L),
                createUserSpecialty(2L));

        when(userSpecialtyRepository.findAll()).thenReturn(expectedUserSpecialties);

        List<UserSpecialty> result = userSpecialtyService.getAllUserSpecialties();

        assertEquals(2, result.size());
        assertEquals(expectedUserSpecialties, result);
    }

    @Test
    void getUserSpecialtyByIdReturnsUserSpecialtyWhenExists() {
        Long id = 1L;
        UserSpecialty expectedUserSpecialty = createUserSpecialty(id);

        when(userSpecialtyRepository.findById(id)).thenReturn(Optional.of(expectedUserSpecialty));

        UserSpecialty result = userSpecialtyService.getUserSpecialtyById(id);

        assertNotNull(result);
        assertEquals(id, result.getId());
    }

    @Test
    void getUserSpecialtyByIdThrowsExceptionWhenNotExists() {
        Long id = 1L;

        when(userSpecialtyRepository.findById(id)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> userSpecialtyService.getUserSpecialtyById(id));

        assertEquals("UserSpecialty with id 1 does not exist", exception.getMessage());
    }

    @Test
    void addSpecialtyToUserSuccessfullyAddsSpecialty() {
        Long userId = 1L;
        Long specialtyId = 2L;

        User user = new User();
        user.setUserId(userId);

        Specialty specialty = new Specialty();
        specialty.setSpecialtyId(specialtyId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(specialtyRepository.findById(specialtyId)).thenReturn(Optional.of(specialty));

        userSpecialtyService.addSpecialtyToUser(userId, specialtyId);

        ArgumentCaptor<UserSpecialty> userSpecialtyCaptor = ArgumentCaptor.forClass(UserSpecialty.class);
        verify(userSpecialtyRepository).save(userSpecialtyCaptor.capture());

        UserSpecialty savedUserSpecialty = userSpecialtyCaptor.getValue();
        assertEquals(user, savedUserSpecialty.getUser());
        assertEquals(specialty, savedUserSpecialty.getSpecialty());
    }

    @Test
    void addSpecialtyToUserThrowsExceptionWhenUserIdIsNull() {
        Long specialtyId = 2L;

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> userSpecialtyService.addSpecialtyToUser(null, specialtyId));

        assertEquals("User ID and Specialty ID cannot be null", exception.getMessage());
        verify(userSpecialtyRepository, never()).save(any());
    }

    @Test
    void addSpecialtyToUserThrowsExceptionWhenSpecialtyIdIsNull() {
        Long userId = 1L;

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> userSpecialtyService.addSpecialtyToUser(userId, null));

        assertEquals("User ID and Specialty ID cannot be null", exception.getMessage());
        verify(userSpecialtyRepository, never()).save(any());
    }

    @Test
    void addSpecialtyToUserThrowsExceptionWhenUserDoesNotExist() {
        Long userId = 1L;
        Long specialtyId = 2L;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> userSpecialtyService.addSpecialtyToUser(userId, specialtyId));

        assertEquals("User with id 1 does not exist", exception.getMessage());
        verify(userSpecialtyRepository, never()).save(any());
    }

    @Test
    void addSpecialtyToUserThrowsExceptionWhenSpecialtyDoesNotExist() {
        Long userId = 1L;
        Long specialtyId = 2L;

        User user = new User();
        user.setUserId(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(specialtyRepository.findById(specialtyId)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> userSpecialtyService.addSpecialtyToUser(userId, specialtyId));

        assertEquals("Specialty with id 2 does not exist", exception.getMessage());
        verify(userSpecialtyRepository, never()).save(any());
    }

    @Test
    void updateUserSpecialtySuccessfullyUpdatesUserSpecialty() {
        Long id = 1L;
        UserSpecialty userSpecialty = createUserSpecialty(id);

        when(userSpecialtyRepository.existsById(id)).thenReturn(true);
        when(userSpecialtyRepository.save(userSpecialty)).thenReturn(userSpecialty);

        UserSpecialty result = userSpecialtyService.updateUserSpecialty(userSpecialty);

        assertNotNull(result);
        assertEquals(userSpecialty, result);
        verify(userSpecialtyRepository).save(userSpecialty);
    }

    @Test
    void updateUserSpecialtyThrowsExceptionWhenUserSpecialtyIsNull() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> userSpecialtyService.updateUserSpecialty(null));

        assertEquals("UserSpecialty cannot be null", exception.getMessage());
        verify(userSpecialtyRepository, never()).save(any());
    }

    @Test
    void updateUserSpecialtyThrowsExceptionWhenIdIsNull() {
        UserSpecialty userSpecialty = new UserSpecialty();

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> userSpecialtyService.updateUserSpecialty(userSpecialty));

        assertEquals("UserSpecialty ID cannot be null", exception.getMessage());
        verify(userSpecialtyRepository, never()).save(any());
    }

    @Test
    void updateUserSpecialtyThrowsExceptionWhenUserSpecialtyDoesNotExist() {
        Long id = 1L;
        UserSpecialty userSpecialty = createUserSpecialty(id);

        when(userSpecialtyRepository.existsById(id)).thenReturn(false);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> userSpecialtyService.updateUserSpecialty(userSpecialty));

        assertEquals("UserSpecialty with id 1 does not exist", exception.getMessage());
        verify(userSpecialtyRepository, never()).save(any());
    }

    @Test
    void deleteUserSpecialtySuccessfullyDeletesUserSpecialty() {
        Long id = 1L;

        when(userSpecialtyRepository.existsById(id)).thenReturn(true);

        userSpecialtyService.deleteUserSpecialty(id);

        verify(userSpecialtyRepository).deleteById(id);
    }

    @Test
    void deleteUserSpecialtyThrowsExceptionWhenUserSpecialtyDoesNotExist() {
        Long id = 1L;

        when(userSpecialtyRepository.existsById(id)).thenReturn(false);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> userSpecialtyService.deleteUserSpecialty(id));

        assertEquals("UserSpecialty with id 1 does not exist", exception.getMessage());
        verify(userSpecialtyRepository, never()).deleteById(any());
    }

    @Test
    void getUserSpecialtiesByUserIdReturnsListWhenUserHasSpecialties() {
        Long userId = 1L;
        List<UserSpecialty> expectedUserSpecialties = List.of(
                createUserSpecialtyWithUserId(1L, userId),
                createUserSpecialtyWithUserId(2L, userId));

        when(userSpecialtyRepository.findByUser_UserId(userId)).thenReturn(expectedUserSpecialties);

        List<UserSpecialty> result = userSpecialtyService.getUserSpecialtiesByUserId(userId);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(expectedUserSpecialties, result);
        verify(userSpecialtyRepository).findByUser_UserId(userId);
    }

    @Test
    void getUserSpecialtiesByUserIdReturnsEmptyListWhenUserHasNoSpecialties() {
        Long userId = 1L;
        List<UserSpecialty> expectedUserSpecialties = List.of();

        when(userSpecialtyRepository.findByUser_UserId(userId)).thenReturn(expectedUserSpecialties);

        List<UserSpecialty> result = userSpecialtyService.getUserSpecialtiesByUserId(userId);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(userSpecialtyRepository).findByUser_UserId(userId);
    }

    @Test
    void getUserSpecialtiesByUserIdThrowsExceptionWhenUserIdIsNull() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> userSpecialtyService.getUserSpecialtiesByUserId(null));

        assertEquals("User ID cannot be null", exception.getMessage());
        verify(userSpecialtyRepository, never()).findByUser_UserId(any());
    }

    @Test
    void getUserSpecialtyByUserIdReturnsOptionalWithValueWhenUserHasSpecialty() {
        Long userId = 1L;
        UserSpecialty expectedUserSpecialty = createUserSpecialtyWithUserId(1L, userId);

        when(userSpecialtyRepository.findFirstByUser_UserId(userId)).thenReturn(Optional.of(expectedUserSpecialty));

        Optional<UserSpecialty> result = userSpecialtyService.getUserSpecialtyByUserId(userId);

        assertTrue(result.isPresent());
        assertEquals(expectedUserSpecialty, result.get());
        verify(userSpecialtyRepository).findFirstByUser_UserId(userId);
    }

    @Test
    void getUserSpecialtyByUserIdReturnsEmptyOptionalWhenUserHasNoSpecialty() {
        Long userId = 1L;

        when(userSpecialtyRepository.findFirstByUser_UserId(userId)).thenReturn(Optional.empty());

        Optional<UserSpecialty> result = userSpecialtyService.getUserSpecialtyByUserId(userId);

        assertFalse(result.isPresent());
        verify(userSpecialtyRepository).findFirstByUser_UserId(userId);
    }

    @Test
    void getUserSpecialtyByUserIdThrowsExceptionWhenUserIdIsNull() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> userSpecialtyService.getUserSpecialtyByUserId(null));

        assertEquals("User ID cannot be null", exception.getMessage());
        verify(userSpecialtyRepository, never()).findFirstByUser_UserId(any());
    }

    private UserSpecialty createUserSpecialty(Long id) {
        UserSpecialty userSpecialty = new UserSpecialty();
        userSpecialty.setId(id);
        return userSpecialty;
    }

    private UserSpecialty createUserSpecialtyWithUserId(Long id, Long userId) {
        UserSpecialty userSpecialty = new UserSpecialty();
        userSpecialty.setId(id);

        User user = new User();
        user.setUserId(userId);
        userSpecialty.setUser(user);

        return userSpecialty;
    }
}