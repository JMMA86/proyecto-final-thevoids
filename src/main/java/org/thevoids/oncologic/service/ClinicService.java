package org.thevoids.oncologic.service;

import org.thevoids.oncologic.entity.Clinic;

import java.util.List;

public interface ClinicService {
    List<Clinic> getAllClinics();

    Clinic getClinicById(Long id);

    Clinic createClinic(Clinic clinic);

    Clinic updateClinic(Long id, Clinic clinic);

    void deleteClinic(Long id);
}
