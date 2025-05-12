package org.thevoids.oncologic.service;

import java.util.Date;
import java.util.List;

import org.thevoids.oncologic.dto.entity.LabDTO;

public interface LabService {
    LabDTO getLabById(Long id);

    LabDTO updateLab(LabDTO labDTO);

    void deleteLab(Long id);

    List<LabDTO> getAllLabs();

    LabDTO assignLab(Long patientId, Long userId, Date requestDate);
}
