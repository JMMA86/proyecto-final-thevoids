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
import org.thevoids.oncologic.exception.ResourceNotFoundException;
import org.thevoids.oncologic.exception.InvalidOperationException;

import java.util.Date;
import java.util.List;

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
            @ApiResponse(responseCode = "200", description = "Lista de exámenes recuperada exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = LabDTO.class))),
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
            @ApiResponse(responseCode = "200", description = "Examen encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = LabDTO.class))),
            @ApiResponse(responseCode = "404", description = "Examen no encontrado"),
            @ApiResponse(responseCode = "403", description = "No autorizado para ver exámenes")
    })
    @PreAuthorize("hasAuthority('VIEW_LABS')")
    @GetMapping("/{id}")
    public ResponseEntity<LabDTO> getLabById(
            @Parameter(description = "ID del examen a buscar") @PathVariable Long id) {
        if (id == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        try {
            LabDTO lab = labService.getLabById(id);
            return ResponseEntity.ok(lab);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    @PreAuthorize("hasAuthority('ASSIGN_LABS')")
    @PostMapping
    public ResponseEntity<LabDTO> assignLab(@RequestBody LabDTO labDTO) {
        if (labDTO == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        try {
            Long patientId = labDTO.getPatientId();
            Long userId = labDTO.getLabTechnicianId();
            Date requestDate = labDTO.getRequestDate();
            LabDTO createdLabDTO = labService.assignLab(patientId, userId, requestDate);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdLabDTO);
        } catch (InvalidOperationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(null);
        } catch (ResourceNotFoundException | IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    /**
     * Updates a lab by its ID.
     *
     * @param id     the ID of the lab to update.
     * @param labDTO the updated lab as a DTO.
     * @return the updated lab as a DTO.
     */
    @Operation(summary = "Actualizar examen", description = "Actualiza un examen de laboratorio existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Examen actualizado exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = LabDTO.class))),
            @ApiResponse(responseCode = "404", description = "Examen no encontrado"),
            @ApiResponse(responseCode = "400", description = "Datos de actualización inválidos"),
            @ApiResponse(responseCode = "403", description = "No autorizado para actualizar exámenes")
    })
    @PreAuthorize("hasAuthority('UPDATE_LABS')")
    @PutMapping("/{id}")
    public ResponseEntity<LabDTO> updateLab(
            @Parameter(description = "ID del examen a actualizar") @PathVariable Long id,
            @Parameter(description = "Datos del examen a actualizar") @RequestBody LabDTO labDTO) {
        if (labDTO == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        try {
            labDTO.setLabId(id);
            LabDTO updatedLab = labService.updateLab(labDTO);
            return ResponseEntity.ok(updatedLab);
        } catch (InvalidOperationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(null);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
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
    public ResponseEntity<LabDTO> deleteLab(
            @Parameter(description = "ID del examen a eliminar") @PathVariable Long id) {
        if (id == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        try {
            labService.deleteLab(id);
            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
