package org.thevoids.oncologic.service;

import org.thevoids.oncologic.entity.MedicalHistory;

import java.util.List;

public interface MedicalHistoryService {

    List<MedicalHistory> getAllMedicalHistories();

    MedicalHistory getMedicalHistoryById(Long id);

    MedicalHistory createMedicalHistory(MedicalHistory medicalHistory);

    MedicalHistory updateMedicalHistory(MedicalHistory medicalHistory);

    void deleteMedicalHistory(Long id);
}
