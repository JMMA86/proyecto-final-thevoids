package org.thevoids.oncologic.service;

import org.thevoids.oncologic.dto.MedicalHistoryDTO;

import java.util.List;

public interface MedicalHistoryService {

    List<MedicalHistoryDTO> getAllMedicalHistories();

    MedicalHistoryDTO getMedicalHistoryById(Long id);

    MedicalHistoryDTO createMedicalHistory(MedicalHistoryDTO medicalHistoryDTO);

    MedicalHistoryDTO updateMedicalHistory(MedicalHistoryDTO medicalHistoryDTO);

    void deleteMedicalHistory(Long id);
}
