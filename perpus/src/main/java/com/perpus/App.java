package com.perpus;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
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
        // Tambahkan tes koneksi sebelum launch()
    try {
        System.out.println("Mencoba menghubungkan ke database...");
        java.sql.Connection conn = com.perpus.config.Database.getConnection();
        if (conn != null) {
            System.out.println("Koneksi Database BERHASIL! (Port 3307)");
            conn.close(); // Tutup setelah tes berhasil
        }
    } catch (Exception e) {
        System.err.println("Koneksi Database GAGAL!");
        System.err.println("Pesan Error: " + e.getMessage());
        // Kamu bisa memilih untuk lanjut launch() atau stop di sini
    }
        launch();
    }

}