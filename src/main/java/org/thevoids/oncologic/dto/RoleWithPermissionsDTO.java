package org.thevoids.oncologic.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoleWithPermissionsDTO {
    private Long roleId;
    private String roleName;
    private List<PermissionDTO> permissions;

    public RoleWithPermissionsDTO(Long roleId, String roleName, List<PermissionDTO> permissions) {
        this.roleId = roleId;
        this.roleName = roleName;
        this.permissions = permissions;
    }

    public RoleWithPermissionsDTO() {
    }
}
