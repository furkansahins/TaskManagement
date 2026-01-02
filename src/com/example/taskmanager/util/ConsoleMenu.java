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
        System.out.println("\n1. Login");
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
            System.out.println("\n1. Task Menu");
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
            System.out.println("\n1. Add Task");
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
    }

    private void addTimedTask() {
        System.out.print("Title: ");
        String title = scanner.nextLine();

        System.out.print("Deadline (yyyy-MM-dd): ");
        LocalDate deadline = LocalDate.parse(scanner.nextLine());

        taskService.createTimedTask(title, "", Priority.HIGH, currentUser, deadline);
    }

    private void listTasks() {
        List<Task> tasks = taskService.getTasksByUser(currentUser);

        if (tasks.isEmpty()) {
            System.out.println("No tasks found.");
            return;
        }

        for (int i = 0; i < tasks.size(); i++) {
            Task task = tasks.get(i);
            System.out.print((i + 1) + ". " + task.getTitle());

            if (task instanceof TimedTask timedTask && timedTask.isOverdue()) {
                System.out.print(" ⚠ OVERDUE (" + timedTask.daysOverdue() + " days)");
            }
            System.out.println();
        }
    }

    private void completeTask() {
        List<Task> tasks = taskService.getTasksByUser(currentUser);
        if (tasks.isEmpty()) return;

        listTasks();
        System.out.print("Select task number: ");

        int index = Integer.parseInt(scanner.nextLine()) - 1;
        taskService.completeTask(tasks.get(index));
    }

    private void deleteTask() {
        List<Task> tasks = taskService.getTasksByUser(currentUser);
        if (tasks.isEmpty()) return;

        listTasks();
        System.out.print("Select task number: ");

        int index = Integer.parseInt(scanner.nextLine()) - 1;
        taskService.deleteTask(tasks.get(index));
    }

    /* ================= PROJECT MENU ================= */

    private void projectMenu() {
        while (true) {
            System.out.println("\n1. Create Project");
            System.out.println("2. List Projects");
            System.out.println("3. Assign Task to Project");
            System.out.println("4. List Tasks in Project");
            System.out.println("5. Delete Project");
            System.out.println("0. Back");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> createProject();
                case "2" -> listProjects();
                case "3" -> assignTaskToProject();
                case "4" -> listTasksInProject();
                case "5" -> deleteProject();
                case "0" -> { return; }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private void createProject() {
        System.out.print("Project name: ");
        String name = scanner.nextLine();

        projectService.createProject(name, currentUser);
    }

    private void listProjects() {
        List<Project> projects = projectService.getProjectsByUser(currentUser);

        if (projects.isEmpty()) {
            System.out.println("No projects found.");
            return;
        }

        for (int i = 0; i < projects.size(); i++) {
            System.out.println((i + 1) + ". " + projects.get(i).getName());
        }
    }

    private void assignTaskToProject() {
        List<Project> projects = projectService.getProjectsByUser(currentUser);
        List<Task> tasks = taskService.getTasksByUser(currentUser);

        if (projects.isEmpty() || tasks.isEmpty()) return;

        listProjects();
        System.out.print("Select project: ");
        int pIndex = Integer.parseInt(scanner.nextLine()) - 1;

        listTasks();
        System.out.print("Select task: ");
        int tIndex = Integer.parseInt(scanner.nextLine()) - 1;

        projectService.addTaskToProject(projects.get(pIndex), tasks.get(tIndex));
    }

    private void listTasksInProject() {
        List<Project> projects = projectService.getProjectsByUser(currentUser);
        if (projects.isEmpty()) return;

        listProjects();
        System.out.print("Select project: ");
        int index = Integer.parseInt(scanner.nextLine()) - 1;

        List<Task> tasks = projects.get(index).getTasks();
        if (tasks.isEmpty()) {
            System.out.println("No tasks in this project.");
            return;
        }

        for (Task task : tasks) {
            System.out.println("- " + task.getTitle());
        }
    }

    private void deleteProject() {
        List<Project> projects = projectService.getProjectsByUser(currentUser);
        if (projects.isEmpty()) return;

        listProjects();
        System.out.print("Select project: ");
        int index = Integer.parseInt(scanner.nextLine()) - 1;

        projectService.deleteProject(projects.get(index));
    }
}
