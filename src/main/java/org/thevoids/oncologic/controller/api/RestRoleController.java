package org.thevoids.oncologic.controller.api;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.thevoids.oncologic.dto.ErrorResponse;
import org.thevoids.oncologic.dto.PermissionDTO;
import org.thevoids.oncologic.dto.RoleDTO;
import org.thevoids.oncologic.dto.RoleWithPermissionsDTO;
import org.thevoids.oncologic.entity.Role;
import org.thevoids.oncologic.mapper.PermissionMapper;
import org.thevoids.oncologic.mapper.RoleMapper;
import org.thevoids.oncologic.mapper.UserMapper;
import org.thevoids.oncologic.service.AssignedRoles;
import org.thevoids.oncologic.service.RolePermissionService;
import org.thevoids.oncologic.service.RoleService;
import org.thevoids.oncologic.service.UserService;

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
    private RolePermissionService rolePermissionService;

    @Autowired
    private UserMapper userMapper;
    
    @Autowired
    private RoleMapper roleMapper;
    
    @Autowired
    private PermissionMapper permissionMapper;

    /**
     * Retrieves all roles.
     *
     * @return a list of all roles as DTOs.
     */
    @PreAuthorize("hasAuthority('VIEW_ROLES')")
    @GetMapping
    public ResponseEntity<?> getAllRoles() {
        try {
            List<Role> roles = roleService.getAllRoles();
            List<RoleDTO> roleDTOs = roles.stream()
                    .map(roleMapper::toRoleDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(roleDTOs);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Failed to retrieve roles", e.getMessage()));
        }
    }

    /**
     * Retrieves all roles with their permissions.
     *
     * @return a list of all roles with permissions as DTOs.
     */
    @PreAuthorize("hasAuthority('VIEW_ROLES')")
    @GetMapping("/with-permissions")
    public ResponseEntity<?> getAllRolesWithPermissions() {
        try {
            List<Role> roles = roleService.getAllRoles();
            List<RoleWithPermissionsDTO> roleWithPermissionsDTOs = roles.stream()
                    .map(roleMapper::toRoleWithPermissionsDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(roleWithPermissionsDTOs);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Failed to retrieve roles with permissions", e.getMessage()));
        }
    }

    /**
     * Retrieves a specific role by its ID.
     *
     * @param roleId the ID of the role to retrieve.
     * @return the role with the specified ID as a DTO.
     */
    @PreAuthorize("hasAuthority('VIEW_ROLES')")
    @GetMapping("/{roleId}")
    public ResponseEntity<?> getRoleById(@PathVariable Long roleId) {
        try {
            Role role = roleService.getRole(roleId);
            RoleDTO roleDTO = roleMapper.toRoleDTO(role);
            return ResponseEntity.ok(roleDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("Role not found", e.getMessage()));
        }
    }

    /**
     * Retrieves a specific role with permissions by its ID.
     *
     * @param roleId the ID of the role to retrieve.
     * @return the role with permissions with the specified ID as a DTO.
     */
    @PreAuthorize("hasAuthority('VIEW_ROLES')")
    @GetMapping("/{roleId}/with-permissions")
    public ResponseEntity<?> getRoleWithPermissionsById(@PathVariable Long roleId) {
        try {
            Role role = roleService.getRole(roleId);
            RoleWithPermissionsDTO roleWithPermissionsDTO = roleMapper.toRoleWithPermissionsDTO(role);
            return ResponseEntity.ok(roleWithPermissionsDTO);
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
    @PreAuthorize("hasAuthority('ADD_ROLES')")
    @PostMapping
    public ResponseEntity<?> createRole(@RequestBody RoleDTO roleDTO) {
        try {
            Role role = new Role();
            role.setRoleName(roleDTO.getRoleName());
            Role createdRole = roleService.createRole(role);
            RoleDTO createdRoleDTO = roleMapper.toRoleDTO(createdRole);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdRoleDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse("Failed to create role", e.getMessage()));
        }
    }

    /**
     * Updates an existing role.
     *
     * @param roleId the ID of the role to update.
     * @param roleDTO the updated role data.
     * @return the updated role as a DTO.
     */
    @PreAuthorize("hasAuthority('EDIT_ROLES')")
    @PutMapping("/{roleId}")
    public ResponseEntity<?> updateRole(@PathVariable Long roleId, @RequestBody RoleDTO roleDTO) {
        try {
            Role role = roleService.getRole(roleId);
            role.setRoleName(roleDTO.getRoleName());
            Role updatedRole = roleService.updateRole(role);
            RoleDTO updatedRoleDTO = roleMapper.toRoleDTO(updatedRole);
            return ResponseEntity.ok(updatedRoleDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse("Failed to update role", e.getMessage()));
        }
    }

    /**
     * Deletes a role.
     *
     * @param roleId the ID of the role to delete.
     * @return a success or error response.
     */
    @PreAuthorize("hasAuthority('DELETE_ROLES')")
    @DeleteMapping("/{roleId}")
    public ResponseEntity<?> deleteRole(@PathVariable Long roleId) {
        try {
            Role role = roleService.getRole(roleId);
            roleService.deleteRole(role);
            return ResponseEntity.ok().body(new ErrorResponse("Success", "Role deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse("Failed to delete role", e.getMessage()));
        }
    }

    /**
     * Assigns a role to a user.
     *
     * @param userId the ID of the user.
     * @param roleId the ID of the role to assign.
     * @return a response indicating the assignment status.
     */
    @PreAuthorize("hasAuthority('MANAGE_USER_ROLES')")
    @PostMapping("/{roleId}/users/{userId}")
    public ResponseEntity<?> assignRoleToUser(@PathVariable Long userId, @PathVariable Long roleId) {
        try {
            assignedRolesService.assignRoleToUser(roleId, userId);
            return ResponseEntity.ok(userMapper.toUserDTO(userService.getUserById(userId)));
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
    @PreAuthorize("hasAuthority('MANAGE_USER_ROLES')")
    @DeleteMapping("/{roleId}/users/{userId}")
    public ResponseEntity<?> removeRoleFromUser(@PathVariable Long userId, @PathVariable Long roleId) {
        try {
            assignedRolesService.removeRoleFromUser(roleId, userId);
            return ResponseEntity.ok(userMapper.toUserDTO(userService.getUserById(userId)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse("Failed to remove role", e.getMessage()));
        }
    }

    /**
     * Retrieves all permissions for a role.
     *
     * @param roleId the ID of the role.
     * @return the list of permissions for the role.
     */
    @PreAuthorize("hasAuthority('MANAGE_ROLE_PERMISSIONS')")
    @GetMapping("/{roleId}/permissions")
    public ResponseEntity<?> getRolePermissions(@PathVariable Long roleId) {
        try {
            Role role = roleService.getRole(roleId);
            RoleWithPermissionsDTO roleWithPermissionsDTO = roleMapper.toRoleWithPermissionsDTO(role);
            return ResponseEntity.ok(roleWithPermissionsDTO.getPermissions());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("Role not found", e.getMessage()));
        }
    }

    /**
     * Assigns a permission to a role.
     *
     * @param roleId the ID of the role.
     * @param permissionId the ID of the permission to assign.
     * @return a response with the updated role.
     */
    @PreAuthorize("hasAuthority('MANAGE_ROLE_PERMISSIONS')")
    @PostMapping("/{roleId}/permissions/{permissionId}")
    public ResponseEntity<?> assignPermissionToRole(@PathVariable Long roleId, @PathVariable Long permissionId) {
        try {
            rolePermissionService.assignPermissionToRole(permissionId, roleId);
            return ResponseEntity.ok(roleMapper.toRoleWithPermissionsDTO(roleService.getRole(roleId)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse("Failed to assign permission to role", e.getMessage()));
        }
    }

    /**
     * Removes a permission from a role.
     *
     * @param roleId the ID of the role.
     * @param permissionId the ID of the permission to remove.
     * @return a response with the updated role.
     */
    @PreAuthorize("hasAuthority('MANAGE_ROLE_PERMISSIONS')")
    @DeleteMapping("/{roleId}/permissions/{permissionId}")
    public ResponseEntity<?> removePermissionFromRole(@PathVariable Long roleId, @PathVariable Long permissionId) {
        try {
            rolePermissionService.removePermissionFromRole(permissionId, roleId);
            return ResponseEntity.ok(roleMapper.toRoleWithPermissionsDTO(roleService.getRole(roleId)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse("Failed to remove permission from role", e.getMessage()));
        }
    }

    /**
     * Get available permissions for a role (permissions not already assigned).
     *
     * @param roleId the ID of the role.
     * @return a list of available permissions for the role.
     */
    @PreAuthorize("hasAuthority('MANAGE_ROLE_PERMISSIONS')")
    @GetMapping("/{roleId}/available-permissions")
    public ResponseEntity<?> getAvailablePermissionsForRole(@PathVariable Long roleId) {
        try {
            Role role = roleService.getRole(roleId);
            RoleWithPermissionsDTO roleWithPermissionsDTO = roleMapper.toRoleWithPermissionsDTO(role);
            
            List<Long> assignedPermissionIds = roleWithPermissionsDTO.getPermissions().stream()
                    .map(PermissionDTO::getPermissionId)
                    .collect(Collectors.toList());
            
            List<PermissionDTO> availablePermissions = rolePermissionService.getAllPermissions().stream()
                    .filter(p -> !assignedPermissionIds.contains(p.getPermissionId()))
                    .map(permissionMapper::toPermissionDTO)
                    .collect(Collectors.toList());
            
            return ResponseEntity.ok(availablePermissions);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse("Failed to get available permissions", e.getMessage()));
        }
    }
}