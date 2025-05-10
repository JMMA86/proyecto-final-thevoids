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

@RestController
@RequestMapping("/api/v1/permissions")
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
     * @param permissionId the ID of the permission to retrieve.
     * @return the permission with the specified ID as a DTO.
     */
    @PreAuthorize("hasAuthority('VIEW_PERMISSIONS')")
    @GetMapping("/{permissionId}")
    public ResponseEntity<PermissionDTO> getPermissionById(@PathVariable Long permissionId) {
        try {
            PermissionDTO permissionDTO = permissionMapper.toPermissionDTO(
                permissionService.getPermission(permissionId)
                    .orElseThrow(() -> new ResourceNotFoundException("Permiso", "id", permissionId))
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
    @PreAuthorize("hasAuthority('ADD_PERMISSIONS')")
    @PostMapping
    public ResponseEntity<PermissionDTO> createPermission(@RequestBody PermissionDTO permissionDTO) {
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
     * @param permissionId the ID of the permission to update.
     * @param permissionDTO the updated permission data.
     * @return the updated permission as a DTO.
     */
    @PreAuthorize("hasAuthority('EDIT_PERMISSIONS')")
    @PutMapping("/{permissionId}")
    public ResponseEntity<PermissionDTO> updatePermission(@PathVariable Long permissionId, @RequestBody PermissionDTO permissionDTO) {
        try {
            Permission existingPermission = permissionService.getPermission(permissionId)
                .orElseThrow(() -> new ResourceNotFoundException("Permiso", "id", permissionId));
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
     * Deletes a permission.
     *
     * @param permissionId the ID of the permission to delete.
     * @return a success or error response.
     */
    @PreAuthorize("hasAuthority('DELETE_PERMISSIONS')")
    @DeleteMapping("/{permissionId}")
    public ResponseEntity<Void> deletePermission(@PathVariable Long permissionId) {
        try {
            permissionService.deletePermission(permissionId);
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