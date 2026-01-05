package com.example.taskmanager.model;
/**
 * Sistemdeki temel görev sınıfıdır.
 * Görevlerin öncelik bilgisi olabilir ve tamamlanabilir.
 */
public class Task implements Completable {

    protected int id;
    protected String title;
    protected Priority priority;
    protected boolean completed;

    public Task(int id, String title, Priority priority, boolean completed) {
        this.id = id;
        this.title = title;
        this.priority = priority;
        this.completed = completed;
    }
    /**
     * Görevi tamamlandı olarak işaretler.
     */
    @Override
    public void complete() {
        this.completed = true;
    }
    /**
     * @return görev tamamlandıysa true, değilse false
     */
    @Override
    public boolean isCompleted() {
        return completed;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }
    public Priority getPriority() {
        return priority;
    }

}
