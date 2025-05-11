package org.thevoids.oncologic.controller.api;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.thevoids.oncologic.dto.TaskDTO;
import org.thevoids.oncologic.entity.Task;
import org.thevoids.oncologic.mapper.TaskMapper;
import org.thevoids.oncologic.service.TaskService;

class RestTaskControllerUnitTest {

    @InjectMocks
    private RestTaskController controller;

    @Mock
    private TaskService taskService;

    @Mock
    private TaskMapper taskMapper;

    private Task task;
    private TaskDTO taskDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        task = new Task();
        task.setId(1L);
        task.setDescription("Prepare patient report");
        task.setCompleted(true);
        task.setStartDate(new Date());
        task.setEndDate(new Date());

        taskDTO = new TaskDTO();
        taskDTO.setId(1L);
        taskDTO.setDescription("Prepare patient report");
        taskDTO.setCompleted(true);
        taskDTO.setStartDate(task.getStartDate());
        taskDTO.setEndDate(task.getEndDate());
    }

    @Test
    void getAllTasks_Success() {
        when(taskService.getAllTasks()).thenReturn(Arrays.asList(task));
        when(taskMapper.toTaskDTO(task)).thenReturn(taskDTO);

        ResponseEntity<List<TaskDTO>> response = controller.getAllTasks();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals(taskDTO.getId(), response.getBody().get(0).getId());
    }

    @Test
    void getAllTasks_InternalServerError() {
        when(taskService.getAllTasks()).thenThrow(new RuntimeException());
        ResponseEntity<List<TaskDTO>> response = controller.getAllTasks();
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void getTaskById_Success() {
        when(taskService.getTaskById(1L)).thenReturn(task);
        when(taskMapper.toTaskDTO(task)).thenReturn(taskDTO);

        ResponseEntity<TaskDTO> response = controller.getTaskById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(taskDTO, response.getBody());
    }

    @Test
    void getTaskById_NotFound() {
        when(taskService.getTaskById(1L)).thenThrow(new IllegalArgumentException());

        ResponseEntity<TaskDTO> response = controller.getTaskById(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void getTaskById_InternalServerError() {
        when(taskService.getTaskById(1L)).thenThrow(new RuntimeException());
        ResponseEntity<TaskDTO> response = controller.getTaskById(1L);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void createTask_Success() {
        when(taskMapper.toTask(taskDTO)).thenReturn(task);
        when(taskService.createTask(task)).thenReturn(task);
        when(taskMapper.toTaskDTO(task)).thenReturn(taskDTO);

        ResponseEntity<TaskDTO> response = controller.createTask(taskDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(taskDTO, response.getBody());
    }

    @Test
    void createTask_BadRequest() {
        when(taskMapper.toTask(taskDTO)).thenReturn(task);
        when(taskService.createTask(task)).thenThrow(new IllegalArgumentException());

        ResponseEntity<TaskDTO> response = controller.createTask(taskDTO);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void createTask_InternalServerError() {
        when(taskMapper.toTask(taskDTO)).thenReturn(task);
        when(taskService.createTask(task)).thenThrow(new RuntimeException());
        ResponseEntity<TaskDTO> response = controller.createTask(taskDTO);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void updateTask_Success() {
        when(taskMapper.toTask(taskDTO)).thenReturn(task);
        when(taskService.updateTask(task)).thenReturn(task);
        when(taskMapper.toTaskDTO(task)).thenReturn(taskDTO);

        ResponseEntity<TaskDTO> response = controller.updateTask(1L, taskDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(taskDTO, response.getBody());
    }

    @Test
    void updateTask_BadRequest() {
        when(taskMapper.toTask(taskDTO)).thenReturn(task);
        when(taskService.updateTask(task)).thenThrow(new IllegalArgumentException());

        ResponseEntity<TaskDTO> response = controller.updateTask(1L, taskDTO);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void updateTask_InternalServerError() {
        when(taskMapper.toTask(taskDTO)).thenReturn(task);
        when(taskService.updateTask(task)).thenThrow(new RuntimeException());
        ResponseEntity<TaskDTO> response = controller.updateTask(1L, taskDTO);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void deleteTask_Success() {
        ResponseEntity<Void> response = controller.deleteTask(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(taskService, times(1)).deleteTask(1L);
    }

    @Test
    void deleteTask_NotFound() {
        doThrow(new IllegalArgumentException()).when(taskService).deleteTask(1L);

        ResponseEntity<Void> response = controller.deleteTask(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void deleteTask_InternalServerError() {
        doThrow(new RuntimeException()).when(taskService).deleteTask(1L);
        ResponseEntity<Void> response = controller.deleteTask(1L);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
}
