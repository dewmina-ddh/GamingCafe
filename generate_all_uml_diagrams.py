import os
import matplotlib
matplotlib.use('Agg')
import matplotlib.pyplot as plt
import matplotlib.patches as patches

os.makedirs('srs_assets', exist_ok=True)

# -------------------------------------------------------------
# 1. Comprehensive Use Case Diagram
# -------------------------------------------------------------
fig, ax = plt.subplots(figsize=(15, 11), dpi=300)
ax.set_facecolor('#f8fafc')
fig.patch.set_facecolor('#ffffff')
ax.set_xlim(0, 150)
ax.set_ylim(0, 110)
ax.axis('off')

ax.text(75, 105, "UML USE CASE DIAGRAM - GAMING CAFE MANAGEMENT SYSTEM", fontsize=14, fontweight='bold', ha='center', color='#0f172a')
ax.text(75, 102, "Standard IEEE 830-1998 / ISO/IEC/IEEE 29148 Actor & Functional Boundary Specification", fontsize=9.5, fontstyle='italic', ha='center', color='#475569')

# Boundary
boundary = patches.FancyBboxPatch((30, 6), 90, 92, boxstyle="round,pad=1", ec="#0284c7", fc="#f0f9ff", lw=2, linestyle='--')
ax.add_patch(boundary)
ax.text(75, 94, "«Gaming Cafe Management System Boundary»", fontsize=11, fontweight='bold', ha='center', color='#0369a1')

def draw_actor(ax, x, y, label, role_color="#1e293b"):
    circle = patches.Circle((x, y+4), 2.2, ec=role_color, fc='#ffffff', lw=2)
    ax.add_patch(circle)
    ax.plot([x, x], [y+1.8, y-3], color=role_color, lw=2)
    ax.plot([x-3.2, x+3.2], [y, y], color=role_color, lw=2)
    ax.plot([x, x-3], [y-3, y-7.5], color=role_color, lw=2)
    ax.plot([x, x+3], [y-3, y-7.5], color=role_color, lw=2)
    ax.text(x, y-10.5, label, fontsize=9.5, fontweight='bold', ha='center', color=role_color)

draw_actor(ax, 15, 75, "System Administrator\n(Owner / IT Manager)", "#b45309")
draw_actor(ax, 15, 30, "Cafe Staff / Cashier\n(Front-Desk Operator)", "#0369a1")
draw_actor(ax, 135, 52, "Gamer / Customer\n(End-User Player)", "#059669")

use_cases = [
    (75, 86, "UC-01: Authenticate & Role-Based UI Access", "#e0f2fe", "#0284c7", ["admin", "staff"]),
    (75, 78, "UC-02: User Administration & Password Flag Reset", "#fef3c7", "#d97706", ["admin"]),
    (75, 70, "UC-03: Live Gaming Stations Telemetry & Grid", "#e0f2fe", "#0284c7", ["admin", "staff"]),
    (75, 62, "UC-04: Start Gaming Session & Duration (Min 1h)", "#e0f2fe", "#0284c7", ["admin", "staff", "customer"]),
    (75, 54, "UC-05: Real-time Countdown & Dynamic '+ Add Time'", "#e0f2fe", "#0284c7", ["admin", "staff", "customer"]),
    (75, 46, "UC-06: End Session & Generate Dedicated BillFrame", "#e0f2fe", "#0284c7", ["admin", "staff", "customer"]),
    (75, 38, "UC-07: Station Hardware Specs & Inventory Config", "#fef3c7", "#d97706", ["admin"]),
    (75, 30, "UC-08: Log Hardware Defect & Track Repair History", "#e0f2fe", "#0284c7", ["admin", "staff"]),
    (75, 22, "UC-09: Export Multi-Category PDF Audit Reports", "#fef3c7", "#d97706", ["admin"]),
    (75, 14, "UC-10: Token / QR Scanner Instant Station Check-in", "#d1fae5", "#059669", ["admin", "staff", "customer"])
]

for ux, uy, utext, ufc, uec, actors in use_cases:
    ellipse = patches.Ellipse((ux, uy), 46, 6.2, ec=uec, fc=ufc, lw=1.5)
    ax.add_patch(ellipse)
    ax.text(ux, uy, utext, fontsize=8.5, fontweight='bold', ha='center', va='center', color='#1e293b')
    if "admin" in actors:
        ax.annotate("", xy=(ux-23, uy), xytext=(17, 75), arrowprops=dict(arrowstyle="-", color="#b45309", lw=1.2, alpha=0.7))
    if "staff" in actors:
        ax.annotate("", xy=(ux-23, uy), xytext=(17, 30), arrowprops=dict(arrowstyle="-", color="#0284c7", lw=1.2, alpha=0.7))
    if "customer" in actors:
        ax.annotate("", xy=(ux+23, uy), xytext=(133, 52), arrowprops=dict(arrowstyle="-", color="#059669", lw=1.2, linestyle="--", alpha=0.7))

plt.tight_layout()
plt.savefig('srs_assets/uml_use_case.png', dpi=300, bbox_inches='tight')
plt.close()
print("1. UML Use Case Diagram generated.")

# -------------------------------------------------------------
# 2. UML Class Diagram (Java OOP Domain Architecture)
# -------------------------------------------------------------
fig, ax = plt.subplots(figsize=(16, 12), dpi=300)
ax.set_facecolor('#f8fafc')
fig.patch.set_facecolor('#ffffff')
ax.set_xlim(0, 160)
ax.set_ylim(0, 120)
ax.axis('off')

ax.text(80, 116, "UML CLASS DIAGRAM - OBJECT-ORIENTED SYSTEM DOMAIN MODEL", fontsize=15, fontweight='bold', ha='center', color='#0f172a')
ax.text(80, 113, "Java Swing Controllers, Domain Entities, Data Access Objects (DAO) & Persistence Classes", fontsize=10, fontstyle='italic', ha='center', color='#475569')

def draw_class_box(ax, x, y, w, h, class_name, stereotype, attributes, methods, header_bg="#003366"):
    outer = patches.Rectangle((x, y-h), w, h, ec='#94a3b8', fc='#ffffff', lw=1.5, zorder=2)
    ax.add_patch(outer)
    hdr_h = 5.5
    hdr = patches.Rectangle((x, y-hdr_h), w, hdr_h, ec=header_bg, fc=header_bg, lw=1.5, zorder=3)
    ax.add_patch(hdr)
    if stereotype:
        ax.text(x+w/2, y-2, f"«{stereotype}»", fontsize=7.5, fontstyle='italic', color='#93c5fd', ha='center', va='center', zorder=4)
        ax.text(x+w/2, y-4, class_name, fontsize=9, fontweight='bold', color='#ffffff', ha='center', va='center', zorder=4)
    else:
        ax.text(x+w/2, y-2.7, class_name, fontsize=9.5, fontweight='bold', color='#ffffff', ha='center', va='center', zorder=4)
    
    # Attributes
    cur_y = y - hdr_h - 2
    for attr in attributes:
        ax.text(x+1.5, cur_y, attr, fontsize=7.5, color='#1e293b', va='center', zorder=4)
        cur_y -= 2.2
    
    # Separator
    ax.plot([x, x+w], [cur_y, cur_y], color='#cbd5e1', lw=1, zorder=3)
    cur_y -= 2
    
    # Methods
    for m in methods:
        ax.text(x+1.5, cur_y, m, fontsize=7.5, color='#0369a1', va='center', zorder=4)
        cur_y -= 2.2

classes = [
    (8, 108, 42, 34, "User", "Entity", [
        "- id: int", "- emp_no: String", "- f_name: String", "- l_name: String",
        "- nic: String", "- email: String", "- phone: String", "- username: String",
        "- password: String (SHA-256)", "- role: String", "- status: int", "- changePass: int"
    ], [
        "+ getRole(): String", "+ getfName(): String", "+ authenticate(): boolean",
        "+ updatePassword(pwd: String): void"
    ], "#003366"),

    (58, 108, 44, 34, "GamingStation (PC)", "Entity", [
        "- pc_id: String [PK]", "- pc_name: String", "- category: String",
        "- cpu: String", "- motherboard: String", "- ram_capacity: String",
        "- vga: String", "- ip_address: String", "- hourly_rate: double",
        "- status: String (Available/Occupied/Repair)"
    ], [
        "+ setStatus(s: String): void", "+ getHourlyRate(): double",
        "+ isAvailable(): boolean", "+ markInRepair(reason: String): void"
    ], "#0284c7"),

    (110, 108, 44, 36, "GamingSession", "Entity", [
        "- session_id: String [PK]", "- pc_id: String [FK]", "- cus_name: String",
        "- phone: String", "- start_time: Timestamp", "- end_time: Timestamp",
        "- add_minutes: int", "- price_per_adding: double", "- total_minutes: int",
        "- total_amount: double", "- status: String"
    ], [
        "+ addExtraTime(mins: int): void", "+ calculateCurrentCharge(): double",
        "+ getRemainingSeconds(): long", "+ endSession(): void"
    ], "#059669"),

    (8, 66, 42, 28, "UserLogs", "Entity", [
        "- log_id: int", "- emp_no: String", "- username: String",
        "- role: String", "- login_time: Timestamp",
        "- logout_time: Timestamp", "- duration: String"
    ], [
        "+ recordLogin(): void", "+ recordLogout(): void",
        "+ computeShiftDuration(): String"
    ], "#334155"),

    (58, 66, 44, 28, "PCMaintenance", "Entity", [
        "- repair_id: int", "- pc_id: String [FK]",
        "- issue_description: String", "- cost: double",
        "- repair_date: Timestamp"
    ], [
        "+ createRepairTicket(): void", "+ resolveRepair(c: double): void"
    ], "#d97706"),

    (110, 66, 44, 30, "Invoice", "Entity", [
        "- invoice_no: String [PK]", "- session_id: String [FK]",
        "- emp_no: String [FK]", "- sub_total: double",
        "- discount: double", "- net_total: double",
        "- payment_method: String", "- invoice_date: Timestamp"
    ], [
        "+ computeNetTotal(): double", "+ exportPdfInvoice(): File",
        "+ printReceipt(): void"
    ], "#047857"),

    (8, 30, 42, 24, "DBConnection (db)", "Utility / Singleton", [
        "- con: Connection", "- url: String", "- user: String", "- pass: String"
    ], [
        "+ getConnection(): Connection", "+ closeConnection(): void",
        "+ testConnection(): boolean"
    ], "#475569"),

    (58, 30, 44, 26, "Dash (Main Controller)", "JFrame / Controller", [
        "- currentSelectedPcId: String", "- sessionTimer: Timer",
        "- liveSessionEntries: List"
    ], [
        "+ selectPcForSession(): void", "+ startNewSession(): void",
        "+ addTimeToActiveSession(): void", "+ showReportDialog(): void"
    ], "#1e293b"),

    (110, 30, 44, 26, "BillFrame", "JFrame / View", [
        "- invoiceNo: String", "- currentSession: GamingSession"
    ], [
        "+ calculateBalanceChange(): void", "+ exportPdfReceipt(): void",
        "+ disposeAndReturn(): void"
    ], "#0f766e")
]

for x, y, w, h, cname, stereo, attrs, meths, hbg in classes:
    draw_class_box(ax, x, y, w, h, cname, stereo, attrs, meths, hbg)

# Association Arrows
# User -> UserLogs (1 to 0..*)
ax.annotate("", xy=(29, 66), xytext=(29, 74), arrowprops=dict(arrowstyle="->", color="#334155", lw=1.5))
ax.text(31, 70, "1 .. *", fontsize=8, fontweight='bold', color='#334155')

# GamingStation -> GamingSession (1 to 0..*)
ax.annotate("", xy=(110, 90), xytext=(102, 90), arrowprops=dict(arrowstyle="->", color="#0284c7", lw=1.5))
ax.text(104, 91.5, "1 .. *", fontsize=8, fontweight='bold', color='#0284c7')

# GamingStation -> PCMaintenance (1 to 0..*)
ax.annotate("", xy=(80, 66), xytext=(80, 74), arrowprops=dict(arrowstyle="->", color="#d97706", lw=1.5))
ax.text(82, 70, "1 .. *", fontsize=8, fontweight='bold', color='#d97706')

# GamingSession -> Invoice (1 to 1)
ax.annotate("", xy=(132, 66), xytext=(132, 72), arrowprops=dict(arrowstyle="->", color="#059669", lw=1.5))
ax.text(134, 69, "1 .. 1", fontsize=8, fontweight='bold', color='#059669')

# Dash -> BillFrame (creates)
ax.annotate("", xy=(110, 17), xytext=(102, 17), arrowprops=dict(arrowstyle="->", color="#0f766e", lw=1.5, linestyle="--"))
ax.text(104, 18.5, "«instantiates»", fontsize=7.5, fontstyle='italic', color='#0f766e')

plt.tight_layout()
plt.savefig('srs_assets/uml_class_diagram.png', dpi=300, bbox_inches='tight')
plt.close()
print("2. UML Class Diagram generated.")

# -------------------------------------------------------------
# 3. UML Sequence Diagram (Session Booking, Time Extension & Billing)
# -------------------------------------------------------------
fig, ax = plt.subplots(figsize=(15, 10), dpi=300)
ax.set_facecolor('#f8fafc')
fig.patch.set_facecolor('#ffffff')
ax.set_xlim(0, 150)
ax.set_ylim(0, 100)
ax.axis('off')

ax.text(75, 96, "UML SEQUENCE DIAGRAM - GAMING SESSION LIFECYCLE & INVOICING", fontsize=14, fontweight='bold', ha='center', color='#0f172a')
ax.text(75, 93, "Object Interaction: Cashier, Dash Controller, SessionTimer, DBConnection (MySQL), BillFrame & PDF Engine", fontsize=9.5, fontstyle='italic', ha='center', color='#475569')

participants = [
    (15, "Cashier / Staff", "#0369a1"),
    (45, "Dash (Controller)", "#1e293b"),
    (75, "SessionTimer", "#ca8a04"),
    (105, "DBConnection (MySQL)", "#059669"),
    (135, "BillFrame / PDF Engine", "#0f766e")
]

for px, pname, pcolor in participants:
    pbox = patches.FancyBboxPatch((px-11, 84), 22, 6, boxstyle="round,pad=0.4", ec=pcolor, fc=pcolor)
    ax.add_patch(pbox)
    ax.text(px, 87, pname, fontsize=8.5, fontweight='bold', ha='center', va='center', color='#ffffff')
    ax.plot([px, px], [84, 8], color='#cbd5e1', lw=1.5, linestyle='--')

seq_events = [
    (15, 45, 78, "1. selectStation(PC-01, 2 Hours)", True, "#0284c7"),
    (45, 45, 73, "2. updateEstimatedPrice(Rs. 400.00)", False, "#1e293b"),
    (15, 45, 68, "3. clickStartSession()", True, "#0284c7"),
    (45, 105, 63, "4. INSERT INTO gaming_sessions (PC-01, Ongoing)", True, "#059669"),
    (105, 45, 58, "5. return success (session_id = SES-001)", False, "#059669"),
    (45, 75, 53, "6. startCountdownTimer(120 mins)", True, "#ca8a04"),
    (75, 45, 48, "7. tick(01:59:59 ... Remaining)", False, "#ca8a04"),
    (15, 45, 43, "8. clickAddTime(+1 Hour / 60 mins)", True, "#0284c7"),
    (45, 105, 38, "9. UPDATE gaming_sessions SET add_minutes += 60", True, "#059669"),
    (45, 75, 33, "10. extendTargetEndTime(+3600s)", True, "#ca8a04"),
    (15, 45, 28, "11. clickEndSession()", True, "#ef4444"),
    (45, 105, 23, "12. UPDATE status='Completed', end_time=NOW()", True, "#059669"),
    (45, 135, 18, "13. new BillFrame(SES-001) & exportPdfReceipt()", True, "#0f766e"),
    (135, 15, 13, "14. display Invoice Window & Print PDF Bill", False, "#0f766e")
]

for src, dst, y, msg, is_call, col in seq_events:
    ls = "-" if is_call else "--"
    ax.annotate("", xy=(dst, y), xytext=(src, y), arrowprops=dict(arrowstyle="->", color=col, lw=1.5, linestyle=ls))
    mid_x = (src + dst) / 2
    ax.text(mid_x, y + 1.2, msg, fontsize=7.5, fontweight='bold', ha='center', color=col)

plt.tight_layout()
plt.savefig('srs_assets/uml_sequence_diagram.png', dpi=300, bbox_inches='tight')
plt.close()
print("3. UML Sequence Diagram generated.")

# -------------------------------------------------------------
# 4. UML Activity Diagram (Gaming Session Operational Flowchart)
# -------------------------------------------------------------
fig, ax = plt.subplots(figsize=(14, 11), dpi=300)
ax.set_facecolor('#f8fafc')
fig.patch.set_facecolor('#ffffff')
ax.set_xlim(0, 140)
ax.set_ylim(0, 110)
ax.axis('off')

ax.text(70, 105, "UML ACTIVITY DIAGRAM - SESSION DISPATCH & BILLING WORKFLOW", fontsize=14, fontweight='bold', ha='center', color='#0f172a')
ax.text(70, 102, "Operational Control Flow with Decision Diamonds, Dynamic Extension Loop & Overtime Logic", fontsize=9.5, fontstyle='italic', ha='center', color='#475569')

# Initial Node
circle_start = patches.Circle((70, 96), 2, ec='#0f172a', fc='#0f172a')
ax.add_patch(circle_start)
ax.annotate("", xy=(70, 90), xytext=(70, 94), arrowprops=dict(arrowstyle="->", color="#0f172a", lw=2))

# Activities
def draw_act(ax, x, y, w, h, text, bg="#e0f2fe", ec="#0284c7"):
    box = patches.FancyBboxPatch((x-w/2, y-h/2), w, h, boxstyle="round,pad=0.5", ec=ec, fc=bg, lw=1.5)
    ax.add_patch(box)
    ax.text(x, y, text, fontsize=8, fontweight='bold', ha='center', va='center', color='#1e293b')

def draw_decision(ax, x, y, size=3.5, label=""):
    diamond = patches.RegularPolygon((x, y), numVertices=4, radius=size, orientation=0, ec='#d97706', fc='#fef3c7', lw=1.5)
    ax.add_patch(diamond)
    if label:
        ax.text(x, y-5, label, fontsize=7.5, fontstyle='italic', ha='center', color='#b45309')

draw_act(ax, 70, 87, 48, 5.5, "1. Staff Selects Station (PC/PS5) & Enters Customer")
ax.annotate("", xy=(70, 79.5), xytext=(70, 84.2), arrowprops=dict(arrowstyle="->", color="#0f172a", lw=1.5))

draw_act(ax, 70, 77, 48, 5.5, "2. Select Duration Preset (Min 1h) -> Auto Price Update")
ax.annotate("", xy=(70, 69.5), xytext=(70, 74.2), arrowprops=dict(arrowstyle="->", color="#0f172a", lw=1.5))

draw_act(ax, 70, 67, 48, 5.5, "3. Click '▶ Start Session' -> Set Status 'Occupied'")
ax.annotate("", xy=(70, 59.5), xytext=(70, 64.2), arrowprops=dict(arrowstyle="->", color="#0f172a", lw=1.5))

draw_act(ax, 70, 57, 48, 5.5, "4. Real-time Countdown Timer Ticks (Per Second)")
ax.annotate("", xy=(70, 50.5), xytext=(70, 54.2), arrowprops=dict(arrowstyle="->", color="#0f172a", lw=1.5))

# Decision: Add Time Requested?
draw_decision(ax, 70, 47, label="Customer requests +Add Time?")
# Yes branch (Left loop back to countdown)
ax.annotate("", xy=(35, 47), xytext=(66.5, 47), arrowprops=dict(arrowstyle="->", color="#d97706", lw=1.5))
ax.text(50, 48.5, "[Yes: +30m/+1h/+2h]", fontsize=7, color='#b45309', ha='center')
draw_act(ax, 28, 47, 24, 5.5, "Execute Atomic\nAdd-Time Update", bg="#fef3c7", ec="#d97706")
ax.annotate("", xy=(46, 57), xytext=(28, 49.8), arrowprops=dict(arrowstyle="->", color="#d97706", lw=1.5))

# No branch (Down to overtime check)
ax.annotate("", xy=(70, 39), xytext=(70, 43.5), arrowprops=dict(arrowstyle="->", color="#0f172a", lw=1.5))
ax.text(76, 41.5, "[No / Ongoing]", fontsize=7, color='#0f172a')

# Decision: Overtime Exceeded?
draw_decision(ax, 70, 35.5, label="Time expired?")
ax.annotate("", xy=(108, 35.5), xytext=(73.5, 35.5), arrowprops=dict(arrowstyle="->", color="#ef4444", lw=1.5))
ax.text(90, 37, "[Yes: Overtime]", fontsize=7, color='#ef4444', ha='center')
draw_act(ax, 118, 35.5, 20, 5.5, "Flag Red Alert &\nOvertime Surcharge", bg="#fee2e2", ec="#ef4444")
ax.annotate("", xy=(94, 25), xytext=(118, 32.7), arrowprops=dict(arrowstyle="->", color="#ef4444", lw=1.5))

ax.annotate("", xy=(70, 27.5), xytext=(70, 32), arrowprops=dict(arrowstyle="->", color="#0f172a", lw=1.5))
ax.text(76, 30, "[End Session]", fontsize=7, color='#0f172a')

draw_act(ax, 70, 25, 48, 5.5, "5. Click 'End Session & Bill' -> Open Dedicated BillFrame")
ax.annotate("", xy=(70, 17.5), xytext=(70, 22.2), arrowprops=dict(arrowstyle="->", color="#0f172a", lw=1.5))

draw_act(ax, 70, 15, 48, 5.5, "6. Collect Cash/Card, Compute Change & Export PDF Bill")
ax.annotate("", xy=(70, 8.5), xytext=(70, 12.2), arrowprops=dict(arrowstyle="->", color="#0f172a", lw=1.5))

# Final Activity Node
circle_outer = patches.Circle((70, 6), 2.5, ec='#0f172a', fc='#ffffff', lw=1.5)
circle_inner = patches.Circle((70, 6), 1.5, ec='#0f172a', fc='#0f172a')
ax.add_patch(circle_outer)
ax.add_patch(circle_inner)

plt.tight_layout()
plt.savefig('srs_assets/uml_activity_diagram.png', dpi=300, bbox_inches='tight')
plt.close()
print("4. UML Activity Diagram generated.")

# -------------------------------------------------------------
# 5. UML State Machine Diagram (Gaming Station Finite State Model)
# -------------------------------------------------------------
fig, ax = plt.subplots(figsize=(14, 8), dpi=300)
ax.set_facecolor('#f8fafc')
fig.patch.set_facecolor('#ffffff')
ax.set_xlim(0, 140)
ax.set_ylim(0, 80)
ax.axis('off')

ax.text(70, 75, "UML STATE MACHINE DIAGRAM - GAMING STATION LIFECYCLE STATES", fontsize=14, fontweight='bold', ha='center', color='#0f172a')
ax.text(70, 71, "Finite State Transitions: Available (Green) <-> Occupied (Red) <-> Repair (Orange)", fontsize=9.5, fontstyle='italic', ha='center', color='#475569')

# Initial Node
s_init = patches.Circle((15, 45), 2, ec='#0f172a', fc='#0f172a')
ax.add_patch(s_init)
ax.annotate("", xy=(30, 45), xytext=(17, 45), arrowprops=dict(arrowstyle="->", color="#0f172a", lw=1.8))
ax.text(23, 47, "pc_table insert", fontsize=7.5, fontstyle='italic', ha='center')

# State 1: Available
s1 = patches.FancyBboxPatch((30, 35), 28, 20, boxstyle="round,pad=0.8", ec="#059669", fc="#ecfdf5", lw=2)
ax.add_patch(s1)
ax.text(44, 51, "AVAILABLE", fontsize=11, fontweight='bold', ha='center', color="#047857")
ax.text(44, 46, "entry / unlockInputs()", fontsize=8, color="#065f46", ha='center')
ax.text(44, 42, "do / renderGreenBadge()", fontsize=8, color="#065f46", ha='center')
ax.text(44, 38, "exit / lockInputs()", fontsize=8, color="#065f46", ha='center')

# State 2: Occupied
s2 = patches.FancyBboxPatch((85, 35), 28, 20, boxstyle="round,pad=0.8", ec="#ef4444", fc="#fef2f2", lw=2)
ax.add_patch(s2)
ax.text(99, 51, "OCCUPIED", fontsize=11, fontweight='bold', ha='center', color="#b91c1c")
ax.text(99, 46, "entry / startCountdownTimer()", fontsize=8, color="#991b1b", ha='center')
ax.text(99, 42, "do / updateLiveTelemetry()", fontsize=8, color="#991b1b", ha='center')
ax.text(99, 38, "exit / stopTimer(), openBill()", fontsize=8, color="#991b1b", ha='center')

# State 3: Repair
s3 = patches.FancyBboxPatch((58, 4), 28, 18, boxstyle="round,pad=0.8", ec="#d97706", fc="#fffbeb", lw=2)
ax.add_patch(s3)
ax.text(72, 18, "UNDER REPAIR", fontsize=10.5, fontweight='bold', ha='center', color="#b45309")
ax.text(72, 14, "entry / blockSessionStart()", fontsize=8, color="#92400e", ha='center')
ax.text(72, 10, "do / logMaintenanceCost()", fontsize=8, color="#92400e", ha='center')
ax.text(72, 6, "exit / restoreToAvailable()", fontsize=8, color="#92400e", ha='center')

# Transitions
# Available -> Occupied
ax.annotate("", xy=(85, 48), xytext=(58, 48), arrowprops=dict(arrowstyle="->", color="#0f172a", lw=1.8))
ax.text(71.5, 50, "startSession(duration)", fontsize=8, fontweight='bold', ha='center', color="#0f172a")

# Occupied -> Available
ax.annotate("", xy=(58, 41), xytext=(85, 41), arrowprops=dict(arrowstyle="->", color="#0f172a", lw=1.8))
ax.text(71.5, 37.5, "endSession() / billSettled", fontsize=8, fontweight='bold', ha='center', color="#0f172a")

# Available -> Repair
ax.annotate("", xy=(60, 22), xytext=(44, 35), arrowprops=dict(arrowstyle="->", color="#d97706", lw=1.5))
ax.text(45, 25, "markInRepair()", fontsize=7.5, color="#b45309", ha='center')

# Repair -> Available
ax.annotate("", xy=(50, 35), xytext=(68, 22), arrowprops=dict(arrowstyle="->", color="#059669", lw=1.5))
ax.text(64, 28, "repairResolved()", fontsize=7.5, color="#059669", ha='center')

# Occupied self-loop (+Add Time)
ax.annotate("", xy=(99, 55), xytext=(99, 59), arrowprops=dict(arrowstyle="->", color="#0284c7", lw=1.5))
ax.text(99, 61, "addExtraTime(+mins)", fontsize=7.5, fontweight='bold', ha='center', color="#0284c7")

plt.tight_layout()
plt.savefig('srs_assets/uml_state_machine.png', dpi=300, bbox_inches='tight')
plt.close()
print("5. UML State Machine Diagram generated.")

# -------------------------------------------------------------
# 6. Database Schema Diagram (Exact Match to User Uploaded Schema)
# -------------------------------------------------------------
fig, ax = plt.subplots(figsize=(16, 12), dpi=300)
ax.set_facecolor('#f8fafc')
fig.patch.set_facecolor('#ffffff')
ax.set_xlim(0, 160)
ax.set_ylim(0, 120)
ax.axis('off')

ax.text(80, 116, "EXACT RELATIONAL DATABASE SCHEMA & ENTITY RELATIONSHIPS", fontsize=15, fontweight='bold', ha='center', color='#0f172a')
ax.text(80, 113, "MySQL Database: 'gaming_cafe' Normalized Schema as Deployed in Production Environment", fontsize=10, fontstyle='italic', ha='center', color='#475569')

def draw_schema_table(ax, x, y, w, h, table_name, columns, header_bg="#0284c7"):
    outer = patches.Rectangle((x, y-h), w, h, ec='#94a3b8', fc='#ffffff', lw=1.5, zorder=2)
    ax.add_patch(outer)
    hdr = patches.Rectangle((x, y-4.8), w, 4.8, ec=header_bg, fc=header_bg, lw=1.5, zorder=3)
    ax.add_patch(hdr)
    ax.text(x+2, y-2.5, "v", fontsize=8.5, fontweight='bold', color='#ffffff', va='center', zorder=4)
    ax.text(x+5, y-2.5, "gaming_cafe  " + table_name, fontsize=8.5, fontweight='bold', color='#ffffff', va='center', zorder=4)
    
    cur_y = y - 7
    for col_name, col_type, is_pk, is_fk in columns:
        bg_col = "#e2e8f0" if is_pk else "#ffffff"
        icon = "[K] " if is_pk else ("[#] " if "int" in col_type or "decimal" in col_type else "[@] ")
        ax.text(x+1.5, cur_y, f"{icon}{col_name} : {col_type}", fontsize=7.5, color='#0f172a' if is_pk else '#334155', va='center', zorder=4)
        cur_y -= 2.5

schema_tables = [
    (10, 108, 42, 30, "invoices", [
        ("invoice_no", "varchar(20)", True, False),
        ("session_id", "varchar(20)", False, True),
        ("emp_no", "varchar(20)", False, True),
        ("sub_total", "decimal(10,2)", False, False),
        ("discount", "decimal(10,2)", False, False),
        ("net_total", "decimal(10,2)", False, False),
        ("payment_method", "varchar(20)", False, False),
        ("invoice_date", "datetime", False, False)
    ], "#0284c7"),

    (60, 108, 44, 38, "gaming_sessions", [
        ("session_id", "varchar(20)", True, False),
        ("pc_id", "varchar(20)", False, True),
        ("cus_name", "varchar(100)", False, False),
        ("phone", "varchar(20)", False, False),
        ("start_time", "datetime", False, False),
        ("end_time", "datetime", False, False),
        ("add_minutes", "int(11)", False, False),
        ("price_per_adding", "decimal(10,2)", False, False),
        ("total_minutes", "int(11)", False, False),
        ("total_amount", "decimal(10,2)", False, False),
        ("status", "varchar(20)", False, False)
    ], "#0284c7"),

    (115, 108, 38, 28, "user_logs", [
        ("log_id", "int(11)", True, False),
        ("emp_no", "varchar(50)", False, True),
        ("username", "varchar(100)", False, False),
        ("role", "varchar(50)", False, False),
        ("login_time", "datetime", False, False),
        ("logout_time", "datetime", False, False),
        ("duration", "varchar(50)", False, False),
        ("status", "varchar(50)", False, False)
    ], "#0284c7"),

    (25, 66, 38, 44, "user", [
        ("id", "int(3)", True, False),
        ("emp_no", "varchar(20)", False, False),
        ("f_name", "varchar(100)", False, False),
        ("l_name", "varchar(100)", False, False),
        ("nic", "varchar(12)", False, False),
        ("email", "varchar(100)", False, False),
        ("phone", "varchar(10)", False, False),
        ("username", "varchar(50)", False, False),
        ("password", "varchar(255)", False, False),
        ("role", "varchar(20)", False, False),
        ("status", "tinyint(1)", False, False),
        ("image", "varchar(255)", False, False),
        ("changePass", "tinyint(4)", False, False),
        ("created_at", "timestamp", False, False)
    ], "#0284c7"),

    (65, 54, 44, 22, "pc_maintenance", [
        ("repair_id", "int(11)", True, False),
        ("pc_id", "varchar(20)", False, True),
        ("issue_description", "varchar(255)", False, False),
        ("cost", "decimal(10,2)", False, False),
        ("repair_date", "datetime", False, False)
    ], "#ca8a04"),

    (115, 66, 40, 42, "pc_table", [
        ("pc_id", "varchar(20)", True, False),
        ("pc_name", "varchar(50)", False, False),
        ("category", "varchar(20)", False, False),
        ("cpu", "varchar(50)", False, False),
        ("motherboard", "varchar(50)", False, False),
        ("ram_capacity", "varchar(20)", False, False),
        ("vga", "varchar(50)", False, False),
        ("ip_address", "varchar(20)", False, False),
        ("hourly_rate", "decimal(10,2)", False, False),
        ("status", "varchar(20)", False, False),
        ("ip_block", "tinyint(4)", False, False),
        ("recorded_at", "timestamp", False, False)
    ], "#0284c7")
]

for x, y, w, h, tname, cols, hbg in schema_tables:
    draw_schema_table(ax, x, y, w, h, tname, cols, hbg)

# Draw exact relationship lines
# 1. invoices -> gaming_sessions on session_id (blue line)
ax.plot([52, 60], [98, 98], color='#2563eb', lw=2)
ax.plot([52, 52], [98, 98], marker='o', color='#2563eb')
ax.plot([60, 60], [98, 98], marker='o', color='#2563eb')

# 2. invoices -> user on emp_no (yellow line)
ax.plot([52, 55, 55, 45], [92, 92, 50, 50], color='#eab308', lw=1.8)

# 3. gaming_sessions -> pc_table on pc_id (green line)
ax.plot([104, 115], [92, 58], color='#16a34a', lw=2)
ax.plot([104, 104], [92, 92], marker='o', color='#16a34a')
ax.plot([115, 115], [58, 58], marker='o', color='#16a34a')

# 4. pc_maintenance -> pc_table on pc_id (green line)
ax.plot([109, 115], [42, 42], color='#16a34a', lw=2)
ax.plot([109, 109], [42, 42], marker='o', color='#16a34a')
ax.plot([115, 115], [42, 42], marker='o', color='#16a34a')

plt.tight_layout()
plt.savefig('srs_assets/uml_database_er.png', dpi=300, bbox_inches='tight')
plt.close()
print("6. UML Database Schema Diagram generated.")

print("All enhanced UML diagrams generated successfully!")
