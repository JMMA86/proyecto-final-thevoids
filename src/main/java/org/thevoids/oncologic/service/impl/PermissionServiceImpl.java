package org.thevoids.oncologic.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thevoids.oncologic.entity.Permission;
import org.thevoids.oncologic.exception.ResourceAlreadyExistsException;
import org.thevoids.oncologic.exception.ResourceNotFoundException;
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
            throw new ResourceAlreadyExistsException("Permiso", "id", permission.getPermissionId());
        }

        permissionRepository.save(permission);

        return permission;
    }

    @Override
    public Permission deletePermission(Long permissionId) {
        if (!permissionRepository.existsById(permissionId)) {
            throw new ResourceNotFoundException("Permiso", "id", permissionId);
        }

        Permission permission = permissionRepository.findById(permissionId)
                .orElseThrow(() -> new ResourceNotFoundException("Permiso", "id", permissionId));

        permissionRepository.delete(permission);

        return permission;
    }

    @Override
    public Permission updatePermission(Permission permission) {
        if (!permissionRepository.existsById(permission.getPermissionId())) {
            throw new ResourceNotFoundException("Permiso", "id", permission.getPermissionId());
        }

        permissionRepository.save(permission);

        return permission;
    }

    @Override
    public Optional<Permission> getPermission(Long permissionId) {
        if (!permissionRepository.existsById(permissionId)) {
            throw new ResourceNotFoundException("Permiso", "id", permissionId);
        }

        return permissionRepository.findById(permissionId);
    }
}