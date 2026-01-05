package com.example.taskmanager.model;
/**
 * Tamamlanabilir nesneler için davranış tanımlar.
 */
public interface Completable {
    /**
     * Nesneyi tamamlandı olarak işaretler.
     */
    void complete();
    /**
     * @return tamamlandıysa true
     */
    boolean isCompleted();
}
