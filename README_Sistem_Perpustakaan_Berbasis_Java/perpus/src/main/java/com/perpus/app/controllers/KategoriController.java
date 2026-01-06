package com.perpus.app.controllers;

import java.util.List;
import java.util.Optional;

import com.perpus.app.dao.KategoriDAO;
import com.perpus.app.models.Kategori;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;

public class KategoriController {

    // Sesuaikan fx:id TableView kamu di Scene Builder (contoh: tableKategori)
    @FXML private TableView<Kategori> tableKategori; 
    
    // fx:id kolom yang sudah kamu buat
    @FXML private TableColumn<Kategori, Integer> colKategoriId;
    @FXML private TableColumn<Kategori, String> colNamaKategori;
    @FXML private TableColumn<Kategori, String> colDeskripsi;

    private KategoriDAO kategoriDAO = new KategoriDAO();

    @FXML
    public void initialize() {
        // Mapping kolom ke property di Kategori.java
        // Note: Nama di dalam petik harus sesuai dengan nama variabel di model/getter
        colKategoriId.setCellValueFactory(new PropertyValueFactory<>("kategoriId"));
        colNamaKategori.setCellValueFactory(new PropertyValueFactory<>("nama_kategori"));
        colDeskripsi.setCellValueFactory(new PropertyValueFactory<>("deskripsi"));

        loadData();
    }

    public void loadData() {
        List<Kategori> list = kategoriDAO.getAll();
        ObservableList<Kategori> data = FXCollections.observableArrayList(list);
        tableKategori.setItems(data);
    }

    @FXML
    private void handleTambahKategori() {
        // Membuat Dialog Pop-up Sederhana
        Dialog<Kategori> dialog = new Dialog<>();
        dialog.setTitle("Tambah Kategori");
        dialog.setHeaderText("Masukkan Data Kategori Baru");

        ButtonType simpanButtonType = new ButtonType("Simpan", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(simpanButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        TextField namaField = new TextField();
        namaField.setPromptText("Nama Kategori");
        TextArea deskripsiArea = new TextArea();
        deskripsiArea.setPromptText("Deskripsi");
        deskripsiArea.setPrefRowCount(3);

        grid.add(new Label("Nama:"), 0, 0);
        grid.add(namaField, 1, 0);
        grid.add(new Label("Deskripsi:"), 0, 1);
        grid.add(deskripsiArea, 1, 1);

        dialog.getDialogPane().setContent(grid);

        // Mengambil hasil input saat tombol simpan ditekan
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == simpanButtonType) {
                return new Kategori(namaField.getText(), deskripsiArea.getText());
            }
            return null;
        });

        Optional<Kategori> result = dialog.showAndWait();
        result.ifPresent(kategori -> {
            if (kategoriDAO.save(kategori)) {
                loadData(); // Refresh tabel setelah simpan berhasil
                System.out.println("Data berhasil disimpan!");
            } else {
                System.out.println("Gagal menyimpan data.");
            }
        });
    }
}