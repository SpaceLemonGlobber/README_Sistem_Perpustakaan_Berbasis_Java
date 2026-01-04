package com.perpus.app.controllers;

import com.perpus.app.dao.BukuDAO;
import com.perpus.app.dao.PeminjamanDAO;
import com.perpus.app.models.Buku;

public class DashboardController {

    private final BukuDAO bukuDAO;
    private final PeminjamanDAO peminjamanDAO;

    public DashboardController() {
        this.bukuDAO = new BukuDAO();
        this.peminjamanDAO = new PeminjamanDAO();
    }

    /* ===============================
       DASHBOARD ADMIN
       =============================== */

    public int getTotalJudulBuku() {
        return bukuDAO.getAll().size();
    }

    public int getTotalStokBuku() {
        return bukuDAO.getAll()
                .stream()
                .mapToInt(Buku::getStok)
                .sum();
    }

    public int getTotalPeminjamanAktif() {
        // PERBAIKAN: Gunakan getByStatus sesuai method di PeminjamanDAO
        return peminjamanDAO.getByStatus("DIPINJAM").size();
    }

    /* ===============================
       DASHBOARD ANGGOTA
       =============================== */

    public int getTotalPeminjamanAnggota(int anggotaId) {
        return peminjamanDAO.getByAnggota(anggotaId).size();
    }

    public int getTotalPeminjamanAktifAnggota(int anggotaId) {
        // PERBAIKAN: Bandingkan dengan String "DIPINJAM", bukan Enum
        return (int) peminjamanDAO.getByAnggota(anggotaId)
            .stream()
            .filter(p -> "DIPINJAM".equalsIgnoreCase(p.getStatus()))
            .count();
    }
}