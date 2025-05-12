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
import org.thevoids.oncologic.dto.entity.AppointmentDTO;
import org.thevoids.oncologic.entity.Appointment;
import org.thevoids.oncologic.mapper.AppointmentMapper;
import org.thevoids.oncologic.service.AppointmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * REST controller for managing appointments.
 */
@RestController
@RequestMapping("/api/v1/appointments")
@Tag(name = "Citas Médicas", description = "API para la gestión de citas médicas")
public class RestAppointmentController {

    @Autowired
    private AppointmentService appointmentService;
    @Autowired
    private AppointmentMapper appointmentMapper;

    /**
     * Retrieves all appointments.
     *
     * @return a list of all appointments as DTOs.
     */
    @Operation(summary = "Obtener todas las citas", description = "Recupera una lista de todas las citas disponibles")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de citas recuperada exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = AppointmentDTO.class))),
        @ApiResponse(responseCode = "403", description = "No autorizado para ver citas")
    })
    @PreAuthorize("hasAuthority('VIEW_APPOINTMENTS')")
    @GetMapping
    public ResponseEntity<List<AppointmentDTO>> getAllAppointments() {
        try {
            List<AppointmentDTO> appointments = appointmentService.getAllAppointments().stream()
                    .map(appointmentMapper::toAppointmentDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(appointments);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Retrieves a specific appointment by its ID.
     *
     * @param id the ID of the appointment to retrieve.
     * @return the appointment with the specified ID as a DTO.
     */
    @Operation(summary = "Obtener cita por ID", description = "Recupera una cita específica por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cita encontrada",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = AppointmentDTO.class))),
        @ApiResponse(responseCode = "404", description = "Cita no encontrada"),
        @ApiResponse(responseCode = "403", description = "No autorizado para ver citas")
    })
    @PreAuthorize("hasAuthority('VIEW_APPOINTMENTS')")
    @GetMapping("/{id}")
    public ResponseEntity<AppointmentDTO> getAppointmentById(
        @Parameter(description = "ID de la cita a buscar")
        @PathVariable Long id
    ) {
        try {
            Appointment appointment = appointmentService.getAppointmentById(id);
            return ResponseEntity.ok(appointmentMapper.toAppointmentDTO(appointment));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Creates a new appointment.
     *
     * @param dto the appointment to create as a DTO.
     * @return the created appointment as a DTO.
     */
    @Operation(summary = "Crear nueva cita", description = "Crea una nueva cita médica en el sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cita creada exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = AppointmentDTO.class))),
        @ApiResponse(responseCode = "400", description = "Datos de cita inválidos"),
        @ApiResponse(responseCode = "403", description = "No autorizado para crear citas")
    })
    @PreAuthorize("hasAuthority('ADD_APPOINTMENTS')")
    @PostMapping
    public ResponseEntity<AppointmentDTO> createAppointment(
        @Parameter(description = "Datos de la cita a crear")
        @RequestBody AppointmentDTO dto
    ) {
        try {
            Appointment appointment = appointmentService.createAppointment(
                    dto.getPatientId(),
                    dto.getClinicAssignmentId(),
                    dto.getAppointmentTypeId(),
                    dto.getDateTime());
            return ResponseEntity.status(HttpStatus.CREATED).body(appointmentMapper.toAppointmentDTO(appointment));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Updates an existing appointment.
     *
     * @param id  the ID of the appointment to update.
     * @param dto the updated appointment data.
     * @return the updated appointment as a DTO.
     */
    @Operation(summary = "Actualizar cita", description = "Actualiza los datos de una cita existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cita actualizada exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = AppointmentDTO.class))),
        @ApiResponse(responseCode = "404", description = "Cita no encontrada"),
        @ApiResponse(responseCode = "400", description = "Datos de actualización inválidos"),
        @ApiResponse(responseCode = "403", description = "No autorizado para actualizar citas")
    })
    @PreAuthorize("hasAuthority('EDIT_APPOINTMENTS')")
    @PutMapping("/{id}")
    public ResponseEntity<AppointmentDTO> updateAppointment(
        @Parameter(description = "ID de la cita a actualizar")
        @PathVariable Long id,
        @Parameter(description = "Datos de la cita a actualizar")
        @RequestBody AppointmentDTO dto
    ) {
        try {
            Appointment existing = appointmentService.getAppointmentById(id);
            // Only update fields from DTO
            if (dto.getDateTime() != null)
                existing.setDateTime(dto.getDateTime());
            if (dto.getStatus() != null)
                existing.setStatus(dto.getStatus());
            // Optionally update related entities if IDs are provided
            if (dto.getPatientId() != null)
                existing.getPatient().setPatientId(dto.getPatientId());
            if (dto.getDoctorId() != null)
                existing.getDoctor().setUserId(dto.getDoctorId());
            if (dto.getAppointmentTypeId() != null)
                existing.getAppointmentType().setTypeId(dto.getAppointmentTypeId());
            if (dto.getClinicAssignmentId() != null)
                existing.getClinicAssignment().setId(dto.getClinicAssignmentId());

            Appointment updated = appointmentService.updateAppointment(existing);
            return ResponseEntity.ok(appointmentMapper.toAppointmentDTO(updated));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Deletes an appointment.
     *
     * @param id the ID of the appointment to delete.
     * @return a success or error response.
     */
    @Operation(summary = "Eliminar cita", description = "Elimina una cita del sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cita eliminada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Cita no encontrada"),
        @ApiResponse(responseCode = "403", description = "No autorizado para eliminar citas")
    })
    @PreAuthorize("hasAuthority('DELETE_APPOINTMENTS')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAppointment(
        @Parameter(description = "ID de la cita a eliminar")
        @PathVariable Long id
    ) {
        try {
            appointmentService.deleteAppointment(id);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
