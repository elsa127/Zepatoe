/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package fitur;

import java.awt.HeadlessException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import zepatoe.Zepatoe;

public class login extends javax.swing.JFrame {

    Zepatoe konek = new Zepatoe();
    Connection conn; 
    
    public login() {
         initComponents();
        setLocationRelativeTo(null); 
        

        conn = konek.getConnection(); 
        if (conn == null) {
            JOptionPane.showMessageDialog(this, "Koneksi ke database gagal!", "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1); 
        }
    }

//    private void CekLogin() {
//    try {
//        if (jusername.getText().trim().isEmpty() || jpass.getPassword().length == 0) {
//            JOptionPane.showMessageDialog(rootPane, "Data Tidak Boleh Kosong", "Pesan", JOptionPane.ERROR_MESSAGE);
//            jusername.requestFocus();
//            HapusLayar();
//        } else {
//            String sql = "SELECT * FROM user WHERE Username = ? AND Password = ?";
//            try (PreparedStatement pst = conn.prepareStatement(sql)) {
//                pst.setString(1, jusername.getText().trim());
//                pst.setString(2, new String(jpass.getPassword())); 
//                ResultSet rs = pst.executeQuery();
//                if (rs.next()) {
//                    this.dispose();
//                    new dashboard().setVisible(true);
//                } else {
//                    JOptionPane.showMessageDialog(rootPane, "Username atau Password Salah", "Pesan", JOptionPane.ERROR_MESSAGE);
//                    HapusLayar();
//                }
//            }
//        }
//    } catch (SQLException e) {
//        e.printStackTrace();
//        JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
//    }
//}
private void onRFIDScanned(String rfidTag) {
    try {
        String sql = "SELECT * FROM user WHERE id_user = ?";
        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, rfidTag);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                this.dispose();
                new dashboard().setVisible(true); // Arahkan ke dashboard
            } else {
                JOptionPane.showMessageDialog(rootPane, "RFID Tidak Dikenali", "Pesan", JOptionPane.ERROR_MESSAGE);
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Kesalahan database: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}
private void HapusLayar() {
    jusername.setText("Username"); // Clear the username field
    jpass.setText("Password"); // Clear the password field
    jusername.setEnabled(true);
    jpass.setEnabled(true);
}

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jusername = new javax.swing.JTextField();
        jlogin = new javax.swing.JButton();
        lupa = new javax.swing.JLabel();
        jpass = new javax.swing.JPasswordField();
        jLabel2 = new javax.swing.JLabel();

        jLabel1.setText("jLabel1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jusername.setText("Username");
        jusername.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jusernameFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jusernameFocusLost(evt);
            }
        });
        jusername.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jusernameActionPerformed(evt);
            }
        });
        jPanel1.add(jusername, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 190, 250, 30));

        jlogin.setText("Login");
        jlogin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jloginActionPerformed(evt);
            }
        });
        jPanel1.add(jlogin, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 340, -1, -1));

        lupa.setForeground(new java.awt.Color(255, 255, 255));
        lupa.setText("Forgot Username/ Password");
        jPanel1.add(lupa, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 290, -1, -1));

        jpass.setText("Password");
        jpass.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jpassFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jpassFocusLost(evt);
            }
        });
        jPanel1.add(jpass, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 250, 250, 30));

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Glogin/Zepatoe (1).png"))); // NOI18N
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 870, 510));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jusernameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jusernameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jusernameActionPerformed

    private void jusernameFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jusernameFocusGained
         String user = jusername.getText();
        if(user.equals("Username")){
            jusername.setText("");
        }
    }//GEN-LAST:event_jusernameFocusGained

    private void jusernameFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jusernameFocusLost
        String user = jusername.getText();
       if(user.equals("")||user.equals("Username")){
            jusername.setText("Username");
        }
    }//GEN-LAST:event_jusernameFocusLost

    private void jpassFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jpassFocusGained
        String pass = new String(jpass.getPassword());
    if (pass.equals("Password")) {
        jpass.setText("");
    }
    }//GEN-LAST:event_jpassFocusGained

    private void jpassFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jpassFocusLost
        String pass = new String(jpass.getPassword());
    if (pass.isEmpty()) {
        jpass.setText("Password");
    }
    }//GEN-LAST:event_jpassFocusLost

    private void jloginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jloginActionPerformed
//        CekLogin(); 
    }//GEN-LAST:event_jloginActionPerformed

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
            java.util.logging.Logger.getLogger(login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new login().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JButton jlogin;
    private javax.swing.JPasswordField jpass;
    private javax.swing.JTextField jusername;
    private javax.swing.JLabel lupa;
    // End of variables declaration//GEN-END:variables
}
