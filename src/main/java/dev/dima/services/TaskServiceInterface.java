package dev.dima.services;

import dev.dima.models.Status;
import dev.dima.models.Task;

import java.util.List;

public interface TaskServiceInterface {
    void addTask(Task task);
    boolean deleteTask(int taskId);
    void updateTask(Task task);
    Task getTaskById(int taskId);
    List<Task> getAllTasks();
    List<Task> getTasksByStatus(Status status);
}
