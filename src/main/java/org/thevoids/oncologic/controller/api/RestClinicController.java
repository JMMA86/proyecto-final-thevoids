package org.thevoids.oncologic.controller.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
import org.thevoids.oncologic.dto.entity.ClinicDTO;
import org.thevoids.oncologic.entity.Clinic;
import org.thevoids.oncologic.mapper.ClinicMapper;
import org.thevoids.oncologic.service.ClinicService;

/**
 * REST controller for managing clinics.
 */
@RestController
@RequestMapping("/api/v1/clinics")
@Tag(name = "Clínicas", description = "API para la gestión de clínicas médicas")
public class RestClinicController {

    @Autowired
    private ClinicService clinicService;
    @Autowired
    private ClinicMapper clinicMapper;

    /**
     * Retrieves all clinics.
     *
     * @return a list of all clinics as DTOs.
     */
    @Operation(summary = "Obtener todas las clínicas", description = "Recupera una lista de todas las clínicas registradas")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de clínicas recuperada exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ClinicDTO.class))),
        @ApiResponse(responseCode = "403", description = "No autorizado para ver clínicas")
    })
    @PreAuthorize("hasAuthority('VIEW_APPOINTMENTS')")
    @GetMapping
    public ResponseEntity<List<ClinicDTO>> getAllClinics() {
        try {
            List<ClinicDTO> clinics = clinicService.getAllClinics().stream()
                    .map(clinicMapper::toClinicDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(clinics);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Retrieves a specific clinic by its ID.
     *
     * @param id the ID of the clinic to retrieve.
     * @return the clinic with the specified ID as a DTO.
     */
    @Operation(summary = "Obtener clínica por ID", description = "Recupera una clínica específica por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Clínica encontrada",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ClinicDTO.class))),
        @ApiResponse(responseCode = "404", description = "Clínica no encontrada"),
        @ApiResponse(responseCode = "403", description = "No autorizado para ver clínicas")
    })
    @PreAuthorize("hasAuthority('VIEW_APPOINTMENTS')")
    @GetMapping("/{id}")
    public ResponseEntity<ClinicDTO> getClinicById(
        @Parameter(description = "ID de la clínica a buscar")
        @PathVariable Long id
    ) {
        try {
            Clinic clinic = clinicService.getClinicById(id);
            if (clinic == null)
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            return ResponseEntity.ok(clinicMapper.toClinicDTO(clinic));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Creates a new clinic.
     *
     * @param dto the clinic to create as a DTO.
     * @return the created clinic as a DTO.
     */
    @Operation(summary = "Crear clínica", description = "Registra una nueva clínica en el sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Clínica creada exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ClinicDTO.class))),
        @ApiResponse(responseCode = "400", description = "Datos de clínica inválidos"),
        @ApiResponse(responseCode = "403", description = "No autorizado para crear clínicas")
    })
    @PreAuthorize("hasAuthority('ADD_APPOINTMENTS')")
    @PostMapping
    public ResponseEntity<ClinicDTO> createClinic(
        @Parameter(description = "Datos de la clínica a crear")
        @RequestBody ClinicDTO dto
    ) {
        try {
            Clinic clinic = clinicMapper.toClinic(dto);
            Clinic created = clinicService.createClinic(clinic);
            return ResponseEntity.status(HttpStatus.CREATED).body(clinicMapper.toClinicDTO(created));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Updates an existing clinic.
     *
     * @param id  the ID of the clinic to update.
     * @param dto the updated clinic data.
     * @return the updated clinic as a DTO.
     */
    @Operation(summary = "Actualizar clínica", description = "Actualiza una clínica existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Clínica actualizada exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ClinicDTO.class))),
        @ApiResponse(responseCode = "404", description = "Clínica no encontrada"),
        @ApiResponse(responseCode = "400", description = "Datos de actualización inválidos"),
        @ApiResponse(responseCode = "403", description = "No autorizado para actualizar clínicas")
    })
    @PreAuthorize("hasAuthority('EDIT_APPOINTMENTS')")
    @PutMapping("/{id}")
    public ResponseEntity<ClinicDTO> updateClinic(
        @Parameter(description = "ID de la clínica a actualizar")
        @PathVariable Long id,
        @Parameter(description = "Datos de la clínica a actualizar")
        @RequestBody ClinicDTO dto
    ) {
        try {
            Clinic clinic = clinicMapper.toClinic(dto);
            Clinic updated = clinicService.updateClinic(id, clinic);
            return ResponseEntity.ok(clinicMapper.toClinicDTO(updated));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Deletes a clinic.
     *
     * @param id the ID of the clinic to delete.
     * @return a success or error response.
     */
    @Operation(summary = "Eliminar clínica", description = "Elimina una clínica del sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Clínica eliminada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Clínica no encontrada"),
        @ApiResponse(responseCode = "403", description = "No autorizado para eliminar clínicas")
    })
    @PreAuthorize("hasAuthority('DELETE_APPOINTMENTS')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClinic(
        @Parameter(description = "ID de la clínica a eliminar")
        @PathVariable Long id
    ) {
        try {
            clinicService.deleteClinic(id);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
