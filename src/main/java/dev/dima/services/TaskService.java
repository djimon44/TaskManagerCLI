package dev.dima.services;

import dev.dima.cli.CommandProcessor;
import dev.dima.models.Task;
import dev.dima.models.Status;

import java.util.List;

public class TaskService implements TaskServiceInterface {

    private CommandProcessor processor;

    public TaskService() {
        this.processor = new CommandProcessor();
    }

    @Override
    public void addTask(Task task) {
        this.processor.addTask(task);
    }

    @Override
    public boolean deleteTask(int taskId) {
        return this.processor.deleteTask(taskId);
    }

    @Override
    public void updateTask(Task task) {
        this.processor.updateTask(task);
    }

    @Override
    public Task getTaskById(int TaskId) {
        return this.processor.getTaskById(TaskId);
    }

    @Override
    public List<Task> getAllTasks() {
        return this.processor.getAllTasks();
    }

    @Override
    public List<Task> getTasksByStatus(Status status) {
        return this.processor.getTasksByStatus(status);
    }
}
