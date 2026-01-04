package com.perpus.app.models;

public class Buku {

    private int bukuId;
    private int kategoriID; 
    private String judul;
    private String penerbit;
    private int tahun_terbit; // Tetap gunakan snake_case untuk variabel jika mau
    private int stok;

    public Buku() {}

    // Constructor untuk SELECT
    public Buku(int bukuId, int kategoriID, String judul, String penerbit, int tahun_terbit, int stok) {
        this.bukuId = bukuId;
        this.kategoriID = kategoriID;
        this.judul = judul;
        this.penerbit = penerbit;
        this.tahun_terbit = tahun_terbit;
        this.stok = stok;
    }

    // Constructor untuk INSERT 
    public Buku(int kategoriID, String judul, String penerbit, int tahun_terbit, int stok) {
        this.kategoriID = kategoriID;
        this.judul = judul;
        this.penerbit = penerbit;
        this.tahun_terbit = tahun_terbit;
        this.stok = stok;
    }

    // --- GETTER (Diselaraskan dengan panggilan di BukuDAO) ---
    public int getBukuId() { return bukuId; }
    public int getKategoriID() { return kategoriID; }
    public String getJudul() { return judul; }
    public String getPenerbit() { return penerbit; }
    
    // Perbaikan: Ubah nama method agar sesuai dengan image_0280e0.png
    public int getTahunTerbit() { return tahun_terbit; } 
    
    public int getStok() { return stok; }

    // --- SETTER ---
    public void setBukuId(int bukuId) { this.bukuId = bukuId; }
    public void setKategoriID(int kategoriID) { this.kategoriID = kategoriID; }
    public void setJudul(String judul) { this.judul = judul; }
    public void setPenerbit(String penerbit) { this.penerbit = penerbit; }
    public void setTahunTerbit(int tahun_terbit) { this.tahun_terbit = tahun_terbit; }
    public void setStok(int stok) { this.stok = stok; }

    @Override
    public String toString() {
        return judul + " (" + penerbit + ", " + tahun_terbit + ")";
    }
}