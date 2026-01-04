package com.perpus.app.models;

public class Kategori {

    private int kategoriId;
    private final String nama_kategori;
    private final String deskripsi;

    // Constructor SELECT
    public Kategori(int kategoriId, String nama_kategori, String deskripsi) {
        this.kategoriId = kategoriId;
        this.nama_kategori = nama_kategori;
        this.deskripsi = deskripsi;
    }

    // Constructor INSERT
    public Kategori(String nama_kategori, String deskripsi) {
        this.nama_kategori = nama_kategori;
        this.deskripsi = deskripsi;
    }

    public int getKategoriId() {
        return kategoriId;
    }

    public String getnama_kategori() {
        return nama_kategori;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    @Override
    public String toString() {
        return nama_kategori;
    }
}
