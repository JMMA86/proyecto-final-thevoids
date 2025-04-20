package org.thevoids.oncologic.service;

import org.thevoids.oncologic.entity.Patient;

import java.util.List;

public interface PatientService {
    Patient getPatientById(Long id);

    Patient createPatient(Patient patient);

    Patient updatePatient(Patient patient);

    void deletePatient(Long id);

    List<Patient> getAllPatients();
}
