package org.thevoids.oncologic.service;

import org.thevoids.oncologic.entity.Role;

import java.util.List;

public interface RoleService {
    List<Role> getAllRoles();

    Role createRole(Role role);

    Role deleteRole(Role role);

    Role updateRole(Role role);

    Role getRole(Long roleId);
}
