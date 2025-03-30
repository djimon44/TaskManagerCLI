package dev.dima;

import dev.dima.models.Status;
import dev.dima.models.Task;
import dev.dima.services.TaskService;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Scanner;

public class App {

    private static final TaskService taskService = new TaskService();
    private static final Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        String line = "";
        System.out.println("Task Manager CLI");

        while (!line.equals("exit")) {
            System.out.println("Enter command: ");
            line = sc.nextLine();

            if (line.replace(" ", "").trim().equalsIgnoreCase("exit")) break;

            String[] arguments = line.split(" ");
            switch (arguments.length) {
                case 0 -> processEmptyArguments(arguments);
                case 1 -> processSingleArguments(arguments);
                default -> processMultipleArguments(arguments);
            }
        }
    }

    public static void processMultipleArguments(String[] args) {
        switch (args[0]) {
            case "add" -> processAddArgument(args);
            case "update" -> processUpdateArguments(args);
            case "delete" -> processDeleteArguments(args);
            case "list" -> processListArguments(args);
            case "mark-in-progress" -> processMarkInProgressArguments(args);
            case "mark-done" -> processMarkDoneArguments(args);
            default -> System.out.println("Unknown command. Use 'help' to display available commands.");
        }
    }

    private static void processAddArgument(String[] args) {
        Task task = new Task(String.join(" ", Arrays.copyOfRange(args, 1, args.length)), Status.TODO);
        taskService.addTask(task);
        System.out.println("Task added, ID: " + task.getTaskId());
    }

    private static void processMarkDoneArguments(String[] args) {
        int id = -1;
        try {
            id = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            System.out.println("Unknown ID. Use 'help' to display available commands.");
        }
        var task = taskService.getTaskById(id);
        if (task != null) {
            task.setStatus(Status.DONE);
            task.setUpdatedAt(LocalDate.now());
            taskService.updateTask(task);
            System.out.println("Task updated, ID: " + id + " Status: " + task.getStatus());
        } else System.out.println("Task not found.");
    }

    private static void processMarkInProgressArguments(String[] args) {
        int id = -1;
        try {
            id = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            System.out.println("Unknown ID. Use 'help' to display available commands.");

        }
        var task = taskService.getTaskById(id);
        if (task != null) {
            task.setStatus(Status.IN_PROGRESS);
            task.setUpdatedAt(LocalDate.now());
            taskService.updateTask(task);
            System.out.println("Task updated, ID: " + id + " Status: " + task.getStatus());
        } else System.out.println("Task not found.");
    }

    private static void processDeleteArguments(String[] args) {
        int id = -1;
        try {
            id = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            System.out.println("Unknown ID. Use 'help' to display available commands.");
        }
        if (taskService.deleteTask(id)) {
            System.out.println("Task deleted, ID: " + id);
        } else System.out.println("Task not found.");
    }

    private static void processUpdateArguments(String[] args) {
        int id = -1;
        try {
            id = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            System.out.println("Unknown ID. Use 'help' to display available commands.");
        }
        var task = taskService.getTaskById(id);
        if (task != null) {
            task.setDescription(String.join(" ", Arrays.copyOfRange(args, 2, args.length)));
            task.setUpdatedAt(LocalDate.now());
            taskService.updateTask(task);
            System.out.println("Task updated, ID: " + id + " Description: " + task.getDescription());
        } else System.out.println("Task not found.");
    }

    private static void processListArguments(String[] args) {
        switch (args[1]) {
            case "done" -> taskService.getTasksByStatus(Status.DONE).forEach(System.out::println);
            case "todo" -> taskService.getTasksByStatus(Status.TODO).forEach(System.out::println);
            case "in-progress" -> taskService.getTasksByStatus(Status.IN_PROGRESS).forEach(System.out::println);
            default -> System.out.println("Unknown command. Use 'help' to display available commands.");
        }
    }

    public static void processSingleArguments(String[] args) {
        String argument = args[0];
        switch (argument) {
            case "list" -> {
                var tasks = taskService.getAllTasks();
                if (tasks == null || tasks.isEmpty()) {
                    System.out.println("No tasks found.");
                } else {
                    tasks.forEach(System.out::println);
                }
            }
            case "help" -> showHelp();
            default -> System.out.println("Unknown command. Use 'help' to display available commands.");
        }
    }

    public static void processEmptyArguments(String[] args) {
        System.out.println("No arguments provided. Use 'help' to display available commands.");
    }

    public static void showHelp() {
        System.out.println("Usage: java -jar task-tracker.jar [command]");
        System.out.println("The options below may be used to perform the desired operations:");
        System.out.println("    add <description>               - Add a new task");
        System.out.println("    update <taskId> <description>   - Update a task");
        System.out.println("    delete <taskId>                 - Delete a task");
        System.out.println("    mark-in-progress <taskId>       - Mark a task as in progress");
        System.out.println("    mark-done <taskId>              - Mark a task as done");
        System.out.println("    list                            - List all tasks");
        System.out.println("    list done                       - List all done tasks");
        System.out.println("    list todo                       - List all todo tasks");
        System.out.println("    list in-progress                - List all in progress tasks");
        System.out.println("    help                            - Display help information");
    }
}
