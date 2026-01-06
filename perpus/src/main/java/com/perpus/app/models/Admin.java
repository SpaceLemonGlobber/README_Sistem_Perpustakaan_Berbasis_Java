package com.perpus.app.models;

public class Admin extends User {
    private int adminId;
    private String email;

    public Admin() {
        super();
    }

    // 1. Constructor 4 Parameter (Memperbaiki error image_022a81.png)
    public Admin(int userId, String username, String password, String nama) {
        super(userId, username, password, nama);
    }

    // 2. Constructor Lengkap (6 Parameter)
    public Admin(int userId, String username, String password, String nama, int adminId, String email) {
        super(userId, username, password, nama);
        this.adminId = adminId;
        this.email = email;
    }

    // 3. Constructor Singkat (Diperbaiki agar menggunakan super)
    public Admin(int id, String username) {
        super(id, username, null, null); 
    }

    @Override
    public String getRole() {
        return "ADMIN";
    }

    // Getter dan Setter
    public int getAdminId() { return adminId; }
    public void setAdminId(int adminId) { this.adminId = adminId; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}