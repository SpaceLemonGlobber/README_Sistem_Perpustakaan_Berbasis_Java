package com.perpus.app.controllers;

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
        return peminjamanDAO.getAktif().size();
    }

    /* ===============================
       DASHBOARD ANGGOTA
       =============================== */

    public int getTotalPeminjamanAnggota(int anggotaId) {
        return peminjamanDAO.getByAnggota(anggotaId).size();
    }

    public int getTotalPeminjamanAktifAnggota(int anggotaId) {
        return (int) peminjamanDAO.getByAnggota(anggotaId)
                .stream()
                .filter(p -> p.getStatus().equalsIgnoreCase("aktif"))
                .count();
    }
}

