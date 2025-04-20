package org.thevoids.oncologic.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.thevoids.oncologic.entity.MedicalHistory;
import org.thevoids.oncologic.repository.MedicalHistoryRepository;
import org.thevoids.oncologic.service.impl.MedicalHistoryServiceImpl;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MedicalHistoryServiceUnitTest {

    @Mock
    private MedicalHistoryRepository medicalHistoryRepository;

    @InjectMocks
    private MedicalHistoryServiceImpl medicalHistoryService;

    @Test
    void getAllMedicalHistoriesReturnsAllHistories() {
        List<MedicalHistory> expectedHistories = List.of(
                createMedicalHistory(1L),
                createMedicalHistory(2L)
        );

        when(medicalHistoryRepository.findAll()).thenReturn(expectedHistories);

        List<MedicalHistory> result = medicalHistoryService.getAllMedicalHistories();

        assertEquals(2, result.size());
        assertEquals(expectedHistories, result);
    }

    @Test
    void getMedicalHistoryByIdReturnsHistoryWhenExists() {
        Long id = 1L;
        MedicalHistory expectedHistory = createMedicalHistory(id);

        when(medicalHistoryRepository.findById(id)).thenReturn(Optional.of(expectedHistory));

        MedicalHistory result = medicalHistoryService.getMedicalHistoryById(id);

        assertNotNull(result);
        assertEquals(id, result.getHistoryId());
    }

    @Test
    void getMedicalHistoryByIdThrowsExceptionWhenNotExists() {
        Long id = 1L;

        when(medicalHistoryRepository.findById(id)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                medicalHistoryService.getMedicalHistoryById(id));

        assertEquals("MedicalHistory with id 1 does not exist", exception.getMessage());
    }

    @Test
    void createMedicalHistorySuccessfullyCreatesMedicalHistory() {
        MedicalHistory medicalHistory = createMedicalHistory(null);
        MedicalHistory savedMedicalHistory = createMedicalHistory(1L);

        when(medicalHistoryRepository.save(medicalHistory)).thenReturn(savedMedicalHistory);

        MedicalHistory result = medicalHistoryService.createMedicalHistory(medicalHistory);

        assertNotNull(result);
        assertEquals(savedMedicalHistory, result);
        verify(medicalHistoryRepository).save(medicalHistory);
    }

    @Test
    void createMedicalHistoryThrowsExceptionWhenMedicalHistoryIsNull() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                medicalHistoryService.createMedicalHistory(null));

        assertEquals("MedicalHistory cannot be null", exception.getMessage());
        verify(medicalHistoryRepository, never()).save(any());
    }

    @Test
    void updateMedicalHistorySuccessfullyUpdatesMedicalHistory() {
        Long id = 1L;
        MedicalHistory medicalHistory = createMedicalHistory(id);

        when(medicalHistoryRepository.existsById(id)).thenReturn(true);
        when(medicalHistoryRepository.save(medicalHistory)).thenReturn(medicalHistory);

        MedicalHistory result = medicalHistoryService.updateMedicalHistory(medicalHistory);

        assertNotNull(result);
        assertEquals(medicalHistory, result);
        verify(medicalHistoryRepository).save(medicalHistory);
    }

    @Test
    void updateMedicalHistoryThrowsExceptionWhenMedicalHistoryIsNull() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                medicalHistoryService.updateMedicalHistory(null));

        assertEquals("MedicalHistory cannot be null", exception.getMessage());
        verify(medicalHistoryRepository, never()).save(any());
    }

    @Test
    void updateMedicalHistoryThrowsExceptionWhenIdIsNull() {
        MedicalHistory medicalHistory = createMedicalHistory(null);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                medicalHistoryService.updateMedicalHistory(medicalHistory));

        assertEquals("MedicalHistory ID cannot be null", exception.getMessage());
        verify(medicalHistoryRepository, never()).save(any());
    }

    @Test
    void updateMedicalHistoryThrowsExceptionWhenMedicalHistoryDoesNotExist() {
        Long id = 1L;
        MedicalHistory medicalHistory = createMedicalHistory(id);

        when(medicalHistoryRepository.existsById(id)).thenReturn(false);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                medicalHistoryService.updateMedicalHistory(medicalHistory));

        assertEquals("MedicalHistory with id 1 does not exist", exception.getMessage());
        verify(medicalHistoryRepository, never()).save(any());
    }

    @Test
    void deleteMedicalHistorySuccessfullyDeletesMedicalHistory() {
        Long id = 1L;

        when(medicalHistoryRepository.existsById(id)).thenReturn(true);

        medicalHistoryService.deleteMedicalHistory(id);

        verify(medicalHistoryRepository).deleteById(id);
    }

    @Test
    void deleteMedicalHistoryThrowsExceptionWhenMedicalHistoryDoesNotExist() {
        Long id = 1L;

        when(medicalHistoryRepository.existsById(id)).thenReturn(false);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                medicalHistoryService.deleteMedicalHistory(id));

        assertEquals("MedicalHistory with id 1 does not exist", exception.getMessage());
        verify(medicalHistoryRepository, never()).deleteById(any());
    }

    private MedicalHistory createMedicalHistory(Long id) {
        MedicalHistory medicalHistory = new MedicalHistory();
        medicalHistory.setHistoryId(id);
        return medicalHistory;
    }
}