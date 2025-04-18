package org.thevoids.oncologic.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.thevoids.oncologic.dto.PermissionDTO;
import org.thevoids.oncologic.entity.Permission;

@Mapper(componentModel = "spring")
public interface PermissionMapper {

    PermissionMapper INSTANCE = Mappers.getMapper(PermissionMapper.class);

    PermissionDTO toPermissionDTO(Permission permission);

    Permission toPermission(PermissionDTO permissionDTO);
}
