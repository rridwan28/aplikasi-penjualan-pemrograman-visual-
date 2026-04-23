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

    private User mapRow(ResultSet rs) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
}
