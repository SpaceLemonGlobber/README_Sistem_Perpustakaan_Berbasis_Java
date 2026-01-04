package com.perpus.app.models;

import java.time.LocalDate;

public class Peminjaman {

    // 1. Pastikan nama variabel sesuai dengan yang dipanggil constructor/DAO
    private int peminjamanId;
    private int anggotaId; 
    private int adminId;   
    private int bukuId;    
    private LocalDate tanggalPinjam;
    private LocalDate tanggalKembali;
    private String status;
    private double denda;

    public Peminjaman() {
    }

    // Constructor untuk INSERT/SELECT simpel (memperbaiki error image_022287.png)
    public Peminjaman(int anggotaId, int bukuId) {
        this.anggotaId = anggotaId;
        this.bukuId = bukuId;
    }

    // Constructor Lengkap untuk SELECT dari Database
    public Peminjaman(int peminjamanId, int anggotaId, int adminId, int bukuId,
                      LocalDate tanggalPinjam, LocalDate tanggalKembali,
                      String status, double denda) {
        this.peminjamanId = peminjamanId;
        this.anggotaId = anggotaId;
        this.adminId = adminId;
        this.bukuId = bukuId;
        this.tanggalPinjam = tanggalPinjam;
        this.tanggalKembali = tanggalKembali;
        this.status = status;
        this.denda = denda;
    }

    // --- GETTER (Sangat Penting untuk PeminjamanDAO agar tidak error image_021f04.png) ---
    public int getPeminjamanId() { return peminjamanId; }
    public int getAnggotaId() { return anggotaId; } // Method ini yang dicari DAO
    public int getAdminId() { return adminId; }
    public int getBukuId() { return bukuId; }
    public LocalDate getTanggalPinjam() { return tanggalPinjam; }
    public LocalDate getTanggalKembali() { return tanggalKembali; }
    public String getStatus() { return status; }
    public double getDenda() { return denda; }

    // --- SETTER ---
    public void setPeminjamanId(int peminjamanId) { this.peminjamanId = peminjamanId; }
    public void setAnggotaId(int anggotaId) { this.anggotaId = anggotaId; }
    public void setAdminId(int adminId) { this.adminId = adminId; }
    public void setBukuId(int bukuId) { this.bukuId = bukuId; }
    public void setTanggalPinjam(LocalDate tanggalPinjam) { this.tanggalPinjam = tanggalPinjam; }
    public void setStatus(String status) { this.status = status; }
}