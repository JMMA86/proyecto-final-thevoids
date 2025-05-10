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
import org.thevoids.oncologic.dto.entity.RoleDTO;
import org.thevoids.oncologic.dto.entity.RoleWithPermissionsDTO;
import org.thevoids.oncologic.entity.Role;
import org.thevoids.oncologic.exception.InvalidOperationException;
import org.thevoids.oncologic.exception.ResourceAlreadyExistsException;
import org.thevoids.oncologic.exception.ResourceNotFoundException;
import org.thevoids.oncologic.mapper.RoleMapper;
import org.thevoids.oncologic.service.RolePermissionService;
import org.thevoids.oncologic.service.RoleService;

@RestController
@RequestMapping("/api/v1/roles")
public class RestRoleController {

    @Autowired
    private RoleService roleService;

    @Autowired
    private RolePermissionService rolePermissionService;

    @Autowired
    private RoleMapper roleMapper;

    /**
     * Retrieves all roles.
     *
     * @return a list of all roles as DTOs.
     */
    @PreAuthorize("hasAuthority('VIEW_ROLES')")
    @GetMapping
    public ResponseEntity<List<RoleDTO>> getAllRoles() {
        try {
            List<Role> roles = roleService.getAllRoles();
            List<RoleDTO> roleDTOs = roles.stream()
                    .map(roleMapper::toRoleDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(roleDTOs);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
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
    public ResponseEntity<RoleWithPermissionsDTO> getRoleById(@PathVariable Long roleId) {
        try {
            Role role = roleService.getRole(roleId);
            RoleWithPermissionsDTO roleDTO = roleMapper.toRoleWithPermissionsDTO(role);
            return ResponseEntity.ok(roleDTO);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
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
    public ResponseEntity<RoleDTO> createRole(@RequestBody RoleDTO roleDTO) {
        try {
            Role role = new Role();
            role.setRoleName(roleDTO.getRoleName());
            Role createdRole = roleService.createRole(role);
            RoleDTO createdRoleDTO = roleMapper.toRoleDTO(createdRole);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdRoleDTO);
        } catch (ResourceAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
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
    public ResponseEntity<RoleDTO> updateRole(@PathVariable Long roleId, @RequestBody RoleDTO roleDTO) {
        try {
            Role existingRole = roleService.getRole(roleId);
            existingRole.setRoleName(roleDTO.getRoleName());
            Role updatedRole = roleService.updateRole(existingRole);
            RoleDTO updatedRoleDTO = roleMapper.toRoleDTO(updatedRole);
            return ResponseEntity.ok(updatedRoleDTO);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
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
    public ResponseEntity<Void> deleteRole(@PathVariable Long roleId) {
        try {
            Role role = roleService.getRole(roleId);
            roleService.deleteRole(role);
            return ResponseEntity.ok().build();
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (InvalidOperationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Adds a permission to a role.
     *
     * @param roleId the ID of the role.
     * @param permissionId the ID of the permission to add.
     * @return a success or error response.
     */
    @PreAuthorize("hasAuthority('EDIT_ROLES')")
    @PostMapping("/{roleId}/permissions/{permissionId}")
    public ResponseEntity<RoleWithPermissionsDTO> assignPermissionToRole(@PathVariable Long roleId, @PathVariable Long permissionId) {
        try {
            rolePermissionService.assignPermissionToRole(permissionId, roleId);
            RoleWithPermissionsDTO roleWithPermissionsDTO = roleMapper.toRoleWithPermissionsDTO(roleService.getRole(roleId));
            return ResponseEntity.ok(roleWithPermissionsDTO);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (InvalidOperationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Removes a permission from a role.
     *
     * @param roleId the ID of the role.
     * @param permissionId the ID of the permission to remove.
     * @return a success or error response.
     */
    @PreAuthorize("hasAuthority('EDIT_ROLES')")
    @DeleteMapping("/{roleId}/permissions/{permissionId}")
    public ResponseEntity<RoleWithPermissionsDTO> removePermissionFromRole(@PathVariable Long roleId, @PathVariable Long permissionId) {
        try {
            rolePermissionService.removePermissionFromRole(permissionId, roleId);
            RoleWithPermissionsDTO roleWithPermissionsDTO = roleMapper.toRoleWithPermissionsDTO(roleService.getRole(roleId));
            return ResponseEntity.ok(roleWithPermissionsDTO);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (InvalidOperationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}