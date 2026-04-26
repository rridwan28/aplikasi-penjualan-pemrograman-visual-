package dao;

import model.Guru;
import util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GuruDAO {
    
    /** Ambil semua guru aktif. */
    public List<Guru> findAll() {
        List<Guru> list = new ArrayList<>();
        String sql = "SELECT guru_id, nip, nama_lengkap, mata_pelajaran, no_telp, email, status_aktif " +
                     "FROM guru WHERE status_aktif = 1 ORDER BY nama_lengkap";
        try (Connection c = DBConnection.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    /** Ambil guru berdasarkan guru_id. */
    public Guru findById(int guruId) {
        String sql = "SELECT guru_id, nip, nama_lengkap, mata_pelajaran, no_telp, email, status_aktif " +
                     "FROM guru WHERE guru_id = ?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, guruId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapRow(rs);
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }
    
    /** Cari guru berdasarkan nama (untuk matching dengan user login). */
    public Guru findByNamaLengkap(String nama) {
        String sql = "SELECT guru_id, nip, nama_lengkap, mata_pelajaran, no_telp, email, status_aktif " +
                     "FROM guru WHERE nama_lengkap = ? LIMIT 1";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, nama);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapRow(rs);
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }    

    /** Insert guru baru. */
    public boolean insert(Guru g) {
        String sql = "INSERT INTO guru(nip, nama_lengkap, mata_pelajaran, no_telp, email, status_aktif) " +
                     "VALUES(?,?,?,?,?,?)";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, g.getNip());
            ps.setString(2, g.getNamaLengkap());
            ps.setString(3, g.getMataPelajaran());
            ps.setString(4, g.getNoTelp());
            ps.setString(5, g.getEmail());
            ps.setInt(6, g.isStatusAktif() ? 1 : 0);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    /** Update guru. */
    public boolean update(Guru g) {
        String sql = "UPDATE guru SET nip=?, nama_lengkap=?, mata_pelajaran=?, no_telp=?, email=?, status_aktif=? " +
                     "WHERE guru_id=?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, g.getNip());
            ps.setString(2, g.getNamaLengkap());
            ps.setString(3, g.getMataPelajaran());
            ps.setString(4, g.getNoTelp());
            ps.setString(5, g.getEmail());
            ps.setInt(6, g.isStatusAktif() ? 1 : 0);
            ps.setInt(7, g.getGuruId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    /** Delete guru (soft delete via status). */
    public boolean delete(int guruId) {
        String sql = "UPDATE guru SET status_aktif=0 WHERE guru_id=?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, guruId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    /** Hitung total kelas yang dipegang guru. */
    public int countKelasByGuru(int guruId) {
        String sql = "SELECT COUNT(DISTINCT kelas_id) FROM kelas_siswa WHERE guru_id = ?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, guruId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) { e.printStackTrace(); }
        return 0;
    }

    /** Hitung total siswa di semua kelas guru. */
    public int countSiswaByGuru(int guruId) {
        String sql = "SELECT COUNT(DISTINCT siswa_id) FROM kelas_siswa WHERE guru_id = ?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, guruId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) { e.printStackTrace(); }
        return 0;
    }

    /** Ambil jadwal mengajar guru (list kelas yang di-assign). */
    public List<Object[]> getJadwalByGuru(int guruId) {
        List<Object[]> list = new ArrayList<>();
        String sql = "SELECT k.kelas_id, k.nama_kelas, k.tahun_ajaran, k.semester, " +
                     "  COUNT(ks.siswa_id) AS total_siswa " +
                     "FROM kelas k " +
                     "JOIN kelas_siswa ks ON ks.kelas_id = k.kelas_id " +
                     "WHERE ks.guru_id = ? " +
                     "GROUP BY k.kelas_id ORDER BY k.nama_kelas";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, guruId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Object[]{
                    rs.getInt("kelas_id"),
                    rs.getString("nama_kelas"),
                    rs.getString("tahun_ajaran"),
                    rs.getString("semester"),
                    rs.getInt("total_siswa")
                });
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    private Guru mapRow(ResultSet rs) throws SQLException {
        Guru g = new Guru();
        g.setGuruId(rs.getInt("guru_id"));
        g.setNip(rs.getString("nip"));
        g.setNamaLengkap(rs.getString("nama_lengkap"));
        g.setMataPelajaran(rs.getString("mata_pelajaran"));
        g.setNoTelp(rs.getString("no_telp"));
        g.setEmail(rs.getString("email"));
        g.setStatusAktif(rs.getInt("status_aktif") == 1);
        return g;
    }
}