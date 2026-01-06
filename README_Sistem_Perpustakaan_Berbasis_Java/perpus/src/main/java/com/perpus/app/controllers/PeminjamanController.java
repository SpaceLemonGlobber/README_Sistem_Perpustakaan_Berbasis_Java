package com.perpus.app.controllers;

import com.perpus.app.dao.BukuDAO;
import com.perpus.app.dao.PeminjamanDAO;
import com.perpus.app.models.Buku;
import com.perpus.app.models.Peminjaman;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.time.LocalDate;

public class PeminjamanController {

    @FXML private TextField txtIdAnggota, txtIdBuku;
    @FXML private TableView<Peminjaman> tablePeminjaman;
    @FXML private TableColumn<Peminjaman, Integer> colId;
    @FXML private TableColumn<Peminjaman, String> colNamaAnggota, colJudulBuku, colStatus;
    @FXML private TableColumn<Peminjaman, LocalDate> colTglPinjam, colTglKembali;

    private final PeminjamanDAO peminjamanDAO = new PeminjamanDAO();
    private final BukuDAO bukuDAO = new BukuDAO();

    @FXML
    public void initialize() {
        // Gunakan getId() karena sudah kita buat aliasnya di model
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNamaAnggota.setCellValueFactory(new PropertyValueFactory<>("namaAnggota"));
        colJudulBuku.setCellValueFactory(new PropertyValueFactory<>("judulBuku"));
        colTglPinjam.setCellValueFactory(new PropertyValueFactory<>("tanggalPinjam"));
        colTglKembali.setCellValueFactory(new PropertyValueFactory<>("tanggalKembali"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        refreshTable();
    }

    private void refreshTable() {
        // Memastikan memanggil getByStatus atau getAktif sesuai yang ada di DAO
        tablePeminjaman.setItems(FXCollections.observableArrayList(peminjamanDAO.getByStatus("DIPINJAM")));
    }

    @FXML
    private void handleProsesPinjam() {
        try {
            int idAnggota = Integer.parseInt(txtIdAnggota.getText());
            int idBuku = Integer.parseInt(txtIdBuku.getText());

            Buku buku = bukuDAO.getById(idBuku);
            if (buku == null || buku.getStok() <= 0) {
                showAlert("Gagal", "Buku tidak ditemukan atau stok habis!");
                return;
            }

            Peminjaman p = new Peminjaman();
            p.setAnggotaId(idAnggota);
            p.setBukuId(idBuku);
            p.setTanggalPeminjaman(LocalDate.now());
            p.setTanggalPengembalian(LocalDate.now().plusDays(7));
            p.setStatus("DIPINJAM");
            p.setDenda(0.0);
            
            // Mengambil ID Admin dari sesi login jika tersedia
            if (LoginController.getUserSession() != null) {
                p.setAdminId(LoginController.getUserSession().getId());
            }

            // Gunakan insert(p) sesuai signature di DAO terbaru
            if (peminjamanDAO.insert(p)) {
                // Update stok
                buku.setStok(buku.getStok() - 1);
                bukuDAO.update(buku);
                
                refreshTable();
                txtIdAnggota.clear();
                txtIdBuku.clear();
                showAlert("Berhasil", "Peminjaman berhasil dicatat!");
            }
        } catch (NumberFormatException e) {
            showAlert("Error", "ID Anggota dan ID Buku harus berupa angka!");
        }
    }

    @FXML
    private void handleProsesKembali() {
        Peminjaman selected = tablePeminjaman.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Peringatan", "Pilih data peminjaman di tabel!");
            return;
        }

        // Gunakan getId() alias dari peminjamanId
        if (peminjamanDAO.updateStatus(selected.getId(), "KEMBALI")) {
            Buku buku = bukuDAO.getById(selected.getBukuId());
            if (buku != null) {
                buku.setStok(buku.getStok() + 1);
                bukuDAO.update(buku);
            }
            
            refreshTable();
            showAlert("Berhasil", "Buku telah dikembalikan!");
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}