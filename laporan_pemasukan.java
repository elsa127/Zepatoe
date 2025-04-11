/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package fitur;

import java.math.BigDecimal;
import zepatoe.Zepatoe;
import java.sql.Connection;
import javax.swing.table.DefaultTableModel;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import javax.swing.JOptionPane;
import com.toedter.calendar.JDateChooser;
import static zepatoe.Zepatoe.conn;
/**
 *
 * @author LENOVO
 */
public class laporan_pemasukan extends javax.swing.JFrame {

    /**
     * Creates new form laporan_pemasukan
     */
    public laporan_pemasukan() {
        initComponents();
        conn = Zepatoe.getConnection();

      getData();
    }
    private void getData() {
    DefaultTableModel model = (DefaultTableModel) jtabelpemasukan.getModel();
    model.setRowCount(0); // Membersihkan tabel sebelum menambahkan data baru

    try {
        // Query SQL untuk mengambil data
        String sql = "SELECT tj.id_transaksi, tj.tanggal, "
                   + "tj.id_barang, b.merk_barang, tj.jumlah, b.harga, tj.username, "
                   + "(b.harga * tj.jumlah) AS total_harga "
                   + "FROM transaksi_jual tj "
                   + "INNER JOIN barang b ON tj.id_barang = b.id_barang "
                   + "INNER JOIN akun a ON tj.username = a.username "
                   + "ORDER BY tj.id_transaksi_jual";

        PreparedStatement st = conn.prepareStatement(sql);

        ResultSet rs = st.executeQuery();

        // Variabel untuk menghitung total pemasukan
        BigDecimal totalPemasukan = BigDecimal.ZERO;

        // Format untuk menampilkan angka dalam format ribuan
        DecimalFormat df = new DecimalFormat("#,###.##");

        // Menentukan header kolom untuk tabel
        Object[] columnNames = {"ID Transaksi", "Tanggal", 
                                "ID Barang", "Nama Barang", "Harga", "Jumlah", "Total Harga"};
        model.setColumnIdentifiers(columnNames);

        // Iterasi hasil query dan tambahkan ke tabel
        while (rs.next()) {
            String id_transaksi_jual = rs.getString("id_transaksi_jual");
            String tgl_jual = rs.getString("tanggal");
            BigDecimal harga = rs.getBigDecimal("harga");
            String id_barang = rs.getString("id_barang");
            String nama_barang = rs.getString("nama_barang");
            int jumlah = rs.getInt("jumlah");

            // Hitung total harga untuk baris ini
            BigDecimal total_harga = harga.multiply(new BigDecimal(jumlah));

            // Tambahkan data ke tabel
            Object[] rowData = {id_transaksi_jual, tgl_jual, 
                                id_barang, nama_barang, df.format(harga), jumlah, df.format(total_harga)};
            model.addRow(rowData);

            // Tambahkan total harga ke total pemasukan
            totalPemasukan = totalPemasukan.add(total_harga);
        }

        // Tampilkan total pemasukan
        if (totalPemasukan.compareTo(BigDecimal.ZERO) > 0) {
            totallabel.setText("Rp " + df.format(totalPemasukan));
        } else {
            totallabel.setText("Rp 0");
        }

        // Tutup koneksi database
        rs.close();
        st.close();

    } catch (SQLException e) {
        // Tampilkan pesan kesalahan jika terjadi masalah dengan SQL
        JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
    }
}

private void tampilkanLaporan() {
    DefaultTableModel model = (DefaultTableModel) jtabelpemasukan.getModel();
    model.setRowCount(0); // Membersihkan tabel sebelum menambahkan data baru

    try {
        // Ambil tanggal awal dan akhir dari komponen JCalendar
        java.util.Date startDate = ctanggalawal.getDate();
        java.util.Date endDate = ctanggalakhir.getDate();

        // Validasi tanggal awal harus diisi
        if (startDate == null) {
            JOptionPane.showMessageDialog(this, "Harap isi tanggal awal!");
            return;
        }

        // Jika tanggal akhir kosong, gunakan tanggal awal sebagai akhir
        if (endDate == null) {
            endDate = startDate;
        }

        // Konversi tanggal ke format SQL (YYYY-MM-DD)
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String tanggalMulai = dateFormat.format(startDate);
        String tanggalAkhir = dateFormat.format(endDate);

        // Debugging: Tampilkan tanggal yang digunakan dalam query
        System.out.println("Tanggal Mulai: " + tanggalMulai);
        System.out.println("Tanggal Akhir: " + tanggalAkhir);

        // Query SQL untuk mengambil data dalam rentang tanggal
        String sql = "SELECT tj.id_transaksi_jual, DATE(tj.tanggal) AS tanggal, "
                   + "tj.id_barang, b.nama_barang, tj.jumlah, b.harga, tj.username, "
                   + "(b.harga * tj.jumlah) AS total_harga "
                   + "FROM transaksi_jual tj "
                   + "INNER JOIN barang b ON tj.id_barang = b.id_barang "
                   + "INNER JOIN akun a ON tj.username = a.username "
                   + "WHERE DATE(tj.tanggal) BETWEEN ? AND ? "
                   + "ORDER BY tj.id_transaksi_jual";

        PreparedStatement st = conn.prepareStatement(sql);
        st.setString(1, tanggalMulai);
        st.setString(2, tanggalAkhir);

        ResultSet rs = st.executeQuery();

        BigDecimal totalPemasukan = BigDecimal.ZERO;
        DecimalFormat df = new DecimalFormat("#,###.##");

        // Header kolom tabel
        Object[] columnNames = {"ID Transaksi", "Tanggal", 
                                "ID Barang", "Nama Barang", "Harga", "Jumlah", "Total Harga"};
        model.setColumnIdentifiers(columnNames);

        // Iterasi hasil query dan tambahkan data ke tabel
        boolean adaData = false;
        while (rs.next()) {
            adaData = true;
            String id_transaksi_jual = rs.getString("id_transaksi_jual");
            String tgl_jual = rs.getString("tanggal");
            BigDecimal harga = rs.getBigDecimal("harga");
            String id_barang = rs.getString("id_barang");
            String nama_barang = rs.getString("nama_barang");
            int jumlah = rs.getInt("jumlah");

            // Hitung total harga untuk baris ini
            BigDecimal total_harga = harga.multiply(new BigDecimal(jumlah));

            // Tambahkan data ke tabel
            Object[] rowData = {id_transaksi_jual, tgl_jual, 
                                id_barang, nama_barang, df.format(harga), jumlah, df.format(total_harga)};
            model.addRow(rowData);

            // Tambahkan total harga ke total pemasukan
            totalPemasukan = totalPemasukan.add(total_harga);
        }

        // Jika tidak ada data, tampilkan pesan
        if (!adaData) {
            JOptionPane.showMessageDialog(this, "Tidak ada data untuk rentang tanggal tersebut.");
        }

        // Tampilkan total pemasukan
        if (totalPemasukan.compareTo(BigDecimal.ZERO) > 0) {
            totallabel.setText("Rp " + df.format(totalPemasukan));
        } else {
            totallabel.setText("Rp 0");
        }

        // Tutup sumber daya
        rs.close();
        st.close();

    } catch (SQLException e) {
        // Tampilkan pesan kesalahan jika ada masalah dengan SQL
        JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
    } catch (Exception e) {
        // Tampilkan pesan kesalahan jika ada masalah umum
        JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
    }
}
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        icon = new javax.swing.JLabel();
        totallabel = new javax.swing.JTextField();
        ctanggalawal = new com.toedter.calendar.JDateChooser();
        ctanggalakhir = new com.toedter.calendar.JDateChooser();
        jScrollPane1 = new javax.swing.JScrollPane();
        jtabelpemasukan = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.add(icon);

        totallabel.setText("jTextField1");
        totallabel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                totallabelActionPerformed(evt);
            }
        });
        jPanel1.add(totallabel);
        jPanel1.add(ctanggalawal);
        jPanel1.add(ctanggalakhir);

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 20, -1, -1));

        jtabelpemasukan.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(jtabelpemasukan);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 90, -1, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void totallabelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_totallabelActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_totallabelActionPerformed

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
            java.util.logging.Logger.getLogger(laporan_pemasukan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(laporan_pemasukan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(laporan_pemasukan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(laporan_pemasukan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new laporan_pemasukan().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.toedter.calendar.JDateChooser ctanggalakhir;
    private com.toedter.calendar.JDateChooser ctanggalawal;
    private javax.swing.JLabel icon;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jtabelpemasukan;
    private javax.swing.JTextField totallabel;
    // End of variables declaration//GEN-END:variables
}
