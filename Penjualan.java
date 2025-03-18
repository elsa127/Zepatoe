/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package fitur;
import com.toedter.calendar.JDateChooser;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.sql.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import zepatoe.Zepatoe;

public class Penjualan extends javax.swing.JFrame {
    Zepatoe konek = new Zepatoe();
    Connection conn;
    public ResultSet rs;
    DefaultTableModel model;
    JDateChooser dateChooser;



    public Penjualan() {
        // Setup GUI
        initComponents();
        // Koneksi database
        conn = konek.getConnection();
        if (conn == null) {
            JOptionPane.showMessageDialog(this, "Koneksi ke database gagal!", "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }

        // Setup model untuk JTable
        model = new DefaultTableModel(new String[]{"ID Barang", "Jenis Barang", "Merk Barang", "Size", "Harga", "Jumlah"}, 0);
        tabel_tr.setModel(model);

        idbarang.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent evt) {
                idbarangKeyPressed(evt);
                
            }
        });

        // Tambahkan ActionListener untuk tombol bayar
        Bayar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                savePenjualan();
            }
        });
        id_mem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Ketika Enter ditekan, ambil data member
                String idMember = id_mem.getText();
                if (!idMember.isEmpty()) {
                    loadMemberData(idMember);
                }
            }
        });
    
    }
 private void loadMemberData(String idMember) {
    String query = "SELECT m.nama_member, d.jumlah " +
                   "FROM member m " +
                   "JOIN diskon d ON m.id_member = id_member " +
                   "WHERE m.id_member = ?";

    try (PreparedStatement pst = conn.prepareStatement(query)) {
        pst.setString(1, idMember);
        rs = pst.executeQuery();

        if (rs.next()) {
            // Ambil nama_member dan jumlah dari ResultSet
            String namaMember = rs.getString("nama_member");
            int jumlahValue = rs.getInt("jumlah"); // Ambil sebagai integer

            // Konversi jumlah menjadi persentase
            String diskonValue = jumlahValue + "%"; // Misalnya, jika jumlah adalah 20, hasilnya "20%"

            // Set nilai ke JTextField
            nama_mem.setText(namaMember);
            diskon_b.setText(diskonValue);
        } else {
            JOptionPane.showMessageDialog(this, "Member tidak ditemukan!", "Error", JOptionPane.ERROR_MESSAGE);
            nama_mem.setText("");
            diskon_b.setText("");
        }
    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    } finally {
        // Menutup ResultSet jika diperlukan
        try {
            if (rs != null) rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
 private void loadBarangData(String idBarang) {
    String query = "SELECT * FROM barang WHERE id_barang = ?";
    try (PreparedStatement pst = conn.prepareStatement(query)) {
        pst.setString(1, idBarang);
        rs = pst.executeQuery();
        
        if (rs.next()) {
            // Ambil data dari ResultSet
            String jenisBarang = rs.getString("jenis_barang");
            String merkBarang = rs.getString("merk_barang");
            String size = rs.getString("size");
            int hargaSatuan = rs.getInt("harga_jual");
            int stok = rs.getInt("stok"); // Ambil stok dari database
            int jumlah = 1; // Default jumlah 1

            // Cek apakah barang sudah ada di tabel
            boolean exists = false;
            for (int i = 0; i < model.getRowCount(); i++) {
                if (model.getValueAt(i, 0).equals(idBarang)) {
                    exists = true;
                    int currentQuantity = Integer.parseInt(model.getValueAt(i, 5).toString()); // Ambil jumlah saat ini
                    if (currentQuantity < stok) { // Cek apakah stok masih ada
                        // Tambahkan jumlah barang
                        model.setValueAt(currentQuantity + jumlah, i, 5); // Update jumlah di tabel
                    } else {
                        JOptionPane.showMessageDialog(this, "Stok tidak cukup untuk menambah barang ini!", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    break;
                }
            }

            // Jika barang tidak ada, tambahkan sebagai baris baru
            if (!exists) {
                model.addRow(new Object[]{idBarang, jenisBarang, merkBarang, size, hargaSatuan, jumlah});
            }

            // Panggil subtotal setelah menambahkan barang
            subtotal();
        } 
    } catch (SQLException ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}
private void subtotal() {
    int total = 0; // Inisialisasi total

    // Iterasi melalui setiap baris di tabel_tr
    for (int i = 0; i < model.getRowCount(); i++) {
        // Ambil harga satuan dan jumlah dari setiap baris
        int hargaSatuan = Integer.parseInt(model.getValueAt(i, 4).toString()); // Kolom harga
        int jumlah = Integer.parseInt(model.getValueAt(i, 5).toString()); // Kolom jumlah

        // Hitung subtotal untuk baris ini
        int subtotal = hargaSatuan * jumlah;

        // Tambahkan subtotal ke total keseluruhan
        total += subtotal;
    }

    // Format subtotal dengan DecimalFormat
    DecimalFormat df = new DecimalFormat("#,##0.00");
    sub_tot.setText(df.format(total)); // Misalnya, sub_tot adalah JTextField untuk subtotal
    System.out.println("Subtotal: " + df.format(total)); // Debugging

    // Hitung total setelah diskon
    applyDiscount(total);
}

private void applyDiscount(int subtotal) {
    // Ambil nilai diskon dari JTextField (misalnya diskon_b)
    String diskonStr = diskon_b.getText().trim(); // Misalnya, diskon_b adalah JTextField untuk diskon
    int diskonPersen = 0;

    // Cek apakah diskon valid
    if (!diskonStr.isEmpty()) {
        try {
            // Mengonversi diskon ke integer
            diskonPersen = Integer.parseInt(diskonStr.replace("%", "").trim()); // Menghapus simbol persen jika ada
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Diskon tidak valid!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
    }

    // Hitung total setelah diskon
    double totalAfterDiscount = subtotal - (subtotal * diskonPersen / 100.0);

    // Format total setelah diskon dengan DecimalFormat
    DecimalFormat df = new DecimalFormat("#,##0.00");
    total_b.setText(df.format(totalAfterDiscount)); // Misalnya, total_tot adalah JTextField untuk total
    System.out.println("Total setelah diskon: " + df.format(totalAfterDiscount)); // Debugging
}

   private void savePenjualan() {
    String query = "INSERT INTO penjualan (id_barang, jenis_barang, merk_barang, size, harga_satuan, jumlah, tanggal_transaksi) VALUES (?, ?, ?, ?, ?, ?, ?)";
    try (PreparedStatement pst = conn.prepareStatement(query)) {
        for (int i = 0; i < model.getRowCount(); i++) {
            pst.setString(1, model.getValueAt(i, 0).toString());
            pst.setString(2, model.getValueAt(i, 1).toString());
            pst.setString(3, model.getValueAt(i, 2).toString());
            pst.setString(4, model.getValueAt(i, 3).toString());
            pst.setInt(5, Integer.parseInt(model.getValueAt(i, 4).toString()));
            pst.setInt(6, Integer.parseInt(model.getValueAt(i, 5).toString()));
            
            // Mengambil tanggal dari JDateChooser
            java.util.Date selectedDate = tanggal_tr.getDate();
            if (selectedDate != null) {
                pst.setDate(7, new java.sql.Date(selectedDate.getTime()));
            } else {
                // Jika tanggal tidak dipilih, bisa mengatur default atau menampilkan pesan
                JOptionPane.showMessageDialog(this, "Tanggal transaksi harus dipilih!", "Error", JOptionPane.ERROR_MESSAGE);
                return; // Keluar dari metode jika tanggal tidak valid
            }

            pst.executeUpdate();
        }
        JOptionPane.showMessageDialog(this, "Data penjualan berhasil disimpan!");
        
        // Reset tabel setelah menyimpan
        model.setRowCount(0); 
        
        // Kosongkan semua JTextField
        clearFields();
        
    } catch (SQLException ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}

// Metode untuk mengosongkan semua JTextField
private void clearFields() {
    id_mem.setText("");
    total_b.setText("");
    sub_tot.setText("");
    idbarang.setText(""); // Kosongkan JTextField untuk ID Barang
    jumlah_b.setText(""); // Kosongkan JTextField untuk Jumlah
    nama_mem.setText(""); // Kosongkan JTextField untuk Nama Member
    diskon_b.setText(""); // Kosongkan JTextField untuk Diskon
    tanggal_tr.setDate(null); // Kosongkan JDateChooser
}
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        BG = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabel_tr = new javax.swing.JTable();
        id_mem = new javax.swing.JTextField();
        nama_mem = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        tanggal_tr = new com.toedter.calendar.JDateChooser();
        idbarang = new javax.swing.JTextField();
        jenis_b = new javax.swing.JTextField();
        merk_b = new javax.swing.JTextField();
        size_b = new javax.swing.JTextField();
        harga_b = new javax.swing.JTextField();
        jumlah_b = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        sub_tot = new javax.swing.JTextField();
        diskon_b = new javax.swing.JTextField();
        total_b = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        Bayar = new javax.swing.JButton();
        print = new javax.swing.JButton();
        hapus = new javax.swing.JButton();

        BG.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Glogin/ppp.png"))); // NOI18N

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tabel_tr.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "ID Barang", "Jenis Barang", "Merk", "Ukuran", "Harga", "jumlah"
            }
        ));
        tabel_tr.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabel_trMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tabel_tr);

        jPanel1.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 140, 890, 350));

        id_mem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                id_memActionPerformed(evt);
            }
        });
        jPanel1.add(id_mem, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 70, 170, -1));
        jPanel1.add(nama_mem, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 100, 170, -1));

        jLabel2.setText("Nama Member:");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 100, 90, -1));

        jLabel3.setText("ID Member :");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 70, 80, -1));

        jLabel4.setText("Tanggal:");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 70, -1, -1));

        tanggal_tr.setDateFormatString("yyyy-MM-dd");
        jPanel1.add(tanggal_tr, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 70, 170, -1));

        idbarang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                idbarangActionPerformed(evt);
            }
        });
        idbarang.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                idbarangKeyPressed(evt);
            }
        });
        jPanel1.add(idbarang, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 100, 290, -1));
        jPanel1.add(jenis_b, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 210, 120, -1));
        jPanel1.add(merk_b, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 210, 130, -1));
        jPanel1.add(size_b, new org.netbeans.lib.awtextra.AbsoluteConstraints(740, 210, 140, -1));
        jPanel1.add(harga_b, new org.netbeans.lib.awtextra.AbsoluteConstraints(900, 210, 150, -1));
        jPanel1.add(jumlah_b, new org.netbeans.lib.awtextra.AbsoluteConstraints(940, 240, 100, -1));

        jLabel1.setText("ID Barang");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 100, 60, -1));

        jLabel5.setText("Jenis Barang:");
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 190, -1, -1));

        jLabel6.setText("Merk :");
        jPanel1.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 190, -1, -1));

        jLabel7.setText("Ukuran :");
        jPanel1.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(740, 190, 60, -1));

        jLabel8.setText("Harga :");
        jPanel1.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(900, 190, -1, -1));

        jLabel9.setText("Jumlah:");
        jPanel1.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(1080, 190, -1, -1));
        jPanel1.add(sub_tot, new org.netbeans.lib.awtextra.AbsoluteConstraints(940, 510, 230, -1));
        jPanel1.add(diskon_b, new org.netbeans.lib.awtextra.AbsoluteConstraints(940, 550, 230, -1));
        jPanel1.add(total_b, new org.netbeans.lib.awtextra.AbsoluteConstraints(940, 590, 230, -1));

        jLabel10.setText("Sub Total :");
        jPanel1.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(840, 510, 80, -1));

        jLabel11.setText("Diskon :");
        jPanel1.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(840, 550, 80, -1));

        jLabel12.setText("Total:");
        jPanel1.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(840, 590, 50, -1));

        Bayar.setText("Bayar");
        Bayar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BayarActionPerformed(evt);
            }
        });
        jPanel1.add(Bayar, new org.netbeans.lib.awtextra.AbsoluteConstraints(1010, 630, -1, -1));

        print.setText("Print");
        jPanel1.add(print, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 510, -1, -1));

        hapus.setText("Hapus");
        hapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hapusActionPerformed(evt);
            }
        });
        jPanel1.add(hapus, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 510, -1, -1));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void id_memActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_id_memActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_id_memActionPerformed

    private void idbarangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_idbarangActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_idbarangActionPerformed

    private void BayarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BayarActionPerformed
        // TODO add your handling code here:
        savePenjualan();
    }//GEN-LAST:event_BayarActionPerformed

    private void tabel_trMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabel_trMouseClicked
       
    int selectedRow = tabel_tr.getSelectedRow();
    
// Memastikan baris valid (tidak -1)
if (selectedRow != -1) {
    // Mendapatkan nilai dari kolom tertentu (misalnya, kolom nama barang dan ID barang)
    String idBarang = tabel_tr.getValueAt(selectedRow, 0).toString(); // Kolom pertama untuk ID
    String jenisBarang = tabel_tr.getValueAt(selectedRow, 1).toString();
    String merkBarang = tabel_tr.getValueAt(selectedRow, 2).toString();
    String ukuran = tabel_tr.getValueAt(selectedRow, 3).toString();
    
    // Konversi String ke Integer
    int harga = Integer.parseInt(tabel_tr.getValueAt(selectedRow, 4).toString());
    int jumlah = Integer.parseInt(tabel_tr.getValueAt(selectedRow, 5).toString());

    
} else {
    System.out.println("Tidak ada baris yang dipilih.");
}


    }//GEN-LAST:event_tabel_trMouseClicked

    private void idbarangKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_idbarangKeyPressed
         if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
        String id_Barang = idbarang.getText(); // Ambil teks dari JTextField
        loadBarangData(id_Barang); // Panggil metode untuk mengambil data
        idbarang.setText(""); // Kosongkan JTextField setelah data diambil
    }
    }//GEN-LAST:event_idbarangKeyPressed

    private void hapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hapusActionPerformed
    
    int selectedRow = tabel_tr.getSelectedRow();

    // Memastikan ada baris yang dipilih
    if (selectedRow != -1) {
        // Mengonfirmasi penghapusan kepada pengguna (opsional)
        int confirm = JOptionPane.showConfirmDialog(null, 
                "Apakah Anda yakin ingin menghapus data ini?", 
                "Konfirmasi Hapus", 
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            // Menghapus baris dari model tabel
            DefaultTableModel model = (DefaultTableModel) tabel_tr.getModel();
            model.removeRow(selectedRow);

            // Pesan untuk memberi tahu bahwa data berhasil dihapus (opsional)
            JOptionPane.showMessageDialog(null, "Data berhasil dihapus!");
        }
    } else {
        // Jika tidak ada baris yang dipilih
        JOptionPane.showMessageDialog(null, "Pilih data yang akan dihapus!", 
                "Error", JOptionPane.ERROR_MESSAGE);
    }

    }//GEN-LAST:event_hapusActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Penjualan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Penjualan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Penjualan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Penjualan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Penjualan().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel BG;
    private javax.swing.JButton Bayar;
    private javax.swing.JTextField diskon_b;
    private javax.swing.JButton hapus;
    private javax.swing.JTextField harga_b;
    private javax.swing.JTextField id_mem;
    private javax.swing.JTextField idbarang;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jenis_b;
    private javax.swing.JTextField jumlah_b;
    private javax.swing.JTextField merk_b;
    private javax.swing.JTextField nama_mem;
    private javax.swing.JButton print;
    private javax.swing.JTextField size_b;
    private javax.swing.JTextField sub_tot;
    private javax.swing.JTable tabel_tr;
    private com.toedter.calendar.JDateChooser tanggal_tr;
    private javax.swing.JTextField total_b;
    // End of variables declaration//GEN-END:variables
}
