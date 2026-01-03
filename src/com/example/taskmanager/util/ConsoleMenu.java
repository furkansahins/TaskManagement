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

    /* ================= MAIN MENU ================= */
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
        String username = scanner.nextLine();

        System.out.print("Password: ");
        String password = scanner.nextLine();

        currentUser = userService.login(username, password);

        if (currentUser != null) {
            System.out.println("Login successful! Welcome, " + currentUser.getUsername());
            userMenu();
        } else {
            System.out.println("Login failed. Try again.");
        }
    }

    private void register() {
        System.out.print("Username: ");
        String username = scanner.nextLine();

        System.out.print("Password: ");
        String password = scanner.nextLine();

        userService.register(username, password);
        System.out.println("User registered successfully.");
    }

    /* ================= USER MENU ================= */
    private void userMenu() {
        while (true) {
            System.out.println("\n=== USER MENU ===");
            System.out.println("1. Task Menu");
            System.out.println("2. Project Menu");
            System.out.println("0. Logout");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> taskMenu();
                case "2" -> projectMenu();
                case "0" -> {
                    currentUser = null;
                    return;
                }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    /* ================= TASK MENU ================= */
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
        System.out.print("Title: ");
        String title = scanner.nextLine();
        taskService.createTask(title, "", Priority.MEDIUM, currentUser);
        System.out.println("Task added.");
    }

    private void addTimedTask() {
        System.out.print("Title: ");
        String title = scanner.nextLine();

        System.out.print("Deadline (yyyy-MM-dd): ");
        LocalDate deadline = LocalDate.parse(scanner.nextLine());

        taskService.createTimedTask(title, "", Priority.HIGH, currentUser, deadline);
        System.out.println("Timed task added.");
    }

    private void listTasks() {
        List<Task> tasks = taskService.getTasksByUser(currentUser);
        if (tasks.isEmpty()) {
            System.out.println("No tasks found.");
            return;
        }

        System.out.println("\n=== YOUR TASKS ===");
        for (Task task : tasks) {
            System.out.print("ID: " + task.getId() + " | " + task.getTitle());
            if (task instanceof TimedTask timedTask) {
                System.out.print(" | Deadline: " + timedTask.getDeadline());
                if (timedTask.isOverdue()) {
                    System.out.print(" ⚠ OVERDUE (" + timedTask.daysOverdue() + " days)");
                }
            }
            System.out.println(task.isCompleted() ? " ✅ Completed" : "");
        }
    }

    private void completeTask() {
        listTasks();
        System.out.print("Enter Task ID to complete: ");
        int taskId = Integer.parseInt(scanner.nextLine());
        Task task = taskService.getTasksByUser(currentUser).stream()
                .filter(t -> t.getId() == taskId)
                .findFirst()
                .orElse(null);

        if (task != null) {
            taskService.completeTask(task);
            System.out.println("Task marked as completed.");
        } else {
            System.out.println("Invalid Task ID.");
        }
    }

    private void deleteTask() {
        listTasks();
        System.out.print("Enter Task ID to delete: ");
        int taskId = Integer.parseInt(scanner.nextLine());
        Task task = taskService.getTasksByUser(currentUser).stream()
                .filter(t -> t.getId() == taskId)
                .findFirst()
                .orElse(null);

        if (task != null) {
            taskService.deleteTask(task);
            System.out.println("Task deleted.");
        } else {
            System.out.println("Invalid Task ID.");
        }
    }

    /* ================= PROJECT MENU ================= */
    private void projectMenu() {
        while (true) {
            System.out.println("\n--- PROJECT MENU ---");
            System.out.println("1. Create Project");
            System.out.println("2. List Projects");
            System.out.println("3. Assign Task to Project");
            System.out.println("4. Delete Project");
            System.out.println("0. Back");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> createProject();
                case "2" -> listProjects();
                case "3" -> assignTaskToProject();
                case "4" -> deleteProject();
                case "0" -> { return; }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private void createProject() {
        System.out.print("Project name: ");
        String name = scanner.nextLine();
        projectService.createProject(name, currentUser);
        System.out.println("Project created.");
    }

    private void listProjects() {
        List<Project> projects = projectService.getProjectsByUser(currentUser);
        if (projects.isEmpty()) {
            System.out.println("No projects found.");
            return;
        }

        System.out.println("\n=== YOUR PROJECTS ===");
        for (Project project : projects) {
            System.out.println("- " + project.getName());

            // Project altındaki taskleri getir
            List<Task> tasksInProject = taskService.getTasksByProject(project.getId());
            if (tasksInProject.isEmpty()) {
                System.out.println("   (No tasks)");
            } else {
                for (Task task : tasksInProject) {
                    System.out.print(task.getId() + " | " + task.getTitle());
                    if (task instanceof TimedTask timedTask) {
                        System.out.print(" | Deadline: " + timedTask.getDeadline());
                        if (timedTask.isOverdue()) {
                            System.out.print(" ⚠ OVERDUE (" + timedTask.daysOverdue() + " days)");
                        }
                    }
                    System.out.println(task.isCompleted() ? " ✅ Completed" : "");
                }
            }
        }
    }

    private void assignTaskToProject() {
        List<Project> projects = projectService.getProjectsByUser(currentUser);
        if (projects.isEmpty()) {
            System.out.println("No projects available.");
            return;
        }

        System.out.println("\n=== PROJECTS ===");
        for (Project project : projects) {
            System.out.println(project.getId() + " | " + project.getName());
        }

        System.out.print("Enter Project ID to assign task: ");
        int projectId = Integer.parseInt(scanner.nextLine());
        Project project = projects.stream()
                .filter(p -> p.getId() == projectId)
                .findFirst()
                .orElse(null);

        if (project == null) {
            System.out.println("Project not found.");
            return;
        }

        List<Task> tasks = taskService.getTasksByUser(currentUser);
        if (tasks.isEmpty()) {
            System.out.println("No tasks to assign.");
            return;
        }

        System.out.println("\n=== TASKS ===");
        for (Task task : tasks) {
            System.out.print(task.getId() + " | " + task.getTitle());
            if (task instanceof TimedTask timedTask) {
                System.out.print(" | Deadline: " + timedTask.getDeadline());
            }
            System.out.println(task.isCompleted() ? " ✅ Completed" : "");
        }

        System.out.print("Enter Task ID to assign to project: ");
        int taskId = Integer.parseInt(scanner.nextLine());
        Task task = tasks.stream().filter(t -> t.getId() == taskId).findFirst().orElse(null);

        if (task != null) {
            projectService.addTaskToProject(project, task);
            System.out.println("Task assigned to project.");
        } else {
            System.out.println("Invalid Task ID.");
        }
    }

    private void deleteProject() {
        List<Project> projects = projectService.getProjectsByUser(currentUser);
        if (projects.isEmpty()) return;

        System.out.println("\n=== PROJECTS ===");
        for (Project project : projects) {
            System.out.println(project.getId() + " | " + project.getName());
        }

        System.out.print("Enter Project ID to delete: ");
        int projectId = Integer.parseInt(scanner.nextLine());
        Project project = projects.stream()
                .filter(p -> p.getId() == projectId)
                .findFirst()
                .orElse(null);

        if (project != null) {
            projectService.deleteProject(project);
            System.out.println("Project deleted.");
        } else {
            System.out.println("Invalid Project ID.");
        }
    }
}
