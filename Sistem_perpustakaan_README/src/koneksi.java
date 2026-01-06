import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class koneksi {
    private static final String URL = "jdbc:mysql://localhost:3306/readmeperpustakaan";
    private static final String USER = "root";
    private static final String PASS = "root";

    public static Connection getConnection() throws SQLException {
        // setiap kali dipanggil, buat koneksi baru
        return DriverManager.getConnection(URL, USER, PASS);
    }
}
