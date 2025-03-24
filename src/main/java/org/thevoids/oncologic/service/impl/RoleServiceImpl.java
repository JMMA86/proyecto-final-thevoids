package org.thevoids.oncologic.service.impl;

import org.springframework.stereotype.Service;
import org.thevoids.oncologic.entity.Role;
import org.thevoids.oncologic.repository.RoleRepository;
import org.thevoids.oncologic.service.RoleService;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    @Override
    public void createRole(Role role) {
        if (roleRepository.existsById(role.getRoleId())) {
            throw new IllegalArgumentException("Role with id " + role.getRoleId() + " already exists");
        }

        if (role.getRolePermissions() == null || role.getRolePermissions().isEmpty()) {
            throw new IllegalArgumentException("Role must have at least one permission");
        }

        roleRepository.save(role);
    }

    @Override
    public void deleteRole(Role role) {
        if (!roleRepository.existsById(role.getRoleId())) {
            throw new IllegalArgumentException("Role with id " + role.getRoleId() + " does not exist");
        }

        roleRepository.delete(role);
    }
}
