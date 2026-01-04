package com.example.taskmanager.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    @Test
    void taskInitiallyNotCompleted() {
        Task task = new Task(
                1,
                "Test Task",
                Priority.LOW,
                false
        );

        assertFalse(task.isCompleted());
    }

    @Test
    void completeMarksTaskAsCompleted() {
        Task task = new Task(
                1,
                "Test Task",
                Priority.MEDIUM,
                false
        );

        task.complete();

        assertTrue(task.isCompleted());
    }
}
