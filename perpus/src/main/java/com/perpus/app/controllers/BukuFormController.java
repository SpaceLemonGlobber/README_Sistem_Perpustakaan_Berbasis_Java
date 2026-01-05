package com.perpus.app.controllers;

import com.perpus.app.models.Buku;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class BukuFormController {
    @FXML private Label headerLabel;
    @FXML private TextField txtJudul, txtPenerbit, txtTahun, txtStok;

    private Buku buku;
    private boolean isSaveClicked = false;

    public void setBuku(Buku buku) {
        this.buku = buku;
        if (buku != null) {
            headerLabel.setText("Edit Data Buku");
            txtJudul.setText(buku.getJudul());
            txtPenerbit.setText(buku.getPenerbit());
            txtTahun.setText(String.valueOf(buku.getTahunTerbit()));
            txtStok.setText(String.valueOf(buku.getStok()));
        }
    }

    public boolean isSaveClicked() { return isSaveClicked; }

    @FXML
    private void handleSave() {
        if (buku == null) buku = new Buku(0, "", "", 0, 0);
        
        buku.setJudul(txtJudul.getText());
        buku.setPenerbit(txtPenerbit.getText());
        buku.setTahunTerbit(Integer.parseInt(txtTahun.getText()));
        buku.setStok(Integer.parseInt(txtStok.getText()));

        isSaveClicked = true;
        closeWindow();
    }

    @FXML private void handleCancel() { closeWindow(); }
    private void closeWindow() { ((Stage) txtJudul.getScene().getWindow()).close(); }
    public Buku getBuku() { return buku; }
}