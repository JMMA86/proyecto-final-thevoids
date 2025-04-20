package org.thevoids.oncologic.service.impl;

import org.springframework.stereotype.Service;
import org.thevoids.oncologic.entity.MedicalHistory;
import org.thevoids.oncologic.repository.MedicalHistoryRepository;
import org.thevoids.oncologic.service.MedicalHistoryService;

import java.util.List;

@Service
public class MedicalHistoryServiceImpl implements MedicalHistoryService {
    private final MedicalHistoryRepository medicalHistoryRepository;

    public MedicalHistoryServiceImpl(MedicalHistoryRepository medicalHistoryRepository) {
        this.medicalHistoryRepository = medicalHistoryRepository;
    }

    @Override
    public List<MedicalHistory> getAllMedicalHistories() {
        return medicalHistoryRepository.findAll();
    }

    @Override
    public MedicalHistory getMedicalHistoryById(Long id) {
        return medicalHistoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("MedicalHistory with id " + id + " does not exist"));
    }

    @Override
    public MedicalHistory createMedicalHistory(MedicalHistory medicalHistory) {
        if (medicalHistory == null) {
            throw new IllegalArgumentException("MedicalHistory cannot be null");
        }

        return medicalHistoryRepository.save(medicalHistory);
    }

    @Override
    public MedicalHistory updateMedicalHistory(MedicalHistory medicalHistory) {
        if (medicalHistory == null) {
            throw new IllegalArgumentException("MedicalHistory cannot be null");
        }

        if (medicalHistory.getHistoryId() == null) {
            throw new IllegalArgumentException("MedicalHistory ID cannot be null");
        }

        if (!medicalHistoryRepository.existsById(medicalHistory.getHistoryId())) {
            throw new IllegalArgumentException("MedicalHistory with id " + medicalHistory.getHistoryId() + " does not exist");
        }

        return medicalHistoryRepository.save(medicalHistory);
    }

    @Override
    public void deleteMedicalHistory(Long id) {
        if (!medicalHistoryRepository.existsById(id)) {
            throw new IllegalArgumentException("MedicalHistory with id " + id + " does not exist");
        }

        medicalHistoryRepository.deleteById(id);
    }
}