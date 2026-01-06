package com.perpus.app.models;

public class Anggota extends User {

    private int anggotaId; 
    private String email;
    private String no_telp;

    public Anggota() {
        super();
    }

    // Constructor 4 Parameter (BARU - Untuk memperbaiki error image_022a81.png)
    public Anggota(int userId, String username, String password, String nama) {
        super(userId, username, password, nama);
    }

    // Constructor Lengkap 7 Parameter
    public Anggota(int userId, String username, String password, String nama, int anggotaId, String email, String no_telp) {
        super(userId, username, password, nama);
        this.anggotaId = anggotaId;
        this.email = email;
        this.no_telp = no_telp;
    }

    // Constructor Singkat
    public Anggota(int id, String nama) {
        this.userId = id;
        this.nama = nama;
    }

    @Override
    public String getRole() {
        return "ANGGOTA";
    }

    // Getter & Setter
    public int getAnggotaId() { return anggotaId; }
    public void setAnggotaId(int anggotaId) { this.anggotaId = anggotaId; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getNo_telp() { return no_telp; }
    public void setNo_telp(String no_telp) { this.no_telp = no_telp; }
}