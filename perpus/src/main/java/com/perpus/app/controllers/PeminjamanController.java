package com.perpus.app.controllers;

import java.time.LocalDate;
import java.util.List;

import com.perpus.app.dao.BukuDAO;
import com.perpus.app.dao.PeminjamanDAO;
import com.perpus.app.models.Anggota;
import com.perpus.app.models.Buku;
import com.perpus.app.models.Peminjaman;

public class PeminjamanController {

    private final PeminjamanDAO peminjamanDAO;
    private final BukuDAO bukuDAO;

    public PeminjamanController() {
        this.peminjamanDAO = new PeminjamanDAO();
        this.bukuDAO = new BukuDAO();
    }

    /* ===============================
       1. PINJAM BUKU
       =============================== */
    public boolean pinjam(Anggota anggota, int bukuId) {
        Buku buku = bukuDAO.getById(bukuId);

        // Validasi stok menggunakan getter yang sudah kita buat
        if (buku == null || buku.getStok() <= 0) {
            return false;
        }

        // PERBAIKAN: Gunakan ID (int), bukan objek
        Peminjaman peminjaman = new Peminjaman(anggota.getUserId(), bukuId);
        peminjaman.setTanggalPinjam(LocalDate.now());
        peminjaman.setStatus("DIPINJAM"); // Gunakan String sesuai DB
        peminjaman.setAdminId(1); // Sementara default admin ID 1

        boolean sukses = peminjamanDAO.insert(peminjaman);
        if (sukses) {
            // Memanggil updateStok di BukuDAO
            bukuDAO.updateStok(-1, bukuId);
        }
        return sukses;
    }

    /* ===============================
       2. KEMBALIKAN BUKU
       =============================== */
    public boolean kembalikan(int peminjamanId) {
        Peminjaman p = peminjamanDAO.getById(peminjamanId);
        
        // Cek apakah data ada dan statusnya masih "DIPINJAM"
        if (p == null || !"DIPINJAM".equalsIgnoreCase(p.getStatus())) {
            return false;
        }

        boolean updated = peminjamanDAO.updateStatus(peminjamanId, "DIKEMBALIKAN");
        if (updated) {
            // Ambil ID buku dari objek p dan tambah stoknya
            bukuDAO.updateStok(1, p.getBukuId());
        }
        return updated;
    }

    /* ===============================
       3. LIST PEMINJAMAN AKTIF
       =============================== */
    public List<Peminjaman> getPeminjamanAktif() {
        // Sesuaikan dengan method di DAO kamu (getByStatus)
        return peminjamanDAO.getByStatus("DIPINJAM");
    }

    /* ===============================
       4. RIWAYAT PER ANGGOTA
       =============================== */
    public List<Peminjaman> getRiwayatAnggota(int anggotaId) {
        return peminjamanDAO.getByAnggota(anggotaId);
    }
}