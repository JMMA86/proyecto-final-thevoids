package org.thevoids.oncologic.service.impl;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.thevoids.oncologic.entity.Clinic;
import org.thevoids.oncologic.entity.ClinicAssignment;
import org.thevoids.oncologic.entity.User;
import org.thevoids.oncologic.repository.AppointmentTypeRepository;
import org.thevoids.oncologic.repository.ClinicAssignmentRepository;
import org.thevoids.oncologic.repository.ClinicRepository;
import org.thevoids.oncologic.repository.UserRepository;
import org.thevoids.oncologic.service.ClinicAssigmentService;

import java.util.List;

@Service
public class ClinicAssigmentImpl implements ClinicAssigmentService {
    private final ClinicAssignmentRepository clinicAssignmentRepository;
    private final ClinicRepository clinicRepository;
    private final UserRepository userRepository;
    private final AppointmentTypeRepository appointmentTypeRepository;

    public ClinicAssigmentImpl(
            ClinicAssignmentRepository clinicAssignmentRepository,
            UserRepository userRepository,
            ClinicRepository clinicRepository,
            AppointmentTypeRepository appointmentTypeRepository) {
        this.clinicAssignmentRepository = clinicAssignmentRepository;
        this.userRepository = userRepository;
        this.clinicRepository = clinicRepository;
        this.appointmentTypeRepository = appointmentTypeRepository;
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
    public void assignClinic(Long userId, Long clinicId) {
        if (userId == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }

        if (clinicId == null) {
            throw new IllegalArgumentException("Clinic ID cannot be null");
        }

        Clinic clinic = clinicRepository.findById(clinicId)
                .orElseThrow(() -> new IllegalArgumentException("Clinic with id " + clinicId + " does not exist"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User with id " + userId + " does not exist"));

        ClinicAssignment clinicAssignment = new ClinicAssignment();
        clinicAssignment.setUser(user);
        clinicAssignment.setClinic(clinic);
        clinicAssignmentRepository.save(clinicAssignment);
    }
}
