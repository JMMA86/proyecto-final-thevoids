package org.thevoids.oncologic.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.thevoids.oncologic.dto.TaskDTO;
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
    @PreAuthorize("hasAuthority('VIEW_APPOINTMENTS')")
    @GetMapping("/{id}")
    public ResponseEntity<TaskDTO> getTaskById(@PathVariable Long id) {
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
    @PreAuthorize("hasAuthority('ADD_APPOINTMENTS')")
    @PostMapping
    public ResponseEntity<TaskDTO> createTask(@RequestBody TaskDTO dto) {
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
    @PreAuthorize("hasAuthority('EDIT_APPOINTMENTS')")
    @PutMapping("/{id}")
    public ResponseEntity<TaskDTO> updateTask(@PathVariable Long id, @RequestBody TaskDTO dto) {
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
    @PreAuthorize("hasAuthority('DELETE_APPOINTMENTS')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
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
