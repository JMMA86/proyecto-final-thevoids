package org.thevoids.oncologic.service;

import org.thevoids.oncologic.entity.Lab;

import java.util.List;

public interface LabService {
    Lab getLabById(Long id);

    Lab createLab(Lab lab);

    Lab updateLab(Lab lab);

    void deleteLab(Long id);

    List<Lab> getAllLabs();
}
