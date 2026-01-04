package com.perpus.app.models;

public class Kategori {

    private int kategoriId;
    private String namaKategori;
    private String deskripsi;

    // Constructor SELECT
    public Kategori(int kategoriId, String namaKategori, String deskripsi) {
        this.kategoriId = kategoriId;
        this.namaKategori = namaKategori;
        this.deskripsi = deskripsi;
    }

    // Constructor INSERT
    public Kategori(String namaKategori, String deskripsi) {
        this.namaKategori = namaKategori;
        this.deskripsi = deskripsi;
    }

    public int getKategoriId() {
        return kategoriId;
    }

    public String getNamaKategori() {
        return namaKategori;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    @Override
    public String toString() {
        return namaKategori;
    }
}
