package org.thevoids.oncologic.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thevoids.oncologic.entity.Permission;
import org.thevoids.oncologic.repository.PermissionRepository;
import org.thevoids.oncologic.service.PermissionService;

@Service
public class PermissionServiceImpl implements PermissionService {
    @Autowired
    private PermissionRepository permissionRepository;

    @Override
    public void createPermission(Permission permission) {
        if (permissionRepository.existsById(permission.getPermissionId())) {
            throw new IllegalArgumentException("Permission already exists");
        }

        permissionRepository.save(permission);
    }

    @Override
    public void deletePermission(Long permissionId) {
        if (!permissionRepository.existsById(permissionId)) {
            throw new IllegalArgumentException("Permission does not exist");
        }

        permissionRepository.deleteById(permissionId);
    }
}
