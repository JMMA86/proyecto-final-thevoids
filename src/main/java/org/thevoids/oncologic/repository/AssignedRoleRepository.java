package org.thevoids.oncologic.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.thevoids.oncologic.entity.AssignedRole;

import java.util.Optional;

@Repository
public interface AssignedRoleRepository extends JpaRepository<AssignedRole, Long> {
    @Query("SELECT CASE WHEN COUNT(ar) > 0 THEN true ELSE false END FROM AssignedRole ar WHERE ar.role.roleId = :roleId AND ar.user.userId = :userId")
    boolean existsByRoleIdAndUserId(@Param("roleId") Long roleId, @Param("userId") Long userId);

    @Query("SELECT ar FROM AssignedRole ar WHERE ar.role.roleId = :roleId AND ar.user.userId = :userId")
    Optional<AssignedRole> findByRoleIdAndUserId(@Param("roleId") Long roleId, @Param("userId") Long userId);
}