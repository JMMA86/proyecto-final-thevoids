package org.thevoids.oncologic.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.thevoids.oncologic.dto.ErrorResponse;
import org.thevoids.oncologic.dto.RoleDTO;
import org.thevoids.oncologic.entity.Role;
import org.thevoids.oncologic.entity.User;
import org.thevoids.oncologic.mapper.UserMapper;
import org.thevoids.oncologic.service.AssignedRoles;
import org.thevoids.oncologic.service.RoleService;
import org.thevoids.oncologic.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/roles")
public class RestRoleController {

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserService userService;

    @Autowired
    private AssignedRoles assignedRolesService;

    @Autowired
    private UserMapper userMapper;

    /**
     * Retrieves all roles.
     *
     * @return a list of all roles as DTOs.
     */
    @GetMapping
    public ResponseEntity<?> getAllRoles() {
        try {
            List<Role> roles = roleService.getAllRoles();
            List<RoleDTO> roleDTOs = roles.stream()
                    .map(role -> new RoleDTO(role.getRoleId(), role.getRoleName()))
                    .collect(Collectors.toList());
            return ResponseEntity.ok(roleDTOs);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Failed to retrieve roles", e.getMessage()));
        }
    }

    /**
     * Retrieves a specific role by its ID.
     *
     * @param roleId the ID of the role to retrieve.
     * @return the role with the specified ID as a DTO.
     */
    @GetMapping("/{roleId}")
    public ResponseEntity<?> getRoleById(@PathVariable Long roleId) {
        try {
            Role role = roleService.getRole(roleId);
            RoleDTO roleDTO = new RoleDTO(role.getRoleId(), role.getRoleName());
            return ResponseEntity.ok(roleDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("Role not found", e.getMessage()));
        }
    }

    /**
     * Creates a new role.
     *
     * @param roleDTO the role to create as a DTO.
     * @return the created role as a DTO.
     */
    @PostMapping
    public ResponseEntity<?> createRole(@RequestBody RoleDTO roleDTO) {
        try {
            Role role = new Role();
            role.setRoleName(roleDTO.getRoleName());
            Role createdRole = roleService.createRole(role);
            RoleDTO createdRoleDTO = new RoleDTO(createdRole.getRoleId(), createdRole.getRoleName());
            return ResponseEntity.ok(createdRoleDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse("Failed to create role", e.getMessage()));
        }
    }

    /**
     * Assigns a role to a user.
     *
     * @param userId the ID of the user.
     * @param roleId the ID of the role to assign.
     * @return a response indicating the assignment status.
     */
    @PostMapping("/{roleId}/users/{userId}")
    public ResponseEntity<?> assignRoleToUser(@PathVariable Long userId, @PathVariable Long roleId) {
        try {
            Role role = roleService.getRole(roleId);
            if (role == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ErrorResponse("Role not found", "Role not found"));
            }
            User user = userService.getUserById(userId);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ErrorResponse("User not found", "User not found"));
            }

            assignedRolesService.assignRoleToUser(roleId, userId);
            return ResponseEntity.ok(userMapper.toUserResponseDTO(userService.getUserById(userId)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse("Failed to assign role", e.getMessage()));
        }
    }

    /**
     * Removes a role from a user.
     *
     * @param userId the ID of the user.
     * @param roleId the ID of the role to remove.
     * @return a response indicating the removal status.
     */
    @DeleteMapping("/{roleId}/users/{userId}")
    public ResponseEntity<?> removeRoleFromUser(@PathVariable Long userId, @PathVariable Long roleId) {
        try {
            assignedRolesService.removeRoleFromUser(roleId, userId);
            User user = userService.getUserById(userId);
            return ResponseEntity.ok(userMapper.toUserResponseDTO(user));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse("Failed to remove role", e.getMessage()));
        }
    }
}