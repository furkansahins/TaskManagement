package com.example.taskmanager.model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
/**
 * Görevlerin son tarih bilgisini ve zaman hesaplamalarını kapsüller.
 * Tarihle ilgili tüm mantık bu sınıfta tutulur.
 */
public class Deadline {

    private final LocalDate date;
    /**
     * @param date görevin son tarihi
     */
    public Deadline(LocalDate date) {
        this.date = date;
    }


    public LocalDate getDate() {
        return date;
    }
    /**
     * @return son tarihe kalan gün sayısı
     */
    public long daysLeft() {
        return ChronoUnit.DAYS.between(LocalDate.now(), date);
    }

    public boolean isOverdue() {
        return date.isBefore(LocalDate.now());
    }
    /**
     * @param days kontrol edilecek gün aralığı
     * @return son tarih belirtilen gün aralığındaysa true
     */
    public boolean isUpcoming(int days) {
        long left = daysLeft();
        return left >= 0 && left <= days;
    }
    /**
     * Deadline nesnesinin kullanıcıya okunabilir şekilde yazdırılmasını sağlar.
     */
    @Override
    public String toString() {
        return date.toString(); // SADECE TARİH
    }
}
