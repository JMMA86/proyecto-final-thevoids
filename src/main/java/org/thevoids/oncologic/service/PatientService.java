package org.thevoids.oncologic.service;

import java.util.List;

import org.thevoids.oncologic.dto.entity.PatientDTO;

public interface PatientService {
    PatientDTO getPatientById(Long id);

    PatientDTO createPatient(PatientDTO patientDTO);

    PatientDTO updatePatient(PatientDTO patientDTO);

    void deletePatient(Long id);

    List<PatientDTO> getAllPatients();
}
