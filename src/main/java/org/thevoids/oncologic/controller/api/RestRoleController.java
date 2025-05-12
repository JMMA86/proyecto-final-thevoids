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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/roles")
@Tag(name = "Roles", description = "API para la gestión de roles y permisos de usuarios")
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
    @Operation(summary = "Obtener todos los roles", description = "Recupera una lista de todos los roles disponibles")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de roles recuperada exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = RoleDTO.class))),
        @ApiResponse(responseCode = "403", description = "No autorizado para ver roles")
    })
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
     * Retrieves a role by its ID.
     *
     * @param roleId the ID of the role to retrieve.
     * @return the role with the specified ID as a DTO.
     */
    @Operation(summary = "Obtener rol por ID", description = "Recupera un rol específico por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Rol encontrado",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = RoleDTO.class))),
        @ApiResponse(responseCode = "404", description = "Rol no encontrado"),
        @ApiResponse(responseCode = "403", description = "No autorizado para ver roles")
    })
    @PreAuthorize("hasAuthority('VIEW_ROLES')")
    @GetMapping("/{roleId}")
    public ResponseEntity<RoleWithPermissionsDTO> getRoleById(
        @Parameter(description = "ID del rol a buscar")
        @PathVariable Long roleId
    ) {
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
    @Operation(summary = "Crear rol", description = "Crea un nuevo rol en el sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Rol creado exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = RoleDTO.class))),
        @ApiResponse(responseCode = "400", description = "Datos de rol inválidos"),
        @ApiResponse(responseCode = "403", description = "No autorizado para crear roles")
    })
    @PreAuthorize("hasAuthority('ADD_ROLES')")
    @PostMapping
    public ResponseEntity<RoleDTO> createRole(
        @Parameter(description = "Datos del rol a crear")
        @RequestBody RoleDTO roleDTO
    ) {
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
     * @param roleDTO the updated role as a DTO.
     * @return the updated role as a DTO.
     */
    @Operation(summary = "Actualizar rol", description = "Actualiza un rol existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Rol actualizado exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = RoleDTO.class))),
        @ApiResponse(responseCode = "404", description = "Rol no encontrado"),
        @ApiResponse(responseCode = "400", description = "Datos de actualización inválidos"),
        @ApiResponse(responseCode = "403", description = "No autorizado para actualizar roles")
    })
    @PreAuthorize("hasAuthority('EDIT_ROLES')")
    @PutMapping("/{roleId}")
    public ResponseEntity<RoleDTO> updateRole(
        @Parameter(description = "ID del rol a actualizar")
        @PathVariable Long roleId,
        @Parameter(description = "Datos del rol a actualizar")
        @RequestBody RoleDTO roleDTO
    ) {
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
     * Deletes a role by its ID.
     *
     * @param roleId the ID of the role to delete.
     * @return a response entity with no content.
     */
    @Operation(summary = "Eliminar rol", description = "Elimina un rol del sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Rol eliminado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Rol no encontrado"),
        @ApiResponse(responseCode = "403", description = "No autorizado para eliminar roles")
    })
    @PreAuthorize("hasAuthority('DELETE_ROLES')")
    @DeleteMapping("/{roleId}")
    public ResponseEntity<Void> deleteRole(
        @Parameter(description = "ID del rol a eliminar")
        @PathVariable Long roleId
    ) {
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
     * Assigns a permission to a role.
     *
     * @param roleId the ID of the role to assign the permission to.
     * @param permissionId the ID of the permission to assign.
     * @return the role with the assigned permission as a DTO.
     */
    @Operation(summary = "Asignar permiso a rol", description = "Asigna un permiso específico a un rol")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Permiso asignado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Rol o permiso no encontrado"),
        @ApiResponse(responseCode = "403", description = "No autorizado para asignar permisos")
    })
    @PreAuthorize("hasAuthority('EDIT_ROLES')")
    @PostMapping("/{roleId}/permissions/{permissionId}")
    public ResponseEntity<RoleWithPermissionsDTO> assignPermissionToRole(
        @Parameter(description = "ID del rol a asignar el permiso")
        @PathVariable Long roleId,
        @Parameter(description = "ID del permiso a asignar")
        @PathVariable Long permissionId
    ) {
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
     * @param roleId the ID of the role to remove the permission from.
     * @param permissionId the ID of the permission to remove.
     * @return the role with the removed permission as a DTO.
     */
    @Operation(summary = "Remover permiso de rol", description = "Remueve un permiso específico de un rol")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Permiso removido exitosamente"),
        @ApiResponse(responseCode = "404", description = "Rol o permiso no encontrado"),
        @ApiResponse(responseCode = "403", description = "No autorizado para remover permisos")
    })
    @PreAuthorize("hasAuthority('DELETE_ROLES')")
    @DeleteMapping("/{roleId}/permissions/{permissionId}")
    public ResponseEntity<RoleWithPermissionsDTO> removePermissionFromRole(
        @Parameter(description = "ID del rol a remover el permiso")
        @PathVariable Long roleId,
        @Parameter(description = "ID del permiso a remover")
        @PathVariable Long permissionId
    ) {
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