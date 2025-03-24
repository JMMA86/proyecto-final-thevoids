package org.thevoids.oncologic.service;

import java.util.Optional;

import org.thevoids.oncologic.entity.Permission;

public interface PermissionService {
    void createPermission(Permission permission);

    void deletePermission(Long permissionId);

    void updatePermission(Permission permission);

    Optional<Permission> getPermission(Long permissionId);
}
