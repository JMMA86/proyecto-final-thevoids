package org.thevoids.oncologic.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thevoids.oncologic.entity.Role;
import org.thevoids.oncologic.exception.ResourceAlreadyExistsException;
import org.thevoids.oncologic.exception.ResourceNotFoundException;
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
            throw new ResourceAlreadyExistsException("Rol", "id", role.getRoleId());
        }

        roleRepository.save(role);

        return role;
    }

    @Override
    public Role deleteRole(Role role) {
        if (!roleRepository.existsById(role.getRoleId())) {
            throw new ResourceNotFoundException("Rol", "id", role.getRoleId());
        }

        roleRepository.delete(role);

        return role;
    }

    @Override
    public Role updateRole(Role role) {
        if (!roleRepository.existsById(role.getRoleId())) {
            throw new ResourceNotFoundException("Rol", "id", role.getRoleId());
        }

        roleRepository.save(role);

        return role;
    }

    @Override
    @Transactional
    public Role getRole(Long roleId) {
        return roleRepository.findById(roleId)
                .orElseThrow(() -> new ResourceNotFoundException("Rol", "id", roleId));
    }
}
