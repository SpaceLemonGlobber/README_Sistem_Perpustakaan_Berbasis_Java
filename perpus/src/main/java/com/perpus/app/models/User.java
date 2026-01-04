package com.perpus.app.models;

public abstract class User {

    // Menggunakan protected agar bisa diakses langsung oleh class Admin dan Anggota
    protected int userId;
    protected String username;
    protected String password;
    protected String nama;

    // Constructor Kosong
    public User() {
    }

    // Constructor Lengkap
    public User(int userId, String username, String password, String nama) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.nama = nama;
    }

    // --- GETTER (Untuk keperluan SELECT & INSERT di DAO) ---
    public int getUserId() { return userId; }
    public String getUsername() { return username; }
    public String getNama() { return nama; }
    
    // Tambahan: Agar UserDAO bisa mengambil password saat validasi Login
    public String getPassword() { return password; }

    // --- SETTER (Penting! Agar DAO bisa mengisi data dari Database ke Objek) ---
    public void setUserId(int userId) { this.userId = userId; }
    public void setUsername(String username) { this.username = username; }
    public void setPassword(String password) { this.password = password; }
    public void setNama(String nama) { this.nama = nama; }

    // Polymorphism point: Akan diimplementasikan berbeda di Admin dan Anggota
    public abstract String getRole();
}