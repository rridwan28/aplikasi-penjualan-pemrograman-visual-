package dao;

import util.DBConnection;
import java.sql.*;
import java.util.*;

public class AbsensiDAO {

    /**
     * Ambil absensi untuk kelas + tanggal tertentu.
     * Return: List of Object[] { siswa_id, nama, nis, status, keterangan }
     */
    public List<Object[]> getAbsensiByKelasAndDate(int kelasId, String tanggal) {
        List<Object[]> list = new ArrayList<>();
        String sql = "SELECT s.siswa_id, s.nama_lengkap, s.nis, " +
                     "  COALESCE(a.status,'H') AS status, COALESCE(a.keterangan,'') AS ket " +
                     "FROM siswa s " +
                     "JOIN kelas_siswa ks ON s.siswa_id=ks.siswa_id " +
                     "LEFT JOIN absensi a ON a.siswa_id=s.siswa_id AND a.kelas_id=? AND a.tanggal=? " +
                     "WHERE ks.kelas_id=? AND s.status_aktif=1 ORDER BY s.nama_lengkap";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, kelasId);
            ps.setString(2, tanggal);
            ps.setInt(3, kelasId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Object[]{
                    rs.getInt("siswa_id"),
                    rs.getString("nama_lengkap"),
                    rs.getString("nis"),
                    rs.getString("status"),
                    rs.getString("ket")
                });
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    /** Simpan batch absensi (upsert). */
    public boolean saveAbsensiBatch(int kelasId, String tanggal, int inputBy,
                                     List<Object[]> rows) {
        // rows: { siswa_id, status, keterangan }
        String sql = "INSERT INTO absensi(siswa_id,kelas_id,tanggal,status,keterangan,input_by) " +
                     "VALUES(?,?,?,?,?,?) " +
                     "ON DUPLICATE KEY UPDATE status=VALUES(status), keterangan=VALUES(keterangan), input_by=VALUES(input_by)";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            for (Object[] row : rows) {
                ps.setInt(1, (int) row[0]);
                ps.setInt(2, kelasId);
                ps.setString(3, tanggal);
                ps.setString(4, (String) row[1]);
                ps.setString(5, (String) row[2]);
                ps.setInt(6, inputBy);
                ps.addBatch();
            }
            ps.executeBatch();
            return true;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    /**
     * Rekap kehadiran per siswa untuk laporan.
     * Return: { nama, nis, hadir, izin, sakit, alpha, total_hari }
     */
    public List<Object[]> getRekapByKelas(int kelasId, String dari, String sampai) {
        List<Object[]> list = new ArrayList<>();
        String sql = "SELECT s.nama_lengkap, s.nis, " +
                     "  SUM(CASE WHEN a.status='H' THEN 1 ELSE 0 END) AS hadir, " +
                     "  SUM(CASE WHEN a.status='I' THEN 1 ELSE 0 END) AS izin, " +
                     "  SUM(CASE WHEN a.status='S' THEN 1 ELSE 0 END) AS sakit, " +
                     "  SUM(CASE WHEN a.status='A' THEN 1 ELSE 0 END) AS alpha, " +
                     "  COUNT(a.absensi_id) AS total_hari " +
                     "FROM siswa s " +
                     "JOIN kelas_siswa ks ON s.siswa_id=ks.siswa_id " +
                     "LEFT JOIN absensi a ON a.siswa_id=s.siswa_id AND a.kelas_id=? " +
                     "  AND a.tanggal BETWEEN ? AND ? " +
                     "WHERE ks.kelas_id=? AND s.status_aktif=1 " +
                     "GROUP BY s.siswa_id ORDER BY s.nama_lengkap";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, kelasId);
            ps.setString(2, dari);
            ps.setString(3, sampai);
            ps.setInt(4, kelasId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Object[]{
                    rs.getString("nama_lengkap"),
                    rs.getString("nis"),
                    rs.getInt("hadir"),
                    rs.getInt("izin"),
                    rs.getInt("sakit"),
                    rs.getInt("alpha"),
                    rs.getInt("total_hari")
                });
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }
}
