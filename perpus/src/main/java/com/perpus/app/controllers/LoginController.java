package com.perpus.app.controllers;

public class LoginController {

    private final UserDAO userDAO;

    public LoginController() {
        this.userDAO = new UserDAO();
    }

    /* ===============================
       LOGIN
       =============================== */
    public User login(String username, String password) {

        if (username == null || password == null ||
            username.isBlank() || password.isBlank()) {
            return null;
        }

        return userDAO.login(username, password);
    }
}

