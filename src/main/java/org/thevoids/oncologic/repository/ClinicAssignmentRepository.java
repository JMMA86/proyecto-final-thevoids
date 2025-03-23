package org.thevoids.oncologic.repository;

import org.thevoids.oncologic.entity.ClinicAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClinicAssignmentRepository extends JpaRepository<ClinicAssignment, Long> {
}