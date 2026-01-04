package com.perpus.app.models;

public class Admin extends User {

    public Admin(int userId, String username, String password, String nama) {
        super(userId, username, password, nama);
    }

    @Override
    public String getRole() {
        return "ADMIN";
    }
}
