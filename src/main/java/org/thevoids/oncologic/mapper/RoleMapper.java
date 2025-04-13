package org.thevoids.oncologic.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.thevoids.oncologic.dto.RoleDTO;
import org.thevoids.oncologic.entity.Role;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    RoleMapper INSTANCE = Mappers.getMapper(RoleMapper.class);

    RoleDTO toRoleDTO(Role role);
}
