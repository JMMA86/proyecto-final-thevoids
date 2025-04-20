package org.thevoids.oncologic.service.impl;

import org.springframework.stereotype.Service;
import org.thevoids.oncologic.entity.Clinic;
import org.thevoids.oncologic.repository.ClinicRepository;
import org.thevoids.oncologic.service.ClinicService;

import java.util.List;

@Service
public class ClinicServiceImpl implements ClinicService {
    private final ClinicRepository clinicRepository;

    public ClinicServiceImpl(ClinicRepository clinicRepository) {
        this.clinicRepository = clinicRepository;
    }

    @Override
    public List<Clinic> getAllClinics() {
        return clinicRepository.findAll();
    }

    @Override
    public Clinic getClinicById(Long id) {
        return clinicRepository.findById(id).orElse(null);
    }

    @Override
    public Clinic createClinic(Clinic clinic) {
        if (clinic == null) {
            throw new IllegalArgumentException("Clinic cannot be null");
        }

        return clinicRepository.save(clinic);
    }

    @Override
    public Clinic updateClinic(Long id, Clinic clinic) {
        if (clinic == null) {
            throw new IllegalArgumentException("Clinic cannot be null");
        }

        if (!clinicRepository.existsById(id)) {
            throw new IllegalArgumentException("Clinic with id " + id + " does not exist");
        }

        Clinic existingClinic = clinicRepository.findById(id).orElse(null);

        existingClinic.setName(clinic.getName());
        existingClinic.setAddress(clinic.getAddress());
        existingClinic.setPhone(clinic.getPhone());
        existingClinic.setSpecialty(clinic.getSpecialty());
        existingClinic.setCapacity(clinic.getCapacity());

        clinicRepository.save(existingClinic);

        return clinic;
    }

    @Override
    public void deleteClinic(Long id) {
        if (!clinicRepository.existsById(id)) {
            throw new IllegalArgumentException("Clinic with id " + id + " does not exist");
        }

        clinicRepository.deleteById(id);
    }
}
