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
            // Memuat LoginView sebagai halaman pertama aplikasi
            Parent root = loadFXML("LoginView"); 
            scene = new Scene(root, 800, 500); // Ukuran default yang pas untuk login
            
            stage.setTitle("Sistem Manajemen Perpustakaan - Login");
            stage.setScene(scene);
            stage.setResizable(false); // Opsional: Agar jendela login tidak bisa di-resize
            stage.show();
        } catch (IOException e) {
            System.err.println("🚨 Gagal memuat file FXML Utama: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Method statis untuk berpindah halaman secara global
    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        // Penyesuaian path agar mencari file .fxml di folder resources/com/perpus/
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/com/perpus/" + fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        // --- CEK KONEKSI DATABASE SAAT STARTUP ---
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

        // Menjalankan UI JavaFX
        launch();
    }
}