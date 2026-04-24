package dao;

import model.Guru;
import util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GuruDAO {
    //ambil semua guru aktif
    public List<Guru> findAll() {
        List<Guru> list = new ArrayList();
        String sql = "SELECT u.user_id, u.nama_lengkap, u.email, u.status_aktif," +
                     "COALESCE(gp.nip,'') AS nip," +
                     "COALESCE(gp.mata_pelajaran,'') AS mata_pelajaran," +
                     "COALESCE(gp.no_telp'') AS no_telp " +
                     "FROM users u " +
                     "LEFT JOIN guru_profil gp ON gp.user_id = u.user_id " +
                     "WHERE u.role = 'guru' ORDER BY u.nama_lengkap";
        
        try(Connection c = DBConnection.getConnection();
            Statement st = c.createStatement();
            ResultSet rs = st.executeQuery(sql)){
            while (rs.next()) list.add(mapRow(rs));            
        }
        
        catch (SQLException e) {e.printStackTrace();}
        return list;
    }
    
    //ambil profil guru by user_id
    public Guru findByUserId(int userId) {
        String sql = "SELECT u.user_id, u.nama_lengkap, u.email, u.status_aktif, " +
                     "  COALESCE(gp.nip,'') AS nip, " +
                     "  COALESCE(gp.mata_pelajaran,'') AS mata_pelajaran, " +
                     "  COALESCE(gp.no_telp,'') AS no_telp " +
                     "FROM users u " +
                     "LEFT JOIN guru_profil gp ON gp.user_id = u.user_id " +
                     "WHERE u.user_id = ?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapRow(rs);
        } 
        
        catch (SQLException e) { e.printStackTrace(); }
        return null;
    }
    
    //simpan atau update profil guru
    public boolean saveProfil(Guru g) {
        String sql = "INSERT INTO guru_profil(user_id, nip, mata_pelajaran, no_telp) VALUES(?,?,?,?) " +
                     "ON DUPLICATE KEY UPDATE nip=VALUES(nip), mata_pelajaran=VALUES(mata_pelajaran), no_telp=VALUES(no_telp)";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, g.getGuruId());
            ps.setString(2, g.getNip());
            ps.setString(3, g.getMataPelajaran());
            ps.setString(4, g.getNoTelp());
            return ps.executeUpdate() > 0;
        } 
        
        catch (SQLException e) { e.printStackTrace(); 
        return false; }
    }
    
    //simpan status aktif guru
    public boolean updateStatus(int userId, boolean aktif) {
        String sql = "UPDATE users SET status_aktif=? WHERE user_id=?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, aktif ? 1 : 0);
            ps.setInt(2, userId);
            return ps.executeUpdate() > 0;
        } 
        
        catch (SQLException e) { e.printStackTrace(); 
        return false; }
    }
 
    //hitung total kelas yang dipegang guru
    public int countKelasByGuru(int userId) {
        String sql = "SELECT COUNT(DISTINCT kelas_id) FROM kelas_siswa WHERE guru_id = ?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } 
        
        catch (SQLException e) { e.printStackTrace(); }
        return 0;
    }
    
    //hitung total siswa di semua kelas yang guru ajar
    public int countSiswaByGuru(int userId) {
        String sql = "SELECT COUNT(DISTINCT siswa_id) FROM kelas_siswa WHERE guru_id = ?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } 
        catch (SQLException e) { e.printStackTrace(); }
        return 0;
    }
    
    //Ambil jadwal mengajar guru (list kelas yang di-assign ke guru ini).
    //Return: { kelas_id, nama_kelas, tahun_ajaran, semester, total_siswa }
    public List<Object[]> getJadwalByGuru(int userId) {
        List<Object[]> list = new ArrayList<>();
        String sql = "SELECT k.kelas_id, k.nama_kelas, k.tahun_ajaran, k.semester, " +
                     "  COUNT(ks.siswa_id) AS total_siswa " +
                     "FROM kelas k " +
                     "JOIN kelas_siswa ks ON ks.kelas_id = k.kelas_id " +
                     "WHERE ks.guru_id = ? " +
                     "GROUP BY k.kelas_id ORDER BY k.nama_kelas";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, userId);
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
        } 
        
        catch (SQLException e) { e.printStackTrace(); }
        return list;
    }
    
    private Guru mapRow(ResultSet rs) throws SQLException {
        Guru g = new Guru();
        g.setGuruId(rs.getInt("user_id"));
        g.setNamaLengkap(rs.getString("nama_lengkap"));
        g.setEmail(rs.getString("email"));
        g.setStatusAktif(rs.getInt("status_aktif") == 1);
        g.setNip(rs.getString("nip"));
        g.setMataPelajaran(rs.getString("mata_pelajaran"));
        g.setNoTelp(rs.getString("no_telp"));
        return g;
    }
    
}
