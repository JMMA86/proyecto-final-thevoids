package org.thevoids.oncologic.service;

import java.util.List;

import org.thevoids.oncologic.entity.Permission;

public interface RolePermissionService {
    void assignPermissionToRole(Long permissionId, Long roleId);

    void removePermissionFromRole(Long permissionId, Long roleId);

    boolean roleHasPermission(Long roleId, Long permissionId);

    void updatePermissionForRole(Long lastPermissionId, Long newPermissionId, Long roleId);

    List<Permission> getPermissionsFromRole(Long roleId);

    List<Permission> getAllPermissions();
}
