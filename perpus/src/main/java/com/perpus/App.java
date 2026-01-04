package com.perpus;

import java.io.IOException;

import com.perpus.app.controllers.DashboardController;
import com.perpus.app.controllers.LoginController;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        // Ini akan memuat tampilan primary.fxml
        scene = new Scene(loadFXML("primary"), 640, 480);
        stage.setScene(scene);
        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        // --- AWAL TEST DRIVE LOGIKA ---
        try {
            System.out.println("=== MEMULAI SISTEM PERPUSTAKAAN ===");
            
            // 1. Tes Koneksi
            java.sql.Connection conn = com.perpus.config.Database.getConnection();
            if (conn != null) {
                System.out.println("✅ Database Terkoneksi (Port 3307)");
                
                // 2. Simulasi Login (Ganti 'admin' dengan data di DB-mu)
                LoginController loginCtrl = new LoginController();
                String role = loginCtrl.autentikasi("admin", "admin");
                
                if (role != null) {
                    System.out.println("✅ Login Berhasil sebagai: " + role);
                    
                    // 3. Cek Data Dashboard
                    DashboardController dashCtrl = new DashboardController();
                    System.out.println("--- Statistik Database ---");
                    System.out.println("Total Judul Buku: " + dashCtrl.getTotalJudulBuku());
                    System.out.println("Peminjaman Aktif: " + dashCtrl.getTotalPeminjamanAktif());
                } else {
                    System.out.println("❌ Akun simulasi tidak ditemukan di database.");
                }
                
                conn.close();
            }
        } catch (Exception e) {
            System.err.println("🚨 Terjadi Masalah: " + e.getMessage());
        }
        System.out.println("=== MEMULAI ANTARMUKA GRAFIS (UI) ===\n");
        // --- AKHIR TEST DRIVE ---

        launch();
    }
}