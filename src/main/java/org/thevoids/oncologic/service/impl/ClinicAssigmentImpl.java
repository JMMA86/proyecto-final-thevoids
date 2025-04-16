package org.thevoids.oncologic.service.impl;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.thevoids.oncologic.entity.ClinicAssignment;
import org.thevoids.oncologic.entity.User;
import org.thevoids.oncologic.repository.ClinicAssignmentRepository;
import org.thevoids.oncologic.repository.UserRepository;
import org.thevoids.oncologic.service.ClinicAssigmentService;

import java.util.List;

@Service
public class ClinicAssigmentImpl implements ClinicAssigmentService {
    private final ClinicAssignmentRepository clinicAssignmentRepository;
    private final UserRepository userRepository;

    public ClinicAssigmentImpl(
            ClinicAssignmentRepository clinicAssignmentRepository,
            UserRepository userRepository
    ) {
        this.clinicAssignmentRepository = clinicAssignmentRepository;
        this.userRepository = userRepository;
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
    public ClinicAssignment createClinicAssignment(ClinicAssignment clinicAssigment) {
        if (clinicAssigment == null) {
            throw new IllegalArgumentException("ClinicAssignment cannot be null");
        }

        return clinicAssignmentRepository.save(clinicAssigment);
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
            throw new IllegalArgumentException("ClinicAssigment with id " + clinicAssigment.getId() + " does not exist");
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
    public void assignClinicToUser(Long userId, Long clinicId) {
        if (userId == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }

        if (clinicId == null) {
            throw new IllegalArgumentException("Clinic ID cannot be null");
        }

        ClinicAssignment clinicAssignment = clinicAssignmentRepository.findById(clinicId)
                .orElseThrow(() -> new IllegalArgumentException("ClinicAssignment with id " + clinicId + " does not exist"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User with id " + userId + " does not exist"));

        clinicAssignment.setUser(user);
        clinicAssignmentRepository.save(clinicAssignment);
    }
}
