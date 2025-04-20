package org.thevoids.oncologic.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import org.thevoids.oncologic.dto.RoleDTO;
import org.thevoids.oncologic.dto.UserDTO;
import org.thevoids.oncologic.dto.UserDTO;
import org.thevoids.oncologic.dto.UserWithRolesDTO;
import org.thevoids.oncologic.entity.AssignedRole;
import org.thevoids.oncologic.entity.User;

@Mapper(componentModel = "spring", uses = RoleMapper.class)
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserDTO toUserDTO(User user);

    User toUser(UserDTO userDTO);

    @Mapping(source = "assignedRoles", target = "roles", qualifiedByName = "mapAssignedRolesToRoleDTOs")
    UserWithRolesDTO toUserWithRolesDTO(User user);

    @Named("mapAssignedRolesToRoleDTOs")
    static List<RoleDTO> mapAssignedRolesToRoleDTOs(List<AssignedRole> assignedRoles) {
        return assignedRoles.stream()
                .map(assignedRole -> RoleMapper.INSTANCE.toRoleDTO(assignedRole.getRole()))
                .collect(Collectors.toList());
    }
}
