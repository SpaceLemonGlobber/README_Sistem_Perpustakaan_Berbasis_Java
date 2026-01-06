package com.perpus.app.models;

public abstract class User {

    protected int userId;
    protected String username;
    protected String password;
    protected String nama;

    public User() {
    }

    public User(int userId, String username, String password, String nama) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.nama = nama;
    }

    // --- PERBAIKAN UTAMA ---
    // Tambahkan method ini agar pemanggilan getId() di Controller tidak error
    public int getId() { 
        return userId; 
    }

    public int getUserId() { return userId; }
    public String getUsername() { return username; }
    public String getNama() { return nama; }
    public String getPassword() { return password; }

    public void setUserId(int userId) { this.userId = userId; }
    public void setUsername(String username) { this.username = username; }
    public void setPassword(String password) { this.password = password; }
    public void setNama(String nama) { this.nama = nama; }

    public abstract String getRole();
}