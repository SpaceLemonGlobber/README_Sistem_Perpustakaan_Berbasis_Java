package com.perpus.app.models;

public class Anggota extends User {

    private String noAnggota;

    public Anggota(int userId, String username, String password,
                   String nama, String noAnggota) {
        super(userId, username, password, nama);
        this.noAnggota = noAnggota;
    }

    public String getNoAnggota() {
        return noAnggota;
    }

    @Override
    public String getRole() {
        return "ANGGOTA";
    }
}
