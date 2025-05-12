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
import org.thevoids.oncologic.dto.entity.TaskDTO;
import org.thevoids.oncologic.entity.Task;
import org.thevoids.oncologic.mapper.TaskMapper;
import org.thevoids.oncologic.service.TaskService;

import java.util.List;
import java.util.stream.Collectors;

/**
 * REST controller for managing tasks.
 */
@RestController
@RequestMapping("/api/v1/tasks")
@Tag(name = "Tareas", description = "API para la gestión de tareas médicas")
public class RestTaskController {

    @Autowired
    private TaskService taskService;
    @Autowired
    private TaskMapper taskMapper;

    /**
     * Retrieves all tasks.
     *
     * @return a list of all tasks as DTOs.
     */
    @Operation(summary = "Obtener todas las tareas", description = "Recupera una lista de todas las tareas médicas")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de tareas recuperada exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = TaskDTO.class))),
        @ApiResponse(responseCode = "403", description = "No autorizado para ver tareas"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PreAuthorize("hasAuthority('VIEW_APPOINTMENTS')")
    @GetMapping
    public ResponseEntity<List<TaskDTO>> getAllTasks() {
        try {
            List<TaskDTO> tasks = taskService.getAllTasks().stream()
                    .map(taskMapper::toTaskDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(tasks);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Retrieves a specific task by its ID.
     *
     * @param id the ID of the task to retrieve.
     * @return the task with the specified ID as a DTO.
     */
    @Operation(summary = "Obtener tarea por ID", description = "Recupera una tarea específica por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Tarea encontrada",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = TaskDTO.class))),
        @ApiResponse(responseCode = "404", description = "Tarea no encontrada"),
        @ApiResponse(responseCode = "403", description = "No autorizado para ver tareas"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PreAuthorize("hasAuthority('VIEW_APPOINTMENTS')")
    @GetMapping("/{id}")
    public ResponseEntity<TaskDTO> getTaskById(
        @Parameter(description = "ID de la tarea a buscar")
        @PathVariable Long id
    ) {
        try {
            Task task = taskService.getTaskById(id);
            return ResponseEntity.ok(taskMapper.toTaskDTO(task));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Creates a new task.
     *
     * @param dto the task to create as a DTO.
     * @return the created task as a DTO.
     */
    @Operation(summary = "Crear tarea", description = "Crea una nueva tarea médica")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Tarea creada exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = TaskDTO.class))),
        @ApiResponse(responseCode = "400", description = "Datos de tarea inválidos"),
        @ApiResponse(responseCode = "403", description = "No autorizado para crear tareas"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PreAuthorize("hasAuthority('ADD_APPOINTMENTS')")
    @PostMapping
    public ResponseEntity<TaskDTO> createTask(
        @Parameter(description = "Datos de la tarea a crear")
        @RequestBody TaskDTO dto
    ) {
        try {
            Task task = taskMapper.toTask(dto);
            Task created = taskService.createTask(task);
            return ResponseEntity.status(HttpStatus.CREATED).body(taskMapper.toTaskDTO(created));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Updates an existing task.
     *
     * @param id  the ID of the task to update.
     * @param dto the updated task data.
     * @return the updated task as a DTO.
     */
    @Operation(summary = "Actualizar tarea", description = "Actualiza una tarea existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Tarea actualizada exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = TaskDTO.class))),
        @ApiResponse(responseCode = "404", description = "Tarea no encontrada"),
        @ApiResponse(responseCode = "400", description = "Datos de actualización inválidos"),
        @ApiResponse(responseCode = "403", description = "No autorizado para actualizar tareas"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PreAuthorize("hasAuthority('EDIT_APPOINTMENTS')")
    @PutMapping("/{id}")
    public ResponseEntity<TaskDTO> updateTask(
        @Parameter(description = "ID de la tarea a actualizar")
        @PathVariable Long id,
        @Parameter(description = "Datos actualizados de la tarea")
        @RequestBody TaskDTO dto
    ) {
        try {
            Task task = taskMapper.toTask(dto);
            task.setId(id);
            Task updated = taskService.updateTask(task);
            return ResponseEntity.ok(taskMapper.toTaskDTO(updated));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Deletes a task.
     *
     * @param id the ID of the task to delete.
     * @return a success or error response.
     */
    @Operation(summary = "Eliminar tarea", description = "Elimina una tarea del sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Tarea eliminada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Tarea no encontrada"),
        @ApiResponse(responseCode = "403", description = "No autorizado para eliminar tareas"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PreAuthorize("hasAuthority('DELETE_APPOINTMENTS')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(
        @Parameter(description = "ID de la tarea a eliminar")
        @PathVariable Long id
    ) {
        try {
            taskService.deleteTask(id);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
