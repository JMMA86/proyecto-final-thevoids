package org.thevoids.oncologic.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.thevoids.oncologic.entity.Specialty;
import org.thevoids.oncologic.repository.SpecialtyRepository;
import org.thevoids.oncologic.service.impl.SpecialtyServiceImpl;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SpecialtyServiceUnitTest {

    @Mock
    private SpecialtyRepository specialtyRepository;

    @InjectMocks
    private SpecialtyServiceImpl specialtyService;

    @Test
    void getAllSpecialtiesReturnsAllSpecialties() {
        List<Specialty> expectedSpecialties = List.of(
                createSpecialty(1L, "Oncology"),
                createSpecialty(2L, "Cardiology")
        );

        when(specialtyRepository.findAll()).thenReturn(expectedSpecialties);

        List<Specialty> result = specialtyService.getAllSpecialties();

        assertEquals(2, result.size());
        assertEquals(expectedSpecialties, result);
    }

    @Test
    void getSpecialtyByIdReturnsSpecialtyWhenExists() {
        Long id = 1L;
        Specialty expectedSpecialty = createSpecialty(id, "Oncology");

        when(specialtyRepository.findById(id)).thenReturn(Optional.of(expectedSpecialty));

        Specialty result = specialtyService.getSpecialtyById(id);

        assertNotNull(result);
        assertEquals(id, result.getSpecialtyId());
        assertEquals("Oncology", result.getSpecialtyName());
    }

    @Test
    void getSpecialtyByIdThrowsExceptionWhenNotExists() {
        Long id = 1L;

        when(specialtyRepository.findById(id)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                specialtyService.getSpecialtyById(id));

        assertEquals("Specialty with id 1 does not exist", exception.getMessage());
    }

    @Test
    void createSpecialtySuccessfullyCreatesSpecialty() {
        Specialty specialty = createSpecialty(null, "Neurology");
        Specialty savedSpecialty = createSpecialty(1L, "Neurology");

        when(specialtyRepository.save(specialty)).thenReturn(savedSpecialty);

        Specialty result = specialtyService.createSpecialty(specialty);

        assertNotNull(result);
        assertEquals(savedSpecialty, result);
        verify(specialtyRepository).save(specialty);
    }

    @Test
    void createSpecialtyThrowsExceptionWhenSpecialtyIsNull() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                specialtyService.createSpecialty(null));

        assertEquals("Specialty cannot be null", exception.getMessage());
        verify(specialtyRepository, never()).save(any());
    }

    @Test
    void updateSpecialtySuccessfullyUpdatesSpecialty() {
        Long id = 1L;
        Specialty specialty = createSpecialty(id, "Updated Specialty");

        when(specialtyRepository.existsById(id)).thenReturn(true);
        when(specialtyRepository.save(specialty)).thenReturn(specialty);

        Specialty result = specialtyService.updateSpecialty(specialty);

        assertNotNull(result);
        assertEquals(specialty, result);
        verify(specialtyRepository).save(specialty);
    }

    @Test
    void updateSpecialtyThrowsExceptionWhenSpecialtyIsNull() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                specialtyService.updateSpecialty(null));

        assertEquals("Specialty cannot be null", exception.getMessage());
        verify(specialtyRepository, never()).save(any());
    }

    @Test
    void updateSpecialtyThrowsExceptionWhenIdIsNull() {
        Specialty specialty = createSpecialty(null, "No ID Specialty");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                specialtyService.updateSpecialty(specialty));

        assertEquals("Specialty ID cannot be null", exception.getMessage());
        verify(specialtyRepository, never()).save(any());
    }

    @Test
    void updateSpecialtyThrowsExceptionWhenSpecialtyDoesNotExist() {
        Long id = 1L;
        Specialty specialty = createSpecialty(id, "Non-existent Specialty");

        when(specialtyRepository.existsById(id)).thenReturn(false);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                specialtyService.updateSpecialty(specialty));

        assertEquals("Specialty with id 1 does not exist", exception.getMessage());
        verify(specialtyRepository, never()).save(any());
    }

    @Test
    void deleteSpecialtySuccessfullyDeletesSpecialty() {
        Long id = 1L;

        when(specialtyRepository.existsById(id)).thenReturn(true);

        specialtyService.deleteSpecialty(id);

        verify(specialtyRepository).deleteById(id);
    }

    @Test
    void deleteSpecialtyThrowsExceptionWhenSpecialtyDoesNotExist() {
        Long id = 1L;

        when(specialtyRepository.existsById(id)).thenReturn(false);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                specialtyService.deleteSpecialty(id));

        assertEquals("Specialty with id 1 does not exist", exception.getMessage());
        verify(specialtyRepository, never()).deleteById(any());
    }

    private Specialty createSpecialty(Long id, String name) {
        Specialty specialty = new Specialty();
        specialty.setSpecialtyId(id);
        specialty.setSpecialtyName(name);
        return specialty;
    }
}