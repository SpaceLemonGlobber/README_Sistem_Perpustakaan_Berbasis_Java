package com.perpus.app.controllers;

import com.perpus.app.dao.BukuDAO;
import com.perpus.app.dao.PeminjamanDAO;
import com.perpus.app.models.Buku;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.IOException;

public class DashboardController {

    @FXML private VBox mainContent;
    @FXML private Label adminLabel;
    @FXML private Label totalBukuLabel;
    @FXML private Label stokBukuLabel;
    @FXML private Label peminjamanAktifLabel;

    private final BukuDAO bukuDAO;
    private final PeminjamanDAO peminjamanDAO;

    public DashboardController() {
        this.bukuDAO = new BukuDAO();
        this.peminjamanDAO = new PeminjamanDAO();
    }

    @FXML
    public void initialize() {
        if (LoginController.getUserSession() != null) {
            adminLabel.setText("Halo, " + LoginController.getUserSession().getNama() + "!");
        }
        refreshDashboardStats();
    }

    private void refreshDashboardStats() {
        int totalJudul = bukuDAO.getAll().size();
        int totalStok = bukuDAO.getAll().stream().mapToInt(Buku::getStok).sum();
        int pinjamAktif = peminjamanDAO.getByStatus("DIPINJAM").size();

        if (totalBukuLabel != null) totalBukuLabel.setText(String.valueOf(totalJudul));
        if (stokBukuLabel != null) stokBukuLabel.setText(String.valueOf(totalStok));
        if (peminjamanAktifLabel != null) peminjamanAktifLabel.setText(String.valueOf(pinjamAktif));
    }

    /* ===============================
       NAVIGASI MENU
       =============================== */

    @FXML
    private void showDashboardBuku(ActionEvent event) {
        loadPage("BukuView.fxml");
    }

    @FXML
    private void showKategoriBuku(ActionEvent event) {
        loadPage("KategoriView.fxml");
    }

    @FXML
    private void showTransaksi(ActionEvent event) {
        loadPage("PeminjamanView.fxml");
    }

    @FXML
    private void showLaporan(ActionEvent event) {
        loadPage("LaporanView.fxml");
    }

    @FXML
    private void showAnggota(ActionEvent event) {
        loadPage("AnggotaView.fxml");
    }

    @FXML
    private void handleLogout(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/perpus/LoginView.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Admin Login");
            stage.centerOnScreen();
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadPage(String fxml) {
        try {
            // Menggunakan VBox atau Parent tergantung root element di file FXML tujuan
            Parent page = FXMLLoader.load(getClass().getResource("/com/perpus/" + fxml));
            mainContent.getChildren().setAll(page);
        } catch (IOException e) {
            System.err.println("Gagal memuat halaman: " + fxml);
            e.printStackTrace();
        }
    }
}