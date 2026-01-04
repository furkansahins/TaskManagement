package com.example.taskmanager.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class DeadlineTest {

    @Test
    void shouldCalculateDaysLeft() {
        Deadline deadline = new Deadline(LocalDate.now().plusDays(5));

        long daysLeft = deadline.daysLeft();

        assertEquals(5, daysLeft);
    }

    @Test
    void shouldBeOverdueWhenDateIsPast() {
        Deadline deadline = new Deadline(LocalDate.now().minusDays(1));

        assertTrue(deadline.isOverdue());
    }

    @Test
    void shouldNotBeOverdueWhenDateIsFuture() {
        Deadline deadline = new Deadline(LocalDate.now().plusDays(2));

        assertFalse(deadline.isOverdue());
    }

    @Test
    void shouldBeUpcomingWithinGivenDays() {
        Deadline deadline = new Deadline(LocalDate.now().plusDays(3));

        assertTrue(deadline.isUpcoming(5));
    }

    @Test
    void shouldNotBeUpcomingOutsideGivenDays() {
        Deadline deadline = new Deadline(LocalDate.now().plusDays(10));

        assertFalse(deadline.isUpcoming(5));
    }
}
