package org.thevoids.oncologic.service;

import java.util.List;

import org.thevoids.oncologic.dto.entity.MedicalHistoryDTO;

public interface MedicalHistoryService {

    List<MedicalHistoryDTO> getAllMedicalHistories();

    MedicalHistoryDTO getMedicalHistoryById(Long id);

    MedicalHistoryDTO createMedicalHistory(MedicalHistoryDTO medicalHistoryDTO);

    MedicalHistoryDTO updateMedicalHistory(MedicalHistoryDTO medicalHistoryDTO);

    void deleteMedicalHistory(Long id);
}
