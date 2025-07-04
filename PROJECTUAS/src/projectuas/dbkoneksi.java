/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package projectuas;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author SILVIA DIAN LESTARI
 * UJIAN AKHIR SEMESTER OOP
 */
public class dbkoneksi {
    static String JDBC_DRIVER="com.mysql.cj.jdbc.Driver";
    static String DB_URL="jdbc:mysql://localhost/mytugas";
    static String DB_USER="root";
    static String DB_PASS="";
    static Connection konek;
    
    static public Connection koneksi(){
        try{
            Class.forName(JDBC_DRIVER);
            return DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
        }catch(ClassNotFoundException | SQLException e){
            System.out.println("Terjadi Kesalahan Koneksi ke Database");
        }
        return null;
    } 
}
