package org.thevoids.oncologic.service.impl;

import org.springframework.stereotype.Service;
import org.thevoids.oncologic.entity.Task;
import org.thevoids.oncologic.repository.TaskRepository;
import org.thevoids.oncologic.service.TaskService;

import java.util.List;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    public TaskServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    @Override
    public Task getTaskById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Task with id " + id + " does not exist"));
    }

    @Override
    public Task createTask(Task task) {
        return taskRepository.save(task);
    }

    @Override
    public Task updateTask(Task task) {
        if (task == null || task.getId() == null) {
            throw new IllegalArgumentException("Task or Task ID cannot be null");
        }
        if (!taskRepository.existsById(task.getId())) {
            throw new IllegalArgumentException("Task with id " + task.getId() + " does not exist");
        }
        return taskRepository.save(task);
    }

    @Override
    public void deleteTask(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new IllegalArgumentException("Task with id " + id + " does not exist");
        }
        taskRepository.deleteById(id);
    }
}
