public class Buku {
    private int bukuId;
    private String judul;
    private String penerbit;
    private int tahunTerbit;
    private int stok;

    public Buku(int bukuId, String judul, String penerbit, int tahunTerbit, int stok) {
        this.bukuId = bukuId;
        this.judul = judul;
        this.penerbit = penerbit;
        this.tahunTerbit = tahunTerbit;
        this.stok = stok;
    }

    // Overloading method updateStok()
    public void updateStok(int perubahan) {
        this.stok += perubahan;
    }

    public void updateStok(boolean tambah, int jumlah) {
        this.stok += (tambah ? jumlah : -jumlah);
    }

    // Getter & Setter
    public int getBukuId() { return bukuId; }
    public String getJudul() { return judul; }
    public void setJudul(String judul) { this.judul = judul; }
    public int getStok() { return stok; }

    @Override
    public String toString() {
        return bukuId + ". " + judul + " (" + penerbit + ", " + tahunTerbit + ") - Stok: " + stok;
    }
}

