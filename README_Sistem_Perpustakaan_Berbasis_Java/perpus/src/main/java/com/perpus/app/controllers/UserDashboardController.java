package com.perpus.app.controllers;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.perpus.app.dao.AnggotaDAO;
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
    @FXML private TableColumn<Peminjaman, LocalDate> colActiveJatuhTempo; 
    @FXML private TableColumn<Peminjaman, String> colActiveStatus;     

    // Tabel History
    @FXML private TableView<Peminjaman> tableHistory;
    @FXML private TableColumn<Peminjaman, Integer> colHistId;
    @FXML private TableColumn<Peminjaman, String> colHistStatus;

    private final BukuDAO bukuDAO = new BukuDAO();
    private final PeminjamanDAO peminjamanDAO = new PeminjamanDAO();
    private final AnggotaDAO anggotaDAO = new AnggotaDAO();

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
        colActiveJatuhTempo.setCellValueFactory(new PropertyValueFactory<>("tanggalPengembalian"));
        colActiveStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colHistId.setCellValueFactory(new PropertyValueFactory<>("peminjamanId"));
        colHistStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
    }

    private void loadAvailableBooks() {
        // Mengambil data dari DAO dan memasukkannya ke tabel
        tableSearch.setItems(FXCollections.observableArrayList(bukuDAO.getAll()));
    }

    private void refreshAllData() {
        int userId = LoginController.getUserSession().getId();
        Integer anggotaId = anggotaDAO.getAnggotaIdByUserId(userId);

        if (anggotaId == null) {
            tableActive.setItems(FXCollections.observableArrayList());
            tableHistory.setItems(FXCollections.observableArrayList());
            System.out.println("USER TIDAK TERDAFTAR SEBAGAI ANGGOTA");
            return;
        }

        tableSearch.setItems(
            FXCollections.observableArrayList(bukuDAO.getAll())
        );

        List<Peminjaman> activeList =
            peminjamanDAO.getByAnggotaAndStatus(anggotaId, "Dipinjam");

        tableActive.setItems(
            FXCollections.observableArrayList(activeList)
        );

        tableHistory.setItems(
            FXCollections.observableArrayList(
                peminjamanDAO.getByAnggota(anggotaId)
            )
        );

        System.out.println("DATA PINJAM = " + activeList.size());
    }


    @FXML
    private void handleBorrow() {
        Buku selected = tableSearch.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        // Decision: Borrow Book?
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Pinjam buku ini?");
        Optional<ButtonType> result = confirm.showAndWait();

        if (selected.getStok() <= 0) {
        new Alert(Alert.AlertType.WARNING,
            "Stok buku habis!"
        ).show();
        return;
        }        
        if (result.isPresent() && result.get() == ButtonType.OK) {
            Peminjaman p = new Peminjaman();
            int userId = LoginController.getUserSession().getId();
            Integer anggotaId = anggotaDAO.getAnggotaIdByUserId(userId);

            if (anggotaId == null) {
                new Alert(Alert.AlertType.ERROR,
                    "Akun ini belum terdaftar sebagai anggota."
                ).show();
                return;
            }

            p.setAnggotaId(anggotaId);
            p.setBukuId(selected.getId());
            
            // LOGIKA: Tanggal Pinjam hari ini, Jatuh Tempo 14 hari (2 minggu)
            LocalDate hariIni = LocalDate.now();
            p.setTanggalPeminjaman(hariIni); 
            p.setTanggalPengembalian(hariIni.plusWeeks(2)); // Set 2 minggu ke depan
            
            p.setStatus("Dipinjam");
            p.setDenda(0.0);

            if (peminjamanDAO.save(p)) {
                boolean stokUpdated = bukuDAO.updateStok(selected.getId(), -1);

                if (!stokUpdated) {
                    new Alert(Alert.AlertType.ERROR,
                        "Stok buku habis!"
                    ).show();
                    return;
                }

                // 2️⃣ Insert detail peminjaman (jika ada tabel detail)
                peminjamanDAO.insertDetail(
                    p.getPeminjamanId(),
                    selected.getId(),
                    1
                );

                refreshAllData();
            }
        }
    }

    @FXML
    private void handleReturn() {
        Peminjaman selected = tableActive.getSelectionModel().getSelectedItem();

        if (selected == null) {
            new Alert(Alert.AlertType.WARNING,
                "Pilih buku yang ingin dikembalikan!"
            ).show();
            return;
        }

        Alert confirm = new Alert(
            Alert.AlertType.CONFIRMATION
        );
        confirm.setTitle("Konfirmasi Pengembalian");
        confirm.setHeaderText("Kembalikan Buku");
        confirm.setContentText(
            "Apakah Anda yakin ingin mengembalikan buku:\n\n" +
            selected.getJudulBuku() +
            "\n\nJatuh tempo: " + selected.getTanggalPengembalian()
        );

        Optional<ButtonType> result = confirm.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {

            boolean statusUpdated = peminjamanDAO.updateStatus(
                selected.getPeminjamanId(),
                "Dikembalikan"
            );

            if (statusUpdated) {
                bukuDAO.updateStok(selected.getBukuId(), +1);
                refreshAllData();

                new Alert(Alert.AlertType.INFORMATION,
                    "Buku berhasil dikembalikan."
                ).show();
            }
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