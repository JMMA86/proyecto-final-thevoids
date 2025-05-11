package org.thevoids.oncologic.service.impl;

import org.springframework.stereotype.Service;
import org.thevoids.oncologic.dto.MedicalHistoryDTO;
import org.thevoids.oncologic.entity.MedicalHistory;
import org.thevoids.oncologic.exception.ResourceNotFoundException;
import org.thevoids.oncologic.exception.InvalidOperationException;
import org.thevoids.oncologic.mapper.MedicalHistoryMapper;
import org.thevoids.oncologic.repository.MedicalHistoryRepository;
import org.thevoids.oncologic.service.MedicalHistoryService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MedicalHistoryServiceImpl implements MedicalHistoryService {
    private final MedicalHistoryRepository medicalHistoryRepository;
    private final MedicalHistoryMapper medicalHistoryMapper;

    public MedicalHistoryServiceImpl(MedicalHistoryRepository medicalHistoryRepository,
            MedicalHistoryMapper medicalHistoryMapper) {
        this.medicalHistoryRepository = medicalHistoryRepository;
        this.medicalHistoryMapper = medicalHistoryMapper;
    }

    @Override
    public List<MedicalHistoryDTO> getAllMedicalHistories() {
        List<MedicalHistory> medicalHistories = medicalHistoryRepository.findAll();
        return medicalHistories.stream()
                .map(medicalHistoryMapper::toMedicalHistoryDTO)
                .collect(Collectors.toList());
    }

    @Override
    public MedicalHistoryDTO getMedicalHistoryById(Long id) {
        MedicalHistory medicalHistory = medicalHistoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("MedicalHistory", "id", id));
        return medicalHistoryMapper.toMedicalHistoryDTO(medicalHistory);
    }

    @Override
    public MedicalHistoryDTO createMedicalHistory(MedicalHistoryDTO medicalHistoryDTO) {
        if (medicalHistoryDTO == null) {
            throw new InvalidOperationException("MedicalHistory cannot be null");
        }

        MedicalHistory medicalHistory = medicalHistoryMapper.toMedicalHistory(medicalHistoryDTO);
        MedicalHistory savedMedicalHistory = medicalHistoryRepository.save(medicalHistory);
        return medicalHistoryMapper.toMedicalHistoryDTO(savedMedicalHistory);
    }

    @Override
    public MedicalHistoryDTO updateMedicalHistory(MedicalHistoryDTO medicalHistoryDTO) {
        if (medicalHistoryDTO == null) {
            throw new InvalidOperationException("MedicalHistory cannot be null");
        }

        if (medicalHistoryDTO.getHistoryId() == null) {
            throw new InvalidOperationException("MedicalHistory ID cannot be null");
        }

        if (!medicalHistoryRepository.existsById(medicalHistoryDTO.getHistoryId())) {
            throw new ResourceNotFoundException("MedicalHistory", "id", medicalHistoryDTO.getHistoryId());
        }

        MedicalHistory medicalHistory = medicalHistoryMapper.toMedicalHistory(medicalHistoryDTO);
        MedicalHistory updatedMedicalHistory = medicalHistoryRepository.save(medicalHistory);
        return medicalHistoryMapper.toMedicalHistoryDTO(updatedMedicalHistory);
    }

    @Override
    public void deleteMedicalHistory(Long id) {
        if (!medicalHistoryRepository.existsById(id)) {
            throw new ResourceNotFoundException("MedicalHistory", "id", id);
        }

        medicalHistoryRepository.deleteById(id);
    }
}
