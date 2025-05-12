package org.thevoids.oncologic.controller.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.thevoids.oncologic.dto.entity.MedicalHistoryDTO;
import org.thevoids.oncologic.service.MedicalHistoryService;
import org.thevoids.oncologic.exception.ResourceNotFoundException;
import org.thevoids.oncologic.exception.InvalidOperationException;

import java.util.List;

@RestController
@RequestMapping("/api/v1/medical-histories")
@Tag(name = "Historias Médicas", description = "API para la gestión de historias médicas de pacientes")
public class RestMedicalHistoryController {
    private final MedicalHistoryService medicalHistoryService;

    public RestMedicalHistoryController(MedicalHistoryService medicalHistoryService) {
        this.medicalHistoryService = medicalHistoryService;
    }

    /**
     * Retrieves all medical histories.
     *
     * @return a list of all medical histories as DTOs.
     */
    @Operation(summary = "Obtener todas las historias médicas", description = "Recupera una lista de todas las historias médicas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de historias médicas recuperada exitosamente"),
            @ApiResponse(responseCode = "500", description = "Error al recuperar historias médicas")
    })
    @PreAuthorize("hasAuthority('VIEW_MEDICAL_HISTORIES')")
    @GetMapping
    public ResponseEntity<List<MedicalHistoryDTO>> getAllMedicalHistories() {
        try {
            List<MedicalHistoryDTO> histories = medicalHistoryService.getAllMedicalHistories();
            return ResponseEntity.ok(histories);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * Retrieves a specific medical history by its ID.
     *
     * @param id the ID of the medical history to retrieve.
     * @return the medical history with the specified ID as a DTO.
     */
    @Operation(summary = "Obtener historia médica por ID", description = "Recupera una historia médica específica por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Historia médica encontrada", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MedicalHistoryDTO.class))),
            @ApiResponse(responseCode = "404", description = "Historia médica no encontrada"),
            @ApiResponse(responseCode = "403", description = "No autorizado para ver historias médicas")
    })
    @PreAuthorize("hasAuthority('VIEW_MEDICAL_HISTORIES')")
    @Parameter(description = "ID de la historia médica a buscar")
    @GetMapping("/{id}")
    public ResponseEntity<MedicalHistoryDTO> getMedicalHistoryById(@PathVariable Long id) {
        if (id == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        try {
            MedicalHistoryDTO history = medicalHistoryService.getMedicalHistoryById(id);
            return ResponseEntity.ok(history);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * Creates a new medical history.
     *
     * @param medicalHistoryDTO the medical history to create.
     * @return the created medical history as a DTO.
     */
    @Operation(summary = "Crear historia médica", description = "Crea una nueva historia médica para un paciente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Historia médica creada exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MedicalHistoryDTO.class))),
            @ApiResponse(responseCode = "400", description = "Datos de historia médica inválidos"),
            @ApiResponse(responseCode = "403", description = "No autorizado para crear historias médicas")
    })
    @PreAuthorize("hasAuthority('CREATE_MEDICAL_HISTORIES')")
    @PostMapping
    public ResponseEntity<MedicalHistoryDTO> createMedicalHistory(@RequestBody MedicalHistoryDTO dto) {
        if (dto == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        try {
            MedicalHistoryDTO created = medicalHistoryService.createMedicalHistory(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (InvalidOperationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * Updates an existing medical history.
     *
     * @param id                the ID of the medical history to update.
     * @param medicalHistoryDTO the updated medical history as a DTO.
     * @return the updated medical history as a DTO.
     */

    @Operation(summary = "Actualizar historia médica", description = "Actualiza una historia médica existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Historia médica actualizada exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MedicalHistoryDTO.class))),
            @ApiResponse(responseCode = "404", description = "Historia médica no encontrada"),
            @ApiResponse(responseCode = "400", description = "Datos de actualización inválidos"),
            @ApiResponse(responseCode = "403", description = "No autorizado para actualizar historias médicas")
    })
    @PreAuthorize("hasAuthority('UPDATE_MEDICAL_HISTORIES')")
    @PutMapping("/{id}")
    public ResponseEntity<MedicalHistoryDTO> updateMedicalHistory(@PathVariable Long id,
            @RequestBody MedicalHistoryDTO dto) {
        if (dto == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        try {
            dto.setHistoryId(id);
            MedicalHistoryDTO updated = medicalHistoryService.updateMedicalHistory(dto);
            return ResponseEntity.ok(updated);
        } catch (InvalidOperationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * Deletes a medical history by its ID.
     *
     * @param id the ID of the medical history to delete.
     * @return a response entity with no content.
     */
    @Operation(summary = "Eliminar historia médica", description = "Elimina una historia médica del sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Historia médica eliminada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Historia médica no encontrada"),
            @ApiResponse(responseCode = "403", description = "No autorizado para eliminar historias médicas")
    })
    @PreAuthorize("hasAuthority('DELETE_MEDICAL_HISTORIES')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMedicalHistory(
            @Parameter(description = "ID de la historia médica a eliminar") @PathVariable Long id) {
        if (id == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        try {
            medicalHistoryService.deleteMedicalHistory(id);
            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
