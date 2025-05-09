package org.thevoids.oncologic.dto.entity;

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

    public UserWithRolesDTO(Long userId, String fullName, String email, List<RoleDTO> roles) {
        this.userId = userId;
        this.fullName = fullName;
        this.email = email;
        this.roles = roles;
    }

    public UserWithRolesDTO() {
    }

    public boolean hasRole(long roleId) {
        return roles.stream().anyMatch(role -> role.getRoleId() == roleId);
    }
} 