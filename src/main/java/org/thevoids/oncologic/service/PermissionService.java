package org.thevoids.oncologic.service;

import org.thevoids.oncologic.entity.Permission;

public interface PermissionService {
    void createPermission(Permission permission);

    void deletePermission(Long permissionId);
}
