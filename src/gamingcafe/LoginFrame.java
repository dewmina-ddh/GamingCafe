package gamingcafe;

import java.awt.Image;
import java.awt.event.KeyEvent;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

public class LoginFrame extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(LoginFrame.class.getName());
    User user;
    DBConnection db = new DBConnection();
    PreparedStatement pst;
    ResultSet rs;

    public LoginFrame() {
        initComponents();
        createUserLogsTable();
        txtUname.requestFocus();
    }

    // user_logs table eka create kirima saha database columns ensure kirima
    private void createUserLogsTable() {
        try {
            PreparedStatement createPst = db.con.prepareStatement(
                "CREATE TABLE IF NOT EXISTS user_logs ("
                + "log_id INT AUTO_INCREMENT PRIMARY KEY, "
                + "emp_no VARCHAR(50), "
                + "username VARCHAR(100), "
                + "role VARCHAR(50), "
                + "login_time DATETIME DEFAULT CURRENT_TIMESTAMP, "
                + "logout_time DATETIME NULL, "
                + "duration VARCHAR(50) DEFAULT 'Active', "
                + "status VARCHAR(50) DEFAULT 'Logged In'"
                + ")"
            );
            createPst.executeUpdate();
            createPst.close();

            // Ensure cus_name in gaming_sessions with default value
            java.sql.Statement stmt = db.con.createStatement();
            try {
                stmt.executeUpdate("ALTER TABLE gaming_sessions ADD COLUMN cus_name VARCHAR(100) NOT NULL DEFAULT 'Walk-in Customer'");
            } catch (Exception e1) {
                try {
                    stmt.executeUpdate("ALTER TABLE gaming_sessions MODIFY COLUMN cus_name VARCHAR(100) NOT NULL DEFAULT 'Walk-in Customer'");
                } catch (Exception ignored) {}
            }
            try {
                stmt.executeUpdate("ALTER TABLE gaming_sessions ADD COLUMN add_minutes INT NOT NULL DEFAULT 0");
            } catch (Exception ignored) {}
            try {
                stmt.executeUpdate("ALTER TABLE gaming_sessions ADD COLUMN price_per_adding DECIMAL(10,2) NOT NULL DEFAULT 0.00");
            } catch (Exception ignored) {}
            stmt.close();

        } catch (Exception ex) {
            System.out.println("database check note: " + ex.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        txtPass = new javax.swing.JPasswordField();
        txtUname = new javax.swing.JTextField();
        btnLogin = new javax.swing.JButton();
        Background = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txtPass.setBackground(new java.awt.Color(0, 0, 0));
        txtPass.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        txtPass.setForeground(new java.awt.Color(255, 255, 255));
        txtPass.setBorder(null);
        txtPass.setMaximumSize(new java.awt.Dimension(64, 25));
        txtPass.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtPassKeyPressed(evt);
            }
        });
        getContentPane().add(txtPass, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 430, 150, -1));

        txtUname.setBackground(new java.awt.Color(0, 0, 0));
        txtUname.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtUname.setForeground(new java.awt.Color(255, 255, 255));
        txtUname.setBorder(null);
        txtUname.setMaximumSize(new java.awt.Dimension(64, 25));
        txtUname.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtUnameKeyPressed(evt);
            }
        });
        getContentPane().add(txtUname, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 430, 150, -1));

        btnLogin.setBackground(new java.awt.Color(25, 170, 98));
        btnLogin.setFont(new java.awt.Font("Intel One Mono", 1, 24)); // NOI18N
        btnLogin.setForeground(new java.awt.Color(255, 255, 255));
        btnLogin.setText("Enter");
        btnLogin.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(21, 244, 147), 2, true));
        btnLogin.addActionListener(this::btnLoginActionPerformed);
        getContentPane().add(btnLogin, new org.netbeans.lib.awtextra.AbsoluteConstraints(840, 380, 130, 80));

        Background.setBackground(new java.awt.Color(0, 0, 0));
        Background.setForeground(new java.awt.Color(255, 255, 255));
        Background.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/log.png"))); // NOI18N
        getContentPane().add(Background, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnLoginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLoginActionPerformed

        String userName = txtUname.getText().trim();
        String password = new String(txtPass.getPassword()).trim();
        
        String hPassword = hashPassword(password);

        User isUser = checkUser(userName, hPassword);

        if (isUser != null) {
            // Check inactive user
            String role = isUser.getRole();
            if (role != null && (role.equalsIgnoreCase("Inactive") || role.equalsIgnoreCase("Deactivated"))) {
                JOptionPane.showMessageDialog(this, "This account is currently deactivated. Please contact an Administrator!", "Account Inactive", JOptionPane.WARNING_MESSAGE);
                return;
            }

            System.out.println("User Role is: " + isUser.getRole());

            // 1. User Login eka user_logs table ekata record kirima
            int currentLogId = 0;
            try {
                pst = db.con.prepareStatement(
                    "INSERT INTO user_logs (emp_no, username, role, login_time, logout_time, duration, status) VALUES (?, ?, ?, NOW(), NULL, 'Active', 'Logged In')",
                    java.sql.Statement.RETURN_GENERATED_KEYS
                );
                pst.setString(1, isUser.getEmpNo() != null ? isUser.getEmpNo() : "-");
                pst.setString(2, isUser.getUserName() != null ? isUser.getUserName() : userName);
                pst.setString(3, isUser.getRole() != null ? isUser.getRole() : "Staff");
                pst.executeUpdate();
                rs = pst.getGeneratedKeys();
                if (rs.next()) {
                    currentLogId = rs.getInt(1);
                }
                pst.close();
                if (rs != null) rs.close();
            } catch (Exception ex) {
                System.out.println("User log record note: " + ex.getMessage());
            }

            // 2. First time login eka check kirima (Username eka NIC ekata samana nam ho default password eka thiyenam)
            String defaultHash = hashPassword(isUser.getNic());
            boolean isFirstTime = (isUser.getUserName() != null && isUser.getNic() != null && isUser.getUserName().equalsIgnoreCase(isUser.getNic()))
                    || (isUser.getPassword() != null && defaultHash != null && isUser.getPassword().equalsIgnoreCase(defaultHash));

            if (isFirstTime) {
                // First time login setup frame eka open kirima
                ChangeUserDetails changeFrame = new ChangeUserDetails(isUser, currentLogId);
                changeFrame.setVisible(true);
                this.dispose();
            } else {
                // Already updated nam direct Dashboard ekata yama
                Dash dashboard = new Dash(isUser, currentLogId);
                dashboard.setVisible(true);
                this.dispose();
            }
        } else {
            JOptionPane.showMessageDialog(null, "Logging Failed.. Please Enter correct one and try again");
            txtUname.setText("");
            txtPass.setText("");
            txtUname.requestFocus();
        }

    }//GEN-LAST:event_btnLoginActionPerformed

    private void txtUnameKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtUnameKeyPressed

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            txtPass.requestFocus();
        }

        if (evt.getKeyCode() == KeyEvent.VK_CONTROL) {
            txtPass.requestFocus();
        }
    }//GEN-LAST:event_txtUnameKeyPressed

    private void txtPassKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPassKeyPressed

        if (evt.getKeyCode() == KeyEvent.VK_ALT) {
            txtUname.requestFocus();
        }

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            btnLogin.doClick();
        }
    }//GEN-LAST:event_txtPassKeyPressed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {

//        UIManager.put("Button.arc", 15);
//        UIManager.put("Component.arc", 15);

        //-----load flat laf-----
        try {
            com.formdev.flatlaf.FlatLightLaf.setup();
//            com.formdev.flatlaf.FlatDarkLaf.setup();

        } catch (Exception ex) {
            ex.printStackTrace();
            System.err.println("Failed to initialize LaF");
        }

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new LoginFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel Background;
    private javax.swing.JButton btnLogin;
    private javax.swing.JPasswordField txtPass;
    private javax.swing.JTextField txtUname;
    // End of variables declaration//GEN-END:variables

    private User checkUser(String userName, String password) {
        try {
            pst = db.con.prepareStatement("SELECT * FROM user WHERE (username=? OR nic=?) AND password =?");
            pst.setString(1, userName);
            pst.setString(2, userName);
            pst.setString(3, password);

            rs = pst.executeQuery();
            if (rs.next()) {
                return new User(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8), rs.getString(9), rs.getString(10), rs.getString(11), (Image) rs.getBlob(12));
            }

        } catch (SQLException ex) {
            System.getLogger(LoginFrame.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        return null;

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
