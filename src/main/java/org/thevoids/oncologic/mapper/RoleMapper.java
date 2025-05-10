package org.thevoids.oncologic.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import org.thevoids.oncologic.dto.entity.PermissionDTO;
import org.thevoids.oncologic.dto.entity.RoleDTO;
import org.thevoids.oncologic.dto.entity.RoleWithPermissionsDTO;
import org.thevoids.oncologic.entity.Role;
import org.thevoids.oncologic.entity.RolePermission;

@Mapper(componentModel = "spring", uses = PermissionMapper.class)
public interface RoleMapper {

    RoleMapper INSTANCE = Mappers.getMapper(RoleMapper.class);

    RoleDTO toRoleDTO(Role role);

    @Mapping(source = "rolePermissions", target = "permissions", qualifiedByName = "mapRolePermissionsToPermissionDTOs")
    RoleWithPermissionsDTO toRoleWithPermissionsDTO(Role role);

    @Named("mapRolePermissionsToPermissionDTOs")
    static List<PermissionDTO> mapRolePermissionsToPermissionDTOs(List<RolePermission> rolePermissions) {
        return rolePermissions.stream()
                .map(rolePermission -> PermissionMapper.INSTANCE.toPermissionDTO(rolePermission.getPermission()))
                .collect(Collectors.toList());
    }
}
