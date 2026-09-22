package gamingcafe;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;
import javax.swing.JOptionPane;

public class UserReg extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(UserReg.class.getName());

    DBConnection db = new DBConnection();
    PreparedStatement pst;
    ResultSet rs;

    public UserReg() {
        initComponents();
        generateUserId();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        lblUId = new javax.swing.JLabel();
        txtFName = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtLName = new javax.swing.JTextField();
        txtNIC = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtEmail = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txtPhone = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        btnAdd = new javax.swing.JButton();
        cmbRole = new javax.swing.JComboBox<>();
        jLabel10 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));
        setMaximumSize(new java.awt.Dimension(500, 600));
        setMinimumSize(new java.awt.Dimension(500, 600));
        setResizable(false);

        jPanel2.setBackground(new java.awt.Color(0, 51, 102));
        jPanel2.setMaximumSize(new java.awt.Dimension(500, 60));
        jPanel2.setMinimumSize(new java.awt.Dimension(500, 60));
        jPanel2.setPreferredSize(new java.awt.Dimension(500, 60));

        jLabel1.setFont(new java.awt.Font("Intel One Mono SemiBold", 0, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("User Registration");
        jLabel1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 15, 1, 1));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 500, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 362, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(132, Short.MAX_VALUE)))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 60, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 48, Short.MAX_VALUE)
                    .addContainerGap()))
        );

        getContentPane().add(jPanel2, java.awt.BorderLayout.PAGE_START);

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jPanel3.setMaximumSize(new java.awt.Dimension(500, 540));
        jPanel3.setMinimumSize(new java.awt.Dimension(500, 540));
        jPanel3.setPreferredSize(new java.awt.Dimension(500, 540));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblUId.setBackground(new java.awt.Color(255, 255, 255));
        lblUId.setFont(new java.awt.Font("Leelawadee", 0, 16)); // NOI18N
        lblUId.setForeground(new java.awt.Color(0, 153, 255));
        jPanel3.add(lblUId, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 20, 210, 30));

        txtFName.setFont(new java.awt.Font("Leelawadee", 0, 16)); // NOI18N
        jPanel3.add(txtFName, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 70, 217, 30));

        jLabel3.setFont(new java.awt.Font("Leelawadee", 0, 16)); // NOI18N
        jLabel3.setText("last Name");
        jPanel3.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 120, 149, 30));

        txtLName.setFont(new java.awt.Font("Leelawadee", 0, 16)); // NOI18N
        jPanel3.add(txtLName, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 120, 217, 30));

        txtNIC.setFont(new java.awt.Font("Leelawadee", 0, 16)); // NOI18N
        jPanel3.add(txtNIC, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 170, 217, 30));

        jLabel4.setFont(new java.awt.Font("Leelawadee", 0, 16)); // NOI18N
        jLabel4.setText("NIC");
        jPanel3.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 170, 149, 30));

        jLabel5.setFont(new java.awt.Font("Leelawadee", 0, 16)); // NOI18N
        jLabel5.setText("Email");
        jPanel3.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 220, 149, 30));

        txtEmail.setFont(new java.awt.Font("Leelawadee", 0, 16)); // NOI18N
        jPanel3.add(txtEmail, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 220, 217, 30));

        jLabel6.setFont(new java.awt.Font("Leelawadee", 0, 16)); // NOI18N
        jLabel6.setText("Role");
        jPanel3.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 320, 50, 30));

        txtPhone.setFont(new java.awt.Font("Leelawadee", 0, 16)); // NOI18N
        jPanel3.add(txtPhone, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 270, 217, 30));

        jLabel7.setFont(new java.awt.Font("Leelawadee", 0, 16)); // NOI18N
        jLabel7.setText("First Name");
        jPanel3.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 70, 149, 30));

        jLabel9.setFont(new java.awt.Font("Leelawadee", 0, 16)); // NOI18N
        jLabel9.setText("User_ID");
        jPanel3.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 20, 149, 30));

        jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/image2.png"))); // NOI18N
        jPanel3.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 380, -1, 160));

        btnAdd.setBackground(new java.awt.Color(0, 51, 102));
        btnAdd.setFont(new java.awt.Font("Leelawadee", 1, 14)); // NOI18N
        btnAdd.setForeground(new java.awt.Color(255, 255, 255));
        btnAdd.setText("Add [Enter]");
        btnAdd.setBorder(null);
        btnAdd.addActionListener(this::btnAddActionPerformed);
        jPanel3.add(btnAdd, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 320, 130, 30));

        cmbRole.setFont(new java.awt.Font("Leelawadee", 0, 14)); // NOI18N
        cmbRole.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Admin", "User" }));
        cmbRole.addActionListener(this::cmbRoleActionPerformed);
        jPanel3.add(cmbRole, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 320, 150, 30));

        jLabel10.setFont(new java.awt.Font("Leelawadee", 0, 16)); // NOI18N
        jLabel10.setText("Phone");
        jPanel3.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 270, 149, 30));

        getContentPane().add(jPanel3, java.awt.BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed

        String userID = lblUId.getText();
        String fName = txtFName.getText().trim();
        String lName = txtLName.getText().trim();
        String nic = txtNIC.getText().trim();
        String email = txtEmail.getText().trim();
        String phone = txtPhone.getText().trim();
        String role = cmbRole.getSelectedItem().toString();

        try {

            if (fName.isEmpty() || lName.isEmpty() || nic.isEmpty() || phone.isEmpty() || email.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all required fields!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!email.contains("@") || !email.contains(".com") || email.matches("^[A-Z]#$%&*+-")) {
                JOptionPane.showMessageDialog(this, "Please Enter a valied Email!", "Error", JOptionPane.ERROR_MESSAGE);
                txtEmail.requestFocus();
                return;
            }

            if (!phone.matches("^[0-9]{10}$")) {
                JOptionPane.showMessageDialog(this, "Please Enter a valied Phone Number!", "Error", JOptionPane.ERROR_MESSAGE);
                txtPhone.requestFocus();
                return;
            }

            if (!nic.matches("^([0-9]{9}[Vv]|[0-9]{12})$")) {
                JOptionPane.showMessageDialog(this, "Please Enter a valied NIC!", "Error", JOptionPane.ERROR_MESSAGE);
                txtNIC.requestFocus();
                return;
            }

            nic = nic.toUpperCase();

            String userName = nic;
            String hPassword = hashPassword(nic);

            pst = db.con.prepareStatement("INSERT INTO user(emp_no, f_name, l_name, nic, email, phone, username, password, role) VALUES(?,?,?,?,?,?,?,?,?)");

            pst.setString(1, userID);
            pst.setString(2, fName);
            pst.setString(3, lName);
            pst.setString(4, nic);
            pst.setString(5, email);
            pst.setString(6, phone);
            pst.setString(7, userName);
            pst.setString(8, hPassword);
            pst.setString(9, role);

            int count = pst.executeUpdate();

            if (count > 0) {
                JOptionPane.showMessageDialog(this, "User Registered Successfully! Role: " + role + " User Name & Password: " + nic);
                txtFName.setText("");
                txtLName.setText("");
                txtNIC.setText("");
                txtEmail.setText("");
                txtPhone.setText("");
                cmbRole.setSelectedIndex(0);
                generateUserId();

            }
        } catch (SQLException ex) {
            System.getLogger(UserReg.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }//GEN-LAST:event_btnAddActionPerformed

    private void cmbRoleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbRoleActionPerformed

    }//GEN-LAST:event_cmbRoleActionPerformed

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
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new UserReg().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdd;
    private javax.swing.JComboBox<String> cmbRole;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JLabel lblUId;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JTextField txtFName;
    private javax.swing.JTextField txtLName;
    private javax.swing.JTextField txtNIC;
    private javax.swing.JTextField txtPhone;
    // End of variables declaration//GEN-END:variables

    private void generateUserId() {
        try {
            Random rand = new Random();
            boolean isUnique = false;
            String newId = "";

            while (!isUnique) {

                int randomNum = rand.nextInt(9000) + 1000;
                newId = "USR-GG-" + randomNum;

                pst = db.con.prepareStatement("SELECT emp_no FROM user WHERE emp_no = ?");
                pst.setString(1, newId);
                rs = pst.executeQuery();

                if (!rs.next()) {
                    isUnique = true;
                }
            }
            lblUId.setText(newId);
        } catch (SQLException ex) {
            System.getLogger(UserReg.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }

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
            System.getLogger(UserReg.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            return null;
        }
    }
}
