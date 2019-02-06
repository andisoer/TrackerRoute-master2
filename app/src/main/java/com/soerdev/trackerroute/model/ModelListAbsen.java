package com.soerdev.trackerroute.model;

public class ModelListAbsen {

    private String id, username, kodeUnik, link_gambar, awal, akhir, id_device, date, waktu_awal, waktu_akhir;

    public ModelListAbsen() {

    }

    public ModelListAbsen(String id, String username, String kodeUnik, String link_gambar, String awal, String akhir, String id_device, String date, String waktu_awal, String waktu_akhir) {
        this.id = id;
        this.username = username;
        this.kodeUnik = kodeUnik;
        this.link_gambar = link_gambar;
        this.awal = awal;
        this.akhir = akhir;
        this.id_device = id_device;
        this.date = date;
        this.waktu_awal = waktu_awal;
        this.waktu_akhir = waktu_akhir;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getKodeUnik() {
        return kodeUnik;
    }

    public void setKodeUnik(String kodeUnik) {
        this.kodeUnik = kodeUnik;
    }

    public String getLink_gambar() {
        return link_gambar;
    }

    public void setLink_gambar(String link_gambar) {
        this.link_gambar = link_gambar;
    }

    public String getAwal() {
        return awal;
    }

    public void setAwal(String awal) {
        this.awal = awal;
    }

    public String getAkhir() {
        return akhir;
    }

    public void setAkhir(String akhir) {
        this.akhir = akhir;
    }

    public String getId_device() {
        return id_device;
    }

    public void setId_device(String id_device) {
        this.id_device = id_device;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getWaktu_awal() {
        return waktu_awal;
    }

    public void setWaktu_awal(String waktu_awal) {
        this.waktu_awal = waktu_awal;
    }

    public String getWaktu_akhir() {
        return waktu_akhir;
    }

    public void setWaktu_akhir(String waktu_akhir) {
        this.waktu_akhir = waktu_akhir;
    }
}
