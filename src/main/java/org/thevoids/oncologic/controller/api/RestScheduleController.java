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
import org.thevoids.oncologic.dto.entity.ScheduleDTO;
import org.thevoids.oncologic.service.ScheduleService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/schedule")
@Tag(name = "Horarios", description = "API para la gestión de horarios de médicos")
public class RestScheduleController {
    @Autowired
    private ScheduleService scheduleService;

    /**
     * Retrieves all schedules.
     *
     * @return a response entity containing a list of schedule DTOs.
     */
    @Operation(summary = "Obtener horario por ID", description = "Recupera un horario específico por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Horario encontrado",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ScheduleDTO.class))),
        @ApiResponse(responseCode = "404", description = "Horario no encontrado"),
        @ApiResponse(responseCode = "403", description = "No autorizado para ver horarios")
    })
    @PreAuthorize("hasAuthority('VIEW_SCHEDULES')")
    @GetMapping
    public ResponseEntity<List<ScheduleDTO>> getAllSchedules() {
        try {
            List<ScheduleDTO> schedules = scheduleService.getAllSchedules();
            return ResponseEntity.ok(schedules);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Retrieves a schedule by its ID.
     *
     * @param id the ID of the schedule to retrieve.
     * @return a response entity containing the schedule DTO.
     */
    @PreAuthorize("hasAuthority('VIEW_SCHEDULES')")
    @GetMapping("/{id}")
    public ResponseEntity<?> getScheduleById(
        @Parameter(description = "ID del horario a buscar")
        @PathVariable Long id
    ) {
        try {
            ScheduleDTO schedule = scheduleService.getScheduleById(id);
            return ResponseEntity.ok(schedule);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "An unexpected error occurred"));
        }
    }

    /**
     * Creates a new schedule.
     *
     * @param scheduleDTO the schedule DTO to create.
     * @return a response entity containing the created schedule DTO.
     */
    @Operation(summary = "Crear horario", description = "Crea un nuevo horario para un médico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Horario creado exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ScheduleDTO.class))),
        @ApiResponse(responseCode = "400", description = "Datos de horario inválidos"),
        @ApiResponse(responseCode = "403", description = "No autorizado para crear horarios")
    })
    @PreAuthorize("hasAuthority('CREATE_SCHEDULES')")
    @PostMapping
    public ResponseEntity<?> createSchedule(
        @Parameter(description = "Datos del horario a crear")
        @RequestBody ScheduleDTO scheduleDTO
    ) {
        try {
            ScheduleDTO createdSchedule = scheduleService.createSchedule(scheduleDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdSchedule);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "An unexpected error occurred"));
        }
    }

    /**
     * Updates an existing schedule.
     *
     * @param id the ID of the schedule to update.
     * @param scheduleDTO the schedule DTO to update.
     * @return a response entity containing the updated schedule DTO.
     */
    @Operation(summary = "Actualizar horario", description = "Actualiza un horario existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Horario actualizado exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ScheduleDTO.class))),
        @ApiResponse(responseCode = "404", description = "Horario no encontrado"),
        @ApiResponse(responseCode = "400", description = "Datos de actualización inválidos"),
        @ApiResponse(responseCode = "403", description = "No autorizado para actualizar horarios")
    })
    @PreAuthorize("hasAuthority('UPDATE_SCHEDULES')")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateSchedule(
        @Parameter(description = "ID del horario a actualizar")
        @PathVariable Long id,
        @Parameter(description = "Datos del horario a actualizar")
        @RequestBody ScheduleDTO scheduleDTO
    ) {
        try {
            scheduleDTO.setScheduleId(id);
            ScheduleDTO updatedSchedule = scheduleService.updateSchedule(scheduleDTO);
            return ResponseEntity.ok(updatedSchedule);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "An unexpected error occurred"));
        }
    }

    /**
     * Deletes a schedule by its ID.
     *
     * @param id the ID of the schedule to delete.
     * @return a response entity with no content.
     */
    @Operation(summary = "Eliminar horario", description = "Elimina un horario del sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Horario eliminado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Horario no encontrado"),
        @ApiResponse(responseCode = "403", description = "No autorizado para eliminar horarios")
    })
    @PreAuthorize("hasAuthority('DELETE_SCHEDULES')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSchedule(
        @Parameter(description = "ID del horario a eliminar")
        @PathVariable Long id
    ) {
        try {
            scheduleService.deleteSchedule(id);
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
