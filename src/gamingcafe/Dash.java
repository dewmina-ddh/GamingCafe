package gamingcafe;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Random;
import java.util.Vector;
import javax.swing.BorderFactory;
import static javax.swing.BorderFactory.createCompoundBorder;
import static javax.swing.BorderFactory.createEmptyBorder;
import javax.swing.ButtonGroup;
import javax.swing.DefaultCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;

// iText PDF library imports for generating PDF reports
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.awt.Window;
import javax.swing.SwingUtilities;

public class Dash extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(Dash.class.getName());

    boolean isSidebarOpen = false;
    boolean isTableClick = false;
    User user;
    private int currentLogId = 0;
    DBConnection db = new DBConnection();
    PreparedStatement pst;
    ResultSet rs;

    // Session Details panel variables & timer
    private javax.swing.Timer sessionTimer;
    private long sessionStartTimeMillis = 0;
    private String currentSelectedPcId = "";
    private String currentSelectedPcName = "";
    private String currentSelectedCategory = "";
    private String currentSelectedRate = "";
    private String currentSelectedStatus = "";
    private String currentSelectedCustomer = "Walk-in Customer";
    private String currentSelectedPhone = "";
    private String activeSessionId = "";
    private int sessionAllocatedMinutes = 60;
    private long sessionEndTimeMillis = 0;

    // Live page Session Table list and real-time timer
    private static class LiveSessionEntry {
        String sessionId;
        String pcId;
        String pcName;
        String category;
        String cusName;
        String cusPhone;
        long startTimeMillis;
        double hourlyRate;
    }
    private final java.util.List<LiveSessionEntry> liveSessionEntries = new java.util.ArrayList<>();
    private javax.swing.Timer liveTableTimer;
    private byte[] currentProfileImageBytes = null;

    public Dash(User user) {
        this(user, 0);
    }

    public Dash(User user, int logId) {
        this.user = user;
        this.currentLogId = logId;

        initComponents();
        ensureDatabaseColumns();
        connerRoud();
        resetSessionDetailsView();
        loadUserTable();
        loadPcTable();
        loadPcRepairTable();
        loadRepairHistoryTable();
        loadPcGrid();
        loadUserPctable();
        loadSummaryCards();

        String userRole = user != null ? user.getRole() : "";
        if (userRole != null && userRole.equalsIgnoreCase("Admin")) {
            btnAdmin.setVisible(true);
        } else {
            btnAdmin.setVisible(false);
        }

        tablestyle2(tableUserPcManage, jScrollPane5);
        tableStyle(tableUser, jScrollPane1);
        tableStyle(tablePc, jScrollPane2);
        tableStyle(tableRepairPc, jScrollPane3);
        tableStyle(jTable2, jScrollPane4);

        // History & Reports table styling and initial load
        tableStyle(jTable1, jScrollPane6);
        tableStyle(jTable3, jScrollPane7);
        tableStyle(jTable4, jScrollPane8);
        loadUserLogsTable();
        loadSessionHistoryTable();
        loadInvoiceHistoryTable();

        // Live sessions table styling & button column
        tableStyle(tableLiveSessions, jScrollPaneLive);
        tableLiveSessions.setRowHeight(42);
        if (tableLiveSessions.getColumnModel().getColumnCount() > 8) {
            tableLiveSessions.getColumnModel().getColumn(8).setCellRenderer(new LiveEndButtonRenderer());
            tableLiveSessions.getColumnModel().getColumn(8).setCellEditor(new LiveEndButtonEditor(new javax.swing.JCheckBox()));
        }
        loadLiveSessionsTable();
        startLiveTableTimer();

        checkAccEdite.setSelected(false);
        loadLoggedInUserSettings();

        if (user != null) {
            String uId = user.getEmpNo();
            loadPcDetails(uId);
        }

        ButtonGroup pcStatusGroup = new ButtonGroup();
        pcStatusGroup.add(radioAwailable);
        pcStatusGroup.add(radioOccu);
        pcStatusGroup.add(radioRepair);

        btnPcUpdate.setVisible(false);
        btnPcDel.setVisible(false);
        btnPcClear.setVisible(false);

        btnUserUpdate.setVisible(false);
        btnUserDel.setVisible(false);

        // Window Closing listener to record user logout time in user_logs table
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                recordUserLogout();
            }
        });

        // Setup Keyboard Shortcuts (F1-F6, Enter key) and right click PDF export popups
        setupKeyboardShortcuts();
        setupTablePdfExportPopups();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnReports = new javax.swing.JButton();
        framePnl = new javax.swing.JPanel();
        contentPnl = new javax.swing.JPanel();
        sidebarPnl = new javax.swing.JPanel();
        pnlButton = new javax.swing.JPanel();
        btnDash = new javax.swing.JButton();
        btnLive = new javax.swing.JButton();
        btnPc = new javax.swing.JButton();
        btnLogOut = new javax.swing.JButton();
        btnAdmin = new javax.swing.JButton();
        jPanel24 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        btnAcc = new javax.swing.JButton();
        main = new javax.swing.JPanel();
        Header = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        btnHam = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        DateTime = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        holder = new javax.swing.JPanel();
        pnlDash = new javax.swing.JPanel();
        pcSessionsPanel = new javax.swing.JPanel();
        summery = new javax.swing.JPanel();
        jPanel18 = new javax.swing.JPanel();
        lblTitle18 = new javax.swing.JLabel();
        lblTotalPc = new javax.swing.JLabel();
        lblSub18 = new javax.swing.JLabel();
        jPanel17 = new javax.swing.JPanel();
        lblTitle17 = new javax.swing.JLabel();
        lblOccupiedPc = new javax.swing.JLabel();
        lblSub17 = new javax.swing.JLabel();
        jPanel19 = new javax.swing.JPanel();
        lblTitle19 = new javax.swing.JLabel();
        lblAvailablePc = new javax.swing.JLabel();
        lblSub19 = new javax.swing.JLabel();
        jPanel20 = new javax.swing.JPanel();
        lblTitle20 = new javax.swing.JLabel();
        lblRepairPc = new javax.swing.JLabel();
        lblSub20 = new javax.swing.JLabel();
        jPanel21 = new javax.swing.JPanel();
        lblTitle21 = new javax.swing.JLabel();
        lblTotalUsers = new javax.swing.JLabel();
        lblSub21 = new javax.swing.JLabel();
        pnlSessionEnd = new javax.swing.JPanel();
        jLabel25 = new javax.swing.JLabel();
        lblDetailPcName = new javax.swing.JLabel();
        lblDetailCategory = new javax.swing.JLabel();
        lblDetailPcId = new javax.swing.JLabel();
        lblDetailRate = new javax.swing.JLabel();
        lblStatusTitle = new javax.swing.JLabel();
        lblDetailStatus = new javax.swing.JLabel();
        lblDetailSessionId = new javax.swing.JLabel();
        lblDetailStartTime = new javax.swing.JLabel();
        lblDetailStaff = new javax.swing.JLabel();
        lblCusNameTitle = new javax.swing.JLabel();
        txtSessionCusName = new javax.swing.JTextField();
        lblCusPhoneTitle = new javax.swing.JLabel();
        txtSessionCusPhone = new javax.swing.JTextField();
        lblDurationTitle = new javax.swing.JLabel();
        cmbSessionDuration = new javax.swing.JComboBox<>();
        pnlTimerBox = new javax.swing.JPanel();
        lblTimerHeader = new javax.swing.JLabel();
        lblDetailTimer = new javax.swing.JLabel();
        pnlAmountBox = new javax.swing.JPanel();
        lblAmountHeader = new javax.swing.JLabel();
        lblDetailAmount = new javax.swing.JLabel();
        btnStartSessionAction = new javax.swing.JButton();
        btnAddTimeAction = new javax.swing.JButton();
        btnEndSessionAction = new javax.swing.JButton();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        pnlLive = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        lblLiveSearch = new javax.swing.JLabel();
        txtLiveSearch = new javax.swing.JTextField();
        btnLiveSearch = new javax.swing.JButton();
        btnLiveRefresh = new javax.swing.JButton();
        lblLiveTotalActive = new javax.swing.JLabel();
        jScrollPaneLive = new javax.swing.JScrollPane();
        tableLiveSessions = new javax.swing.JTable();
        pnlPc = new javax.swing.JPanel();
        jPanel14 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        tableUserPcManage = new javax.swing.JTable();
        jTextField5 = new javax.swing.JTextField();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        pnlReports = new javax.swing.JPanel();
        jPanel15 = new javax.swing.JPanel();
        pnlAdmin = new javax.swing.JPanel();
        adminLeft = new javax.swing.JPanel();
        UserEdite = new javax.swing.JPanel();
        pnlUTable = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableUser = new javax.swing.JTable();
        userEdit = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtUserNo = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        lblImage = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jToggleButton1 = new javax.swing.JToggleButton();
        txtFullName = new javax.swing.JTextField();
        txtUserName = new javax.swing.JTextField();
        btnUserDel = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        cmbRole = new javax.swing.JComboBox<>();
        btnUserUpdate = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        jToggleButton2 = new javax.swing.JToggleButton();
        btnClean = new javax.swing.JButton();
        pnlPcM = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tablePc = new javax.swing.JTable();
        jLabel17 = new javax.swing.JLabel();
        txtPCSearch = new javax.swing.JTextField();
        jPanel7 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        txtPcId = new javax.swing.JTextField();
        cmbPcCategory = new javax.swing.JComboBox<>();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        txtName = new javax.swing.JTextField();
        btnPcDel = new javax.swing.JButton();
        btnPcAdd = new javax.swing.JButton();
        jLabel14 = new javax.swing.JLabel();
        txtIP = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        txtHrate = new javax.swing.JTextField();
        btnPcUpdate = new javax.swing.JButton();
        btnPcClear = new javax.swing.JButton();
        chkblockIP = new javax.swing.JCheckBox();
        jPanel8 = new javax.swing.JPanel();
        txtCPU = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        txtMBoard = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        txtRAM = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        txtVGA = new javax.swing.JTextField();
        jPanel9 = new javax.swing.JPanel();
        radioOccu = new javax.swing.JRadioButton();
        radioAwailable = new javax.swing.JRadioButton();
        jLabel16 = new javax.swing.JLabel();
        radioRepair = new javax.swing.JRadioButton();
        pnlRepair = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tableRepairPc = new javax.swing.JTable();
        jPanel12 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jPanel13 = new javax.swing.JPanel();
        jTextField1 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jLabel18 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField();
        pnlHistory = new javax.swing.JPanel();
        jPanel22 = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel24 = new javax.swing.JLabel();
        jScrollPane7 = new javax.swing.JScrollPane();
        jTable3 = new javax.swing.JTable();
        jLabel26 = new javax.swing.JLabel();
        jScrollPane8 = new javax.swing.JScrollPane();
        jTable4 = new javax.swing.JTable();
        adminRigh = new javax.swing.JPanel();
        adminRightCard = new javax.swing.JPanel();
        btnUserAdd = new javax.swing.JButton();
        btnUserManage = new javax.swing.JButton();
        btnPcManage = new javax.swing.JButton();
        btnToRepair = new javax.swing.JButton();
        btnHistory = new javax.swing.JButton();
        btnIncome = new javax.swing.JButton();
        pnlSet = new javax.swing.JPanel();
        pnlAcc = new javax.swing.JPanel();
        jPanel16 = new javax.swing.JPanel();
        jPanel23 = new javax.swing.JPanel();
        lblUserImage = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        txtFName = new javax.swing.JTextField();
        txtLName = new javax.swing.JTextField();
        jLabel28 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        txtUName = new javax.swing.JTextField();
        txtEmail = new javax.swing.JTextField();
        jLabel30 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        txtPhone = new javax.swing.JTextField();
        txtNIC = new javax.swing.JTextField();
        jLabel32 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        txtEmpNo = new javax.swing.JTextField();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        Settings = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        setting1 = new javax.swing.JPanel();
        jCheckBoxHam = new javax.swing.JCheckBox();
        checkAccEdite = new javax.swing.JCheckBox();

        btnReports.setBackground(new java.awt.Color(11, 19, 43));
        btnReports.setFont(new java.awt.Font("Leelawadee", 0, 16)); // NOI18N
        btnReports.setForeground(new java.awt.Color(255, 255, 255));
        btnReports.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/reports.png"))); // NOI18N
        btnReports.setText("Reports");
        btnReports.setToolTipText("");
        btnReports.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 14, 1, 1));
        btnReports.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnReports.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnReports.addActionListener(this::btnReportsActionPerformed);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(1280, 720));
        setResizable(false);

        framePnl.setMaximumSize(new java.awt.Dimension(1280, 720));
        framePnl.setMinimumSize(new java.awt.Dimension(1280, 720));
        framePnl.setPreferredSize(new java.awt.Dimension(1280, 720));
        framePnl.setLayout(new java.awt.BorderLayout());

        contentPnl.setLayout(new java.awt.BorderLayout());

        sidebarPnl.setBackground(new java.awt.Color(0, 204, 255));
        sidebarPnl.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 1));
        sidebarPnl.setMaximumSize(new java.awt.Dimension(140, 720));
        sidebarPnl.setMinimumSize(new java.awt.Dimension(140, 720));
        sidebarPnl.setPreferredSize(new java.awt.Dimension(140, 720));
        sidebarPnl.setRequestFocusEnabled(false);
        sidebarPnl.setLayout(new java.awt.BorderLayout());

        pnlButton.setBackground(new java.awt.Color(11, 19, 43));
        pnlButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(100, 0, 20, 0));
        pnlButton.setMaximumSize(new java.awt.Dimension(140, 392));
        pnlButton.setMinimumSize(new java.awt.Dimension(140, 392));
        pnlButton.setPreferredSize(new java.awt.Dimension(140, 392));
        pnlButton.setLayout(new java.awt.GridLayout(8, 1, 5, 0));

        btnDash.setBackground(new java.awt.Color(11, 19, 43));
        btnDash.setFont(new java.awt.Font("Leelawadee", 0, 16)); // NOI18N
        btnDash.setForeground(new java.awt.Color(255, 255, 255));
        btnDash.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/dash.png"))); // NOI18N
        btnDash.setText("Dashboard");
        btnDash.setToolTipText("Dashboard");
        btnDash.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 14, 1, 1));
        btnDash.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnDash.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnDash.addActionListener(this::btnDashActionPerformed);
        pnlButton.add(btnDash);

        btnLive.setBackground(new java.awt.Color(11, 19, 43));
        btnLive.setFont(new java.awt.Font("Leelawadee", 0, 16)); // NOI18N
        btnLive.setForeground(new java.awt.Color(255, 255, 255));
        btnLive.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/live.png"))); // NOI18N
        btnLive.setText("Live");
        btnLive.setToolTipText("");
        btnLive.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 14, 1, 1));
        btnLive.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnLive.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnLive.addActionListener(this::btnLiveActionPerformed);
        pnlButton.add(btnLive);

        btnPc.setBackground(new java.awt.Color(11, 19, 43));
        btnPc.setFont(new java.awt.Font("Leelawadee", 0, 16)); // NOI18N
        btnPc.setForeground(new java.awt.Color(255, 255, 255));
        btnPc.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/pc.png"))); // NOI18N
        btnPc.setText("Pc");
        btnPc.setToolTipText("");
        btnPc.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 14, 1, 1));
        btnPc.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnPc.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnPc.addActionListener(this::btnPcActionPerformed);
        pnlButton.add(btnPc);

        btnLogOut.setBackground(new java.awt.Color(153, 0, 0));
        btnLogOut.setFont(new java.awt.Font("Leelawadee", 0, 16)); // NOI18N
        btnLogOut.setForeground(new java.awt.Color(255, 255, 255));
        btnLogOut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/logOut.png"))); // NOI18N
        btnLogOut.setText("LogOut");
        btnLogOut.setToolTipText("");
        btnLogOut.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 14, 1, 1));
        btnLogOut.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnLogOut.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnLogOut.addActionListener(this::btnLogOutActionPerformed);
        pnlButton.add(btnLogOut);

        btnAdmin.setBackground(new java.awt.Color(218, 165, 32));
        btnAdmin.setFont(new java.awt.Font("Leelawadee", 0, 16)); // NOI18N
        btnAdmin.setForeground(new java.awt.Color(255, 255, 255));
        btnAdmin.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/admin.png"))); // NOI18N
        btnAdmin.setText("Admin");
        btnAdmin.setToolTipText("");
        btnAdmin.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 14, 1, 1));
        btnAdmin.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnAdmin.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnAdmin.addActionListener(this::btnAdminActionPerformed);
        pnlButton.add(btnAdmin);

        jPanel24.setBackground(new java.awt.Color(11, 19, 43));

        javax.swing.GroupLayout jPanel24Layout = new javax.swing.GroupLayout(jPanel24);
        jPanel24.setLayout(jPanel24Layout);
        jPanel24Layout.setHorizontalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 139, Short.MAX_VALUE)
        );
        jPanel24Layout.setVerticalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 75, Short.MAX_VALUE)
        );

        pnlButton.add(jPanel24);

        jPanel1.setBackground(new java.awt.Color(11, 19, 43));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 139, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 76, Short.MAX_VALUE)
        );

        pnlButton.add(jPanel1);

        btnAcc.setBackground(new java.awt.Color(11, 19, 43));
        btnAcc.setFont(new java.awt.Font("Leelawadee", 0, 16)); // NOI18N
        btnAcc.setForeground(new java.awt.Color(255, 255, 255));
        btnAcc.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/setting.png"))); // NOI18N
        btnAcc.setText("Settings");
        btnAcc.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 14, 1, 1));
        btnAcc.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnAcc.addActionListener(this::btnAccActionPerformed);
        pnlButton.add(btnAcc);

        sidebarPnl.add(pnlButton, java.awt.BorderLayout.CENTER);

        contentPnl.add(sidebarPnl, java.awt.BorderLayout.LINE_START);

        main.setBackground(new java.awt.Color(11, 19, 43));
        main.setLayout(new java.awt.BorderLayout());

        Header.setBackground(new java.awt.Color(0, 153, 255));
        Header.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 1, 0));
        Header.setMaximumSize(new java.awt.Dimension(1140, 60));
        Header.setMinimumSize(new java.awt.Dimension(1140, 60));
        Header.setName(""); // NOI18N
        Header.setPreferredSize(new java.awt.Dimension(1140, 60));
        Header.setLayout(new java.awt.BorderLayout());

        jPanel2.setBackground(new java.awt.Color(0, 51, 102));

        btnHam.setBackground(new java.awt.Color(0, 51, 102));
        btnHam.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/ci--hamburger-md.png"))); // NOI18N
        btnHam.setBorder(null);
        btnHam.addActionListener(this::btnHamActionPerformed);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnHam)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnHam, javax.swing.GroupLayout.DEFAULT_SIZE, 47, Short.MAX_VALUE)
                .addContainerGap())
        );

        Header.add(jPanel2, java.awt.BorderLayout.LINE_START);

        jPanel5.setBackground(new java.awt.Color(0, 51, 102));
        jPanel5.setMaximumSize(new java.awt.Dimension(300, 60));
        jPanel5.setMinimumSize(new java.awt.Dimension(300, 60));
        jPanel5.setPreferredSize(new java.awt.Dimension(300, 60));

        DateTime.setFont(new java.awt.Font("Segoe UI", 0, 36)); // NOI18N
        DateTime.setForeground(new java.awt.Color(255, 255, 255));
        DateTime.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        DateTime.setText("dsfdghtrhtr");
        DateTime.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                    .addContainerGap(60, Short.MAX_VALUE)
                    .addComponent(DateTime, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap()))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 60, Short.MAX_VALUE)
            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(DateTime)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        Header.add(jPanel5, java.awt.BorderLayout.LINE_END);

        jPanel6.setBackground(new java.awt.Color(0, 51, 102));

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 796, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 59, Short.MAX_VALUE)
        );

        Header.add(jPanel6, java.awt.BorderLayout.CENTER);

        main.add(Header, java.awt.BorderLayout.PAGE_START);

        holder.setBackground(new java.awt.Color(11, 19, 43));
        holder.setLayout(new java.awt.CardLayout());

        pnlDash.setBackground(new java.awt.Color(11, 19, 43));

        pcSessionsPanel.setBackground(new java.awt.Color(11, 19, 43));
        pcSessionsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 153, 255)), "PC_Sessions", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 12), new java.awt.Color(0, 153, 255))); // NOI18N
        pcSessionsPanel.setMaximumSize(new java.awt.Dimension(832, 450));
        pcSessionsPanel.setMinimumSize(new java.awt.Dimension(832, 450));
        pcSessionsPanel.setPreferredSize(new java.awt.Dimension(832, 450));

        javax.swing.GroupLayout pcSessionsPanelLayout = new javax.swing.GroupLayout(pcSessionsPanel);
        pcSessionsPanel.setLayout(pcSessionsPanelLayout);
        pcSessionsPanelLayout.setHorizontalGroup(
            pcSessionsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 822, Short.MAX_VALUE)
        );
        pcSessionsPanelLayout.setVerticalGroup(
            pcSessionsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 427, Short.MAX_VALUE)
        );

        summery.setBackground(new java.awt.Color(0, 0, 51));
        summery.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));
        summery.setMaximumSize(new java.awt.Dimension(620, 80));
        summery.setMinimumSize(new java.awt.Dimension(620, 80));
        summery.setPreferredSize(new java.awt.Dimension(620, 80));
        summery.setLayout(new java.awt.GridLayout(1, 5, 10, 0));

        jPanel18.setBackground(new java.awt.Color(15, 23, 42));
        jPanel18.setBorder(javax.swing.BorderFactory.createCompoundBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 153, 255), 1, true), javax.swing.BorderFactory.createEmptyBorder(6, 10, 6, 10)));
        jPanel18.setLayout(new java.awt.GridLayout(3, 1, 0, 2));

        lblTitle18.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        lblTitle18.setForeground(new java.awt.Color(0, 153, 255));
        lblTitle18.setText("TOTAL STATIONS");
        jPanel18.add(lblTitle18);

        lblTotalPc.setFont(new java.awt.Font("Segoe UI", 1, 22)); // NOI18N
        lblTotalPc.setForeground(new java.awt.Color(255, 255, 255));
        lblTotalPc.setText("0");
        jPanel18.add(lblTotalPc);

        lblSub18.setFont(new java.awt.Font("Segoe UI", 0, 10)); // NOI18N
        lblSub18.setForeground(new java.awt.Color(148, 163, 184));
        lblSub18.setText("All PCs & PS5s");
        jPanel18.add(lblSub18);

        summery.add(jPanel18);

        jPanel17.setBackground(new java.awt.Color(38, 18, 24));
        jPanel17.setBorder(javax.swing.BorderFactory.createCompoundBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 71, 87), 1, true), javax.swing.BorderFactory.createEmptyBorder(6, 10, 6, 10)));
        jPanel17.setLayout(new java.awt.GridLayout(3, 1, 0, 2));

        lblTitle17.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        lblTitle17.setForeground(new java.awt.Color(255, 71, 87));
        lblTitle17.setText("ACTIVE SESSIONS");
        jPanel17.add(lblTitle17);

        lblOccupiedPc.setFont(new java.awt.Font("Segoe UI", 1, 22)); // NOI18N
        lblOccupiedPc.setForeground(new java.awt.Color(255, 255, 255));
        lblOccupiedPc.setText("0");
        jPanel17.add(lblOccupiedPc);

        lblSub17.setFont(new java.awt.Font("Segoe UI", 0, 10)); // NOI18N
        lblSub17.setForeground(new java.awt.Color(148, 163, 184));
        lblSub17.setText("Currently Playing");
        jPanel17.add(lblSub17);

        summery.add(jPanel17);

        jPanel19.setBackground(new java.awt.Color(16, 36, 28));
        jPanel19.setBorder(javax.swing.BorderFactory.createCompoundBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 230, 118), 1, true), javax.swing.BorderFactory.createEmptyBorder(6, 10, 6, 10)));
        jPanel19.setLayout(new java.awt.GridLayout(3, 1, 0, 2));

        lblTitle19.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        lblTitle19.setForeground(new java.awt.Color(0, 230, 118));
        lblTitle19.setText("AVAILABLE PCS");
        jPanel19.add(lblTitle19);

        lblAvailablePc.setFont(new java.awt.Font("Segoe UI", 1, 22)); // NOI18N
        lblAvailablePc.setForeground(new java.awt.Color(255, 255, 255));
        lblAvailablePc.setText("0");
        jPanel19.add(lblAvailablePc);

        lblSub19.setFont(new java.awt.Font("Segoe UI", 0, 10)); // NOI18N
        lblSub19.setForeground(new java.awt.Color(148, 163, 184));
        lblSub19.setText("Ready to Play");
        jPanel19.add(lblSub19);

        summery.add(jPanel19);

        jPanel20.setBackground(new java.awt.Color(38, 30, 12));
        jPanel20.setBorder(javax.swing.BorderFactory.createCompoundBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 170, 0), 1, true), javax.swing.BorderFactory.createEmptyBorder(6, 10, 6, 10)));
        jPanel20.setLayout(new java.awt.GridLayout(3, 1, 0, 2));

        lblTitle20.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        lblTitle20.setForeground(new java.awt.Color(255, 170, 0));
        lblTitle20.setText("IN REPAIR");
        jPanel20.add(lblTitle20);

        lblRepairPc.setFont(new java.awt.Font("Segoe UI", 1, 22)); // NOI18N
        lblRepairPc.setForeground(new java.awt.Color(255, 255, 255));
        lblRepairPc.setText("0");
        jPanel20.add(lblRepairPc);

        lblSub20.setFont(new java.awt.Font("Segoe UI", 0, 10)); // NOI18N
        lblSub20.setForeground(new java.awt.Color(148, 163, 184));
        lblSub20.setText("Under Service");
        jPanel20.add(lblSub20);

        summery.add(jPanel20);

        jPanel21.setBackground(new java.awt.Color(26, 18, 42));
        jPanel21.setBorder(javax.swing.BorderFactory.createCompoundBorder(new javax.swing.border.LineBorder(new java.awt.Color(186, 85, 211), 1, true), javax.swing.BorderFactory.createEmptyBorder(6, 10, 6, 10)));
        jPanel21.setLayout(new java.awt.GridLayout(3, 1, 0, 2));

        lblTitle21.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        lblTitle21.setForeground(new java.awt.Color(186, 85, 211));
        lblTitle21.setText("TOTAL USERS");
        jPanel21.add(lblTitle21);

        lblTotalUsers.setFont(new java.awt.Font("Segoe UI", 1, 22)); // NOI18N
        lblTotalUsers.setForeground(new java.awt.Color(255, 255, 255));
        lblTotalUsers.setText("0");
        jPanel21.add(lblTotalUsers);

        lblSub21.setFont(new java.awt.Font("Segoe UI", 0, 10)); // NOI18N
        lblSub21.setForeground(new java.awt.Color(148, 163, 184));
        lblSub21.setText("Registered Staff");
        jPanel21.add(lblSub21);

        summery.add(jPanel21);

        pnlSessionEnd.setBackground(new java.awt.Color(11, 19, 43));
        pnlSessionEnd.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 153, 255)));
        pnlSessionEnd.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel25.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel25.setForeground(new java.awt.Color(0, 204, 255));
        jLabel25.setText("DETAILS ABOUT SESSIONS");
        pnlSessionEnd.add(jLabel25, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 12, 250, 22));

        lblDetailPcName.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        lblDetailPcName.setForeground(new java.awt.Color(255, 255, 255));
        lblDetailPcName.setText("Select Station");
        pnlSessionEnd.add(lblDetailPcName, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 40, 130, 22));

        lblDetailCategory.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblDetailCategory.setForeground(new java.awt.Color(0, 204, 255));
        lblDetailCategory.setText("[PC]");
        pnlSessionEnd.add(lblDetailCategory, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 40, 70, 22));

        lblDetailPcId.setForeground(new java.awt.Color(148, 163, 184));
        lblDetailPcId.setText("Device ID: -");
        pnlSessionEnd.add(lblDetailPcId, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 60, 120, 18));

        lblDetailRate.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblDetailRate.setForeground(new java.awt.Color(255, 209, 102));
        lblDetailRate.setText("Rate: -");
        pnlSessionEnd.add(lblDetailRate, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 60, 125, 18));

        lblStatusTitle.setForeground(new java.awt.Color(148, 163, 184));
        lblStatusTitle.setText("Status:");
        pnlSessionEnd.add(lblStatusTitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 80, 50, 18));

        lblDetailStatus.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblDetailStatus.setForeground(new java.awt.Color(148, 163, 184));
        lblDetailStatus.setText("NO SELECTION");
        pnlSessionEnd.add(lblDetailStatus, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 80, 195, 18));

        lblDetailSessionId.setForeground(new java.awt.Color(148, 163, 184));
        lblDetailSessionId.setText("Session ID: -");
        pnlSessionEnd.add(lblDetailSessionId, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 100, 250, 18));

        lblDetailStartTime.setForeground(new java.awt.Color(148, 163, 184));
        lblDetailStartTime.setText("Start Time: -");
        pnlSessionEnd.add(lblDetailStartTime, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 120, 250, 18));

        lblDetailStaff.setForeground(new java.awt.Color(148, 163, 184));
        lblDetailStaff.setText("Staff In-Charge: -");
        pnlSessionEnd.add(lblDetailStaff, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 140, 250, 18));

        lblCusNameTitle.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        lblCusNameTitle.setForeground(new java.awt.Color(0, 204, 255));
        lblCusNameTitle.setText("Customer Name:");
        pnlSessionEnd.add(lblCusNameTitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 160, 125, 10));

        txtSessionCusName.setBackground(new java.awt.Color(16, 25, 45));
        txtSessionCusName.setForeground(new java.awt.Color(255, 255, 255));
        txtSessionCusName.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(31, 45, 74), 1, true));
        pnlSessionEnd.add(txtSessionCusName, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 180, 250, 20));

        lblCusPhoneTitle.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        lblCusPhoneTitle.setForeground(new java.awt.Color(0, 204, 255));
        lblCusPhoneTitle.setText("Customer Phone:");
        pnlSessionEnd.add(lblCusPhoneTitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 200, 260, 15));

        txtSessionCusPhone.setBackground(new java.awt.Color(16, 25, 45));
        txtSessionCusPhone.setForeground(new java.awt.Color(255, 255, 255));
        txtSessionCusPhone.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(31, 45, 74), 1, true));
        pnlSessionEnd.add(txtSessionCusPhone, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 220, 250, 26));

        lblDurationTitle.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        lblDurationTitle.setForeground(new java.awt.Color(0, 204, 255));
        lblDurationTitle.setText("Session Duration (Min 1 Hr):");
        pnlSessionEnd.add(lblDurationTitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 250, 250, 15));

        cmbSessionDuration.setBackground(new java.awt.Color(16, 25, 45));
        cmbSessionDuration.setForeground(new java.awt.Color(255, 255, 255));
        cmbSessionDuration.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1 Hour (60 mins)", "1 Hour 30 Mins (90 mins)", "2 Hours (120 mins)", "3 Hours (180 mins)", "4 Hours (240 mins)", "5 Hours (300 mins)", "6 Hours (360 mins)", "Custom Minutes..." }));
        cmbSessionDuration.addActionListener(this::cmbSessionDurationActionPerformed);
        pnlSessionEnd.add(cmbSessionDuration, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 270, 250, 26));

        pnlTimerBox.setBackground(new java.awt.Color(7, 13, 31));
        pnlTimerBox.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 204)));
        pnlTimerBox.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblTimerHeader.setFont(new java.awt.Font("Segoe UI", 1, 10)); // NOI18N
        lblTimerHeader.setForeground(new java.awt.Color(0, 204, 255));
        lblTimerHeader.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTimerHeader.setText("REMAINING TIME / DURATION");
        pnlTimerBox.add(lblTimerHeader, new org.netbeans.lib.awtextra.AbsoluteConstraints(5, 3, 240, 14));

        lblDetailTimer.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblDetailTimer.setForeground(new java.awt.Color(0, 255, 163));
        lblDetailTimer.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblDetailTimer.setText("01:00:00");
        pnlTimerBox.add(lblDetailTimer, new org.netbeans.lib.awtextra.AbsoluteConstraints(5, 18, 240, 26));

        pnlSessionEnd.add(pnlTimerBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 310, 250, 50));

        pnlAmountBox.setBackground(new java.awt.Color(7, 13, 31));
        pnlAmountBox.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(31, 45, 74)));
        pnlAmountBox.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblAmountHeader.setFont(new java.awt.Font("Segoe UI", 1, 10)); // NOI18N
        lblAmountHeader.setForeground(new java.awt.Color(148, 163, 184));
        lblAmountHeader.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblAmountHeader.setText("CURRENT CHARGES");
        pnlAmountBox.add(lblAmountHeader, new org.netbeans.lib.awtextra.AbsoluteConstraints(5, 3, 240, 14));

        lblDetailAmount.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        lblDetailAmount.setForeground(new java.awt.Color(255, 255, 255));
        lblDetailAmount.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblDetailAmount.setText("Rs. 0.00");
        pnlAmountBox.add(lblDetailAmount, new org.netbeans.lib.awtextra.AbsoluteConstraints(5, 18, 240, 24));

        pnlSessionEnd.add(pnlAmountBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 370, 250, 48));

        btnStartSessionAction.setBackground(new java.awt.Color(0, 153, 51));
        btnStartSessionAction.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        btnStartSessionAction.setForeground(new java.awt.Color(255, 255, 255));
        btnStartSessionAction.setText("▶ Start Session");
        btnStartSessionAction.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 0)));
        btnStartSessionAction.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnStartSessionAction.addActionListener(this::btnStartSessionActionActionPerformed);
        pnlSessionEnd.add(btnStartSessionAction, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 430, 250, 30));

        btnAddTimeAction.setBackground(new java.awt.Color(51, 153, 255));
        btnAddTimeAction.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        btnAddTimeAction.setForeground(new java.awt.Color(255, 255, 255));
        btnAddTimeAction.setText("⏱ + Add Time");
        btnAddTimeAction.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 51, 153)));
        btnAddTimeAction.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnAddTimeAction.addActionListener(this::btnAddTimeActionActionPerformed);
        pnlSessionEnd.add(btnAddTimeAction, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 470, 250, 30));

        btnEndSessionAction.setBackground(new java.awt.Color(217, 4, 41));
        btnEndSessionAction.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        btnEndSessionAction.setForeground(new java.awt.Color(255, 255, 255));
        btnEndSessionAction.setText("⏹ End Session & Bill");
        btnEndSessionAction.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 0, 0)));
        btnEndSessionAction.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnEndSessionAction.addActionListener(this::btnEndSessionActionActionPerformed);
        pnlSessionEnd.add(btnEndSessionAction, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 510, 250, 30));

        jLabel21.setFont(new java.awt.Font("Leelawadee UI Semilight", 1, 14)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(255, 255, 255));
        jLabel21.setText("Day-OverAll");

        jLabel22.setFont(new java.awt.Font("Leelawadee", 1, 24)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(255, 255, 255));
        jLabel22.setText("WELCOME BACK,");
        jLabel22.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 10, 1, 1));

        javax.swing.GroupLayout pnlDashLayout = new javax.swing.GroupLayout(pnlDash);
        pnlDash.setLayout(pnlDashLayout);
        pnlDashLayout.setHorizontalGroup(
            pnlDashLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDashLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlDashLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(summery, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(pnlDashLayout.createSequentialGroup()
                        .addGroup(pnlDashLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnlDashLayout.createSequentialGroup()
                                .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(678, 678, 678))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlDashLayout.createSequentialGroup()
                                .addGroup(pnlDashLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(pcSessionsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 272, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                        .addComponent(pnlSessionEnd, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        pnlDashLayout.setVerticalGroup(
            pnlDashLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDashLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlDashLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(pnlDashLayout.createSequentialGroup()
                        .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(pcSessionsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel21))
                    .addComponent(pnlSessionEnd, javax.swing.GroupLayout.PREFERRED_SIZE, 551, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(summery, javax.swing.GroupLayout.DEFAULT_SIZE, 91, Short.MAX_VALUE)
                .addContainerGap())
        );

        holder.add(pnlDash, "card7");

        pnlLive.setBackground(new java.awt.Color(11, 19, 43));

        jPanel4.setBackground(new java.awt.Color(11, 19, 43));
        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 153, 255)), "Live Gaming Sessions", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 12), new java.awt.Color(0, 204, 255))); // NOI18N
        jPanel4.setForeground(new java.awt.Color(11, 19, 43));

        lblLiveSearch.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblLiveSearch.setForeground(new java.awt.Color(0, 204, 255));
        lblLiveSearch.setText("Search:");

        txtLiveSearch.setBackground(new java.awt.Color(16, 25, 45));
        txtLiveSearch.setForeground(new java.awt.Color(255, 255, 255));
        txtLiveSearch.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(31, 45, 74), 1, true));
        txtLiveSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtLiveSearchKeyReleased(evt);
            }
        });

        btnLiveSearch.setBackground(new java.awt.Color(0, 153, 255));
        btnLiveSearch.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnLiveSearch.setForeground(new java.awt.Color(255, 255, 255));
        btnLiveSearch.setText("Search");
        btnLiveSearch.addActionListener(this::btnLiveSearchActionPerformed);

        btnLiveRefresh.setBackground(new java.awt.Color(6, 214, 160));
        btnLiveRefresh.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnLiveRefresh.setForeground(new java.awt.Color(255, 255, 255));
        btnLiveRefresh.setText("Refresh");
        btnLiveRefresh.addActionListener(this::btnLiveRefreshActionPerformed);

        lblLiveTotalActive.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        lblLiveTotalActive.setForeground(new java.awt.Color(0, 230, 118));
        lblLiveTotalActive.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblLiveTotalActive.setText("Active Sessions: 0");

        tableLiveSessions.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Session ID", "PC / Station", "Category", "Customer Name", "Customer Phone", "Start Time", "Live Duration", "Current Amount", "Action"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPaneLive.setViewportView(tableLiveSessions);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPaneLive, javax.swing.GroupLayout.DEFAULT_SIZE, 1106, Short.MAX_VALUE)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(lblLiveSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtLiveSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnLiveSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnLiveRefresh, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lblLiveTotalActive, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblLiveSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtLiveSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnLiveSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnLiveRefresh, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblLiveTotalActive, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPaneLive, javax.swing.GroupLayout.DEFAULT_SIZE, 570, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout pnlLiveLayout = new javax.swing.GroupLayout(pnlLive);
        pnlLive.setLayout(pnlLiveLayout);
        pnlLiveLayout.setHorizontalGroup(
            pnlLiveLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlLiveLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        pnlLiveLayout.setVerticalGroup(
            pnlLiveLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlLiveLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        holder.add(pnlLive, "card6");

        pnlPc.setBackground(new java.awt.Color(11, 19, 43));

        jPanel14.setBackground(new java.awt.Color(11, 19, 43));
        jPanel14.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 153, 255)), "PC & Gaming Stations Overview", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 12), new java.awt.Color(0, 204, 255))); // NOI18N
        jPanel14.setForeground(new java.awt.Color(11, 19, 43));

        tableUserPcManage.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "PC-ID", "PC-Name", "Category", "CPU", "RAM", "VGA", "Rate", "Status", "Action"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane5.setViewportView(tableUserPcManage);

        jTextField5.setBackground(new java.awt.Color(16, 25, 45));
        jTextField5.setForeground(new java.awt.Color(255, 255, 255));
        jTextField5.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(31, 45, 74), 1, true));
        jTextField5.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField5KeyReleased(evt);
            }
        });

        jButton4.setBackground(new java.awt.Color(0, 153, 255));
        jButton4.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton4.setForeground(new java.awt.Color(255, 255, 255));
        jButton4.setText("Search");
        jButton4.addActionListener(this::jButton4ActionPerformed);

        jButton5.setBackground(new java.awt.Color(6, 214, 160));
        jButton5.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton5.setForeground(new java.awt.Color(255, 255, 255));
        jButton5.setText("Refresh");
        jButton5.addActionListener(this::jButton5ActionPerformed);

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 1106, Short.MAX_VALUE)
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel14Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 572, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout pnlPcLayout = new javax.swing.GroupLayout(pnlPc);
        pnlPc.setLayout(pnlPcLayout);
        pnlPcLayout.setHorizontalGroup(
            pnlPcLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlPcLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        pnlPcLayout.setVerticalGroup(
            pnlPcLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlPcLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        holder.add(pnlPc, "card5");

        pnlReports.setBackground(new java.awt.Color(11, 19, 43));

        jPanel15.setBackground(new java.awt.Color(11, 19, 43));
        jPanel15.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 153, 255)), "Reports", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 0, 14), new java.awt.Color(255, 255, 255))); // NOI18N

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1118, Short.MAX_VALUE)
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 629, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout pnlReportsLayout = new javax.swing.GroupLayout(pnlReports);
        pnlReports.setLayout(pnlReportsLayout);
        pnlReportsLayout.setHorizontalGroup(
            pnlReportsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlReportsLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        pnlReportsLayout.setVerticalGroup(
            pnlReportsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlReportsLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        holder.add(pnlReports, "card4");

        pnlAdmin.setLayout(new java.awt.BorderLayout());

        adminLeft.setBackground(new java.awt.Color(255, 255, 255));
        adminLeft.setLayout(new java.awt.CardLayout());

        UserEdite.setBackground(new java.awt.Color(255, 255, 255));

        pnlUTable.setBackground(new java.awt.Color(255, 255, 255));
        pnlUTable.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 102, 255), 1, true), "Users", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Intel One Mono SemiBold", 0, 14), new java.awt.Color(102, 102, 102))); // NOI18N
        pnlUTable.setMaximumSize(new java.awt.Dimension(968, 200));
        pnlUTable.setMinimumSize(new java.awt.Dimension(968, 200));

        tableUser.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        tableUser.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Emp ID", "Full Name", "User Name", "NIC", "Email", "Phone", "Role"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tableUser.setMaximumSize(new java.awt.Dimension(450, 80));
        tableUser.setMinimumSize(new java.awt.Dimension(450, 80));
        tableUser.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableUserMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tableUser);

        javax.swing.GroupLayout pnlUTableLayout = new javax.swing.GroupLayout(pnlUTable);
        pnlUTable.setLayout(pnlUTableLayout);
        pnlUTableLayout.setHorizontalGroup(
            pnlUTableLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlUTableLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 946, Short.MAX_VALUE)
                .addContainerGap())
        );
        pnlUTableLayout.setVerticalGroup(
            pnlUTableLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlUTableLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 369, Short.MAX_VALUE)
                .addContainerGap())
        );

        userEdit.setBackground(new java.awt.Color(255, 255, 255));
        userEdit.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 204)), "Edite Details", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 0, 14), new java.awt.Color(0, 102, 204))); // NOI18N

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel1.setText("Emp No");

        txtUserNo.setEditable(false);
        txtUserNo.setBackground(new java.awt.Color(255, 255, 255));
        txtUserNo.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel2.setText("Full Name");

        lblImage.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(204, 0, 0));
        jLabel3.setText("This action will reset users user name & password !");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel4.setText("User Name");

        jToggleButton1.setBackground(new java.awt.Color(255, 193, 7));
        jToggleButton1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jToggleButton1.setForeground(new java.awt.Color(255, 255, 255));
        jToggleButton1.setText("Reset Username & Password");
        jToggleButton1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 204, 0)));
        jToggleButton1.addActionListener(this::jToggleButton1ActionPerformed);

        txtFullName.setEditable(false);
        txtFullName.setBackground(new java.awt.Color(255, 255, 255));
        txtFullName.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        txtUserName.setEditable(false);
        txtUserName.setBackground(new java.awt.Color(255, 255, 255));
        txtUserName.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        btnUserDel.setBackground(new java.awt.Color(204, 0, 0));
        btnUserDel.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnUserDel.setForeground(new java.awt.Color(255, 255, 255));
        btnUserDel.setText("Delete");
        btnUserDel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 0, 0)));
        btnUserDel.addActionListener(this::btnUserDelActionPerformed);

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel5.setText("Role");

        cmbRole.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Admin", "User" }));
        cmbRole.setToolTipText("");

        btnUserUpdate.setBackground(new java.awt.Color(0, 204, 51));
        btnUserUpdate.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnUserUpdate.setForeground(new java.awt.Color(255, 255, 255));
        btnUserUpdate.setText("Update User");
        btnUserUpdate.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 153, 0)));
        btnUserUpdate.addActionListener(this::btnUserUpdateActionPerformed);

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(204, 0, 0));
        jLabel6.setText("This action will detactivate this user!");

        jToggleButton2.setBackground(new java.awt.Color(153, 153, 153));
        jToggleButton2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jToggleButton2.setForeground(new java.awt.Color(255, 255, 255));
        jToggleButton2.setText("Deactivate User");
        jToggleButton2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 102, 102)));
        jToggleButton2.addActionListener(this::jToggleButton2ActionPerformed);

        btnClean.setBackground(new java.awt.Color(0, 153, 153));
        btnClean.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnClean.setForeground(new java.awt.Color(255, 255, 255));
        btnClean.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/clean.png"))); // NOI18N
        btnClean.setText("[F5]");
        btnClean.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 102)));
        btnClean.addActionListener(this::btnCleanActionPerformed);

        javax.swing.GroupLayout userEditLayout = new javax.swing.GroupLayout(userEdit);
        userEdit.setLayout(userEditLayout);
        userEditLayout.setHorizontalGroup(
            userEditLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(userEditLayout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(lblImage, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31)
                .addGroup(userEditLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(userEditLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(userEditLayout.createSequentialGroup()
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(txtUserNo, javax.swing.GroupLayout.DEFAULT_SIZE, 201, Short.MAX_VALUE))
                        .addGroup(userEditLayout.createSequentialGroup()
                            .addGroup(userEditLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addGroup(userEditLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(txtFullName)
                                .addComponent(txtUserName))))
                    .addGroup(userEditLayout.createSequentialGroup()
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cmbRole, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnClean, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(userEditLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 351, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jToggleButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(userEditLayout.createSequentialGroup()
                        .addComponent(btnUserUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnUserDel, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 351, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jToggleButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(22, 22, 22))
        );
        userEditLayout.setVerticalGroup(
            userEditLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(userEditLayout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(userEditLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblImage, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(userEditLayout.createSequentialGroup()
                        .addGroup(userEditLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(userEditLayout.createSequentialGroup()
                                .addGroup(userEditLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(userEditLayout.createSequentialGroup()
                                        .addGap(2, 2, 2)
                                        .addComponent(txtUserNo))
                                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(userEditLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(userEditLayout.createSequentialGroup()
                                        .addGap(2, 2, 2)
                                        .addComponent(txtFullName, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(userEditLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(txtUserName, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(userEditLayout.createSequentialGroup()
                                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jToggleButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jToggleButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(userEditLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, 31, Short.MAX_VALUE)
                            .addGroup(userEditLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(cmbRole, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(btnUserUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(btnUserDel, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(btnClean, javax.swing.GroupLayout.PREFERRED_SIZE, 30, Short.MAX_VALUE)))))
                .addGap(26, 26, 26))
        );

        javax.swing.GroupLayout UserEditeLayout = new javax.swing.GroupLayout(UserEdite);
        UserEdite.setLayout(UserEditeLayout);
        UserEditeLayout.setHorizontalGroup(
            UserEditeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(UserEditeLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(UserEditeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnlUTable, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(userEdit, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        UserEditeLayout.setVerticalGroup(
            UserEditeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(UserEditeLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnlUTable, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(userEdit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        adminLeft.add(UserEdite, "card4");

        pnlPcM.setBackground(new java.awt.Color(255, 255, 255));

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 204)), "Pc's", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 12), new java.awt.Color(0, 102, 204))); // NOI18N

        tablePc.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "PC ID", "Name", "Category", "Hourly Rate", "Status"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tablePc.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablePcMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tablePc);

        jLabel17.setFont(new java.awt.Font("Leelawadee", 0, 12)); // NOI18N
        jLabel17.setText("Search");

        txtPCSearch.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        txtPCSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtPCSearchKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtPCSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtPCSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 293, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel7.setBackground(new java.awt.Color(255, 255, 255));
        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 204)), "Change Details", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 12), new java.awt.Color(0, 102, 204))); // NOI18N
        jPanel7.setForeground(new java.awt.Color(0, 102, 204));
        jPanel7.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel7.setFont(new java.awt.Font("Leelawadee", 0, 12)); // NOI18N
        jLabel7.setText("PC ID *");
        jPanel7.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 70, 136, 30));

        txtPcId.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        jPanel7.add(txtPcId, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 70, 190, 30));

        cmbPcCategory.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "PC", "PS5" }));
        cmbPcCategory.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        cmbPcCategory.addActionListener(this::cmbPcCategoryActionPerformed);
        jPanel7.add(cmbPcCategory, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 30, 190, 30));

        jLabel8.setFont(new java.awt.Font("Leelawadee", 0, 12)); // NOI18N
        jLabel8.setText("Category *");
        jPanel7.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 30, 130, 30));

        jLabel9.setFont(new java.awt.Font("Leelawadee", 0, 12)); // NOI18N
        jLabel9.setText("PC Name *");
        jPanel7.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 110, 136, 30));

        txtName.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        jPanel7.add(txtName, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 110, 190, 30));

        btnPcDel.setBackground(new java.awt.Color(204, 51, 0));
        btnPcDel.setFont(new java.awt.Font("Inter", 1, 14)); // NOI18N
        btnPcDel.setForeground(new java.awt.Color(255, 255, 255));
        btnPcDel.setText("Delete Data");
        btnPcDel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 0, 0)));
        btnPcDel.addActionListener(this::btnPcDelActionPerformed);
        jPanel7.add(btnPcDel, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 240, 140, 30));

        btnPcAdd.setBackground(new java.awt.Color(51, 153, 255));
        btnPcAdd.setFont(new java.awt.Font("Inter", 1, 14)); // NOI18N
        btnPcAdd.setForeground(new java.awt.Color(255, 255, 255));
        btnPcAdd.setText("Add PC");
        btnPcAdd.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 204)));
        btnPcAdd.addActionListener(this::btnPcAddActionPerformed);
        jPanel7.add(btnPcAdd, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 240, 140, 30));

        jLabel14.setFont(new java.awt.Font("Leelawadee", 0, 12)); // NOI18N
        jLabel14.setText("IP Address");
        jPanel7.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 150, 136, 30));

        txtIP.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        jPanel7.add(txtIP, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 150, 190, 30));

        jLabel15.setFont(new java.awt.Font("Leelawadee", 0, 12)); // NOI18N
        jLabel15.setText("Hourly Rate *");
        jPanel7.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 190, 136, 30));

        txtHrate.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        jPanel7.add(txtHrate, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 190, 190, 30));

        btnPcUpdate.setBackground(new java.awt.Color(0, 204, 51));
        btnPcUpdate.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnPcUpdate.setForeground(new java.awt.Color(255, 255, 255));
        btnPcUpdate.setText("Update PC");
        btnPcUpdate.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 153, 51)));
        btnPcUpdate.addActionListener(this::btnPcUpdateActionPerformed);
        jPanel7.add(btnPcUpdate, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 240, 140, 30));

        btnPcClear.setBackground(new java.awt.Color(255, 193, 7));
        btnPcClear.setFont(new java.awt.Font("Inter", 1, 14)); // NOI18N
        btnPcClear.setForeground(new java.awt.Color(255, 255, 255));
        btnPcClear.setText("Clear Feilds");
        btnPcClear.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 204, 0)));
        btnPcClear.addActionListener(this::btnPcClearActionPerformed);
        jPanel7.add(btnPcClear, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 240, 140, 30));

        chkblockIP.setFont(new java.awt.Font("Leelawadee", 0, 12)); // NOI18N
        chkblockIP.setText("Block IP address");
        jPanel7.add(chkblockIP, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 210, 130, -1));

        jPanel8.setBackground(new java.awt.Color(255, 255, 255));
        jPanel8.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        txtCPU.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        jLabel10.setFont(new java.awt.Font("Leelawadee", 0, 12)); // NOI18N
        jLabel10.setText("CPU");

        jLabel11.setFont(new java.awt.Font("Leelawadee", 0, 12)); // NOI18N
        jLabel11.setText("MotherBoard");

        txtMBoard.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        jLabel12.setFont(new java.awt.Font("Leelawadee", 0, 12)); // NOI18N
        jLabel12.setText("Ram Capasity");

        txtRAM.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        jLabel13.setFont(new java.awt.Font("Leelawadee", 0, 12)); // NOI18N
        jLabel13.setText("VGA card");

        txtVGA.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addContainerGap(22, Short.MAX_VALUE)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(20, 20, 20)
                        .addComponent(txtVGA, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(20, 20, 20)
                        .addComponent(txtRAM, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(jPanel8Layout.createSequentialGroup()
                            .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(20, 20, 20)
                            .addComponent(txtMBoard, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel8Layout.createSequentialGroup()
                            .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(20, 20, 20)
                            .addComponent(txtCPU, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(16, 16, 16))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCPU, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtMBoard, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtRAM, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtVGA, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(15, 15, 15))
        );

        jPanel7.add(jPanel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 30, 440, 170));

        jPanel9.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        radioOccu.setText("Occupied");
        radioOccu.addActionListener(this::radioOccuActionPerformed);

        radioAwailable.setText("Awailable");
        radioAwailable.addActionListener(this::radioAwailableActionPerformed);

        jLabel16.setText("Status");

        radioRepair.setText("Repair");

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGap(11, 11, 11)
                        .addComponent(radioAwailable)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(radioOccu)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(radioRepair))
                    .addComponent(jLabel16))
                .addContainerGap(23, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel16)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 9, Short.MAX_VALUE)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(radioOccu)
                    .addComponent(radioRepair)
                    .addComponent(radioAwailable))
                .addContainerGap())
        );

        jPanel7.add(jPanel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(680, 210, 270, 60));

        javax.swing.GroupLayout pnlPcMLayout = new javax.swing.GroupLayout(pnlPcM);
        pnlPcM.setLayout(pnlPcMLayout);
        pnlPcMLayout.setHorizontalGroup(
            pnlPcMLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlPcMLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlPcMLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, 968, Short.MAX_VALUE))
                .addContainerGap())
        );
        pnlPcMLayout.setVerticalGroup(
            pnlPcMLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlPcMLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, 292, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        adminLeft.add(pnlPcM, "card3");

        pnlRepair.setBackground(new java.awt.Color(255, 255, 255));

        jPanel11.setBackground(new java.awt.Color(255, 255, 255));
        jPanel11.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 204)), "All PC", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Leelawadee", 1, 12), new java.awt.Color(0, 102, 204))); // NOI18N

        tableRepairPc.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "No", "PC-Name", "Category", "Status", "IP-Address"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tableRepairPc.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableRepairPcMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(tableRepairPc);

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 946, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel12.setBackground(new java.awt.Color(255, 255, 255));
        jPanel12.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)), "PC", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 12), new java.awt.Color(153, 153, 153))); // NOI18N

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "PC-ID", "PC-Name", "Repiar-ID", "Reason"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable2MouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(jTable2);

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane4)
                .addContainerGap())
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 381, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel13.setBackground(new java.awt.Color(255, 255, 255));
        jPanel13.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)), "Details", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 12), new java.awt.Color(153, 153, 153))); // NOI18N

        jTextField1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField1KeyReleased(evt);
            }
        });

        jButton1.setText("Search");
        jButton1.addActionListener(this::jButton1ActionPerformed);

        jButton2.setText("To Repair");
        jButton2.addActionListener(this::jButton2ActionPerformed);

        jButton3.setText("Clear");
        jButton3.addActionListener(this::jButton3ActionPerformed);

        jLabel18.setText("PC-ID");

        jLabel19.setText("Reason");

        jLabel20.setText("Amount");

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 226, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1))
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(9, 9, 9)
                        .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel13Layout.createSequentialGroup()
                        .addGap(9, 9, 9)
                        .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 4, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(11, 11, 11)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(8, 8, 8)
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout pnlRepairLayout = new javax.swing.GroupLayout(pnlRepair);
        pnlRepair.setLayout(pnlRepairLayout);
        pnlRepairLayout.setHorizontalGroup(
            pnlRepairLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlRepairLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlRepairLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(pnlRepairLayout.createSequentialGroup()
                        .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        pnlRepairLayout.setVerticalGroup(
            pnlRepairLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlRepairLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlRepairLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        adminLeft.add(pnlRepair, "card5");

        pnlHistory.setBackground(new java.awt.Color(255, 255, 255));

        jPanel22.setBackground(new java.awt.Color(255, 255, 255));
        jPanel22.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 153, 255)), "History & Reports", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 14), new java.awt.Color(0, 153, 255))); // NOI18N

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Invoice No", "Session ID", "Staff ID", "Invoice Date", "Payment Method", "Sub Total (Rs.)", "Net Total (Rs.)"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane6.setViewportView(jTable1);

        jLabel24.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel24.setText("User Log");

        jTable3.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Session ID", "Station / PC", "Category", "Customer", "Start Time", "End Time", "Duration (mins)", "Total Due (Rs.)"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane7.setViewportView(jTable3);

        jLabel26.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel26.setText("PC-History");

        jTable4.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Log ID", "Emp No", "Username", "Role", "Login Time", "Logout Time", "Duration", "Status"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane8.setViewportView(jTable4);

        javax.swing.GroupLayout jPanel22Layout = new javax.swing.GroupLayout(jPanel22);
        jPanel22.setLayout(jPanel22Layout);
        jPanel22Layout.setHorizontalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel22Layout.createSequentialGroup()
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel22Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel22Layout.createSequentialGroup()
                                .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(394, 394, 394)
                                .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel22Layout.createSequentialGroup()
                                .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 461, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(15, 15, 15)
                                .addComponent(jScrollPane7, javax.swing.GroupLayout.DEFAULT_SIZE, 470, Short.MAX_VALUE))))
                    .addGroup(jPanel22Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane6)))
                .addContainerGap())
        );
        jPanel22Layout.setVerticalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel22Layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6)
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(73, 73, 73)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout pnlHistoryLayout = new javax.swing.GroupLayout(pnlHistory);
        pnlHistory.setLayout(pnlHistoryLayout);
        pnlHistoryLayout.setHorizontalGroup(
            pnlHistoryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlHistoryLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel22, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        pnlHistoryLayout.setVerticalGroup(
            pnlHistoryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlHistoryLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel22, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        adminLeft.add(pnlHistory, "card2");

        pnlAdmin.add(adminLeft, java.awt.BorderLayout.CENTER);

        adminRigh.setBackground(new java.awt.Color(0, 153, 255));
        adminRigh.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 1, 0, 0));
        adminRigh.setMaximumSize(new java.awt.Dimension(160, 600));
        adminRigh.setMinimumSize(new java.awt.Dimension(160, 600));
        adminRigh.setPreferredSize(new java.awt.Dimension(160, 600));
        adminRigh.setLayout(new java.awt.CardLayout());

        adminRightCard.setBackground(new java.awt.Color(11, 19, 43));
        adminRightCard.setBorder(javax.swing.BorderFactory.createEmptyBorder(50, 0, 0, 0));
        adminRightCard.setMaximumSize(new java.awt.Dimension(155, 392));
        adminRightCard.setLayout(new java.awt.GridLayout(10, 1, 0, 10));

        btnUserAdd.setBackground(new java.awt.Color(0, 51, 153));
        btnUserAdd.setFont(new java.awt.Font("Leelawadee", 1, 14)); // NOI18N
        btnUserAdd.setForeground(new java.awt.Color(255, 255, 255));
        btnUserAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/user Add.png"))); // NOI18N
        btnUserAdd.setText("Add User");
        btnUserAdd.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 14, 1, 1));
        btnUserAdd.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnUserAdd.addActionListener(this::btnUserAddActionPerformed);
        adminRightCard.add(btnUserAdd);

        btnUserManage.setBackground(new java.awt.Color(11, 19, 43));
        btnUserManage.setFont(new java.awt.Font("Leelawadee", 1, 14)); // NOI18N
        btnUserManage.setForeground(new java.awt.Color(255, 255, 255));
        btnUserManage.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/userManege.png"))); // NOI18N
        btnUserManage.setText("User Mange");
        btnUserManage.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 14, 1, 14));
        btnUserManage.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnUserManage.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnUserManage.addActionListener(this::btnUserManageActionPerformed);
        adminRightCard.add(btnUserManage);

        btnPcManage.setBackground(new java.awt.Color(11, 19, 43));
        btnPcManage.setFont(new java.awt.Font("Leelawadee", 1, 14)); // NOI18N
        btnPcManage.setForeground(new java.awt.Color(255, 255, 255));
        btnPcManage.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/pcMange.png"))); // NOI18N
        btnPcManage.setText("Pc Manage");
        btnPcManage.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 14, 1, 14));
        btnPcManage.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnPcManage.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnPcManage.addActionListener(this::btnPcManageActionPerformed);
        adminRightCard.add(btnPcManage);

        btnToRepair.setBackground(new java.awt.Color(11, 19, 43));
        btnToRepair.setFont(new java.awt.Font("Leelawadee", 1, 14)); // NOI18N
        btnToRepair.setForeground(new java.awt.Color(255, 255, 255));
        btnToRepair.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/repair.png"))); // NOI18N
        btnToRepair.setText("To Repair");
        btnToRepair.setToolTipText("");
        btnToRepair.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 14, 1, 1));
        btnToRepair.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnToRepair.addActionListener(this::btnToRepairActionPerformed);
        adminRightCard.add(btnToRepair);

        btnHistory.setBackground(new java.awt.Color(11, 19, 43));
        btnHistory.setFont(new java.awt.Font("Leelawadee", 1, 14)); // NOI18N
        btnHistory.setForeground(new java.awt.Color(255, 255, 255));
        btnHistory.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/history.png"))); // NOI18N
        btnHistory.setText("History");
        btnHistory.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 14, 0, 0));
        btnHistory.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnHistory.addActionListener(this::btnHistoryActionPerformed);
        adminRightCard.add(btnHistory);

        btnIncome.setBackground(new java.awt.Color(11, 19, 43));
        btnIncome.setFont(new java.awt.Font("Leelawadee", 1, 14)); // NOI18N
        btnIncome.setForeground(new java.awt.Color(255, 255, 255));
        btnIncome.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/income.png"))); // NOI18N
        btnIncome.setText("Get Report");
        btnIncome.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 14, 1, 1));
        btnIncome.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnIncome.addActionListener(this::btnIncomeActionPerformed);
        adminRightCard.add(btnIncome);

        adminRigh.add(adminRightCard, "card2");

        pnlAdmin.add(adminRigh, java.awt.BorderLayout.LINE_END);

        holder.add(pnlAdmin, "card3");

        pnlSet.setBackground(new java.awt.Color(11, 19, 43));
        pnlSet.setLayout(new java.awt.BorderLayout());

        pnlAcc.setBackground(new java.awt.Color(11, 19, 43));
        pnlAcc.setMaximumSize(new java.awt.Dimension(700, 600));
        pnlAcc.setMinimumSize(new java.awt.Dimension(700, 600));
        pnlAcc.setPreferredSize(new java.awt.Dimension(700, 600));

        jPanel16.setBackground(new java.awt.Color(11, 19, 43));
        jPanel16.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 153, 255)), "Account Details", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 0, 14), new java.awt.Color(0, 102, 255))); // NOI18N

        lblUserImage.setBackground(new java.awt.Color(255, 255, 255));
        lblUserImage.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));

        jLabel27.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        jLabel27.setText("First Name");

        jLabel28.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        jLabel28.setText("Last Name");

        jLabel29.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        jLabel29.setText("User Name");

        jLabel30.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        jLabel30.setText("Email");

        jLabel31.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        jLabel31.setText("Phone");

        jLabel32.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        jLabel32.setText("NIC");

        jLabel33.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        jLabel33.setText("EMP-No");

        txtEmpNo.setEditable(false);
        txtEmpNo.setBackground(new java.awt.Color(255, 255, 255));
        txtEmpNo.setEnabled(false);
        txtEmpNo.setRequestFocusEnabled(false);

        jButton6.setBackground(new java.awt.Color(255, 204, 0));
        jButton6.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        jButton6.setForeground(new java.awt.Color(255, 255, 255));
        jButton6.setText("Clear");
        jButton6.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 153, 0)));
        jButton6.addActionListener(this::jButton6ActionPerformed);

        jButton7.setBackground(new java.awt.Color(51, 204, 0));
        jButton7.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        jButton7.setForeground(new java.awt.Color(255, 255, 255));
        jButton7.setText("Update ");
        jButton7.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 0)));
        jButton7.addActionListener(this::jButton7ActionPerformed);

        jButton8.setBackground(new java.awt.Color(204, 204, 204));
        jButton8.setText("Add Image");
        jButton8.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        jButton8.addActionListener(this::jButton8ActionPerformed);

        javax.swing.GroupLayout jPanel23Layout = new javax.swing.GroupLayout(jPanel23);
        jPanel23.setLayout(jPanel23Layout);
        jPanel23Layout.setHorizontalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel23Layout.createSequentialGroup()
                .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel23Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel23Layout.createSequentialGroup()
                        .addGap(37, 37, 37)
                        .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(lblUserImage, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton8, javax.swing.GroupLayout.DEFAULT_SIZE, 178, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 89, Short.MAX_VALUE)
                        .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel23Layout.createSequentialGroup()
                                    .addComponent(jLabel32, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(18, 18, 18)
                                    .addComponent(txtNIC, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel23Layout.createSequentialGroup()
                                    .addComponent(jLabel27, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(18, 18, 18)
                                    .addComponent(txtFName, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel23Layout.createSequentialGroup()
                                    .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(18, 18, 18)
                                    .addComponent(txtLName, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel23Layout.createSequentialGroup()
                                    .addComponent(jLabel29, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(18, 18, 18)
                                    .addComponent(txtUName, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel23Layout.createSequentialGroup()
                                    .addComponent(jLabel30, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(18, 18, 18)
                                    .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel23Layout.createSequentialGroup()
                                    .addComponent(jLabel31, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(18, 18, 18)
                                    .addComponent(txtPhone, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel23Layout.createSequentialGroup()
                                .addComponent(jLabel33, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(txtEmpNo, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGap(48, 48, 48))
        );
        jPanel23Layout.setVerticalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel23Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblUserImage, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel23Layout.createSequentialGroup()
                        .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtEmpNo, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel33, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtFName, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel27, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtLName, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtUName, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel29, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(18, 18, 18)
                .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel30, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton8))
                .addGap(18, 18, 18)
                .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtPhone, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel31, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtNIC, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel32, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(223, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel23, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel23, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout pnlAccLayout = new javax.swing.GroupLayout(pnlAcc);
        pnlAcc.setLayout(pnlAccLayout);
        pnlAccLayout.setHorizontalGroup(
            pnlAccLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlAccLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        pnlAccLayout.setVerticalGroup(
            pnlAccLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlAccLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pnlSet.add(pnlAcc, java.awt.BorderLayout.LINE_START);

        Settings.setBackground(new java.awt.Color(0, 153, 255));
        Settings.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 1, 0, 0));
        Settings.setLayout(new java.awt.CardLayout());

        jPanel10.setBackground(new java.awt.Color(11, 19, 43));

        setting1.setBackground(new java.awt.Color(11, 19, 43));
        setting1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Settings", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Intel One Mono SemiBold", 0, 14), new java.awt.Color(255, 255, 255))); // NOI18N

        jCheckBoxHam.setFont(new java.awt.Font("Intel One Mono SemiBold", 0, 14)); // NOI18N
        jCheckBoxHam.setForeground(new java.awt.Color(255, 255, 255));
        jCheckBoxHam.setText("Toggle Button off");
        jCheckBoxHam.addItemListener(this::jCheckBoxHamItemStateChanged);
        jCheckBoxHam.addActionListener(this::jCheckBoxHamActionPerformed);

        checkAccEdite.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        checkAccEdite.setForeground(new java.awt.Color(255, 255, 255));
        checkAccEdite.setText("Update Profile");
        checkAccEdite.addItemListener(this::checkAccEditeItemStateChanged);

        javax.swing.GroupLayout setting1Layout = new javax.swing.GroupLayout(setting1);
        setting1.setLayout(setting1Layout);
        setting1Layout.setHorizontalGroup(
            setting1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(setting1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(setting1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jCheckBoxHam)
                    .addComponent(checkAccEdite))
                .addContainerGap(279, Short.MAX_VALUE))
        );
        setting1Layout.setVerticalGroup(
            setting1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(setting1Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jCheckBoxHam)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(checkAccEdite)
                .addContainerGap(557, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(setting1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(setting1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        Settings.add(jPanel10, "card2");

        pnlSet.add(Settings, java.awt.BorderLayout.CENTER);

        holder.add(pnlSet, "card7");

        main.add(holder, java.awt.BorderLayout.CENTER);

        contentPnl.add(main, java.awt.BorderLayout.CENTER);

        framePnl.add(contentPnl, java.awt.BorderLayout.CENTER);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(framePnl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(framePnl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnDashActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDashActionPerformed
        pnlDash.setVisible(true);
        pnlLive.setVisible(false);
        pnlPc.setVisible(false);
        pnlReports.setVisible(false);
        pnlAdmin.setVisible(false);
        pnlSet.setVisible(false);

    }//GEN-LAST:event_btnDashActionPerformed

    private void btnLiveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLiveActionPerformed
        pnlLive.setVisible(true);
        pnlDash.setVisible(false);
        pnlPc.setVisible(false);
        pnlReports.setVisible(false);
        pnlAdmin.setVisible(false);
        pnlSet.setVisible(false);
        loadLiveSessionsTable();
    }//GEN-LAST:event_btnLiveActionPerformed

    private void txtLiveSearchKeyReleased(java.awt.event.KeyEvent evt) {
        String keyword = txtLiveSearch.getText().trim();
        loadLiveSessionsTable(keyword);
    }

    private void btnLiveSearchActionPerformed(java.awt.event.ActionEvent evt) {
        String keyword = txtLiveSearch.getText().trim();
        loadLiveSessionsTable(keyword);
    }

    private void btnLiveRefreshActionPerformed(java.awt.event.ActionEvent evt) {
        txtLiveSearch.setText("");
        loadLiveSessionsTable();
    }

    private void btnPcActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPcActionPerformed
        pnlPc.setVisible(true);
        pnlDash.setVisible(false);
        pnlLive.setVisible(false);
        pnlReports.setVisible(false);
        pnlAdmin.setVisible(false);
        pnlSet.setVisible(false);
        loadUserPctable();
    }//GEN-LAST:event_btnPcActionPerformed

    private void jTextField5KeyReleased(java.awt.event.KeyEvent evt) {
        String searchKey = jTextField5.getText().trim();
        loadUserPctable(searchKey);
    }

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {
        String searchKey = jTextField5.getText().trim();
        loadUserPctable(searchKey);
    }

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {
        jTextField5.setText("");
        loadUserPctable();
    }

    private void btnReportsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReportsActionPerformed
        pnlPc.setVisible(false);
        pnlDash.setVisible(false);
        pnlLive.setVisible(false);
        pnlReports.setVisible(true);
        pnlAdmin.setVisible(false);
        pnlSet.setVisible(false);
        showReportGenerationDialog();
    }//GEN-LAST:event_btnReportsActionPerformed

    // Get Report button action in Admin History
    private void btnIncomeActionPerformed(java.awt.event.ActionEvent evt) {
        showReportGenerationDialog();
    }

    // Interactive Report Generation Dialog for Admin / Reports
    public void showReportGenerationDialog() {
        String[] options = {
            "1. User Activity Logs Report (PDF)",
            "2. PC Session History & Usage Report (PDF)",
            "3. Invoices & Revenue Billing Report (PDF)",
            "4. PC Inventory & Stations Report (PDF)",
            "5. PC Maintenance & Repair Report (PDF)",
            "Cancel"
        };

        int choice = JOptionPane.showOptionDialog(
            this,
            "Select the Report you would like to generate and export as PDF:",
            "Generate Cafe PDF Report",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            options,
            options[0]
        );

        switch (choice) {
            case 0:
                loadUserLogsTable();
                exportTableToPdf(jTable4, "User Activity Logs Report", "User_Logs_Report");
                break;
            case 1:
                loadSessionHistoryTable();
                exportTableToPdf(jTable3, "PC Session History & Usage Report", "PC_Session_History_Report");
                break;
            case 2:
                loadInvoiceHistoryTable();
                exportTableToPdf(jTable1, "Invoices & Revenue Billing Report", "Invoices_Billing_Report");
                break;
            case 3:
                loadPcTable();
                exportTableToPdf(tablePc, "PC Inventory & Stations Report", "PC_Inventory_Report");
                break;
            case 4:
                loadRepairHistoryTable();
                exportTableToPdf(tableRepairPc, "PC Maintenance & Repair Report", "PC_Repair_Report");
                break;
            default:
                break;
        }
    }

    private void btnAdminActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAdminActionPerformed
        pnlPc.setVisible(false);
        pnlDash.setVisible(false);
        pnlLive.setVisible(false);
        pnlReports.setVisible(false);
        pnlSet.setVisible(false);
        pnlAdmin.setVisible(true);
    }//GEN-LAST:event_btnAdminActionPerformed

    private void btnHamActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHamActionPerformed
        Thread th = new Thread() {
            @Override
            public void run() {
                try {

                    if (isSidebarOpen) {
                        btnDash.setText("");
                        btnReports.setText("");
                        btnLive.setText("");
                        btnPc.setText("");
                        btnAdmin.setText("");
                        btnLogOut.setText("");
                        btnAcc.setText("");

                        for (int i = 140; i >= 60; i -= 10) {
                            Thread.sleep(15);
                            sidebarPnl.setPreferredSize(new java.awt.Dimension(i, sidebarPnl.getHeight()));
                            javax.swing.SwingUtilities.updateComponentTreeUI(sidebarPnl);
                        }
                        isSidebarOpen = false;

                    } else {

                        for (int i = 60; i <= 140; i += 10) {
                            Thread.sleep(15);
                            sidebarPnl.setPreferredSize(new java.awt.Dimension(i, sidebarPnl.getHeight()));
                            javax.swing.SwingUtilities.updateComponentTreeUI(sidebarPnl);

                            btnDash.setText("Dashboard");
                            btnReports.setText("Reports");
                            btnLive.setText("Live");
                            btnPc.setText("Pc");
                            btnAdmin.setText("Admin");
                            btnLogOut.setText("LogOut");
                            btnAcc.setText("Settings");

                        }
                        isSidebarOpen = true;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        th.start();
    }//GEN-LAST:event_btnHamActionPerformed

    private void btnAccActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAccActionPerformed
        pnlPc.setVisible(false);
        pnlDash.setVisible(false);
        pnlLive.setVisible(false);
        pnlReports.setVisible(false);
        pnlAdmin.setVisible(false);
        pnlSet.setVisible(true);
        loadLoggedInUserSettings();
    }//GEN-LAST:event_btnAccActionPerformed

    private void jCheckBoxHamActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxHamActionPerformed

    }//GEN-LAST:event_jCheckBoxHamActionPerformed

    private void btnUserAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUserAddActionPerformed
        UserReg reg = new UserReg();
        reg.setVisible(true);
    }//GEN-LAST:event_btnUserAddActionPerformed

    // User delete action
    private void btnUserDelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUserDelActionPerformed
        String empNo = txtUserNo.getText().trim();
        if (empNo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select a user from the table first!", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to delete user " + empNo + " permanently?",
            "Confirm Delete User",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                pst = db.con.prepareStatement("DELETE FROM user WHERE emp_no = ?");
                pst.setString(1, empNo);
                int deleted = pst.executeUpdate();
                pst.close();

                if (deleted > 0) {
                    JOptionPane.showMessageDialog(this, "User deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    btnCleanActionPerformed(evt);
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to delete user.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_btnUserDelActionPerformed

    // User update action
    private void btnUserUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUserUpdateActionPerformed
        String empNo = txtUserNo.getText().trim();
        String fullName = txtFullName.getText().trim();
        String uName = txtUserName.getText().trim();
        String role = cmbRole.getSelectedItem() != null ? cmbRole.getSelectedItem().toString() : "Staff";

        if (empNo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select a user from the table first!", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (fullName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter User Full Name!", "Validation Error", JOptionPane.WARNING_MESSAGE);
            txtFullName.requestFocus();
            return;
        }

        if (uName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a Username!", "Validation Error", JOptionPane.WARNING_MESSAGE);
            txtUserName.requestFocus();
            return;
        }

        if (uName.length() < 3) {
            JOptionPane.showMessageDialog(this, "Username must be at least 3 characters long!", "Validation Error", JOptionPane.WARNING_MESSAGE);
            txtUserName.requestFocus();
            return;
        }

        String[] parts = fullName.split(" ", 2);
        String fName = parts.length > 0 ? parts[0] : "";
        String lName = parts.length > 1 ? parts[1] : "";

        try {
            // Check username duplicate for other users
            pst = db.con.prepareStatement("SELECT emp_no FROM user WHERE username = ? AND emp_no != ?");
            pst.setString(1, uName);
            pst.setString(2, empNo);
            rs = pst.executeQuery();
            if (rs.next()) {
                JOptionPane.showMessageDialog(this, "This username is already in use by another user!", "Duplicate Error", JOptionPane.WARNING_MESSAGE);
                pst.close();
                rs.close();
                txtUserName.requestFocus();
                return;
            }
            pst.close();
            rs.close();

            pst = db.con.prepareStatement("UPDATE user SET f_name = ?, l_name = ?, username = ?, role = ? WHERE emp_no = ?");
            pst.setString(1, fName);
            pst.setString(2, lName);
            pst.setString(3, uName);
            pst.setString(4, role);
            pst.setString(5, empNo);

            int updated = pst.executeUpdate();
            pst.close();

            if (updated > 0) {
                JOptionPane.showMessageDialog(this, "User updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadUserTable();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update user.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnUserUpdateActionPerformed

    // Reset User Username and Password to Default (NIC)
    private void jToggleButton1ActionPerformed(java.awt.event.ActionEvent evt) {
        String empNo = txtUserNo.getText().trim();
        if (empNo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select a user from the table first!", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            pst = db.con.prepareStatement("SELECT nic, username FROM user WHERE emp_no = ?");
            pst.setString(1, empNo);
            rs = pst.executeQuery();

            if (rs.next()) {
                String nic = rs.getString("nic");
                if (nic == null || nic.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Selected user does not have an NIC recorded!", "Error", JOptionPane.ERROR_MESSAGE);
                    pst.close();
                    rs.close();
                    return;
                }

                int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Reset Username and Password for user " + empNo + " to default NIC (" + nic + ")?",
                    "Confirm Credential Reset",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE
                );

                if (confirm == JOptionPane.YES_OPTION) {
                    String defaultUsername = nic.trim();
                    String defaultPasswordHash = hashPassword(nic.trim());

                    pst.close();
                    rs.close();

                    pst = db.con.prepareStatement("UPDATE user SET username = ?, password = ? WHERE emp_no = ?");
                    pst.setString(1, defaultUsername);
                    pst.setString(2, defaultPasswordHash);
                    pst.setString(3, empNo);

                    int updated = pst.executeUpdate();
                    pst.close();

                    if (updated > 0) {
                        JOptionPane.showMessageDialog(this, "Credentials reset successfully!\nUsername: " + defaultUsername + "\nPassword: [NIC]", "Success", JOptionPane.INFORMATION_MESSAGE);
                        txtUserName.setText(defaultUsername);
                        loadUserTable();
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "User not found!", "Error", JOptionPane.ERROR_MESSAGE);
                pst.close();
                rs.close();
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Deactivate / Activate user (Toggle status)
    private void jToggleButton2ActionPerformed(java.awt.event.ActionEvent evt) {
        String empNo = txtUserNo.getText().trim();
        if (empNo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select a user from the table first!", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            pst = db.con.prepareStatement("SELECT role FROM user WHERE emp_no = ?");
            pst.setString(1, empNo);
            rs = pst.executeQuery();

            if (rs.next()) {
                String currentRole = rs.getString("role");
                String newRole = (currentRole != null && currentRole.equalsIgnoreCase("Inactive")) ? "Staff" : "Inactive";
                String actionText = newRole.equalsIgnoreCase("Inactive") ? "deactivate" : "activate";

                int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to " + actionText + " user " + empNo + "?",
                    "Confirm User Status Change",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
                );

                if (confirm == JOptionPane.YES_OPTION) {
                    pst.close();
                    rs.close();

                    pst = db.con.prepareStatement("UPDATE user SET role = ? WHERE emp_no = ?");
                    pst.setString(1, newRole);
                    pst.setString(2, empNo);
                    pst.executeUpdate();
                    pst.close();

                    cmbRole.setSelectedItem(newRole);
                    JOptionPane.showMessageDialog(this, "User status updated to: " + newRole, "Success", JOptionPane.INFORMATION_MESSAGE);
                    loadUserTable();
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Clean user edit fields [F5]
    private void btnCleanActionPerformed(java.awt.event.ActionEvent evt) {
        txtUserNo.setText("");
        txtFullName.setText("");
        txtUserName.setText("");
        if (cmbRole.getItemCount() > 0) {
            cmbRole.setSelectedIndex(0);
        }
        lblImage.setIcon(null);
        lblImage.setText("No Image set yet...");
        btnUserUpdate.setVisible(false);
        btnUserDel.setVisible(false);
        loadUserTable();
    }

    private void tableUserMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableUserMouseClicked
        if (evt.getClickCount() == 2) {
            int selectedRow = tableUser.getSelectedRow();
            if (selectedRow != -1) {
                String selectUID = tableUser.getValueAt(selectedRow, 0).toString();
                try {
                    pst = db.con.prepareStatement("SELECT * FROM user WHERE emp_no = ?");
                    pst.setString(1, selectUID);
                    rs = pst.executeQuery();

                    if (rs.next()) {
                        txtUserNo.setText(rs.getString("emp_no"));
                        txtFullName.setText(rs.getString("f_name") + " " + rs.getString("l_name"));
                        txtUserName.setText(rs.getString("username"));
                        cmbRole.setSelectedItem(rs.getString("role"));

                        //image load 
                        byte[] imgByte = rs.getBytes("image");
                        if (imgByte != null) {
                            ImageIcon img = new ImageIcon(imgByte);
                            Image scalledImage = img.getImage().getScaledInstance(lblImage.getWidth(), lblImage.getHeight(), Image.SCALE_SMOOTH);
                            lblImage.setIcon(new ImageIcon(scalledImage));

                        } else {
                            lblImage.setIcon(null);
                            lblImage.setText("No Iamge set yet...");
                        }

                    }
                } catch (SQLException ex) {
                    System.getLogger(Dash.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
                }
            }
        }
        btnUserUpdate.setVisible(true);
        btnUserDel.setVisible(true);
    }//GEN-LAST:event_tableUserMouseClicked

    private void btnPcManageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPcManageActionPerformed
        UserEdite.setVisible(false);
        pnlPcM.setVisible(true);
        pnlHistory.setVisible(false);
        pnlRepair.setVisible(false);
    }//GEN-LAST:event_btnPcManageActionPerformed

    private void btnUserManageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUserManageActionPerformed
        UserEdite.setVisible(true);
        pnlPcM.setVisible(false);
        pnlHistory.setVisible(false);
        pnlRepair.setVisible(false);
    }//GEN-LAST:event_btnUserManageActionPerformed

    private void btnHistoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHistoryActionPerformed
        UserEdite.setVisible(false);
        pnlPcM.setVisible(false);
        pnlHistory.setVisible(true);
        pnlRepair.setVisible(false);
        loadUserLogsTable();
        loadSessionHistoryTable();
        loadInvoiceHistoryTable();
    }//GEN-LAST:event_btnHistoryActionPerformed

    private void btnPcDelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPcDelActionPerformed
        String id = txtPcId.getText().trim();
        if (id.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select a PC!");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, id + " mema system eken sampurnayenma makala danna oona da?", "Delete Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                pst = db.con.prepareStatement("DELETE FROM pc_table WHERE pc_id=?");
                pst.setString(1, id);

                int success = pst.executeUpdate();

                if (success > 0) {
                    JOptionPane.showMessageDialog(this, "Record eka sarthakawa makala damana ladi!");
                    loadPcTable();
                    loadPcRepairTable();

                    clear();

                } else {
                    JOptionPane.showMessageDialog(this, "Delete kireemata haki wuye naha!");
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Delete Error: " + e.getMessage());
            }
        }

    }//GEN-LAST:event_btnPcDelActionPerformed

    private void btnPcAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPcAddActionPerformed

        String pcID = txtPcId.getText().trim();
        String pcName = txtName.getText().trim();
        String category = cmbPcCategory.getSelectedItem() != null ? cmbPcCategory.getSelectedItem().toString() : "PC";
        String cpu = txtCPU.getText().trim();
        String board = txtMBoard.getText().trim();
        String ram = txtRAM.getText().trim();
        String vga = txtVGA.getText().trim();
        String ip = txtIP.getText().trim();
        String rateStr = txtHrate.getText().trim();
        String status = "";

        if (radioAwailable.isSelected()) {
            status = "Available";
        } else if (radioOccu.isSelected()) {
            status = "Occupied";
        } else if (radioRepair.isSelected()) {
            status = "Repair";
        } else {
            status = "Available";
        }

        // 1. Required field validations
        if (pcID.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter or select a PC ID!", "Validation Error", JOptionPane.WARNING_MESSAGE);
            txtPcId.requestFocus();
            return;
        }

        if (pcName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a PC Name!", "Validation Error", JOptionPane.WARNING_MESSAGE);
            txtName.requestFocus();
            return;
        }

        if (rateStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter an Hourly Rate!", "Validation Error", JOptionPane.WARNING_MESSAGE);
            txtHrate.requestFocus();
            return;
        }

        // 2. Numeric validation for hourly rate
        double rateVal = 0.0;
        try {
            rateVal = Double.parseDouble(rateStr);
            if (rateVal <= 0) {
                JOptionPane.showMessageDialog(this, "Hourly rate must be greater than 0!", "Validation Error", JOptionPane.WARNING_MESSAGE);
                txtHrate.requestFocus();
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid numeric hourly rate (e.g. 150.00)!", "Validation Error", JOptionPane.WARNING_MESSAGE);
            txtHrate.requestFocus();
            return;
        }

        try {
            // 3. Duplicate check for PC ID
            pst = db.con.prepareStatement("SELECT pc_id FROM pc_table WHERE pc_id = ?");
            pst.setString(1, pcID);
            rs = pst.executeQuery();
            if (rs.next()) {
                JOptionPane.showMessageDialog(this, "A Station/PC with ID (" + pcID + ") already exists!", "Duplicate Error", JOptionPane.ERROR_MESSAGE);
                pst.close();
                rs.close();
                txtPcId.requestFocus();
                return;
            }
            pst.close();
            rs.close();

            pst = db.con.prepareStatement("INSERT INTO pc_table(pc_id, pc_name, category, cpu, motherboard, ram_capacity, vga, ip_address, hourly_rate, status) VALUES(?,?,?,?,?,?,?,?,?,?)");
            pst.setString(1, pcID);
            pst.setString(2, pcName);
            pst.setString(3, category);
            pst.setString(4, cpu);
            pst.setString(5, board);
            pst.setString(6, ram);
            pst.setString(7, vga);
            pst.setString(8, ip);
            pst.setString(9, String.format("%.2f", rateVal));
            pst.setString(10, status);

            int success = pst.executeUpdate();
            pst.close();

            if (success > 0) {
                JOptionPane.showMessageDialog(this, "PC/Console added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);

                loadPcTable();
                loadPcRepairTable();
                loadPcGrid();
                loadSummaryCards();
                loadUserPctable();
                clear();
            }

            // Save QR Code for the PC
            QRCodeGenerator.saveQRCodeAsFile(pcID, pcName);

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnPcAddActionPerformed

    private void cmbPcCategoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbPcCategoryActionPerformed

        if (isTableClick) {
            return;
        }
        try {
            // Random ID generate
            String category = cmbPcCategory.getSelectedItem().toString().toUpperCase();
            generatePcId(category);

            // Sequential name generation
            pst = db.con.prepareStatement("SELECT MAX(pc_name) FROM pc_table WHERE category = ?");
            pst.setString(1, category);
            rs = pst.executeQuery();

            if (rs.next() && rs.getString(1) != null) {
                String lastId = rs.getString(1);
                String numberPart = lastId.replaceAll("[^0-9]", "");
                int idNum = Integer.parseInt(numberPart);

                idNum++;

                txtName.setText(category + "-" + String.format("%02d", idNum));
            } else {
                txtName.setText(category + "-01");
            }

            pst.close();
            rs.close();

            radioAwailable.setSelected(true);

        } catch (SQLException ex) {
            System.getLogger(Dash.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }

    }//GEN-LAST:event_cmbPcCategoryActionPerformed

    private void radioOccuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radioOccuActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_radioOccuActionPerformed

    private void radioAwailableActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radioAwailableActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_radioAwailableActionPerformed

    private void tablePcMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablePcMouseClicked

        isTableClick = true;
        try {
            if (evt.getClickCount() == 2) {
                int selectedRow = tablePc.getSelectedRow();
                if (selectedRow != -1) {
                    String selectPcID = tablePc.getValueAt(selectedRow, 0).toString();

                    pst = db.con.prepareStatement("SELECT * FROM pc_table WHERE pc_id = ?");
                    pst.setString(1, selectPcID);
                    rs = pst.executeQuery();

                    if (rs.next()) {
                        txtPcId.setText(rs.getString("pc_id"));
                        txtName.setText(rs.getString("pc_name"));

                        String category = rs.getString("category");
                        if (category != null) {
                            cmbPcCategory.setSelectedItem(category);
                        }

                        txtCPU.setText(rs.getString("cpu") == null ? "" : rs.getString("cpu"));
                        txtMBoard.setText(rs.getString("motherboard") == null ? "" : rs.getString("motherboard"));
                        txtRAM.setText(rs.getString("ram_capacity") == null ? "" : rs.getString("ram_capacity"));
                        txtVGA.setText(rs.getString("vga") == null ? "" : rs.getString("vga"));
                        txtIP.setText(rs.getString("ip_address") == null ? "" : rs.getString("ip_address"));
                        txtHrate.setText(rs.getString("hourly_rate"));

                        String status = rs.getString("status");
                        if (status != null) {
                            if (status.equalsIgnoreCase("Available") || status.equalsIgnoreCase("Awailable")) {
                                radioAwailable.setSelected(true);
                            } else if (status.equalsIgnoreCase("Occupied")) {
                                radioOccu.setSelected(true);
                            } else if (status.equalsIgnoreCase("Repair") || status.equalsIgnoreCase("Repiar")) {
                                radioRepair.setSelected(true);
                            }
                        }
                    }
                    pst.close();
                    rs.close();
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Data load error: " + ex.getMessage());
        } finally {
            isTableClick = false;
        }
        btnPcUpdate.setVisible(true);
        btnPcDel.setVisible(true);
        btnPcClear.setVisible(true);
    }//GEN-LAST:event_tablePcMouseClicked

    private void btnPcUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPcUpdateActionPerformed

        String id = txtPcId.getText().trim();
        String ip = txtIP.getText().trim();
        String rateStr = txtHrate.getText().trim();

        String cpu = txtCPU.getText().trim();
        String mobo = txtMBoard.getText().trim();
        String ram = txtRAM.getText().trim();
        String vga = txtVGA.getText().trim();

        String status = "";
        if (radioAwailable.isSelected()) {
            status = "Available";
        } else if (radioOccu.isSelected()) {
            status = "Occupied";
        } else if (radioRepair.isSelected()) {
            status = "Repair";
        }

        if (id.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select a PC from the table first!", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (rateStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter an Hourly Rate!", "Validation Error", JOptionPane.WARNING_MESSAGE);
            txtHrate.requestFocus();
            return;
        }

        double rateVal = 0.0;
        try {
            rateVal = Double.parseDouble(rateStr);
            if (rateVal <= 0) {
                JOptionPane.showMessageDialog(this, "Hourly rate must be greater than 0!", "Validation Error", JOptionPane.WARNING_MESSAGE);
                txtHrate.requestFocus();
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid numeric hourly rate (e.g. 150.00)!", "Validation Error", JOptionPane.WARNING_MESSAGE);
            txtHrate.requestFocus();
            return;
        }

        try {
            pst = db.con.prepareStatement("UPDATE pc_table SET ip_address=?, hourly_rate=?, cpu=?, motherboard=?, ram_capacity=?, vga=?, status=? WHERE pc_id=?");
            pst.setString(1, ip);
            pst.setString(2, String.format("%.2f", rateVal));
            pst.setString(3, cpu);
            pst.setString(4, mobo);
            pst.setString(5, ram);
            pst.setString(6, vga);
            pst.setString(7, status);
            pst.setString(8, id);

            int success = pst.executeUpdate();
            pst.close();

            if (success > 0) {
                JOptionPane.showMessageDialog(this, "PC details updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadPcTable();
                loadPcRepairTable();
                loadPcGrid();
                loadSummaryCards();
                loadUserPctable();
                clear();
            } else {
                JOptionPane.showMessageDialog(this, "Update Failed. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Update Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnPcUpdateActionPerformed

    private void btnPcClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPcClearActionPerformed
        clear();
    }//GEN-LAST:event_btnPcClearActionPerformed

    private void btnToRepairActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnToRepairActionPerformed
        UserEdite.setVisible(false);
        pnlPcM.setVisible(false);
        pnlHistory.setVisible(false);
        pnlRepair.setVisible(true);
        loadPcRepairTable();
        loadRepairHistoryTable();
    }//GEN-LAST:event_btnToRepairActionPerformed

    private void tableRepairPcMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableRepairPcMouseClicked
        int selectedRow = tableRepairPc.getSelectedRow();
        if (selectedRow != -1) {
            String pcId = tableRepairPc.getValueAt(selectedRow, 0).toString();
            jTextField2.setText(pcId);
        }
    }//GEN-LAST:event_tableRepairPcMouseClicked

    private void jTable2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable2MouseClicked
        int selectedRow = jTable2.getSelectedRow();
        if (selectedRow != -1) {
            String pcId = jTable2.getValueAt(selectedRow, 0) != null ? jTable2.getValueAt(selectedRow, 0).toString() : "";
            String reason = jTable2.getValueAt(selectedRow, 3) != null ? jTable2.getValueAt(selectedRow, 3).toString() : "";
            jTextField2.setText(pcId);
            jTextField3.setText(reason);
        }
    }//GEN-LAST:event_jTable2MouseClicked

    private void jTextField1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField1KeyReleased
        searchRepairPc();
    }//GEN-LAST:event_jTextField1KeyReleased

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        searchRepairPc();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        String pcId = jTextField2.getText().trim();
        String reason = jTextField3.getText().trim();
        String amountStr = jTextField4.getText().trim();

        if (pcId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select or enter a PC ID!", "Validation Error", JOptionPane.WARNING_MESSAGE);
            jTextField2.requestFocus();
            return;
        }

        if (reason.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter the repair/maintenance reason!", "Validation Error", JOptionPane.WARNING_MESSAGE);
            jTextField3.requestFocus();
            return;
        }

        double cost = 0.0;
        if (!amountStr.isEmpty()) {
            try {
                cost = Double.parseDouble(amountStr);
                if (cost < 0) {
                    JOptionPane.showMessageDialog(this, "Repair cost cannot be a negative value!", "Validation Error", JOptionPane.WARNING_MESSAGE);
                    jTextField4.requestFocus();
                    return;
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Please enter a valid numeric repair cost (e.g. 1200.00)!", "Validation Error", JOptionPane.WARNING_MESSAGE);
                jTextField4.requestFocus();
                return;
            }
        }

        try {
            pst = db.con.prepareStatement("SELECT status FROM pc_table WHERE pc_id = ?");
            pst.setString(1, pcId);
            rs = pst.executeQuery();

            if (!rs.next()) {
                JOptionPane.showMessageDialog(this, "Station/PC not found with ID: " + pcId, "Error", JOptionPane.ERROR_MESSAGE);
                pst.close();
                rs.close();
                return;
            }
            pst.close();
            rs.close();

            pst = db.con.prepareStatement("INSERT INTO pc_maintenance (pc_id, issue_description, cost, repair_date) VALUES (?, ?, ?, NOW())");
            pst.setString(1, pcId);
            pst.setString(2, reason);
            pst.setDouble(3, cost);
            pst.executeUpdate();
            pst.close();

            pst = db.con.prepareStatement("UPDATE pc_table SET status = 'Repair' WHERE pc_id = ?");
            pst.setString(1, pcId);
            pst.executeUpdate();
            pst.close();

            JOptionPane.showMessageDialog(this, "Station " + pcId + " successfully sent to repair/maintenance!", "Success", JOptionPane.INFORMATION_MESSAGE);

            loadPcRepairTable();
            loadRepairHistoryTable();
            loadPcTable();
            loadPcGrid();
            loadSummaryCards();
            loadUserPctable();

            clearRepair();

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        clearRepair();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void txtPCSearchKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPCSearchKeyReleased
        String rawSearchData = txtPCSearch.getText();
        String numberOnly = rawSearchData.replaceAll("[^0-9]", "");

        if (!numberOnly.isEmpty() && numberOnly.length() == 1) {
            numberOnly = "0" + numberOnly;
        }
        try {
            DefaultTableModel dtm = (DefaultTableModel) tablePc.getModel();
            dtm.setRowCount(0);

            String sql = "SELECT * FROM pc_table WHERE pc_id LIKE ? OR pc_name LIKE ? ORDER BY pc_id ASC";
            pst = db.con.prepareStatement(sql);

            String searchParam = numberOnly.isEmpty() ? rawSearchData : numberOnly;

            // '%' pawichi karana nisa, user '1' kiyala gahuwath 'PC-01' kiyana eka auto match wenawa
            pst.setString(1, "%" + searchParam + "%");
            pst.setString(2, "%" + searchParam + "%");

            rs = pst.executeQuery();

            while (rs.next()) {
                Vector v = new Vector();
                v.add(rs.getString("pc_id"));
                v.add(rs.getString("pc_name"));
                v.add(rs.getString("category"));
                v.add(rs.getString("hourly_rate"));
                v.add(rs.getString("status"));

                dtm.addRow(v);
            }

        } catch (Exception e) {
            System.out.println("Search Error: " + e.getMessage());
        }
    }//GEN-LAST:event_txtPCSearchKeyReleased

    private void jCheckBoxHamItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCheckBoxHamItemStateChanged
        if (jCheckBoxHam.isSelected()) {
            btnHam.setVisible(false);
        } else {
            btnHam.setVisible(true);
        }
    }//GEN-LAST:event_jCheckBoxHamItemStateChanged

    private void checkAccEditeItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_checkAccEditeItemStateChanged
        updateSettingsFieldsEditableState();
    }//GEN-LAST:event_checkAccEditeItemStateChanged

    private void updateSettingsFieldsEditableState() {
        boolean canEdit = checkAccEdite.isSelected();
        txtFName.setEditable(canEdit);
        txtLName.setEditable(canEdit);
        txtUName.setEditable(canEdit);
        txtNIC.setEditable(canEdit);
        txtPhone.setEditable(canEdit);
        txtEmail.setEditable(canEdit);

        txtFName.setRequestFocusEnabled(canEdit);
        txtLName.setRequestFocusEnabled(canEdit);
        txtUName.setRequestFocusEnabled(canEdit);
        txtNIC.setRequestFocusEnabled(canEdit);
        txtPhone.setRequestFocusEnabled(canEdit);
        txtEmail.setRequestFocusEnabled(canEdit);

        jButton7.setEnabled(canEdit);
        jButton8.setEnabled(canEdit);
        jButton6.setEnabled(canEdit);

        txtEmpNo.setEditable(false);
        txtEmpNo.setEnabled(false);
    }

    private void loadLoggedInUserSettings() {
        if (user == null) {
            return;
        }

        String empNo = user.getEmpNo();
        String uName = user.getUserName();

        try {
            if (empNo != null && !empNo.trim().isEmpty()) {
                pst = db.con.prepareStatement("SELECT * FROM user WHERE emp_no = ?");
                pst.setString(1, empNo);
            } else if (uName != null && !uName.trim().isEmpty()) {
                pst = db.con.prepareStatement("SELECT * FROM user WHERE username = ?");
                pst.setString(1, uName);
            } else {
                return;
            }

            rs = pst.executeQuery();
            if (rs.next()) {
                txtEmpNo.setText(rs.getString("emp_no") != null ? rs.getString("emp_no") : "");
                txtFName.setText(rs.getString("f_name") != null ? rs.getString("f_name") : "");
                txtLName.setText(rs.getString("l_name") != null ? rs.getString("l_name") : "");
                txtUName.setText(rs.getString("username") != null ? rs.getString("username") : "");
                txtEmail.setText(rs.getString("email") != null ? rs.getString("email") : "");
                txtPhone.setText(rs.getString("phone") != null ? rs.getString("phone") : "");
                txtNIC.setText(rs.getString("nic") != null ? rs.getString("nic") : "");

                byte[] imgByte = rs.getBytes("image");
                currentProfileImageBytes = imgByte;

                if (imgByte != null && imgByte.length > 0) {
                    ImageIcon imgIcon = new ImageIcon(imgByte);
                    int lblW = lblUserImage.getWidth() > 0 ? lblUserImage.getWidth() : 178;
                    int lblH = lblUserImage.getHeight() > 0 ? lblUserImage.getHeight() : 172;
                    Image scalledImage = imgIcon.getImage().getScaledInstance(lblW, lblH, Image.SCALE_SMOOTH);
                    lblUserImage.setIcon(new ImageIcon(scalledImage));
                    lblUserImage.setText("");
                } else {
                    lblUserImage.setIcon(null);
                    lblUserImage.setText("No Image");
                    lblUserImage.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
                    lblUserImage.setForeground(new Color(200, 200, 200));
                }
            }
            pst.close();
            rs.close();
        } catch (SQLException ex) {
            System.getLogger(Dash.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }

        updateSettingsFieldsEditableState();
    }

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        if (!checkAccEdite.isSelected()) {
            JOptionPane.showMessageDialog(this, "Please check 'Update Profile' to enable editing!", "Notice", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Select Profile Image");
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Image Files (*.jpg, *.jpeg, *.png, *.gif)", "jpg", "jpeg", "png", "gif");
        chooser.setFileFilter(filter);

        int result = chooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = chooser.getSelectedFile();
            try {
                java.nio.file.Path path = selectedFile.toPath();
                byte[] bytes = java.nio.file.Files.readAllBytes(path);
                this.currentProfileImageBytes = bytes;

                ImageIcon icon = new ImageIcon(bytes);
                int lblW = lblUserImage.getWidth() > 0 ? lblUserImage.getWidth() : 178;
                int lblH = lblUserImage.getHeight() > 0 ? lblUserImage.getHeight() : 172;
                Image scaled = icon.getImage().getScaledInstance(lblW, lblH, Image.SCALE_SMOOTH);
                lblUserImage.setIcon(new ImageIcon(scaled));
                lblUserImage.setText("");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Failed to load selected image: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        if (!checkAccEdite.isSelected()) {
            JOptionPane.showMessageDialog(this, "Please check 'Update Profile' to enable editing!", "Notice", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String empNo = txtEmpNo.getText().trim();
        String fName = txtFName.getText().trim();
        String lName = txtLName.getText().trim();
        String uName = txtUName.getText().trim();
        String email = txtEmail.getText().trim();
        String phone = txtPhone.getText().trim();
        String nic = txtNIC.getText().trim();

        // 1. Validation
        if (fName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter First Name!", "Validation Error", JOptionPane.WARNING_MESSAGE);
            txtFName.requestFocus();
            return;
        }
        if (lName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter Last Name!", "Validation Error", JOptionPane.WARNING_MESSAGE);
            txtLName.requestFocus();
            return;
        }
        if (!fName.matches("^[a-zA-Z\\s]+$")) {
            JOptionPane.showMessageDialog(this, "First Name can only contain letters!", "Validation Error", JOptionPane.WARNING_MESSAGE);
            txtFName.requestFocus();
            return;
        }
        if (!lName.matches("^[a-zA-Z\\s]+$")) {
            JOptionPane.showMessageDialog(this, "Last Name can only contain letters!", "Validation Error", JOptionPane.WARNING_MESSAGE);
            txtLName.requestFocus();
            return;
        }
        if (uName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter User Name!", "Validation Error", JOptionPane.WARNING_MESSAGE);
            txtUName.requestFocus();
            return;
        }
        if (uName.length() < 3) {
            JOptionPane.showMessageDialog(this, "Username must be at least 3 characters long!", "Validation Error", JOptionPane.WARNING_MESSAGE);
            txtUName.requestFocus();
            return;
        }
        if (nic.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter NIC number!", "Validation Error", JOptionPane.WARNING_MESSAGE);
            txtNIC.requestFocus();
            return;
        }
        if (!nic.matches("^([0-9]{9}[VvXx]|[0-9]{12})$")) {
            JOptionPane.showMessageDialog(this, "Please enter a valid NIC (e.g. 200012345678 or 991234567V)!", "Validation Error", JOptionPane.WARNING_MESSAGE);
            txtNIC.requestFocus();
            return;
        }
        nic = nic.toUpperCase();

        if (email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter Email address!", "Validation Error", JOptionPane.WARNING_MESSAGE);
            txtEmail.requestFocus();
            return;
        }
        if (!email.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$")) {
            JOptionPane.showMessageDialog(this, "Please enter a valid Email address (e.g. user@gmail.com)!", "Validation Error", JOptionPane.WARNING_MESSAGE);
            txtEmail.requestFocus();
            return;
        }

        if (phone.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter Phone number!", "Validation Error", JOptionPane.WARNING_MESSAGE);
            txtPhone.requestFocus();
            return;
        }
        if (!phone.matches("^(0[0-9]{9})$")) {
            JOptionPane.showMessageDialog(this, "Please enter a valid 10-digit Phone number (e.g. 0771234567)!", "Validation Error", JOptionPane.WARNING_MESSAGE);
            txtPhone.requestFocus();
            return;
        }

        try {
            // Check username collision with other accounts
            pst = db.con.prepareStatement("SELECT emp_no FROM user WHERE username = ? AND emp_no != ?");
            pst.setString(1, uName);
            pst.setString(2, empNo);
            rs = pst.executeQuery();
            if (rs.next()) {
                JOptionPane.showMessageDialog(this, "This username is already taken by another user! Please choose a different one.", "Warning", JOptionPane.WARNING_MESSAGE);
                pst.close();
                rs.close();
                txtUName.requestFocus();
                return;
            }
            pst.close();
            rs.close();

            // Check NIC collision with other accounts
            pst = db.con.prepareStatement("SELECT emp_no FROM user WHERE nic = ? AND emp_no != ?");
            pst.setString(1, nic);
            pst.setString(2, empNo);
            rs = pst.executeQuery();
            if (rs.next()) {
                JOptionPane.showMessageDialog(this, "This NIC is already associated with another account!", "Warning", JOptionPane.WARNING_MESSAGE);
                pst.close();
                rs.close();
                txtNIC.requestFocus();
                return;
            }
            pst.close();
            rs.close();

            // Check Email collision with other accounts
            pst = db.con.prepareStatement("SELECT emp_no FROM user WHERE email = ? AND emp_no != ?");
            pst.setString(1, email);
            pst.setString(2, empNo);
            rs = pst.executeQuery();
            if (rs.next()) {
                JOptionPane.showMessageDialog(this, "This Email is already associated with another account!", "Warning", JOptionPane.WARNING_MESSAGE);
                pst.close();
                rs.close();
                txtEmail.requestFocus();
                return;
            }
            pst.close();
            rs.close();

            // Check Phone collision with other accounts
            pst = db.con.prepareStatement("SELECT emp_no FROM user WHERE phone = ? AND emp_no != ?");
            pst.setString(1, phone);
            pst.setString(2, empNo);
            rs = pst.executeQuery();
            if (rs.next()) {
                JOptionPane.showMessageDialog(this, "This Phone number is already associated with another account!", "Warning", JOptionPane.WARNING_MESSAGE);
                pst.close();
                rs.close();
                txtPhone.requestFocus();
                return;
            }
            pst.close();
            rs.close();

            // Update user table without touching password
            if (currentProfileImageBytes != null) {
                pst = db.con.prepareStatement(
                    "UPDATE user SET f_name = ?, l_name = ?, username = ?, email = ?, phone = ?, nic = ?, image = ? WHERE emp_no = ?"
                );
                pst.setString(1, fName);
                pst.setString(2, lName);
                pst.setString(3, uName);
                pst.setString(4, email);
                pst.setString(5, phone);
                pst.setString(6, nic);
                pst.setBytes(7, currentProfileImageBytes);
                pst.setString(8, empNo);
            } else {
                pst = db.con.prepareStatement(
                    "UPDATE user SET f_name = ?, l_name = ?, username = ?, email = ?, phone = ?, nic = ? WHERE emp_no = ?"
                );
                pst.setString(1, fName);
                pst.setString(2, lName);
                pst.setString(3, uName);
                pst.setString(4, email);
                pst.setString(5, phone);
                pst.setString(6, nic);
                pst.setString(7, empNo);
            }

            int rowsUpdated = pst.executeUpdate();
            pst.close();

            if (rowsUpdated > 0) {
                // Update in-memory user object
                if (user != null) {
                    user.setfName(fName);
                    user.setlName(lName);
                    user.setUserName(uName);
                    user.setEmail(email);
                    user.setPhone(phone);
                    user.setNic(nic);
                    if (lblUserImage.getIcon() instanceof ImageIcon) {
                        user.setImage(((ImageIcon) lblUserImage.getIcon()).getImage());
                    }
                }

                JOptionPane.showMessageDialog(this, "Profile details updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                checkAccEdite.setSelected(false);
                updateSettingsFieldsEditableState();
                loadUserTable(); // refresh table in admin if visible
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update profile details. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        loadLoggedInUserSettings();
    }//GEN-LAST:event_jButton6ActionPerformed

    private void cmbSessionDurationActionPerformed(java.awt.event.ActionEvent evt) {
        if (currentSelectedStatus != null && (currentSelectedStatus.equalsIgnoreCase("Available") || currentSelectedStatus.equalsIgnoreCase("Awailable"))) {
            updateEstimatedSessionPrice();
        }
    }

    private void btnAddTimeActionActionPerformed(java.awt.event.ActionEvent evt) {
        addTimeToActiveSession();
    }

    private void btnEndSessionActionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEndSessionActionActionPerformed
        endSelectedSession();
    }//GEN-LAST:event_btnEndSessionActionActionPerformed

    private void btnStartSessionActionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnStartSessionActionActionPerformed
        startNewSession();
    }//GEN-LAST:event_btnStartSessionActionActionPerformed

    private void btnLogOutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLogOutActionPerformed
        int response = JOptionPane.showConfirmDialog(this, "Are you sure you want to log out?", "Confirm Log Out", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (response == JOptionPane.YES_OPTION) {
            recordUserLogout();
            this.dispose();
            LoginFrame login = new LoginFrame();
            login.setVisible(true);
        }
    }//GEN-LAST:event_btnLogOutActionPerformed

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
        java.awt.EventQueue.invokeLater(() -> new Dash(null).setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel DateTime;
    private javax.swing.JPanel Header;
    private javax.swing.JPanel Settings;
    private javax.swing.JPanel UserEdite;
    private javax.swing.JPanel adminLeft;
    private javax.swing.JPanel adminRigh;
    private javax.swing.JPanel adminRightCard;
    private javax.swing.JButton btnAcc;
    private javax.swing.JButton btnAddTimeAction;
    private javax.swing.JButton btnAdmin;
    private javax.swing.JButton btnClean;
    private javax.swing.JButton btnDash;
    private javax.swing.JButton btnEndSessionAction;
    private javax.swing.JButton btnHam;
    private javax.swing.JButton btnHistory;
    private javax.swing.JButton btnIncome;
    private javax.swing.JButton btnLive;
    private javax.swing.JButton btnLiveRefresh;
    private javax.swing.JButton btnLiveSearch;
    private javax.swing.JButton btnLogOut;
    private javax.swing.JButton btnPc;
    private javax.swing.JButton btnPcAdd;
    private javax.swing.JButton btnPcClear;
    private javax.swing.JButton btnPcDel;
    private javax.swing.JButton btnPcManage;
    private javax.swing.JButton btnPcUpdate;
    private javax.swing.JButton btnReports;
    private javax.swing.JButton btnStartSessionAction;
    private javax.swing.JButton btnToRepair;
    private javax.swing.JButton btnUserAdd;
    private javax.swing.JButton btnUserDel;
    private javax.swing.JButton btnUserManage;
    private javax.swing.JButton btnUserUpdate;
    private javax.swing.JCheckBox checkAccEdite;
    private javax.swing.JCheckBox chkblockIP;
    private javax.swing.JComboBox<String> cmbPcCategory;
    private javax.swing.JComboBox<String> cmbRole;
    private javax.swing.JComboBox<String> cmbSessionDuration;
    private javax.swing.JPanel contentPnl;
    private javax.swing.JPanel framePnl;
    private javax.swing.JPanel holder;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JCheckBox jCheckBoxHam;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel24;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPaneLive;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTable jTable3;
    private javax.swing.JTable jTable4;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JToggleButton jToggleButton1;
    private javax.swing.JToggleButton jToggleButton2;
    private javax.swing.JLabel lblAmountHeader;
    private javax.swing.JLabel lblAvailablePc;
    private javax.swing.JLabel lblCusNameTitle;
    private javax.swing.JLabel lblCusPhoneTitle;
    private javax.swing.JLabel lblDetailAmount;
    private javax.swing.JLabel lblDetailCategory;
    private javax.swing.JLabel lblDetailPcId;
    private javax.swing.JLabel lblDetailPcName;
    private javax.swing.JLabel lblDetailRate;
    private javax.swing.JLabel lblDetailSessionId;
    private javax.swing.JLabel lblDetailStaff;
    private javax.swing.JLabel lblDetailStartTime;
    private javax.swing.JLabel lblDetailStatus;
    private javax.swing.JLabel lblDetailTimer;
    private javax.swing.JLabel lblDurationTitle;
    private javax.swing.JLabel lblImage;
    private javax.swing.JLabel lblLiveSearch;
    private javax.swing.JLabel lblLiveTotalActive;
    private javax.swing.JLabel lblOccupiedPc;
    private javax.swing.JLabel lblRepairPc;
    private javax.swing.JLabel lblStatusTitle;
    private javax.swing.JLabel lblSub17;
    private javax.swing.JLabel lblSub18;
    private javax.swing.JLabel lblSub19;
    private javax.swing.JLabel lblSub20;
    private javax.swing.JLabel lblSub21;
    private javax.swing.JLabel lblTimerHeader;
    private javax.swing.JLabel lblTitle17;
    private javax.swing.JLabel lblTitle18;
    private javax.swing.JLabel lblTitle19;
    private javax.swing.JLabel lblTitle20;
    private javax.swing.JLabel lblTitle21;
    private javax.swing.JLabel lblTotalPc;
    private javax.swing.JLabel lblTotalUsers;
    private javax.swing.JLabel lblUserImage;
    private javax.swing.JPanel main;
    private javax.swing.JPanel pcSessionsPanel;
    private javax.swing.JPanel pnlAcc;
    private javax.swing.JPanel pnlAdmin;
    private javax.swing.JPanel pnlAmountBox;
    private javax.swing.JPanel pnlButton;
    private javax.swing.JPanel pnlDash;
    private javax.swing.JPanel pnlHistory;
    private javax.swing.JPanel pnlLive;
    private javax.swing.JPanel pnlPc;
    private javax.swing.JPanel pnlPcM;
    private javax.swing.JPanel pnlRepair;
    private javax.swing.JPanel pnlReports;
    private javax.swing.JPanel pnlSessionEnd;
    private javax.swing.JPanel pnlSet;
    private javax.swing.JPanel pnlTimerBox;
    private javax.swing.JPanel pnlUTable;
    private javax.swing.JRadioButton radioAwailable;
    private javax.swing.JRadioButton radioOccu;
    private javax.swing.JRadioButton radioRepair;
    private javax.swing.JPanel setting1;
    private javax.swing.JPanel sidebarPnl;
    private javax.swing.JPanel summery;
    private javax.swing.JTable tableLiveSessions;
    private javax.swing.JTable tablePc;
    private javax.swing.JTable tableRepairPc;
    private javax.swing.JTable tableUser;
    private javax.swing.JTable tableUserPcManage;
    private javax.swing.JTextField txtCPU;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JTextField txtEmpNo;
    private javax.swing.JTextField txtFName;
    private javax.swing.JTextField txtFullName;
    private javax.swing.JTextField txtHrate;
    private javax.swing.JTextField txtIP;
    private javax.swing.JTextField txtLName;
    private javax.swing.JTextField txtLiveSearch;
    private javax.swing.JTextField txtMBoard;
    private javax.swing.JTextField txtNIC;
    private javax.swing.JTextField txtName;
    private javax.swing.JTextField txtPCSearch;
    private javax.swing.JTextField txtPcId;
    private javax.swing.JTextField txtPhone;
    private javax.swing.JTextField txtRAM;
    private javax.swing.JTextField txtSessionCusName;
    private javax.swing.JTextField txtSessionCusPhone;
    private javax.swing.JTextField txtUName;
    private javax.swing.JTextField txtUserName;
    private javax.swing.JTextField txtUserNo;
    private javax.swing.JTextField txtVGA;
    private javax.swing.JPanel userEdit;
    // End of variables declaration//GEN-END:variables

    private void connerRoud() {
        setting1.putClientProperty("FlatLaf.style", "arc: 20");
    }

    private void tableStyle(JTable table, JScrollPane scrollPane) {
        // 1. Table Main Colors & Grid
        table.setBackground(new java.awt.Color(16, 25, 45));
        table.setForeground(new java.awt.Color(230, 238, 248));
        table.setSelectionBackground(new java.awt.Color(0, 102, 255));
        table.setSelectionForeground(java.awt.Color.WHITE);

        // 2. Grid & Layout
        table.setRowHeight(38);
        table.setShowVerticalLines(false);
        table.setShowHorizontalLines(true);
        table.setGridColor(new java.awt.Color(31, 45, 74));
        table.setIntercellSpacing(new java.awt.Dimension(0, 0));
        table.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 13));

        // 3. Table Header
        javax.swing.table.JTableHeader header = table.getTableHeader();
        header.setPreferredSize(new java.awt.Dimension(header.getPreferredSize().width, 38));
        header.setDefaultRenderer(new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(t, value, isSelected, hasFocus, row, column);
                label.setBackground(new java.awt.Color(7, 13, 31));
                label.setForeground(new java.awt.Color(0, 204, 255));
                label.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 13));
                label.setHorizontalAlignment(JLabel.CENTER);
                label.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(0, 204, 255)));
                return label;
            }
        });

        // 4. ScrollPane
        if (scrollPane != null) {
            scrollPane.setBackground(new java.awt.Color(16, 25, 45));
            scrollPane.getViewport().setBackground(new java.awt.Color(16, 25, 45));
            scrollPane.setBorder(javax.swing.BorderFactory.createEmptyBorder());
        }

        // 5. Custom Cell & Status Renderer
        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(t, value, isSelected, hasFocus, row, column);
                label.setHorizontalAlignment(JLabel.CENTER);

                if (!isSelected) {
                    label.setBackground(row % 2 == 0 ? new java.awt.Color(16, 25, 45) : new java.awt.Color(23, 35, 60));
                }

                String valStr = value != null ? value.toString().trim() : "";
                if (valStr.equalsIgnoreCase("Available") || valStr.equalsIgnoreCase("Occupied") || valStr.equalsIgnoreCase("Repair") || valStr.equalsIgnoreCase("Repiar")) {
                    String color = "#00e676";
                    String bg = "#0d3320";
                    if (valStr.equalsIgnoreCase("Occupied")) {
                        color = "#ff4d4d";
                        bg = "#3d141d";
                    } else if (valStr.equalsIgnoreCase("Repair") || valStr.equalsIgnoreCase("Repiar")) {
                        color = "#ffaa00";
                        bg = "#38260b";
                    }
                    label.setText("<html><center><div style='background-color:" + bg + "; color:" + color + "; border:1px solid " + color + "; padding:2px 8px; border-radius:10px; font-weight:bold;'>â— " + valStr + "</div></center></html>");
                }
                return label;
            }
        };

        for (int i = 0; i < table.getColumnModel().getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(cellRenderer);
        }
    }

    private void loadUserTable() {

        try {
            DefaultTableModel dt = (DefaultTableModel) tableUser.getModel();
            dt.setRowCount(0);

            pst = db.con.prepareStatement("SELECT emp_no, f_name, l_name, nic, username, email, phone, role FROM user");
            rs = pst.executeQuery();

            while (rs.next()) {
                Vector v = new Vector();

                v.add(rs.getString("emp_no"));
                v.add(rs.getString("f_name") + " " + rs.getString("l_name"));
                v.add(rs.getString("username"));
                v.add(rs.getString("nic"));
                v.add(rs.getString("email"));
                v.add(rs.getString("phone"));
                v.add(rs.getString("role"));

                dt.addRow(v);
            }
            pst.close();
            rs.close();

        } catch (Exception e) {
            e.printStackTrace();
            javax.swing.JOptionPane.showMessageDialog(this, "Table load error: " + e.getMessage(), "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void generatePcId(String category) {
        try {
            Random randpc = new Random();
            boolean isUnique = false;
            String newPcId = "";

            while (!isUnique) {

                int randmNum = randpc.nextInt(9000) + 1000;
                newPcId = category + "-GG-" + randmNum;

                pst = db.con.prepareStatement("SELECT pc_id FROM pc_table WHERE pc_id = ?");
                pst.setString(1, newPcId);
                rs = pst.executeQuery();

                if (!rs.next()) {
                    isUnique = true;
                }

                pst.close();
                rs.close();

                txtPcId.setText(newPcId);
            }
        } catch (SQLException ex) {
            System.getLogger(Dash.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }

    }

    private void loadPcTable() {
        try {
            DefaultTableModel dt = (DefaultTableModel) tablePc.getModel();
            dt.setRowCount(0);

            pst = db.con.prepareStatement("SELECT pc_id, pc_name, category, hourly_rate, status FROM pc_table");
            rs = pst.executeQuery();

            while (rs.next()) {
                Vector v2 = new Vector();

                v2.add(rs.getString("pc_id"));
                v2.add(rs.getString("pc_name"));
                v2.add(rs.getString("category"));
                v2.add(rs.getString("hourly_rate"));
                v2.add(rs.getString("status"));

                dt.addRow(v2);
            }

            pst.close();
            rs.close();

        } catch (Exception e) {
            e.printStackTrace();
            javax.swing.JOptionPane.showMessageDialog(this, "Table load error: " + e.getMessage(), "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clear() {
        txtPcId.setText("");
        txtName.setText("");
        txtIP.setText("");
        txtHrate.setText("");
        txtCPU.setText("");
        txtMBoard.setText("");
        txtRAM.setText("");
        txtVGA.setText("");

        if (cmbPcCategory.getItemCount() > 0) {
            cmbPcCategory.setToolTipText("-Select-");
        }

        radioAwailable.setSelected(false);
        radioOccu.setSelected(false);
        radioRepair.setSelected(false);

        btnPcUpdate.setVisible(false);
        btnPcDel.setVisible(false);
        btnPcClear.setVisible(false);

        loadPcTable();
        loadPcRepairTable();
        loadPcGrid();
    }

    private void loadPcRepairTable() {
        try {
            DefaultTableModel model = (DefaultTableModel) tableRepairPc.getModel();
            model.setRowCount(0);

            pst = db.con.prepareStatement("SELECT pc_id, pc_name, category, status, ip_address FROM pc_table");
            rs = pst.executeQuery();

            while (rs.next()) {
                Vector v3 = new Vector();

                v3.add(rs.getString("pc_id"));
                v3.add(rs.getString("pc_name"));
                v3.add(rs.getString("category"));
                v3.add(rs.getString("status"));
                v3.add(rs.getString("ip_address"));

                model.addRow(v3);
            }
            pst.close();
            rs.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "All PC table load error: " + e.getMessage());
        }
    }

    private void loadRepairHistoryTable() {
        try {
            DefaultTableModel model = (DefaultTableModel) jTable2.getModel();
            model.setRowCount(0);

            String sql = "SELECT m.pc_id, COALESCE(p.pc_name, m.pc_id) AS pc_name, m.repair_id, m.issue_description, m.cost, m.repair_date "
                    + "FROM pc_maintenance m "
                    + "LEFT JOIN pc_table p ON m.pc_id = p.pc_id "
                    + "ORDER BY m.repair_id DESC";
            pst = db.con.prepareStatement(sql);
            rs = pst.executeQuery();

            while (rs.next()) {
                Vector v = new Vector();
                v.add(rs.getString("pc_id"));
                v.add(rs.getString("pc_name"));
                v.add(rs.getString("repair_id"));
                v.add(rs.getString("issue_description"));
                model.addRow(v);
            }
            pst.close();
            rs.close();
        } catch (Exception e) {
            System.out.println("Repair History load error: " + e.getMessage());
        }
    }

    private void searchRepairPc() {
        String rawSearchData = jTextField1.getText().trim();
        String numberOnly = rawSearchData.replaceAll("[^0-9]", "");

        if (!numberOnly.isEmpty() && numberOnly.length() == 1) {
            numberOnly = "0" + numberOnly;
        }

        try {
            DefaultTableModel dtm = (DefaultTableModel) tableRepairPc.getModel();
            dtm.setRowCount(0);

            String sql = "SELECT pc_id, pc_name, category, status, ip_address FROM pc_table WHERE pc_id LIKE ? OR pc_name LIKE ? ORDER BY pc_id ASC";
            pst = db.con.prepareStatement(sql);

            String searchParam = numberOnly.isEmpty() ? rawSearchData : numberOnly;

            pst.setString(1, "%" + searchParam + "%");
            pst.setString(2, "%" + searchParam + "%");

            rs = pst.executeQuery();

            while (rs.next()) {
                Vector v = new Vector();
                v.add(rs.getString("pc_id"));
                v.add(rs.getString("pc_name"));
                v.add(rs.getString("category"));
                v.add(rs.getString("status"));
                v.add(rs.getString("ip_address"));

                dtm.addRow(v);
            }
            pst.close();
            rs.close();

        } catch (Exception e) {
            System.out.println("Repair Search Error: " + e.getMessage());
        }
    }

    private void clearRepair() {
        jTextField1.setText("");
        jTextField2.setText("");
        jTextField3.setText("");
        jTextField4.setText("");
        loadPcRepairTable();
    }

    private void loadPcGrid() {
        pcSessionsPanel.removeAll();
        pcSessionsPanel.setPreferredSize(new java.awt.Dimension(832, 450));
        pcSessionsPanel.setMinimumSize(new java.awt.Dimension(832, 450));
        pcSessionsPanel.setLayout(new BorderLayout());

        JPanel gridPanel = new JPanel();
        gridPanel.setBackground(new Color(11, 19, 43));
        gridPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        gridPanel.setLayout(new GridLayout(0, 6, 10, 10));

        JPanel wrapper = new JPanel(new java.awt.BorderLayout());
        wrapper.setBackground(new java.awt.Color(11, 19, 43));
        wrapper.add(gridPanel, java.awt.BorderLayout.NORTH);

        JScrollPane scrollPane = new JScrollPane(wrapper);
        scrollPane.setBackground(new Color(11, 19, 43));
        scrollPane.getViewport().setBackground(new Color(11, 19, 43));
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        pcSessionsPanel.add(scrollPane, java.awt.BorderLayout.CENTER);

        try {
            pst = db.con.prepareStatement("SELECT pc_id, pc_name, category, hourly_rate, status FROM pc_table");
            rs = pst.executeQuery();

            while (rs.next()) {
                final String id = rs.getString("pc_id");
                final String name = rs.getString("pc_name");
                final String category = rs.getString("category");
                final String rate = rs.getString("hourly_rate");
                final String status = rs.getString("status");

                JButton btnPc = new javax.swing.JButton();
                btnPc.setPreferredSize(new java.awt.Dimension(122, 68));
                btnPc.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
                btnPc.setFocusPainted(false);
                btnPc.putClientProperty("FlatLaf.style", "arc: 12");

                String statusColor = "#00e676";
                String statusLabel = "Available";
                java.awt.Color cardBg = new java.awt.Color(16, 36, 28);
                java.awt.Color borderColor = new java.awt.Color(0, 200, 115);

                if (status != null) {
                    if (status.equalsIgnoreCase("Occupied")) {
                        statusColor = "#ff4757";
                        statusLabel = "Occupied";
                        cardBg = new java.awt.Color(38, 18, 24);
                        borderColor = new java.awt.Color(235, 60, 75);
                    } else if (status.equalsIgnoreCase("Repair") || status.equalsIgnoreCase("Repiar")) {
                        statusColor = "#ffa502";
                        statusLabel = "Repair";
                        cardBg = new java.awt.Color(28, 32, 40);
                        borderColor = new java.awt.Color(115, 125, 140);
                    }
                }

                btnPc.setBackground(cardBg);
                btnPc.setBorder(createCompoundBorder(new javax.swing.border.LineBorder(borderColor, 1, true), createEmptyBorder(3, 4, 3, 4)));

                String rateText = "";
                try {
                    if (rate != null && !rate.isEmpty()) {
                        double r = Double.parseDouble(rate);
                        rateText = "Rs. " + (int) r + "/h";
                    }
                } catch (Exception ignored) {
                    rateText = rate != null ? rate : "";
                }

                java.net.URL iconUrl = null;
                try {
                    if (category != null && category.equalsIgnoreCase("PS5")) {
                        iconUrl = getClass().getResource("/icons/ps5_icon.png");
                    }
                    if (iconUrl == null) {
                        iconUrl = getClass().getResource("/icons/pc.png");
                    }
                } catch (Exception ex) {
                    // ignore
                }

                String iconTag = "";
                if (iconUrl != null) {
                    iconTag = "<img src='" + iconUrl + "' width='16' height='16'>&nbsp;";
                }

                String btnText = "<html><center><div style='padding-top:2px; font-family: Segoe UI, sans-serif;'>"
                        + "<span style='font-size:12px; font-weight:bold; color:#FFFFFF;'>" + iconTag + name + "</span><br>"
                        + "<span style='font-size:10px; color:" + statusColor + "; font-weight:bold;'>● " + statusLabel + "</span>"
                        + (rateText.isEmpty() ? "" : ("<br><span style='font-size:9px; color:#8fa0c0;'>" + rateText + "</span>"))
                        + "</div></center></html>";

                btnPc.setText(btnText);
                btnPc.setIcon(null);

                btnPc.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent evt) {
                        // pc grid button eka click karapu welawe session details load kirima
                        selectPcForSession(id, name, category, rate, status);
                    }
                });

                gridPanel.add(btnPc);
            }

            pcSessionsPanel.revalidate();
            pcSessionsPanel.repaint();
            loadSummaryCards();

            pst.close();
            rs.close();

        } catch (SQLException ex) {
            System.getLogger(Dash.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            JOptionPane.showMessageDialog(this, "PC Grid Load Error: " + ex.getMessage());
        }

    }

    // =========================================================================
    // Helper to get selected session duration in minutes (Minimum: 60 minutes / 1 Hour)
    // =========================================================================
    private int getSelectedDurationMinutes() {
        if (cmbSessionDuration == null) {
            return 60;
        }
        int selectedIndex = cmbSessionDuration.getSelectedIndex();
        switch (selectedIndex) {
            case 0: return 60;   // 1 Hour
            case 1: return 90;   // 1 Hour 30 Mins
            case 2: return 120;  // 2 Hours
            case 3: return 180;  // 3 Hours
            case 4: return 240;  // 4 Hours
            case 5: return 300;  // 5 Hours
            case 6: return 360;  // 6 Hours
            case 7: // Custom Minutes...
                String input = JOptionPane.showInputDialog(this, "Enter custom duration in minutes (Minimum 60 minutes):", "Custom Session Duration", JOptionPane.QUESTION_MESSAGE);
                if (input != null && !input.trim().isEmpty()) {
                    try {
                        int mins = Integer.parseInt(input.trim());
                        if (mins < 60) {
                            JOptionPane.showMessageDialog(this, "Minimum session duration is 60 minutes (1 Hour)!", "Notice", JOptionPane.INFORMATION_MESSAGE);
                            return 60;
                        }
                        return mins;
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(this, "Invalid number entered. Defaulting to 60 minutes (1 Hour).", "Warning", JOptionPane.WARNING_MESSAGE);
                    }
                }
                return 60;
            default:
                return 60;
        }
    }

    // =========================================================================
    // Auto-calculate and update estimated session charge based on duration & hourly rate
    // =========================================================================
    private void updateEstimatedSessionPrice() {
        int minutes = getSelectedDurationMinutes();
        double hrRate = 0.0;
        try {
            hrRate = Double.parseDouble(currentSelectedRate);
        } catch (Exception ignored) {}

        double estimatedPrice = (minutes / 60.0) * hrRate;

        if (lblDetailAmount != null) {
            lblDetailAmount.setText("Rs. " + String.format("%.2f", estimatedPrice));
        }

        int h = minutes / 60;
        int m = minutes % 60;
        if (lblDetailTimer != null) {
            lblDetailTimer.setText(String.format("%02d:%02d:00", h, m));
            lblDetailTimer.setForeground(new java.awt.Color(0, 255, 163));
        }
    }

    // =========================================================================
    // 1. Station selection handler - updates session panel details based on state
    // =========================================================================
    public void selectPcForSession(String id, String name, String category, String rate, String status) {
        this.currentSelectedPcId = id;
        this.currentSelectedPcName = name;
        this.currentSelectedCategory = category != null ? category : "PC";
        this.currentSelectedRate = rate != null ? rate : "0";
        this.currentSelectedStatus = status != null ? status : "Available";

        // Station Info Update
        lblDetailPcName.setText(name);
        lblDetailCategory.setText("[" + currentSelectedCategory + "]");
        lblDetailPcId.setText("ID: " + id);

        double hrRate = 0.0;
        try {
            hrRate = Double.parseDouble(currentSelectedRate);
        } catch (Exception ignored) {}
        lblDetailRate.setText("Rs. " + (int) hrRate + "/h");

        // Staff Info
        String staffName = (user != null && user.getfName() != null)
                ? user.getfName() + " " + (user.getlName() != null ? user.getlName() : "")
                : "Staff In-Charge";
        lblDetailStaff.setText("Staff: " + staffName);

        // Status & Session details check
        if (currentSelectedStatus.equalsIgnoreCase("Occupied")) {
            lblDetailStatus.setText("● OCCUPIED");
            lblDetailStatus.setForeground(new java.awt.Color(255, 71, 87));

            if (btnStartSessionAction != null) {
                btnStartSessionAction.setEnabled(false);
                btnStartSessionAction.setVisible(false);
            }
            if (btnAddTimeAction != null) {
                btnAddTimeAction.setEnabled(true);
                btnAddTimeAction.setVisible(true);
            }
            if (btnEndSessionAction != null) {
                btnEndSessionAction.setEnabled(true);
                btnEndSessionAction.setVisible(true);
            }
            if (cmbSessionDuration != null) {
                cmbSessionDuration.setEnabled(false);
            }

            // Fetch active ongoing session from gaming_sessions table
            int dbAllocatedMins = 60;
            double dbTotalAmount = 0.0;
            try {
                pst = db.con.prepareStatement("SELECT session_id, cus_name, phone, start_time, add_minutes, total_minutes, total_amount FROM gaming_sessions WHERE pc_id = ? AND status = 'Ongoing' ORDER BY start_time DESC LIMIT 1");
                pst.setString(1, id);
                rs = pst.executeQuery();

                if (rs.next()) {
                    activeSessionId = rs.getString("session_id");
                    String dbCusName = rs.getString("cus_name");
                    String dbPhone = rs.getString("phone");

                    if (dbCusName != null && !dbCusName.trim().isEmpty()) {
                        currentSelectedCustomer = dbCusName.trim();
                    } else {
                        currentSelectedCustomer = "Walk-in Customer";
                    }
                    currentSelectedPhone = dbPhone != null ? dbPhone.trim() : "";

                    java.sql.Timestamp startTime = rs.getTimestamp("start_time");
                    sessionStartTimeMillis = startTime != null ? startTime.getTime() : System.currentTimeMillis();

                    int totalMins = rs.getInt("total_minutes");
                    if (totalMins > 0) {
                        dbAllocatedMins = totalMins;
                    }
                    dbTotalAmount = rs.getDouble("total_amount");

                    java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("hh:mm:ss a");
                    lblDetailStartTime.setText("Started: " + (startTime != null ? sdf.format(startTime) : "Now"));
                } else {
                    activeSessionId = "SES-" + (System.currentTimeMillis() % 100000);
                    sessionStartTimeMillis = System.currentTimeMillis();
                    lblDetailStartTime.setText("Started: Active");
                }
                pst.close();
                rs.close();
            } catch (Exception e) {
                activeSessionId = "SES-" + id;
                sessionStartTimeMillis = System.currentTimeMillis();
                lblDetailStartTime.setText("Started: Active");
            }

            this.sessionAllocatedMinutes = dbAllocatedMins;
            this.sessionEndTimeMillis = sessionStartTimeMillis + (sessionAllocatedMinutes * 60 * 1000L);

            lblDetailSessionId.setText("Session ID: " + activeSessionId);

            if (txtSessionCusName != null) {
                txtSessionCusName.setText(currentSelectedCustomer);
                txtSessionCusName.setEditable(false);
            }
            if (txtSessionCusPhone != null) {
                txtSessionCusPhone.setText(currentSelectedPhone);
                txtSessionCusPhone.setEditable(false);
            }

            // Start Live Countdown Timer
            if (sessionTimer != null && sessionTimer.isRunning()) {
                sessionTimer.stop();
            }
            sessionTimer = new javax.swing.Timer(1000, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    updateLiveSessionTimer();
                }
            });
            sessionTimer.start();
            updateLiveSessionTimer();

        } else if (currentSelectedStatus.equalsIgnoreCase("Repair") || currentSelectedStatus.equalsIgnoreCase("Repiar")) {
            lblDetailStatus.setText("● REPAIR");
            lblDetailStatus.setForeground(new java.awt.Color(255, 165, 2));
            lblDetailSessionId.setText("Session: Under Maintenance");
            lblDetailStartTime.setText("Start Time: -");
            lblDetailTimer.setText("00:00:00");
            lblDetailAmount.setText("Rs. 0.00");

            if (txtSessionCusName != null) {
                txtSessionCusName.setText("-");
                txtSessionCusName.setEditable(false);
            }
            if (txtSessionCusPhone != null) {
                txtSessionCusPhone.setText("-");
                txtSessionCusPhone.setEditable(false);
            }

            if (sessionTimer != null && sessionTimer.isRunning()) {
                sessionTimer.stop();
            }
            if (btnStartSessionAction != null) {
                btnStartSessionAction.setEnabled(false);
                btnStartSessionAction.setVisible(false);
            }
            if (btnAddTimeAction != null) {
                btnAddTimeAction.setEnabled(false);
                btnAddTimeAction.setVisible(false);
            }
            if (btnEndSessionAction != null) {
                btnEndSessionAction.setEnabled(false);
                btnEndSessionAction.setVisible(false);
            }
            if (cmbSessionDuration != null) {
                cmbSessionDuration.setEnabled(false);
            }

        } else {
            // Available
            lblDetailStatus.setText("● AVAILABLE");
            lblDetailStatus.setForeground(new java.awt.Color(0, 230, 118));
            lblDetailSessionId.setText("Session: Ready");
            lblDetailStartTime.setText("Start Time: -");

            if (txtSessionCusName != null) {
                txtSessionCusName.setText("Walk-in Customer");
                txtSessionCusName.setEditable(true);
            }
            if (txtSessionCusPhone != null) {
                txtSessionCusPhone.setText("");
                txtSessionCusPhone.setEditable(true);
            }
            if (cmbSessionDuration != null) {
                cmbSessionDuration.setEnabled(true);
            }

            if (sessionTimer != null && sessionTimer.isRunning()) {
                sessionTimer.stop();
            }
            if (btnStartSessionAction != null) {
                btnStartSessionAction.setEnabled(true);
                btnStartSessionAction.setVisible(true);
            }
            if (btnAddTimeAction != null) {
                btnAddTimeAction.setEnabled(false);
                btnAddTimeAction.setVisible(false);
            }
            if (btnEndSessionAction != null) {
                btnEndSessionAction.setEnabled(false);
                btnEndSessionAction.setVisible(false);
            }

            updateEstimatedSessionPrice();
        }

        pnlSessionEnd.revalidate();
        pnlSessionEnd.repaint();
    }

    // =========================================================================
    // 2. Real-time Countdown Timer update method (Per second)
    // =========================================================================
    private void updateLiveSessionTimer() {
        if (sessionStartTimeMillis <= 0 || sessionEndTimeMillis <= 0) {
            return;
        }

        long remainingMillis = sessionEndTimeMillis - System.currentTimeMillis();
        double hrRate = 0.0;
        try {
            hrRate = Double.parseDouble(currentSelectedRate);
        } catch (Exception ignored) {}

        if (remainingMillis > 0) {
            long remainingSeconds = remainingMillis / 1000;
            long hours = remainingSeconds / 3600;
            long minutes = (remainingSeconds % 3600) / 60;
            long seconds = remainingSeconds % 60;

            String formattedTime = String.format("%02d:%02d:%02d Remaining", hours, minutes, seconds);
            if (lblDetailTimer != null) {
                lblDetailTimer.setText(formattedTime);
                lblDetailTimer.setForeground(new java.awt.Color(0, 255, 163)); // Green
            }

            double totalCost = (sessionAllocatedMinutes / 60.0) * hrRate;
            if (lblDetailAmount != null) {
                lblDetailAmount.setText("Rs. " + String.format("%.2f", totalCost));
            }
        } else {
            // Time Expired / Overtime tracking
            long overdueSeconds = (-remainingMillis) / 1000;
            long hours = overdueSeconds / 3600;
            long minutes = (overdueSeconds % 3600) / 60;
            long seconds = overdueSeconds % 60;

            String formattedTime = String.format("00:00:00 (+%02d:%02d Over)", minutes, seconds);
            if (lblDetailTimer != null) {
                lblDetailTimer.setText(formattedTime);
                lblDetailTimer.setForeground(new java.awt.Color(255, 71, 87)); // Red alert
            }

            long elapsedMillis = System.currentTimeMillis() - sessionStartTimeMillis;
            int totalElapsedMins = Math.max(sessionAllocatedMinutes, (int) Math.ceil(elapsedMillis / 60000.0));
            double totalCost = (totalElapsedMins / 60.0) * hrRate;

            if (lblDetailAmount != null) {
                lblDetailAmount.setText("Rs. " + String.format("%.2f", totalCost));
            }
        }
    }

    // =========================================================================
    // 3. Add Extra Time to Ongoing Session (+30m, +1h, +2h, Custom)
    // =========================================================================
    private void addTimeToActiveSession() {
        if (currentSelectedPcId == null || currentSelectedPcId.isEmpty() || !currentSelectedStatus.equalsIgnoreCase("Occupied")) {
            JOptionPane.showMessageDialog(this, "Please select an active, occupied gaming session first!", "No Active Session", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String[] options = {
            "+30 Minutes (0.5 Hr)",
            "+1 Hour (60 mins)",
            "+2 Hours (120 mins)",
            "+Custom Minutes...",
            "Cancel"
        };

        int choice = JOptionPane.showOptionDialog(
            this,
            "Select additional time to add to " + currentSelectedPcName + " (" + currentSelectedPcId + "):\nCurrent Allocated Duration: " + sessionAllocatedMinutes + " mins",
            "Add Session Time",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            options,
            options[1]
        );

        int extraMinutes = 0;
        switch (choice) {
            case 0: extraMinutes = 30; break;
            case 1: extraMinutes = 60; break;
            case 2: extraMinutes = 120; break;
            case 3:
                String input = JOptionPane.showInputDialog(this, "Enter extra minutes to add:", "Custom Extra Time", JOptionPane.QUESTION_MESSAGE);
                if (input != null && !input.trim().isEmpty()) {
                    try {
                        extraMinutes = Integer.parseInt(input.trim());
                        if (extraMinutes <= 0) {
                            JOptionPane.showMessageDialog(this, "Please enter a positive duration in minutes!", "Warning", JOptionPane.WARNING_MESSAGE);
                            return;
                        }
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(this, "Invalid number entered!", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                } else {
                    return;
                }
                break;
            default:
                return; // Cancelled
        }

        double hrRate = 0.0;
        try {
            hrRate = Double.parseDouble(currentSelectedRate);
        } catch (Exception ignored) {}

        double extraCost = (extraMinutes / 60.0) * hrRate;

        try {
            pst = db.con.prepareStatement(
                "UPDATE gaming_sessions SET add_minutes = add_minutes + ?, total_minutes = total_minutes + ?, price_per_adding = ?, total_amount = total_amount + ? WHERE session_id = ?"
            );
            pst.setInt(1, extraMinutes);
            pst.setInt(2, extraMinutes);
            pst.setDouble(3, extraCost);
            pst.setDouble(4, extraCost);
            pst.setString(5, activeSessionId);
            pst.executeUpdate();
            pst.close();

            this.sessionAllocatedMinutes += extraMinutes;
            this.sessionEndTimeMillis += (extraMinutes * 60 * 1000L);

            updateLiveSessionTimer();
            loadLiveSessionsTable();

            JOptionPane.showMessageDialog(
                this,
                "Successfully added +" + extraMinutes + " minutes to " + currentSelectedPcName + "!\nNew Total Duration: " + sessionAllocatedMinutes + " mins (" + String.format("%02dh %02dm", sessionAllocatedMinutes / 60, sessionAllocatedMinutes % 60) + ")\nExtra Charge: Rs. " + String.format("%.2f", extraCost),
                "Time Added Successfully",
                JOptionPane.INFORMATION_MESSAGE
            );

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database Error adding time: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // =========================================================================
    // 4. Session panel eka initial / reset state ekata path kirima
    // =========================================================================
    private void resetSessionDetailsView() {
        if (sessionTimer != null && sessionTimer.isRunning()) {
            sessionTimer.stop();
        }
        currentSelectedPcId = "";
        currentSelectedPcName = "";
        currentSelectedCategory = "";
        currentSelectedRate = "";
        currentSelectedStatus = "";
        currentSelectedCustomer = "Walk-in Customer";
        currentSelectedPhone = "";
        activeSessionId = "";
        sessionStartTimeMillis = 0;
        sessionAllocatedMinutes = 60;
        sessionEndTimeMillis = 0;

        if (lblDetailPcName != null) lblDetailPcName.setText("Select Station");
        if (lblDetailCategory != null) lblDetailCategory.setText("");
        if (lblDetailPcId != null) lblDetailPcId.setText("Device ID: -");
        if (lblDetailRate != null) lblDetailRate.setText("Rate: -");
        if (lblDetailStatus != null) {
            lblDetailStatus.setText("NO SELECTION");
            lblDetailStatus.setForeground(new java.awt.Color(143, 160, 192));
        }
        if (lblDetailSessionId != null) lblDetailSessionId.setText("Session ID: -");
        if (lblDetailStartTime != null) lblDetailStartTime.setText("Start Time: -");
        if (lblDetailStaff != null) lblDetailStaff.setText("Staff: -");
        if (txtSessionCusName != null) {
            txtSessionCusName.setText("Walk-in Customer");
            txtSessionCusName.setEditable(true);
        }
        if (txtSessionCusPhone != null) {
            txtSessionCusPhone.setText("");
            txtSessionCusPhone.setEditable(true);
        }
        if (cmbSessionDuration != null) {
            if (cmbSessionDuration.getItemCount() > 0) {
                cmbSessionDuration.setSelectedIndex(0);
            }
            cmbSessionDuration.setEnabled(true);
        }
        if (lblDetailTimer != null) lblDetailTimer.setText("01:00:00");
        if (lblDetailAmount != null) lblDetailAmount.setText("Rs. 0.00");
        if (btnStartSessionAction != null) {
            btnStartSessionAction.setEnabled(false);
            btnStartSessionAction.setVisible(false);
        }
        if (btnAddTimeAction != null) {
            btnAddTimeAction.setEnabled(false);
            btnAddTimeAction.setVisible(false);
        }
        if (btnEndSessionAction != null) {
            btnEndSessionAction.setEnabled(false);
            btnEndSessionAction.setVisible(false);
        }
    }

    // =========================================================================
    // 5. Active Session eka End kirima & BillFrame Open kirima (End Session Action)
    // =========================================================================
    private void endSelectedSession() {
        if (currentSelectedPcId == null || currentSelectedPcId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select an occupied PC first!", "No PC Selected", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to end session for " + currentSelectedPcName + " (" + currentSelectedPcId + ") and generate the bill?",
            "End Session Confirmation",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            long elapsedMillis = Math.max(0, System.currentTimeMillis() - sessionStartTimeMillis);
            int elapsedMinutes = Math.max(1, (int) Math.ceil(elapsedMillis / 60000.0));
            int totalMinutes = Math.max(sessionAllocatedMinutes, elapsedMinutes);

            double hrRate = 0.0;
            try {
                hrRate = Double.parseDouble(currentSelectedRate);
            } catch (Exception ignored) {}
            double totalAmount = (totalMinutes / 60.0) * hrRate;

            String customerDisplay = currentSelectedCustomer;
            if (currentSelectedPhone != null && !currentSelectedPhone.trim().isEmpty() && !customerDisplay.contains(currentSelectedPhone)) {
                customerDisplay += " (" + currentSelectedPhone.trim() + ")";
            }

            // 1. Update gaming_sessions table
            pst = db.con.prepareStatement(
                "UPDATE gaming_sessions SET end_time = NOW(), total_minutes = ?, total_amount = ?, status = 'Completed' WHERE session_id = ?"
            );
            pst.setInt(1, totalMinutes);
            pst.setDouble(2, totalAmount);
            pst.setString(3, activeSessionId);
            int updated = pst.executeUpdate();
            pst.close();

            if (updated == 0) {
                try {
                    pst = db.con.prepareStatement(
                        "INSERT INTO gaming_sessions (session_id, pc_id, cus_name, phone, start_time, end_time, total_minutes, total_amount, status) VALUES (?, ?, ?, ?, ?, NOW(), ?, ?, 'Completed')"
                    );
                    pst.setString(1, activeSessionId);
                    pst.setString(2, currentSelectedPcId);
                    pst.setString(3, currentSelectedCustomer);
                    pst.setString(4, currentSelectedPhone);
                    pst.setTimestamp(5, new java.sql.Timestamp(sessionStartTimeMillis));
                    pst.setInt(6, totalMinutes);
                    pst.setDouble(7, totalAmount);
                    pst.executeUpdate();
                    pst.close();
                } catch (Exception ignored) {}
            }

            // 2. Invoices table ekata insert kirima
            String invoiceNo = "INV-" + (System.currentTimeMillis() % 1000000);
            String empNo = (user != null && user.getEmpNo() != null) ? user.getEmpNo() : "GG_1234";
            try {
                pst = db.con.prepareStatement(
                    "INSERT INTO invoices (invoice_no, session_id, emp_no, sub_total, discount, net_total, payment_method, invoice_date) VALUES (?, ?, ?, ?, 0.00, ?, 'Cash', NOW())"
                );
                pst.setString(1, invoiceNo);
                pst.setString(2, activeSessionId);
                pst.setString(3, empNo);
                pst.setDouble(4, totalAmount);
                pst.setDouble(5, totalAmount);
                pst.executeUpdate();
                pst.close();
            } catch (Exception ex) {
                System.out.println("Invoice save note: " + ex.getMessage());
            }

            // 3. pc_table status eka 'Available' kirima
            pst = db.con.prepareStatement("UPDATE pc_table SET status = 'Available' WHERE pc_id = ?");
            pst.setString(1, currentSelectedPcId);
            pst.executeUpdate();
            pst.close();

            // 4. Timer stop kirima
            if (sessionTimer != null) {
                sessionTimer.stop();
            }

            // 5. Direct Bill Summary & PDF Generation in Dashboard View (No separate frame)
            String staff = (user != null && user.getUserName() != null) ? user.getUserName() : "System Staff";
            
            String billMessage = "========================================\n"
                    + "        GAMING CAFE & LOUNGE BILL\n"
                    + "========================================\n"
                    + "Invoice No     : " + invoiceNo + "\n"
                    + "Session ID     : " + activeSessionId + "\n"
                    + "Station        : " + currentSelectedPcName + " (" + currentSelectedPcId + ")\n"
                    + "Category       : " + currentSelectedCategory + "\n"
                    + "Customer       : " + currentSelectedCustomer + "\n"
                    + "Phone          : " + (currentSelectedPhone != null && !currentSelectedPhone.isEmpty() ? currentSelectedPhone : "-") + "\n"
                    + "Duration       : " + totalMinutes + " mins (" + String.format("%02dh %02dm", totalMinutes / 60, totalMinutes % 60) + ")\n"
                    + "Hourly Rate    : Rs. " + String.format("%.2f", hrRate) + " / hr\n"
                    + "----------------------------------------\n"
                    + "TOTAL AMOUNT   : Rs. " + String.format("%.2f", totalAmount) + "\n"
                    + "Staff In-Charge: " + staff + "\n"
                    + "========================================\n\n"
                    + "Session completed successfully!\nWould you like to Print / Save the PDF Bill receipt?";

            int printChoice = JOptionPane.showConfirmDialog(
                this,
                billMessage,
                "Bill Summary - " + invoiceNo,
                JOptionPane.YES_NO_OPTION,
                JOptionPane.INFORMATION_MESSAGE
            );

            if (printChoice == JOptionPane.YES_OPTION) {
                generateInvoiceBillPdf(
                    invoiceNo,
                    activeSessionId,
                    currentSelectedPcName + " (" + currentSelectedPcId + ")",
                    currentSelectedCategory,
                    currentSelectedCustomer,
                    totalMinutes,
                    hrRate,
                    totalAmount,
                    staff
                );
            }

            // 6. Refresh UI components
            loadLiveSessionsTable();
            loadPcGrid();
            loadSummaryCards();
            loadPcTable();
            loadUserPctable();
            loadPcRepairTable();

            // 7. Update Session panel to Available
            selectPcForSession(currentSelectedPcId, currentSelectedPcName, currentSelectedCategory, currentSelectedRate, "Available");

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error ending session: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // =========================================================================
    // 6. Aluth Session ekak Start kirima (Start Session Button Action)
    // =========================================================================
    private void startNewSession() {
        if (currentSelectedPcId == null || currentSelectedPcId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select an available PC first!", "No PC Selected", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Customer Name & Phone from input fields
        String cusName = txtSessionCusName != null ? txtSessionCusName.getText().trim() : "";
        String cusPhone = txtSessionCusPhone != null ? txtSessionCusPhone.getText().trim() : "";

        // Phone validation if provided
        if (!cusPhone.isEmpty() && !cusPhone.matches("^(0[0-9]{9})$")) {
            JOptionPane.showMessageDialog(this, "Please enter a valid 10-digit phone number (e.g. 0771234567)!", "Validation Error", JOptionPane.WARNING_MESSAGE);
            if (txtSessionCusPhone != null) {
                txtSessionCusPhone.requestFocus();
            }
            return;
        }

        if (cusName.isEmpty()) {
            cusName = "Walk-in Customer";
        }
        this.currentSelectedCustomer = cusName;
        this.currentSelectedPhone = cusPhone;

        String customerDisplay = cusPhone.isEmpty() ? cusName : cusName + " (" + cusPhone + ")";

        int durationMins = getSelectedDurationMinutes(); // Minimum 60 minutes
        this.sessionAllocatedMinutes = durationMins;

        double hrRate = 0.0;
        try {
            hrRate = Double.parseDouble(currentSelectedRate);
        } catch (Exception ignored) {}
        double initialAmount = (durationMins / 60.0) * hrRate;

        try {
            String newSessionId = "SES-" + (System.currentTimeMillis() % 1000000);

            // Dynamic columns check for gaming_sessions
            java.util.Set<String> tableCols = new java.util.HashSet<>();
            try {
                java.sql.Statement colStmt = db.con.createStatement();
                ResultSet colRs = colStmt.executeQuery("SHOW COLUMNS FROM gaming_sessions");
                while (colRs.next()) {
                    tableCols.add(colRs.getString("Field").toLowerCase());
                }
                colRs.close();
                colStmt.close();
            } catch (Exception ex) {
                System.out.println("Columns inspect note: " + ex.getMessage());
            }

            StringBuilder colNames = new StringBuilder("session_id, pc_id, start_time");
            StringBuilder placeholders = new StringBuilder("?, ?, NOW()");
            java.util.List<Object> values = new java.util.ArrayList<>();
            values.add(newSessionId);
            values.add(currentSelectedPcId);

            if (tableCols.contains("cus_name")) {
                colNames.append(", cus_name");
                placeholders.append(", ?");
                values.add(cusName);
            }
            if (tableCols.contains("phone")) {
                colNames.append(", phone");
                placeholders.append(", ?");
                values.add(cusPhone);
            }
            if (tableCols.contains("cus_phone")) {
                colNames.append(", cus_phone");
                placeholders.append(", ?");
                values.add(cusPhone);
            }
            if (tableCols.contains("add_minutes")) {
                colNames.append(", add_minutes");
                placeholders.append(", ?");
                values.add(0);
            }
            if (tableCols.contains("price_per_adding")) {
                colNames.append(", price_per_adding");
                placeholders.append(", ?");
                values.add(0.00);
            }
            if (tableCols.contains("total_minutes")) {
                colNames.append(", total_minutes");
                placeholders.append(", ?");
                values.add(durationMins);
            }
            if (tableCols.contains("total_amount")) {
                colNames.append(", total_amount");
                placeholders.append(", ?");
                values.add(initialAmount);
            }
            if (tableCols.contains("status")) {
                colNames.append(", status");
                placeholders.append(", ?");
                values.add("Ongoing");
            }

            String insertSql = "INSERT INTO gaming_sessions (" + colNames.toString() + ") VALUES (" + placeholders.toString() + ")";
            pst = db.con.prepareStatement(insertSql);
            for (int i = 0; i < values.size(); i++) {
                Object val = values.get(i);
                if (val instanceof String) {
                    pst.setString(i + 1, (String) val);
                } else if (val instanceof Integer) {
                    pst.setInt(i + 1, (Integer) val);
                } else if (val instanceof Double) {
                    pst.setDouble(i + 1, (Double) val);
                } else {
                    pst.setObject(i + 1, val);
                }
            }
            pst.executeUpdate();
            pst.close();

            // pc_table status eka 'Occupied' kirima
            pst = db.con.prepareStatement("UPDATE pc_table SET status = 'Occupied' WHERE pc_id = ?");
            pst.setString(1, currentSelectedPcId);
            pst.executeUpdate();
            pst.close();

            JOptionPane.showMessageDialog(
                this,
                "Gaming session started successfully!\n\n"
                + "Station    : " + currentSelectedPcName + " (" + currentSelectedPcId + ")\n"
                + "Customer   : " + customerDisplay + "\n"
                + "Duration   : " + durationMins + " mins (" + String.format("%02dh %02dm", durationMins / 60, durationMins % 60) + ")\n"
                + "Charge Est.: Rs. " + String.format("%.2f", initialAmount) + "\n"
                + "Session ID : " + newSessionId,
                "Session Started",
                JOptionPane.INFORMATION_MESSAGE
            );

            // Refresh UI
            loadLiveSessionsTable();
            loadPcGrid();
            loadSummaryCards();
            loadPcTable();
            loadUserPctable();

            // Select and start live session details immediately
            selectPcForSession(currentSelectedPcId, currentSelectedPcName, currentSelectedCategory, currentSelectedRate, "Occupied");

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error starting session: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Auto-migrate database columns to ensure cus_name and phone have safe default values
    private void ensureDatabaseColumns() {
        try {
            if (db != null && db.con != null) {
                java.sql.Statement stmt = db.con.createStatement();
                try {
                    stmt.executeUpdate("ALTER TABLE gaming_sessions ADD COLUMN cus_name VARCHAR(100) NULL DEFAULT 'Walk-in Customer'");
                } catch (Exception e1) {
                    try {
                        stmt.executeUpdate("ALTER TABLE gaming_sessions MODIFY COLUMN cus_name VARCHAR(100) NULL DEFAULT 'Walk-in Customer'");
                    } catch (Exception ignored) {}
                }
                try {
                    stmt.executeUpdate("ALTER TABLE gaming_sessions MODIFY COLUMN phone VARCHAR(20) NULL DEFAULT ''");
                } catch (Exception ignored) {}
                try {
                    stmt.executeUpdate("ALTER TABLE gaming_sessions MODIFY COLUMN cus_phone VARCHAR(20) NULL DEFAULT ''");
                } catch (Exception ignored) {}
                try {
                    stmt.executeUpdate("ALTER TABLE gaming_sessions ADD COLUMN add_minutes INT NULL DEFAULT 0");
                } catch (Exception ignored) {
                    try { stmt.executeUpdate("ALTER TABLE gaming_sessions MODIFY COLUMN add_minutes INT NULL DEFAULT 0"); } catch (Exception ignored2) {}
                }
                try {
                    stmt.executeUpdate("ALTER TABLE gaming_sessions ADD COLUMN price_per_adding DECIMAL(10,2) NULL DEFAULT 0.00");
                } catch (Exception ignored) {
                    try { stmt.executeUpdate("ALTER TABLE gaming_sessions MODIFY COLUMN price_per_adding DECIMAL(10,2) NULL DEFAULT 0.00"); } catch (Exception ignored2) {}
                }
                try {
                    stmt.executeUpdate("ALTER TABLE gaming_sessions MODIFY COLUMN total_minutes INT NULL DEFAULT 0");
                } catch (Exception ignored) {}
                try {
                    stmt.executeUpdate("ALTER TABLE gaming_sessions MODIFY COLUMN total_amount DECIMAL(10,2) NULL DEFAULT 0.00");
                } catch (Exception ignored) {}
                try {
                    stmt.executeUpdate("ALTER TABLE gaming_sessions MODIFY COLUMN status VARCHAR(20) NULL DEFAULT 'Ongoing'");
                } catch (Exception ignored) {}
                stmt.close();
            }
        } catch (Exception ex) {
            System.out.println("ensureDatabaseColumns note: " + ex.getMessage());
        }
    }

    
    private void loadSummaryCards() {
        try {
            int totalPc = 0;
            int availablePc = 0;
            int occupiedPc = 0;
            int repairPc = 0;
            int totalUsers = 0;

            pst = db.con.prepareStatement("SELECT status FROM pc_table");
            rs = pst.executeQuery();
            while (rs.next()) {
                totalPc++;
                String status = rs.getString("status");
                if (status != null) {
                    if (status.equalsIgnoreCase("Occupied")) {
                        occupiedPc++;
                    } else if (status.equalsIgnoreCase("Repair") || status.equalsIgnoreCase("Repiar")) {
                        repairPc++;
                    } else {
                        availablePc++;
                    }
                } else {
                    availablePc++;
                }
            }
            pst.close();
            rs.close();

            pst = db.con.prepareStatement("SELECT COUNT(*) FROM user");
            rs = pst.executeQuery();
            if (rs.next()) {
                totalUsers = rs.getInt(1);
            }
            pst.close();
            rs.close();

            lblTotalPc.setText(String.valueOf(totalPc));
            lblOccupiedPc.setText(String.valueOf(occupiedPc));
            lblAvailablePc.setText(String.valueOf(availablePc));
            lblRepairPc.setText(String.valueOf(repairPc));
            lblTotalUsers.setText(String.valueOf(totalUsers));

        } catch (Exception e) {
            e.printStackTrace();
            javax.swing.JOptionPane.showMessageDialog(this, "Summary load error: " + e.getMessage(), "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    // =========================================================================
    // PC Page (pnlPc) Table Load, Search & Edit Methods
    // =========================================================================
    private void loadUserPctable() {
        loadUserPctable("");
    }

    private void loadUserPctable(String searchKey) {
        try {
            DefaultTableModel model = (DefaultTableModel) tableUserPcManage.getModel();
            model.setRowCount(0);

            String query = "SELECT pc_id, pc_name, category, cpu, ram_capacity, vga, hourly_rate, status FROM pc_table";
            boolean hasSearch = (searchKey != null && !searchKey.trim().isEmpty());
            if (hasSearch) {
                query += " WHERE pc_id LIKE ? OR pc_name LIKE ? OR category LIKE ? OR cpu LIKE ? OR vga LIKE ? OR status LIKE ?";
            }
            query += " ORDER BY pc_id ASC";

            pst = db.con.prepareStatement(query);
            if (hasSearch) {
                String pattern = "%" + searchKey.trim() + "%";
                pst.setString(1, pattern);
                pst.setString(2, pattern);
                pst.setString(3, pattern);
                pst.setString(4, pattern);
                pst.setString(5, pattern);
                pst.setString(6, pattern);
            }

            rs = pst.executeQuery();

            while (rs.next()) {
                Vector v4 = new Vector();

                v4.add(rs.getString("pc_id"));
                v4.add(rs.getString("pc_name"));
                v4.add(rs.getString("category"));
                v4.add(rs.getString("cpu"));
                v4.add(rs.getString("ram_capacity"));
                v4.add(rs.getString("vga"));
                v4.add(rs.getString("hourly_rate"));
                v4.add(rs.getString("status"));
                v4.add("Update ⚙️");

                model.addRow(v4);
            }
            pst.close();
            rs.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "PC table load error: " + e.getMessage());
        }
    }

    public void editPcFromTable(int selectedRow) {
        if (selectedRow < 0 || selectedRow >= tableUserPcManage.getRowCount()) {
            return;
        }

        String pcId = tableUserPcManage.getValueAt(selectedRow, 0).toString();
        String currentName = tableUserPcManage.getValueAt(selectedRow, 1) != null ? tableUserPcManage.getValueAt(selectedRow, 1).toString() : "";
        String currentCategory = tableUserPcManage.getValueAt(selectedRow, 2) != null ? tableUserPcManage.getValueAt(selectedRow, 2).toString() : "PC";
        String currentCpu = tableUserPcManage.getValueAt(selectedRow, 3) != null ? tableUserPcManage.getValueAt(selectedRow, 3).toString() : "";
        String currentRam = tableUserPcManage.getValueAt(selectedRow, 4) != null ? tableUserPcManage.getValueAt(selectedRow, 4).toString() : "";
        String currentVga = tableUserPcManage.getValueAt(selectedRow, 5) != null ? tableUserPcManage.getValueAt(selectedRow, 5).toString() : "";
        String currentRate = tableUserPcManage.getValueAt(selectedRow, 6) != null ? tableUserPcManage.getValueAt(selectedRow, 6).toString() : "0";
        String currentStatus = tableUserPcManage.getValueAt(selectedRow, 7) != null ? tableUserPcManage.getValueAt(selectedRow, 7).toString() : "Available";

        // Simple dialog to edit PC details
        javax.swing.JPanel editPanel = new javax.swing.JPanel(new java.awt.GridLayout(8, 2, 8, 8));

        javax.swing.JTextField txtEditName = new javax.swing.JTextField(currentName);
        javax.swing.JComboBox<String> cmbEditCategory = new javax.swing.JComboBox<>(new String[]{"PC", "PS5", "Xbox", "VIP", "Racing"});
        cmbEditCategory.setSelectedItem(currentCategory);
        javax.swing.JTextField txtEditCpu = new javax.swing.JTextField(currentCpu);
        javax.swing.JTextField txtEditRam = new javax.swing.JTextField(currentRam);
        javax.swing.JTextField txtEditVga = new javax.swing.JTextField(currentVga);
        javax.swing.JTextField txtEditRate = new javax.swing.JTextField(currentRate);
        javax.swing.JComboBox<String> cmbEditStatus = new javax.swing.JComboBox<>(new String[]{"Available", "Occupied", "Repair"});
        cmbEditStatus.setSelectedItem(currentStatus);

        editPanel.add(new javax.swing.JLabel("Station ID:"));
        javax.swing.JLabel lblId = new javax.swing.JLabel(pcId);
        lblId.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 12));
        editPanel.add(lblId);

        editPanel.add(new javax.swing.JLabel("Station Name:"));
        editPanel.add(txtEditName);

        editPanel.add(new javax.swing.JLabel("Category:"));
        editPanel.add(cmbEditCategory);

        editPanel.add(new javax.swing.JLabel("CPU:"));
        editPanel.add(txtEditCpu);

        editPanel.add(new javax.swing.JLabel("RAM:"));
        editPanel.add(txtEditRam);

        editPanel.add(new javax.swing.JLabel("VGA:"));
        editPanel.add(txtEditVga);

        editPanel.add(new javax.swing.JLabel("Hourly Rate (Rs):"));
        editPanel.add(txtEditRate);

        editPanel.add(new javax.swing.JLabel("Status:"));
        editPanel.add(cmbEditStatus);

        int result = JOptionPane.showConfirmDialog(
            this,
            editPanel,
            "Edit Station Details - " + pcId,
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {
            String newName = txtEditName.getText().trim();
            String newCategory = cmbEditCategory.getSelectedItem().toString();
            String newCpu = txtEditCpu.getText().trim();
            String newRam = txtEditRam.getText().trim();
            String newVga = txtEditVga.getText().trim();
            String newRate = txtEditRate.getText().trim();
            String newStatus = cmbEditStatus.getSelectedItem().toString();

            if (newName.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Station name cannot be empty!", "Validation", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                pst = db.con.prepareStatement(
                    "UPDATE pc_table SET pc_name=?, category=?, cpu=?, ram_capacity=?, vga=?, hourly_rate=?, status=? WHERE pc_id=?"
                );
                pst.setString(1, newName);
                pst.setString(2, newCategory);
                pst.setString(3, newCpu);
                pst.setString(4, newRam);
                pst.setString(5, newVga);
                pst.setString(6, newRate);
                pst.setString(7, newStatus);
                pst.setString(8, pcId);

                int updated = pst.executeUpdate();
                pst.close();

                if (updated > 0) {
                    JOptionPane.showMessageDialog(this, "Station " + pcId + " updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    loadUserPctable();
                    loadPcGrid();
                    loadSummaryCards();
                    loadPcTable();
                    loadPcRepairTable();
                    loadLiveSessionsTable();
                } else {
                    JOptionPane.showMessageDialog(this, "Update failed for " + pcId, "Failed", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error updating station: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void tablestyle2(JTable tableUserPcManage, JScrollPane jScrollPane) {
        tableStyle(tableUserPcManage, jScrollPane);
        tableUserPcManage.setRowHeight(42);

        if (tableUserPcManage.getColumnModel().getColumnCount() > 8) {
            tableUserPcManage.getColumnModel().getColumn(8).setCellRenderer(new ButtonRenderer());
            tableUserPcManage.getColumnModel().getColumn(8).setCellEditor(new ButtonEditor(new javax.swing.JCheckBox()));
        }
    }

    private void loadPcDetails(String empNo) {

        try {
            pst = db.con.prepareStatement("SELECT * FROM  user WHERE emp_no = ? ");
            pst.setString(1, empNo);

            String fName = user.getfName();
            String lName = user.getlName();
            String uName = user.getUserName();
            String email = user.getEmail();
            String phone = user.getPhone();
            String nic = user.getNic();
            Image image = user.getImage();

            if (image == null) {
                System.out.println("Image not found");
            } else {
                ImageIcon icon = new ImageIcon(image);
                Image myImage = icon.getImage();
                Image scaledImg = myImage.getScaledInstance(lblUserImage.getWidth(), lblUserImage.getHeight(), Image.SCALE_SMOOTH);
                lblUserImage.setIcon(new javax.swing.ImageIcon(scaledImg));
            }

            txtEmpNo.setText(empNo);
            txtFName.setText(fName);
            txtLName.setText(lName);
            txtUName.setText(uName);
            txtEmail.setText(email);
            txtPhone.setText(phone);
            txtNIC.setText(nic);
        } catch (SQLException ex) {
            System.getLogger(Dash.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }

    }

    class ButtonRenderer extends JButton implements TableCellRenderer {

        public ButtonRenderer() {
            setOpaque(true);
            setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 12));
            setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            setText("Update ⚙️");
            setBackground(new java.awt.Color(0, 153, 255));
            setForeground(java.awt.Color.WHITE);
            setBorder(javax.swing.BorderFactory.createEmptyBorder(4, 8, 4, 8));
            return this;
        }
    }

    // =========================================================================
    // Live Sessions Table Methods (Load, Search, Real-time Timer & End Action)
    // =========================================================================
    private void loadLiveSessionsTable() {
        loadLiveSessionsTable("");
    }

    private void loadLiveSessionsTable(String searchKey) {
        try {
            DefaultTableModel model = (DefaultTableModel) tableLiveSessions.getModel();
            model.setRowCount(0);
            liveSessionEntries.clear();

            String query = "SELECT s.session_id, s.pc_id, p.pc_name, p.category, p.hourly_rate, s.cus_name, s.start_time, s.status "
                    + "FROM gaming_sessions s "
                    + "LEFT JOIN pc_table p ON s.pc_id = p.pc_id "
                    + "WHERE s.status = 'Ongoing' ";

            boolean hasSearch = (searchKey != null && !searchKey.trim().isEmpty());
            if (hasSearch) {
                query += "AND (s.session_id LIKE ? OR s.pc_id LIKE ? OR p.pc_name LIKE ? OR s.cus_name LIKE ?) ";
            }
            query += "ORDER BY s.start_time DESC";

            pst = db.con.prepareStatement(query);
            if (hasSearch) {
                String pattern = "%" + searchKey.trim() + "%";
                pst.setString(1, pattern);
                pst.setString(2, pattern);
                pst.setString(3, pattern);
                pst.setString(4, pattern);
            }

            rs = pst.executeQuery();
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss a");

            int activeCount = 0;
            while (rs.next()) {
                activeCount++;
                String sId = rs.getString("session_id");
                String pId = rs.getString("pc_id");
                String pName = rs.getString("pc_name");
                if (pName == null || pName.isEmpty()) {
                    pName = pId;
                }
                String cat = rs.getString("category");
                if (cat == null || cat.isEmpty()) {
                    cat = "PC";
                }
                double rate = rs.getDouble("hourly_rate");

                String rawCus = rs.getString("cus_name");
                String cName = "Walk-in Customer";
                String cPhone = "-";
                if (rawCus != null && !rawCus.trim().isEmpty()) {
                    rawCus = rawCus.trim();
                    if (rawCus.contains("(") && rawCus.endsWith(")")) {
                        int idxOpen = rawCus.indexOf("(");
                        cName = rawCus.substring(0, idxOpen).trim();
                        cPhone = rawCus.substring(idxOpen + 1, rawCus.length() - 1).trim();
                    } else if (rawCus.contains("[") && rawCus.endsWith("]")) {
                        int idxOpen = rawCus.indexOf("[");
                        cName = rawCus.substring(0, idxOpen).trim();
                        cPhone = rawCus.substring(idxOpen + 1, rawCus.length() - 1).trim();
                    } else {
                        cName = rawCus;
                    }
                }

                Timestamp st = rs.getTimestamp("start_time");
                long startMillis = (st != null) ? st.getTime() : System.currentTimeMillis();
                String stStr = (st != null) ? sdf.format(st) : "Just Now";

                long elapsedSec = Math.max(0, (System.currentTimeMillis() - startMillis) / 1000);
                long hrs = elapsedSec / 3600;
                long mins = (elapsedSec % 3600) / 60;
                long secs = elapsedSec % 60;
                String durStr = String.format("%02d:%02d:%02d", hrs, mins, secs);

                int totalMin = Math.max(1, (int) Math.ceil(elapsedSec / 60.0));
                double curAmount = (totalMin / 60.0) * rate;
                String amountStr = "Rs. " + String.format("%.2f", curAmount);

                LiveSessionEntry entry = new LiveSessionEntry();
                entry.sessionId = sId;
                entry.pcId = pId;
                entry.pcName = pName;
                entry.category = cat;
                entry.cusName = cName;
                entry.cusPhone = cPhone;
                entry.startTimeMillis = startMillis;
                entry.hourlyRate = rate;
                liveSessionEntries.add(entry);

                Vector row = new Vector();
                row.add(sId);
                row.add(pName + " (" + pId + ")");
                row.add(cat);
                row.add(cName);
                row.add(cPhone);
                row.add(stStr);
                row.add(durStr);
                row.add(amountStr);
                row.add("End Session ⏹");

                model.addRow(row);
            }
            pst.close();
            rs.close();

            if (lblLiveTotalActive != null) {
                lblLiveTotalActive.setText("Active Sessions: " + activeCount);
            }

        } catch (SQLException ex) {
            System.out.println("Live session table error: " + ex.getMessage());
        }
    }

    private void startLiveTableTimer() {
        if (liveTableTimer != null && liveTableTimer.isRunning()) {
            return;
        }
        liveTableTimer = new javax.swing.Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateLiveTableTimers();
            }
        });
        liveTableTimer.start();
    }

    private void updateLiveTableTimers() {
        if (tableLiveSessions == null || tableLiveSessions.isEditing()) {
            return;
        }
        DefaultTableModel model = (DefaultTableModel) tableLiveSessions.getModel();
        int rowCount = model.getRowCount();
        long now = System.currentTimeMillis();

        for (int i = 0; i < rowCount && i < liveSessionEntries.size(); i++) {
            LiveSessionEntry entry = liveSessionEntries.get(i);
            long elapsedSec = Math.max(0, (now - entry.startTimeMillis) / 1000);
            long hrs = elapsedSec / 3600;
            long mins = (elapsedSec % 3600) / 60;
            long secs = elapsedSec % 60;
            String durStr = String.format("%02d:%02d:%02d", hrs, mins, secs);

            int totalMin = Math.max(1, (int) Math.ceil(elapsedSec / 60.0));
            double curAmount = (totalMin / 60.0) * entry.hourlyRate;
            String amountStr = "Rs. " + String.format("%.2f", curAmount);

            model.setValueAt(durStr, i, 6);
            model.setValueAt(amountStr, i, 7);
        }
    }

    public void endLiveSessionByRow(int selectedRow) {
        if (selectedRow < 0 || selectedRow >= liveSessionEntries.size()) {
            return;
        }
        LiveSessionEntry entry = liveSessionEntries.get(selectedRow);

        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to end session " + entry.sessionId + " for " + entry.pcName + " (" + entry.pcId + ")?",
            "End Live Session Confirmation",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            long elapsedMillis = Math.max(0, System.currentTimeMillis() - entry.startTimeMillis);
            long elapsedSeconds = elapsedMillis / 1000;
            int totalMinutes = Math.max(1, (int) Math.ceil(elapsedSeconds / 60.0));
            double totalAmount = (totalMinutes / 60.0) * entry.hourlyRate;

            // 1. Update gaming_sessions
            pst = db.con.prepareStatement(
                "UPDATE gaming_sessions SET end_time = NOW(), total_minutes = ?, total_amount = ?, status = 'Completed' WHERE session_id = ?"
            );
            pst.setInt(1, totalMinutes);
            pst.setDouble(2, totalAmount);
            pst.setString(3, entry.sessionId);
            pst.executeUpdate();
            pst.close();

            // 2. Insert Invoice
            String invoiceNo = "INV-" + (System.currentTimeMillis() % 1000000);
            String empNo = (user != null && user.getEmpNo() != null) ? user.getEmpNo() : "GG_1234";
            try {
                pst = db.con.prepareStatement(
                    "INSERT INTO invoices (invoice_no, session_id, emp_no, sub_total, discount, net_total, payment_method, invoice_date) VALUES (?, ?, ?, ?, 0.00, ?, 'Cash', NOW())"
                );
                pst.setString(1, invoiceNo);
                pst.setString(2, entry.sessionId);
                pst.setString(3, empNo);
                pst.setDouble(4, totalAmount);
                pst.setDouble(5, totalAmount);
                pst.executeUpdate();
                pst.close();
            } catch (Exception ex) {
                System.out.println("Invoice save note: " + ex.getMessage());
            }

            // 3. Update pc_table status
            pst = db.con.prepareStatement("UPDATE pc_table SET status = 'Available' WHERE pc_id = ?");
            pst.setString(1, entry.pcId);
            pst.executeUpdate();
            pst.close();

            // 4. Receipt Dialog & Bill PDF Prompt
            String customerDisplay = entry.cusName;
            if (entry.cusPhone != null && !entry.cusPhone.equals("-") && !entry.cusPhone.isEmpty()) {
                customerDisplay += " (" + entry.cusPhone + ")";
            }

            String receipt = "========================================\n"
                    + "            SESSION COMPLETED           \n"
                    + "========================================\n"
                    + "Invoice No : " + invoiceNo + "\n"
                    + "Session ID : " + entry.sessionId + "\n"
                    + "Station    : " + entry.pcName + " (" + entry.pcId + ")\n"
                    + "Customer   : " + customerDisplay + "\n"
                    + "Duration   : " + totalMinutes + " mins (" + String.format("%02dh %02dm", totalMinutes / 60, totalMinutes % 60) + ")\n"
                    + "Hourly Rate: Rs. " + String.format("%.2f", entry.hourlyRate) + "/hr\n"
                    + "Total Due  : Rs. " + String.format("%.2f", totalAmount) + "\n"
                    + "========================================";

            int printOption = JOptionPane.showConfirmDialog(
                this,
                receipt + "\n\nDo you want to generate / print the PDF Bill Receipt?",
                "Session Summary & Bill",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.INFORMATION_MESSAGE
            );

            if (printOption == JOptionPane.YES_OPTION) {
                generateInvoiceBillPdf(invoiceNo, entry.sessionId, entry.pcName, entry.category, customerDisplay, totalMinutes, entry.hourlyRate, totalAmount, empNo);
            }

            // 5. Refresh all views
            loadLiveSessionsTable();
            loadPcGrid();
            loadSummaryCards();
            loadPcTable();
            loadUserPctable();
            loadPcRepairTable();

            // If ended PC is currently selected in dashboard panel, refresh it
            if (currentSelectedPcId != null && currentSelectedPcId.equals(entry.pcId)) {
                selectPcForSession(entry.pcId, entry.pcName, entry.category, String.valueOf(entry.hourlyRate), "Available");
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error ending live session: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    class LiveEndButtonRenderer extends JButton implements TableCellRenderer {
        public LiveEndButtonRenderer() {
            setOpaque(true);
            setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 12));
            setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            setText("End Session ⏹");
            setBackground(new java.awt.Color(217, 4, 41));
            setForeground(java.awt.Color.WHITE);
            setBorder(javax.swing.BorderFactory.createEmptyBorder(4, 8, 4, 8));
            return this;
        }
    }

    // =========================================================================
    // User Log Management & Recording (Login & Logout Tracker)
    // =========================================================================
    public void recordUserLogout() {
        try {
            if (currentLogId > 0) {
                pst = db.con.prepareStatement(
                    "UPDATE user_logs SET logout_time = NOW(), "
                    + "duration = CONCAT(TIMESTAMPDIFF(MINUTE, login_time, NOW()), ' mins'), "
                    + "status = 'Logged Out' WHERE log_id = ?"
                );
                pst.setInt(1, currentLogId);
                pst.executeUpdate();
                pst.close();
            } else if (user != null && user.getEmpNo() != null) {
                pst = db.con.prepareStatement(
                    "UPDATE user_logs SET logout_time = NOW(), "
                    + "duration = CONCAT(TIMESTAMPDIFF(MINUTE, login_time, NOW()), ' mins'), "
                    + "status = 'Logged Out' WHERE emp_no = ? AND status = 'Logged In' "
                    + "ORDER BY log_id DESC LIMIT 1"
                );
                pst.setString(1, user.getEmpNo());
                pst.executeUpdate();
                pst.close();
            }
        } catch (Exception ex) {
            System.out.println("Logout log record note: " + ex.getMessage());
        }
    }

    // SHA-256 password hashing helper
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
            return password;
        }
    }

    // =========================================================================
    // History & Reports Panel Table Loaders (User Logs, PC History, Invoices)
    // =========================================================================

    // 1. User Logs Table (jTable4)
    public void loadUserLogsTable() {
        try {
            DefaultTableModel dt = (DefaultTableModel) jTable4.getModel();
            dt.setRowCount(0);

            pst = db.con.prepareStatement("SELECT log_id, emp_no, username, role, login_time, logout_time, duration, status FROM user_logs ORDER BY log_id DESC");
            rs = pst.executeQuery();

            while (rs.next()) {
                Vector v = new Vector();
                v.add(rs.getInt("log_id"));
                v.add(rs.getString("emp_no"));
                v.add(rs.getString("username"));
                v.add(rs.getString("role"));
                v.add(rs.getString("login_time"));
                v.add(rs.getString("logout_time") != null ? rs.getString("logout_time") : "-");
                v.add(rs.getString("duration"));
                v.add(rs.getString("status"));
                dt.addRow(v);
            }
            pst.close();
            rs.close();
        } catch (Exception e) {
            System.out.println("User Logs load error: " + e.getMessage());
        }
    }

    // 2. PC Session History Table (jTable3)
    public void loadSessionHistoryTable() {
        try {
            DefaultTableModel dt = (DefaultTableModel) jTable3.getModel();
            dt.setRowCount(0);

            String sql = "SELECT s.session_id, s.pc_id, COALESCE(p.category, '-') AS category, s.cus_name, s.start_time, s.end_time, s.total_minutes, s.total_amount "
                    + "FROM gaming_sessions s "
                    + "LEFT JOIN pc_table p ON s.pc_id = p.pc_id "
                    + "ORDER BY s.start_time DESC";
            pst = db.con.prepareStatement(sql);
            rs = pst.executeQuery();

            while (rs.next()) {
                Vector v = new Vector();
                v.add(rs.getString("session_id"));
                v.add(rs.getString("pc_id"));
                v.add(rs.getString("category"));
                v.add(rs.getString("cus_name") != null ? rs.getString("cus_name") : "Walk-in");
                v.add(rs.getString("start_time"));
                v.add(rs.getString("end_time") != null ? rs.getString("end_time") : "Ongoing");
                v.add(rs.getInt("total_minutes"));
                v.add(String.format("%.2f", rs.getDouble("total_amount")));
                dt.addRow(v);
            }
            pst.close();
            rs.close();
        } catch (Exception e) {
            System.out.println("Session History load error: " + e.getMessage());
        }
    }

    // 3. Invoice & Billing History Table (jTable1)
    public void loadInvoiceHistoryTable() {
        try {
            DefaultTableModel dt = (DefaultTableModel) jTable1.getModel();
            dt.setRowCount(0);

            String sql = "SELECT invoice_no, session_id, emp_no, invoice_date, payment_method, sub_total, net_total FROM invoices ORDER BY invoice_date DESC";
            pst = db.con.prepareStatement(sql);
            rs = pst.executeQuery();

            while (rs.next()) {
                Vector v = new Vector();
                v.add(rs.getString("invoice_no"));
                v.add(rs.getString("session_id"));
                v.add(rs.getString("emp_no"));
                v.add(rs.getString("invoice_date"));
                v.add(rs.getString("payment_method"));
                v.add(String.format("%.2f", rs.getDouble("sub_total")));
                v.add(String.format("%.2f", rs.getDouble("net_total")));
                dt.addRow(v);
            }
            pst.close();
            rs.close();
        } catch (Exception e) {
            System.out.println("Invoice History load error: " + e.getMessage());
        }
    }

    // =========================================================================
    // PDF Export Utility (using iText library)
    // =========================================================================
    public void exportTableToPdf(JTable table, String reportTitle, String defaultFileName) {
        if (table == null || table.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "The table contains no data to export!", "No Data", JOptionPane.WARNING_MESSAGE);
            return;
        }

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save " + reportTitle + " as PDF");
        fileChooser.setSelectedFile(new File(defaultFileName + "_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".pdf"));
        fileChooser.setFileFilter(new FileNameExtensionFilter("PDF Documents (*.pdf)", "pdf"));

        int userSelection = fileChooser.showSaveDialog(this);
        if (userSelection != JFileChooser.APPROVE_OPTION) {
            return;
        }

        File fileToSave = fileChooser.getSelectedFile();
        if (!fileToSave.getName().toLowerCase().endsWith(".pdf")) {
            fileToSave = new File(fileToSave.getAbsolutePath() + ".pdf");
        }

        Document document = new Document(table.getColumnCount() > 5 ? PageSize.A4.rotate() : PageSize.A4, 20, 20, 20, 20);

        try {
            PdfWriter.getInstance(document, new FileOutputStream(fileToSave));
            document.open();

            // 1. Report Header Section
            com.itextpdf.text.Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, new BaseColor(11, 19, 43));
            Paragraph title = new Paragraph("GAMING CAFE MANAGEMENT SYSTEM", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            com.itextpdf.text.Font subTitleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, new BaseColor(0, 153, 255));
            Paragraph subTitle = new Paragraph(reportTitle, subTitleFont);
            subTitle.setAlignment(Element.ALIGN_CENTER);
            subTitle.setSpacingAfter(8);
            document.add(subTitle);

            // Generated date and printed staff details
            com.itextpdf.text.Font metaFont = FontFactory.getFont(FontFactory.HELVETICA, 9, BaseColor.DARK_GRAY);
            String printedBy = (user != null && user.getUserName() != null) ? user.getUserName() : "System Admin";
            String metaText = "Generated On: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + "  |  Generated By: " + printedBy;
            Paragraph meta = new Paragraph(metaText, metaFont);
            meta.setAlignment(Element.ALIGN_CENTER);
            meta.setSpacingAfter(15);
            document.add(meta);

            // 2. Report Table Section
            int colCount = table.getColumnCount();
            PdfPTable pdfTable = new PdfPTable(colCount);
            pdfTable.setWidthPercentage(100);

            // Table Header Row
            com.itextpdf.text.Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, BaseColor.WHITE);
            for (int c = 0; c < colCount; c++) {
                String colName = table.getColumnName(c);
                PdfPCell cell = new PdfPCell(new Phrase(colName, headerFont));
                cell.setBackgroundColor(new BaseColor(11, 19, 43));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setPaddingTop(6);
                cell.setPaddingBottom(6);
                pdfTable.addCell(cell);
            }

            // Table Data Rows
            com.itextpdf.text.Font dataFont = FontFactory.getFont(FontFactory.HELVETICA, 9, BaseColor.BLACK);
            for (int r = 0; r < table.getRowCount(); r++) {
                BaseColor rowBg = (r % 2 == 0) ? BaseColor.WHITE : new BaseColor(245, 247, 250);
                for (int c = 0; c < colCount; c++) {
                    Object val = table.getValueAt(r, c);
                    String text = val != null ? val.toString().replaceAll("<[^>]*>", "").trim() : "";
                    PdfPCell cell = new PdfPCell(new Phrase(text, dataFont));
                    cell.setBackgroundColor(rowBg);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    cell.setPaddingTop(5);
                    cell.setPaddingBottom(5);
                    pdfTable.addCell(cell);
                }
            }

            document.add(pdfTable);

            // Footer note
            Paragraph footer = new Paragraph("\n* End of Report - Gaming Cafe Management System *", metaFont);
            footer.setAlignment(Element.ALIGN_CENTER);
            document.add(footer);

            document.close();

            int openOption = JOptionPane.showConfirmDialog(
                this,
                "PDF Report saved successfully!\nLocation: " + fileToSave.getAbsolutePath() + "\n\nWould you like to open it now?",
                "Export Successful",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.INFORMATION_MESSAGE
            );

            if (openOption == JOptionPane.YES_OPTION) {
                if (Desktop.isDesktopSupported()) {
                    Desktop.getDesktop().open(fileToSave);
                }
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Failed to generate PDF: " + ex.getMessage(), "PDF Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            if (document.isOpen()) {
                document.close();
            }
        }
    }

    // =========================================================================
    // Bill / Invoice Receipt PDF Generator (using iText)
    // =========================================================================
    public void generateInvoiceBillPdf(String invoiceNo, String sessionId, String pcName, String category, String customer, int durationMinutes, double hourlyRate, double totalAmount, String staffId) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Bill / Invoice Receipt as PDF");
        fileChooser.setSelectedFile(new File("Bill_" + invoiceNo + ".pdf"));
        fileChooser.setFileFilter(new FileNameExtensionFilter("PDF Documents (*.pdf)", "pdf"));

        int choice = fileChooser.showSaveDialog(this);
        if (choice != JFileChooser.APPROVE_OPTION) {
            return;
        }

        File fileToSave = fileChooser.getSelectedFile();
        if (!fileToSave.getName().toLowerCase().endsWith(".pdf")) {
            fileToSave = new File(fileToSave.getAbsolutePath() + ".pdf");
        }

        Document document = new Document(PageSize.A6, 15, 15, 15, 15);
        try {
            PdfWriter.getInstance(document, new FileOutputStream(fileToSave));
            document.open();

            com.itextpdf.text.Font brandFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 15, new BaseColor(11, 19, 43));
            Paragraph brand = new Paragraph("GAMING CAFE", brandFont);
            brand.setAlignment(Element.ALIGN_CENTER);
            document.add(brand);

            com.itextpdf.text.Font subFont = FontFactory.getFont(FontFactory.HELVETICA, 8, BaseColor.GRAY);
            Paragraph sub = new Paragraph("High-End Gaming & Esports Zone\nTel: +94 77 123 4567\n-----------------------------------------------------", subFont);
            sub.setAlignment(Element.ALIGN_CENTER);
            document.add(sub);

            com.itextpdf.text.Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, new BaseColor(0, 153, 255));
            Paragraph receiptTitle = new Paragraph("OFFICIAL PAYMENT RECEIPT", titleFont);
            receiptTitle.setAlignment(Element.ALIGN_CENTER);
            receiptTitle.setSpacingAfter(6);
            document.add(receiptTitle);

            PdfPTable table = new PdfPTable(2);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{40, 60});

            com.itextpdf.text.Font keyFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 8, BaseColor.BLACK);
            com.itextpdf.text.Font valFont = FontFactory.getFont(FontFactory.HELVETICA, 8, BaseColor.DARK_GRAY);

            addBillRow(table, "Invoice No:", invoiceNo, keyFont, valFont);
            addBillRow(table, "Date & Time:", new SimpleDateFormat("yyyy-MM-dd hh:mm a").format(new Date()), keyFont, valFont);
            addBillRow(table, "Session ID:", sessionId, keyFont, valFont);
            addBillRow(table, "Station / PC:", pcName + (category != null && !category.isEmpty() ? " (" + category + ")" : ""), keyFont, valFont);
            addBillRow(table, "Customer:", customer, keyFont, valFont);
            addBillRow(table, "Cashier / Staff:", staffId, keyFont, valFont);
            addBillRow(table, "Duration:", durationMinutes + " mins (" + String.format("%02dh %02dm", durationMinutes / 60, durationMinutes % 60) + ")", keyFont, valFont);
            addBillRow(table, "Hourly Rate:", "Rs. " + String.format("%.2f", hourlyRate) + "/hr", keyFont, valFont);

            document.add(table);

            Paragraph divider = new Paragraph("-----------------------------------------------------", subFont);
            divider.setAlignment(Element.ALIGN_CENTER);
            document.add(divider);

            // Total Amount Highlight
            com.itextpdf.text.Font totalFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, new BaseColor(217, 4, 41));
            Paragraph totalPara = new Paragraph("NET TOTAL : Rs. " + String.format("%.2f", totalAmount), totalFont);
            totalPara.setAlignment(Element.ALIGN_CENTER);
            totalPara.setSpacingAfter(6);
            document.add(totalPara);

            Paragraph thankYou = new Paragraph("Thank you for gaming with us!\nCome back soon!", subFont);
            thankYou.setAlignment(Element.ALIGN_CENTER);
            document.add(thankYou);

            document.close();

            int open = JOptionPane.showConfirmDialog(
                this,
                "Bill PDF saved successfully!\nLocation: " + fileToSave.getAbsolutePath() + "\n\nWould you like to open it now?",
                "Bill Generated",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.INFORMATION_MESSAGE
            );

            if (open == JOptionPane.YES_OPTION && Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(fileToSave);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error generating bill: " + ex.getMessage(), "PDF Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            if (document.isOpen()) {
                document.close();
            }
        }
    }

    private void addBillRow(PdfPTable table, String key, String val, com.itextpdf.text.Font keyFont, com.itextpdf.text.Font valFont) {
        PdfPCell c1 = new PdfPCell(new Phrase(key, keyFont));
        c1.setBorder(com.itextpdf.text.Rectangle.NO_BORDER);
        c1.setPaddingTop(2);
        c1.setPaddingBottom(2);

        PdfPCell c2 = new PdfPCell(new Phrase(val, valFont));
        c2.setBorder(com.itextpdf.text.Rectangle.NO_BORDER);
        c2.setHorizontalAlignment(Element.ALIGN_RIGHT);
        c2.setPaddingTop(2);
        c2.setPaddingBottom(2);

        table.addCell(c1);
        table.addCell(c2);
    }

    // Right-click context popup menu for PDF export on tables
    private void setupTablePdfExportPopups() {
        addPdfPopupToTable(jTable4, "User Activity Logs Report", "User_Logs_Report");
        addPdfPopupToTable(jTable3, "PC Session History Report", "PC_Session_History");

        // Custom Popup on Invoices Table with Print Bill option
        if (jTable1 != null) {
            JPopupMenu invPopup = new JPopupMenu();
            JMenuItem printBillItem = new JMenuItem("Print Selected Bill / Receipt 🧾");
            printBillItem.setFont(new Font("Segoe UI", Font.BOLD, 12));
            printBillItem.addActionListener(e -> {
                int row = jTable1.getSelectedRow();
                if (row != -1) {
                    String invNo = jTable1.getValueAt(row, 0).toString();
                    String sesId = jTable1.getValueAt(row, 1).toString();
                    String staffId = jTable1.getValueAt(row, 2).toString();
                    String netTotStr = jTable1.getValueAt(row, 6).toString();
                    double netTot = 0.0;
                    try { netTot = Double.parseDouble(netTotStr.replace(",", "")); } catch (Exception ignored) {}
                    generateInvoiceBillPdf(invNo, sesId, "Station", "Gaming Cafe", "Customer", 60, netTot, netTot, staffId);
                } else {
                    JOptionPane.showMessageDialog(this, "Please select an invoice row from the table first!", "Select Invoice", JOptionPane.WARNING_MESSAGE);
                }
            });
            invPopup.add(printBillItem);

            JMenuItem exportItem = new JMenuItem("Export as PDF Report 📄");
            exportItem.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            exportItem.addActionListener(e -> exportTableToPdf(jTable1, "Invoices & Billing Report", "Invoices_Report"));
            invPopup.add(exportItem);

            JMenuItem refreshItem = new JMenuItem("Refresh Data 🔄");
            refreshItem.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            refreshItem.addActionListener(e -> loadInvoiceHistoryTable());
            invPopup.add(refreshItem);

            jTable1.setComponentPopupMenu(invPopup);
        }

        addPdfPopupToTable(tablePc, "PC Inventory & Stations Report", "PC_Inventory_Report");
        addPdfPopupToTable(tableUser, "System User Accounts Report", "User_Accounts_Report");
        addPdfPopupToTable(tableRepairPc, "PC Maintenance & Repair Report", "PC_Repair_Report");
        addPdfPopupToTable(tableLiveSessions, "Live Active Gaming Sessions Report", "Live_Sessions_Report");
        addPdfPopupToTable(tableUserPcManage, "Station Management Report", "Station_Manage_Report");
    }

    private void addPdfPopupToTable(JTable table, String reportTitle, String fileName) {
        if (table == null) return;
        JPopupMenu popup = new JPopupMenu();
        JMenuItem exportItem = new JMenuItem("Export as PDF Report 📄");
        exportItem.setFont(new Font("Segoe UI", Font.BOLD, 12));
        exportItem.addActionListener(e -> exportTableToPdf(table, reportTitle, fileName));
        popup.add(exportItem);

        JMenuItem refreshItem = new JMenuItem("Refresh Data 🔄");
        refreshItem.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        refreshItem.addActionListener(e -> {
            loadUserLogsTable();
            loadSessionHistoryTable();
            loadInvoiceHistoryTable();
            loadPcTable();
            loadUserTable();
            loadPcRepairTable();
            loadLiveSessionsTable();
            loadUserPctable();
        });
        popup.add(refreshItem);

        table.setComponentPopupMenu(popup);
    }

    // =========================================================================
    // Keyboard Shortcuts & Enter Key Navigation
    // =========================================================================
    private void setupKeyboardShortcuts() {
        // Global Function Keys (F1 - F6) Dispatcher
        java.awt.KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new java.awt.KeyEventDispatcher() {
            @Override
            public boolean dispatchKeyEvent(KeyEvent e) {
                if (e.getID() == KeyEvent.KEY_PRESSED) {
                    switch (e.getKeyCode()) {
                        case KeyEvent.VK_F1:
                            btnDash.doClick();
                            return true;
                        case KeyEvent.VK_F2:
                            btnLive.doClick();
                            return true;
                        case KeyEvent.VK_F3:
                            btnPc.doClick();
                            return true;
                        case KeyEvent.VK_F4:
                            if (btnAdmin.isVisible()) {
                                btnAdmin.doClick();
                            } else {
                                btnReports.doClick();
                            }
                            return true;
                        case KeyEvent.VK_F5:
                            // F5: Refresh current view / Clean
                            btnClean.doClick();
                            loadPcGrid();
                            loadSummaryCards();
                            loadLiveSessionsTable();
                            loadUserLogsTable();
                            loadSessionHistoryTable();
                            loadInvoiceHistoryTable();
                            return true;
                        case KeyEvent.VK_F6:
                            // F6: Quick PDF Export of History tables
                            if (pnlHistory.isVisible()) {
                                exportTableToPdf(jTable4, "User Activity Logs Report", "User_Logs_Report");
                            } else if (pnlLive.isVisible()) {
                                exportTableToPdf(tableLiveSessions, "Live Active Gaming Sessions Report", "Live_Sessions_Report");
                            } else if (pnlPc.isVisible() || pnlPcM.isVisible()) {
                                exportTableToPdf(tablePc, "PC Inventory Report", "PC_Inventory_Report");
                            } else {
                                exportTableToPdf(jTable1, "Invoices Report", "Invoices_Report");
                            }
                            return true;
                    }
                }
                return false;
            }
        });

        // Enter key listeners on Search TextBoxes
        if (txtPCSearch != null) {
            txtPCSearch.addActionListener(e -> txtPCSearchKeyReleased(null));
        }
        if (txtLiveSearch != null) {
            txtLiveSearch.addActionListener(e -> loadLiveSessionsTable(txtLiveSearch.getText().trim()));
        }
        if (jTextField1 != null) {
            jTextField1.addActionListener(e -> jButton1.doClick());
        }
        if (jTextField5 != null) {
            jTextField5.addActionListener(e -> jButton4.doClick());
        }
    }

}

class ButtonEditor extends javax.swing.DefaultCellEditor {

    private javax.swing.JPanel panel;
    private javax.swing.JButton button;
    private javax.swing.JTable table;
    private boolean isPushed;

    public ButtonEditor(javax.swing.JCheckBox checkBox) {
        super(checkBox);
        panel = new javax.swing.JPanel(new java.awt.BorderLayout());
        panel.setBorder(javax.swing.BorderFactory.createEmptyBorder(8, 15, 8, 15));

        button = new javax.swing.JButton("Update ⚙️");
        button.setBackground(new java.awt.Color(0, 153, 255));
        button.setForeground(java.awt.Color.WHITE);
        button.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 12));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR)); // Mouse cursor

        button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                fireEditingStopped();
            }
        });
        panel.add(button, java.awt.BorderLayout.CENTER);
    }

    @Override
    public java.awt.Component getTableCellEditorComponent(javax.swing.JTable table, Object value,
            boolean isSelected, int row, int column) {
        this.table = table;
        isPushed = true;

        if (isSelected) {
            panel.setBackground(table.getSelectionBackground());
        } else {
            panel.setBackground(row % 2 == 0 ? java.awt.Color.WHITE : new java.awt.Color(248, 250, 252));
        }
        return panel;
    }

    @Override
    public Object getCellEditorValue() {
        if (isPushed) {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                java.awt.Window window = javax.swing.SwingUtilities.getWindowAncestor(table);
                if (window instanceof Dash) {
                    ((Dash) window).editPcFromTable(selectedRow);
                }
            }
        }
        isPushed = false;
        return "Update ⚙️";
    }

    @Override
    public boolean stopCellEditing() {
        isPushed = false;
        return super.stopCellEditing();
    }
}

class LiveEndButtonEditor extends javax.swing.DefaultCellEditor {

    private javax.swing.JPanel panel;
    private javax.swing.JButton button;
    private javax.swing.JTable table;
    private boolean isPushed;

    public LiveEndButtonEditor(javax.swing.JCheckBox checkBox) {
        super(checkBox);
        panel = new javax.swing.JPanel(new java.awt.BorderLayout());
        panel.setBorder(javax.swing.BorderFactory.createEmptyBorder(6, 10, 6, 10));

        button = new javax.swing.JButton("End Session ⏹");
        button.setBackground(new java.awt.Color(217, 4, 41));
        button.setForeground(java.awt.Color.WHITE);
        button.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 12));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                fireEditingStopped();
            }
        });
        panel.add(button, java.awt.BorderLayout.CENTER);
    }

    @Override
    public java.awt.Component getTableCellEditorComponent(javax.swing.JTable table, Object value,
            boolean isSelected, int row, int column) {
        this.table = table;
        isPushed = true;
        panel.setBackground(table.getSelectionBackground());
        return panel;
    }

    @Override
    public Object getCellEditorValue() {
        if (isPushed) {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                Window window = SwingUtilities.getWindowAncestor(table);
                if (window instanceof Dash) {
                    ((Dash) window).endLiveSessionByRow(selectedRow);
                }
            }
        }
        isPushed = false;
        return "End Session ⏹";
    }

    @Override
    public boolean stopCellEditing() {
        isPushed = false;
        return super.stopCellEditing();
    }
}
