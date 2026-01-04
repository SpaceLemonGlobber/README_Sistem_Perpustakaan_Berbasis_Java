package com.perpus.app.models;

public abstract class User {

    protected int userId;
    protected String username;
    protected String password;
    protected String nama;

    public User(int userId, String username, String password, String nama) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.nama = nama;
    }

    public int getUserId() { return userId; }
    public String getUsername() { return username; }
    public String getNama() { return nama; }

    // polymorphism point
    public abstract String getRole();
}
