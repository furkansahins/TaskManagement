package com.example.taskmanager.service;

import com.example.taskmanager.model.Project;
import com.example.taskmanager.model.Task;

import java.util.ArrayList;
import java.util.List;

public class ProjectService {

    private List<Project> projects = new ArrayList<>();
    private long idCounter = 1;

    public Project createProject(String name, String description) {
        if (name == null || name.isBlank()) {
            return null;
        }

        Project project = new Project(idCounter++, name, description);
        projects.add(project);
        return project;
    }

    public void addTaskToProject(Project project, Task task) {
        project.addTask(task);
    }

    public List<Project> getAllProjects() {
        return projects;
    }
}
