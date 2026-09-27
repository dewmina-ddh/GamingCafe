package gamingcafe;

import java.awt.Image;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class ChangeUserDetails extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(ChangeUserDetails.class.getName());

    User user;
    private int currentLogId = 0;
    DBConnection db = new DBConnection();
    PreparedStatement pst;
    ResultSet rs;

    public ChangeUserDetails() {
        initComponents();
    }

    public ChangeUserDetails(User user) {
        this(user, 0);
    }

    public ChangeUserDetails(User user, int logId) {
        initComponents();
        this.user = user;
        this.currentLogId = logId;
        if (user != null && user.getUserName() != null) {
            txtUName.setText(user.getUserName());
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtUName = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtPass = new javax.swing.JPasswordField();
        jLabel4 = new javax.swing.JLabel();
        txtConfirm = new javax.swing.JPasswordField();
        jLabel5 = new javax.swing.JLabel();
        btnFinish = new javax.swing.JButton();
        btnSkip = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("First Time Login - Gaming Cafe");
        setMinimumSize(new java.awt.Dimension(430, 360));
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(11, 19, 43));
        jLabel1.setText("First Time Login Setup");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 15, 370, 28));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(51, 51, 51));
        jLabel2.setText("User Name");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 55, 130, 30));

        txtUName.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        jPanel1.add(txtUName, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 55, 235, 30));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(51, 51, 51));
        jLabel3.setText("New Password");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 100, 130, 30));

        txtPass.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        jPanel1.add(txtPass, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 100, 235, 30));

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(51, 51, 51));
        jLabel4.setText("Confirm Password");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 145, 130, 30));

        txtConfirm.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        jPanel1.add(txtConfirm, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 145, 235, 30));

        jLabel5.setFont(new java.awt.Font("Segoe UI", 2, 11)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 51, 0));
        jLabel5.setText("* You can customize credentials or click Skip to keep default.");
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 185, 370, 20));

        btnFinish.setBackground(new java.awt.Color(11, 19, 43));
        btnFinish.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        btnFinish.setForeground(new java.awt.Color(255, 255, 255));
        btnFinish.setText("Finish & Login");
        btnFinish.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnFinish.addActionListener(this::btnFinishActionPerformed);
        jPanel1.add(btnFinish, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 220, 180, 36));

        btnSkip.setBackground(new java.awt.Color(100, 116, 139));
        btnSkip.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        btnSkip.setForeground(new java.awt.Color(255, 255, 255));
        btnSkip.setText("Skip & Continue");
        btnSkip.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSkip.addActionListener(this::btnSkipActionPerformed);
        jPanel1.add(btnSkip, new org.netbeans.lib.awtextra.AbsoluteConstraints(215, 220, 180, 36));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 430, 360));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    // User name සහ Password update කර Login වීම
    private void btnFinishActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFinishActionPerformed
        String newUName = txtUName.getText().trim();
        String newPass = new String(txtPass.getPassword()).trim();
        String confirmPass = new String(txtConfirm.getPassword()).trim();

        if (newUName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a Username!", "Warning", JOptionPane.WARNING_MESSAGE);
            txtUName.requestFocus();
            return;
        }

        if (newPass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a Password!", "Warning", JOptionPane.WARNING_MESSAGE);
            txtPass.requestFocus();
            return;
        }

        if (confirmPass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please confirm your Password!", "Warning", JOptionPane.WARNING_MESSAGE);
            txtConfirm.requestFocus();
            return;
        }

        if (!newPass.equals(confirmPass)) {
            JOptionPane.showMessageDialog(this, "Passwords do not match! Please re-enter.", "Warning", JOptionPane.WARNING_MESSAGE);
            txtConfirm.requestFocus();
            return;
        }

        if (newPass.length() < 4) {
            JOptionPane.showMessageDialog(this, "Password must be at least 4 characters long!", "Warning", JOptionPane.WARNING_MESSAGE);
            txtPass.requestFocus();
            return;
        }

        try {
            // වෙනත් user කෙනෙකු මෙම username එක භාවිතා කර ඇත්දැයි බැලීම
            if (user != null && !newUName.equalsIgnoreCase(user.getUserName())) {
                pst = db.con.prepareStatement("SELECT emp_no FROM user WHERE username = ? AND emp_no != ?");
                pst.setString(1, newUName);
                pst.setString(2, user.getEmpNo() != null ? user.getEmpNo() : "");
                rs = pst.executeQuery();
                if (rs.next()) {
                    JOptionPane.showMessageDialog(this, "This username is already taken! Please choose another.", "Warning", JOptionPane.WARNING_MESSAGE);
                    pst.close();
                    rs.close();
                    txtUName.requestFocus();
                    return;
                }
                pst.close();
                rs.close();
            }

            String hPass = hashPassword(newPass);
            String empNo = user != null ? user.getEmpNo() : "";

            pst = db.con.prepareStatement("UPDATE user SET username = ?, password = ? WHERE emp_no = ?");
            pst.setString(1, newUName);
            pst.setString(2, hPass);
            pst.setString(3, empNo);

            int updated = pst.executeUpdate();
            pst.close();

            if (updated > 0) {
                if (user != null) {
                    user.setUserName(newUName);
                    user.setPassword(hPass);
                }
                JOptionPane.showMessageDialog(this, "Username & Password updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);

                // Dashboard විවෘත කිරීම
                Dash dashboard = new Dash(user, currentLogId);
                dashboard.setVisible(true);
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update details. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnFinishActionPerformed

    // වෙනස් නොකර Default විස්තර සමඟ Login වීම (Skip)
    private void btnSkipActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSkipActionPerformed
        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to continue with default credentials?\nYou can change them later from your profile.",
            "Skip First Time Setup",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );

        if (confirm == JOptionPane.YES_OPTION) {
            Dash dashboard = new Dash(user, currentLogId);
            dashboard.setVisible(true);
            this.dispose();
        }
    }//GEN-LAST:event_btnSkipActionPerformed

    // Password Hash (SHA-256)
    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = md.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();

            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException ex) {
            System.getLogger(ChangeUserDetails.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            return null;
        }
    }

    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(() -> new ChangeUserDetails().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnFinish;
    private javax.swing.JButton btnSkip;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPasswordField txtConfirm;
    private javax.swing.JPasswordField txtPass;
    private javax.swing.JTextField txtUName;
    // End of variables declaration//GEN-END:variables
}
