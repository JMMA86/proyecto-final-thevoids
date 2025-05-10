package org.thevoids.oncologic.dto.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PermissionDTO {
    private Long permissionId;
    private String permissionName;

    public PermissionDTO(Long permissionId, String permissionName) {
        this.permissionId = permissionId;
        this.permissionName = permissionName;
    }

    public PermissionDTO() {
    }
} 