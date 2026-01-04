package com.example.taskmanager.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProjectTest {

    @Test
    void projectIsCreatedCorrectly() {
        Project project = new Project(
                1,
                "Test Project",
                10
        );

        assertEquals(1, project.getId());
        assertEquals("Test Project", project.getName());
        assertEquals(10, project.getOwnerId());
    }
}
