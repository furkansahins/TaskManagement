package com.example.taskmanager.model;
/**
 * Deadline (son tarih) bilgisi olan görevleri temsil eder.
 * Task sınıfından kalıtım alır ve zaman bazlı kontroller ekler.
 */
public class TimedTask extends Task {

    private Deadline deadline;
    /**
     * @param deadline görevin son tarih bilgisi
     */
    public TimedTask(int id,
                     String title,
                     Priority priority,
                     boolean completed,
                     Deadline deadline) {

        super(id, title, priority, completed);
        this.deadline = deadline;
    }

    public Deadline getDeadline() {
        return deadline;
    }
    /**
     * @return görevin bitimine kalan gün sayısı
     */
    public long daysLeft() {
        return deadline.daysLeft();
    }
    /**
     * @return görev tamamlanmadıysa ve süresi geçtiyse true
     */
    public boolean isOverdue() {
        return !completed && deadline.isOverdue();
    }
    /**
     * @return görev önümüzdeki 7 gün içindeyse true
     */
    public boolean isUpcoming() {
        return !completed && deadline.isUpcoming(7);
    }
    /**
     * @return görev önümüzdeki 3 gün içindeyse true
     */
    public boolean isUrgent() {
        return !completed && deadline.isUpcoming(3);
    }
}
