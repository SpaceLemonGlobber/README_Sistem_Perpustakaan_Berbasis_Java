package com.perpus;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) {
        try {
            Parent root = loadFXML("LoginView"); 
            scene = new Scene(root, 800, 500); 
            
            stage.setTitle("Sistem Manajemen Perpustakaan - Login");
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            System.err.println("🚨 Gagal memuat file FXML Utama: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/com/perpus/" + fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        try {
            System.out.println("=== SISTEM PERPUSTAKAAN: MEMULAI KONEKSI ===");
            java.sql.Connection conn = com.perpus.config.Database.getConnection();
            if (conn != null) {
                System.out.println("✅ Database Terkoneksi.");
                conn.close();
            }
        } catch (Exception e) {
            System.err.println("🚨 Peringatan: Gagal koneksi database di awal. Periksa port 3307.");
        }

        launch();
    }
}