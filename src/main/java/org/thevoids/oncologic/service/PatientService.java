package org.thevoids.oncologic.service;

import org.thevoids.oncologic.dto.PatientDTO;

import java.util.List;

public interface PatientService {
    PatientDTO getPatientById(Long id);

    PatientDTO createPatient(PatientDTO patientDTO);

    PatientDTO updatePatient(PatientDTO patientDTO);

    void deletePatient(Long id);

    List<PatientDTO> getAllPatients();
}
