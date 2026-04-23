package dao;

import model.Kelas;
import util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * KelasDAO - Data Access Object untuk Kelas
 */
public class KelasDAO {

    public List<Kelas> findAll() {
        List<Kelas> list = new ArrayList<>();
        String sql = "SELECT k.*, u.nama_lengkap AS nama_wali " +
                     "FROM kelas k LEFT JOIN users u ON k.wali_kelas_id=u.user_id " +
                     "ORDER BY k.nama_kelas";
        try (Connection c = DBConnection.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    public Kelas findById(int kelasId) {
        String sql = "SELECT k.*, u.nama_lengkap AS nama_wali " +
                     "FROM kelas k LEFT JOIN users u ON k.wali_kelas_id=u.user_id " +
                     "WHERE k.kelas_id=?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, kelasId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapRow(rs);
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    public boolean insert(Kelas k) {
        String sql = "INSERT INTO kelas(nama_kelas,tahun_ajaran,semester,wali_kelas_id) VALUES(?,?,?,?)";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, k.getNamaKelas());
            ps.setString(2, k.getTahunAjaran());
            ps.setString(3, k.getSemester());
            ps.setInt(4, k.getWaliKelasId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    public boolean update(Kelas k) {
        String sql = "UPDATE kelas SET nama_kelas=?,tahun_ajaran=?,semester=?,wali_kelas_id=? WHERE kelas_id=?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, k.getNamaKelas());
            ps.setString(2, k.getTahunAjaran());
            ps.setString(3, k.getSemester());
            ps.setInt(4, k.getWaliKelasId());
            ps.setInt(5, k.getKelasId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    public boolean delete(int kelasId) {
        String sql = "DELETE FROM kelas WHERE kelas_id=?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, kelasId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    private Kelas mapRow(ResultSet rs) throws SQLException {
        Kelas k = new Kelas();
        k.setKelasId(rs.getInt("kelas_id"));
        k.setNamaKelas(rs.getString("nama_kelas"));
        k.setTahunAjaran(rs.getString("tahun_ajaran"));
        k.setSemester(rs.getString("semester"));
        k.setWaliKelasId(rs.getInt("wali_kelas_id"));
        k.setNamaWaliKelas(rs.getString("nama_wali"));
        return k;
    }
}
