package org.thevoids.oncologic.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.thevoids.oncologic.dto.TaskDTO;
import org.thevoids.oncologic.entity.Task;
import org.thevoids.oncologic.mapper.TaskMapper;
import org.thevoids.oncologic.service.TaskService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/tasks")
public class RestTaskController {

    @Autowired
    private TaskService taskService;
    @Autowired
    private TaskMapper taskMapper;

    @PreAuthorize("hasAuthority('VIEW_APPOINTMENTS')")
    @GetMapping
    public List<TaskDTO> getAllTasks() {
        return taskService.getAllTasks().stream()
                .map(taskMapper::toTaskDTO)
                .collect(Collectors.toList());
    }

    @PreAuthorize("hasAuthority('VIEW_APPOINTMENTS')")
    @GetMapping("/{id}")
    public ResponseEntity<TaskDTO> getTaskById(@PathVariable Long id) {
        try {
            Task task = taskService.getTaskById(id);
            return ResponseEntity.ok(taskMapper.toTaskDTO(task));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PreAuthorize("hasAuthority('ADD_APPOINTMENTS')")
    @PostMapping
    public ResponseEntity<TaskDTO> createTask(@RequestBody TaskDTO dto) {
        try {
            Task task = taskMapper.toTask(dto);
            Task created = taskService.createTask(task);
            return ResponseEntity.ok(taskMapper.toTaskDTO(created));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PreAuthorize("hasAuthority('EDIT_APPOINTMENTS')")
    @PutMapping("/{id}")
    public ResponseEntity<TaskDTO> updateTask(@PathVariable Long id, @RequestBody TaskDTO dto) {
        try {
            Task task = taskMapper.toTask(dto);
            task.setId(id);
            Task updated = taskService.updateTask(task);
            return ResponseEntity.ok(taskMapper.toTaskDTO(updated));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PreAuthorize("hasAuthority('DELETE_APPOINTMENTS')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        try {
            taskService.deleteTask(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
