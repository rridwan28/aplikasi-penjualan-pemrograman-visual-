/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package config;
import java.sql.*;
/**
 *
 * @author Melindaa
 */
public class koneksi {
    private Connection koneksi;
    public Connection connect(){
    try{
        Class.forName("com.mysql.jdbc.Driver");
        System.out.println("berhasil tersambung");
    }
    catch(ClassNotFoundException ex){
        System.out.println("gagal tersambung " + ex);    
    }
    String url = "jdbc:mysql://localhost/penjualan";
    try{
        koneksi = DriverManager.getConnection(url,"root","");
        System.out.println("berhasil tersambung ke database");
    }
    catch(SQLException ex){
        System.out.println("gagal tersambung ke database");
    }
    return koneksi;
  }
}
