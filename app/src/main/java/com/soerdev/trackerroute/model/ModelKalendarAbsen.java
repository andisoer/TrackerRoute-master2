package com.soerdev.trackerroute.model;

public class ModelKalendarAbsen {

    private String userName, tanggalAbsen;

    public ModelKalendarAbsen() {
    }

    public ModelKalendarAbsen(String userName, String tanggalAbsen) {
        this.userName = userName;
        this.tanggalAbsen = tanggalAbsen;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTanggalAbsen() {
        return tanggalAbsen;
    }

    public void setTanggalAbsen(String tanggalAbsen) {
        this.tanggalAbsen = tanggalAbsen;
    }
}
