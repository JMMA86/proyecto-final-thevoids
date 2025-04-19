package org.thevoids.oncologic.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thevoids.oncologic.entity.Role;
import org.thevoids.oncologic.repository.RoleRepository;
import org.thevoids.oncologic.service.RoleService;

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
    public Role createRole(Role role) {
        if (role.getRoleId() != null && roleRepository.existsById(role.getRoleId())) {
            throw new IllegalArgumentException("Role with id " + role.getRoleId() + " already exists");
        }

        roleRepository.save(role);

        return role;
    }

    @Override
    public void deleteRole(Role role) {
        if (!roleRepository.existsById(role.getRoleId())) {
            throw new IllegalArgumentException("Role with id " + role.getRoleId() + " does not exist");
        }

        roleRepository.delete(role);
    }

    @Override
    public void updateRole(Role role) {
        if (!roleRepository.existsById(role.getRoleId())) {
            throw new IllegalArgumentException("Role with id " + role.getRoleId() + " does not exist");
        }

        roleRepository.save(role);
    }

    @Override
    @Transactional
    public Role getRole(Long roleId) {
        return roleRepository.findById(roleId).orElseThrow(() -> new IllegalArgumentException("Role not found"));
    }
}
