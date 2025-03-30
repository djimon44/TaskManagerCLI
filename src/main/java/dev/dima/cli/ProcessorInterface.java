package dev.dima.cli;

import dev.dima.models.Status;

import java.util.List;

public interface ProcessorInterface<Task> {
    void addTask(Task task);
    boolean deleteTask(int taskId);
    void updateTask(Task task);
    Task getTaskById(int taskId);
    List<Task> getAllTasks();
    List<Task> getTasksByStatus(Status status);
}
