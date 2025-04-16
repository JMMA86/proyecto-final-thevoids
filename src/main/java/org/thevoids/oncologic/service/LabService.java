package org.thevoids.oncologic.service;

import org.thevoids.oncologic.entity.Lab;

import java.util.Date;
import java.util.List;

public interface LabService {
    Lab getLabById(Long id);

    Lab updateLab(Lab lab);

    void deleteLab(Long id);

    List<Lab> getAllLabs();

    void assignLab(Long patientId, Long userId, Date requestDate);
}
