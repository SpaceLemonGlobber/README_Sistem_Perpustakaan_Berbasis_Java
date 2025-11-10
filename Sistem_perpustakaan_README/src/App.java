public class App {
    private static Scanner sc = new Scanner(System.in);
    private static Admin admin = new Admin("Admin Utama");
    private static List<Anggota> anggotaList = new ArrayList<>();

    public static void main(String[] args) {
        System.out.println("=== SISTEM PERPUSTAKAAN JAVA ===");

        while (true) {
            System.out.println("\n1. Login");
            System.out.println("2. Register Anggota");
            System.out.println("3. Keluar");
            System.out.print("Pilih: ");
            int pilih = sc.nextInt(); sc.nextLine();

            switch (pilih) {
                case 1 : login();
                case 2 : register();
                case 3 : {
                    System.out.println("Program selesai. Terima kasih!");
                    return;
                }
                default : System.out.println("Pilihan tidak valid!");
            }
        }
    }

    private static void login() {
        System.out.print("Username: ");
        String user = sc.nextLine();
        System.out.print("Password: ");
        String pass = sc.nextLine();

        User u = null;

        // admin login khusus
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
            System.out.println("\nLogin berhasil sebagai " + u.getRole());
            u.menu(); // Polymorphism: bisa Admin atau Anggota
            if (u instanceof Admin) menuAdmin((Admin) u);
            else menuAnggota((Anggota) u);
        } else {
            System.out.println("Login gagal! Username atau password salah.");
        }
    }

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

        Anggota baru = new Anggota(anggotaList.size() + 1, nama, email, no, user, pass);
        anggotaList.add(baru);
        System.out.println("Registrasi berhasil! Silakan login.");
    }

    private static void menuAdmin(Admin a) {
        while (true) {
            System.out.println("\n=== MENU ADMIN ===");
            System.out.println("1. Lihat Buku");
            System.out.println("2. Tambah Buku");
            System.out.println("3. Ubah Buku");
            System.out.println("4. Hapus Buku");
            System.out.println("5. Logout");
            System.out.print("Pilih: ");
            int p = sc.nextInt(); sc.nextLine();

            switch (p) {
                case 1 : a.lihatBuku();
                case 2 : {
                    System.out.print("Judul: ");
                    String judul = sc.nextLine();
                    System.out.print("Penerbit: ");
                    String penerbit = sc.nextLine();
                    System.out.print("Tahun Terbit: ");
                    int tahun = sc.nextInt();
                    System.out.print("Stok: ");
                    int stok = sc.nextInt();
                    a.tambahBuku(a.getDaftarBuku().size() + 1, judul, penerbit, tahun, stok);
                }
                case 3 : {
                    System.out.print("ID Buku: ");
                    int id = sc.nextInt(); sc.nextLine();
                    System.out.print("Judul baru: ");
                    String baru = sc.nextLine();
                    a.ubahBuku(id, baru);
                }
                case 4 : {
                    System.out.print("ID Buku: ");
                    int id = sc.nextInt();
                    a.hapusBuku(id);
                }
                case 5 : {
                    a.logout();
                    return;
                }
                default : System.out.println("Pilihan tidak valid!");
            }
        }
    }

    private static void menuAnggota(Anggota a) {
        while (true) {
            System.out.println("\n=== MENU ANGGOTA ===");
            System.out.println("1. Lihat Buku");
            System.out.println("2. Pinjam Buku");
            System.out.println("3. Kembalikan Buku");
            System.out.println("4. Lihat Riwayat");
            System.out.println("5. Logout");
            System.out.print("Pilih: ");
            int p = sc.nextInt(); sc.nextLine();

            switch (p) {
                case 1 : admin.lihatBuku();
                case 2 : {
                    admin.lihatBuku();
                    System.out.print("ID buku: ");
                    int id = sc.nextInt();
                    admin.getDaftarBuku().stream()
                        .filter(b : b.getBukuId() == id)
                        .findFirst()
                        .ifPresent(a::pinjamBuku);
                }
                case 3 : {
                    a.lihatRiwayat();
                    System.out.print("ID peminjaman: ");
                    int idP = sc.nextInt();
                    a.kembalikanBuku(idP);
                }
                case 4 : a.lihatRiwayat();
                case 5 : {
                    a.logout();
                    return;
                }
                default : System.out.println("Pilihan tidak valid!");
            }
        }
    }
}
