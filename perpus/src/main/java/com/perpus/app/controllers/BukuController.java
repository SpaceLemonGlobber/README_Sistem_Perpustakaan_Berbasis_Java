package com.perpus.app.controllers;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.perpus.app.dao.BukuDAO;
import com.perpus.app.models.Buku;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView; // Tambahkan import ini
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class BukuController {

    @FXML private TableView<Buku> bukuTable;
    @FXML private TableColumn<Buku, Integer> colId;
    @FXML private TableColumn<Buku, String> colJudul;
    @FXML private TableColumn<Buku, String> colPenerbit;
    @FXML private TableColumn<Buku, Integer> colTahun;
    @FXML private TableColumn<Buku, Integer> colStok;
    @FXML private TextField searchField;

    private final BukuDAO bukuDAO;
    private ObservableList<Buku> masterData = FXCollections.observableArrayList();

    public BukuController() {
        this.bukuDAO = new BukuDAO();
    }

    @FXML
    public void initialize() {
        // "id" memanggil getId() yang sudah kita buat sebagai alias di model Buku
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colJudul.setCellValueFactory(new PropertyValueFactory<>("judul"));
        colPenerbit.setCellValueFactory(new PropertyValueFactory<>("penerbit"));
        
        // PERBAIKAN: Harus sesuai dengan nama getter di model Buku (getTahunTerbit)
        colTahun.setCellValueFactory(new PropertyValueFactory<>("tahunTerbit"));
        colStok.setCellValueFactory(new PropertyValueFactory<>("stok"));

        refreshTable();
    }

    private void refreshTable() {
        masterData.setAll(bukuDAO.getAll());
        bukuTable.setItems(masterData);
    }

    @FXML
    private void handleSearch() {
        String query = searchField.getText();
        if (query == null || query.isEmpty()) {
            refreshTable();
        } else {
            // PERBAIKAN: Gunakan .collect(Collectors.toList()) agar kompatibel dengan Java 8/11
            List<Buku> hasilCari = bukuDAO.getAll().stream()
                .filter(b -> b.getJudul().toLowerCase().contains(query.toLowerCase()))
                .collect(Collectors.toList());
            bukuTable.setItems(FXCollections.observableArrayList(hasilCari));
        }
    }

    @FXML
    private void showAddDialog() {
        showForm(null);
    }

    @FXML
    private void handleEdit() {
        Buku selected = bukuTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            showForm(selected);
        } else {
            showAlert("Peringatan", "Pilih buku yang ingin diedit!");
        }
    }

    @FXML
    private void handleDelete() {
        Buku selected = bukuTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Peringatan", "Pilih buku yang ingin dihapus!");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Konfirmasi Hapus");
        confirm.setHeaderText(null);
        confirm.setContentText("Hapus buku: " + selected.getJudul() + "?");
        
        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // Memanggil delete(int id) yang ada di BukuDAO
            if (bukuDAO.delete(selected.getId())) {
                refreshTable();
            } else {
                showAlert("Error", "Gagal menghapus buku. Buku mungkin masih terkait dengan transaksi.");
            }
        }
    }

    private void showForm(Buku buku) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/perpus/BukuForm.fxml"));
            Parent root = loader.load();
            
            BukuFormController controller = loader.getController();
            controller.setBuku(buku);

            Stage stage = new Stage();
            stage.setTitle(buku == null ? "Tambah Buku" : "Edit Buku");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.showAndWait();

            if (controller.isSaveClicked()) {
                Buku b = controller.getBuku();
                if (b.getId() == 0) {
                    bukuDAO.insert(b);
                } else {
                    bukuDAO.update(b);
                }
                refreshTable();
            }
        } catch (IOException e) { 
            e.printStackTrace();
            showAlert("Error", "Gagal memuat form buku.");
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}