package com.perpus.app.controllers;
import com.perpus.app.dao.BukuDAO;

import java.util.List;

public class AnggotaController {

    private final BukuDAO bukuDAO;
    private final PeminjamanDAO peminjamanDAO;
    private final Anggota anggotaLogin;

    public AnggotaController(Anggota anggotaLogin) {
        this.anggotaLogin = anggotaLogin;
        this.bukuDAO = new BukuDAO();
        this.peminjamanDAO = new PeminjamanDAO();
    }

    /* ===============================
       LIHAT DAFTAR BUKU
       =============================== */
    public List<Buku> getDaftarBuku() {
        return bukuDAO.getAll();
    }

    /* ===============================
       PINJAM BUKU
       =============================== */
    public boolean pinjamBuku(int bukuId) {
        Buku buku = bukuDAO.getById(bukuId);

        if (buku == null || buku.getStok() <= 0) {
            return false;
        }

        Peminjaman p = new Peminjaman(anggotaLogin, buku);

        boolean sukses = peminjamanDAO.insert(p);
        if (sukses) {
            bukuDAO.updateStok(bukuId, -1);
        }
        return sukses;
    }

    /* ===============================
       KEMBALIKAN BUKU
       =============================== */
    public boolean kembalikanBuku(int peminjamanId) {
        boolean updated = peminjamanDAO.updateStatus(peminjamanId, "selesai");

        if (updated) {
            Peminjaman p = peminjamanDAO.getById(peminjamanId);
            bukuDAO.updateStok(p.getBuku().getBukuId(), +1);
        }
        return updated;
    }

    /* ===============================
       RIWAYAT PEMINJAMAN
       =============================== */
    public List<Peminjaman> getRiwayat() {
        return peminjamanDAO.getByAnggota(anggotaLogin.getUserId());
    }
}

