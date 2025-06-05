package org.thevoids.oncologic.service;

import java.util.Date;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;
import org.thevoids.oncologic.dto.entity.LabDTO;

public interface LabService {
    LabDTO getLabById(Long id);

    LabDTO updateLab(LabDTO labDTO);

    LabDTO updateLabWithFile(LabDTO labDTO, MultipartFile file);

    void deleteLab(Long id);

    List<LabDTO> getAllLabs();

    LabDTO assignLab(Long patientId, Long userId, Date requestDate, String testType, Date completionDate,
            String result);

    LabDTO assignLabWithFile(Long patientId, Long userId, Date requestDate, String testType, Date completionDate,
            String result, MultipartFile file);
}
