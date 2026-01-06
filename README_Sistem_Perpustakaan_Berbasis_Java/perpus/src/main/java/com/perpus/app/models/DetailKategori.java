package com.perpus.app.models;

public class DetailKategori {

    private final Kategori kategori;
    private final Buku buku;

    public DetailKategori(Kategori kategori, Buku buku) {
        this.kategori = kategori;
        this.buku = buku;
    }

    public Kategori getKategori() {
        return kategori;
    }

    public Buku getBuku() {
        return buku;
    }
}
