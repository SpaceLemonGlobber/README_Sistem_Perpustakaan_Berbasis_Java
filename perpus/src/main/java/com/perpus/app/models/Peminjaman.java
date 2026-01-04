package com.perpus.app.models;

import java.time.LocalDate;

public class Peminjaman {

    private int peminjamanId;
    private Anggota anggota;
    private Admin admin;
    private LocalDate tanggalPinjam;
    private LocalDate tanggalKembali;
    private StatusPeminjaman status;
    private double denda;
    private Buku buku;

    public Peminjaman() {
    }

    // Tambahkan di dalam class Peminjaman.java
    public Peminjaman(Anggota anggota, Buku buku) {
        this.anggota = anggota;
        this.buku = buku;
    }

    // Constructor untuk INSERT
    public Peminjaman(Anggota anggota, Admin admin, LocalDate tanggalPinjam) {
        this.anggota = anggota;
        this.admin = admin;
        this.tanggalPinjam = tanggalPinjam;
        this.status = StatusPeminjaman.DIPINJAM;
        this.denda = 0;
    }

    // Constructor untuk SELECT
    public Peminjaman(int peminjamanId, Anggota anggota, Admin admin,
                      LocalDate tanggalPinjam, LocalDate tanggalKembali,
                      StatusPeminjaman status, double denda) {
        this.peminjamanId = peminjamanId;
        this.anggota = anggota;
        this.admin = admin;
        this.tanggalPinjam = tanggalPinjam;
        this.tanggalKembali = tanggalKembali;
        this.status = status;
        this.denda = denda;
    }

    // Business logic (OOP point)
    public void kembalikan(LocalDate tanggalKembali, double denda) {
        this.tanggalKembali = tanggalKembali;
        this.denda = denda;
        this.status = StatusPeminjaman.DIKEMBALIKAN;
    }

    // Getter
    public int getPeminjamanId() { return peminjamanId; }
    public Anggota getAnggota() { return anggota; }
    public Admin getAdmin() { return admin; }
    public LocalDate getTanggalPinjam() { return tanggalPinjam; }
    public LocalDate getTanggalKembali() { return tanggalKembali; }
    public StatusPeminjaman getStatus() { return status; }
    public double getDenda() { return denda; }


    public void setPeminjamanId(int peminjamanId) { 
        this.peminjamanId = peminjamanId; 
    }

    public void setAnggota(Anggota anggota) { 
        this.anggota = anggota; 
    }

    public void setBuku(Buku buku) { 
        this.buku = buku; 
    }

    public void setTanggalPinjam(LocalDate tanggalPinjam) { 
        this.tanggalPinjam = tanggalPinjam; 
    }

    public void setStatus(StatusPeminjaman status) { 
        this.status = status; 
    }

    // --- GETTER TAMBAHAN ---
    public Buku getBuku() { 
        return buku; 
    }
}
