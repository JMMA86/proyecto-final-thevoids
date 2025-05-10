package org.thevoids.oncologic.service;

import org.thevoids.oncologic.dto.LabDTO;

import java.util.Date;
import java.util.List;

public interface LabService {
    LabDTO getLabById(Long id);

    LabDTO updateLab(LabDTO labDTO);

    void deleteLab(Long id);

    List<LabDTO> getAllLabs();

    LabDTO assignLab(Long patientId, Long userId, Date requestDate);
}
