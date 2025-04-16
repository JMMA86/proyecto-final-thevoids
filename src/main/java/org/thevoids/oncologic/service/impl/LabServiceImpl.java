package org.thevoids.oncologic.service.impl;

import org.springframework.stereotype.Service;
import org.thevoids.oncologic.entity.Lab;
import org.thevoids.oncologic.repository.LabRepository;
import org.thevoids.oncologic.service.LabService;

import java.util.List;

@Service
public class LabServiceImpl implements LabService {
    private final LabRepository labRepository;

    public LabServiceImpl(LabRepository labRepository) {
        this.labRepository = labRepository;
    }

    @Override
    public Lab getLabById(Long id) {
        return labRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Lab with id " + id + " does not exist"));
    }

    @Override
    public Lab createLab(Lab lab) {
        if (lab == null) {
            throw new IllegalArgumentException("Lab cannot be null");
        }

        return labRepository.save(lab);
    }

    @Override
    public Lab updateLab(Lab lab) {
        if (lab == null) {
            throw new IllegalArgumentException("Lab cannot be null");
        }

        if (lab.getLabId() == null) {
            throw new IllegalArgumentException("Lab ID cannot be null");
        }

        if (!labRepository.existsById(lab.getLabId())) {
            throw new IllegalArgumentException("Lab with id " + lab.getLabId() + " does not exist");
        }

        return labRepository.save(lab);
    }

    @Override
    public void deleteLab(Long id) {
        if (!labRepository.existsById(id)) {
            throw new IllegalArgumentException("Lab with id " + id + " does not exist");
        }

        labRepository.deleteById(id);
    }

    @Override
    public List<Lab> getAllLabs() {
        return labRepository.findAll();
    }
}
