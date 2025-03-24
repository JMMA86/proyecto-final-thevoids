package org.thevoids.oncologic.service;

public interface RolePermissionService {
    void assignPermissionToRole(Long permissionId, Long roleId);

    void removePermissionFromRole(Long permissionId, Long roleId);

    boolean roleHasPermission(Long roleId, Long permissionId);
}
