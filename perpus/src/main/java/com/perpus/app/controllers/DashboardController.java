package com.perpus.app.controllers;

import java.io.IOException;
import java.util.stream.Collectors;

import com.perpus.app.dao.AnggotaDAO;
import com.perpus.app.dao.BukuDAO;
import com.perpus.app.dao.KategoriDAO;
import com.perpus.app.dao.PeminjamanDAO;
import com.perpus.app.models.Anggota;
import com.perpus.app.models.Buku;
import com.perpus.app.models.Kategori;
import com.perpus.app.models.Peminjaman;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class DashboardController {

    @FXML private Label adminLabel;
    
    // --- Table Buku ---
    @FXML private TableView<Buku> tableBuku;
    @FXML private TableColumn<Buku, Integer> colBukuId;
    @FXML private TableColumn<Buku, String> colJudul, colPenerbit, colKategori;
    @FXML private TableColumn<Buku, Integer> colStok;
    @FXML private TableColumn<Buku, Void> colAksi;
    @FXML private TextField searchBukuField;

    // --- Table Anggota ---
    @FXML private TableView<Anggota> tableAnggota;
    @FXML private TableColumn<Anggota, Integer> colAnggotaId;
    @FXML private TableColumn<Anggota, String> colNama, colEmail, colNoTelp;
    @FXML private TableColumn<Anggota, Void> colAksiAnggota;

    // --- Table Transaksi & Kategori ---
    @FXML private TableView<Peminjaman> tableTransaksi;
    @FXML private TableColumn<Peminjaman, Integer> colTransId;
    @FXML private TableColumn<Peminjaman, String> colTransUser, colTransBuku, colTransStatus;

    @FXML private TableView<Kategori> tableKategori;
    @FXML private TableColumn<Kategori, Integer> colKategoriId;
    @FXML private TableColumn<Kategori, String> colNamaKategori, colDeskripsi;
    @FXML private TableColumn<Kategori, String> colNamaKategori;
    @FXML private TableColumn<Kategori, String> colDeskripsi;
    @FXML private TableColumn<Kategori, Void> colAksiKategori;

    // --- DAO Instances ---
    private final BukuDAO bukuDAO = new BukuDAO();
    private final AnggotaDAO anggotaDAO = new AnggotaDAO();
    private final PeminjamanDAO peminjamanDAO = new PeminjamanDAO();
    private final KategoriDAO kategoriDAO = new KategoriDAO();

    @FXML
    public void initialize() {
        if (LoginController.getUserSession() != null) {
            adminLabel.setText("Halo, " + LoginController.getUserSession().getNama());
        }
        setupTableColumns();
        refreshAllData();
    }

    private void setupTableColumns() {
        // Mapping Buku
        colBukuId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colJudul.setCellValueFactory(new PropertyValueFactory<>("judul"));
        colPenerbit.setCellValueFactory(new PropertyValueFactory<>("penerbit"));
        colKategori.setCellValueFactory(new PropertyValueFactory<>("namaKategori"));
        colStok.setCellValueFactory(new PropertyValueFactory<>("stok"));
        setupAksiBuku();

        // Mapping Anggota
        colAnggotaId.setCellValueFactory(new PropertyValueFactory<>("anggotaId"));
        //kategori
        colAksiKategori.setCellFactory(param -> new TableCell<>() {
            private final Button btnEdit = new Button("Edit");
            private final Button btnDelete = new Button("Hapus");
            private final HBox pane = new HBox(5, btnEdit, btnDelete);

            {
                // Styling agar sama dengan tabel Buku
                btnEdit.setStyle("-fx-background-color: #f1c40f; -fx-text-fill: white; -fx-cursor: hand;");
                btnDelete.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-cursor: hand;");
                
                // Event tombol Hapus
                btnDelete.setOnAction(e -> {
                    Kategori k = getTableRow().getItem();
                    if (k != null) handleDeleteKategori(k);
                });

                // Event tombol Edit
                btnEdit.setOnAction(e -> {
                    Kategori k = getTableRow().getItem();
                    if (k != null) handleEditKategori(k);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : pane);
            }
        });


        // Setup Tombol Edit & Hapus di Kolom Aksi
        colAksi.setCellFactory(param -> new TableCell<>() {
            private final Button btnEdit = new Button("Edit");
            private final Button btnDelete = new Button("Hapus");
            private final HBox pane = new HBox(5, btnEdit, btnDelete);

            {
                btnEdit.setStyle("-fx-background-color: #f1c40f; -fx-text-fill: white; -fx-cursor: hand;");
                btnDelete.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-cursor: hand;");
                
                btnEdit.setOnAction(e -> {
                    Buku b = getTableRow().getItem();
                    if (b != null) showEditBuku(b);
                });
                
                btnDelete.setOnAction(e -> {
                    Buku b = getTableRow().getItem();
                    if (b != null) handleDeleteBuku(b);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : pane);
            }
        });

        // Mapping Anggota, Transaksi, Kategori
        colAnggotaId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNama.setCellValueFactory(new PropertyValueFactory<>("nama"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colNoTelp.setCellValueFactory(new PropertyValueFactory<>("noTelp"));
        setupAksiAnggota();

        // Mapping Transaksi & Kategori
        colTransId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colTransUser.setCellValueFactory(new PropertyValueFactory<>("namaAnggota"));
        colTransBuku.setCellValueFactory(new PropertyValueFactory<>("judulBuku"));
        colTransStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        colKategoriId.setCellValueFactory(new PropertyValueFactory<>("kategoriId"));
        colNamaKategori.setCellValueFactory(new PropertyValueFactory<>("nama_kategori"));
        colDeskripsi.setCellValueFactory(new PropertyValueFactory<>("deskripsi"));
    }

    private void refreshAllData() {
        tableBuku.setItems(FXCollections.observableArrayList(bukuDAO.getAll()));
        tableAnggota.setItems(FXCollections.observableArrayList(anggotaDAO.getAll()));
        tableTransaksi.setItems(FXCollections.observableArrayList(peminjamanDAO.getAll()));
        tableKategori.setItems(FXCollections.observableArrayList(kategoriDAO.getAll()));
    }

    /* ===============================
        MANAJEMEN BUKU
       =============================== */

    private void setupAksiBuku() {
        colAksi.setCellFactory(param -> new TableCell<>() {
            private final Button btnEdit = new Button("Edit");
            private final Button btnDelete = new Button("Hapus");
            private final HBox pane = new HBox(5, btnEdit, btnDelete);
            {
                btnEdit.setStyle("-fx-background-color: #f1c40f; -fx-text-fill: white; -fx-cursor: hand;");
                btnDelete.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-cursor: hand;");
                btnEdit.setOnAction(e -> showEditBuku(getTableRow().getItem()));
                btnDelete.setOnAction(e -> handleDeleteBuku(getTableRow().getItem()));
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : pane);
            }
        });
    }

    @FXML
    private void showAddBuku() {
        openBukuForm(null, "Tambah Buku Baru");
    }


    private void showEditBuku(Buku buku) {
        if (buku != null) openBukuForm(buku, "Edit Buku");
    }

    private void openBukuForm(Buku buku, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/perpus/BukuForm.fxml"));
            Parent root = loader.load();
            BukuFormController controller = loader.getController();
            if (buku != null) controller.setBuku(buku);

            Stage stage = new Stage();
            stage.setTitle(title);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.showAndWait();

            if (controller.isSaveClicked()) {
                boolean success = (buku == null) ? bukuDAO.insert(controller.getBuku()) : bukuDAO.update(controller.getBuku());
                if (success) refreshAllData();
                else showError("Gagal menyimpan data buku.");
            }
        } catch (IOException e) { e.printStackTrace(); }
    }

    private void handleDeleteBuku(Buku buku) {
        if (buku == null) return;
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Hapus buku: " + buku.getJudul() + "?");
        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                if (bukuDAO.delete(buku.getBukuId())) refreshAllData();
                else showError("Gagal menghapus! Buku mungkin sedang dipinjam.");
            }
        });
    }

    /* ===============================
        MANAJEMEN ANGGOTA
       =============================== */

    private void setupAksiAnggota() {
        colAksiAnggota.setCellFactory(param -> new TableCell<>() {
            private final Button btnEdit = new Button("Edit");
            private final Button btnDelete = new Button("Hapus");
            private final HBox pane = new HBox(5, btnEdit, btnDelete);
            {
                btnEdit.setStyle("-fx-background-color: #f1c40f; -fx-text-fill: white; -fx-cursor: hand;");
                btnDelete.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-cursor: hand;");
                btnEdit.setOnAction(e -> showEditAnggota(getTableRow().getItem()));
                btnDelete.setOnAction(e -> handleDeleteAnggota(getTableRow().getItem()));
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : pane);
            }
        });
    }

    @FXML
    private void showAddAnggota() {
        Anggota res = showAnggotaForm(null);
        if (res != null && anggotaDAO.insert(res)) refreshAllData();
    }

    private void showEditAnggota(Anggota a) {
        if (a != null) {
            Anggota res = showAnggotaForm(a);
            if (res != null && anggotaDAO.update(res)) refreshAllData();
        }
    }

    private void handleDeleteAnggota(Anggota a) {
    if (a == null) return;
    
    Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Hapus anggota: " + a.getNama() + "?");
    confirm.showAndWait().ifPresent(response -> {
        if (response == ButtonType.OK) {
            if (anggotaDAO.delete(a.getUserId())) { 
                refreshAllData();
            } else {
                showError("Gagal menghapus! Pastikan anggota tidak memiliki tanggungan pinjaman.");
            }
        }
    });
    }

    private Anggota showAnggotaForm(Anggota existing) {
        Dialog<Anggota> dialog = new Dialog<>();
        dialog.setTitle("Form Anggota");
        dialog.setHeaderText(existing == null ? "Tambah Anggota Baru" : "Edit Data Anggota");

        ButtonType saveButtonType = new ButtonType("Simpan", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10); grid.setVgap(10);
        grid.setPadding(new Insets(20, 20, 10, 20));

        TextField nama = new TextField(existing != null ? existing.getNama() : "");
        TextField email = new TextField(existing != null ? existing.getEmail() : "");
        TextField noTelp = new TextField(existing != null ? existing.getNo_telp() : "");
        TextField user = new TextField(existing != null ? existing.getUsername() : "");
        PasswordField pass = new PasswordField();
        if (existing != null) pass.setText(existing.getPassword());

        grid.add(new Label("Nama:"), 0, 0); grid.add(nama, 1, 0);
        grid.add(new Label("Email:"), 0, 1); grid.add(email, 1, 1);
        grid.add(new Label("No Telp:"), 0, 2); grid.add(noTelp, 1, 2);
        grid.add(new Label("Username:"), 0, 3); grid.add(user, 1, 3);
        grid.add(new Label("Password:"), 0, 4); grid.add(pass, 1, 4);

        dialog.getDialogPane().setContent(grid);
        dialog.setResultConverter(btn -> {
            if (btn == saveButtonType) {
                if (existing == null) {
                    return new Anggota(0, user.getText(), pass.getText(), nama.getText(), 0, email.getText(), noTelp.getText());
                }
                existing.setNama(nama.getText()); existing.setEmail(email.getText());
                existing.setNo_telp(noTelp.getText()); existing.setUsername(user.getText());
                existing.setPassword(pass.getText());
                return existing;
            }
            return null;
        });
        return dialog.showAndWait().orElse(null);
    }

    /* ===============================
        SISTEM UTILITAS
       =============================== */

    @FXML
    private void handleSearchBuku() {
        String query = searchBukuField.getText().toLowerCase();
        tableBuku.setItems(FXCollections.observableArrayList(
            bukuDAO.getAll().stream()
                .filter(b -> b.getJudul().toLowerCase().contains(query) || b.getPenerbit().toLowerCase().contains(query))
                .collect(Collectors.toList())
        ));
    }

    @FXML
    private void handleLogout(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/perpus/LoginView.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) { e.printStackTrace(); }
    }

        /* ===============================
        LOGIKA CRUD KATEGORI
    =============================== */

    @FXML
    private void handleTambahKategori() {
        // 1. Membuat Dialog Input menggunakan TextInputDialog atau Custom Dialog
        javafx.scene.control.Dialog<Kategori> dialog = new javafx.scene.control.Dialog<>();
        dialog.setTitle("Tambah Kategori Baru");
        dialog.setHeaderText("Masukkan detail kategori master");

        // Set Button Simpan dan Batal
        ButtonType simpanButtonType = new ButtonType("Simpan", javafx.scene.control.ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(simpanButtonType, ButtonType.CANCEL);

        // Layout input (GridPane)
        javafx.scene.layout.GridPane grid = new javafx.scene.layout.GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        TextField namaField = new TextField();
        namaField.setPromptText("Nama Kategori");
        javafx.scene.control.TextArea deskripsiArea = new javafx.scene.control.TextArea();
        deskripsiArea.setPromptText("Deskripsi singkat");
        deskripsiArea.setPrefRowCount(3);

        grid.add(new Label("Nama Kategori:"), 0, 0);
        grid.add(namaField, 1, 0);
        grid.add(new Label("Deskripsi:"), 0, 1);
        grid.add(deskripsiArea, 1, 1);
        dialog.getDialogPane().setContent(grid);

        // 2. Convert input ke objek Kategori saat tombol simpan diklik
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == simpanButtonType) {
                // Menggunakan Constructor INSERT (String nama, String deskripsi)
                return new Kategori(namaField.getText(), deskripsiArea.getText());
            }
            return null;
        });

        // 3. Tampilkan Dialog dan proses simpan
        dialog.showAndWait().ifPresent(kategoriBaru -> {
            if (kategoriBaru.getNama_kategori().isEmpty()) {
                showError("Nama kategori tidak boleh kosong!");
                return;
            }

            // Pastikan KategoriDAO sudah ada method save()
            if (kategoriDAO.save(kategoriBaru)) {
                refreshAllData(); // Refresh semua tabel termasuk kategori
                Alert info = new Alert(Alert.AlertType.INFORMATION, "Kategori berhasil ditambahkan!");
                info.showAndWait();
            } else {
                showError("Gagal menyimpan kategori ke database.");
            }
        });
    }


    private void handleDeleteKategori(Kategori k) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Hapus kategori: " + k.getNama_kategori() + "?");
        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                // Memanggil method delete di KategoriDAO
                if (kategoriDAO.delete(k.getKategoriId())) {
                    refreshAllData(); // Refresh tampilan tabel
                } else {
                    showError("Gagal menghapus! Kategori ini mungkin masih digunakan oleh data buku.");
                }
            }
        });
    }

    private void handleEditKategori(Kategori kategoriLama) {
        javafx.scene.control.Dialog<Kategori> dialog = new javafx.scene.control.Dialog<>();
        dialog.setTitle("Edit Kategori");
        dialog.setHeaderText("Ubah data kategori: " + kategoriLama.getNama_kategori());

        ButtonType simpanButtonType = new ButtonType("Simpan Perubahan", javafx.scene.control.ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(simpanButtonType, ButtonType.CANCEL);

        TextField namaField = new TextField(kategoriLama.getNama_kategori());
        javafx.scene.control.TextArea deskripsiArea = new javafx.scene.control.TextArea(kategoriLama.getDeskripsi());
        deskripsiArea.setPrefRowCount(3);

        javafx.scene.layout.GridPane grid = new javafx.scene.layout.GridPane();
        grid.setHgap(10); grid.setVgap(10);
        grid.add(new Label("Nama Kategori:"), 0, 0);
        grid.add(namaField, 1, 0);
        grid.add(new Label("Deskripsi:"), 0, 1);
        grid.add(deskripsiArea, 1, 1);
        dialog.getDialogPane().setContent(grid);

        
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == simpanButtonType) {
                // Tetap bawa ID yang lama agar UPDATE tahu baris mana yang diubah
                return new Kategori(kategoriLama.getKategoriId(), namaField.getText(), deskripsiArea.getText());
            }
            return null;
        });

        //Proses Update
        dialog.showAndWait().ifPresent(kategoriUpdate -> {
            if (kategoriUpdate.getNama_kategori().isEmpty()) {
                showError("Nama kategori tidak boleh kosong!");
            } else if (kategoriDAO.update(kategoriUpdate)) {
                refreshAllData(); // Refresh tabel agar data baru muncul
                Alert info = new Alert(Alert.AlertType.INFORMATION, "Kategori berhasil diperbarui!");
                info.showAndWait();
            } else {
                showError("Gagal memperbarui kategori di database.");
            }
        });
    }




    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(message);
        alert.showAndWait();
    }
}