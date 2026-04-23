package dao;

import util.DBConnection;
import java.sql.*;
import java.util.*;

public class NilaiDAO {

    /**
     * Ambil nilai siswa dalam satu kelas + mapel.
     * Return: { siswa_id, nama, nis, tugas, uts, uas, praktik }
     */
    public List<Object[]> getNilaiByKelasMapel(int kelasId, int mapelId) {
        List<Object[]> list = new ArrayList<>();
        String sql = "SELECT s.siswa_id, s.nama_lengkap, s.nis, " +
                     "  COALESCE(n.tugas,0) t, COALESCE(n.uts,0) u, " +
                     "  COALESCE(n.uas,0) ua, COALESCE(n.praktik,0) p " +
                     "FROM siswa s " +
                     "JOIN kelas_siswa ks ON s.siswa_id=ks.siswa_id " +
                     "LEFT JOIN nilai n ON n.siswa_id=s.siswa_id AND n.kelas_id=? AND n.mapel_id=? " +
                     "WHERE ks.kelas_id=? AND s.status_aktif=1 ORDER BY s.nama_lengkap";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, kelasId);
            ps.setInt(2, mapelId);
            ps.setInt(3, kelasId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Object[]{
                    rs.getInt("siswa_id"),
                    rs.getString("nama_lengkap"),
                    rs.getString("nis"),
                    rs.getDouble("t"),
                    rs.getDouble("u"),
                    rs.getDouble("ua"),
                    rs.getDouble("p")
                });
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    /** Simpan / update batch nilai. rows: { siswa_id, tugas, uts, uas, praktik } */
    public boolean saveNilaiBatch(int kelasId, int mapelId, int inputBy, List<Object[]> rows) {
        String sql = "INSERT INTO nilai(siswa_id,kelas_id,mapel_id,tugas,uts,uas,praktik,input_by) VALUES(?,?,?,?,?,?,?,?) " +
                     "ON DUPLICATE KEY UPDATE tugas=VALUES(tugas),uts=VALUES(uts),uas=VALUES(uas),praktik=VALUES(praktik),input_by=VALUES(input_by)";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            for (Object[] row : rows) {
                ps.setInt(1, (int) row[0]);
                ps.setInt(2, kelasId);
                ps.setInt(3, mapelId);
                ps.setDouble(4, (double) row[1]);
                ps.setDouble(5, (double) row[2]);
                ps.setDouble(6, (double) row[3]);
                ps.setDouble(7, (double) row[4]);
                ps.setInt(8, inputBy);
                ps.addBatch();
            }
            ps.executeBatch();
            return true;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    /**
     * Ambil semua nilai siswa di satu kelas (semua mapel) untuk rapot.
     * Return: { mapel, kkm, tugas, uts, uas, nilai_akhir }
     */
    public List<Object[]> getRapotBySiswaKelas(int siswaId, int kelasId) {
        List<Object[]> list = new ArrayList<>();
        String sql = "SELECT mp.nama_mapel, mp.kkm, " +
                     "  COALESCE(n.tugas,0) t, COALESCE(n.uts,0) u, COALESCE(n.uas,0) ua, " +
                     "  ROUND(COALESCE(n.tugas,0)*0.3 + COALESCE(n.uts,0)*0.3 + COALESCE(n.uas,0)*0.4, 2) akhir " +
                     "FROM mata_pelajaran mp " +
                     "LEFT JOIN nilai n ON n.mapel_id=mp.mapel_id AND n.siswa_id=? AND n.kelas_id=? " +
                     "ORDER BY mp.nama_mapel";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, siswaId);
            ps.setInt(2, kelasId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Object[]{
                    rs.getString("nama_mapel"),
                    rs.getDouble("kkm"),
                    rs.getDouble("t"),
                    rs.getDouble("u"),
                    rs.getDouble("ua"),
                    rs.getDouble("akhir")
                });
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    /**
     * Daftar nilai seluruh siswa (semua mapel rata-rata) untuk laporan daftar nilai.
     */
    public List<Object[]> getDaftarNilaiKelas(int kelasId) {
        List<Object[]> list = new ArrayList<>();
        String sql = "SELECT s.nama_lengkap, s.nis, " +
                     "  ROUND(AVG(n.tugas*0.3 + n.uts*0.3 + n.uas*0.4), 2) AS rata " +
                     "FROM siswa s " +
                     "JOIN kelas_siswa ks ON s.siswa_id=ks.siswa_id " +
                     "LEFT JOIN nilai n ON n.siswa_id=s.siswa_id AND n.kelas_id=? " +
                     "WHERE ks.kelas_id=? AND s.status_aktif=1 " +
                     "GROUP BY s.siswa_id ORDER BY rata DESC";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, kelasId);
            ps.setInt(2, kelasId);
            ResultSet rs = ps.executeQuery();
            int rank = 1;
            while (rs.next()) {
                list.add(new Object[]{
                    rank++,
                    rs.getString("nama_lengkap"),
                    rs.getString("nis"),
                    rs.getDouble("rata")
                });
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }
}
