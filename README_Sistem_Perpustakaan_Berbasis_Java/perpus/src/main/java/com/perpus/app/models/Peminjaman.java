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

    private String namaAnggota;
    private String judulBuku;

    public Peminjaman() {}

    public Peminjaman(int anggotaId, int bukuId) {
        this.anggotaId = anggotaId;
        this.bukuId = bukuId;
    }

    public int getId() { return peminjamanId; } 

    public int getAdminId() { return adminId; }
    public void setAdminId(int adminId) { this.adminId = adminId; }

    public void setTanggalPeminjaman(LocalDate date) { this.tanggalPinjam = date; }
    public LocalDate getTanggalPeminjaman() { return tanggalPinjam; }

    public void setTanggalPengembalian(LocalDate date) { this.tanggalKembali = date; }
    public LocalDate getTanggalPengembalian() { return tanggalKembali; }

    public int getPeminjamanId() { return peminjamanId; }
    public void setPeminjamanId(int peminjamanId) { this.peminjamanId = peminjamanId; }
    
    public int getAnggotaId() { return anggotaId; } 
    public void setAnggotaId(int anggotaId) { this.anggotaId = anggotaId; }
    
    public int getBukuId() { return bukuId; }
    public void setBukuId(int bukuId) { this.bukuId = bukuId; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public double getDenda() { return denda; }
    public void setDenda(double denda) { this.denda = denda; }

    public String getNamaAnggota() { return namaAnggota; }
    public void setNamaAnggota(String namaAnggota) { this.namaAnggota = namaAnggota; }
    
    public String getJudulBuku() { return judulBuku; }
    public void setJudulBuku(String judulBuku) { this.judulBuku = judulBuku; }
}