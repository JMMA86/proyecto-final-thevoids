package org.thevoids.oncologic.service;

import java.util.List;

import org.thevoids.oncologic.entity.Role;

public interface AssignedRoles {
    void assignRoleToUser(Long roleId, Long userId);

    void removeRoleFromUser(Long roleId, Long userId);

    void updateRoleForUser(Long roleId, Long userId);

    List<Role> getRolesFromUser(Long userId);
}
