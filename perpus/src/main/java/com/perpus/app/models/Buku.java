package com.perpus.app.models;

public class Buku {

    private int bukuId;
    private String judul;
    private String penerbit;
    private int tahunTerbit;
    private int stok;

    // Constructor untuk SELECT (dari DB)
    public Buku(int bukuId, String judul, String penerbit, int tahunTerbit, int stok) {
        this.bukuId = bukuId;
        this.judul = judul;
        this.penerbit = penerbit;
        this.tahunTerbit = tahunTerbit;
        this.stok = stok;
    }

    // Constructor untuk INSERT (ke DB)
    public Buku(String judul, String penerbit, int tahunTerbit, int stok) {
        this.judul = judul;
        this.penerbit = penerbit;
        this.tahunTerbit = tahunTerbit;
        this.stok = stok;
    }

    // Overloading
    public void updateStok(int perubahan) {
        if (stok + perubahan >= 0) {
            stok += perubahan;
        }
    }

    public void updateStok(boolean tambah, int jumlah) {
        updateStok(tambah ? jumlah : -jumlah);
    }

    // Getter & Setter
    public int getBukuId() { return bukuId; }

    public String getJudul() { return judul; }
    public void setJudul(String judul) { this.judul = judul; }

    public String getPenerbit() { return penerbit; }
    public void setPenerbit(String penerbit) { this.penerbit = penerbit; }

    public int getTahunTerbit() { return tahunTerbit; }
    public void setTahunTerbit(int tahunTerbit) { this.tahunTerbit = tahunTerbit; }

    

    public int getStok() { return stok; }

    @Override
    public String toString() {
        return judul + " (" + penerbit + ", " + tahunTerbit + ")";
    }
}
