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
import org.thevoids.oncologic.dto.entity.PermissionDTO;
import org.thevoids.oncologic.entity.Permission;
import org.thevoids.oncologic.exception.InvalidOperationException;
import org.thevoids.oncologic.exception.ResourceAlreadyExistsException;
import org.thevoids.oncologic.exception.ResourceNotFoundException;
import org.thevoids.oncologic.mapper.PermissionMapper;
import org.thevoids.oncologic.service.PermissionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/permissions")
@Tag(name = "Permisos", description = "API para la gestión de permisos del sistema")
public class RestPermissionController {

    @Autowired
    private PermissionService permissionService;
    
    @Autowired
    private PermissionMapper permissionMapper;

    /**
     * Retrieves all permissions.
     *
     * @return a list of all permissions as DTOs.
     */
    @Operation(summary = "Obtener todos los permisos", description = "Recupera una lista de todos los permisos disponibles")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de permisos recuperada exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = PermissionDTO.class))),
        @ApiResponse(responseCode = "403", description = "No autorizado para ver permisos")
    })
    @PreAuthorize("hasAuthority('VIEW_PERMISSIONS')")
    @GetMapping
    public ResponseEntity<List<PermissionDTO>> getAllPermissions() {
        try {
            List<Permission> permissions = permissionService.getAllPermissions();
            List<PermissionDTO> permissionDTOs = permissions.stream()
                    .map(permissionMapper::toPermissionDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(permissionDTOs);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Retrieves a specific permission by its ID.
     *
     * @param id the ID of the permission to retrieve.
     * @return the permission with the specified ID as a DTO.
     */
    @Operation(summary = "Obtener permiso por ID", description = "Recupera un permiso específico por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Permiso encontrado",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = PermissionDTO.class))),
        @ApiResponse(responseCode = "404", description = "Permiso no encontrado"),
        @ApiResponse(responseCode = "403", description = "No autorizado para ver permisos")
    })
    @PreAuthorize("hasAuthority('VIEW_PERMISSIONS')")
    @GetMapping("/{permissionId}")
    public ResponseEntity<PermissionDTO> getPermissionById(
        @Parameter(description = "ID del permiso a buscar")
        @PathVariable Long id
    ) {
        try {
            PermissionDTO permissionDTO = permissionMapper.toPermissionDTO(
                permissionService.getPermission(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Permiso", "id", id))
            );
            return ResponseEntity.ok(permissionDTO);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Creates a new permission.
     *
     * @param permissionDTO the permission to create as a DTO.
     * @return the created permission as a DTO.
     */
    @Operation(summary = "Crear permiso", description = "Crea un nuevo permiso en el sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Permiso creado exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = PermissionDTO.class))),
        @ApiResponse(responseCode = "400", description = "Datos de permiso inválidos"),
        @ApiResponse(responseCode = "403", description = "No autorizado para crear permisos")
    })
    @PreAuthorize("hasAuthority('ADD_PERMISSIONS')")
    @PostMapping
    public ResponseEntity<PermissionDTO> createPermission(
        @Parameter(description = "Datos del permiso a crear")
        @RequestBody PermissionDTO permissionDTO
    ) {
        try {
            Permission permission = permissionMapper.toPermission(permissionDTO);
            Permission createdPermission = permissionService.createPermission(permission);
            PermissionDTO createdPermissionDTO = permissionMapper.toPermissionDTO(createdPermission);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdPermissionDTO);
        } catch (ResourceAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Updates an existing permission.
     *
     * @param id the ID of the permission to update.
     * @param permissionDTO the updated permission as a DTO.
     * @return the updated permission as a DTO.
     */
    @Operation(summary = "Actualizar permiso", description = "Actualiza un permiso existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Permiso actualizado exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = PermissionDTO.class))),
        @ApiResponse(responseCode = "404", description = "Permiso no encontrado"),
        @ApiResponse(responseCode = "400", description = "Datos de actualización inválidos"),
        @ApiResponse(responseCode = "403", description = "No autorizado para actualizar permisos")
    })
    @PreAuthorize("hasAuthority('EDIT_PERMISSIONS')")
    @PutMapping("/{permissionId}")
    public ResponseEntity<PermissionDTO> updatePermission(
        @Parameter(description = "ID del permiso a actualizar")
        @PathVariable Long id,
        @Parameter(description = "Datos del permiso a actualizar")
        @RequestBody PermissionDTO permissionDTO
    ) {
        try {
            Permission existingPermission = permissionService.getPermission(id)
                .orElseThrow(() -> new ResourceNotFoundException("Permiso", "id", id));
            existingPermission.setPermissionName(permissionDTO.getPermissionName());
            Permission updatedPermission = permissionService.updatePermission(existingPermission);
            PermissionDTO updatedPermissionDTO = permissionMapper.toPermissionDTO(updatedPermission);
            return ResponseEntity.ok(updatedPermissionDTO);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Deletes a permission by its ID.
     *
     * @param id the ID of the permission to delete.
     * @return a response entity with no content.
     */
    @Operation(summary = "Eliminar permiso", description = "Elimina un permiso del sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Permiso eliminado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Permiso no encontrado"),
        @ApiResponse(responseCode = "403", description = "No autorizado para eliminar permisos")
    })
    @PreAuthorize("hasAuthority('DELETE_PERMISSIONS')")
    @DeleteMapping("/{permissionId}")
    public ResponseEntity<Void> deletePermission(
        @Parameter(description = "ID del permiso a eliminar")
        @PathVariable Long id
    ) {
        try {
            permissionService.deletePermission(id);
            return ResponseEntity.ok().build();
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (InvalidOperationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}