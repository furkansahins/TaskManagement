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
            System.out.println("Login successful!");
            userMenu();
        } else {
            System.out.println("Login failed.");
        }
    }

    private void register() {
        System.out.print("Username: ");
        String username = scanner.nextLine();

        System.out.print("Password: ");
        String password = scanner.nextLine();

        userService.register(username, password);
        System.out.println("User registered.");
    }

    /* ================= USER MENU ================= */

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
        System.out.print("Task title: ");
        String title = scanner.nextLine();

        Priority priority = askPriority();

        taskService.createTask(title, priority, currentUser.getId());
        System.out.println("Task created.");
    }

    private void addTimedTask() {
        System.out.print("Task title: ");
        String title = scanner.nextLine();

        Priority priority = askPriority();

        System.out.print("Deadline (yyyy-MM-dd): ");
        LocalDate deadline = LocalDate.parse(scanner.nextLine());

        taskService.createTimedTask(title, priority, currentUser.getId(), deadline);
        System.out.println("Timed task created.");
    }

    private void listTasks() {
        List<Task> tasks = taskService.getTasksByUserId(currentUser.getId());

        if (tasks.isEmpty()) {
            System.out.println("No tasks.");
            return;
        }

        System.out.println("\n=== TASKS ===");

        for (Task task : tasks) {
            printTask(task);
        }
    }

    private void completeTask() {
        listTasks();

        System.out.print("Enter Task ID to complete: ");
        int taskId = Integer.parseInt(scanner.nextLine());

        boolean success = taskService.completeTask(taskId, currentUser.getId());

        System.out.println(success ? "Task completed." : "Task not found.");
    }

    private void deleteTask() {
        listTasks();

        System.out.print("Enter Task ID to delete: ");
        int taskId = Integer.parseInt(scanner.nextLine());

        boolean success = taskService.deleteTask(taskId, currentUser.getId());

        System.out.println(success ? "Task deleted." : "Task not found.");
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
        String projectName = scanner.nextLine();

        projectService.createProject(projectName, currentUser.getId());
        System.out.println("Project created.");
    }

    private void listProjects() {
        List<Project> projects =
                projectService.getProjectsByUser(currentUser.getId());

        if (projects.isEmpty()) {
            System.out.println("No projects.");
            return;
        }

        System.out.println("\n=== PROJECTS ===");

        for (Project project : projects) {
            System.out.println(project.getId() + " | " + project.getName());

            List<Task> tasks =
                    taskService.getTasksByProject(project.getId());

            for (Task task : tasks) {
                System.out.print("   - ");
                printTask(task);
            }
        }
    }

    private void assignTaskToProject() {
        listProjects();

        System.out.print("Enter Project ID: ");
        int projectId = Integer.parseInt(scanner.nextLine());

        listTasks();

        System.out.print("Enter Task ID: ");
        int taskId = Integer.parseInt(scanner.nextLine());

        boolean success =
                projectService.assignTaskToProject(projectId, taskId, currentUser.getId());

        System.out.println(success ? "Task assigned." : "Assignment failed.");
    }

    private void deleteProject() {
        listProjects();

        System.out.print("Enter Project ID to delete: ");
        int projectId = Integer.parseInt(scanner.nextLine());

        boolean success =
                projectService.deleteProject(projectId, currentUser.getId());

        System.out.println(success ? "Project deleted." : "Project not found.");
    }

    /* ================= UPCOMING DEADLINES ================= */

    private void showUpcomingDeadlines() {
        List<TimedTask> tasks =
                taskService.getUpcomingTimedTasks(currentUser.getId());

        if (tasks.isEmpty()) {
            System.out.println("No upcoming deadlines.");
            return;
        }

        System.out.println("\n=== UPCOMING DEADLINES ===");

        for (TimedTask task : tasks) {
            long daysLeft = task.daysLeft();

            if (daysLeft <= 3) {
                System.out.println("⚠ " + task.getTitle()
                        + " | Deadline: " + task.getDeadline()
                        + " (" + daysLeft + " days left)");
            } else {
                System.out.println("ℹ " + task.getTitle()
                        + " | Deadline: " + task.getDeadline()
                        + " (" + daysLeft + " days left)");
            }
        }
    }

    /* ================= HELPERS ================= */

    private Priority askPriority() {
        while (true) {
            System.out.println("Priority:");
            System.out.println("1 - LOW");
            System.out.println("2 - MEDIUM");
            System.out.println("3 - HIGH");
            System.out.print("Choice: ");

            String input = scanner.nextLine().trim();

            if (input.isEmpty()) {
                return null; // ENTER → priority yok
            }

            switch (input) {
                case "1":
                    return Priority.LOW;
                case "2":
                    return Priority.MEDIUM;
                case "3":
                    return Priority.HIGH;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }



    private void printTask(Task task) {
        System.out.print(
                task.getId()
                        + " | " + task.getTitle()
                        + (task.getPriority() != null ? " | " + task.getPriority() : "")

        );

        if (task instanceof TimedTask timedTask) {
            System.out.print(" | Deadline: " + timedTask.getDeadline());

            if (timedTask.isOverdue()) {
                System.out.print(" ⚠ OVERDUE");
            }
        }

        if (task.isCompleted()) {
            System.out.print(" ✅ DONE");
        }

        System.out.println();
    }
}
