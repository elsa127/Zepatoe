/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package fitur;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import zepatoe.Zepatoe;
/**
 *
 * @author LENOVO
 */
public class laporan_pembelian_new extends javax.swing.JFrame {
    private Connection conn;

    /**
     * Creates new form laporan_pembelian_new
     */
    public laporan_pembelian_new() {
        initComponents();
        conn = Zepatoe.getConnection(); // pastikan Zepatoe memiliki method getConnection()
        getData();
    }
    private void getData() {
        DefaultTableModel model = (DefaultTableModel) tabellaporan.getModel();
        model.setRowCount(0);
        DecimalFormat df = new DecimalFormat("#,###.##");

        try {
            String sql = "SELECT id_transaksi, id_user, tanggal_transaksi, jumlah, id_barang, jenis_barang, merk_barang, size, harga_beli FROM pembelian";
            PreparedStatement st = conn.prepareStatement(sql);
            ResultSet rs = st.executeQuery();

            BigDecimal totalPengeluaran = BigDecimal.ZERO;

            model.setColumnIdentifiers(new Object[]{
                "ID Transaksi", "ID User", "Tanggal", "ID Barang",
                "Jenis", "Merk", "Ukuran", "Jumlah", "Harga Beli", "Total Harga"
            });

            while (rs.next()) {
                String idTransaksi = rs.getString("id_transaksi");
                String idUser = rs.getString("id_user");
                String tanggal = rs.getString("tanggal_transaksi");
                String idBarang = rs.getString("id_barang");
                String jenis = rs.getString("jenis_barang");
                String merk = rs.getString("merk_barang");
                String size = rs.getString("size");
                int jumlah = rs.getInt("jumlah");
                BigDecimal hargaBeli = rs.getBigDecimal("harga_beli");
                BigDecimal total = hargaBeli.multiply(new BigDecimal(jumlah));

                model.addRow(new Object[]{
                    idTransaksi, idUser, tanggal, idBarang,
                    jenis, merk, size, jumlah,
                    df.format(hargaBeli), df.format(total)
                });

                totalPengeluaran = totalPengeluaran.add(total);
            }

            totallabel.setText("Total Pengeluaran: Rp " + df.format(totalPengeluaran));

            rs.close();
            st.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal mengambil data: " + e.getMessage());
        }
    }

    private void tampilkanLaporan() {
        DefaultTableModel model = (DefaultTableModel) tabellaporan.getModel();
        model.setRowCount(0);
        DecimalFormat df = new DecimalFormat("#,###.##");

        try {
            java.util.Date startDate = ctanggalawal.getDate();
            java.util.Date endDate = ctanggalakhir.getDate();

            if (startDate == null) {
                JOptionPane.showMessageDialog(this, "Tanggal awal harus diisi.");
                return;
            }

            if (endDate == null) {
                endDate = startDate;
            }

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String tanggalMulai = sdf.format(startDate);
            String tanggalAkhir = sdf.format(endDate);

            String sql = "SELECT id_transaksi, id_user, tanggal_transaksi, jumlah, id_barang, jenis_barang, merk_barang, size, harga_beli FROM pembelian WHERE tanggal_transaksi BETWEEN ? AND ?";
            PreparedStatement st = conn.prepareStatement(sql);
            st.setString(1, tanggalMulai);
            st.setString(2, tanggalAkhir);

            ResultSet rs = st.executeQuery();
            BigDecimal totalPengeluaran = BigDecimal.ZERO;

            model.setColumnIdentifiers(new Object[]{
                "ID Transaksi", "ID User", "Tanggal", "ID Barang",
                "Jenis", "Merk", "Ukuran", "Jumlah", "Harga Beli", "Total Harga"
            });

            while (rs.next()) {
                String idTransaksi = rs.getString("id_transaksi");
                String idUser = rs.getString("id_user");
                String tanggal = rs.getString("tanggal_transaksi");
                String idBarang = rs.getString("id_barang");
                String jenis = rs.getString("jenis_barang");
                String merk = rs.getString("merk_barang");
                String size = rs.getString("size");
                int jumlah = rs.getInt("jumlah");
                BigDecimal hargaBeli = rs.getBigDecimal("harga_beli");
                BigDecimal total = hargaBeli.multiply(new BigDecimal(jumlah));

                model.addRow(new Object[]{
                    idTransaksi, idUser, tanggal, idBarang,
                    jenis, merk, size, jumlah,
                    df.format(hargaBeli), df.format(total)
                });

                totalPengeluaran = totalPengeluaran.add(total);
            }

            totallabel.setText("Total Pengeluaran: Rp " + df.format(totalPengeluaran));

            rs.close();
            st.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal memfilter data: " + e.getMessage());
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
        tombol = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        tabellaporan.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "id transaksi", "id user", "tanggal transaksi", "jumlah", "id barang", "jenis barang", "merk barang", "size", "harga", "harga beli"
            }
        ));
        jScrollPane1.setViewportView(tabellaporan);

        totallabel.setText("jLabel1");

        tombol.setText("jButton1");
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
                .addContainerGap(65, Short.MAX_VALUE)
                .addComponent(totallabel, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(50, 50, 50)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 375, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(81, 81, 81))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(ctanggalawal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(106, 106, 106)
                        .addComponent(ctanggalakhir, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(tombol)
                        .addGap(41, 41, 41))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(96, 96, 96)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(ctanggalawal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(ctanggalakhir, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(tombol))
                        .addGap(29, 29, 29)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 275, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(173, 173, 173)
                        .addComponent(totallabel)))
                .addContainerGap(57, Short.MAX_VALUE))
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
            java.util.logging.Logger.getLogger(laporan_pembelian_new.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(laporan_pembelian_new.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(laporan_pembelian_new.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(laporan_pembelian_new.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new laporan_pembelian_new().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.toedter.calendar.JDateChooser ctanggalakhir;
    private com.toedter.calendar.JDateChooser ctanggalawal;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tabellaporan;
    private javax.swing.JButton tombol;
    private javax.swing.JLabel totallabel;
    // End of variables declaration//GEN-END:variables
}
