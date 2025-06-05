package org.thevoids.oncologic.controller.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.thevoids.oncologic.dto.entity.UserSpecialtyDTO;
import org.thevoids.oncologic.entity.Specialty;
import org.thevoids.oncologic.entity.User;
import org.thevoids.oncologic.entity.UserSpecialty;
import org.thevoids.oncologic.exception.ResourceNotFoundException;
import org.thevoids.oncologic.mapper.UserSpecialtyMapper;
import org.thevoids.oncologic.service.UserSpecialtyService;

class RestUserSpecialtyControllerUnitTest {

    @Mock
    private UserSpecialtyService userSpecialtyService;

    @Mock
    private UserSpecialtyMapper userSpecialtyMapper;

    @InjectMocks
    private RestUserSpecialtyController restUserSpecialtyController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllUserSpecialties_ReturnsUserSpecialtyList() {
        // Arrange
        User user = new User();
        user.setUserId(1L);
        user.setFullName("John Doe");

        Specialty specialty = new Specialty();
        specialty.setSpecialtyId(1L);
        specialty.setSpecialtyName("Oncología");

        UserSpecialty userSpecialty = new UserSpecialty();
        userSpecialty.setId(1L);
        userSpecialty.setUser(user);
        userSpecialty.setSpecialty(specialty);

        UserSpecialtyDTO userSpecialtyDTO = new UserSpecialtyDTO(1L, 1L, 1L, "John Doe", "Oncología");

        when(userSpecialtyService.getAllUserSpecialties()).thenReturn(List.of(userSpecialty));
        when(userSpecialtyMapper.toUserSpecialtyDTO(userSpecialty)).thenReturn(userSpecialtyDTO);

        // Act
        ResponseEntity<List<UserSpecialtyDTO>> response = restUserSpecialtyController.getAllUserSpecialties();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<UserSpecialtyDTO> userSpecialties = response.getBody();
        assertNotNull(userSpecialties);
        assertEquals(1, userSpecialties.size());
        assertEquals("John Doe", userSpecialties.get(0).getUserName());
        assertEquals("Oncología", userSpecialties.get(0).getSpecialtyName());
        verify(userSpecialtyService, times(1)).getAllUserSpecialties();
    }

    @Test
    void getUserSpecialtyById_UserSpecialtyExists_ReturnsUserSpecialty() {
        // Arrange
        Long userSpecialtyId = 1L;
        User user = new User();
        user.setUserId(1L);
        user.setFullName("John Doe");

        Specialty specialty = new Specialty();
        specialty.setSpecialtyId(1L);
        specialty.setSpecialtyName("Oncología");

        UserSpecialty userSpecialty = new UserSpecialty();
        userSpecialty.setId(userSpecialtyId);
        userSpecialty.setUser(user);
        userSpecialty.setSpecialty(specialty);

        UserSpecialtyDTO userSpecialtyDTO = new UserSpecialtyDTO(1L, 1L, 1L, "John Doe", "Oncología");

        when(userSpecialtyService.getUserSpecialtyById(userSpecialtyId)).thenReturn(userSpecialty);
        when(userSpecialtyMapper.toUserSpecialtyDTO(userSpecialty)).thenReturn(userSpecialtyDTO);

        // Act
        ResponseEntity<UserSpecialtyDTO> response = restUserSpecialtyController.getUserSpecialtyById(userSpecialtyId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        UserSpecialtyDTO retrievedUserSpecialty = response.getBody();
        assertNotNull(retrievedUserSpecialty);
        assertEquals(userSpecialtyId, retrievedUserSpecialty.getId());
        assertEquals("John Doe", retrievedUserSpecialty.getUserName());
        assertEquals("Oncología", retrievedUserSpecialty.getSpecialtyName());
        verify(userSpecialtyService, times(1)).getUserSpecialtyById(userSpecialtyId);
    }

    @Test
    void getUserSpecialtyById_UserSpecialtyNotFound_ReturnsNotFound() {
        // Arrange
        Long userSpecialtyId = 1L;
        when(userSpecialtyService.getUserSpecialtyById(userSpecialtyId))
                .thenThrow(new ResourceNotFoundException("UserSpecialty", "id", userSpecialtyId));

        // Act
        ResponseEntity<UserSpecialtyDTO> response = restUserSpecialtyController.getUserSpecialtyById(userSpecialtyId);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(userSpecialtyService, times(1)).getUserSpecialtyById(userSpecialtyId);
    }

    @Test
    void getUserSpecialtyByUserId_UserHasSpecialty_ReturnsUserSpecialty() {
        // Arrange
        Long userId = 1L;
        User user = new User();
        user.setUserId(userId);
        user.setFullName("John Doe");

        Specialty specialty = new Specialty();
        specialty.setSpecialtyId(1L);
        specialty.setSpecialtyName("Oncología");

        UserSpecialty userSpecialty = new UserSpecialty();
        userSpecialty.setId(1L);
        userSpecialty.setUser(user);
        userSpecialty.setSpecialty(specialty);

        UserSpecialtyDTO userSpecialtyDTO = new UserSpecialtyDTO(1L, userId, 1L, "John Doe", "Oncología");

        when(userSpecialtyService.getUserSpecialtyByUserId(userId)).thenReturn(Optional.of(userSpecialty));
        when(userSpecialtyMapper.toUserSpecialtyDTO(userSpecialty)).thenReturn(userSpecialtyDTO);

        // Act
        ResponseEntity<UserSpecialtyDTO> response = restUserSpecialtyController.getUserSpecialtyByUserId(userId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        UserSpecialtyDTO retrievedUserSpecialty = response.getBody();
        assertNotNull(retrievedUserSpecialty);
        assertEquals(userId, retrievedUserSpecialty.getUserId());
        assertEquals("John Doe", retrievedUserSpecialty.getUserName());
        assertEquals("Oncología", retrievedUserSpecialty.getSpecialtyName());
        verify(userSpecialtyService, times(1)).getUserSpecialtyByUserId(userId);
        verify(userSpecialtyMapper, times(1)).toUserSpecialtyDTO(userSpecialty);
    }

    @Test
    void getUserSpecialtyByUserId_UserHasNoSpecialty_ReturnsNotFound() {
        // Arrange
        Long userId = 1L;

        when(userSpecialtyService.getUserSpecialtyByUserId(userId)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<UserSpecialtyDTO> response = restUserSpecialtyController.getUserSpecialtyByUserId(userId);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(userSpecialtyService, times(1)).getUserSpecialtyByUserId(userId);
    }

    @Test
    void getUserSpecialtyByUserId_ServiceThrowsException_ReturnsInternalServerError() {
        // Arrange
        Long userId = 1L;
        when(userSpecialtyService.getUserSpecialtyByUserId(userId))
                .thenThrow(new RuntimeException("Database connection error"));

        // Act
        ResponseEntity<UserSpecialtyDTO> response = restUserSpecialtyController.getUserSpecialtyByUserId(userId);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        verify(userSpecialtyService, times(1)).getUserSpecialtyByUserId(userId);
    }

    @Test
    void assignSpecialtyToUser_Success() {
        // Arrange
        Long userId = 1L;
        Long specialtyId = 1L;
        doNothing().when(userSpecialtyService).addSpecialtyToUser(userId, specialtyId);

        // Act
        ResponseEntity<UserSpecialtyDTO> response = restUserSpecialtyController.assignSpecialtyToUser(userId,
                specialtyId);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        verify(userSpecialtyService, times(1)).addSpecialtyToUser(userId, specialtyId);
    }

    @Test
    void assignSpecialtyToUser_UserNotFound_ReturnsNotFound() {
        // Arrange
        Long userId = 1L;
        Long specialtyId = 1L;
        doThrow(new ResourceNotFoundException("Usuario", "id", userId))
                .when(userSpecialtyService).addSpecialtyToUser(userId, specialtyId);

        // Act
        ResponseEntity<UserSpecialtyDTO> response = restUserSpecialtyController.assignSpecialtyToUser(userId,
                specialtyId);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(userSpecialtyService, times(1)).addSpecialtyToUser(userId, specialtyId);
    }

    @Test
    void deleteUserSpecialty_ExistingUserSpecialty_ReturnsOk() {
        // Arrange
        Long userSpecialtyId = 1L;
        doNothing().when(userSpecialtyService).deleteUserSpecialty(userSpecialtyId);

        // Act
        ResponseEntity<Void> response = restUserSpecialtyController.deleteUserSpecialty(userSpecialtyId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(userSpecialtyService, times(1)).deleteUserSpecialty(userSpecialtyId);
    }

    @Test
    void deleteUserSpecialty_NonExistingUserSpecialty_ReturnsNotFound() {
        // Arrange
        Long userSpecialtyId = 1L;
        doThrow(new ResourceNotFoundException("UserSpecialty", "id", userSpecialtyId))
                .when(userSpecialtyService).deleteUserSpecialty(userSpecialtyId);

        // Act
        ResponseEntity<Void> response = restUserSpecialtyController.deleteUserSpecialty(userSpecialtyId);

        // Assert assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(userSpecialtyService, times(1)).deleteUserSpecialty(userSpecialtyId);
    }
}