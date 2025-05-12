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
import org.thevoids.oncologic.dto.entity.AppointmentTypeDTO;
import org.thevoids.oncologic.entity.AppointmentType;
import org.thevoids.oncologic.mapper.AppointmentTypeMapper;
import org.thevoids.oncologic.service.AppointmentTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * REST controller for managing appointment types.
 */
@RestController
@RequestMapping("/api/v1/appointment-types")
@Tag(name = "Tipos de Citas", description = "API para la gestión de tipos de citas médicas")
public class RestAppointmentTypeController {

    @Autowired
    private AppointmentTypeService appointmentTypeService;
    @Autowired
    private AppointmentTypeMapper appointmentTypeMapper;

    /**
     * Retrieves all appointment types.
     *
     * @return a list of all appointment types as DTOs.
     */
    @Operation(summary = "Obtener todos los tipos de citas", description = "Recupera una lista de todos los tipos de citas disponibles")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de tipos de citas recuperada exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = AppointmentTypeDTO.class))),
        @ApiResponse(responseCode = "403", description = "No autorizado para ver tipos de citas")
    })
    @PreAuthorize("hasAuthority('VIEW_APPOINTMENTS')")
    @GetMapping
    public ResponseEntity<List<AppointmentTypeDTO>> getAllAppointmentTypes() {
        try {
            List<AppointmentTypeDTO> types = appointmentTypeService.getAllAppointmentTypes().stream()
                    .map(appointmentTypeMapper::toAppointmentTypeDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(types);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Retrieves a specific appointment type by its ID.
     *
     * @param id the ID of the appointment type to retrieve.
     * @return the appointment type with the specified ID as a DTO.
     */
    @Operation(summary = "Obtener tipo de cita por ID", description = "Recupera un tipo de cita específico por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Tipo de cita encontrado",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = AppointmentTypeDTO.class))),
        @ApiResponse(responseCode = "404", description = "Tipo de cita no encontrado"),
        @ApiResponse(responseCode = "403", description = "No autorizado para ver tipos de citas")
    })
    @PreAuthorize("hasAuthority('VIEW_APPOINTMENTS')")
    @GetMapping("/{id}")
    public ResponseEntity<AppointmentTypeDTO> getAppointmentTypeById(
        @Parameter(description = "ID del tipo de cita a buscar")
        @PathVariable Long id
    ) {
        try {
            AppointmentType type = appointmentTypeService.getAppointmentTypeById(id);
            if (type == null)
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            return ResponseEntity.ok(appointmentTypeMapper.toAppointmentTypeDTO(type));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Creates a new appointment type.
     *
     * @param dto the appointment type to create as a DTO.
     * @return the created appointment type as a DTO.
     */
    @Operation(summary = "Crear tipo de cita", description = "Crea un nuevo tipo de cita médica")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Tipo de cita creado exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = AppointmentTypeDTO.class))),
        @ApiResponse(responseCode = "400", description = "Datos de tipo de cita inválidos"),
        @ApiResponse(responseCode = "403", description = "No autorizado para crear tipos de citas")
    })
    @PreAuthorize("hasAuthority('ADD_APPOINTMENTS')")
    @PostMapping
    public ResponseEntity<AppointmentTypeDTO> createAppointmentType(
        @Parameter(description = "Datos de tipo de cita a crear")
        @RequestBody AppointmentTypeDTO dto
    ) {
        try {
            AppointmentType type = appointmentTypeMapper.toAppointmentType(dto);
            AppointmentType created = appointmentTypeService.createAppointmentType(type);
            return ResponseEntity.status(HttpStatus.CREATED).body(appointmentTypeMapper.toAppointmentTypeDTO(created));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Updates an existing appointment type.
     *
     * @param id  the ID of the appointment type to update.
     * @param dto the updated appointment type data.
     * @return the updated appointment type as a DTO.
     */
    @Operation(summary = "Actualizar tipo de cita", description = "Actualiza un tipo de cita existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Tipo de cita actualizado exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = AppointmentTypeDTO.class))),
        @ApiResponse(responseCode = "404", description = "Tipo de cita no encontrado"),
        @ApiResponse(responseCode = "400", description = "Datos de actualización inválidos"),
        @ApiResponse(responseCode = "403", description = "No autorizado para actualizar tipos de citas")
    })
    @PreAuthorize("hasAuthority('EDIT_APPOINTMENTS')")
    @PutMapping("/{id}")
    public ResponseEntity<AppointmentTypeDTO> updateAppointmentType(
        @Parameter(description = "ID del tipo de cita a actualizar")
        @PathVariable Long id,
        @Parameter(description = "Datos de tipo de cita a actualizar")
        @RequestBody AppointmentTypeDTO dto
    ) {
        try {
            AppointmentType type = appointmentTypeMapper.toAppointmentType(dto);
            type.setTypeId(id);
            AppointmentType updated = appointmentTypeService.updateAppointmentType(type);
            return ResponseEntity.ok(appointmentTypeMapper.toAppointmentTypeDTO(updated));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Deletes an appointment type.
     *
     * @param id the ID of the appointment type to delete.
     * @return a success or error response.
     */
    @Operation(summary = "Eliminar tipo de cita", description = "Elimina un tipo de cita del sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Tipo de cita eliminado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Tipo de cita no encontrado"),
        @ApiResponse(responseCode = "403", description = "No autorizado para eliminar tipos de citas")
    })
    @PreAuthorize("hasAuthority('DELETE_APPOINTMENTS')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAppointmentType(
        @Parameter(description = "ID del tipo de cita a eliminar")
        @PathVariable Long id
    ) {
        try {
            appointmentTypeService.deleteAppointmentType(id);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
