package com.perpus.app.controllers;

import java.io.IOException;

import com.perpus.app.dao.UserDAO;
import com.perpus.app.models.User;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label statusLabel;

    private final UserDAO userDAO = new UserDAO();
    private static User userSession; // Menyimpan data user yang sedang login

    // Method Penting: Harus ada agar DashboardController tidak error
    public static User getUserSession() {
        return userSession;
    }

    @FXML
    private void handleLogin(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();
        userSession = userDAO.login(username, password);

        if (userSession != null) {
            try {
                // Tentukan alur: ADMIN ke MainView, USER ke UserDashboard
                String fxml = userSession.getRole().equalsIgnoreCase("ADMIN") ? "MainView.fxml" : "UserDashboard.fxml";
                
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/perpus/" + fxml));
                Parent root = loader.load();
                
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.centerOnScreen();
                stage.show();
            } catch (IOException e) {
                statusLabel.setText("Gagal memuat halaman: " + e.getMessage());
            }
        } else {
            statusLabel.setText("Username atau Password salah!");
        }
    }
}