package org.thevoids.oncologic.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.thevoids.oncologic.dto.MedicalHistoryDTO;
import org.thevoids.oncologic.entity.MedicalHistory;
import org.thevoids.oncologic.mapper.MedicalHistoryMapper;
import org.thevoids.oncologic.repository.MedicalHistoryRepository;
import org.thevoids.oncologic.service.impl.MedicalHistoryServiceImpl;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MedicalHistoryServiceUnitTest {

    @Mock
    private MedicalHistoryRepository medicalHistoryRepository;

    @Mock
    private MedicalHistoryMapper medicalHistoryMapper;

    @InjectMocks
    private MedicalHistoryServiceImpl medicalHistoryService;

    @Test
    void getAllMedicalHistoriesReturnsAllHistories() {
        // Create entity list
        List<MedicalHistory> entityList = List.of(
                createMedicalHistoryEntity(1L),
                createMedicalHistoryEntity(2L)
        );

        // Create DTO list
        List<MedicalHistoryDTO> dtoList = List.of(
                createMedicalHistoryDTO(1L),
                createMedicalHistoryDTO(2L)
        );

        // Mock repository and mapper
        when(medicalHistoryRepository.findAll()).thenReturn(entityList);
        when(medicalHistoryMapper.toMedicalHistoryDTO(entityList.get(0))).thenReturn(dtoList.get(0));
        when(medicalHistoryMapper.toMedicalHistoryDTO(entityList.get(1))).thenReturn(dtoList.get(1));

        // Call service method
        List<MedicalHistoryDTO> result = medicalHistoryService.getAllMedicalHistories();

        // Verify results
        assertEquals(2, result.size());
        assertEquals(dtoList.get(0).getHistoryId(), result.get(0).getHistoryId());
        assertEquals(dtoList.get(1).getHistoryId(), result.get(1).getHistoryId());
        verify(medicalHistoryRepository).findAll();
        verify(medicalHistoryMapper, times(2)).toMedicalHistoryDTO(any(MedicalHistory.class));
    }

    @Test
    void getMedicalHistoryByIdReturnsHistoryWhenExists() {
        Long id = 1L;
        MedicalHistory entity = createMedicalHistoryEntity(id);
        MedicalHistoryDTO dto = createMedicalHistoryDTO(id);

        when(medicalHistoryRepository.findById(id)).thenReturn(Optional.of(entity));
        when(medicalHistoryMapper.toMedicalHistoryDTO(entity)).thenReturn(dto);

        MedicalHistoryDTO result = medicalHistoryService.getMedicalHistoryById(id);

        assertNotNull(result);
        assertEquals(id, result.getHistoryId());
        verify(medicalHistoryRepository).findById(id);
        verify(medicalHistoryMapper).toMedicalHistoryDTO(entity);
    }

    @Test
    void getMedicalHistoryByIdThrowsExceptionWhenNotExists() {
        Long id = 1L;

        when(medicalHistoryRepository.findById(id)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                medicalHistoryService.getMedicalHistoryById(id));

        assertEquals("MedicalHistory with id 1 does not exist", exception.getMessage());
        verify(medicalHistoryRepository).findById(id);
        verify(medicalHistoryMapper, never()).toMedicalHistoryDTO(any());
    }

    @Test
    void createMedicalHistorySuccessfullyCreatesMedicalHistory() {
        MedicalHistoryDTO inputDto = createMedicalHistoryDTO(null);
        MedicalHistory entity = createMedicalHistoryEntity(null);
        MedicalHistory savedEntity = createMedicalHistoryEntity(1L);
        MedicalHistoryDTO outputDto = createMedicalHistoryDTO(1L);

        when(medicalHistoryMapper.toMedicalHistory(inputDto)).thenReturn(entity);
        when(medicalHistoryRepository.save(entity)).thenReturn(savedEntity);
        when(medicalHistoryMapper.toMedicalHistoryDTO(savedEntity)).thenReturn(outputDto);

        MedicalHistoryDTO result = medicalHistoryService.createMedicalHistory(inputDto);

        assertNotNull(result);
        assertEquals(1L, result.getHistoryId());
        verify(medicalHistoryMapper).toMedicalHistory(inputDto);
        verify(medicalHistoryRepository).save(entity);
        verify(medicalHistoryMapper).toMedicalHistoryDTO(savedEntity);
    }

    @Test
    void createMedicalHistoryThrowsExceptionWhenMedicalHistoryIsNull() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                medicalHistoryService.createMedicalHistory(null));

        assertEquals("MedicalHistory cannot be null", exception.getMessage());
        verify(medicalHistoryMapper, never()).toMedicalHistory(any());
        verify(medicalHistoryRepository, never()).save(any());
    }

    @Test
    void updateMedicalHistorySuccessfullyUpdatesMedicalHistory() {
        Long id = 1L;
        MedicalHistoryDTO inputDto = createMedicalHistoryDTO(id);
        MedicalHistory entity = createMedicalHistoryEntity(id);
        MedicalHistory updatedEntity = createMedicalHistoryEntity(id);
        MedicalHistoryDTO outputDto = createMedicalHistoryDTO(id);

        when(medicalHistoryRepository.existsById(id)).thenReturn(true);
        when(medicalHistoryMapper.toMedicalHistory(inputDto)).thenReturn(entity);
        when(medicalHistoryRepository.save(entity)).thenReturn(updatedEntity);
        when(medicalHistoryMapper.toMedicalHistoryDTO(updatedEntity)).thenReturn(outputDto);

        MedicalHistoryDTO result = medicalHistoryService.updateMedicalHistory(inputDto);

        assertNotNull(result);
        assertEquals(id, result.getHistoryId());
        verify(medicalHistoryRepository).existsById(id);
        verify(medicalHistoryMapper).toMedicalHistory(inputDto);
        verify(medicalHistoryRepository).save(entity);
        verify(medicalHistoryMapper).toMedicalHistoryDTO(updatedEntity);
    }

    @Test
    void updateMedicalHistoryThrowsExceptionWhenMedicalHistoryIsNull() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                medicalHistoryService.updateMedicalHistory(null));

        assertEquals("MedicalHistory cannot be null", exception.getMessage());
        verify(medicalHistoryRepository, never()).existsById(any());
        verify(medicalHistoryMapper, never()).toMedicalHistory(any());
        verify(medicalHistoryRepository, never()).save(any());
    }

    @Test
    void updateMedicalHistoryThrowsExceptionWhenIdIsNull() {
        MedicalHistoryDTO dto = createMedicalHistoryDTO(null);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                medicalHistoryService.updateMedicalHistory(dto));

        assertEquals("MedicalHistory ID cannot be null", exception.getMessage());
        verify(medicalHistoryRepository, never()).existsById(any());
        verify(medicalHistoryMapper, never()).toMedicalHistory(any());
        verify(medicalHistoryRepository, never()).save(any());
    }

    @Test
    void updateMedicalHistoryThrowsExceptionWhenMedicalHistoryDoesNotExist() {
        Long id = 1L;
        MedicalHistoryDTO dto = createMedicalHistoryDTO(id);

        when(medicalHistoryRepository.existsById(id)).thenReturn(false);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                medicalHistoryService.updateMedicalHistory(dto));

        assertEquals("MedicalHistory with id 1 does not exist", exception.getMessage());
        verify(medicalHistoryRepository).existsById(id);
        verify(medicalHistoryMapper, never()).toMedicalHistory(any());
        verify(medicalHistoryRepository, never()).save(any());
    }

    @Test
    void deleteMedicalHistorySuccessfullyDeletesMedicalHistory() {
        Long id = 1L;

        when(medicalHistoryRepository.existsById(id)).thenReturn(true);

        medicalHistoryService.deleteMedicalHistory(id);

        verify(medicalHistoryRepository).existsById(id);
        verify(medicalHistoryRepository).deleteById(id);
    }

    @Test
    void deleteMedicalHistoryThrowsExceptionWhenMedicalHistoryDoesNotExist() {
        Long id = 1L;

        when(medicalHistoryRepository.existsById(id)).thenReturn(false);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                medicalHistoryService.deleteMedicalHistory(id));

        assertEquals("MedicalHistory with id 1 does not exist", exception.getMessage());
        verify(medicalHistoryRepository).existsById(id);
        verify(medicalHistoryRepository, never()).deleteById(any());
    }

    private MedicalHistory createMedicalHistoryEntity(Long id) {
        MedicalHistory medicalHistory = new MedicalHistory();
        medicalHistory.setHistoryId(id);
        return medicalHistory;
    }

    private MedicalHistoryDTO createMedicalHistoryDTO(Long id) {
        MedicalHistoryDTO dto = new MedicalHistoryDTO();
        dto.setHistoryId(id);
        dto.setPatientId(1L);
        dto.setDiagnosis("Test Diagnosis");
        dto.setTreatment("Test Treatment");
        dto.setMedications("Test Medications");
        dto.setRecordDate(new Timestamp(System.currentTimeMillis()));
        return dto;
    }
}
