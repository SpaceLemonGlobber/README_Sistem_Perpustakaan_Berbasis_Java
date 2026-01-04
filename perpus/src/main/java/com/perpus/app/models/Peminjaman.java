package com.perpus.app.models;

import java.time.LocalDate;

public class Peminjaman {
    private int peminjamanId;
    private int anggotaId; 
    private int adminId;   
    private int bukuId;     
    private LocalDate tanggalPinjam;
    private LocalDate tanggalKembali;
    private String status;
    private double denda;

    // A. Constructor Kosong: Wajib agar 'new Peminjaman()' di DAO tidak error
    public Peminjaman() {}

    // B. Constructor (int, int): Wajib agar proses pinjamBuku tidak error
    public Peminjaman(int anggotaId, int bukuId) {
        this.anggotaId = anggotaId;
        this.bukuId = bukuId;
    }

    // --- GETTER ---
    public int getPeminjamanId() { return peminjamanId; }
    public int getAnggotaId() { return anggotaId; } 
    public int getAdminId() { return adminId; }
    public int getBukuId() { return bukuId; }
    public LocalDate getTanggalPinjam() { return tanggalPinjam; }
    public LocalDate getTanggalKembali() { return tanggalKembali; }
    public String getStatus() { return status; }
    public double getDenda() { return denda; }

    // --- SETTER (Lengkap sesuai kebutuhan DAO) ---
    public void setPeminjamanId(int peminjamanId) { this.peminjamanId = peminjamanId; }
    public void setAnggotaId(int anggotaId) { this.anggotaId = anggotaId; }
    public void setAdminId(int adminId) { this.adminId = adminId; }
    public void setBukuId(int bukuId) { this.bukuId = bukuId; }
    public void setTanggalPinjam(LocalDate tanggalPinjam) { this.tanggalPinjam = tanggalPinjam; }
    
    // Perbaikan Error: Method ini yang sebelumnya hilang di screenshot kamu
    public void setTanggalKembali(LocalDate tanggalKembali) { this.tanggalKembali = tanggalKembali; }
    public void setStatus(String status) { this.status = status; }
    public void setDenda(double denda) { this.denda = denda; }
}