package org.thevoids.oncologic.service;

public interface AssignedRoles {
    void assignRoleToUser(Long roleId, Long userId);

    void removeRoleFromUser(Long roleId, Long userId);
}
