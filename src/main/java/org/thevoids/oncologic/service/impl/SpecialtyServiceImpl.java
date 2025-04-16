package org.thevoids.oncologic.service.impl;

import org.springframework.stereotype.Service;
import org.thevoids.oncologic.entity.Specialty;
import org.thevoids.oncologic.repository.SpecialtyRepository;
import org.thevoids.oncologic.service.SpecialtyService;

import java.util.List;

@Service
public class SpecialtyServiceImpl implements SpecialtyService {
    private final SpecialtyRepository specialtyRepository;

    public SpecialtyServiceImpl(SpecialtyRepository specialtyRepository) {
        this.specialtyRepository = specialtyRepository;
    }

    @Override
    public Specialty getSpecialtyById(Long id) {
        return specialtyRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Specialty with id " + id + " does not exist"));
    }

    @Override
    public Specialty createSpecialty(Specialty specialty) {
        if (specialty == null) {
            throw new IllegalArgumentException("Specialty cannot be null");
        }

        return specialtyRepository.save(specialty);
    }

    @Override
    public Specialty updateSpecialty(Specialty specialty) {
        if (specialty == null) {
            throw new IllegalArgumentException("Specialty cannot be null");
        }

        if (specialty.getSpecialtyId() == null) {
            throw new IllegalArgumentException("Specialty ID cannot be null");
        }

        if (!specialtyRepository.existsById(specialty.getSpecialtyId())) {
            throw new IllegalArgumentException("Specialty with id " + specialty.getSpecialtyId() + " does not exist");
        }

        return specialtyRepository.save(specialty);
    }

    @Override
    public void deleteSpecialty(Long id) {
        if (!specialtyRepository.existsById(id)) {
            throw new IllegalArgumentException("Specialty with id " + id + " does not exist");
        }

        specialtyRepository.deleteById(id);
    }

    @Override
    public List<Specialty> getAllSpecialties() {
        return specialtyRepository.findAll();
    }
}