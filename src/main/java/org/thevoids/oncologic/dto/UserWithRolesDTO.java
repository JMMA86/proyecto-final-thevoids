package org.thevoids.oncologic.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserWithRolesDTO {
    private Long userId;
    private String fullName;
    private String email;
    private List<RoleDTO> roles;

    public void setRoles(List<RoleDTO> roles) {
        this.roles = roles;
    }
    public List<RoleDTO> getRoles() {
        return roles;
    }

    public boolean hasRole(long roleId) {
        return roles.stream().anyMatch(role -> role.getRoleId() == roleId);
    }
}
