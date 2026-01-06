package com.perpus.app.controllers;

import com.perpus.app.dao.KategoriDAO;
import com.perpus.app.models.Buku;
import com.perpus.app.models.Kategori;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class BukuFormController {

    @FXML private Label headerLabel;
    @FXML private TextField txtJudul;
    @FXML private TextField txtPenerbit;
    @FXML private TextField txtTahun;
    @FXML private TextField txtStok;
    @FXML private ComboBox<Kategori> comboKategori;

    private Buku buku;
    private boolean isSaveClicked = false;
    private final KategoriDAO kategoriDAO = new KategoriDAO();

    /**
     * Inisialisasi otomatis saat FXML dimuat.
     * Mengambil daftar kategori dari database untuk ComboBox.
     */
    @FXML
    public void initialize() {
        try {
            ObservableList<Kategori> listKategori = FXCollections.observableArrayList(kategoriDAO.getAll());
            comboKategori.setItems(listKategori);
            
            // Memberikan prompt agar user tahu harus memilih kategori
            comboKategori.setPromptText("-- Pilih Kategori --");
        } catch (Exception e) {
            System.err.println("Gagal memuat kategori: " + e.getMessage());
        }
    }

    /**
     * Menyiapkan data buku jika dalam mode EDIT.
     * Jika buku != null, field akan terisi otomatis.
     */
    public void setBuku(Buku buku) {
        this.buku = buku;
        if (buku != null) {
            headerLabel.setText("Edit Data Buku");
            txtJudul.setText(buku.getJudul());
            txtPenerbit.setText(buku.getPenerbit());
            txtTahun.setText(String.valueOf(buku.getTahunTerbit()));
            txtStok.setText(String.valueOf(buku.getStok()));

            // Sinkronisasi ComboBox dengan kategori buku yang sedang diedit
            for (Kategori k : comboKategori.getItems()) {
                if (k.getKategoriId() == buku.getKategoriId()) {
                    comboKategori.setValue(k);
                    break;
                }
            }
        }
    }

    /**
     * Menangani aksi tombol Simpan.
     */
    @FXML
private void handleSave() {
    if (isInputValid()) {
        // AMBIL ID KATEGORI YANG VALID DARI COMBOBOX
        int selectedKategoriId = comboKategori.getValue().getKategoriId();

        if (buku == null) {
            // GUNAKAN CONSTRUCTOR INSERT (5 PARAMETER): 
            // 1. kategoriID, 2. judul, 3. penerbit, 4. tahun, 5. stok
            buku = new Buku(
                selectedKategoriId, // Jangan gunakan angka 0
                txtJudul.getText(),
                txtPenerbit.getText(),
                Integer.parseInt(txtTahun.getText()),
                Integer.parseInt(txtStok.getText())
            );
        } else {
            // Mode Edit
            buku.setKategoriId(selectedKategoriId);
            buku.setJudul(txtJudul.getText());
            buku.setPenerbit(txtPenerbit.getText());
            buku.setTahunTerbit(Integer.parseInt(txtTahun.getText()));
            buku.setStok(Integer.parseInt(txtStok.getText()));
        }

        isSaveClicked = true;
        closeWindow();
    }
}

    /**
     * Menangani aksi tombol Batal.
     */
    @FXML
    private void handleCancel() {
        closeWindow();
    }

    /**
     * Validasi input sederhana sebelum menyimpan ke database.
     */
    private boolean isInputValid() {
        String errorMessage = "";

        if (txtJudul.getText() == null || txtJudul.getText().isEmpty()) {
            errorMessage += "Judul tidak boleh kosong!\n";
        }
        if (comboKategori.getValue() == null) {
            errorMessage += "Kategori harus dipilih!\n";
        }
        
        try {
            Integer.parseInt(txtTahun.getText());
            Integer.parseInt(txtStok.getText());
        } catch (NumberFormatException e) {
            errorMessage += "Tahun dan Stok harus berupa angka!\n";
        }

        if (errorMessage.isEmpty()) {
            return true;
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Input Tidak Valid");
            alert.setHeaderText("Silakan perbaiki data berikut:");
            alert.setContentText(errorMessage);
            alert.showAndWait();
            return false;
        }
    }

    private void closeWindow() {
        Stage stage = (Stage) txtJudul.getScene().getWindow();
        stage.close();
    }

    public Buku getBuku() { return buku; }
    public boolean isSaveClicked() { return isSaveClicked; }
}