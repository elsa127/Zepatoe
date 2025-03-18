/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package zepatoe;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;

public class Zepatoe {

    public static Connection conn;
    public static Statement st;

     public static Connection koneksi() {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost/zepatoe_loss", "root", "");
            JOptionPane.showMessageDialog(null, "Berhasil");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Gagal terkoneksi Karena " + e.getMessage());
        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null, "Gagal terkoneksi Karena " + e.getMessage());
        }
        return conn;
    }

    public static Connection getConnection() {
        if (conn == null) {
            try {
                return koneksi(); // Memanggil metode koneksiDB jika koneksi belum ada
            } catch (Exception e) {
                Logger.getLogger(Zepatoe.class.getName()).log(Level.SEVERE, null, e);
            }
        }
        return conn;
    }

    public static void main(String[] args) {
        // TODO code application logic here
    }
    
}
