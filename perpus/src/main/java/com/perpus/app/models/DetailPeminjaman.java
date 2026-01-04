package com.perpus.app.models;

public class DetailPeminjaman {

    private int detailId;
    private final Peminjaman peminjaman;
    private final Buku buku;
    private final int jumlah;

    // Constructor untuk INSERT
    public DetailPeminjaman(Peminjaman peminjaman, Buku buku, int jumlah) {
        this.peminjaman = peminjaman;
        this.buku = buku;
        this.jumlah = jumlah;
    }

    // Constructor untuk SELECT
    public DetailPeminjaman(int detailId, Peminjaman peminjaman, Buku buku, int jumlah) {
        this.detailId = detailId;
        this.peminjaman = peminjaman;
        this.buku = buku;
        this.jumlah = jumlah;
    }

    // Business logic
    public void kurangiStokBuku() {
        buku.updateStok(-jumlah);
    }

    public void kembalikanBuku() {
        buku.updateStok(jumlah);
    }

    // Getter
    public int getDetailId() { return detailId; }
    public Peminjaman getPeminjaman() { return peminjaman; }
    public Buku getBuku() { return buku; }
    public int getJumlah() { return jumlah; }
}
