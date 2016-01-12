package com.marek.astronotes.entity;

/**
 * Created by Marek on 11/24/2015.
 */
public class MessierTrophy extends MessierObject {
    private String note;

    public MessierTrophy() {
        setNote("");
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
