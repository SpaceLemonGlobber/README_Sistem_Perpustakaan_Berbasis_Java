package com.perpus.app.controllers;

import java.time.LocalDate;
import java.util.List;

import com.perpus.app.dao.BukuDAO;
import com.perpus.app.dao.PeminjamanDAO;
import com.perpus.app.models.Anggota;
import com.perpus.app.models.Buku;
import com.perpus.app.models.Peminjaman;

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

        // Validasi stok menggunakan getter
        if (buku == null || buku.getStok() <= 0) {
            return false;
        }

        // PERBAIKAN: Gunakan ID (int), bukan objek
        Peminjaman p = new Peminjaman(anggotaLogin.getUserId(), bukuId);
        p.setTanggalPinjam(LocalDate.now());
        p.setStatus("DIPINJAM");

        boolean sukses = peminjamanDAO.insert(p);
        if (sukses) {
            // Update stok buku di database
            bukuDAO.updateStok(-1, bukuId);
        }
        return sukses;
    }

    /* ===============================
       KEMBALIKAN BUKU
       =============================== */
    public boolean kembalikanBuku(int peminjamanId) {
        // Ambil data peminjaman terlebih dahulu
        Peminjaman p = peminjamanDAO.getById(peminjamanId);
        
        if (p == null || !"DIPINJAM".equalsIgnoreCase(p.getStatus())) {
            return false;
        }

        boolean updated = peminjamanDAO.updateStatus(peminjamanId, "DIKEMBALIKAN");

        if (updated) {
            // PERBAIKAN: Langsung p.getBukuId() karena tipenya sudah int
            bukuDAO.updateStok(1, p.getBukuId());
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