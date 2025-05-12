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
import org.thevoids.oncologic.dto.entity.PatientDTO;
import org.thevoids.oncologic.service.PatientService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/patients")
@Tag(name = "Pacientes", description = "API para la gestión de pacientes")
public class RestPatientController {
    private final PatientService patientService;

    public RestPatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    /**
     * Retrieves all patients.
     *
     * @return a list of all patients as DTOs.
     */
    @Operation(summary = "Obtener todos los pacientes", description = "Recupera una lista de todos los pacientes registrados")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de pacientes obtenida exitosamente"),
        @ApiResponse(responseCode = "500", description = "Error al obtener la lista de pacientes")
    })
    @PreAuthorize("hasAuthority('VIEW_PATIENTS')")
    @GetMapping
    public ResponseEntity<List<PatientDTO>> getAllPatients() {
        try {
            List<PatientDTO> patients = patientService.getAllPatients();
            return ResponseEntity.ok(patients);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Retrieves a specific patient by their ID.
     *
     * @param id the ID of the patient to retrieve.
     * @return the patient with the specified ID as a DTO.
     */ 
    @Operation(summary = "Obtener paciente por ID", description = "Recupera un paciente específico por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Paciente encontrado",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = PatientDTO.class))),
        @ApiResponse(responseCode = "404", description = "Paciente no encontrado"),
        @ApiResponse(responseCode = "403", description = "No autorizado para ver pacientes")
    })
    @PreAuthorize("hasAuthority('VIEW_PATIENTS')")
    @GetMapping("/{id}")
    public ResponseEntity<?> getPatientById(
        @Parameter(description = "ID del paciente a buscar")
        @PathVariable Long id
    ) {
        try {
            PatientDTO patient = patientService.getPatientById(id);
            return ResponseEntity.ok(patient);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "An unexpected error occurred"));
        }
    }

    /**
     * Creates a new patient.
     *
     * @param patientDTO the patient to create.
     * @return the created patient as a DTO.
     */
    @Operation(summary = "Crear nuevo paciente", description = "Registra un nuevo paciente en el sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Paciente creado exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = PatientDTO.class))),
        @ApiResponse(responseCode = "400", description = "Datos de paciente inválidos"),
        @ApiResponse(responseCode = "403", description = "No autorizado para crear pacientes")
    })
    @PreAuthorize("hasAuthority('CREATE_PATIENTS')")
    @PostMapping
    public ResponseEntity<?> createPatient(
        @Parameter(description = "Datos del paciente a crear")
        @RequestBody PatientDTO patientDTO
    ) {
        try {
            PatientDTO createdPatient = patientService.createPatient(patientDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdPatient);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "An unexpected error occurred"));
        }
    }

    /**
     * Updates an existing patient.
     *
     * @param id the ID of the patient to update.
     * @param patientDTO the updated patient as a DTO.
     * @return the updated patient as a DTO.
     */
    @Operation(summary = "Actualizar paciente", description = "Actualiza los datos de un paciente existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Paciente actualizado exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = PatientDTO.class))),
        @ApiResponse(responseCode = "404", description = "Paciente no encontrado"),
        @ApiResponse(responseCode = "400", description = "Datos de actualización inválidos"),
        @ApiResponse(responseCode = "403", description = "No autorizado para actualizar pacientes")
    })
    @PreAuthorize("hasAuthority('UPDATE_PATIENTS')")
    @PutMapping("/{id}")
    public ResponseEntity<?> updatePatient(
        @Parameter(description = "ID del paciente a actualizar")
        @PathVariable Long id,
        @Parameter(description = "Datos del paciente a actualizar")
        @RequestBody PatientDTO patientDTO) {
        try {
            patientDTO.setPatientId(id);
            PatientDTO updatedPatient = patientService.updatePatient(patientDTO);
            return ResponseEntity.ok(updatedPatient);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "An unexpected error occurred"));
        }
    }

    /**
     * Deletes a patient by their ID.
     *
     * @param id the ID of the patient to delete.
     * @return a response entity with no content.
     */
    @Operation(summary = "Eliminar paciente", description = "Elimina un paciente del sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Paciente eliminado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Paciente no encontrado"),
        @ApiResponse(responseCode = "403", description = "No autorizado para eliminar pacientes")
    })
    @PreAuthorize("hasAuthority('DELETE_PATIENTS')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePatient(
        @Parameter(description = "ID del paciente a eliminar")
        @PathVariable Long id
    ) {
        try {
            patientService.deletePatient(id);
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