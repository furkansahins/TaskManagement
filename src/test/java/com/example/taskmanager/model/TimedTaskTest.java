package com.example.taskmanager.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class TimedTaskTest {

    @Test
    void daysLeft_shouldReturnCorrectValue() {
        Deadline deadline = new Deadline(LocalDate.now().plusDays(5));

        TimedTask task = new TimedTask(
                1,
                "Test",
                Priority.MEDIUM,
                false,
                deadline
        );

        assertEquals(5, task.daysLeft());
    }

    @Test
    void isOverdue_shouldBeTrue_whenDeadlinePassedAndNotCompleted() {
        Deadline deadline = new Deadline(LocalDate.now().minusDays(1));

        TimedTask task = new TimedTask(
                1,
                "Overdue",
                Priority.HIGH,
                false,
                deadline
        );

        assertTrue(task.isOverdue());
    }

    @Test
    void isUpcoming_shouldBeTrue_within7Days() {
        Deadline deadline = new Deadline(LocalDate.now().plusDays(6));

        TimedTask task = new TimedTask(
                1,
                "Upcoming",
                Priority.MEDIUM,
                false,
                deadline
        );

        assertTrue(task.isUpcoming());
    }

    @Test
    void isUrgent_shouldBeTrue_within3Days() {
        Deadline deadline = new Deadline(LocalDate.now().plusDays(2));

        TimedTask task = new TimedTask(
                1,
                "Urgent",
                Priority.HIGH,
                false,
                deadline
        );

        assertTrue(task.isUrgent());
    }

    @Test
    void completedTask_shouldNotBeUpcomingOrUrgent() {
        Deadline deadline = new Deadline(LocalDate.now().plusDays(2));

        TimedTask task = new TimedTask(
                1,
                "Done",
                Priority.LOW,
                true,
                deadline
        );

        assertFalse(task.isUpcoming());
        assertFalse(task.isUrgent());
    }
}
