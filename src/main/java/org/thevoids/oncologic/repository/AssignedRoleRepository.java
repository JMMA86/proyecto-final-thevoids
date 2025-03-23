package org.thevoids.oncologic.repository;

import org.thevoids.oncologic.entity.AssignedRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssignedRoleRepository extends JpaRepository<AssignedRole, Long> {
}