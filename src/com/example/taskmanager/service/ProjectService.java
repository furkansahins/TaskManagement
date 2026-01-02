package com.example.taskmanager.service;

import com.example.taskmanager.model.Project;
import com.example.taskmanager.model.Task;
import com.example.taskmanager.model.User;

import java.util.ArrayList;
import java.util.List;

public class ProjectService {

    private final List<Project> projects = new ArrayList<>();

    public Project createProject(String name, User owner) {
        if (name == null || name.isBlank()) {
            return null;
        }

        Project project = new Project(name, owner);
        projects.add(project);
        return project;
    }

    public List<Project> getProjectsByUser(User user) {
        List<Project> result = new ArrayList<>();

        for (Project project : projects) {
            if (project.getOwner().equals(user)) {
                result.add(project);
            }
        }
        return result;
    }

    public void addTaskToProject(Project project, Task task) {
        if (project != null && task != null) {
            project.addTask(task);
        }
    }

    public void deleteProject(Project project) {
        projects.remove(project);
    }
}
