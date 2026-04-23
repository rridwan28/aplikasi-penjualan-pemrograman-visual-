package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 * Singleton Database Connection Manager
 * Gunakan DBConnection.getConnection() untuk mendapatkan koneksi.
 */
public class DBConnection {

    // ── Sesuaikan dengan konfigurasi MySQL lokal kamu ──
    private static final String HOST     = "localhost";
    private static final String PORT     = "3306";
    private static final String DATABASE = "db_sekolah";
    private static final String USER     = "root";
    private static final String PASSWORD = "";   // kosong jika XAMPP default

    private static final String URL =
        "jdbc:mysql://" + HOST + ":" + PORT + "/" + DATABASE
        + "?useSSL=false&serverTimezone=Asia/Jakarta&allowPublicKeyRetrieval=true";

    private static Connection connection = null;

    /** Kembalikan koneksi yang aktif, atau buat baru jika belum ada / sudah tertutup. */
    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
            }
        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null,
                "Driver MySQL tidak ditemukan!\nPastikan mysql-connector-java sudah di-add ke Libraries.",
                "Error Driver", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                "Gagal terhubung ke database!\n" + e.getMessage() +
                "\n\nCek: XAMPP sudah jalan? Username/Password benar?",
                "Error Koneksi", JOptionPane.ERROR_MESSAGE);
        }
        return connection;
    }

    /** Tutup koneksi jika masih terbuka (panggil saat aplikasi ditutup). */
    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                connection = null;
            }
        } catch (SQLException e) {
            System.err.println("Error closing connection: " + e.getMessage());
        }
    }
}
