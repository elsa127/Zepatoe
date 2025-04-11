/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package fitur;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import zepatoe.Zepatoe;
import static zepatoe.Zepatoe.conn;

/**
 *
 * @author LENOVO
 */
public class laporan_pengeluaran extends javax.swing.JFrame {

    /**
     * Creates new form laporan_pengeluaran
     */
    public laporan_pengeluaran() {
        initComponents();
        conn = Zepatoe.getConnection();
        getData();
    }
    private void getData() {
    DefaultTableModel model = (DefaultTableModel) jtabel.getModel();
    model.setRowCount(0); 

    try {
        // SQL Query
        String sql = "SELECT tb.id_transaksi_beli, tb.tanggal, tb.id_suplier, s.nama_suplier, "
                   + "tb.id_barang, b.nama_barang, tb.jumlah, b.harga, "
                   + "(b.harga * tb.jumlah) AS total_harga "
                   + "FROM transaksi_beli tb "
                   + "INNER JOIN barang b ON tb.id_barang = b.id_barang "
                   + "INNER JOIN suplier s ON tb.id_suplier = s.id_suplier "
                   + "ORDER BY tb.id_transaksi_beli";

        PreparedStatement st = conn.prepareStatement(sql);
        ResultSet rs = st.executeQuery();
        BigDecimal totalPengeluaran = BigDecimal.ZERO;
        DecimalFormat df = new DecimalFormat("#,###.##");

        
        Object[] columnNames = {"ID Transaksi Beli", "Tanggal", "ID Suplier", "Nama Suplier", 
                                "ID Barang", "Nama Barang", "Jumlah", "Harga", "Total Harga"};
        model.setColumnIdentifiers(columnNames);

        while (rs.next()) {
            
            String idtransaksibeli = rs.getString("id_transaksi_beli");
            String tanggal = rs.getString("tanggal");
            String idSuplier = rs.getString("id_suplier");
            String namaSuplier = rs.getString("nama_suplier");
            String idBarang = rs.getString("id_barang");
            String namaBarang = rs.getString("nama_barang");
            int jumlah = rs.getInt("jumlah");
            BigDecimal harga = rs.getBigDecimal("harga");

            
            BigDecimal total_harga = harga.multiply(new BigDecimal(jumlah));

            
            Object[] rowData = {idtransaksibeli, tanggal, idSuplier, namaSuplier, 
                                idBarang, namaBarang, jumlah, df.format(harga), df.format(total_harga)};
            model.addRow(rowData);
            totalPengeluaran = totalPengeluaran.add(total_harga);
        }
        if (totalPengeluaran.compareTo(BigDecimal.ZERO) > 0) {
                jlabelpengeluaran.setText("Rp " + df.format(totalPengeluaran));
            } else {
                jlabelpengeluaran.setText("Rp 0");
            }

        rs.close();
        st.close();

    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
    }
}



   private void tampilkanLaporan() {
    DefaultTableModel model = (DefaultTableModel) jtabel.getModel();
    model.setRowCount(0);  // Membersihkan tabel

    try {
        // Mendapatkan tanggal awal dan akhir dari komponen JCalendar
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

        // Menyusun query SQL
        String sql = "SELECT tb.id_transaksi_beli, tb.tanggal, tb.id_suplier, s.nama_suplier, "
                   + "tb.id_barang, b.nama_barang, tb.jumlah, b.harga, "
                   + "(b.harga * tb.jumlah) AS total_harga "
                   + "FROM transaksi_beli tb "
                   + "INNER JOIN barang b ON tb.id_barang = b.id_barang "
                   + "INNER JOIN suplier s ON tb.id_suplier = s.id_suplier "
                   + "WHERE DATE(tb.tanggal) BETWEEN ? AND ? "
                   + "ORDER BY tb.id_transaksi_beli";

        PreparedStatement st = conn.prepareStatement(sql);
        st.setString(1, tanggalMulai); 
        st.setString(2, tanggalAkhir); 

        ResultSet rs = st.executeQuery();
        BigDecimal totalPengeluaran = BigDecimal.ZERO;
        DecimalFormat df = new DecimalFormat("#,###.##");

        // Menetapkan nama kolom tabel
        Object[] columnNames = {"ID Transaksi Beli", "Tanggal", "ID Suplier", "Nama Suplier", 
                                "ID Barang", "Nama Barang", "Jumlah", "Harga", "Total Harga"};
        model.setColumnIdentifiers(columnNames);

        // Mengolah hasil query
        while (rs.next()) {
            String idtransaksibeli = rs.getString("id_transaksi_beli");
            String tanggal = rs.getString("tanggal");
            String idSuplier = rs.getString("id_suplier");
            String namaSuplier = rs.getString("nama_suplier");
            String idBarang = rs.getString("id_barang");
            String namaBarang = rs.getString("nama_barang");
            int jumlah = rs.getInt("jumlah");
            BigDecimal harga = rs.getBigDecimal("harga");
            BigDecimal total_harga = harga.multiply(new BigDecimal(jumlah));

            // Menambahkan data baris ke tabel
            Object[] rowData = {idtransaksibeli, tanggal, idSuplier, namaSuplier, 
                                idBarang, namaBarang, jumlah, df.format(harga), df.format(total_harga)};
            model.addRow(rowData);

            // Menambah total pengeluaran
            totalPengeluaran = totalPengeluaran.add(total_harga);
        }

        // Menampilkan total pengeluaran
        if (totalPengeluaran.compareTo(BigDecimal.ZERO) > 0) {
            jlabelpengeluaran.setText("Total Pengeluaran: Rp " + df.format(totalPengeluaran));
        } else {
            jlabelpengeluaran.setText("Total Pengeluaran: Rp 0");
        }

        // Menutup ResultSet dan PreparedStatement
        rs.close();
        st.close();

    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
    }
}
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jlabelpengeluaran = new javax.swing.JLabel();
        ctanggalawal = new com.toedter.calendar.JDateChooser();
        ctanggalakhir = new com.toedter.calendar.JDateChooser();
        jScrollPane1 = new javax.swing.JScrollPane();
        jtabel = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel2.setText("jLabel2");

        jlabelpengeluaran.setText("jLabel1");

        jtabel.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane1.setViewportView(jtabel);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(223, 223, 223)
                .addComponent(jlabelpengeluaran, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(212, 212, 212)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 297, Short.MAX_VALUE)
                .addComponent(ctanggalawal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(266, 266, 266)
                .addComponent(ctanggalakhir, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(289, 289, 289))
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGap(285, 285, 285)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(285, Short.MAX_VALUE)))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(80, 80, 80)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(ctanggalakhir, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ctanggalawal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(17, 17, 17)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jlabelpengeluaran))
                .addContainerGap(488, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGap(98, 98, 98)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(98, Short.MAX_VALUE)))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

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
            java.util.logging.Logger.getLogger(laporan_pengeluaran.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(laporan_pengeluaran.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(laporan_pengeluaran.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(laporan_pengeluaran.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new laporan_pengeluaran().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.toedter.calendar.JDateChooser ctanggalakhir;
    private com.toedter.calendar.JDateChooser ctanggalawal;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel jlabelpengeluaran;
    private javax.swing.JTable jtabel;
    // End of variables declaration//GEN-END:variables
}
