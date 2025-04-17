package org.thevoids.oncologic.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.thevoids.oncologic.dto.UserResponseDTO;
import org.thevoids.oncologic.entity.Role;
import org.thevoids.oncologic.entity.User;

public class UserMapperUnitTest {

    @Test
    public void testToUserResponseDTO() {
        // Arrange
        Role role = new Role();
        role.setRoleId(1L);
        role.setRoleName("ADMIN");

        User user = new User();
        user.setUserId(1L);
        user.setFullName("John Doe");
        user.setIdentification("123456789");
        user.setEmail("johndoe@example.com");
        user.setRole(role);

        UserMapper userMapper = UserMapper.INSTANCE;

        // Act
        UserResponseDTO userResponseDTO = userMapper.toUserResponseDTO(user);

        // Assert
        assertEquals(user.getUserId(), userResponseDTO.getUserId());
        assertEquals(user.getIdentification(), userResponseDTO.getIdentification());
        assertEquals(user.getFullName(), userResponseDTO.getFullName());
        assertEquals(user.getEmail(), userResponseDTO.getEmail());
        assertEquals(user.getRole().getRoleName(), userResponseDTO.getRoleName());
    }

    @Test
    public void testToUserResponseDTOWithMock() {
        // Arrange
        Role role = mock(Role.class);
        when(role.getRoleName()).thenReturn("USER");

        User user = mock(User.class);
        when(user.getUserId()).thenReturn(2L);
        when(user.getFullName()).thenReturn("Jane Doe");
        when(user.getIdentification()).thenReturn("987654321");
        when(user.getEmail()).thenReturn("janedoe@example.com");
        when(user.getRole()).thenReturn(role);

        UserMapper userMapper = UserMapper.INSTANCE;

        // Act
        UserResponseDTO userResponseDTO = userMapper.toUserResponseDTO(user);

        // Assert
        assertEquals(2L, userResponseDTO.getUserId());
        assertEquals("987654321", userResponseDTO.getIdentification());
        assertEquals("Jane Doe", userResponseDTO.getFullName());
        assertEquals("janedoe@example.com", userResponseDTO.getEmail());
        assertEquals("USER", userResponseDTO.getRoleName());
    }
}