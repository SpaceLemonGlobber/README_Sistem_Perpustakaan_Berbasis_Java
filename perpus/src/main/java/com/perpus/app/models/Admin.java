package com.perpus.app.models;

public class Admin extends User {

    public Admin(int userId, String username, String password, String nama) {
        super(userId, username, password, nama);
    }

    // Tambahkan constructor ini
    public Admin(int id, String username) {
        super(); // Memanggil constructor kosong di User
        this.userId = id;
        this.username = username;
    }
    
    // Constructor lama kamu (int, String, String, String) tetap biarkan ada


    @Override
    public String getRole() {
        return "ADMIN";
    }
}
