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

    @FXML private TableView<Kategori> tableKategori; 

    @FXML private TableColumn<Kategori, Integer> colKategoriId;
    @FXML private TableColumn<Kategori, String> colNamaKategori;
    @FXML private TableColumn<Kategori, String> colDeskripsi;

    private KategoriDAO kategoriDAO = new KategoriDAO();

    @FXML
    public void initialize() {
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

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == simpanButtonType) {
                return new Kategori(namaField.getText(), deskripsiArea.getText());
            }
            return null;
        });

        Optional<Kategori> result = dialog.showAndWait();
        result.ifPresent(kategori -> {
            if (kategoriDAO.save(kategori)) {
                loadData();
                System.out.println("Data berhasil disimpan!");
            } else {
                System.out.println("Gagal menyimpan data.");
            }
        });
    }
}