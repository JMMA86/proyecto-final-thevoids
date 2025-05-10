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
import org.thevoids.oncologic.dto.custom.ApiResponse;
import org.thevoids.oncologic.dto.entity.RoleDTO;
import org.thevoids.oncologic.dto.entity.RoleWithPermissionsDTO;
import org.thevoids.oncologic.entity.Role;
import org.thevoids.oncologic.mapper.RoleMapper;
import org.thevoids.oncologic.service.RoleService;

@RestController
@RequestMapping("/api/v1/roles")
public class RestRoleController {

    @Autowired
    private RoleService roleService;

    @Autowired
    private RoleMapper roleMapper;

    /**
     * Retrieves all roles.
     *
     * @return a list of all roles as DTOs.
     */
    @PreAuthorize("hasAuthority('VIEW_ROLES')")
    @GetMapping
    public ResponseEntity<ApiResponse<List<RoleDTO>>> getAllRoles() {
        try {
            List<Role> roles = roleService.getAllRoles();
            List<RoleDTO> roleDTOs = roles.stream()
                    .map(roleMapper::toRoleDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(ApiResponse.exito("Roles recuperados con éxito", roleDTOs));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error al recuperar los roles: " + e.getMessage()));
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
    public ResponseEntity<ApiResponse<RoleWithPermissionsDTO>> getRoleById(@PathVariable Long roleId) {
        try {
            Role role = roleService.getRole(roleId);
            if (role == null) {
                throw new Exception("Rol no encontrado");
            }
            RoleWithPermissionsDTO roleDTO = roleMapper.toRoleWithPermissionsDTO(role);
            return ResponseEntity.ok(ApiResponse.exito("Rol recuperado con éxito", roleDTO));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Error al recuperar el rol: " + e.getMessage()));
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
    public ResponseEntity<ApiResponse<RoleDTO>> createRole(@RequestBody RoleDTO roleDTO) {
        try {
            Role role = new Role();
            role.setRoleName(roleDTO.getRoleName());
            Role createdRole = roleService.createRole(role);
            RoleDTO createdRoleDTO = roleMapper.toRoleDTO(createdRole);
            return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.exito("Rol creado con éxito", createdRoleDTO));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Error al crear el rol: " + e.getMessage()));
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
    public ResponseEntity<ApiResponse<RoleDTO>> updateRole(
            @PathVariable Long roleId, 
            @RequestBody RoleDTO roleDTO) {
        try {
            Role existingRole = roleService.getRole(roleId);
            if (existingRole == null) {
                throw new Exception("Rol no encontrado");
            }
            existingRole.setRoleName(roleDTO.getRoleName());
            Role updatedRole = roleService.updateRole(existingRole);
            RoleDTO updatedRoleDTO = roleMapper.toRoleDTO(updatedRole);
            return ResponseEntity.ok(ApiResponse.exito("Rol actualizado con éxito", updatedRoleDTO));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Error al actualizar el rol: " + e.getMessage()));
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
    public ResponseEntity<ApiResponse<Void>> deleteRole(@PathVariable Long roleId) {
        try {
            Role role = roleService.getRole(roleId);
            if (role == null) {
                throw new Exception("Rol no encontrado");
            }
            roleService.deleteRole(role);
            return ResponseEntity.ok(ApiResponse.exito("Rol eliminado con éxito", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Error al eliminar el rol: " + e.getMessage()));
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
    public ResponseEntity<ApiResponse<RoleWithPermissionsDTO>> addPermissionToRole(
            @PathVariable Long roleId, 
            @PathVariable Long permissionId) {
        try {
            // Esta funcionalidad debe ser implementada en el servicio correspondiente
            // Por ahora, simulamos un error
            throw new Exception("Funcionalidad no implementada");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Error al añadir permiso al rol: " + e.getMessage()));
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
    public ResponseEntity<ApiResponse<RoleWithPermissionsDTO>> removePermissionFromRole(
            @PathVariable Long roleId, 
            @PathVariable Long permissionId) {
        try {
            // Esta funcionalidad debe ser implementada en el servicio correspondiente
            // Por ahora, simulamos un error
            throw new Exception("Funcionalidad no implementada");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Error al eliminar permiso del rol: " + e.getMessage()));
        }
    }
}