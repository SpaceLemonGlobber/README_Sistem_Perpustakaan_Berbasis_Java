package com.perpus.app.controllers;

import java.time.LocalDate;
import java.util.Optional;
import java.util.stream.Collectors;

import com.perpus.app.dao.BukuDAO;
import com.perpus.app.dao.PeminjamanDAO;
import com.perpus.app.models.Buku;
import com.perpus.app.models.Peminjaman;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class UserDashboardController {
    // Tabel Search & Borrow
    @FXML private TextField searchField;
    @FXML private TableView<Buku> tableSearch;
    @FXML private TableColumn<Buku, String> colJudul;
    @FXML private TableColumn<Buku, Integer> colStok;
    @FXML private TableColumn<Buku, String> colKategori;
    @FXML private TableColumn<Buku, String> colPenerbit;
    @FXML private TableColumn<Buku, Integer> colTahunTerbit;


    // Tabel Active & Return
    @FXML private TableView<Peminjaman> tableActive;
    @FXML private TableColumn<Peminjaman, Integer> colActiveId;
    @FXML private TableColumn<Peminjaman, String> colActiveJudul;

    // Tabel History
    @FXML private TableView<Peminjaman> tableHistory;
    @FXML private TableColumn<Peminjaman, Integer> colHistId;
    @FXML private TableColumn<Peminjaman, String> colHistStatus;

    private final BukuDAO bukuDAO = new BukuDAO();
    private final PeminjamanDAO peminjamanDAO = new PeminjamanDAO();

    @FXML
    public void initialize() {
        setupColumns();
        refreshAllData();
        loadAvailableBooks();
    }

    private void setupColumns() {
        colJudul.setCellValueFactory(new PropertyValueFactory<>("judul"));
        colStok.setCellValueFactory(new PropertyValueFactory<>("stok"));
        colKategori.setCellValueFactory(new PropertyValueFactory<>("namaKategori")); 
        colPenerbit.setCellValueFactory(new PropertyValueFactory<>("penerbit"));
        colTahunTerbit.setCellValueFactory(new PropertyValueFactory<>("tahunTerbit"));
        colActiveId.setCellValueFactory(new PropertyValueFactory<>("peminjamanId"));
        colActiveJudul.setCellValueFactory(new PropertyValueFactory<>("judulBuku"));
        colHistId.setCellValueFactory(new PropertyValueFactory<>("peminjamanId"));
        colHistStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
    }

    private void loadAvailableBooks() {
        // Mengambil data dari DAO dan memasukkannya ke tabel
        tableSearch.setItems(FXCollections.observableArrayList(bukuDAO.getAll()));
    }

    private void refreshAllData() {
        int userId = LoginController.getUserSession().getId();
        tableSearch.setItems(FXCollections.observableArrayList(bukuDAO.getAll()));
        tableActive.setItems(FXCollections.observableArrayList(peminjamanDAO.getByAnggotaAndStatus(userId, "BORROWED")));
        tableHistory.setItems(FXCollections.observableArrayList(peminjamanDAO.getByAnggota(userId)));
    }

    @FXML
    private void handleBorrow() {
        Buku selected = tableSearch.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        // Decision: Borrow Book?
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Pinjam buku ini?");
        Optional<ButtonType> result = confirm.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            Peminjaman p = new Peminjaman();
            p.setAnggotaId(LoginController.getUserSession().getId());
            p.setBukuId(selected.getId());
            p.setTanggalPeminjaman(LocalDate.now()); // Simpan tgl pinjam
            p.setTanggalPengembalian(LocalDate.now().plusDays(7)); // Simpan tgl kembali
            p.setStatus("BORROWED"); // Simpan status
            p.setDenda(0.0); // Simpan denda awal

            if (peminjamanDAO.save(p)) {
                refreshAllData();
            }
        }
    }

    @FXML
    private void handleReturn() {
        Peminjaman selected = tableActive.getSelectionModel().getSelectedItem();
        if (selected != null) {
            // Remove from borrow list / Update status
            peminjamanDAO.updateStatus(selected.getPeminjamanId(), "RETURNED");
            refreshAllData();
        }
    }

    @FXML
    private void handleSearch() {
        // Logika pencarian buku
        String query = searchField.getText().toLowerCase();
        tableSearch.setItems(FXCollections.observableArrayList(
            bukuDAO.getAll().stream()
                .filter(b -> b.getJudul().toLowerCase().contains(query) || 
                             b.getNamaKategori().toLowerCase().contains(query))
                .collect(Collectors.toList())
        ));
    }

    // Tambahkan ini ke dalam UserDashboardController.java
    @FXML
    private void handleLogout(ActionEvent event) {
    try {
        // Arahkan kembali ke halaman Login
        javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/com/perpus/LoginView.fxml"));
        javafx.scene.Parent root = loader.load();
        
        javafx.stage.Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.setScene(new javafx.scene.Scene(root));
        stage.setTitle("Login - Perpustakaan");
        stage.show();
    } catch (java.io.IOException e) {
        e.printStackTrace();
    }
}
}