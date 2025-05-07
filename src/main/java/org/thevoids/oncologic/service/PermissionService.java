package org.thevoids.oncologic.service;

import java.util.List;
import java.util.Optional;

import org.thevoids.oncologic.entity.Permission;

public interface PermissionService {
    List<Permission> getAllPermissions();

    Permission createPermission(Permission permission);

    Permission deletePermission(Long permissionId);

    Permission updatePermission(Permission permission);

    Optional<Permission> getPermission(Long permissionId);
}
