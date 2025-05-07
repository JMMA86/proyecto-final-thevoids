package org.thevoids.oncologic.controller.api;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.thevoids.oncologic.dto.ErrorResponse;
import org.thevoids.oncologic.dto.RoleDTO;
import org.thevoids.oncologic.dto.UserDTO;
import org.thevoids.oncologic.dto.UserWithRolesDTO;
import org.thevoids.oncologic.entity.Role;
import org.thevoids.oncologic.entity.User;
import org.thevoids.oncologic.mapper.RoleMapper;
import org.thevoids.oncologic.mapper.UserMapper;
import org.thevoids.oncologic.service.AssignedRoles;
import org.thevoids.oncologic.service.RoleService;
import org.thevoids.oncologic.service.UserService;

@RestController
@RequestMapping("/api/v1/users")
public class RestUserController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RoleService roleService;
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private AssignedRoles assignedRolesService;

    /**
     * Retrieves a list of all users in the system.
     *
     * @return A {@link ResponseEntity} containing a list of {@link UserDTO} objects.
     */
    @PreAuthorize("hasAuthority('VIEW_USERS')")
    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<User> userList = userService.getAllUsers();
        List<UserDTO> UserDTOList = userList.stream()
                .map(userMapper::toUserDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(UserDTOList);
    }

    /**
     * Retrieves a list of all users with their roles.
     *
     * @return A {@link ResponseEntity} containing a list of {@link UserWithRolesDTO} objects.
     */
    @PreAuthorize("hasAuthority('VIEW_USERS')")
    @GetMapping("/with-roles")
    public ResponseEntity<List<UserWithRolesDTO>> getAllUsersWithRoles() {
        List<User> userList = userService.getAllUsers();
        List<UserWithRolesDTO> userWithRolesDTOList = userList.stream()
                .map(userMapper::toUserWithRolesDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(userWithRolesDTOList);
    }

    /**
     * Retrieves a user by ID.
     *
     * @param userId The ID of the user to retrieve.
     * @return A {@link ResponseEntity} containing the requested {@link UserDTO} or an error response.
     */
    @PreAuthorize("hasAuthority('VIEW_USERS')")
    @GetMapping("/{userId}")
    public ResponseEntity<?> getUserById(@PathVariable Long userId) {
        try {
            User user = userService.getUserById(userId);
            UserDTO userDTO = userMapper.toUserDTO(user);
            return ResponseEntity.ok(userDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("User not found", e.getMessage()));
        }
    }

    /**
     * Retrieves a user with roles by ID.
     *
     * @param userId The ID of the user to retrieve.
     * @return A {@link ResponseEntity} containing the requested {@link UserWithRolesDTO} or an error response.
     */
    @PreAuthorize("hasAuthority('VIEW_USERS')")
    @GetMapping("/{userId}/with-roles")
    public ResponseEntity<?> getUserWithRolesById(@PathVariable Long userId) {
        try {
            User user = userService.getUserById(userId);
            UserWithRolesDTO userWithRolesDTO = userMapper.toUserWithRolesDTO(user);
            return ResponseEntity.ok(userWithRolesDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("User not found", e.getMessage()));
        }
    }

    /**
     * Registers a new user in the system.
     *
     * @param user The {@link User} object containing the user's details.
     * @return A {@link ResponseEntity} containing the created {@link UserDTO} object or an error response.
     */
    @PreAuthorize("hasAuthority('ADD_USERS')")
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@ModelAttribute UserDTO userDTO, @RequestParam Long roleId, Model model) {
        try {
            User user = userMapper.toUser(userDTO);
            User createdUser = userService.createUser(user);
            assignedRolesService.assignRoleToUser(roleId, createdUser.getUserId());
            return ResponseEntity.ok(userDTO);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    /**
     * Retrieves all available roles for a user (roles not already assigned).
     *
     * @param userId The ID of the user.
     * @return A {@link ResponseEntity} containing a list of available {@link RoleDTO} objects.
     */
    @PreAuthorize("hasAuthority('MANAGE_USER_ROLES')")
    @GetMapping("/{userId}/available-roles")
    public ResponseEntity<?> getAvailableRolesForUser(@PathVariable Long userId) {
        try {
            User user = userService.getUserById(userId);
            UserWithRolesDTO userWithRolesDTO = userMapper.toUserWithRolesDTO(user);
            
            // Get all roles
            List<Role> allRoles = roleService.getAllRoles();
            
            // Filter out roles already assigned to user
            List<RoleDTO> availableRoles = allRoles.stream()
                    .map(roleMapper::toRoleDTO)
                    .filter(role -> !userWithRolesDTO.hasRole(role.getRoleId()))
                    .collect(Collectors.toList());
            
            return ResponseEntity.ok(availableRoles);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("Failed to get available roles", e.getMessage()));
        }
    }

    /**
     * Assigns a role to a user.
     *
     * @param userId The ID of the user.
     * @param roleId The ID of the role to assign.
     * @return A {@link ResponseEntity} containing the updated user with roles.
     */
    @PreAuthorize("hasAuthority('MANAGE_USER_ROLES')")
    @PostMapping("/{userId}/roles/{roleId}")
    public ResponseEntity<?> assignRoleToUser(@PathVariable Long userId, @PathVariable Long roleId) {
        try {
            // Assign role to user
            assignedRolesService.assignRoleToUser(roleId, userId);
            
            // Return updated user with roles
            UserWithRolesDTO updatedUser = userMapper.toUserWithRolesDTO(userService.getUserById(userId));
            return ResponseEntity.ok(updatedUser);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse("Failed to assign role", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Server error", e.getMessage()));
        }
    }

    /**
     * Removes a role from a user.
     *
     * @param userId The ID of the user.
     * @param roleId The ID of the role to remove.
     * @return A {@link ResponseEntity} containing the updated user with roles.
     */
    @PreAuthorize("hasAuthority('MANAGE_USER_ROLES')")
    @DeleteMapping("/{userId}/roles/{roleId}")
    public ResponseEntity<?> removeRoleFromUser(@PathVariable Long userId, @PathVariable Long roleId) {
        try {
            // Remove role from user
            assignedRolesService.removeRoleFromUser(roleId, userId);
            
            // Return updated user with roles
            UserWithRolesDTO updatedUser = userMapper.toUserWithRolesDTO(userService.getUserById(userId));
            return ResponseEntity.ok(updatedUser);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse("Failed to remove role", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Server error", e.getMessage()));
        }
    }

    /**
     * Retrieves all roles assigned to a user.
     * 
     * @param userId The ID of the user.
     * @return A {@link ResponseEntity} containing a list of {@link RoleDTO} objects.
     */
    @PreAuthorize("hasAuthority('MANAGE_USER_ROLES')")
    @GetMapping("/{userId}/roles")
    public ResponseEntity<?> getUserRoles(@PathVariable Long userId) {
        try {
            // Get roles for the user
            List<Role> roles = assignedRolesService.getRolesFromUser(userId);
            List<RoleDTO> roleDTOs = roles.stream()
                    .map(roleMapper::toRoleDTO)
                    .collect(Collectors.toList());
            
            return ResponseEntity.ok(roleDTOs);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Failed to get user roles", e.getMessage()));
        }
    }
}