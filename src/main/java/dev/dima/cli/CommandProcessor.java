package dev.dima.cli;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import dev.dima.models.Status;
import dev.dima.models.Task;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class CommandProcessor implements ProcessorInterface<Task> {

    private List<Task> processor;
    private final ObjectMapper mapper;
    private final File file;

    public CommandProcessor() {
        this.processor = new LinkedList<>();
        this.file = new File("tasks.json");
        this.mapper = new ObjectMapper();
        this.mapper.registerModule(new JavaTimeModule());
        verifyFile();
        loadTasks();
    }

    @Override
    public void addTask(Task task) {
        processor.add(task);
        saveTasks();
    }

    @Override
    public boolean deleteTask(int taskId) {
        var status = this.processor.removeIf(t -> t.getTaskId() == taskId);
        if (status) {
            saveTasks();
            Task.id--;
        }
        return status;
    }

    @Override
    public void updateTask(Task task) {
        this.processor.stream()
                .filter(t -> t.getTaskId() == task.getTaskId())
                .forEach(t -> {
                    t.setDescription(task.getDescription());
                    t.setStatus(task.getStatus());
                    t.setUpdatedAt(task.getUpdatedAt());
                });
        saveTasks();
    }

    @Override
    public Task getTaskById(int taskId) {
        return this.processor.stream()
                .filter(t -> t.getTaskId() == taskId)
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Task> getAllTasks() {
        return this.processor;
    }

    @Override
    public List<Task> getTasksByStatus(Status status) {
        return this.processor.stream()
                .filter(t -> t.getStatus().equals(status))
                .toList();
    }

    private void verifyFile() {
        if (!this.file.exists()) {
            try {
                this.file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void loadTasks() {
        try {
            this.processor = mapper.readValue(this.file, new TypeReference<List<Task>>() {});
        } catch (JsonProcessingException e) {
            System.out.println("Error while loading tasks.json");
        } catch (IOException e) {
            System.out.println("Error while loading tasks.json");
        }
    }

    private void saveTasks() {
        try {
            mapper.writeValue(this.file, this.processor);
        } catch (JsonProcessingException e) {
            System.out.println("Error while saving tasks.json");
        } catch (IOException e) {
            System.out.println("Error while saving tasks.json");
        }
    }
}
