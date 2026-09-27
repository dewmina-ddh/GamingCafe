import os
import matplotlib
matplotlib.use('Agg')
import matplotlib.pyplot as plt
import matplotlib.patches as patches

os.makedirs('srs_assets', exist_ok=True)

# -------------------------------------------------------------
# 1. Use Case Diagram
# -------------------------------------------------------------
fig, ax = plt.subplots(figsize=(14, 10), dpi=300)
ax.set_facecolor('#f8fafc')
fig.patch.set_facecolor('#ffffff')
ax.set_xlim(0, 140)
ax.set_ylim(0, 100)
ax.axis('off')

# Title
ax.text(70, 96, "USE CASE DIAGRAM - GAMING CAFE MANAGEMENT SYSTEM", fontsize=14, fontweight='bold', ha='center', color='#0f172a', fontfamily='sans-serif')
ax.text(70, 93, "IEEE 830 / ISO/IEC/IEEE 29148 Standard Behavioral Specification", fontsize=10, fontstyle='italic', ha='center', color='#475569')

# System Boundary Box
boundary = patches.FancyBboxPatch((28, 5), 84, 84, boxstyle="round,pad=1", ec="#0284c7", fc="#f0f9ff", lw=2, linestyle='--')
ax.add_patch(boundary)
ax.text(70, 86, "«Gaming Cafe Desktop Application System Boundary»", fontsize=11, fontweight='bold', ha='center', color='#0369a1')

# Actors: Admin (Left), Staff / Cashier (Left), Customer / Gamer (Right)
def draw_actor(ax, x, y, label, role_color="#1e293b"):
    # Head
    circle = patches.Circle((x, y+3.5), 1.8, ec=role_color, fc='#ffffff', lw=2)
    ax.add_patch(circle)
    # Body
    ax.plot([x, x], [y+1.7, y-2.5], color=role_color, lw=2)
    # Arms
    ax.plot([x-2.5, x+2.5], [y, y], color=role_color, lw=2)
    # Legs
    ax.plot([x, x-2.2], [y-2.5, y-6], color=role_color, lw=2)
    ax.plot([x, x+2.2], [y-2.5, y-6], color=role_color, lw=2)
    # Label
    ax.text(x, y-8.5, label, fontsize=10, fontweight='bold', ha='center', color=role_color)

draw_actor(ax, 14, 68, "System Administrator\n(Owner / IT Manager)", "#b45309")
draw_actor(ax, 14, 30, "Cafe Staff / Cashier\n(Front-Desk Operator)", "#0369a1")
draw_actor(ax, 126, 48, "Gamer / Customer\n(Indirect Player / Token Holder)", "#059669")

# Use Cases (Ovals)
use_cases = [
    (70, 78, "UC-01: Secure Login & Role Gating", "#e0f2fe", "#0284c7", ["admin", "staff"]),
    (70, 70, "UC-02: Manage Staff Users & Reset Credentials", "#fef3c7", "#d97706", ["admin"]),
    (70, 62, "UC-03: Live Gaming Stations Telemetry & Grid", "#e0f2fe", "#0284c7", ["admin", "staff"]),
    (70, 54, "UC-04: Start Session & Allocate Time (Min 1h)", "#e0f2fe", "#0284c7", ["admin", "staff", "customer"]),
    (70, 46, "UC-05: Real-time Countdown & Dynamic Add Time", "#e0f2fe", "#0284c7", ["admin", "staff", "customer"]),
    (70, 38, "UC-06: End Session & Compute Billing Invoice", "#e0f2fe", "#0284c7", ["admin", "staff", "customer"]),
    (70, 30, "UC-07: Hardware Station & Specs Inventory", "#fef3c7", "#d97706", ["admin"]),
    (70, 22, "UC-08: Log Hardware Defect & Track Repair History", "#e0f2fe", "#0284c7", ["admin", "staff"]),
    (70, 14, "UC-09: Generate Comprehensive Business PDF Reports", "#fef3c7", "#d97706", ["admin"]),
    (70, 6, "UC-10: Token / QR Code Instant Station Check-in", "#d1fae5", "#059669", ["admin", "staff", "customer"])
]

for ux, uy, utext, ufc, uec, actors in use_cases:
    ellipse = patches.Ellipse((ux, uy), 42, 5.8, ec=uec, fc=ufc, lw=1.5)
    ax.add_patch(ellipse)
    ax.text(ux, uy, utext, fontsize=8.5, fontweight='bold', ha='center', va='center', color='#1e293b')
    
    # Connecting Lines
    if "admin" in actors:
        ax.annotate("", xy=(ux-21, uy), xytext=(16, 68), arrowprops=dict(arrowstyle="-", color="#b45309", lw=1.2, alpha=0.7))
    if "staff" in actors:
        ax.annotate("", xy=(ux-21, uy), xytext=(16, 30), arrowprops=dict(arrowstyle="-", color="#0284c7", lw=1.2, alpha=0.7))
    if "customer" in actors:
        ax.annotate("", xy=(ux+21, uy), xytext=(124, 48), arrowprops=dict(arrowstyle="-", color="#059669", lw=1.2, linestyle="--", alpha=0.7))

plt.tight_layout()
plt.savefig('srs_assets/use_case_diagram.png', dpi=300, bbox_inches='tight')
plt.close()
print("Use Case Diagram generated.")

# -------------------------------------------------------------
# 2. Database Entity Relationship (ER) Diagram
# -------------------------------------------------------------
fig, ax = plt.subplots(figsize=(16, 11), dpi=300)
ax.set_facecolor('#f8fafc')
fig.patch.set_facecolor('#ffffff')
ax.set_xlim(0, 160)
ax.set_ylim(0, 110)
ax.axis('off')

ax.text(80, 106, "RELATIONAL DATABASE ENTITY RELATIONSHIP (ER) DIAGRAM", fontsize=15, fontweight='bold', ha='center', color='#0f172a')
ax.text(80, 103, "MySQL Relational Schema Normalized to Third Normal Form (3NF)", fontsize=10.5, fontstyle='italic', ha='center', color='#475569')

def draw_table_box(ax, x, y, w, h, title, columns, header_color="#0f766e"):
    # Outer box
    outer = patches.Rectangle((x, y-h), w, h, ec='#cbd5e1', fc='#ffffff', lw=1.5, zorder=2)
    ax.add_patch(outer)
    # Header box
    hdr = patches.Rectangle((x, y-4.5), w, 4.5, ec=header_color, fc=header_color, lw=1.5, zorder=3)
    ax.add_patch(hdr)
    ax.text(x + w/2, y - 2.5, title, fontsize=9.5, fontweight='bold', color='#ffffff', ha='center', va='center', zorder=4)
    # Columns
    cur_y = y - 7
    for col, col_type, is_pk, is_fk in columns:
        pkfk = "[PK] " if is_pk else ("[FK] " if is_fk else "      ")
        text_color = "#b91c1c" if is_pk else ("#0284c7" if is_fk else "#334155")
        weight = "bold" if (is_pk or is_fk) else "normal"
        ax.text(x + 1.5, cur_y, f"{pkfk}{col}", fontsize=8, fontweight=weight, color=text_color, va='center', zorder=4)
        ax.text(x + w - 1.5, cur_y, col_type, fontsize=7.5, color='#64748b', ha='right', va='center', zorder=4)
        cur_y -= 2.6

tables = [
    (10, 98, 38, 32, "users", [
        ("user_id", "INT AUTO_INC", True, False),
        ("f_name", "VARCHAR(50)", False, False),
        ("l_name", "VARCHAR(50)", False, False),
        ("nic", "VARCHAR(20)", False, False),
        ("phone", "VARCHAR(15)", False, False),
        ("email", "VARCHAR(100)", False, False),
        ("username", "VARCHAR(50)", False, False),
        ("password", "VARCHAR(255)", False, False),
        ("role", "VARCHAR(20)", False, False),
        ("status", "VARCHAR(20)", False, False),
        ("first_time", "INT (0/1)", False, False)
    ], "#1e293b"),
    
    (10, 58, 38, 22, "user_logs", [
        ("log_id", "INT AUTO_INC", True, False),
        ("user_id", "INT", False, True),
        ("username", "VARCHAR(50)", False, False),
        ("role", "VARCHAR(20)", False, False),
        ("login_time", "DATETIME", False, False),
        ("logout_time", "DATETIME", False, False),
        ("session_duration", "VARCHAR(30)", False, False)
    ], "#334155"),

    (62, 98, 42, 34, "pc (gaming_stations)", [
        ("id", "VARCHAR(20)", True, False),
        ("pc_name", "VARCHAR(50)", False, False),
        ("category", "VARCHAR(30)", False, False),
        ("hourly_rate", "DECIMAL(10,2)", False, False),
        ("cpu", "VARCHAR(50)", False, False),
        ("motherboard", "VARCHAR(50)", False, False),
        ("ram", "VARCHAR(30)", False, False),
        ("vga", "VARCHAR(50)", False, False),
        ("status", "VARCHAR(20)", False, False),
        ("created_at", "TIMESTAMP", False, False)
    ], "#0284c7"),

    (115, 98, 40, 36, "gaming_sessions", [
        ("session_id", "VARCHAR(50)", True, False),
        ("pc_id", "VARCHAR(20)", False, True),
        ("cus_name", "VARCHAR(100)", False, False),
        ("phone", "VARCHAR(15)", False, False),
        ("start_time", "DATETIME", False, False),
        ("end_time", "DATETIME", False, False),
        ("add_minutes", "INT", False, False),
        ("price_per_adding", "DECIMAL(10,2)", False, False),
        ("total_minutes", "INT", False, False),
        ("total_amount", "DECIMAL(10,2)", False, False),
        ("status", "VARCHAR(20)", False, False)
    ], "#059669"),

    (62, 56, 42, 22, "repair_pc", [
        ("repair_id", "INT AUTO_INC", True, False),
        ("pc_id", "VARCHAR(20)", False, True),
        ("pc_name", "VARCHAR(50)", False, False),
        ("reason", "VARCHAR(255)", False, False),
        ("date", "DATE", False, False),
        ("status", "VARCHAR(20)", False, False)
    ], "#d97706"),

    (62, 28, 42, 22, "repair_history", [
        ("history_id", "INT AUTO_INC", True, False),
        ("pc_id", "VARCHAR(20)", False, True),
        ("pc_name", "VARCHAR(50)", False, False),
        ("repair_reason", "VARCHAR(255)", False, False),
        ("repaired_date", "DATE", False, False),
        ("cost", "DECIMAL(10,2)", False, False),
        ("technician", "VARCHAR(50)", False, False)
    ], "#b45309"),

    (115, 54, 40, 32, "invoices", [
        ("invoice_no", "VARCHAR(50)", True, False),
        ("session_id", "VARCHAR(50)", False, True),
        ("pc_id", "VARCHAR(20)", False, True),
        ("customer_name", "VARCHAR(100)", False, False),
        ("duration_mins", "INT", False, False),
        ("total_amount", "DECIMAL(10,2)", False, False),
        ("discount", "DECIMAL(10,2)", False, False),
        ("net_amount", "DECIMAL(10,2)", False, False),
        ("payment_type", "VARCHAR(20)", False, False),
        ("cashier_name", "VARCHAR(50)", False, False),
        ("created_at", "DATETIME", False, False)
    ], "#047857"),

    (10, 28, 38, 18, "settings", [
        ("setting_id", "INT", True, False),
        ("default_password", "VARCHAR(50)", False, False),
        ("cafe_name", "VARCHAR(100)", False, False),
        ("currency", "VARCHAR(10)", False, False),
        ("updated_at", "TIMESTAMP", False, False)
    ], "#475569")
]

for x, y, w, h, tname, cols, hcol in tables:
    draw_table_box(ax, x, y, w, h, tname, cols, hcol)

# Relationship Lines with Crow's foot / notations
# 1. users -> user_logs (1:N)
ax.annotate("", xy=(29, 58), xytext=(29, 66), arrowprops=dict(arrowstyle="->", color="#334155", lw=1.5))
ax.text(31, 62, "1 : N", fontsize=8, fontweight='bold', color="#334155")

# 2. pc -> gaming_sessions (1:N)
ax.annotate("", xy=(115, 85), xytext=(104, 85), arrowprops=dict(arrowstyle="->", color="#0284c7", lw=1.5))
ax.text(107, 86.5, "1 : N", fontsize=8, fontweight='bold', color="#0284c7")

# 3. gaming_sessions -> invoices (1:1)
ax.annotate("", xy=(135, 54), xytext=(135, 62), arrowprops=dict(arrowstyle="->", color="#059669", lw=1.5))
ax.text(137, 58, "1 : 1", fontsize=8, fontweight='bold', color="#059669")

# 4. pc -> repair_pc (1:N)
ax.annotate("", xy=(83, 56), xytext=(83, 64), arrowprops=dict(arrowstyle="->", color="#d97706", lw=1.5))
ax.text(85, 60, "1 : N", fontsize=8, fontweight='bold', color="#d97706")

# 5. repair_pc -> repair_history (1:1)
ax.annotate("", xy=(83, 28), xytext=(83, 34), arrowprops=dict(arrowstyle="->", color="#b45309", lw=1.5))
ax.text(85, 31, "1 : 1", fontsize=8, fontweight='bold', color="#b45309")

plt.tight_layout()
plt.savefig('srs_assets/er_diagram.png', dpi=300, bbox_inches='tight')
plt.close()
print("ER Diagram generated.")

# -------------------------------------------------------------
# 3. 3-Tier Layered Architecture Diagram
# -------------------------------------------------------------
fig, ax = plt.subplots(figsize=(14, 8), dpi=300)
ax.set_facecolor('#f8fafc')
fig.patch.set_facecolor('#ffffff')
ax.set_xlim(0, 140)
ax.set_ylim(0, 90)
ax.axis('off')

ax.text(70, 85, "3-TIER LAYERED ARCHITECTURE SPECIFICATION", fontsize=14, fontweight='bold', ha='center', color='#0f172a')
ax.text(70, 81, "Presentation, Business Logic Controller, and JDBC Data Access Architecture", fontsize=10, fontstyle='italic', ha='center', color='#475569')

def draw_tier(ax, x, y, w, h, title, subtitle, items, color_bg, color_border, color_hdr):
    box = patches.FancyBboxPatch((x, y), w, h, boxstyle="round,pad=0.8", ec=color_border, fc=color_bg, lw=2)
    ax.add_patch(box)
    ax.text(x + 4, y + h - 4, title, fontsize=11, fontweight='bold', color=color_hdr)
    ax.text(x + 4, y + h - 7, subtitle, fontsize=8.5, fontstyle='italic', color='#64748b')
    
    # Items inside
    box_w = (w - 8) / len(items)
    for i, it in enumerate(items):
        ibox = patches.FancyBboxPatch((x + 4 + i*box_w + 1, y + 2), box_w - 2, h - 11, boxstyle="round,pad=0.4", ec='#cbd5e1', fc='#ffffff', lw=1)
        ax.add_patch(ibox)
        ax.text(x + 4 + i*box_w + box_w/2, y + h/2 - 2, it[0], fontsize=9, fontweight='bold', ha='center', va='center', color='#1e293b')
        ax.text(x + 4 + i*box_w + box_w/2, y + h/2 - 6, it[1], fontsize=7.5, ha='center', va='center', color='#64748b')

draw_tier(ax, 10, 56, 120, 20, "1. PRESENTATION LAYER (GUI TIER)", "Java Swing UI Engine with FlatLaf Theme & Event Listeners", [
    ("Login & Auth Views", "LoginFrame.java\nChangeUserDetails.java"),
    ("Main Dashboard View", "Dash.java / Dash.form\nLive Session Grid"),
    ("Billing & Invoice View", "BillFrame.java\nReceipt Dialog"),
    ("Reporting & Dialogs", "PDF Export Dialog\nJasper / iText Render")
], "#f0f9ff", "#0284c7", "#0369a1")

draw_tier(ax, 10, 31, 120, 20, "2. BUSINESS LOGIC LAYER (CONTROLLER TIER)", "Domain Rules, Session Timers, Price Engine, Input Validations & Security", [
    ("Real-time Countdown Engine", "javax.swing.Timer\nPer-second Telemetry"),
    ("Pricing & Time Extension", "Hourly Rate Calculator\nDynamic Add-Time"),
    ("Cryptographic Security", "SHA-256 Digest Engine\nfirst_time Life-cycle"),
    ("PDF Generation Services", "iText / Jasper Exporter\nMulti-table PDF Writer")
], "#fefce8", "#ca8a04", "#854d0e")

draw_tier(ax, 10, 6, 120, 20, "3. DATA ACCESS & PERSISTENCE LAYER (DATA TIER)", "Centralized JDBC Connection Pool, Parameterized PreparedStatements & MySQL RDBMS", [
    ("JDBC Connection Manager", "db.java (con instance)\nAuto Reconnection"),
    ("Parameterized SQL Engine", "PreparedStatement\nSQL Injection Shield"),
    ("ACID Transaction Manager", "Commit / Rollback Safe\nIntegrity Constraint"),
    ("MySQL Relational Store", "Port 3306 Store\nInnoDB Engine 3NF")
], "#ecfdf5", "#059669", "#047857")

# Flow arrows between tiers
ax.annotate("", xy=(70, 56), xytext=(70, 51), arrowprops=dict(arrowstyle="<->", color="#0284c7", lw=2))
ax.annotate("", xy=(70, 31), xytext=(70, 26), arrowprops=dict(arrowstyle="<->", color="#059669", lw=2))

plt.tight_layout()
plt.savefig('srs_assets/architecture_diagram.png', dpi=300, bbox_inches='tight')
plt.close()
print("Architecture Diagram generated.")

# -------------------------------------------------------------
# 4. QR Code / Token Station Check-in Workflow Diagram
# -------------------------------------------------------------
fig, ax = plt.subplots(figsize=(14, 7), dpi=300)
ax.set_facecolor('#f8fafc')
fig.patch.set_facecolor('#ffffff')
ax.set_xlim(0, 140)
ax.set_ylim(0, 80)
ax.axis('off')

ax.text(70, 75, "QR CODE / TOKEN SCANNER SESSION CHECK-IN WORKFLOW", fontsize=14, fontweight='bold', ha='center', color='#0f172a')
ax.text(70, 71, "Automated Player Pass Scanning, Station Allocation & Instant Countdown Initialization", fontsize=10, fontstyle='italic', ha='center', color='#475569')

steps = [
    (15, 45, 22, 18, "Step 1: Scan Token/QR", "Player presents QR pass\nor token at station barcode scanner.", "#e0f2fe", "#0284c7"),
    (45, 45, 22, 18, "Step 2: Token Verification", "System verifies Player ID,\nwallet balance & validity.", "#fef3c7", "#d97706"),
    (75, 45, 22, 18, "Step 3: Station Allocation", "Auto-selects available PC\nor PS5 & sets Occupied status.", "#d1fae5", "#059669"),
    (105, 45, 22, 18, "Step 4: Session Start & Run", "Timer activates countdown;\nunlocks client screen.", "#f3e8ff", "#9333ea")
]

for sx, sy, sw, sh, stitle, sdesc, sbg, sborder in steps:
    box = patches.FancyBboxPatch((sx, sy), sw, sh, boxstyle="round,pad=0.6", ec=sborder, fc=sbg, lw=2)
    ax.add_patch(box)
    ax.text(sx + sw/2, sy + sh - 4, stitle, fontsize=9.5, fontweight='bold', ha='center', color='#1e293b')
    ax.text(sx + sw/2, sy + sh/2 - 2, sdesc, fontsize=8, ha='center', va='center', color='#475569')

for i in range(len(steps)-1):
    x_start = steps[i][0] + steps[i][2]
    x_end = steps[i+1][0]
    ax.annotate("", xy=(x_end, 54), xytext=(x_start, 54), arrowprops=dict(arrowstyle="->", color="#0284c7", lw=2.5))

# Bottom Exception / Fallback Box
fbox = patches.FancyBboxPatch((15, 12), 112, 22, boxstyle="round,pad=0.8", ec="#cbd5e1", fc="#ffffff", lw=1.5)
ax.add_patch(fbox)
ax.text(18, 28, "Security & Error Handling Features:", fontsize=10, fontweight='bold', color='#0f172a')
ax.text(18, 23, "• Invalid / Expired Token: Instant alert buzzer & audio cue; front-desk notified immediately via TCP socket.", fontsize=8.5, color='#475569')
ax.text(18, 18, "• Overtime Protection: Screen automatically locks or flags overtime surcharge upon timer expiry unless extended.", fontsize=8.5, color='#475569')
ax.text(18, 13, "• Offline Fallback: Manual cashier entry available in Dashboard session panel in case of optical scanner failure.", fontsize=8.5, color='#475569')

plt.tight_layout()
plt.savefig('srs_assets/qr_token_workflow.png', dpi=300, bbox_inches='tight')
plt.close()
print("QR Token Workflow Diagram generated.")

# -------------------------------------------------------------
# 5. UI Screen Mockups / Screenshots for Document Illustration
# -------------------------------------------------------------
# Mockup 1: Login Frame
fig, ax = plt.subplots(figsize=(10, 6.5), dpi=300)
ax.set_facecolor('#0f172a')
fig.patch.set_facecolor('#ffffff')
ax.set_xlim(0, 100)
ax.set_ylim(0, 65)
ax.axis('off')

# Window Frame
win = patches.FancyBboxPatch((5, 5), 90, 55, boxstyle="round,pad=0.5", ec='#0284c7', fc='#1e293b', lw=2)
ax.add_patch(win)
# Header
ax.text(50, 54, "GAMING CAFE & ESPORTS CENTER - USER LOGIN", fontsize=12, fontweight='bold', ha='center', color='#38bdf8')
ax.text(50, 50, "Authorized Staff & Administrator Authentication Portal", fontsize=8.5, ha='center', color='#94a3b8')

# Input Boxes
# Username
ax.text(20, 42, "Username / NIC:", fontsize=9, fontweight='bold', color='#cbd5e1')
ubox = patches.Rectangle((20, 36), 60, 4.5, ec='#334155', fc='#0f172a', lw=1.5)
ax.add_patch(ubox)
ax.text(23, 38.5, "admin_dewmina", fontsize=8.5, color='#ffffff', va='center')

# Password
ax.text(20, 31, "Password:", fontsize=9, fontweight='bold', color='#cbd5e1')
pbox = patches.Rectangle((20, 25), 60, 4.5, ec='#334155', fc='#0f172a', lw=1.5)
ax.add_patch(pbox)
ax.text(23, 27.5, "••••••••••••", fontsize=11, color='#38bdf8', va='center')

# Buttons
lbtn = patches.FancyBboxPatch((20, 15), 28, 6, boxstyle="round,pad=0.3", ec='#0284c7', fc='#0284c7', lw=1)
ax.add_patch(lbtn)
ax.text(34, 18, "LOGIN ▶", fontsize=9.5, fontweight='bold', ha='center', color='#ffffff')

cbtn = patches.FancyBboxPatch((52, 15), 28, 6, boxstyle="round,pad=0.3", ec='#64748b', fc='#334155', lw=1)
ax.add_patch(cbtn)
ax.text(66, 18, "CANCEL / EXIT", fontsize=9.5, fontweight='bold', ha='center', color='#cbd5e1')

plt.tight_layout()
plt.savefig('srs_assets/ui_login_mockup.png', dpi=300, bbox_inches='tight')
plt.close()
print("UI Login Mockup generated.")

# Mockup 2: Main Dashboard Frame
fig, ax = plt.subplots(figsize=(12, 7.5), dpi=300)
ax.set_facecolor('#0b132b')
fig.patch.set_facecolor('#ffffff')
ax.set_xlim(0, 120)
ax.set_ylim(0, 75)
ax.axis('off')

# Sidebar
sbar = patches.Rectangle((2, 2), 22, 71, ec='#1f2d4a', fc='#070d1f', lw=1.5)
ax.add_patch(sbar)
ax.text(13, 68, "GAMING CAFE", fontsize=10, fontweight='bold', ha='center', color='#38bdf8')
menu_items = ["[1] Dashboard", "[2] Live Sessions", "[3] PC Manage", "[4] Maintenance", "[5] Reports", "[6] Admin Menu"]
for i, m in enumerate(menu_items):
    m_bg = '#1e3a8a' if i==0 else '#070d1f'
    ax.add_patch(patches.Rectangle((4, 58 - i*7), 18, 5, fc=m_bg, ec='#1f2d4a', lw=1))
    ax.text(5, 60.5 - i*7, m, fontsize=8, color='#ffffff' if i==0 else '#94a3b8', va='center')

# Header KPI summary cards
kpis = [
    ("TOTAL STATIONS", "12 Units", "#0284c7"),
    ("ACTIVE PLAYING", "4 Active", "#ef4444"),
    ("AVAILABLE PCS", "7 Ready", "#10b981"),
    ("UNDER REPAIR", "1 Repair", "#f59e0b")
]
for i, (k_title, k_val, k_col) in enumerate(kpis):
    kbox = patches.FancyBboxPatch((27 + i*22.5, 59), 21, 14, boxstyle="round,pad=0.4", ec=k_col, fc='#0f172a', lw=1.5)
    ax.add_patch(kbox)
    ax.text(28.5 + i*22.5, 69, k_title, fontsize=7.5, fontweight='bold', color=k_col)
    ax.text(28.5 + i*22.5, 64, k_val, fontsize=12, fontweight='bold', color='#ffffff')

# Live Grid Area (Left Center)
gbox = patches.FancyBboxPatch((27, 4), 62, 52, boxstyle="round,pad=0.5", ec='#0284c7', fc='#0f172a', lw=1.5)
ax.add_patch(gbox)
ax.text(30, 52, "GAMING STATIONS GRID & LIVE STATUS (PC & PS5)", fontsize=9, fontweight='bold', color='#38bdf8')

stations = [
    ("PC-01", "OCCUPIED", "#ef4444", "01:14:20 Remaining"),
    ("PC-02", "AVAILABLE", "#10b981", "Rs. 200/h - Ready"),
    ("PC-03", "OCCUPIED", "#ef4444", "00:45:10 Remaining"),
    ("PC-04", "AVAILABLE", "#10b981", "Rs. 200/h - Ready"),
    ("PC-05", "REPAIR", "#f59e0b", "GPU Artifact Issue"),
    ("PC-06", "AVAILABLE", "#10b981", "Rs. 200/h - Ready"),
    ("PS5-01", "OCCUPIED", "#ef4444", "01:40:00 Remaining"),
    ("PS5-02", "AVAILABLE", "#10b981", "Rs. 350/h - Ready")
]
for idx, (st_name, st_status, st_col, st_sub) in enumerate(stations):
    row = idx // 4
    col = idx % 4
    x = 30 + col * 14.5
    y = 38 - row * 15
    s_card = patches.FancyBboxPatch((x, y), 13.5, 12, boxstyle="round,pad=0.3", ec=st_col, fc='#1e293b', lw=1.2)
    ax.add_patch(s_card)
    ax.text(x+6.75, y+9.5, st_name, fontsize=8.5, fontweight='bold', ha='center', color='#ffffff')
    ax.text(x+6.75, y+6.5, st_status, fontsize=7, fontweight='bold', ha='center', color=st_col)
    ax.text(x+6.75, y+3, st_sub, fontsize=6, ha='center', color='#94a3b8')

# Session Details Right Panel (pnlSessionEnd)
pnl = patches.FancyBboxPatch((91, 4), 27, 52, boxstyle="round,pad=0.5", ec='#0284c7', fc='#070d1f', lw=1.5)
ax.add_patch(pnl)
ax.text(104.5, 52, "SESSION CONTROL", fontsize=9, fontweight='bold', ha='center', color='#38bdf8')
ax.text(93, 47, "Station: PC-01 [Occupied]", fontsize=7.5, color='#ffffff')
ax.text(93, 43, "Customer: Kasun Perera", fontsize=7.5, color='#cbd5e1')
ax.text(93, 39, "Phone: 0771234567", fontsize=7.5, color='#cbd5e1')
ax.text(93, 35, "Duration: 2 Hours (120m)", fontsize=7.5, color='#fde047')

# Timer Box
tbox = patches.Rectangle((93, 25), 23, 8, ec='#0284c7', fc='#0f172a', lw=1)
ax.add_patch(tbox)
ax.text(104.5, 30.5, "REMAINING TIME", fontsize=6.5, fontweight='bold', ha='center', color='#38bdf8')
ax.text(104.5, 27, "01:14:20", fontsize=11, fontweight='bold', ha='center', color='#10b981')

# Amount Box
abox = patches.Rectangle((93, 17), 23, 6.5, ec='#334155', fc='#0f172a', lw=1)
ax.add_patch(abox)
ax.text(104.5, 21.5, "TOTAL CHARGE", fontsize=6.5, fontweight='bold', ha='center', color='#94a3b8')
ax.text(104.5, 18.5, "Rs. 400.00", fontsize=9.5, fontweight='bold', ha='center', color='#ffffff')

# Buttons
ax.add_patch(patches.Rectangle((93, 11), 23, 4.5, ec='#0284c7', fc='#0284c7'))
ax.text(104.5, 13.2, "+ ADD TIME", fontsize=7.5, fontweight='bold', ha='center', color='#ffffff')
ax.add_patch(patches.Rectangle((93, 5.5), 23, 4.5, ec='#ef4444', fc='#ef4444'))
ax.text(104.5, 7.7, "END SESSION & BILL", fontsize=7.5, fontweight='bold', ha='center', color='#ffffff')

plt.tight_layout()
plt.savefig('srs_assets/ui_dash_mockup.png', dpi=300, bbox_inches='tight')
plt.close()
print("UI Dash Mockup generated.")

# Mockup 3: BillFrame Invoice
fig, ax = plt.subplots(figsize=(9, 7.5), dpi=300)
ax.set_facecolor('#0f172a')
fig.patch.set_facecolor('#ffffff')
ax.set_xlim(0, 90)
ax.set_ylim(0, 75)
ax.axis('off')

# Invoice Card
icard = patches.FancyBboxPatch((5, 4), 80, 67, boxstyle="round,pad=0.5", ec='#0284c7', fc='#1e293b', lw=2)
ax.add_patch(icard)

ax.text(45, 66, "GAMING CAFE OFFICIAL INVOICE & RECEIPT", fontsize=11.5, fontweight='bold', ha='center', color='#38bdf8')
ax.text(45, 63, "Invoice No: INV-20260927-0104 | Date: 2026-09-27 16:30", fontsize=8, ha='center', color='#94a3b8')
ax.plot([8, 82], [61, 61], color='#334155', lw=1.2)

# Details Grid
details = [
    ("Station ID & Name:", "PC-01 (RTX 4070 Station)", "Customer Name:", "Kasun Perera"),
    ("Station Category:", "PC Gaming [High-Tier]", "Customer Phone:", "0771234567"),
    ("Session Start:", "14:15:00", "Session End:", "16:15:00"),
    ("Total Duration:", "2 Hours (120 Minutes)", "Hourly Rate:", "Rs. 200.00 / Hour")
]
for r_idx, (l1, v1, l2, v2) in enumerate(details):
    y = 57 - r_idx * 4.5
    ax.text(8, y, l1, fontsize=7.5, color='#94a3b8')
    ax.text(26, y, v1, fontsize=8, fontweight='bold', color='#ffffff')
    ax.text(48, y, l2, fontsize=7.5, color='#94a3b8')
    ax.text(66, y, v2, fontsize=8, fontweight='bold', color='#ffffff')

ax.plot([8, 82], [38, 38], color='#334155', lw=1.2)

# Financial Breakdown Box
ax.text(12, 33, "Subtotal Amount:", fontsize=8.5, color='#cbd5e1')
ax.text(78, 33, "Rs. 400.00", fontsize=9, fontweight='bold', ha='right', color='#ffffff')
ax.text(12, 29, "Special Discount (0%):", fontsize=8.5, color='#cbd5e1')
ax.text(78, 29, "Rs. 0.00", fontsize=9, fontweight='bold', ha='right', color='#10b981')
ax.text(12, 24, "NET PAYABLE TOTAL:", fontsize=10, fontweight='bold', color='#38bdf8')
ax.text(78, 24, "Rs. 400.00", fontsize=12, fontweight='bold', ha='right', color='#38bdf8')

ax.text(12, 19, "Payment Method: Cash | Received: Rs. 500.00 | Change Due: Rs. 100.00", fontsize=8, color='#fde047')

# Buttons
ax.add_patch(patches.FancyBboxPatch((8, 8), 35, 6, boxstyle="round,pad=0.3", ec='#059669', fc='#059669'))
ax.text(25.5, 11, "PRINT / SAVE PDF BILL", fontsize=8.5, fontweight='bold', ha='center', color='#ffffff')

ax.add_patch(patches.FancyBboxPatch((47, 8), 35, 6, boxstyle="round,pad=0.3", ec='#334155', fc='#334155'))
ax.text(64.5, 11, "CLOSE INVOICE WINDOW", fontsize=8.5, fontweight='bold', ha='center', color='#ffffff')

plt.tight_layout()
plt.savefig('srs_assets/ui_bill_mockup.png', dpi=300, bbox_inches='tight')
plt.close()
print("UI Bill Mockup generated.")

# Mockup 4: Multi-Dimensional Business Reports Dialog
fig, ax = plt.subplots(figsize=(9, 6.5), dpi=300)
ax.set_facecolor('#0f172a')
fig.patch.set_facecolor('#ffffff')
ax.set_xlim(0, 90)
ax.set_ylim(0, 65)
ax.axis('off')

# Dialog Box
dbox = patches.FancyBboxPatch((5, 5), 80, 55, boxstyle="round,pad=0.5", ec='#ca8a04', fc='#1e293b', lw=2)
ax.add_patch(dbox)

ax.text(45, 54, "EXPORT CAFE BUSINESS & AUDIT REPORTS (PDF)", fontsize=11, fontweight='bold', ha='center', color='#fde047')
ax.text(45, 50, "Select a report category to compile and generate via Jasper / iText Engine", fontsize=8, ha='center', color='#94a3b8')

rep_options = [
    ("1. User Activity Logs & Staff Attendance Audit Report", "Logins, logouts, session durations"),
    ("2. PC & Console Session History & Usage Utilization Report", "All gaming records, players, durations"),
    ("3. Invoices, Revenue Billing & Cashier Collection Report", "Total cafe income, discounts, cash/card"),
    ("4. Station Hardware Inventory & Specs Report", "CPU, GPU, RAM, Motherboard, rates"),
    ("5. Hardware Maintenance, Defect & Repair History Report", "Issues fixed, technician costs")
]
for i, (rtitle, rdesc) in enumerate(rep_options):
    r_card = patches.FancyBboxPatch((10, 39 - i*6.5), 70, 5.5, boxstyle="round,pad=0.2", ec='#334155', fc='#0f172a', lw=1)
    ax.add_patch(r_card)
    ax.text(12, 42.5 - i*6.5, rtitle, fontsize=8, fontweight='bold', color='#38bdf8')
    ax.text(12, 40.2 - i*6.5, rdesc, fontsize=7, color='#64748b')

plt.tight_layout()
plt.savefig('srs_assets/ui_reports_mockup.png', dpi=300, bbox_inches='tight')
plt.close()
print("UI Reports Mockup generated.")

print("All diagrams and mockups successfully created in srs_assets/!")
