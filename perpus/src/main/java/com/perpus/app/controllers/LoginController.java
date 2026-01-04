package com.perpus.app.controllers;

import com.perpus.app.dao.UserDAO;
import com.perpus.app.models.User;

public class LoginController {
    private final UserDAO userDAO;
    private User userLogin; // Menyimpan sesi user yang sedang login

    public LoginController() {
        this.userDAO = new UserDAO();
    }

    public String autentikasi(String username, String password) {
        // Memanggil method login dari UserDAO yang sudah kita perbaiki
        userLogin = userDAO.login(username, password);

        if (userLogin != null) {
            // Mengembalikan role untuk menentukan navigasi di View nanti
            return userLogin.getRole(); 
        }
        return null; // Login gagal
    }

    // Method untuk mengambil data user yang sedang login
    public User getUserSession() {
        return userLogin;
    }
}