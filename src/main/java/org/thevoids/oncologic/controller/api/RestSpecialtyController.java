package org.thevoids.oncologic.controller.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.thevoids.oncologic.dto.entity.SpecialtyDTO;
import org.thevoids.oncologic.entity.Specialty;
import org.thevoids.oncologic.exception.ResourceNotFoundException;
import org.thevoids.oncologic.mapper.SpecialtyMapper;
import org.thevoids.oncologic.service.SpecialtyService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/specialties")
@Tag(name = "Especialidades", description = "API para la gestión de especialidades médicas")
public class RestSpecialtyController {

    @Autowired
    private SpecialtyService specialtyService;

    @Autowired
    private SpecialtyMapper specialtyMapper;

    /**
     * Retrieves all specialties.
     *
     * @return a response entity containing a list of specialty DTOs.
     */
    @Operation(summary = "Obtener todas las especialidades", description = "Retorna una lista de todas las especialidades médicas disponibles")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de especialidades encontrada",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = SpecialtyDTO.class))),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PreAuthorize("hasAuthority('VIEW_SPECIALTIES')")
    @GetMapping
    public ResponseEntity<List<SpecialtyDTO>> getAllSpecialties() {
        try {
            List<Specialty> specialties = specialtyService.getAllSpecialties();
            List<SpecialtyDTO> specialtyDTOs = specialties.stream()
                    .map(specialtyMapper::toSpecialtyDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(specialtyDTOs);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Retrieves a specialty by its ID.
     *
     * @param id the ID of the specialty to retrieve.
     * @return a response entity containing the specialty DTO.
     */
    @Operation(summary = "Obtener especialidad por ID", description = "Retorna una especialidad médica basada en su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Especialidad encontrada",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = SpecialtyDTO.class))),
        @ApiResponse(responseCode = "404", description = "Especialidad no encontrada"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PreAuthorize("hasAuthority('VIEW_SPECIALTIES')")
    @GetMapping("/{id}")
    public ResponseEntity<SpecialtyDTO> getSpecialtyById(
        @Parameter(description = "ID de la especialidad a buscar")
        @PathVariable Long id
    ) {
        try {
            Specialty specialty = specialtyService.getSpecialtyById(id);
            SpecialtyDTO specialtyDTO = specialtyMapper.toSpecialtyDTO(specialty);
            return ResponseEntity.ok(specialtyDTO);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Creates a new specialty.
     *
     * @param specialtyDTO the specialty DTO to create.
     * @return a response entity containing the created specialty DTO.
     */
    @Operation(summary = "Crear nueva especialidad", description = "Crea una nueva especialidad médica")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Especialidad creada exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = SpecialtyDTO.class))),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PreAuthorize("hasAuthority('ADD_SPECIALTIES')")
    @PostMapping
    public ResponseEntity<SpecialtyDTO> createSpecialty(
        @Parameter(description = "Datos de la especialidad a crear")
        @RequestBody SpecialtyDTO specialtyDTO
    ) {
        try {
            Specialty specialty = specialtyMapper.toSpecialty(specialtyDTO);
            Specialty createdSpecialty = specialtyService.createSpecialty(specialty);
            SpecialtyDTO createdSpecialtyDTO = specialtyMapper.toSpecialtyDTO(createdSpecialty);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdSpecialtyDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Updates an existing specialty.
     *
     * @param id the ID of the specialty to update.
     * @param specialtyDTO the specialty DTO to update.
     * @return a response entity containing the updated specialty DTO.
     */
    @Operation(summary = "Actualizar especialidad", description = "Actualiza una especialidad médica existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Especialidad actualizada exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = SpecialtyDTO.class))),
        @ApiResponse(responseCode = "404", description = "Especialidad no encontrada"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PreAuthorize("hasAuthority('EDIT_SPECIALTIES')")
    @PutMapping("/{id}")
    public ResponseEntity<SpecialtyDTO> updateSpecialty(
        @Parameter(description = "ID de la especialidad a actualizar")
        @PathVariable Long id,
        @Parameter(description = "Nuevos datos de la especialidad")
        @RequestBody SpecialtyDTO specialtyDTO
    ) {
        try {
            Specialty specialty = specialtyMapper.toSpecialty(specialtyDTO);
            specialty.setSpecialtyId(id);
            Specialty updatedSpecialty = specialtyService.updateSpecialty(specialty);
            SpecialtyDTO updatedSpecialtyDTO = specialtyMapper.toSpecialtyDTO(updatedSpecialty);
            return ResponseEntity.ok(updatedSpecialtyDTO);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Deletes a specialty by its ID.
     *
     * @param id the ID of the specialty to delete.
     * @return a response entity with no content.
     */
    @Operation(summary = "Eliminar especialidad", description = "Elimina una especialidad médica existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Especialidad eliminada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Especialidad no encontrada"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PreAuthorize("hasAuthority('DELETE_SPECIALTIES')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSpecialty(
        @Parameter(description = "ID de la especialidad a eliminar")
        @PathVariable Long id
    ) {
        try {
            specialtyService.deleteSpecialty(id);
            return ResponseEntity.ok().build();
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}