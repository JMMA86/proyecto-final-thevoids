package org.thevoids.oncologic.repository;

import org.thevoids.oncologic.entity.Role;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    @Query("SELECT r FROM Role r JOIN r.assignedRoles ar WHERE ar.user.userId = :userId")
    List<Role> findRolesByUserId(Long userId);
}