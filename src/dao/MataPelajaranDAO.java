package dao;

import model.MataPelajaran;
import util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * MataPelajaranDAO - Data Access Object untuk Mata Pelajaran
 */
public class MataPelajaranDAO {

    public List<MataPelajaran> findAll() {
        List<MataPelajaran> list = new ArrayList<>();
        String sql = "SELECT * FROM mata_pelajaran ORDER BY nama_mapel";
        try (Connection c = DBConnection.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    public boolean insert(MataPelajaran m) {
        String sql = "INSERT INTO mata_pelajaran(kode_mapel,nama_mapel,kkm,kategori) VALUES(?,?,?,?)";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, m.getKodeMapel());
            ps.setString(2, m.getNamaMapel());
            ps.setDouble(3, m.getKkm());
            ps.setString(4, m.getKategori());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    public boolean update(MataPelajaran m) {
        String sql = "UPDATE mata_pelajaran SET kode_mapel=?,nama_mapel=?,kkm=?,kategori=? WHERE mapel_id=?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, m.getKodeMapel());
            ps.setString(2, m.getNamaMapel());
            ps.setDouble(3, m.getKkm());
            ps.setString(4, m.getKategori());
            ps.setInt(5, m.getMapelId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    public boolean delete(int mapelId) {
        String sql = "DELETE FROM mata_pelajaran WHERE mapel_id=?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, mapelId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    private MataPelajaran mapRow(ResultSet rs) throws SQLException {
        MataPelajaran m = new MataPelajaran();
        m.setMapelId(rs.getInt("mapel_id"));
        m.setKodeMapel(rs.getString("kode_mapel"));
        m.setNamaMapel(rs.getString("nama_mapel"));
        m.setKkm(rs.getDouble("kkm"));
        m.setKategori(rs.getString("kategori"));
        return m;
    }
}
