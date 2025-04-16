package org.thevoids.oncologic.service.impl;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.thevoids.oncologic.entity.Lab;
import org.thevoids.oncologic.entity.Patient;
import org.thevoids.oncologic.entity.User;
import org.thevoids.oncologic.repository.LabRepository;
import org.thevoids.oncologic.repository.PatientRepository;
import org.thevoids.oncologic.repository.UserRepository;
import org.thevoids.oncologic.service.LabService;

import java.util.Date;
import java.util.List;

@Service
public class LabServiceImpl implements LabService {
    private final LabRepository labRepository;
    private final UserRepository userRepository;
    private final PatientRepository patientRepository;

    public LabServiceImpl(
            LabRepository labRepository,
            UserRepository userRepository,
            PatientRepository patientRepository
    ) {
        this.labRepository = labRepository;
        this.userRepository = userRepository;
        this.patientRepository = patientRepository;
    }

    @Override
    public Lab getLabById(Long id) {
        return labRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Lab with id " + id + " does not exist"));
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

    @Override
    @Transactional
    public void assignLab(Long patientId, Long userId, Date requestDate) {
        if (patientId == null || userId == null || requestDate == null) {
            throw new IllegalArgumentException("Patient ID, User ID, and Request Date cannot be null");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User with id " + userId + " does not exist"));
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new IllegalArgumentException("Patient with id " + patientId + " does not exist"));

        Lab lab = new Lab();
        lab.setRequestDate(requestDate);
        lab.setLabTechnician(user);
        lab.setPatient(patient);

        labRepository.save(lab);
    }
}
