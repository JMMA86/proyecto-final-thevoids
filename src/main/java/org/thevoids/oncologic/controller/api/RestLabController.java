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
import org.thevoids.oncologic.dto.entity.LabDTO;
import org.thevoids.oncologic.service.LabService;

import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/labs")
@Tag(name = "Laboratorios", description = "API para la gestión de exámenes de laboratorio")
public class RestLabController {
    @Autowired
    private LabService labService;

    /**
     * Retrieves all labs.
     *
     * @return a list of all labs as DTOs.
     */
    @Operation(summary = "Obtener todos los exámenes", description = "Recupera una lista de todos los exámenes de laboratorio")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de exámenes recuperada exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = LabDTO.class))),
        @ApiResponse(responseCode = "403", description = "No autorizado para ver exámenes")
    })
    @PreAuthorize("hasAuthority('VIEW_LABS')")
    @GetMapping
    public ResponseEntity<List<LabDTO>> getAllLabs() {
        try {
            List<LabDTO> labs = labService.getAllLabs();
            return ResponseEntity.ok(labs);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Retrieves a specific lab by its ID.
     *
     * @param id the ID of the lab to retrieve.
     * @return the lab with the specified ID as a DTO.
     */
    @Operation(summary = "Obtener examen por ID", description = "Recupera un examen de laboratorio específico por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Examen encontrado",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = LabDTO.class))),
        @ApiResponse(responseCode = "404", description = "Examen no encontrado"),
        @ApiResponse(responseCode = "403", description = "No autorizado para ver exámenes")
    })
    @PreAuthorize("hasAuthority('VIEW_LABS')")
    @GetMapping("/{id}")
    public ResponseEntity<?> getLabById(
        @Parameter(description = "ID del examen a buscar")
        @PathVariable Long id
    ) {
        try {
            LabDTO lab = labService.getLabById(id);
            return ResponseEntity.ok(lab);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "An unexpected error occurred"));
        }
    }

    /**
     * Creates a new lab.
     *
     * @param patientId the ID of the patient.
     * @param technicianId the ID of the technician.
     * @param requestDate the date of the request.
     * @return the created lab as a DTO.
     */
    @Operation(summary = "Crear examen", description = "Registra un nuevo examen de laboratorio")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Examen creado exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = LabDTO.class))),
        @ApiResponse(responseCode = "400", description = "Datos de examen inválidos"),
        @ApiResponse(responseCode = "403", description = "No autorizado para crear exámenes")
    })
    @PreAuthorize("hasAuthority('ASSIGN_LABS')")
    @PostMapping("/assign")
    public ResponseEntity<?> assignLab(
        @Parameter(description = "ID del paciente")
        @RequestParam Long patientId,
        @Parameter(description = "ID del técnico")
        @RequestParam Long technicianId,
        @Parameter(description = "Fecha de solicitud")
        @RequestParam Date requestDate
    ) {
        try {
            LabDTO createdLab = labService.assignLab(patientId, technicianId, requestDate);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdLab);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "An unexpected error occurred"));
        }
    }

    /**
     * Updates a lab by its ID.
     *
     * @param id the ID of the lab to update.
     * @param labDTO the updated lab as a DTO.
     * @return the updated lab as a DTO.
     */
    @Operation(summary = "Actualizar examen", description = "Actualiza un examen de laboratorio existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Examen actualizado exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = LabDTO.class))),
        @ApiResponse(responseCode = "404", description = "Examen no encontrado"),
        @ApiResponse(responseCode = "400", description = "Datos de actualización inválidos"),
        @ApiResponse(responseCode = "403", description = "No autorizado para actualizar exámenes")
    })
    @PreAuthorize("hasAuthority('UPDATE_LABS')")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateLab(
        @Parameter(description = "ID del examen a actualizar")
        @PathVariable Long id,
        @Parameter(description = "Datos del examen a actualizar")
        @RequestBody LabDTO labDTO) {
        try {
            labDTO.setLabId(id);
            LabDTO updatedLab = labService.updateLab(labDTO);
            return ResponseEntity.ok(updatedLab);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "An unexpected error occurred"));
        }
    }

    /**
     * Deletes a lab by its ID.
     *
     * @param id the ID of the lab to delete.
     * @return a response entity with no content.
     */
    @Operation(summary = "Eliminar examen", description = "Elimina un examen de laboratorio del sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Examen eliminado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Examen no encontrado"),
        @ApiResponse(responseCode = "403", description = "No autorizado para eliminar exámenes")
    })
    @PreAuthorize("hasAuthority('DELETE_LABS')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteLab(
        @Parameter(description = "ID del examen a eliminar")
        @PathVariable Long id
    ) {
        try {
            labService.deleteLab(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "An unexpected error occurred"));
        }
    }
}