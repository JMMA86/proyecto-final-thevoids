package org.thevoids.oncologic.service;

import org.thevoids.oncologic.entity.Specialty;

import java.util.List;

public interface SpecialtyService {
    Specialty getSpecialtyById(Long id);

    Specialty createSpecialty(Specialty specialty);

    Specialty updateSpecialty(Specialty specialty);

    void deleteSpecialty(Long id);

    List<Specialty> getAllSpecialties();
}
