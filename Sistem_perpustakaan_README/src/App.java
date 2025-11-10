import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class App {
    private static final Scanner sc = new Scanner(System.in);
    private static final Admin admin = new Admin("Admin Utama");
    private static final List<Anggota> anggotaList = new ArrayList<>();

    public static void main(String[] args) {
        System.out.println("=== SISTEM PERPUSTAKAAN JAVA ===");

        while (true) {
            try {
                System.out.println("\n1. Login");
                System.out.println("2. Register Anggota");
                System.out.println("3. Keluar");
                System.out.print("Pilih: ");
                int pilih = Integer.parseInt(sc.nextLine());

                switch (pilih) {
                    case 1 -> login();
                    case 2 -> register();
                    case 3 -> {
                        System.out.println("Program selesai. Terima kasih!");
                        return;
                    }
                    default -> System.out.println("⚠️ Pilihan tidak valid!");
                }
            } catch (NumberFormatException e) {
                System.out.println("⚠️ Input harus berupa angka!");
            }
        }
    }

    // ==================== LOGIN ====================
    private static void login() {
        System.out.print("Username: ");
        String user = sc.nextLine();
        System.out.print("Password: ");
        String pass = sc.nextLine();

        User u = null;

        if (user.equals("admin") && pass.equals("#12345")) {
            u = admin;
        } else {
            for (Anggota a : anggotaList) {
                if (a.login(user, pass)) {
                    u = a;
                    break;
                }
            }
        }

        if (u != null) {
            System.out.println("\n✅ Login berhasil sebagai " + u.getRole());
            u.menu();
            if (u instanceof Admin) menuAdmin((Admin) u);
            else menuAnggota((Anggota) u);
        } else {
            System.out.println("❌ Login gagal! Username atau password salah.");
        }
    }

    // ==================== REGISTER ====================
    private static void register() {
        System.out.print("Nama: ");
        String nama = sc.nextLine();
        System.out.print("Email: ");
        String email = sc.nextLine();
        System.out.print("No Telp: ");
        String no = sc.nextLine();
        System.out.print("Username: ");
        String user = sc.nextLine();
        System.out.print("Password: ");
        String pass = sc.nextLine();

        String sql = "INSERT INTO anggota (nama, email, noTelp, username, password) VALUES (?, ?, ?, ?, ?)";

        try (Connection c = koneksi.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, nama);
            ps.setString(2, email);
            ps.setString(3, no);
            ps.setString(4, user);
            ps.setString(5, pass);
            ps.executeUpdate();

            anggotaList.add(new Anggota(nama, email, no, user, pass));
            System.out.println("✅ Registrasi berhasil! Silakan login.");
        } catch (SQLException e) {
            System.out.println("❌ Gagal registrasi: " + e.getMessage());
        }
    }

    // ==================== MENU ADMIN ====================
    private static void menuAdmin(Admin a) {
        while (true) {
            try {
                System.out.println("\n=== MENU ADMIN ===");
                System.out.println("1. Lihat Buku");
                System.out.println("2. Tambah Buku");
                System.out.println("3. Ubah Buku");
                System.out.println("4. Hapus Buku");
                System.out.println("5. Logout");
                System.out.print("Pilih: ");
                int p = Integer.parseInt(sc.nextLine());

                switch (p) {
                    case 1 -> a.lihatBuku();

                    case 2 -> {
                        System.out.print("Judul: ");
                        String judul = sc.nextLine();
                        System.out.print("Penerbit: ");
                        String penerbit = sc.nextLine();
                        System.out.print("Tahun Terbit: ");
                        int tahun = Integer.parseInt(sc.nextLine());
                        System.out.print("Stok: ");
                        int stok = Integer.parseInt(sc.nextLine());
                        a.tambahBuku(judul, penerbit, tahun, stok);
                    }

                    case 3 -> {
                        System.out.print("ID Buku: ");
                        int id = Integer.parseInt(sc.nextLine());
                        System.out.print("Judul baru: ");
                        String judulBaru = sc.nextLine();
                        System.out.print("Penerbit baru: ");
                        String penerbitBaru = sc.nextLine();
                        System.out.print("Tahun terbit baru: ");
                        int tahunBaru = Integer.parseInt(sc.nextLine());
                        System.out.print("Stok baru: ");
                        int stokBaru = Integer.parseInt(sc.nextLine());
                        a.ubahBuku(id, judulBaru, penerbitBaru, tahunBaru, stokBaru);
                    }

                    case 4 -> {
                        System.out.print("ID Buku: ");
                        int id = Integer.parseInt(sc.nextLine());
                        a.hapusBuku(id);
                    }

                    case 5 -> {
                        a.logout();
                        return;
                    }

                    default -> System.out.println("⚠️ Pilihan tidak valid!");
                }
            } catch (NumberFormatException e) {
                System.out.println("⚠️ Input harus berupa angka!");
            }
        }
    }

    // ==================== MENU ANGGOTA ====================
    private static void menuAnggota(Anggota a) {
        while (true) {
            try {
                System.out.println("\n=== MENU ANGGOTA ===");
                System.out.println("1. Lihat Buku");
                System.out.println("2. Pinjam Buku");
                System.out.println("3. Kembalikan Buku");
                System.out.println("4. Lihat Riwayat");
                System.out.println("5. Logout");
                System.out.print("Pilih: ");
                int p = Integer.parseInt(sc.nextLine());

                switch (p) {
                    case 1 -> admin.lihatBuku();

                    case 2 -> {
                        admin.lihatBuku();
                        System.out.print("ID buku: ");
                        int id = Integer.parseInt(sc.nextLine());
                        Buku bukuDitemukan = admin.getDaftarBuku()
                                .stream()
                                .filter(b -> b.getBukuId() == id)
                                .findFirst()
                                .orElse(null);

                        if (bukuDitemukan != null) {
                            a.pinjamBuku(bukuDitemukan);
                        } else {
                            System.out.println("⚠️ Buku dengan ID tersebut tidak ditemukan.");
                        }
                    }

                    case 3 -> {
                        a.lihatRiwayat();
                        System.out.print("ID peminjaman: ");
                        int idP = Integer.parseInt(sc.nextLine());
                        a.kembalikanBuku(idP);
                    }

                    case 4 -> a.lihatRiwayat();

                    case 5 -> {
                        a.logout();
                        return;
                    }

                    default -> System.out.println("⚠️ Pilihan tidak valid!");
                }
            } catch (NumberFormatException e) {
                System.out.println("⚠️ Input harus berupa angka!");
            }
        }
    }
}
