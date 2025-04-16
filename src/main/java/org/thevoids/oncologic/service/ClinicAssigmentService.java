package org.thevoids.oncologic.service;

import org.thevoids.oncologic.entity.ClinicAssignment;

import java.util.List;

public interface ClinicAssigmentService {
    List<ClinicAssignment> getAllClinicAssignments();

    ClinicAssignment getClinicAssignmentById(Long id);

    ClinicAssignment createClinicAssignment(ClinicAssignment clinicAssigment);

    ClinicAssignment updateClinicAssignment(ClinicAssignment clinicAssigment);

    void deleteClinicAssigment(Long id);

    void assignClinicToUser(Long userId, Long clinicId);
}
