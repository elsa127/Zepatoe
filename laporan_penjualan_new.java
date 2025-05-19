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
public class laporan_penjualan_new extends javax.swing.JFrame {
    private Connection conn;
    /**
     * Creates new form laporan_penjualan_new
     */
    public laporan_penjualan_new() {
        initComponents();
        conn = Zepatoe.getConnection(); // Pastikan Zepatoe memiliki metode getConnection()
        getData();
    }
    private void getData() {
        DefaultTableModel model = (DefaultTableModel) tabellaporan.getModel();
        model.setRowCount(0);

        try {
            String sql = "SELECT `id_transaksi`, `tanggal_transaksi`, `id_barang`, `jenis_barang`, `merk_barang`, `size`, `harga_beli`, `harga_satuan`, `diskon`, `harga_total`, `jumlah` FROM `penjualan`";
            PreparedStatement st = conn.prepareStatement(sql);
            ResultSet rs = st.executeQuery();

            BigDecimal totalPemasukan = BigDecimal.ZERO;
            DecimalFormat df = new DecimalFormat("#,###.##");

            Object[] columnNames = {"ID Transaksi", "Tanggal", "ID Barang", "Nama Barang", "Harga Satuan", "Jumlah", "Total Harga"};
            model.setColumnIdentifiers(columnNames);

            while (rs.next()) {
                String id_transaksi = rs.getString("id_transaksi");
                String tanggal_transaksi = rs.getString("tanggal_transaksi");
                String id_barang = rs.getString("id_barang");
                String jenis_barang = rs.getString("jenis_barang");
                String merk_barang = rs.getString("merk_barang");
                String size = rs.getString("size");
                BigDecimal harga_satuan = rs.getBigDecimal("harga_satuan");
                int jumlah = rs.getInt("jumlah");
                BigDecimal harga_total = rs.getBigDecimal("harga_total");

                String nama_barang = jenis_barang + " " + merk_barang + " " + size;

                Object[] rowData = {id_transaksi, tanggal_transaksi, id_barang, nama_barang, df.format(harga_satuan), jumlah, df.format(harga_total)};
                model.addRow(rowData);

                totalPemasukan = totalPemasukan.add(harga_total);
            }

            totallabel.setText("Rp " + df.format(totalPemasukan));
            rs.close();
            st.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void tampilkanLaporan() {
        DefaultTableModel model = (DefaultTableModel) tabellaporan.getModel();
        model.setRowCount(0);

        try {
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

            String sql = "SELECT `id_transaksi`, `tanggal_transaksi`, `id_barang`, `jenis_barang`, `merk_barang`, `size`, `harga_beli`, `harga_satuan`, `diskon`, `harga_total`, `jumlah` FROM `penjualan` WHERE tanggal_transaksi BETWEEN ? AND ?";
            PreparedStatement st = conn.prepareStatement(sql);
            st.setString(1, tanggalMulai);
            st.setString(2, tanggalAkhir);

            ResultSet rs = st.executeQuery();

            BigDecimal totalPemasukan = BigDecimal.ZERO;
            DecimalFormat df = new DecimalFormat("#,###.##");

            
            boolean adaData = false;
            while (rs.next()) {
                adaData = true;
                String id_transaksi = rs.getString("id_transaksi");
                String tanggal_transaksi = rs.getString("tanggal_transaksi");
                String id_barang = rs.getString("id_barang");
                String jenis_barang = rs.getString("jenis_barang");
                String merk_barang = rs.getString("merk_barang");
                String size = rs.getString("size");
                BigDecimal harga_satuan = rs.getBigDecimal("harga_satuan");
                int jumlah = rs.getInt("jumlah");
                BigDecimal harga_total = rs.getBigDecimal("harga_total");

                String nama_barang = jenis_barang + " " + merk_barang + " " + size;

                Object[] rowData = {id_transaksi, tanggal_transaksi, id_barang, nama_barang, df.format(harga_satuan), jumlah, df.format(harga_total)};
                model.addRow(rowData);

                totalPemasukan = totalPemasukan.add(harga_total);
            }

            if (!adaData) {
                JOptionPane.showMessageDialog(this, "Tidak ada data untuk rentang tanggal tersebut.");
            }

            totallabel.setText("Rp " + df.format(totalPemasukan));

            rs.close();
            st.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error SQL: " + e.getMessage());
        } catch (Exception e) {
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
        ctanggalawal = new com.toedter.calendar.JDateChooser();
        ctanggalakhir = new com.toedter.calendar.JDateChooser();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabellaporan = new javax.swing.JTable();
        totallabel = new javax.swing.JLabel();
        tombol = new java.awt.Button();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        tabellaporan.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "id transaksi", "tanggal transaksi", "id barang", "jenis barang", "merk barang", "size", "harga beli", "harga satuan", "diskon", "harga total", "jumlah"
            }
        ));
        jScrollPane1.setViewportView(tabellaporan);

        totallabel.setText("jLabel1");

        tombol.setLabel("button1");
        tombol.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tombolActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(189, 189, 189)
                        .addComponent(ctanggalawal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(53, 53, 53)
                        .addComponent(ctanggalakhir, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(68, 68, 68)
                        .addComponent(totallabel, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 84, Short.MAX_VALUE)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 375, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tombol, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(83, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(52, 52, 52)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tombol, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(ctanggalakhir, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(ctanggalawal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 275, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(62, 62, 62)
                                .addComponent(totallabel)))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tombolActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tombolActionPerformed
        // TODO add your handling code here:
        tampilkanLaporan();
    }//GEN-LAST:event_tombolActionPerformed

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
            java.util.logging.Logger.getLogger(laporan_penjualan_new.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(laporan_penjualan_new.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(laporan_penjualan_new.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(laporan_penjualan_new.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new laporan_penjualan_new().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.toedter.calendar.JDateChooser ctanggalakhir;
    private com.toedter.calendar.JDateChooser ctanggalawal;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tabellaporan;
    private java.awt.Button tombol;
    private javax.swing.JLabel totallabel;
    // End of variables declaration//GEN-END:variables
}
