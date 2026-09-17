package gamingcafe;

import java.awt.Image;
import java.awt.event.KeyEvent;
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
        txtUname.requestFocus();

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

        User isUser = checkUser(userName, password);

        if (isUser != null) {
            System.out.println("User Role is: " + isUser.getRole());

            Dash dashboard = new Dash(isUser);
            dashboard.setVisible(true);
            this.dispose();
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

}
