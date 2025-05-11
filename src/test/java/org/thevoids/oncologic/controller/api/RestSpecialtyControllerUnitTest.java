package org.thevoids.oncologic.controller.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.thevoids.oncologic.dto.entity.SpecialtyDTO;
import org.thevoids.oncologic.entity.Specialty;
import org.thevoids.oncologic.exception.ResourceNotFoundException;
import org.thevoids.oncologic.mapper.SpecialtyMapper;
import org.thevoids.oncologic.service.SpecialtyService;

class RestSpecialtyControllerUnitTest {

    @Mock
    private SpecialtyService specialtyService;

    @Mock
    private SpecialtyMapper specialtyMapper;

    @InjectMocks
    private RestSpecialtyController restSpecialtyController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllSpecialties_ReturnsSpecialtyList() {
        // Arrange
        Specialty specialty = new Specialty();
        specialty.setSpecialtyId(1L);
        specialty.setSpecialtyName("Oncología");

        SpecialtyDTO specialtyDTO = new SpecialtyDTO();
        specialtyDTO.setSpecialtyId(1L);
        specialtyDTO.setSpecialtyName("Oncología");

        when(specialtyService.getAllSpecialties()).thenReturn(List.of(specialty));
        when(specialtyMapper.toSpecialtyDTO(specialty)).thenReturn(specialtyDTO);

        // Act
        ResponseEntity<List<SpecialtyDTO>> response = restSpecialtyController.getAllSpecialties();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<SpecialtyDTO> specialties = response.getBody();
        assertNotNull(specialties);
        assertEquals(1, specialties.size());
        assertEquals("Oncología", specialties.get(0).getSpecialtyName());
        verify(specialtyService, times(1)).getAllSpecialties();
    }

    @Test
    void getSpecialtyById_SpecialtyExists_ReturnsSpecialty() {
        // Arrange
        Long specialtyId = 1L;
        Specialty specialty = new Specialty();
        specialty.setSpecialtyId(specialtyId);
        specialty.setSpecialtyName("Oncología");

        SpecialtyDTO specialtyDTO = new SpecialtyDTO();
        specialtyDTO.setSpecialtyId(specialtyId);
        specialtyDTO.setSpecialtyName("Oncología");

        when(specialtyService.getSpecialtyById(specialtyId)).thenReturn(specialty);
        when(specialtyMapper.toSpecialtyDTO(specialty)).thenReturn(specialtyDTO);

        // Act
        ResponseEntity<SpecialtyDTO> response = restSpecialtyController.getSpecialtyById(specialtyId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        SpecialtyDTO retrievedSpecialty = response.getBody();
        assertNotNull(retrievedSpecialty);
        assertEquals(specialtyId, retrievedSpecialty.getSpecialtyId());
        assertEquals("Oncología", retrievedSpecialty.getSpecialtyName());
        verify(specialtyService, times(1)).getSpecialtyById(specialtyId);
    }

    @Test
    void getSpecialtyById_SpecialtyNotFound_ReturnsNotFound() {
        // Arrange
        Long specialtyId = 1L;
        when(specialtyService.getSpecialtyById(specialtyId))
            .thenThrow(new ResourceNotFoundException("Especialidad", "id", specialtyId));

        // Act
        ResponseEntity<SpecialtyDTO> response = restSpecialtyController.getSpecialtyById(specialtyId);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(specialtyService, times(1)).getSpecialtyById(specialtyId);
    }

    @Test
    void createSpecialty_ValidSpecialty_ReturnsCreatedSpecialty() {
        // Arrange
        SpecialtyDTO specialtyDTO = new SpecialtyDTO();
        specialtyDTO.setSpecialtyName("Oncología");

        Specialty specialty = new Specialty();
        specialty.setSpecialtyName("Oncología");

        Specialty createdSpecialty = new Specialty();
        createdSpecialty.setSpecialtyId(1L);
        createdSpecialty.setSpecialtyName("Oncología");

        SpecialtyDTO createdSpecialtyDTO = new SpecialtyDTO();
        createdSpecialtyDTO.setSpecialtyId(1L);
        createdSpecialtyDTO.setSpecialtyName("Oncología");

        when(specialtyMapper.toSpecialty(specialtyDTO)).thenReturn(specialty);
        when(specialtyService.createSpecialty(specialty)).thenReturn(createdSpecialty);
        when(specialtyMapper.toSpecialtyDTO(createdSpecialty)).thenReturn(createdSpecialtyDTO);

        // Act
        ResponseEntity<SpecialtyDTO> response = restSpecialtyController.createSpecialty(specialtyDTO);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        SpecialtyDTO returnedSpecialty = response.getBody();
        assertNotNull(returnedSpecialty);
        assertEquals(1L, returnedSpecialty.getSpecialtyId());
        assertEquals("Oncología", returnedSpecialty.getSpecialtyName());
        verify(specialtyService, times(1)).createSpecialty(any(Specialty.class));
    }

    @Test
    void updateSpecialty_ValidSpecialty_ReturnsUpdatedSpecialty() {
        // Arrange
        Long specialtyId = 1L;
        SpecialtyDTO specialtyDTO = new SpecialtyDTO();
        specialtyDTO.setSpecialtyName("Oncología Actualizada");

        Specialty specialty = new Specialty();
        specialty.setSpecialtyId(specialtyId);
        specialty.setSpecialtyName("Oncología Actualizada");

        when(specialtyMapper.toSpecialty(specialtyDTO)).thenReturn(specialty);
        when(specialtyService.updateSpecialty(specialty)).thenReturn(specialty);
        when(specialtyMapper.toSpecialtyDTO(specialty)).thenReturn(specialtyDTO);

        // Act
        ResponseEntity<SpecialtyDTO> response = restSpecialtyController.updateSpecialty(specialtyId, specialtyDTO);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        SpecialtyDTO updatedSpecialty = response.getBody();
        assertNotNull(updatedSpecialty);
        assertEquals("Oncología Actualizada", updatedSpecialty.getSpecialtyName());
        verify(specialtyService, times(1)).updateSpecialty(any(Specialty.class));
    }

    @Test
    void deleteSpecialty_ExistingSpecialty_ReturnsOk() {
        // Arrange
        Long specialtyId = 1L;
        doNothing().when(specialtyService).deleteSpecialty(specialtyId);

        // Act
        ResponseEntity<Void> response = restSpecialtyController.deleteSpecialty(specialtyId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(specialtyService, times(1)).deleteSpecialty(specialtyId);
    }

    @Test
    void deleteSpecialty_NonExistingSpecialty_ReturnsNotFound() {
        // Arrange
        Long specialtyId = 1L;
        doThrow(new ResourceNotFoundException("Especialidad", "id", specialtyId))
            .when(specialtyService).deleteSpecialty(specialtyId);

        // Act
        ResponseEntity<Void> response = restSpecialtyController.deleteSpecialty(specialtyId);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(specialtyService, times(1)).deleteSpecialty(specialtyId);
    }
} 