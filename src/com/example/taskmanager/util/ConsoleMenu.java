package com.example.taskmanager.util;

import com.example.taskmanager.model.*;
import com.example.taskmanager.service.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class ConsoleMenu {

    private final Scanner scanner = new Scanner(System.in);

    private final UserService userService = new UserService();
    private final TaskService taskService = new TaskService();
    private final ProjectService projectService = new ProjectService();

    private User currentUser;

    public void start() {
        while (true) {
            showMainMenu();
        }
    }


    private void showMainMenu() {
        System.out.println("\n=== MAIN MENU ===");
        System.out.println("1. Login");
        System.out.println("2. Register");
        System.out.println("0. Exit");

        String choice = scanner.nextLine();

        switch (choice) {
            case "1" -> login();
            case "2" -> register();
            case "0" -> System.exit(0);
            default -> System.out.println("Invalid choice.");
        }
    }

    private void login() {
        System.out.print("Username: ");
        String username = scanner.nextLine().trim();

        System.out.print("Password: ");
        String password = scanner.nextLine().trim();

        if (username.isEmpty() || password.isEmpty()) {
            System.out.println("Username and password cannot be empty.");
            return;
        }

        currentUser = userService.login(username, password);

        if (currentUser != null) {
            System.out.println("Login successful!");
            userMenu();
        } else {
            System.out.println("Login failed.");
        }
    }

    private void register() {
        System.out.print("Username: ");
        String username = scanner.nextLine().trim();

        System.out.print("Password: ");
        String password = scanner.nextLine().trim();

        if (username.isEmpty() || password.isEmpty()) {
            System.out.println("Username and password cannot be empty.");
            return;
        }

        userService.register(username, password);
        System.out.println("User registered.");
    }

    private void userMenu() {
        while (true) {
            System.out.println("\n=== USER MENU ===");
            System.out.println("1. Task Menu");
            System.out.println("2. Project Menu");
            System.out.println("3. Upcoming Deadlines");
            System.out.println("0. Logout");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> taskMenu();
                case "2" -> projectMenu();
                case "3" -> showUpcomingDeadlines();
                case "0" -> {
                    currentUser = null;
                    return;
                }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private void taskMenu() {
        while (true) {
            System.out.println("\n--- TASK MENU ---");
            System.out.println("1. Add Task");
            System.out.println("2. Add Timed Task");
            System.out.println("3. List Tasks");
            System.out.println("4. Complete Task");
            System.out.println("5. Delete Task");
            System.out.println("0. Back");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> addTask();
                case "2" -> addTimedTask();
                case "3" -> listTasks();
                case "4" -> completeTask();
                case "5" -> deleteTask();
                case "0" -> { return; }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private void addTask() {
        System.out.print("Task title: ");
        String title = scanner.nextLine().trim();

        if (title.isEmpty()) {
            System.out.println("Title cannot be empty.");
            return;
        }

        Priority priority = askPriority();
        taskService.createTask(title, priority, currentUser.getId());
        System.out.println("Task created.");
    }

    private void addTimedTask() {
        try {
            System.out.print("Task title: ");
            String title = scanner.nextLine().trim();
            if (title.isEmpty()) {
                System.out.println("Title cannot be empty.");
                return;
            }

            Priority priority = askPriority();

            System.out.print("Deadline (yyyy-MM-dd): ");
            LocalDate deadline = LocalDate.parse(scanner.nextLine().trim());

            taskService.createTimedTask(title, priority, currentUser.getId(), deadline);
            System.out.println("Timed task created.");

        } catch (Exception e) {
            System.out.println("Invalid date format.");
        }
    }

    private void listTasks() {
        List<Task> tasks = taskService.getTasksByUserId(currentUser.getId());

        if (tasks.isEmpty()) {
            System.out.println("No tasks.");
            return;
        }

        System.out.println("\n=== TASKS ===");
        tasks.forEach(this::printTask);
    }

    private void completeTask() {
        listTasks();

        try {
            System.out.print("Enter Task ID to complete: ");
            int taskId = Integer.parseInt(scanner.nextLine().trim());

            boolean success = taskService.completeTask(taskId, currentUser.getId());
            System.out.println(success ? "Task completed." : "Task not found.");

        } catch (NumberFormatException e) {
            System.out.println("Invalid Task ID.");
        }
    }

    private void deleteTask() {
        listTasks();

        try {
            System.out.print("Enter Task ID to delete: ");
            int taskId = Integer.parseInt(scanner.nextLine().trim());

            boolean success = taskService.deleteTask(taskId, currentUser.getId());
            System.out.println(success ? "Task deleted." : "Task not found.");

        } catch (NumberFormatException e) {
            System.out.println("Invalid Task ID.");
        }
    }

    private void projectMenu() {
        while (true) {
            System.out.println("\n--- PROJECT MENU ---");
            System.out.println("1. Create Project");
            System.out.println("2. List Projects");
            System.out.println("3. Assign Task to Project");
            System.out.println("4. Complete Project Task");
            System.out.println("5. Delete Project");
            System.out.println("6. Export Tasks (TXT)");
            System.out.println("0. Back");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> createProject();
                case "2" -> listProjects();
                case "3" -> assignTaskToProject();
                case "4" -> completeProjectTask();
                case "5" -> deleteProject();
                case "6" -> exportProjectTasks();
                case "0" -> { return; }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private void createProject() {
        System.out.print("Project name: ");
        String projectName = scanner.nextLine();

        boolean success =
                projectService.createProject(projectName, currentUser.getId());

        System.out.println(success
                ? "Project created."
                : "Project already exists.");
    }


    private void listProjects() {
        List<Project> projects = projectService.getProjectsByUser(currentUser.getId());

        if (projects.isEmpty()) {
            System.out.println("No projects.");
            return;
        }

        System.out.println("\n=== PROJECTS ===");

        for (Project project : projects) {
            System.out.println(project.getId() + " | " + project.getName());

            for (Task task : taskService.getTasksByProject(project.getId())) {
                System.out.print("   - ");
                printTask(task);
            }
        }
    }

    private void assignTaskToProject() {
        listProjects();

        try {
            System.out.print("Enter Project ID: ");
            int projectId = Integer.parseInt(scanner.nextLine().trim());

            listTasks();

            System.out.print("Enter Task ID: ");
            int taskId = Integer.parseInt(scanner.nextLine().trim());

            boolean success =
                    projectService.assignTaskToProject(projectId, taskId, currentUser.getId());

            System.out.println(success ? "Task assigned." : "Assignment failed.");

        } catch (NumberFormatException e) {
            System.out.println("Invalid ID input.");
        }
    }

    private void deleteProject() {
        listProjects();

        try {
            System.out.print("Enter Project ID to delete: ");
            int projectId = Integer.parseInt(scanner.nextLine().trim());

            boolean success =
                    projectService.deleteProject(projectId, currentUser.getId());

            System.out.println(success ? "Project deleted." : "Project not found.");

        } catch (NumberFormatException e) {
            System.out.println("Invalid Project ID.");
        }
    }

    private void exportProjectTasks() {
        listProjects();

        try {
            System.out.print("Enter Project ID: ");
            int projectId = Integer.parseInt(scanner.nextLine().trim());

            Project project = projectService.getProjectById(projectId);

            if (project == null) {
                System.out.println("Project not found.");
                return;
            }

            String path = taskService.exportProjectTasksToTxt(
                    project.getId(),
                    project.getName()
            );

            System.out.println("Export completed.");
            System.out.println("Saved to: " + path);

        } catch (NumberFormatException e) {
            System.out.println("Invalid Project ID.");
        }
    }

    private void showUpcomingDeadlines() {
        List<TimedTask> tasks =
                taskService.getUpcomingTimedTasks(currentUser.getId());

        if (tasks.isEmpty()) {
            System.out.println("No upcoming deadlines.");
            return;
        }

        System.out.println("\n=== UPCOMING DEADLINES ===");

        for (TimedTask task : tasks) {
            System.out.print(task.getTitle()
                    + " | Deadline: " + task.getDeadline()
                    + " (" + task.daysLeft() + " days left)");

            if (task.isOverdue()) {
                System.out.print(" ⚠ OVERDUE");
            } else if (task.isUrgent()) {
                System.out.print(" ⚠ URGENT");
            }

            if (task.isCompleted()) {
                System.out.print(" ✅ DONE");
            }

            System.out.println();
        }
    }


    private Priority askPriority() {
        while (true) {
            System.out.println("Priority:");
            System.out.println("1 - LOW");
            System.out.println("2 - MEDIUM");
            System.out.println("3 - HIGH");
            System.out.print("Choice: ");

            String input = scanner.nextLine().trim();

            if (input.isEmpty()) return null;

            switch (input) {
                case "1": return Priority.LOW;
                case "2": return Priority.MEDIUM;
                case "3": return Priority.HIGH;
                default: System.out.println("Invalid choice.");
            }
        }
    }

    private void printTask(Task task) {
        System.out.print(
                task.getId()
                        + " | " + task.getTitle()
        );

        if (task.getPriority() != null) {
            System.out.print(" | " + task.getPriority());
        }

        if (task instanceof TimedTask timedTask) {
            long daysLeft = timedTask.daysLeft();

            System.out.print(" | Deadline: " + timedTask.getDeadline());

            if (timedTask.isOverdue()) {
                System.out.print(" ⚠ OVERDUE");
            } else {
                System.out.print(" (" + daysLeft + " days left)");

                if (timedTask.isUrgent()) {
                    System.out.print(" ⚠ URGENT");
                }
            }
        }

        if (task.isCompleted()) {
            System.out.print(" ✅ DONE");
        }

        System.out.println();
    }


    private void completeProjectTask() {
        listProjects();

        try {
            System.out.print("Enter Project ID: ");
            int projectId = Integer.parseInt(scanner.nextLine().trim());

            List<Task> tasks = taskService.getTasksByProject(projectId);

            if (tasks.isEmpty()) {
                System.out.println("No tasks in this project.");
                return;
            }

            System.out.println("\n--- PROJECT TASKS ---");
            for (Task task : tasks) {
                printTask(task);
            }

            System.out.print("Enter Task ID to complete: ");
            int taskId = Integer.parseInt(scanner.nextLine().trim());

            boolean success =
                    taskService.completeTask(taskId, currentUser.getId());

            System.out.println(success ? "Task completed." : "Task not found.");

        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
        }
    }

}
