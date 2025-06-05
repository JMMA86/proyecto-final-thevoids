package org.thevoids.oncologic.controller.api;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.thevoids.oncologic.dto.entity.UserSpecialtyDTO;
import org.thevoids.oncologic.entity.UserSpecialty;
import org.thevoids.oncologic.exception.ResourceNotFoundException;
import org.thevoids.oncologic.mapper.UserSpecialtyMapper;
import org.thevoids.oncologic.service.UserSpecialtyService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/user-specialties")
@Tag(name = "Especialidades de Usuario", description = "API para la gestión de especialidades asignadas a usuarios")
public class RestUserSpecialtyController {

    @Autowired
    private UserSpecialtyService userSpecialtyService;

    @Autowired
    private UserSpecialtyMapper userSpecialtyMapper;

    /**
     * Retrieves all user specialties.
     *
     * @return a response entity containing a list of user specialty DTOs.
     */
    @Operation(summary = "Obtener todas las especialidades de usuarios", description = "Recupera una lista de todas las especialidades asignadas a usuarios")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de especialidades recuperada exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserSpecialtyDTO.class))),
            @ApiResponse(responseCode = "403", description = "No autorizado para ver especialidades de usuarios"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PreAuthorize("hasAuthority('VIEW_USER_SPECIALTIES')")
    @GetMapping
    public ResponseEntity<List<UserSpecialtyDTO>> getAllUserSpecialties() {
        try {
            List<UserSpecialty> userSpecialties = userSpecialtyService.getAllUserSpecialties();
            List<UserSpecialtyDTO> userSpecialtyDTOs = userSpecialties.stream()
                    .map(userSpecialtyMapper::toUserSpecialtyDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(userSpecialtyDTOs);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Retrieves a user specialty by its ID.
     *
     * @param id the ID of the user specialty to retrieve.
     * @return a response entity containing the user specialty DTO.
     */
    @Operation(summary = "Obtener especialidad de usuario por ID", description = "Recupera una especialidad específica asignada a un usuario por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Especialidad encontrada", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserSpecialtyDTO.class))),
            @ApiResponse(responseCode = "404", description = "Especialidad no encontrada"),
            @ApiResponse(responseCode = "403", description = "No autorizado para ver especialidades de usuarios"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PreAuthorize("hasAuthority('VIEW_USER_SPECIALTIES')")
    @GetMapping("/{id}")
    public ResponseEntity<UserSpecialtyDTO> getUserSpecialtyById(
            @Parameter(description = "ID de la especialidad de usuario a buscar") @PathVariable Long id) {
        try {
            UserSpecialty userSpecialty = userSpecialtyService.getUserSpecialtyById(id);
            UserSpecialtyDTO userSpecialtyDTO = userSpecialtyMapper.toUserSpecialtyDTO(userSpecialty);
            return ResponseEntity.ok(userSpecialtyDTO);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Retrieves user specialties by user ID.
     *
     * @param userId the ID of the user to get specialties for.
     * @return a response entity containing a list of user specialty DTOs.
     */
    @Operation(summary = "Obtener especialidades por ID de usuario", description = "Recupera todas las especialidades asignadas a un usuario específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Especialidades encontradas", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserSpecialtyDTO.class))),
            @ApiResponse(responseCode = "404", description = "No se encontraron especialidades para el usuario"),
            @ApiResponse(responseCode = "403", description = "No autorizado para ver especialidades de usuarios"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PreAuthorize("hasAuthority('VIEW_USER_SPECIALTIES')")
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<UserSpecialtyDTO>> getUserSpecialtiesByUserId(
            @Parameter(description = "ID del usuario para buscar sus especialidades") @PathVariable Long userId) {
        try {
            List<UserSpecialty> userSpecialties = userSpecialtyService.getUserSpecialtiesByUserId(userId);
            List<UserSpecialtyDTO> userSpecialtyDTOs = userSpecialties.stream()
                    .map(userSpecialtyMapper::toUserSpecialtyDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(userSpecialtyDTOs);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Assigns a specialty to a user.
     *
     * @param userId      the ID of the user to assign the specialty to.
     * @param specialtyId the ID of the specialty to assign.
     * @return a response entity containing the user specialty DTO.
     */
    @Operation(summary = "Asignar especialidad a usuario", description = "Asigna una especialidad específica a un usuario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Especialidad asignada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Usuario o especialidad no encontrada"),
            @ApiResponse(responseCode = "403", description = "No autorizado para asignar especialidades"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PreAuthorize("hasAuthority('MANAGE_USER_SPECIALTIES')")
    @PostMapping("/users/{userId}/specialties/{specialtyId}")
    public ResponseEntity<UserSpecialtyDTO> assignSpecialtyToUser(
            @Parameter(description = "ID del usuario") @PathVariable Long userId,
            @Parameter(description = "ID de la especialidad") @PathVariable Long specialtyId) {
        try {
            userSpecialtyService.addSpecialtyToUser(userId, specialtyId);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Deletes a user specialty by its ID.
     *
     * @param id the ID of the user specialty to delete.
     * @return a response entity with no content.
     */
    @Operation(summary = "Eliminar especialidad de usuario", description = "Elimina una especialidad asignada a un usuario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Especialidad eliminada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Especialidad no encontrada"),
            @ApiResponse(responseCode = "403", description = "No autorizado para eliminar especialidades"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PreAuthorize("hasAuthority('MANAGE_USER_SPECIALTIES')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserSpecialty(
            @Parameter(description = "ID de la especialidad de usuario a eliminar") @PathVariable Long id) {
        try {
            userSpecialtyService.deleteUserSpecialty(id);
            return ResponseEntity.ok().build();
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}