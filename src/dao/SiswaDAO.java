package dao;

import model.Siswa;
import util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SiswaDAO {

    public List<Siswa> findAll() {
        List<Siswa> list = new ArrayList<>();
        String sql = "SELECT * FROM siswa ORDER BY nama_lengkap";
        try (Connection c = DBConnection.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    public List<Siswa> search(String keyword) {
        List<Siswa> list = new ArrayList<>();
        String sql = "SELECT * FROM siswa WHERE nama_lengkap LIKE ? OR nis LIKE ? ORDER BY nama_lengkap";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            String kw = "%" + keyword + "%";
            ps.setString(1, kw); ps.setString(2, kw);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    /** Siswa yang terdaftar di kelas tertentu. */
    public List<Siswa> findByKelas(int kelasId) {
        List<Siswa> list = new ArrayList<>();
        String sql = "SELECT s.* FROM siswa s " +
                     "JOIN kelas_siswa ks ON s.siswa_id=ks.siswa_id " +
                     "WHERE ks.kelas_id=? AND s.status_aktif=1 ORDER BY s.nama_lengkap";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, kelasId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    public boolean insert(Siswa s) {
        String sql = "INSERT INTO siswa(nis,nama_lengkap,jenis_kelamin,tanggal_lahir,alamat,nama_ortu,no_telp) VALUES(?,?,?,?,?,?,?)";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, s.getNis());
            ps.setString(2, s.getNamaLengkap());
            ps.setString(3, s.getJenisKelamin());
            ps.setString(4, s.getTanggalLahir());
            ps.setString(5, s.getAlamat());
            ps.setString(6, s.getNamaOrtu());
            ps.setString(7, s.getNoTelp());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    public boolean update(Siswa s) {
        String sql = "UPDATE siswa SET nis=?,nama_lengkap=?,jenis_kelamin=?,tanggal_lahir=?,alamat=?,nama_ortu=?,no_telp=?,status_aktif=? WHERE siswa_id=?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, s.getNis());
            ps.setString(2, s.getNamaLengkap());
            ps.setString(3, s.getJenisKelamin());
            ps.setString(4, s.getTanggalLahir());
            ps.setString(5, s.getAlamat());
            ps.setString(6, s.getNamaOrtu());
            ps.setString(7, s.getNoTelp());
            ps.setInt(8, s.isStatusAktif() ? 1 : 0);
            ps.setInt(9, s.getSiswaId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    public boolean delete(int siswaId) {
        String sql = "UPDATE siswa SET status_aktif=0 WHERE siswa_id=?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, siswaId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    private Siswa mapRow(ResultSet rs) throws SQLException {
        Siswa s = new Siswa();
        s.setSiswaId(rs.getInt("siswa_id"));
        s.setNis(rs.getString("nis"));
        s.setNamaLengkap(rs.getString("nama_lengkap"));
        s.setJenisKelamin(rs.getString("jenis_kelamin"));
        s.setTanggalLahir(rs.getString("tanggal_lahir"));
        s.setAlamat(rs.getString("alamat"));
        s.setNamaOrtu(rs.getString("nama_ortu"));
        s.setNoTelp(rs.getString("no_telp"));
        s.setStatusAktif(rs.getInt("status_aktif") == 1);
        return s;
    }
}
