package com.perpus.app.models;

public class Admin extends User {
    private int adminId;
    private String email;

    public Admin() {
        super();
    }

    public Admin(int userId, String username, String password, String nama) {
        super(userId, username, password, nama);
    }

    public Admin(int userId, String username, String password, String nama, int adminId, String email) {
        super(userId, username, password, nama);
        this.adminId = adminId;
        this.email = email;
    }

    public Admin(int id, String username) {
        super(id, username, null, null); 
    }

    @Override
    public String getRole() {
        return "ADMIN";
    }

    public int getAdminId() { return adminId; }
    public void setAdminId(int adminId) { this.adminId = adminId; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}