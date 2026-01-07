package com.perpus.app.controllers;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.perpus.app.dao.AnggotaDAO;
import com.perpus.app.models.Anggota;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class AnggotaController {

    @FXML private TableView<Anggota> anggotaTable;
    @FXML private TableColumn<Anggota, Integer> colId;
    @FXML private TableColumn<Anggota, String> colNama;
    @FXML private TableColumn<Anggota, String> colEmail; 
    @FXML private TableColumn<Anggota, String> colTelepon;
    @FXML private TextField searchField;

    private final AnggotaDAO anggotaDAO = new AnggotaDAO();
    private ObservableList<Anggota> masterData = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNama.setCellValueFactory(new PropertyValueFactory<>("nama"));

        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colTelepon.setCellValueFactory(new PropertyValueFactory<>("no_telp"));

        refreshTable();
    }

    private void refreshTable() {
        masterData.setAll(anggotaDAO.getAll());
        anggotaTable.setItems(masterData);
    }

    @FXML
    private void handleSearch() {
        String query = searchField.getText();
        if (query == null || query.isEmpty()) {
            refreshTable();
        } else {
            List<Anggota> hasil = anggotaDAO.getAll().stream()
                .filter(a -> a.getNama().toLowerCase().contains(query.toLowerCase()))
                .collect(Collectors.toList()); 
            anggotaTable.setItems(FXCollections.observableArrayList(hasil));
        }
    }

    @FXML
    private void handleDelete() {
        Anggota selected = anggotaTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Peringatan", "Pilih anggota yang akan dihapus!");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Konfirmasi Hapus");
        alert.setHeaderText(null);
        alert.setContentText("Hapus anggota " + selected.getNama() + "?");
        
        Optional<ButtonType> res = alert.showAndWait();
        if (res.isPresent() && res.get() == ButtonType.OK) {
            if (anggotaDAO.delete(selected.getId())) {
                refreshTable();
                showAlert("Sukses", "Data anggota berhasil dihapus.");
            } else {
                showAlert("Error", "Gagal menghapus anggota. Pastikan anggota tidak memiliki pinjaman aktif.");
            }
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML private void showAddDialog() { }
    @FXML private void handleEdit() { }
}