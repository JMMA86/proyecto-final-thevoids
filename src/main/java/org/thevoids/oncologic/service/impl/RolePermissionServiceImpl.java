package org.thevoids.oncologic.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.thevoids.oncologic.entity.Permission;
import org.thevoids.oncologic.entity.RolePermission;
import org.thevoids.oncologic.repository.PermissionRepository;
import org.thevoids.oncologic.repository.RolePermissionRepository;
import org.thevoids.oncologic.repository.RoleRepository;
import org.thevoids.oncologic.service.RolePermissionService;

@Service
public class RolePermissionServiceImpl implements RolePermissionService {
    private final RolePermissionRepository rolePermissionRepository;
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    public RolePermissionServiceImpl(
            RolePermissionRepository rolePermissionRepository,
            RoleRepository roleRepository,
            PermissionRepository permissionRepository
    ) {
        this.rolePermissionRepository = rolePermissionRepository;
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
    }

    @Override
    public void assignPermissionToRole(Long permissionId, Long roleId) {
        if (!permissionRepository.existsById(permissionId)) {
            throw new IllegalArgumentException("Permission does not exist");
        }

        if (!roleRepository.existsById(roleId)) {
            throw new IllegalArgumentException("Role does not exist");
        }

        if (rolePermissionRepository.existsByRoleIdAndPermissionId(roleId, permissionId)) {
            throw new IllegalArgumentException("Role already has this permission");
        }

        // Ensure the Permission and Role are managed entities
        var role = roleRepository.findById(roleId)
                .orElseThrow(() -> new IllegalArgumentException("Role does not exist"));
        var permission = permissionRepository.findById(permissionId)
                .orElseThrow(() -> new IllegalArgumentException("Permission does not exist"));

        var newPermission = new RolePermission();
        newPermission.setRole(role);
        newPermission.setPermission(permission);

        // Save the RolePermission
        rolePermissionRepository.save(newPermission);
    }

    @Override
    public void removePermissionFromRole(Long permissionId, Long roleId) {
        if (!permissionRepository.existsById(permissionId)) {
            throw new IllegalArgumentException("Permission does not exist");
        }

        if (!roleRepository.existsById(roleId)) {
            throw new IllegalArgumentException("Role does not exist");
        }

        if (!rolePermissionRepository.existsByRoleIdAndPermissionId(roleId, permissionId)) {
            throw new IllegalArgumentException("This permission is not assigned to the role");
        }

        // Ensure the Permission entity is managed
        var permission = permissionRepository.findById(permissionId)
                .orElseThrow(() -> new IllegalArgumentException("Permission does not exist"));

        var rolePermission = rolePermissionRepository.findByRoleIdAndPermissionId(roleId, permissionId)
                .orElseThrow(() -> new IllegalArgumentException("This permission is not assigned to the role"));

        rolePermission.setPermission(permission); // Ensure the permission is attached to the session
        rolePermissionRepository.delete(rolePermission);
    }

    @Override
    public boolean roleHasPermission(Long roleId, Long permissionId) {
        if (!roleRepository.existsById(roleId)) {
            throw new IllegalArgumentException("Role does not exist");
        }
        
        if (!permissionRepository.existsById(permissionId)) {
            throw new IllegalArgumentException("Permission does not exist");
        }

        return rolePermissionRepository.existsByRoleIdAndPermissionId(roleId, permissionId);
    }

    @Override
    public void updatePermissionForRole(Long lastPermissionId, Long newPermissionId, Long roleId) {
        if (!roleRepository.existsById(roleId)) {
            throw new IllegalArgumentException("Role does not exist");
        }

        if (!permissionRepository.existsById(lastPermissionId)) {
            throw new IllegalArgumentException("Last permission does not exist");
        }

        if (!permissionRepository.existsById(newPermissionId)) {
            throw new IllegalArgumentException("New permission does not exist");
        }

        if (!rolePermissionRepository.existsByRoleIdAndPermissionId(roleId, lastPermissionId)) {
            throw new IllegalArgumentException("This role does not have this permission");
        }

        var rolePermission = rolePermissionRepository.findByRoleIdAndPermissionId(roleId, lastPermissionId).orElse(null);

        rolePermission.setPermission(permissionRepository.findById(newPermissionId).orElse(null));

        rolePermissionRepository.save(rolePermission);
    }

    @Override
    public List<Permission> getPermissionsFromRole(Long roleId) {
        if (!roleRepository.existsById(roleId)) {
            throw new IllegalArgumentException("Role does not exist");
        }

        return rolePermissionRepository.getPermissionsFromRole(roleId);
    }

    @Override
    public List<Permission> getAllPermissions() {
        return permissionRepository.findAll();
    }
}
