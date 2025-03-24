package org.thevoids.oncologic.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thevoids.oncologic.entity.AssignedRole;
import org.thevoids.oncologic.entity.Role;
import org.thevoids.oncologic.repository.AssignedRoleRepository;
import org.thevoids.oncologic.repository.RoleRepository;
import org.thevoids.oncologic.repository.UserRepository;
import org.thevoids.oncologic.service.AssignedRoles;

@Service
public class AssignedRolesImpl implements AssignedRoles {
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AssignedRoleRepository assignedRoleRepository;

    @Override
    public void assignRoleToUser(Long roleId, Long userId) {
        var newAssignedRole = new AssignedRole();

        if (!userRepository.existsById(userId)) {
            throw new IllegalArgumentException("User does not exists");
        }

        if (!roleRepository.existsById(roleId)) {
            throw new IllegalArgumentException("Role does not exists");
        }

        if (assignedRoleRepository.existsByRoleIdAndUserId(roleId, userId)) {
            throw new IllegalArgumentException("Role already assigned");
        }

        var user = userRepository.findById(userId).orElse(null);
        var role = roleRepository.findById(roleId).orElse(null);

        newAssignedRole.setUser(user);
        newAssignedRole.setRole(role);

        assignedRoleRepository.save(newAssignedRole);
    }

    @Override
    public void removeRoleFromUser(Long roleId, Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new IllegalArgumentException("User does not exists");
        }

        if (!roleRepository.existsById(roleId)) {
            throw new IllegalArgumentException("Role does not exists");
        }

        if (!assignedRoleRepository.existsByRoleIdAndUserId(roleId, userId)) {
            throw new IllegalArgumentException("This assigment was never made");
        }

        var assignedRole = assignedRoleRepository.findByRoleIdAndUserId(roleId, userId).orElse(null);
        
        assignedRoleRepository.deleteById(assignedRole.getId());
    }

    @Override
    public void updateRoleForUser(Long roleId, Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new IllegalArgumentException("User does not exists");
        }

        if (!roleRepository.existsById(roleId)) {
            throw new IllegalArgumentException("Role does not exists");
        }

        if (!assignedRoleRepository.existsByRoleIdAndUserId(roleId, userId)) {
            throw new IllegalArgumentException("This assigment was never made");
        }

        var assignedRole = assignedRoleRepository.findByRoleIdAndUserId(roleId, userId).orElse(null);
        
        assignedRole.setRole(roleRepository.findById(roleId).orElse(null));

        assignedRoleRepository.save(assignedRole);
    }

    @Override
    public List<Role> getRolesFromUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new IllegalArgumentException("User does not exists");
        }

        return roleRepository.findRolesByUserId(userId);
    }
}
