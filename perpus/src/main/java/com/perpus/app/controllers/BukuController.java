package com.perpus.app.controllers;

import java.util.List;

public class BukuController {

    private final BukuDAO bukuDAO;

    public BukuController() {
        this.bukuDAO = new BukuDAO();
    }

    /* ===============================
       LIHAT SEMUA BUKU
       =============================== */
    public List<Buku> getAllBuku() {
        return bukuDAO.getAll();
    }

    /* ===============================
       TAMBAH BUKU
       =============================== */
    public boolean tambahBuku(String judul, String penerbit, int tahunTerbit, int stok) {
        if (judul == null || judul.isBlank() || stok < 0) {
            return false;
        }

        Buku buku = new Buku(0, judul, penerbit, tahunTerbit, stok);
        return bukuDAO.insert(buku);
    }

    /* ===============================
       UPDATE BUKU
       =============================== */
    public boolean updateBuku(int bukuId, String judul, String penerbit, int tahunTerbit, int stok) {
        if (bukuId <= 0 || stok < 0) {
            return false;
        }

        Buku buku = new Buku(bukuId, judul, penerbit, tahunTerbit, stok);
        return bukuDAO.update(buku);
    }

    /* ===============================
       HAPUS BUKU
       =============================== */
    public boolean hapusBuku(int bukuId) {
        if (bukuId <= 0) {
            return false;
        }
        return bukuDAO.delete(bukuId);
    }

    /* ===============================
       CARI BUKU BY ID
       =============================== */
    public Buku getBukuById(int bukuId) {
        if (bukuId <= 0) {
            return null;
        }
        return bukuDAO.getById(bukuId);
    }
}

