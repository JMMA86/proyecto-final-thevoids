package org.thevoids.oncologic.service.impl;

import java.util.List;
import java.util.Optional;

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
    public List<Permission> getAllPermissions() {
        return permissionRepository.findAll();
    }

    @Override
    public Permission createPermission(Permission permission) {
        if (permission.getPermissionId() != null && permissionRepository.existsById(permission.getPermissionId())) {
            throw new IllegalArgumentException("Permission already exists");
        }

        permissionRepository.save(permission);

        return permission;
    }

    @Override
    public Permission deletePermission(Long permissionId) {
        if (!permissionRepository.existsById(permissionId)) {
            throw new IllegalArgumentException("Permission does not exist");
        }

        Permission permission = permissionRepository.findById(permissionId)
                .orElseThrow(() -> new IllegalArgumentException("Permission not found"));

        permissionRepository.deleteById(permissionId);

        return permission;
    }

    @Override
    public Permission updatePermission(Permission permission) {
        if (!permissionRepository.existsById(permission.getPermissionId())) {
            throw new IllegalArgumentException("Permission does not exist");
        }

        permissionRepository.save(permission);

        return permission;
    }

    @Override
    public Optional<Permission> getPermission(Long permissionId) {
        if (!permissionRepository.existsById(permissionId)) {
            throw new IllegalArgumentException("Permission does not exist");
        }

        return permissionRepository.findById(permissionId);
    }
}