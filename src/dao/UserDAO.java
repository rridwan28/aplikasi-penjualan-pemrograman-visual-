package dao;
import model.User;
import util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    private String keyword;
    
    public User login (String username, String password){
        String sql = "SELECT * FROM users WHERE username=? AND password=? AND status_aktif=1";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)){
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapRow(rs);                        
        }
        
        catch (SQLException e) {e.printStackTrace();}
        return null;        
    }
    
    //mengambil semua user
    public List<User>findAll(){
        List<User> list = new ArrayList<>();
        String sql      = "SELECT * FROM users ORDER BY role, nama_lengkap";        
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)){
            String kw = "%" + keyword + "%";
            ps.setString(1, kw); ps.setString(2, kw);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapRow(rs));            
        } 
        
        catch (SQLException e) {e.printStackTrace();}
        return list; 
        
    }
    
    //mencari user by keyword atau username
    public List<User> search(String keyword) {
        List<User> list = new ArrayList<>();
        String sql = "SELECT * FROM users WHERE nama_lengkap LIKE ? OR username LIKE ? ORDER BY nama_lengkap";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            String kw = "%" + keyword + "%";
            ps.setString(1, kw); ps.setString(2, kw);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapRow(rs));
        } 
        
        catch (SQLException e) { e.printStackTrace(); }
        return list;
    }
    
    //mengambil semua guru (untuk combo box kelas)
    public List<User> findAllGuru() {
        List<User> list = new ArrayList<>();
        String sql = "SELECT * FROM users WHERE role='guru' AND status_aktif=1 ORDER BY nama_lengkap";
        try (Connection c = DBConnection.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(mapRow(rs));
        } 
        
        catch (SQLException e) { e.printStackTrace(); }
        return list;
    }    

    public boolean insert(User u) {
        String sql = "INSERT INTO users(username,password,nama_lengkap,email,role,status_aktif) VALUES(?,?,?,?,?,?)";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, u.getUsername());
            ps.setString(2, u.getPassword());
            ps.setString(3, u.getNamaLengkap());
            ps.setString(4, u.getEmail());
            ps.setString(5, u.getRole());
            ps.setInt(6, u.isStatusAktif() ? 1 : 0);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }
 
    public boolean update(User u) {
        String sql = "UPDATE users SET username=?,nama_lengkap=?,email=?,role=?,status_aktif=? WHERE user_id=?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, u.getUsername());
            ps.setString(2, u.getNamaLengkap());
            ps.setString(3, u.getEmail());
            ps.setString(4, u.getRole());
            ps.setInt(5, u.isStatusAktif() ? 1 : 0);
            ps.setInt(6, u.getUserId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }
 
    public boolean updatePassword(int userId, String newPass) {
        String sql = "UPDATE users SET password=? WHERE user_id=?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, newPass);
            ps.setInt(2, userId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }
 
    public boolean delete(int userId) {
        String sql = "UPDATE users SET status_aktif=0 WHERE user_id=?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, userId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }
 
    
    private User mapRow(ResultSet rs) throws SQLException {
        User u = new User();
        u.setUserId(rs.getInt("user_id"));
        u.setUsername(rs.getString("username"));
        u.setPassword(rs.getString("password"));
        u.setNamaLengkap(rs.getString("nama_lengkap"));
        u.setEmail(rs.getString("email"));
        u.setRole(rs.getString("role"));
        u.setStatusAktif(rs.getInt("status_aktif") == 1);
        return u;
    }
    
    
}
