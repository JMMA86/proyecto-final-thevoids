package org.thevoids.oncologic.mapper;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import org.thevoids.oncologic.dto.RoleDTO;
import org.thevoids.oncologic.dto.UserDTO;
import org.thevoids.oncologic.dto.UserWithRolesDTO;
import org.thevoids.oncologic.entity.AssignedRole;
import org.thevoids.oncologic.entity.Role;
import org.thevoids.oncologic.entity.User;

public class UserMapperUnitTest {

    private final UserMapper userMapper = UserMapper.INSTANCE;

    @Test
    public void testToUserDTO() {
        User user = new User();
        user.setUserId(1L);
        user.setFullName("John Doe");
        user.setEmail("john.doe@example.com");

        UserDTO userDTO = userMapper.toUserDTO(user);

        assertNotNull(userDTO);
        assertEquals(user.getUserId(), userDTO.getUserId());
        assertEquals(user.getFullName(), userDTO.getFullName());
        assertEquals(user.getEmail(), userDTO.getEmail());
    }

    @Test
    public void testToUser() {
        UserDTO userDTO = new UserDTO();
        userDTO.setUserId(1L);
        userDTO.setFullName("John Doe");
        userDTO.setEmail("john.doe@example.com");

        User user = userMapper.toUser(userDTO);

        assertNotNull(user);
        assertEquals(userDTO.getUserId(), user.getUserId());
        assertEquals(userDTO.getFullName(), user.getFullName());
        assertEquals(userDTO.getEmail(), user.getEmail());
    }

    @Test
    public void testToUserWithRolesDTO() {
        User user = new User();
        user.setUserId(1L);
        user.setFullName("John Doe");
        user.setEmail("john.doe@example.com");

        Role role = new Role();
        role.setRoleId(1L);
        role.setRoleName("Admin");

        AssignedRole assignedRole = new AssignedRole();
        assignedRole.setRole(role);
        user.setAssignedRoles(List.of(assignedRole));

        UserWithRolesDTO userWithRolesDTO = userMapper.toUserWithRolesDTO(user);

        assertNotNull(userWithRolesDTO);
        assertEquals(user.getUserId(), userWithRolesDTO.getUserId());
        assertEquals(user.getFullName(), userWithRolesDTO.getFullName());
        assertEquals(user.getEmail(), userWithRolesDTO.getEmail());
        assertNotNull(userWithRolesDTO.getRoles());
        assertEquals(1, userWithRolesDTO.getRoles().size());
        assertEquals(role.getRoleId(), userWithRolesDTO.getRoles().get(0).getRoleId());
        assertEquals(role.getRoleName(), userWithRolesDTO.getRoles().get(0).getRoleName());
    }

    @Test
    public void testMapAssignedRolesToRoleDTOs() {
        Role role1 = new Role();
        role1.setRoleId(1L);
        role1.setRoleName("Admin");

        Role role2 = new Role();
        role2.setRoleId(2L);
        role2.setRoleName("Doctor");

        AssignedRole assignedRole1 = new AssignedRole();
        assignedRole1.setRole(role1);

        AssignedRole assignedRole2 = new AssignedRole();
        assignedRole2.setRole(role2);

        List<RoleDTO> roleDTOs = UserMapper.mapAssignedRolesToRoleDTOs(List.of(assignedRole1, assignedRole2));

        assertNotNull(roleDTOs);
        assertEquals(2, roleDTOs.size());
        assertEquals(role1.getRoleId(), roleDTOs.get(0).getRoleId());
        assertEquals(role1.getRoleName(), roleDTOs.get(0).getRoleName());
        assertEquals(role2.getRoleId(), roleDTOs.get(1).getRoleId());
        assertEquals(role2.getRoleName(), roleDTOs.get(1).getRoleName());
    }
}