package com.marek.astronotes.entity;

/**
 * Created by Marek on 11/21/2015.
 */
public class MessierObject {
    private int messierNumber;
    private String ngcNumber;
    private String type;
    private String pictureUrl;
    private String constellation;
    private double apparentMagnitude;
    private String note;

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setMessierNumber(int messierNumber) {
        this.messierNumber = messierNumber;
    }

    public void setNgcNumber(String ngcNumber) {
        this.ngcNumber = ngcNumber;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public void setConstellation(String constellation) {
        this.constellation = constellation;
    }

    public void setApparentMagnitude(double apparentMagnitude) {
        this.apparentMagnitude = apparentMagnitude;
    }

    public int getMessierNumber() {

        return messierNumber;
    }

    public String getNgcNumber() {
        return ngcNumber;
    }

    public String getType() {
        return type;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public String getConstellation() {
        return constellation;
    }

    public double getApparentMagnitude() {
        return apparentMagnitude;
    }

    public String getMessierString() {
        return "M " + String.valueOf(messierNumber);
    }
}
