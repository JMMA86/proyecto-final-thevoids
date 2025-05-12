package org.thevoids.oncologic.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.thevoids.oncologic.entity.Clinic;
import org.thevoids.oncologic.entity.ClinicAssignment;
import org.thevoids.oncologic.entity.User;
import org.thevoids.oncologic.repository.ClinicAssignmentRepository;
import org.thevoids.oncologic.repository.ClinicRepository;
import org.thevoids.oncologic.repository.UserRepository;
import org.thevoids.oncologic.service.ClinicAssigmentService;

import jakarta.transaction.Transactional;

@Service
public class ClinicAssigmentImpl implements ClinicAssigmentService {
    private final ClinicAssignmentRepository clinicAssignmentRepository;
    private final ClinicRepository clinicRepository;
    private final UserRepository userRepository;

    public ClinicAssigmentImpl(
            ClinicAssignmentRepository clinicAssignmentRepository,
            UserRepository userRepository,
            ClinicRepository clinicRepository) {
        this.clinicAssignmentRepository = clinicAssignmentRepository;
        this.userRepository = userRepository;
        this.clinicRepository = clinicRepository;
    }

    @Override
    public List<ClinicAssignment> getAllClinicAssignments() {
        return clinicAssignmentRepository.findAll();
    }

    @Override
    public ClinicAssignment getClinicAssignmentById(Long id) {
        return clinicAssignmentRepository.findById(id).orElse(null);
    }

    @Override
    public ClinicAssignment updateClinicAssignment(ClinicAssignment clinicAssigment) {
        if (clinicAssigment == null) {
            throw new IllegalArgumentException("ClinicAssigment cannot be null");
        }

        if (clinicAssigment.getId() == null) {
            throw new IllegalArgumentException("ClinicAssigment ID cannot be null");
        }

        if (!clinicAssignmentRepository.existsById(clinicAssigment.getId())) {
            throw new IllegalArgumentException(
                    "ClinicAssigment with id " + clinicAssigment.getId() + " does not exist");
        }

        return clinicAssignmentRepository.save(clinicAssigment);
    }

    @Override
    public void deleteClinicAssigment(Long id) {
        if (!clinicAssignmentRepository.existsById(id)) {
            throw new IllegalArgumentException("ClinicAssigment with id " + id + " does not exist");
        }

        clinicAssignmentRepository.deleteById(id);
    }

    @Transactional
    @Override
    public ClinicAssignment assignClinic(ClinicAssignment clinicAssignment) {
        if (clinicAssignment == null) {
            throw new IllegalArgumentException("ClinicAssignment cannot be null");
        }
        if (clinicAssignment.getUser() == null || clinicAssignment.getUser().getUserId() == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }
        if (clinicAssignment.getClinic() == null || clinicAssignment.getClinic().getId() == null) {
            throw new IllegalArgumentException("Clinic ID cannot be null");
        }
        if (clinicAssignment.getStartTime() == null || clinicAssignment.getEndTime() == null) {
            throw new IllegalArgumentException("Start time and end time cannot be null");
        }

        Clinic clinic = clinicRepository.findById(clinicAssignment.getClinic().getId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Clinic with id " + clinicAssignment.getClinic().getId() + " does not exist"));

        User user = userRepository.findById(clinicAssignment.getUser().getUserId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "User with id " + clinicAssignment.getUser().getUserId() + " does not exist"));

        clinicAssignment.setClinic(clinic);
        clinicAssignment.setUser(user);

        return clinicAssignmentRepository.save(clinicAssignment);
    }
}
