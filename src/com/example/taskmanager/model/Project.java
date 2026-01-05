package com.example.taskmanager.model;
/**
 * Kullanıcıya ait projeleri temsil eder.
 */
public class Project {

    private int id;
    private String name;
    private int ownerId;

    public Project(int id, String name, int ownerId) {
        this.id = id;
        this.name = name;
        this.ownerId = ownerId;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
    /**
     * @return projeyi oluşturan kullanıcının id bilgisi
     */
    public int getOwnerId() {
        return ownerId;
    }

}
