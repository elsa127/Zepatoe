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
import java.text.DecimalFormatSymbols;
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
        String sql = "SELECT id_transaksi, tanggal_transaksi, "
                   + "id_barang, merk_barang, jenis_barang, size, jumlah, harga_satuan, "
                   + "(harga_satuan * jumlah) AS total_harga "
                   + "FROM penjualan ";
     

        PreparedStatement st = conn.prepareStatement(sql);

        ResultSet rs = st.executeQuery();

        // Variabel untuk menghitung total pemasukan
        BigDecimal totalPemasukan = BigDecimal.ZERO;

        // Format untuk menampilkan angka dalam format ribuan
        DecimalFormat df = new DecimalFormat("#,###.##");

        // Menentukan header kolom untuk tabel
        Object[] columnNames = {"id_transaksi", "tanggal_transaksi", 
                                "id_Barang", "merk_barang","jenis_barang","size", "harga_satuan", "jumlah"};
        model.setColumnIdentifiers(columnNames);

        // Iterasi hasil query
        while (rs.next()) {
            String id_transaksi = rs.getString("id_transaksi");
            String tanggal = rs.getString("tanggal_transaksi");
            String id_barang = rs.getString("id_barang");
            String merk_barang = rs.getString("merk_barang");
            String jenis_barang = rs.getString("jenis_barang");
            String size = rs.getString("size");
            BigDecimal harga_satuan = rs.getBigDecimal("harga_satuan");
            int jumlah = rs.getInt("jumlah");

            // Hitung total harga untuk baris ini
            BigDecimal total_harga = harga_satuan.multiply(BigDecimal.valueOf(jumlah));

            // Tambahkan data ke tabel
            Object[] rowData = {
                id_transaksi, tanggal, id_barang, merk_barang, jenis_barang, size,
                "Rp " + df.format(harga_satuan),
                jumlah,
                "Rp " + df.format(total_harga)
            };
            model.addRow(rowData);

            // Tambahkan ke total pemasukan
            totalPemasukan = totalPemasukan.add(total_harga);
        }

        // Tampilkan total pemasukan
        if (totalPemasukan.compareTo(BigDecimal.ZERO) > 0) {
            totallabel.setText("Total Pemasukan: Rp " + df.format(totalPemasukan));
        } else {
            totallabel.setText("Total Pemasukan: Rp 0");
        }

        // Tutup koneksi
        rs.close();
        st.close();

    } catch (SQLException e) {
        // Tampilkan error jika terjadi
        JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
    }
}
private void tampilkanLaporan() {
    DefaultTableModel model = (DefaultTableModel) jtabelpemasukan.getModel();
    model.setRowCount(0); // Membersihkan tabel sebelum menambahkan data baru

    try {
        // Ambil tanggal awal dan akhir dari JCalendar
        java.util.Date startDate = ctanggalawal.getDate();
        java.util.Date endDate = ctanggalakhir.getDate();

        if (startDate == null) {
            JOptionPane.showMessageDialog(this, "Harap isi tanggal awal!");
            return;
        }

        if (endDate == null) {
            endDate = startDate;
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String tanggalMulai = dateFormat.format(startDate);
        String tanggalAkhir = dateFormat.format(endDate);

        // Format angka Indonesia
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator('.');
        symbols.setDecimalSeparator(',');
        DecimalFormat df = new DecimalFormat("#,##0.##", symbols);

        // SQL Query dengan join
        String sql = "SELECT id_transaksi, tanggal AS tanggal_transaksi, "
                   + "id_barang, merk_barang, jenis_barang, size, "
                   + "harga AS harga_satuan, jumlah, "
                   + "(harga * jumlah) AS total_harga "
                   + "FROM penjualan tj "
                   + "WHERE DATE(tj.tanggal) BETWEEN ? AND ? "
                   + "ORDER BY tj.id_transaksi";

        PreparedStatement st = conn.prepareStatement(sql);
        st.setString(1, tanggalMulai);
        st.setString(2, tanggalAkhir);

        ResultSet rs = st.executeQuery();

        BigDecimal totalPemasukan = BigDecimal.ZERO;
        boolean adaData = false;

        Object[] columnNames = {"ID Transaksi", "Tanggal_transaksi", 
                                "ID Barang","jenis_barang", "Merk Barang", "Harga_Satuan", "Jumlah", "Total Harga"};
        model.setColumnIdentifiers(columnNames);

        while (rs.next()) {
            adaData = true;
            String id_transaksi_jual = rs.getString("id_transaksi");
            String tgl_jual = rs.getString("tanggal_transaksi");
            String id_barang = rs.getString("id_barang");
            String merk_barang = rs.getString("merk_barang");
            BigDecimal harga_satuan = rs.getBigDecimal("harga_satuan");
            int jumlah = rs.getInt("jumlah");

            BigDecimal total_harga = harga_satuan.multiply(BigDecimal.valueOf(jumlah));

            Object[] rowData = {
                id_transaksi_jual, tgl_jual,
                id_barang, merk_barang,
                "Rp " + df.format(harga_satuan),
                jumlah,
                "Rp " + df.format(total_harga)
            };
            model.addRow(rowData);

            totalPemasukan = totalPemasukan.add(total_harga);
        }

        if (!adaData) {
            JOptionPane.showMessageDialog(this, "Tidak ada data untuk rentang tanggal tersebut.");
        }

        if (totalPemasukan.compareTo(BigDecimal.ZERO) > 0) {
            totallabel.setText("Total Pemasukan: Rp " + df.format(totalPemasukan));
        } else {
            totallabel.setText("Total Pemasukan: Rp 0");
        }

        rs.close();
        st.close();

    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Error SQL: " + e.getMessage());
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
    }
}

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
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
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "id_transaksi", "tanggal_transaksi", "id_barang", "jenis_barang", "merk_barang", "size", "harga_satuan", "jumlah"
            }
        ));
        jScrollPane1.setViewportView(jtabelpemasukan);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 90, -1, -1));

        pack();
    }// </editor-fold>                        

    private void totallabelActionPerformed(java.awt.event.ActionEvent evt) {                                           
        // TODO add your handling code here:
    }                                          

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

    // Variables declaration - do not modify                     
    private com.toedter.calendar.JDateChooser ctanggalakhir;
    private com.toedter.calendar.JDateChooser ctanggalawal;
    private javax.swing.JLabel icon;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jtabelpemasukan;
    private javax.swing.JTextField totallabel;
    // End of variables declaration                   
}
