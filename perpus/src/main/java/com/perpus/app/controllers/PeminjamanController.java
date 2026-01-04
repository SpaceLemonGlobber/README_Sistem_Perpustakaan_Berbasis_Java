package com.perpus.app.controllers;
import java.util.List;

import com.perpus.app.dao.BukuDAO;
import com.perpus.app.dao.PeminjamanDAO;
import com.perpus.app.models.Anggota;
import com.perpus.app.models.Buku;
import com.perpus.app.models.Peminjaman;
import com.perpus.app.models.StatusPeminjaman;

public class PeminjamanController {

    private final PeminjamanDAO peminjamanDAO;
    private final BukuDAO bukuDAO;

    public PeminjamanController() {
        this.peminjamanDAO = new PeminjamanDAO();
        this.bukuDAO = new BukuDAO();
    }

    /* ===============================
       PINJAM BUKU
       =============================== */
    public boolean pinjam(Anggota anggota, int bukuId) {
        Buku buku = bukuDAO.getById(bukuId);

        if (buku == null || buku.getStok() <= 0) {
            return false;
        }

        Peminjaman peminjaman = new Peminjaman(anggota, buku);

        boolean sukses = peminjamanDAO.insert(peminjaman);
        if (sukses) {
            bukuDAO.updateStok(bukuId, -1);
        }
        return sukses;
    }

    /* ===============================
       KEMBALIKAN BUKU
       =============================== */
    public boolean kembalikan(int peminjamanId) {
        Peminjaman p = peminjamanDAO.getById(peminjamanId);
        if (p == null || p.getStatus() != StatusPeminjaman.AKTIF) {
        return false;
    }

        boolean updated = peminjamanDAO.updateStatus(peminjamanId, "selesai");
        if (updated) {
            bukuDAO.updateStok(p.getBuku().getBukuId(), +1);
        }
        return updated;
    }

    /* ===============================
       LIST PEMINJAMAN AKTIF
       =============================== */
    public List<Peminjaman> getPeminjamanAktif() {
        return peminjamanDAO.getAktif();
    }

    /* ===============================
       RIWAYAT PER ANGGOTA
       =============================== */
    public List<Peminjaman> getRiwayatAnggota(int anggotaId) {
        return peminjamanDAO.getByAnggota(anggotaId);
    }
}

