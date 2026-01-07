package com.perpus.app.models;

public class DetailPeminjaman {

    private int detailId;
    private final Peminjaman peminjaman;
    private final Buku buku;
    private final int jumlah_pinjam;

    public DetailPeminjaman(Peminjaman peminjaman, Buku buku, int jumlah_pinjam) {
        this.peminjaman = peminjaman;
        this.buku = buku;
        this.jumlah_pinjam = jumlah_pinjam;
    }

    public DetailPeminjaman(int detailId, Peminjaman peminjaman, Buku buku, int jumlah_pinjam) {
        this.detailId = detailId;
        this.peminjaman = peminjaman;
        this.buku = buku;
        this.jumlah_pinjam = jumlah_pinjam;
    }

    public int getDetailId() { return detailId; }
    public Peminjaman getPeminjaman() { return peminjaman; }
    public Buku getBuku() { return buku; }
    public int getJumlah_pinjam() { return jumlah_pinjam; }
}