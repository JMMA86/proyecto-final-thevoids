package org.thevoids.oncologic.service;

import java.util.List;

import org.thevoids.oncologic.entity.ClinicAssignment;

public interface ClinicAssigmentService {
    List<ClinicAssignment> getAllClinicAssignments();

    ClinicAssignment getClinicAssignmentById(Long id);

    ClinicAssignment updateClinicAssignment(ClinicAssignment clinicAssigment);

    void deleteClinicAssigment(Long id);

    ClinicAssignment assignClinic(ClinicAssignment clinicAssignment);
}
