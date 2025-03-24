package org.thevoids.oncologic.service;

import org.thevoids.oncologic.entity.Role;

import java.util.List;

public interface RoleService {
    List<Role> getAllRoles();

    void createRole(Role role);

    void deleteRole(Role role);

    void updateRole(Role role);

    Role getRole(Long roleId);
}
