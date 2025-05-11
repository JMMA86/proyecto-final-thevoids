package org.thevoids.oncologic.service;

import org.thevoids.oncologic.entity.Task;

import java.util.List;

public interface TaskService {
    List<Task> getAllTasks();

    Task getTaskById(Long id);

    Task createTask(Task task);

    Task updateTask(Task task);

    void deleteTask(Long id);
}
