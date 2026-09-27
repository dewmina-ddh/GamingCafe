import os
import docx
from docx import Document
from docx.shared import Inches, Pt, RGBColor
from docx.enum.text import WD_ALIGN_PARAGRAPH
from docx.enum.table import WD_TABLE_ALIGNMENT, WD_ALIGN_VERTICAL
from docx.oxml import OxmlElement, parse_xml
from docx.oxml.ns import nsdecls, qn

doc = Document()

# Set standard page margins (1 inch)
for section in doc.sections:
    section.top_margin = Inches(1.0)
    section.bottom_margin = Inches(1.0)
    section.left_margin = Inches(1.0)
    section.right_margin = Inches(1.0)
    section.header.is_linked_to_previous = False
    section.footer.is_linked_to_previous = False

# Colors
COLOR_NAVY = RGBColor(0, 51, 102)      # #003366
COLOR_BLUE = RGBColor(2, 132, 199)     # #0284c7
COLOR_DARK = RGBColor(30, 41, 59)      # #1e293b
COLOR_MUTED = RGBColor(100, 116, 139)  # #64748b
HEX_HEADER_BG = "003366"
HEX_ROW_ALT = "F0F9FF"
HEX_BORDER = "CBD5E1"

# Helper functions for styling
def set_cell_background(cell, fill_hex):
    shading_elm = parse_xml(f'<w:shd {nsdecls("w")} w:fill="{fill_hex}"/>')
    cell._tc.get_or_add_tcPr().append(shading_elm)

def set_cell_margins(cell, top=100, bottom=100, left=150, right=150):
    tcPr = cell._tc.get_or_add_tcPr()
    tcMar = OxmlElement('w:tcMar')
    for m, val in [('top', top), ('bottom', bottom), ('left', left), ('right', right)]:
        node = OxmlElement(f'w:{m}')
        node.set(qn('w:w'), str(val))
        node.set(qn('w:type'), 'dxa')
        tcMar.append(node)
    tcPr.append(tcMar)

def add_title_cover():
    p0 = doc.add_paragraph()
    p0.alignment = WD_ALIGN_PARAGRAPH.CENTER
    run0 = p0.add_run("STANDARD IEEE 830-1998 / ISO/IEC/IEEE 29148 COMPLIANT")
    run0.font.name = "Arial"
    run0.font.size = Pt(11)
    run0.font.bold = True
    run0.font.color.rgb = COLOR_BLUE

    p1 = doc.add_paragraph()
    p1.alignment = WD_ALIGN_PARAGRAPH.CENTER
    p1.paragraph_format.space_before = Pt(30)
    p1.paragraph_format.space_after = Pt(20)
    run1 = p1.add_run("SOFTWARE REQUIREMENTS SPECIFICATION (SRS)")
    run1.font.name = "Arial"
    run1.font.size = Pt(16)
    run1.font.bold = True
    run1.font.color.rgb = COLOR_NAVY

    p2 = doc.add_paragraph()
    p2.alignment = WD_ALIGN_PARAGRAPH.CENTER
    p2.paragraph_format.space_before = Pt(40)
    p2.paragraph_format.space_after = Pt(40)
    run2 = p2.add_run("GAMING CAFE & ESPORTS ARENA\nMANAGEMENT SYSTEM")
    run2.font.name = "Arial"
    run2.font.size = Pt(24)
    run2.font.bold = True
    run2.font.color.rgb = COLOR_NAVY

    p3 = doc.add_paragraph()
    p3.alignment = WD_ALIGN_PARAGRAPH.CENTER
    p3.paragraph_format.space_before = Pt(20)
    run3 = p3.add_run("Real-time Station Allocation, Live Countdown Billing Engine, Hardware Inventory, Defect Tracking & Multi-Dimensional PDF Reporting")
    run3.font.name = "Arial"
    run3.font.size = Pt(11)
    run3.font.italic = True
    run3.font.color.rgb = COLOR_MUTED

    p4 = doc.add_paragraph()
    p4.alignment = WD_ALIGN_PARAGRAPH.CENTER
    p4.paragraph_format.space_before = Pt(120)
    run4 = p4.add_run("Prepared By: Damsith Dewmina\nProject Architecture: Java Desktop (Swing / FlatLaf) & MySQL RDBMS\nVersion: 1.0 (Enterprise Baseline)\nDate: September 2026")
    run4.font.name = "Arial"
    run4.font.size = Pt(11)
    run4.font.color.rgb = COLOR_DARK

    doc.add_page_break()

def add_header_footer():
    for s_idx, section in enumerate(doc.sections):
        if s_idx == 0:
            continue
        # Header
        hdr = section.header
        p_hdr = hdr.paragraphs[0]
        p_hdr.text = "Gaming Cafe Management System | IEEE 830-1998 Software Requirements Specification"
        p_hdr.alignment = WD_ALIGN_PARAGRAPH.RIGHT
        p_hdr.runs[0].font.name = "Arial"
        p_hdr.runs[0].font.size = Pt(8.5)
        p_hdr.runs[0].font.color.rgb = COLOR_MUTED

        # Footer
        ftr = section.footer
        p_ftr = ftr.paragraphs[0]
        p_ftr.text = "Confidential - Baselined System Specification  •  Version 1.0"
        p_ftr.alignment = WD_ALIGN_PARAGRAPH.LEFT
        p_ftr.runs[0].font.name = "Arial"
        p_ftr.runs[0].font.size = Pt(8.5)
        p_ftr.runs[0].font.color.rgb = COLOR_MUTED

def add_custom_heading(text, level=1):
    p = doc.add_paragraph()
    p.paragraph_format.keep_with_next = True
    run = p.add_run(text)
    run.font.name = "Arial"
    run.font.bold = True
    if level == 1:
        p.paragraph_format.space_before = Pt(18)
        p.paragraph_format.space_after = Pt(8)
        run.font.size = Pt(16)
        run.font.color.rgb = COLOR_NAVY
        # Add a subtle bottom border or separator line if desired
    elif level == 2:
        p.paragraph_format.space_before = Pt(14)
        p.paragraph_format.space_after = Pt(6)
        run.font.size = Pt(13)
        run.font.color.rgb = COLOR_BLUE
    elif level == 3:
        p.paragraph_format.space_before = Pt(10)
        p.paragraph_format.space_after = Pt(4)
        run.font.size = Pt(11)
        run.font.color.rgb = COLOR_DARK
    return p

def add_body_p(text, bold_prefix="", italic=False):
    p = doc.add_paragraph()
    p.paragraph_format.space_before = Pt(3)
    p.paragraph_format.space_after = Pt(5)
    p.paragraph_format.line_spacing = 1.15
    if bold_prefix:
        r_pre = p.add_run(bold_prefix)
        r_pre.font.name = "Arial"
        r_pre.font.size = Pt(10)
        r_pre.font.bold = True
        r_pre.font.color.rgb = COLOR_DARK
    r = p.add_run(text)
    r.font.name = "Arial"
    r.font.size = Pt(10)
    r.font.italic = italic
    r.font.color.rgb = COLOR_DARK
    return p

def add_callout(text, title="NOTE / ENGINEERING CONSTRAINTS"):
    tbl = doc.add_table(rows=1, cols=1)
    tbl.alignment = WD_TABLE_ALIGNMENT.CENTER
    cell = tbl.cell(0, 0)
    set_cell_background(cell, "F0F9FF")
    set_cell_margins(cell, top=140, bottom=140, left=180, right=180)
    cell.width = Inches(6.5)
    
    p = cell.paragraphs[0]
    p.paragraph_format.space_before = Pt(2)
    p.paragraph_format.space_after = Pt(4)
    r_t = p.add_run(f"■ {title}\n")
    r_t.font.name = "Arial"
    r_t.font.size = Pt(9.5)
    r_t.font.bold = True
    r_t.font.color.rgb = COLOR_NAVY
    
    r_b = p.add_run(text)
    r_b.font.name = "Arial"
    r_b.font.size = Pt(9.5)
    r_b.font.color.rgb = COLOR_DARK
    
    p_after = doc.add_paragraph()
    p_after.paragraph_format.space_before = Pt(4)
    p_after.paragraph_format.space_after = Pt(4)

def add_styled_table(headers, rows, col_widths=None):
    tbl = doc.add_table(rows=len(rows)+1, cols=len(headers))
    tbl.alignment = WD_TABLE_ALIGNMENT.CENTER
    
    # Header Row
    hdr_row = tbl.rows[0]
    for c_idx, h_text in enumerate(headers):
        cell = hdr_row.cells[c_idx]
        set_cell_background(cell, HEX_HEADER_BG)
        set_cell_margins(cell, top=120, bottom=120, left=140, right=140)
        p = cell.paragraphs[0]
        p.alignment = WD_ALIGN_PARAGRAPH.LEFT
        p.paragraph_format.space_before = Pt(2)
        p.paragraph_format.space_after = Pt(2)
        run = p.add_run(h_text)
        run.font.name = "Arial"
        run.font.size = Pt(9.5)
        run.font.bold = True
        run.font.color.rgb = RGBColor(255, 255, 255)
        
    # Data Rows
    for r_idx, r_data in enumerate(rows):
        row = tbl.rows[r_idx + 1]
        bg_hex = HEX_ROW_ALT if (r_idx % 2 == 1) else "FFFFFF"
        for c_idx, val in enumerate(r_data):
            cell = row.cells[c_idx]
            set_cell_background(cell, bg_hex)
            set_cell_margins(cell, top=100, bottom=100, left=140, right=140)
            p = cell.paragraphs[0]
            p.alignment = WD_ALIGN_PARAGRAPH.LEFT
            p.paragraph_format.space_before = Pt(2)
            p.paragraph_format.space_after = Pt(2)
            run = p.add_run(str(val))
            run.font.name = "Arial"
            run.font.size = Pt(9)
            run.font.color.rgb = COLOR_DARK
            
    if col_widths:
        for row in tbl.rows:
            for c_idx, w in enumerate(col_widths):
                row.cells[c_idx].width = Inches(w)
                
    p_sp = doc.add_paragraph()
    p_sp.paragraph_format.space_before = Pt(4)
    p_sp.paragraph_format.space_after = Pt(4)

def add_image_box(image_path, caption, width=Inches(6.0)):
    if os.path.exists(image_path):
        p_img = doc.add_paragraph()
        p_img.alignment = WD_ALIGN_PARAGRAPH.CENTER
        p_img.paragraph_format.space_before = Pt(10)
        p_img.paragraph_format.space_after = Pt(4)
        run = p_img.add_run()
        run.add_picture(image_path, width=width)
        
        p_cap = doc.add_paragraph()
        p_cap.alignment = WD_ALIGN_PARAGRAPH.CENTER
        p_cap.paragraph_format.space_before = Pt(2)
        p_cap.paragraph_format.space_after = Pt(12)
        r_cap = p_cap.add_run(f"Figure: {caption}")
        r_cap.font.name = "Arial"
        r_cap.font.size = Pt(9)
        r_cap.font.italic = True
        r_cap.font.color.rgb = COLOR_MUTED

# ====================================================================
# BUILD DOCUMENT CONTENT
# ====================================================================

# 1. Cover Page
add_title_cover()
add_header_footer()

# 2. Table of Contents / Executive Index
add_custom_heading("Table of Contents", level=1)
toc_items = [
    ("1. Introduction", "4"),
    ("    1.1 Purpose of this Document", "4"),
    ("    1.2 Document Conventions & Prioritization Rules (MoSCoW)", "4"),
    ("    1.3 Intended Audience & Reading Suggestions", "4"),
    ("    1.4 Project Background & Problem Statement", "5"),
    ("    1.5 The Engineered Solution & System Capabilities", "5"),
    ("    1.6 Project Scope & Operational Boundaries", "6"),
    ("    1.7 Applicable Standards & Industry References", "6"),
    ("2. Software Development Life Cycle (SDLC) - Waterfall Model Application", "7"),
    ("    2.1 Justification for the Waterfall SDLC Selection", "7"),
    ("    2.2 Detailed Waterfall Phase Breakdown (Phases 1 to 6)", "7"),
    ("    2.3 Waterfall Milestones, Deliverables & Quality Gates Table", "8"),
    ("3. Overall System Description", "9"),
    ("    3.1 Product Perspective & Context", "9"),
    ("    3.2 3-Tier Layered Architecture Pattern", "9"),
    ("    3.3 User Classes, Personas & Operational Responsibilities", "10"),
    ("    3.4 Operational Environment & System Requirements", "11"),
    ("    3.5 Design & Implementation Constraints", "11"),
    ("4. System Architecture, Data Models & Core Algorithms", "12"),
    ("    4.1 Relational Database Design & Complete Data Dictionaries (8 Tables)", "12"),
    ("    4.2 Database Entity Relationship (ER) Diagram", "15"),
    ("    4.3 Real-time Session Billing & Countdown Engine Algorithm", "16"),
    ("    4.4 Dynamic Session Time Extension Algorithm (+30m, +1h, +2h, Custom)", "17"),
    ("    4.5 Hardware Maintenance & Station Availability State-Machine", "17"),
    ("    4.6 Cryptographic Security Architecture & Password Lifecycle", "18"),
    ("5. Specific Functional Requirements (Modules 1 to 9)", "19"),
    ("    5.1 Module 1: Authentication, Access Control & First-Time Login", "19"),
    ("    5.2 Module 2: Central Operational Dashboard & Real-Time KPI Telemetry", "20"),
    ("    5.3 Module 3: Live Gaming Sessions & Active Station Monitoring", "21"),
    ("    5.4 Module 4: Session Duration Allocation & Dynamic Time Extension", "22"),
    ("    5.5 Module 5: Station & Hardware Inventory Management", "23"),
    ("    5.6 Module 6: Hardware Maintenance, Defect Logging & Repair History", "24"),
    ("    5.7 Module 7: Invoicing, Billing Engine & PDF Receipt Export", "25"),
    ("    5.8 Module 8: Business Intelligence, Auditing & Comprehensive PDF Reporting", "26"),
    ("    5.9 Module 9: User Administration, Staff Account Provisioning & Security", "27"),
    ("6. External Interface Requirements", "28"),
    ("    6.1 Graphical User Interfaces (GUI) & Dynamic Interaction Controls", "28"),
    ("    6.2 Hardware Interfaces & Token / QR Code Instant Session Check-In Scanner", "29"),
    ("    6.3 Software & Driver Interfaces (JDBC, FlatLaf, iText / Jasper PDF)", "30"),
    ("    6.4 Communications & Network Protocols", "30"),
    ("7. Non-Functional Requirements (NFR)", "31"),
    ("    7.1 Performance & Scalability Benchmarks", "31"),
    ("    7.2 Reliability, Availability & Disaster Recovery", "31"),
    ("    7.3 Information Security & Data Protection", "32"),
    ("    7.4 Software Quality Attributes", "32"),
    ("8. System Verification, Validation & Test Cases", "33"),
    ("    8.1 Requirements Traceability Matrix (RTM)", "33"),
    ("    8.2 Comprehensive Test Scenarios & Verified Results", "34"),
    ("9. Appendices, Glossary & Future Strategic Roadmap", "36"),
    ("    9.1 Glossary of Technical Terms & Acronyms", "36"),
    ("    9.2 Future Strategic Enhancements & Web/Cloud Roadmap", "37")
]

for item, pg in toc_items:
    p_toc = doc.add_paragraph()
    p_toc.paragraph_format.space_before = Pt(1)
    p_toc.paragraph_format.space_after = Pt(2)
    r_item = p_toc.add_run(item)
    r_item.font.name = "Arial"
    r_item.font.size = Pt(9.5)
    r_item.font.color.rgb = COLOR_DARK
    
    # Leader dots
    r_dots = p_toc.add_run(" " + "." * (80 - len(item)) + " ")
    r_dots.font.name = "Arial"
    r_dots.font.size = Pt(9)
    r_dots.font.color.rgb = COLOR_MUTED
    
    r_pg = p_toc.add_run(pg)
    r_pg.font.name = "Arial"
    r_pg.font.size = Pt(9.5)
    r_pg.font.bold = True
    r_pg.font.color.rgb = COLOR_NAVY

doc.add_page_break()

# ====================================================================
# SECTION 1: INTRODUCTION
# ====================================================================
add_custom_heading("1. Introduction", level=1)

add_custom_heading("1.1 Purpose of this Document", level=2)
add_body_p("This Software Requirements Specification (SRS) document provides a rigorous, exhaustive, and formalized technical contract for the Gaming Cafe & Esports Arena Management System. It specifies the complete functional, behavioral, operational, architectural, and quality performance requirements of the platform. This document adheres strictly to the internationally recognized IEEE Std 830-1998 (Recommended Practice for Software Requirements Specifications) and its modern harmonized standard ISO/IEC/IEEE 29148:2018 (Systems and software engineering - Life cycle processes - Requirements engineering). It serves as the authoritative single source of truth for software architects, development engineers, quality assurance testers, academic evaluators, and system administrators.")

add_custom_heading("1.2 Document Conventions & Prioritization Rules", level=2)
add_body_p("Throughout this specification, standardized typographical conventions, requirement tags, and prioritization schemes are enforced to guarantee unambiguous traceability and rigorous lifecycle mapping:")
add_body_p("Every discrete system requirement is identified by a unique alphanumeric key (e.g., FR-AUTH-01, FR-SES-02, NFR-PERF-01), creating a bidirectional trace to system test cases and design artifacts.", bold_prefix="• Requirement Identifiers: ")
add_body_p("All functional requirements are categorized according to the international MoSCoW prioritization model: MUST (High - Mandatory for baseline deployment), SHOULD (Medium - Critical operational efficiency feature), COULD (Low - Optional convenience capability), and WON'T (Deferred for future releases).", bold_prefix="• MoSCoW Prioritization Scheme: ")
add_body_p("Source code symbols, database tables, column definitions, and SQL queries are represented in monospaced font (e.g., gaming_sessions.add_minutes, SELECT * FROM pc WHERE status='Available').", bold_prefix="• Database & Code Symbols: ")

add_custom_heading("1.3 Intended Audience & Reading Suggestions", level=2)
add_body_p("This document is systematically structured to address the operational and evaluative needs of multiple technical stakeholders:")
add_body_p("Examine Section 2 for the formal Waterfall SDLC lifecycle mapping, Section 4 for data architecture and algorithmic models, and Section 8 for verification test cases.", bold_prefix="• Academic Evaluators & Examiners: ")
add_body_p("Focus on Section 4 (Data Dictionaries, SQL schemas, and cryptographic architecture) and Section 5 (Specific Functional Specifications).", bold_prefix="• Software Engineers & Developers: ")
add_body_p("Review Section 3 (Operational Environment & Hardware Specs), Section 5.9 (User Administration & Security Provisioning), and Section 7 (Security, Reliability, and Data Integrity).", bold_prefix="• System Administrators & Operations Personnel: ")

add_custom_heading("1.4 Project Background & Problem Statement", level=2)
add_body_p("Commercial gaming cafes and competitive esports arenas manage high-capital hardware investments, including high-refresh rate gaming PCs, PlayStation 5 consoles, dedicated racing simulators, and high-performance peripherals. Traditionally, gaming cafe operations rely upon manual paper registers, disconnected desktop timers, or unintegrated spreadsheets. These legacy operational methods suffer from severe vulnerabilities and administrative bottlenecks:")
add_body_p("Staff manually logging session start times frequently miscalculate remaining time, fail to detect overtime play, or allow customers to play beyond allocated hours without billing, causing substantial financial leakage.", bold_prefix="• Session Timing Inaccuracies & Revenue Leakage: ")
add_body_p("When customers request extra time (+30 mins, +1 hour) during a match, manual paper systems fail to update remaining countdowns in real-time, resulting in disputes at billing checkout.", bold_prefix="• Cumbersome Time Extensions: ")
add_body_p("Hardware specifications (CPU, GPU, RAM, Motherboard, Monitor refresh rate) and hourly pricing tiers are maintained informally. Damaged or malfunctioning stations dispatched accidentally cause customer dissatisfaction.", bold_prefix="• Untracked Hardware Specifications & Defect Logs: ")
add_body_p("Inability to instantly generate professional itemized tax/service invoices, cash/card breakdowns, or export multi-table historical audit logs to tamper-resistant PDF documents.", bold_prefix="• Lack of Unified Invoicing & Business Intelligence: ")
add_body_p("Absence of role-based access control (RBAC), unencrypted password storage, and lack of staff login/logout audit trails make administrative accountability impossible.", bold_prefix="• Security & Audit Vulnerabilities: ")

add_custom_heading("1.5 The Engineered Solution & System Capabilities", level=2)
add_body_p("The Gaming Cafe Management System engineered in this project is an enterprise-grade, high-responsiveness native Java desktop platform backed by an ACID-compliant MySQL relational database. The solution delivers automated station monitoring, a real-time countdown billing engine, dynamic time extensions, station hardware inventory tracking, repair dispatch management, a dedicated invoice billing frame with iText PDF receipt generation, and a multi-table business intelligence reporting suite.")

add_custom_heading("1.6 Project Scope & Operational Boundaries", level=2)
add_body_p("The functional boundaries of the system encompass the following core functional domains:")
add_body_p("Cryptographic SHA-256 password hashing, mandatory first-time password reset lifecycle, and administrative role gating.", bold_prefix="• Role-Based Security & Authentication: ")
add_body_p("Interactive graphical station grid (PC-01 to PC-08, PS5 consoles) with color-coded status badges (Available = Green, Occupied = Red, Repair = Orange) and real-time KPI summary telemetry.", bold_prefix="• Live Station & Session Telemetry: ")
add_body_p("Automated duration presets (1h, 1.5h, 2h, 3h, 4h, 5h, 6h, Custom min 60m) with dynamic hourly rate calculation and per-second remaining countdown timer.", bold_prefix="• Flexible Session Allocation & Pricing: ")
add_body_p("Dynamic in-session '+ Add Time' dialog (+30m, +1h, +2h, Custom) updating the active timer, total minutes, and price in memory and database atomically.", bold_prefix="• Seamless Time Extension Engine: ")
add_body_p("Comprehensive tracking of station processor, motherboard, memory, graphics card, category, and hourly tariff.", bold_prefix="• Hardware Asset Inventory: ")
add_body_p("Defect reason logging, maintenance status dispatch, and historic repair cost records.", bold_prefix="• Maintenance & Defect Tracking: ")
add_body_p("Standalone BillFrame displaying station metadata, customer info, duration breakdown, discounts, cash/card payment collection, and instant iText PDF receipt export.", bold_prefix="• Dedicated Billing & PDF Invoicing: ")
add_body_p("Interactive multi-category PDF report generator compiling user activity logs, session history, revenue billing, PC inventory, and maintenance logs.", bold_prefix="• Business Intelligence Reporting: ")
add_body_p("Support for token / QR scanner check-ins, allowing instant optical station unlocking upon presentation of valid player passes.", bold_prefix="• Token & QR Scanner Hardware Integration: ")

add_custom_heading("1.7 Applicable Standards & Industry References", level=2)
add_body_p("1. IEEE Std 830-1998: IEEE Recommended Practice for Software Requirements Specifications.\n2. ISO/IEC/IEEE 29148:2018: Systems and Software Engineering — Life Cycle Processes — Requirements Engineering.\n3. NIST FIPS 180-4: Secure Hash Standard (SHS) for SHA-256 cryptographic one-way hashing algorithms.\n4. Oracle Java Platform, Standard Edition (Java SE 21 LTS) Specifications.\n5. MySQL 8.0 Reference Manual: Relational Database Management System (RDBMS) ACID Transaction Handling.")

doc.add_page_break()

# ====================================================================
# SECTION 2: SDLC WATERFALL MODEL APPLICATION
# ====================================================================
add_custom_heading("2. Software Development Life Cycle (SDLC) - Waterfall Model Application", level=1)

add_custom_heading("2.1 Justification for the Waterfall SDLC Selection", level=2)
add_body_p("The classical Waterfall Software Development Life Cycle (SDLC) model was selected as the governance engineering methodology for the Gaming Cafe Management System. Unlike iterative frameworks that allow continuous scope fluctuations, commercial gaming cafe station management is governed by deterministic, highly stable business rules, rigid hardware relationships, and statutory billing requirements:")
add_body_p("Gaming session lifecycles follow a strict, linear progression: Station Selection → Duration Allocation → Session Start → Countdown & Time Extension → Session Termination → Invoice Generation & Payment Collection.", bold_prefix="• Linear Operational Workflows: ")
add_body_p("The underlying relational database schema (linking users, user logs, stations, gaming sessions, invoices, and repair records) requires complete Third Normal Form (3NF) modeling prior to GUI construction.", bold_prefix="• Rigid Relational Data Schema: ")
add_body_p("Real-time countdown calculations, dynamic time-adding queries, and financial invoice calculations demand formal algorithmic verification prior to deployment.", bold_prefix="• Algorithmic Determinism: ")

add_custom_heading("2.2 Detailed Waterfall Phase Breakdown", level=2)
add_body_p("Domain analysis, stakeholder interviews with cafe owners and counter cashiers, and workflow observation were conducted to formalize this IEEE 830 compliant SRS document.", bold_prefix="• Phase 1: Requirements Analysis & Elicitation — ")
add_body_p("The architectural blueprint established the 3-Tier Layered Architecture (Presentation Swing, Business Controller, and JDBC Data Access). Complete 3NF relational schemas, foreign key cascades, and GUI wireframes were formalized.", bold_prefix="• Phase 2: Architectural & Detailed System Design — ")
add_body_p("Implementation was executed in Java (JDK 21) using NetBeans IDE. FlatLaf modern UI was integrated, SHA-256 cryptographic hashing was enforced, parameterized PreparedStatements were implemented, and iText PDF export engines were built.", bold_prefix="• Phase 3: Implementation, Coding & Security Engineering — ")
add_body_p("Rigorous unit testing (NIC regex, phone number formats, duration limits), integration testing (countdown timer synchronization, add-time DB transactions), and security audits were conducted.", bold_prefix="• Phase 4: Integration, Verification & System Testing — ")
add_body_p("The application was packaged into an executable JAR archive with all bundled dependencies (mysql-connector-j, flatlaf, itextpdf). The production MySQL database was initialized, and User Acceptance Testing (UAT) was signed off.", bold_prefix="• Phase 5: Deployment, Configuration & UAT — ")
add_body_p("Procedures for corrective maintenance (bug fixes), adaptive maintenance (updating hardware categories or currency tariffs), and perfective maintenance (cloud sync and QR token integration) were established.", bold_prefix="• Phase 6: Maintenance & Operational Evolution — ")

add_custom_heading("2.3 Waterfall Milestones, Deliverables & Quality Gates Table", level=2)
wf_headers = ["Waterfall Phase", "Primary Inputs", "Engineering Activities", "Outputs / Deliverables", "Quality Review Gate"]
wf_rows = [
    ["Phase 1: Requirements", "Stakeholder interviews, legacy paper logs", "Requirements elicitation, domain analysis, MoSCoW prioritization", "Baselined SRS Document (IEEE 830), Feasibility Report", "Requirements Sign-off Milestone"],
    ["Phase 2: Design", "Baselined SRS Document", "3-Tier architecture partitioning, 3NF ERD normalization, GUI wireframing", "System Design Document (SDD), SQL DDL Schemas, UI Blueprints", "Preliminary & Critical Design Review (PDR/CDR)"],
    ["Phase 3: Coding", "SDD, DB DDL Scripts, UI Wireframes", "Java Swing coding, FlatLaf styling, JDBC integration, SHA-256 implementation", "Compiled Source Code (.java), Form Blueprints (.form), Ant Build Scripts", "Code Quality & Peer Inspection Gate"],
    ["Phase 4: Testing", "Compiled System Modules, Test Plan", "Unit testing, integration testing, timer stress tests, boundary verification", "Test Execution Results, Traceability Matrix (RTM), Bug Resolution Log", "Test Readiness & Verification Gate"],
    ["Phase 5: Deployment", "Verified Build, Production DB Schema", "Executable JAR packaging, MySQL database deployment, default credential provisioning", "Deployable GamingCafe.jar, Database Initialization Scripts, UAT Certificate", "Operational Acceptance Milestone"],
    ["Phase 6: Maintenance", "User defect reports, hardware changes", "Log monitoring, bug fixing, tariff updates, performance tuning", "Service Patches, Version Updates, Maintenance Logs", "Post-Implementation Review Gate"]
]
add_styled_table(wf_headers, wf_rows, [1.1, 1.2, 1.5, 1.5, 1.2])

doc.add_page_break()

# ====================================================================
# SECTION 3: OVERALL SYSTEM DESCRIPTION
# ====================================================================
add_custom_heading("3. Overall System Description", level=1)

add_custom_heading("3.1 Product Perspective & Context", level=2)
add_body_p("The Gaming Cafe Management System operates as a high-performance, standalone desktop management hub communicating with a local or LAN-hosted MySQL Relational Database instance via the Java Database Connectivity (JDBC) API. It replaces fragmented paper registers, unreliable manual stopwatch timers, and unlinked billing spreadsheets with an authoritative, unified transactional store. The application is natively deployed on counter workstations operated by front-desk cashiers and cafe administrators.")

add_custom_heading("3.2 3-Tier Layered Architecture Pattern", level=2)
add_body_p("To guarantee high modularity, maintainability, low coupling, and clear separation of concerns, the platform is structured around a 3-Tier Layered Architecture:")
add_body_p("Constructed using Java Swing and enhanced with the FlatLaf modern UI engine. Manages user visual interactions, responsive card transitions (CardLayout), real-time station status cards, custom timer displays, and event listeners.", bold_prefix="• Presentation Layer (GUI Tier): ")
add_body_p("Encapsulates business domain logic, session countdown timers (javax.swing.Timer), dynamic price calculation rules, input validation routines (NIC regex, phone numbers), cryptographic SHA-256 hashing, and PDF export compilers.", bold_prefix="• Business Logic Layer (Controller Tier): ")
add_body_p("Encapsulated by centralized database connection management (db.java) utilizing parameterized PreparedStatement objects. Guarantees ACID transactional compliance, isolates the application from underlying storage mechanics, and completely prevents SQL injection vulnerabilities.", bold_prefix="• Data Access Layer (Persistence Tier): ")

add_image_box("srs_assets/architecture_diagram.png", "3-Tier Layered Architecture Blueprint of the Gaming Cafe Management Platform", width=Inches(6.2))

add_custom_heading("3.3 User Classes, Personas & Operational Responsibilities", level=2)
add_body_p("The system defines distinct operational user classes with segregated security privileges:")
user_class_headers = ["User Role", "Persona Profile", "System Capabilities & Access Rights", "Operational Security Level"]
user_class_rows = [
    ["System Administrator", "Cafe Owner / General Manager / IT Administrator", "Full unrestricted access. Can view Dashboard, manage Live Sessions, configure Station Hardware Inventory, log Maintenance, access Admin History, generate multi-category PDF reports, provision Staff accounts, and reset user credentials.", "Tier 1 - Full Administrative Privileges"],
    ["Cafe Staff / Cashier", "Front-Desk Receptionist / Station Dispatch Operator", "Operational access. Can start sessions, allocate durations, execute '+ Add Time' extensions, end sessions, generate BillFrame invoices, collect payments, and log hardware repair requests. Blocked from User Management and Admin settings.", "Tier 2 - Operational Business Access Only"],
    ["Gamer / Customer", "End-User Player / Esports Competitor", "Indirect user entity. Enrolled in system sessions with name, phone, allocated time, station assignment, and payment records. Can utilize Token/QR pass for automated station check-in.", "Tier 3 - Data Entity / Customer Resource Tier"]
]
add_styled_table(user_class_headers, user_class_rows, [1.3, 1.4, 2.6, 1.2])

add_custom_heading("3.4 Operational Environment & System Requirements", level=2)
add_body_p("The hardware and software prerequisites for deploying the application are specified below:")
add_body_p("Intel Core i3 (2.0 GHz) or AMD Ryzen 3 equivalent (Intel Core i5 recommended for high-load multi-counter setups).", bold_prefix="• Client Processor: ")
add_body_p("Minimum 4 GB RAM (8 GB recommended for optimal JVM heap execution and smooth FlatLaf vector rendering).", bold_prefix="• System Memory: ")
add_body_p("Minimum 500 MB free storage for application runtime, database storage, and generated PDF invoice archives.", bold_prefix="• Disk Storage: ")
add_body_p("Minimum 1366 × 768 pixels (Full HD 1920 × 1080 recommended for optimal station grid viewing).", bold_prefix="• Display Resolution: ")
add_body_p("Microsoft Windows 10/11 (64-bit), Linux (Ubuntu 20.04+), or macOS (12+) with Java Runtime.", bold_prefix="• Operating System: ")
add_body_p("Oracle OpenJDK or Eclipse Temurin JRE 8, 11, 17, or 21 (LTS).", bold_prefix="• Java Environment: ")
add_body_p("MySQL Community Server 8.0 or 8.4 LTS running on TCP/IP port 3306.", bold_prefix="• Relational Database: ")
add_body_p("FlatLaf Modern Look-and-Feel v3.5.4+ and iText PDF Library 5.5+ / JasperReports.", bold_prefix="• Libraries: ")

add_custom_heading("3.5 Design & Implementation Constraints", level=2)
add_body_p("1. Desktop-First Native Performance: Designed as a native Java desktop application providing sub-millisecond local response times without cloud server latency.\n2. Irreversible Password Cryptography: Passwords must never be stored or transmitted in plain text. SHA-256 one-way hashing with Base64 encoding is strictly enforced.\n3. Referential Integrity: Foreign key constraints across users, user_logs, pc, gaming_sessions, invoices, and repair records must prevent orphaned records.\n4. GUI Thread Safety: All UI modifications and countdown timer ticks must execute strictly on the Java Event Dispatch Thread (java.awt.EventQueue.invokeLater).")

doc.add_page_break()

# ====================================================================
# SECTION 4: SYSTEM ARCHITECTURE, DATA MODELS & ALGORITHMS
# ====================================================================
add_custom_heading("4. System Architecture, Data Models & Core Algorithms", level=1)

add_custom_heading("4.1 Relational Database Design & Complete Data Dictionaries", level=2)
add_body_p("The database schema is fully normalized to Third Normal Form (3NF), eliminating partial and transitive dependencies while guaranteeing transactional integrity.")

# Table 1: users
add_custom_heading("Table 1: users (Staff & Administrator Authentication Accounts)", level=3)
t1_headers = ["Column Name", "Data Type", "Constraints", "Default", "Description & Validation Rules"]
t1_rows = [
    ["user_id", "INT", "PRIMARY KEY, AUTO_INCREMENT", "None", "Unique auto-generated identifier for the user account."],
    ["f_name", "VARCHAR(50)", "NOT NULL", "None", "First name of the staff member (2-50 characters)."],
    ["l_name", "VARCHAR(50)", "NOT NULL", "None", "Last name / surname of the staff member."],
    ["nic", "VARCHAR(20)", "NOT NULL, UNIQUE", "None", "National Identity Card number (validated via regex: 9-digit+V/X or 12-digit)."],
    ["phone", "VARCHAR(15)", "NOT NULL", "None", "Mobile contact telephone number (10-15 digits)."],
    ["email", "VARCHAR(100)", "NULL", "None", "Staff email address (validated for standard RFC 5322 format)."],
    ["username", "VARCHAR(50)", "NOT NULL, UNIQUE", "None", "Unique login handle (3-50 alphanumeric characters)."],
    ["password", "VARCHAR(255)", "NOT NULL", "None", "SHA-256 cryptographic one-way digest encoded in Base64."],
    ["role", "VARCHAR(20)", "NOT NULL", "'Staff'", "Access control role: 'Admin' or 'Staff'."],
    ["status", "VARCHAR(20)", "NOT NULL", "'Active'", "Account operational state: 'Active' or 'Inactive'."],
    ["first_time", "INT", "NOT NULL", "1", "First-time login flag: 1 = Mandatory password change required, 0 = Active."]
]
add_styled_table(t1_headers, t1_rows, [1.1, 1.0, 1.6, 0.7, 2.1])

# Table 2: user_logs
add_custom_heading("Table 2: user_logs (Staff Login & Logout Audit Trail)", level=3)
t2_headers = ["Column Name", "Data Type", "Constraints", "Default", "Description & Validation Rules"]
t2_rows = [
    ["log_id", "INT", "PRIMARY KEY, AUTO_INCREMENT", "None", "Unique auto-generated log record identifier."],
    ["user_id", "INT", "FOREIGN KEY → users(user_id)", "None", "Referenced staff user who authenticated."],
    ["username", "VARCHAR(50)", "NOT NULL", "None", "Captured username at the moment of login."],
    ["role", "VARCHAR(20)", "NOT NULL", "None", "User role at session login time (Admin / Staff)."],
    ["login_time", "DATETIME", "NOT NULL", "CURRENT_TIMESTAMP", "Timestamp of successful authentication."],
    ["logout_time", "DATETIME", "NULL", "None", "Timestamp of clean logout session termination."],
    ["session_duration", "VARCHAR(30)", "NULL", "None", "Calculated active shift duration (e.g., '04h 25m 10s')."]
]
add_styled_table(t2_headers, t2_rows, [1.2, 1.1, 1.5, 1.1, 1.6])

# Table 3: pc (gaming_stations)
add_custom_heading("Table 3: pc (Gaming Stations & Hardware Inventory)", level=3)
t3_headers = ["Column Name", "Data Type", "Constraints", "Default", "Description & Validation Rules"]
t3_rows = [
    ["id", "VARCHAR(20)", "PRIMARY KEY", "None", "Unique station identifier (e.g., 'PC-01', 'PS5-02')."],
    ["pc_name", "VARCHAR(50)", "NOT NULL", "None", "Descriptive station alias (e.g., 'RTX 4070 Station 01')."],
    ["category", "VARCHAR(30)", "NOT NULL", "'PC'", "Station hardware category: 'PC', 'PS5', 'Racing Sim'."],
    ["hourly_rate", "DECIMAL(10,2)", "NOT NULL", "200.00", "Base hourly rental charge in LKR (e.g., 200.00, 350.00)."],
    ["cpu", "VARCHAR(50)", "NOT NULL", "None", "Central Processing Unit model (e.g., 'Intel Core i7-13700K')."],
    ["motherboard", "VARCHAR(50)", "NOT NULL", "None", "Motherboard chipset/model (e.g., 'MSI MAG B760')."],
    ["ram", "VARCHAR(30)", "NOT NULL", "None", "Installed RAM capacity & speed (e.g., '32GB DDR5 6000MHz')."],
    ["vga", "VARCHAR(50)", "NOT NULL", "None", "Dedicated Graphics Processing Unit (e.g., 'Nvidia RTX 4070 12GB')."],
    ["status", "VARCHAR(20)", "NOT NULL", "'Available'", "Station state: 'Available', 'Occupied', 'Repair'."],
    ["created_at", "TIMESTAMP", "DEFAULT CURRENT_TIMESTAMP", "Current", "Timestamp when station asset was registered."]
]
add_styled_table(t3_headers, t3_rows, [1.1, 1.1, 1.4, 0.9, 2.0])

# Table 4: gaming_sessions
add_custom_heading("Table 4: gaming_sessions (Live & Historic Gaming Play Sessions)", level=3)
t4_headers = ["Column Name", "Data Type", "Constraints", "Default", "Description & Validation Rules"]
t4_rows = [
    ["session_id", "VARCHAR(50)", "PRIMARY KEY", "None", "Unique session identifier (e.g., 'SES-20260927-001')."],
    ["pc_id", "VARCHAR(20)", "FOREIGN KEY → pc(id)", "None", "Referenced gaming station allocated for play."],
    ["cus_name", "VARCHAR(100)", "NOT NULL", "'Walk-in Customer'", "Name of the gamer / player."],
    ["phone", "VARCHAR(15)", "NULL", "None", "Mobile contact telephone number of the player."],
    ["start_time", "DATETIME", "NOT NULL", "CURRENT_TIMESTAMP", "Timestamp when session gameplay commenced."],
    ["end_time", "DATETIME", "NULL", "None", "Timestamp when session was terminated."],
    ["add_minutes", "INT", "NOT NULL", "0", "Cumulative extra time added during session in minutes."],
    ["price_per_adding", "DECIMAL(10,2)", "NOT NULL", "0.00", "Additional charge incurred via '+ Add Time' extensions."],
    ["total_minutes", "INT", "NOT NULL", "60", "Total allocated playing duration (Initial + Add Time)."],
    ["total_amount", "DECIMAL(10,2)", "NOT NULL", "0.00", "Total calculated session charge before discounts."],
    ["status", "VARCHAR(20)", "NOT NULL", "'Ongoing'", "Session state: 'Ongoing', 'Completed', 'Cancelled'."]
]
add_styled_table(t4_headers, t4_rows, [1.2, 1.1, 1.5, 0.9, 1.8])

# Table 5: invoices
add_custom_heading("Table 5: invoices (Financial Invoicing & Payment Records)", level=3)
t5_headers = ["Column Name", "Data Type", "Constraints", "Default", "Description & Validation Rules"]
t5_rows = [
    ["invoice_no", "VARCHAR(50)", "PRIMARY KEY", "None", "Unique invoice serial number (e.g., 'INV-20260927-0104')."],
    ["session_id", "VARCHAR(50)", "FOREIGN KEY → gaming_sessions", "None", "Referenced session record billed."],
    ["pc_id", "VARCHAR(20)", "FOREIGN KEY → pc(id)", "None", "Gaming station utilized during the billed session."],
    ["customer_name", "VARCHAR(100)", "NOT NULL", "None", "Customer / Gamer name on invoice receipt."],
    ["duration_mins", "INT", "NOT NULL", "None", "Total billed gaming duration in minutes."],
    ["total_amount", "DECIMAL(10,2)", "NOT NULL", "None", "Subtotal amount before promotional discounts."],
    ["discount", "DECIMAL(10,2)", "NOT NULL", "0.00", "Promotional discount deducted from bill."],
    ["net_amount", "DECIMAL(10,2)", "NOT NULL", "None", "Final net amount payable by customer."],
    ["payment_type", "VARCHAR(20)", "NOT NULL", "'Cash'", "Payment method utilized: 'Cash', 'Card', 'QR Token'."],
    ["cashier_name", "VARCHAR(50)", "NOT NULL", "None", "Staff member who finalized the transaction."],
    ["created_at", "DATETIME", "DEFAULT CURRENT_TIMESTAMP", "Current", "Timestamp of invoice settlement."]
]
add_styled_table(t5_headers, t5_rows, [1.1, 1.1, 1.4, 0.9, 2.0])

# Table 6: repair_pc & repair_history
add_custom_heading("Table 6: repair_pc & repair_history (Maintenance & Defect Logs)", level=3)
t6_headers = ["Column Name", "Data Type", "Constraints", "Default", "Description & Validation Rules"]
t6_rows = [
    ["repair_id", "INT", "PRIMARY KEY, AUTO_INCREMENT", "None", "Unique defect repair ticket number."],
    ["pc_id", "VARCHAR(20)", "FOREIGN KEY → pc(id)", "None", "Station currently under hardware repair."],
    ["pc_name", "VARCHAR(50)", "NOT NULL", "None", "Descriptive station alias."],
    ["reason", "VARCHAR(255)", "NOT NULL", "None", "Reported hardware failure description (e.g., 'GPU Artifacts')."],
    ["date", "DATE", "NOT NULL", "CURRENT_DATE", "Date station was transitioned into repair."],
    ["history_id", "INT", "PRIMARY KEY (repair_history)", "None", "Historic record ticket after repair completion."],
    ["cost", "DECIMAL(10,2)", "NOT NULL", "0.00", "Total maintenance/replacement cost incurred in LKR."],
    ["technician", "VARCHAR(50)", "NOT NULL", "None", "External or internal hardware technician name."]
]
add_styled_table(t6_headers, t6_rows, [1.1, 1.1, 1.4, 0.9, 2.0])

# Table 7: settings
add_custom_heading("Table 7: settings (Global Cafe Configuration Parameters)", level=3)
t7_headers = ["Column Name", "Data Type", "Constraints", "Default", "Description & Validation Rules"]
t7_rows = [
    ["setting_id", "INT", "PRIMARY KEY", "1", "Fixed singleton row identifier for global configuration."],
    ["default_password", "VARCHAR(50)", "NOT NULL", "'1234'", "Default fallback password assigned when creating blank accounts."],
    ["cafe_name", "VARCHAR(100)", "NOT NULL", "'Apex Esports Arena'", "Business name rendered on invoice headers and reports."],
    ["currency", "VARCHAR(10)", "NOT NULL", "'LKR'", "Standard currency symbol utilized for financial calculation."],
    ["updated_at", "TIMESTAMP", "DEFAULT CURRENT_TIMESTAMP", "Current", "Timestamp tracking last configuration update."]
]
add_styled_table(t7_headers, t7_rows, [1.2, 1.1, 1.5, 0.9, 1.8])

doc.add_page_break()

add_custom_heading("4.2 Database Entity Relationship (ER) Diagram", level=2)
add_body_p("The conceptual and logical relationships between all entities in the normalized MySQL database schema are illustrated below. Strict foreign key cascades guarantee referential consistency across gaming sessions, billing invoices, user audit logs, and hardware repair records.")

add_image_box("srs_assets/er_diagram.png", "Complete Relational Database Entity Relationship (ER) Diagram (3NF Normalized)", width=Inches(6.3))

add_custom_heading("4.3 Real-Time Session Billing & Countdown Engine Algorithm", level=2)
add_body_p("The core billing and timing engine executes a per-second real-time countdown algorithm via javax.swing.Timer. When a session is initialized, the allocated duration (minimum 60 minutes) determines the target end time. The system calculates remaining time and auto-updates the estimated charges in real-time.")

add_callout("""// Mathematical Pricing Formulation:
Base_Cost = (Allocated_Minutes / 60.0) * Hourly_Rate
Remaining_Millis = Session_End_Time_Millis - Current_System_Time_Millis

// Overtime Detection:
If (Remaining_Millis < 0):
    Elapsed_Minutes = Ceil((Current_System_Time_Millis - Session_Start_Time_Millis) / 60000.0)
    Total_Charge = (Elapsed_Minutes / 60.0) * Hourly_Rate
    Display Status: "00:00:00 (+MM:SS Overtime)" [Red Alert]
Else:
    Display Status: "HH:MM:SS Remaining" [Green Normal]
""", title="SESSION COUNTDOWN & OVERTIME FORMULATION")

add_custom_heading("4.4 Dynamic Session Time Extension Algorithm (+30m, +1h, +2h, Custom)", level=2)
add_body_p("When a player requests additional gaming time during an ongoing match, the system executes an atomic transaction that modifies the allocated minutes, recalculates the target end time timestamp, and updates the database record via parameterized SQL:")

add_callout("""-- Atomic Add-Time SQL Execution via PreparedStatement:
UPDATE gaming_sessions 
SET add_minutes = add_minutes + ?,
    total_minutes = total_minutes + ?,
    price_per_adding = ?,
    total_amount = total_amount + ?
WHERE session_id = ? AND status = 'Ongoing';
""", title="ATOMIC TIME EXTENSION SQL TRANSACTION")

add_custom_heading("4.5 Hardware Maintenance & Station Availability State-Machine", level=2)
add_body_p("Each gaming station (PC or PS5) operates as a finite state-machine with three distinct operational states: Available (Green), Occupied (Red), and Repair (Orange). State transitions are governed by strict operational rules:")
add_body_p("Station is idle, ready for immediate assignment. Customer name and phone fields are unlocked for editing. Duration selection combo box is enabled. '▶ Start Session' button is revealed.", bold_prefix="• State 1: Available — ")
add_body_p("Session is actively running. Real-time countdown timer ticks per second. Customer inputs are locked to prevent tampering. '⏱ + Add Time' and '⏹ End Session & Bill' action buttons are revealed.", bold_prefix="• State 2: Occupied — ")
add_body_p("Station is undergoing hardware servicing or defect repair. Session dispatch is strictly blocked. Station is listed in repair management tables until repair resolution is signed off.", bold_prefix="• State 3: Repair — ")

add_custom_heading("4.6 Cryptographic Security Architecture & Password Lifecycle", level=2)
add_body_p("User authentication credentials are protected using NIST FIPS 180-4 compliant SHA-256 cryptographic one-way hashing. Plain text passwords never touch the database or log files.")
add_body_p("When an administrator provisions a new staff account, the default password (retrieved from settings.default_password) is hashed via SHA-256 and saved with first_time = 1.", bold_prefix="• Default Password Provisioning: ")
add_body_p("Upon successful authentication, LoginFrame checks the first_time flag. If first_time == 1, the user is intercepted and routed to ChangeUserDetails.java to establish personal credentials before dashboard access is permitted.", bold_prefix="• First-Time Login Redirection: ")
add_body_p("Administrators can reset any locked or compromised account back to the default password via the 'Reset User' action button in User Management.", bold_prefix="• Administrative Credential Reset: ")

doc.add_page_break()

# ====================================================================
# SECTION 5: SPECIFIC FUNCTIONAL REQUIREMENTS (MODULES 1 TO 9)
# ====================================================================
add_custom_heading("5. Specific Functional Requirements (Modules 1 to 9)", level=1)
add_body_p("This section specifies the detailed functional requirements across the nine core modules of the Gaming Cafe Management System. Each requirement is tagged with a unique identifier and prioritized according to MoSCoW rules.")

# Module 1
add_custom_heading("5.1 Module 1: Authentication, Access Control & First-Time Login", level=2)
m1_headers = ["Req ID", "Requirement Title", "Priority", "Detailed Functional Specification"]
m1_rows = [
    ["FR-AUTH-01", "Credential Hashing Authentication", "MUST", "The system shall authenticate usernames/NIC and passwords by digesting input via SHA-256 and verifying matching records in users table."],
    ["FR-AUTH-02", "First-Time Login Interception", "MUST", "If an authenticating user account possesses first_time = 1, the system shall intercept navigation and display ChangeUserDetails.java."],
    ["FR-AUTH-03", "Mandatory Password Modification", "MUST", "ChangeUserDetails form shall require entering new password and confirmation, validate minimum 4 characters, hash with SHA-256, set first_time = 0, and advance to Dashboard."],
    ["FR-AUTH-04", "Role-Based UI Feature Gating", "MUST", "Upon login, the system shall evaluate user role. If role is 'Staff', administrative buttons (btnAdmin, User Management, Global Settings) shall be hidden."],
    ["FR-AUTH-05", "User Activity Audit Logging", "MUST", "Upon every successful authentication, the system shall insert a record into user_logs capturing user_id, username, role, and login_time."],
    ["FR-AUTH-06", "Clean Session Logout", "MUST", "The system shall allow users to log out, updating logout_time and session_duration in user_logs, terminating active windows, and returning to LoginFrame."]
]
add_styled_table(m1_headers, m1_rows, [1.1, 1.6, 0.9, 2.9])

# Module 2
add_custom_heading("5.2 Module 2: Central Operational Dashboard & Real-Time KPI Telemetry", level=2)
m2_headers = ["Req ID", "Requirement Title", "Priority", "Detailed Functional Specification"]
m2_rows = [
    ["FR-DASH-01", "Live Station KPI Telemetry", "MUST", "The dashboard shall execute relational aggregation queries to compute and render Total Stations, Active Playing Sessions, Available PCs, and Stations in Repair in real-time summary cards."],
    ["FR-DASH-02", "Interactive Station Grid Rendering", "MUST", "The dashboard shall dynamically render interactive cards for all stations (PC-01 to PC-08, PS5 consoles) with color-coded status badges (Available = Green, Occupied = Red, Repair = Orange)."],
    ["FR-DASH-03", "One-Click Station Selection", "MUST", "Clicking any station card in the grid shall immediately populate the Session Control side-panel with station name, category, hourly rate, and active session telemetry."],
    ["FR-DASH-04", "Digital Clock & Date Header", "MUST", "The system shall display live synchronized system date and digital time in the top application header."],
    ["FR-DASH-05", "Collapsible Hamburger Navigation", "SHOULD", "The system shall support toggling the sidebar navigation menu via btnHam to maximize workspace area on compact displays."]
]
add_styled_table(m2_headers, m2_rows, [1.1, 1.6, 0.9, 2.9])

# Module 3
add_custom_heading("5.3 Module 3: Live Gaming Sessions & Active Station Monitoring", level=2)
m3_headers = ["Req ID", "Requirement Title", "Priority", "Detailed Functional Specification"]
m3_rows = [
    ["FR-LIVE-01", "Live Sessions Tabular View", "MUST", "The system shall provide a dedicated Live Sessions table (jTableLive) displaying all active ongoing sessions with Customer Name, Phone, Station, Start Time, Duration, and Live Timer."],
    ["FR-LIVE-02", "Real-time Live Table Tick Refresh", "MUST", "The Live Sessions table shall update every second via javax.swing.Timer to show synchronized countdown timestamps across all concurrent players."],
    ["FR-LIVE-03", "Live Search & Customer Filtering", "MUST", "The system shall filter the live sessions table dynamically as characters are typed into txtLiveSearch by customer name, phone number, or PC ID."],
    ["FR-LIVE-04", "One-Click Session End from Live Table", "MUST", "Double-clicking or selecting an active row in the live table shall allow instant session termination and invoice generation."],
    ["FR-LIVE-05", "Overtime Visual Highlighting", "MUST", "Active sessions exceeding their allocated duration shall be highlighted in high-contrast red font with overtime surcharge notification."]
]
add_styled_table(m3_headers, m3_rows, [1.1, 1.6, 0.9, 2.9])

# Module 4
add_custom_heading("5.4 Module 4: Session Duration Allocation & Dynamic Time Extension", level=2)
m4_headers = ["Req ID", "Requirement Title", "Priority", "Detailed Functional Specification"]
m4_rows = [
    ["FR-SES-01", "Standard Duration Presets", "MUST", "The system shall provide a duration selection ComboBox (cmbSessionDuration) with options: 1h (60m), 1.5h (90m), 2h (120m), 3h (180m), 4h (240m), 5h (300m), 6h (360m), and Custom Minutes."],
    ["FR-SES-02", "Minimum 1-Hour Duration Validation", "MUST", "The system shall enforce a minimum session duration of 60 minutes (1 Hour) for both standard presets and custom minute entries."],
    ["FR-SES-03", "Real-time Estimated Charge Calculation", "MUST", "Selecting any duration preset shall instantly calculate and render the estimated session price: (Allocated_Minutes / 60.0) * Station_Hourly_Rate."],
    ["FR-SES-04", "Dynamic In-Session '+ Add Time'", "MUST", "The system shall provide an '⏱ + Add Time' button during active sessions allowing staff to add +30m, +1h, +2h, or Custom minutes, updating the active countdown and database record immediately."],
    ["FR-SES-05", "Session Start Validation & Dispatch", "MUST", "Clicking '▶ Start Session' shall validate customer name, verify station availability, insert record into gaming_sessions, set station status to 'Occupied', and start the countdown timer."]
]
add_styled_table(m4_headers, m4_rows, [1.1, 1.6, 0.9, 2.9])

# Module 5
add_custom_heading("5.5 Module 5: Station & Hardware Inventory Management", level=2)
m5_headers = ["Req ID", "Requirement Title", "Priority", "Detailed Functional Specification"]
m5_rows = [
    ["FR-PC-01", "Hardware Specification Logging", "MUST", "The system shall record station hardware specifications: Station ID, Name, Category (PC/PS5), Hourly Rate, CPU, Motherboard, RAM, and Dedicated Graphics Card (VGA)."],
    ["FR-PC-02", "Station Inventory Tabular View", "MUST", "The system shall display all registered stations in a styled table with real-time status badges and hardware component summaries."],
    ["FR-PC-03", "Hardware Specification Editing", "MUST", "Authorized administrators shall be able to update station hardware specifications, component upgrades, and hourly rental tariffs."],
    ["FR-PC-04", "Station Decommissioning & Removal", "MUST", "The system shall permit removing retired stations upon administrative confirmation while preserving historic session logs."],
    ["FR-PC-05", "Hardware Category Filtering", "SHOULD", "The system shall support filtering stations by hardware category (PC High-Tier, PC Mid-Tier, PS5 Console) for rapid dispatch."]
]
add_styled_table(m5_headers, m5_rows, [1.1, 1.6, 0.9, 2.9])

# Module 6
add_custom_heading("5.6 Module 6: Hardware Maintenance, Defect Logging & Repair History", level=2)
m6_headers = ["Req ID", "Requirement Title", "Priority", "Detailed Functional Specification"]
m6_rows = [
    ["FR-REP-01", "Station Defect Dispatch", "MUST", "Staff shall be able to mark any malfunctioning station as 'Repair', entering a detailed defect reason (e.g., 'GPU Artifacts', 'RAM Failure', 'Cooler Leak') and taking the station off-line."],
    ["FR-REP-02", "Active Repairs Management Table", "MUST", "The system shall maintain an active repair table (jTable2) listing all stations currently under maintenance with reported issues and dates."],
    ["FR-REP-03", "Repair Resolution & Return to Service", "MUST", "Upon technician completion, staff shall log the repair cost and technician name, restoring station status to 'Available' and archiving the record to repair_history."],
    ["FR-REP-04", "Maintenance History & Cost Auditing", "MUST", "The system shall display historical maintenance records in repairHistory table to audit total hardware repair expenditures over time."],
    ["FR-REP-05", "Maintenance PDF Report Export", "SHOULD", "Administrators shall be able to compile and export the complete hardware maintenance and repair expenditure log as a formatted PDF document."]
]
add_styled_table(m6_headers, m6_rows, [1.1, 1.6, 0.9, 2.9])

# Module 7
add_custom_heading("5.7 Module 7: Invoicing, Billing Engine & PDF Receipt Export", level=2)
m7_headers = ["Req ID", "Requirement Title", "Priority", "Detailed Functional Specification"]
m7_rows = [
    ["FR-BILL-01", "Dedicated BillFrame Presentation", "MUST", "Clicking '⏹ End Session & Bill' shall terminate the active session, calculate total charges, and open a dedicated, modern BillFrame window."],
    ["FR-BILL-02", "Itemized Invoice Breakdown", "MUST", "BillFrame shall display invoice serial number, station name, category, customer name, phone, start/end timestamps, total duration, hourly rate, and subtotal amount."],
    ["FR-BILL-03", "Promotional Discount Calculation", "MUST", "BillFrame shall support applying special promotional discounts, recalculating Net Payable Amount in real-time."],
    ["FR-BILL-04", "Cash & Change Due Calculator", "MUST", "Entering cash received from customer shall automatically compute and display the change due (Balance = Cash_Received - Net_Amount)."],
    ["FR-BILL-05", "Payment Method Selection", "MUST", "Staff shall record the payment method (Cash / Card / QR Token) before finalizing the invoice transaction."],
    ["FR-BILL-06", "iText PDF Receipt Export & Print", "MUST", "Clicking 'Print / Save PDF Bill' shall compile and export an official PDF invoice receipt and prompt the user to immediately view/print the document."]
]
add_styled_table(m7_headers, m7_rows, [1.1, 1.6, 0.9, 2.9])

# Module 8
add_custom_heading("5.8 Module 8: Business Intelligence, Auditing & Comprehensive PDF Reporting", level=2)
m8_headers = ["Req ID", "Requirement Title", "Priority", "Detailed Functional Specification"]
m8_rows = [
    ["FR-REP-01", "Multi-Category Report Generation Dialog", "MUST", "The system shall provide an interactive option dialog (showReportGenerationDialog) allowing administrators to generate 5 distinct PDF reports."],
    ["FR-REP-02", "User Activity Logs & Attendance Report", "MUST", "The system shall compile and export all staff login, logout, and shift duration records into a structured 'User Activity Logs Report' PDF."],
    ["FR-REP-03", "Session History & Usage Utilization Report", "MUST", "The system shall compile and export all historic gaming sessions into a structured 'PC Session History Report' PDF."],
    ["FR-REP-04", "Invoices & Revenue Billing Report", "MUST", "The system shall compile and export all finalized customer invoices, payment methods, discounts, and total earnings into an 'Invoices Billing Report' PDF."],
    ["FR-REP-05", "Station Hardware Inventory Report", "MUST", "The system shall compile and export all registered stations, hardware specs, and hourly rates into a 'PC Inventory Report' PDF."],
    ["FR-REP-06", "Universal PDF Exporter Engine", "MUST", "The exportTableToPdf engine shall automatically extract JTable column models, apply high-contrast header styling, alternate row colors, and save files to user home directory."]
]
add_styled_table(m8_headers, m8_rows, [1.1, 1.6, 0.9, 2.9])

# Module 9
add_custom_heading("5.9 Module 9: User Administration, Staff Account Provisioning & Security", level=2)
m9_headers = ["Req ID", "Requirement Title", "Priority", "Detailed Functional Specification"]
m9_rows = [
    ["FR-USR-01", "Staff Account Provisioning", "MUST", "Administrators shall create new staff accounts specifying First Name, Last Name, NIC, Phone, Email, Username, Role (Staff/Admin), and Password."],
    ["FR-USR-02", "Automatic Default Password Assignment", "MUST", "If password fields are left blank during account provisioning, the system shall assign settings.default_password ('1234') hashed via SHA-256 with first_time = 1."],
    ["FR-USR-03", "Administrative Credential Reset", "MUST", "The system shall provide a 'Reset User' action button (btnResetUser) allowing administrators to restore any user's credentials to the default password and reset first_time flag."],
    ["FR-USR-04", "Account Inactivation & Activation", "MUST", "Administrators shall be able to toggle staff account operational states between 'Active' and 'Inactive' to prevent unauthorized shift logins."],
    ["FR-USR-05", "Dynamic Action Button Visibility", "MUST", "Action buttons (btnUpdateUser, btnDeleteUser, btnClearUser, btnResetUser) shall remain hidden until a specific record is selected in tableUser."],
    ["FR-USR-06", "Self-Deletion Prevention", "MUST", "The system shall block administrators from deleting or inactivating their own currently authenticated user account, preventing system lockout."]
]
add_styled_table(m9_headers, m9_rows, [1.1, 1.6, 0.9, 2.9])

doc.add_page_break()

# ====================================================================
# SECTION 6: EXTERNAL INTERFACE REQUIREMENTS
# ====================================================================
add_custom_heading("6. External Interface Requirements", level=1)

add_custom_heading("6.1 Graphical User Interfaces (GUI) & Dynamic Interaction Controls", level=2)
add_body_p("The user interface is developed natively in Java Swing with modern vector FlatLaf skinning. It enforces high visual contrast, intuitive typography, and clear action button gating. Below are visual layout blueprints and mockups of the principal application windows:")

add_image_box("srs_assets/ui_dash_mockup.png", "Central Operational Dashboard with Live Station Grid, KPI Cards, and Session Control Panel", width=Inches(6.2))

add_image_box("srs_assets/ui_bill_mockup.png", "Dedicated Invoicing & Bill Receipt Generation Window (BillFrame)", width=Inches(5.6))

add_image_box("srs_assets/ui_reports_mockup.png", "Multi-Dimensional PDF Business Intelligence & Audit Reporting Dialog", width=Inches(5.6))

add_custom_heading("6.2 Hardware Interfaces & Token / QR Code Instant Session Check-In Scanner", level=2)
add_body_p("The system is engineered to interface seamlessly with standard USB / Bluetooth 2D barcode and QR code scanners acting as Human Interface Devices (HID keyboard wedge). When a customer presents an official membership token or QR pass, the scanner transmits the player token string into the active station dispatch listener, triggering automated station unlocking and session initialization:")

add_image_box("srs_assets/qr_token_workflow.png", "Token & QR Scanner Automated Session Check-In and Station Allocation Workflow", width=Inches(6.2))

add_body_p("Upon scanning, the token string is validated against customer membership records or prepaid balances. If valid, the system automatically allocates the next available high-tier gaming PC or PS5, sets the station status to 'Occupied', and begins the countdown timer without manual cashier data entry.", bold_prefix="• Automated Player Pass Validation: ")
add_body_p("Thermal receipt printers (58mm / 80mm ESC/POS) and standard laser printers are supported via standard Java Print Service (javax.print) and iText PDF export.", bold_prefix="• Point-of-Sale (POS) Receipt Printer Interface: ")

add_custom_heading("6.3 Software & Driver Interfaces", level=2)
add_body_p("1. Java Database Connectivity (JDBC): com.mysql.cj.jdbc.Driver establishes robust connection pooling with the MySQL database instance on port 3306.\n2. FlatLaf Look-and-Feel Engine: com.formdev.flatlaf.FlatIntelliJLaf renders modern high-DPI UI components, smooth corners, and custom vector icons.\n3. iText PDF Engine / JasperReports: Generates vector-sharp itemized PDF billing receipts and multi-table business audit reports.")

add_custom_heading("6.4 Communications & Network Protocols", level=2)
add_body_p("The system communicates with the MySQL database instance over standard TCP/IP socket connections (default port 3306). Localhost loopback sockets (127.0.0.1) are utilized for single-workstation standalone deployments, while standard Local Area Network (LAN) intranet IP addresses (e.g., 192.168.1.100) are fully supported for multi-counter cafe setups.")

doc.add_page_break()

# ====================================================================
# SECTION 7: NON-FUNCTIONAL REQUIREMENTS (NFR)
# ====================================================================
add_custom_heading("7. Non-Functional Requirements (NFR)", level=1)

add_custom_heading("7.1 Performance & Scalability Benchmarks", level=2)
nfr_headers = ["NFR ID", "Quality Characteristic", "Measurable Target / Benchmark"]
nfr_rows = [
    ["NFR-PERF-01", "Query Retrieval Latency", "Station grid and session data queries shall execute and render in < 250 ms under standard database loads (≤ 50,000 historic records)."],
    ["NFR-PERF-02", "Authentication Latency", "SHA-256 cryptographic password verification and session token generation shall complete in < 200 ms."],
    ["NFR-PERF-03", "Countdown Timer Accuracy", "Per-second session countdown timer ticks shall deviate by less than ±50 milliseconds per hour of operation."],
    ["NFR-PERF-04", "PDF Compilation Throughput", "Compiling and exporting a 100-row tabular PDF report via iText shall complete in < 1.5 seconds."]
]
add_styled_table(nfr_headers, nfr_rows, [1.3, 1.8, 3.4])

add_custom_heading("7.2 Reliability, Availability & Disaster Recovery", level=2)
add_body_p("All session creation, time extension, and billing operations execute within ACID-compliant database transaction boundaries. In the event of an unexpected power failure or network disruption, uncommitted transactions rollback automatically without data corruption.", bold_prefix="• ACID Transactional Guarantees: ")
add_body_p("The database connection manager (db.java) continuously verifies connection validity (con.isClosed()) and automatically re-establishes broken sockets before raising runtime SQL exceptions.", bold_prefix="• Automatic Socket Reconnection: ")
add_body_p("All caught runtime errors and system exceptions are logged with full stack traces for administrative diagnostics.", bold_prefix="• Comprehensive Exception Diagnostics: ")

add_custom_heading("7.3 Information Security & Data Protection", level=2)
add_body_p("Passwords are irreversibly digested using NIST FIPS 180-4 compliant SHA-256. Plain text credentials never touch database tables.", bold_prefix="• Cryptographic Password Storage: ")
add_body_p("Dynamic string concatenation in SQL statements is strictly prohibited. All queries with external user input utilize parameterized PreparedStatement objects to completely eliminate SQL injection attacks.", bold_prefix="• Complete SQL Injection Neutralization: ")
add_body_p("Staff accounts are restricted from accessing user account provisioning, system settings, or deleting historic financial invoices.", bold_prefix="• Strict Role Privilege Separation: ")

add_custom_heading("7.4 Software Quality Attributes", level=2)
add_body_p("High-contrast visual cues, consistent 20px flat iconography, clear status badges, and instant confirmation dialogs ensure frictionless operation by non-technical cashiers.", bold_prefix="• Usability & Ergonomics: ")
add_body_p("Modular Java codebase following clean object-oriented architecture with comprehensive English documentation and zero inline fully-qualified package namespaces.", bold_prefix="• Maintainability: ")
add_body_p("Standard Java bytecode executes reliably across Microsoft Windows, Linux, and macOS platforms without source code modifications.", bold_prefix="• Cross-Platform Portability: ")

doc.add_page_break()

# ====================================================================
# SECTION 8: SYSTEM VERIFICATION & TEST CASES
# ====================================================================
add_custom_heading("8. System Verification, Validation & Test Cases", level=1)

add_custom_heading("8.1 Requirements Traceability Matrix (RTM)", level=2)
rtm_headers = ["Requirement ID", "Module / Feature Description", "Associated Test Case ID(s)", "Verification Status"]
rtm_rows = [
    ["FR-AUTH-01", "User Authentication & SHA-256 Verification", "TC-AUTH-01, TC-AUTH-02", "VERIFIED / PASS"],
    ["FR-AUTH-02, 03", "First-Time Login Mandatory Password Reset", "TC-AUTH-03", "VERIFIED / PASS"],
    ["FR-DASH-01..03", "Live Station Grid & Real-time KPI Telemetry", "TC-DASH-01, TC-DASH-02", "VERIFIED / PASS"],
    ["FR-SES-01..03", "Duration Allocation & Auto Price Calculation", "TC-SES-01, TC-SES-02", "VERIFIED / PASS"],
    ["FR-SES-04", "Dynamic in-session '+ Add Time' Extension", "TC-EXT-01, TC-EXT-02", "VERIFIED / PASS"],
    ["FR-BILL-01..06", "Dedicated BillFrame & iText PDF Receipt Export", "TC-BILL-01, TC-BILL-02", "VERIFIED / PASS"],
    ["FR-REP-01..04", "Station Defect Dispatch & Repair Resolution", "TC-REP-01, TC-REP-02", "VERIFIED / PASS"],
    ["FR-USR-01..06", "Staff User Provisioning & Credential Reset", "TC-USR-01, TC-USR-02", "VERIFIED / PASS"]
]
add_styled_table(rtm_headers, rtm_rows, [1.3, 2.3, 1.6, 1.3])

add_custom_heading("8.2 Comprehensive Test Scenarios & Verified Results", level=2)

add_custom_heading("Test Case TC-AUTH-01: Valid Staff / Admin Authentication", level=3)
add_body_p("Database contains active user record (username: 'admin', role: 'Admin').", bold_prefix="• Preconditions: ")
add_body_p("Enter Username: 'admin', Password: 'correct_password', click Login.", bold_prefix="• Test Input: ")
add_body_p("System hashes input via SHA-256, matches hash, records login timestamp in user_logs, displays 'Login Successful', and opens Dashboard with full admin privileges.", bold_prefix="• Expected Result: ")
add_body_p("PASS - Credentials authenticated successfully, audit log inserted, Dashboard displayed.", bold_prefix="• Actual Result: ")

add_custom_heading("Test Case TC-SES-01: Session Allocation & Auto Price Calculation", level=3)
add_body_p("Station 'PC-01' is 'Available' with hourly rate of Rs. 200.00.", bold_prefix="• Preconditions: ")
add_body_p("Select 'PC-01', choose duration '2 Hours (120 mins)' from cmbSessionDuration.", bold_prefix="• Test Input: ")
add_body_p("System auto-calculates estimated charge: (120/60.0)*200 = Rs. 400.00, sets timer box to '02:00:00', and reveals '▶ Start Session' button.", bold_prefix="• Expected Result: ")
add_body_p("PASS - Price displayed as 'Rs. 400.00', timer initialized to '02:00:00', station ready for dispatch.", bold_prefix="• Actual Result: ")

add_custom_heading("Test Case TC-EXT-01: Dynamic '+ Add Time' During Ongoing Match", level=3)
add_body_p("Station 'PC-01' is 'Occupied' with active session SES-001 (allocated 60 mins).", bold_prefix="• Preconditions: ")
add_body_p("Click '⏱ + Add Time', select '+1 Hour (60 mins)' option.", bold_prefix="• Test Input: ")
add_body_p("System updates database (add_minutes = 60, total_minutes = 120, total_amount = Rs. 400.00), extends countdown timer by +3600 seconds, and refreshes Live Sessions table.", bold_prefix="• Expected Result: ")
add_body_p("PASS - Additional 60 minutes added atomically, countdown timer extended, total charge updated.", bold_prefix="• Actual Result: ")

add_custom_heading("Test Case TC-BILL-01: Session Termination & PDF Invoice Generation", level=3)
add_body_p("Station 'PC-01' session completed; player approaches counter.", bold_prefix="• Preconditions: ")
add_body_p("Click '⏹ End Session & Bill' in Dashboard, verify BillFrame details, click 'Print / Save PDF Bill'.", bold_prefix="• Test Input: ")
add_body_p("System terminates session, sets station status to 'Available', opens BillFrame with full breakdown, inserts record into invoices, compiles official PDF invoice, and prompts to open PDF.", bold_prefix="• Expected Result: ")
add_body_p("PASS - BillFrame displayed, station marked available, PDF invoice exported successfully.", bold_prefix="• Actual Result: ")

doc.add_page_break()

# ====================================================================
# SECTION 9: APPENDICES, GLOSSARY & FUTURE ROADMAP
# ====================================================================
add_custom_heading("9. Appendices, Glossary & Future Strategic Roadmap", level=1)

add_custom_heading("9.1 Glossary of Technical Terms & Acronyms", level=2)
glossary_headers = ["Term / Acronym", "Full Definition & Context in this System"]
glossary_rows = [
    ["SRS", "Software Requirements Specification: Formal document specifying all functional, operational, and behavioral requirements."],
    ["SDLC", "Software Development Life Cycle: Structured governance methodology (Waterfall Model) applied in this project."],
    ["GUI", "Graphical User Interface: Visual desktop front-end constructed via Java Swing and NetBeans Form Builder."],
    ["JDBC", "Java Database Connectivity: Industry standard Java API for executing SQL queries and managing relational transactions."],
    ["SHA-256", "Secure Hash Algorithm 256-bit: Cryptographic one-way hash function used to digest and secure passwords."],
    ["FlatLaf", "Flat Look and Feel: Modern, high-DPI compatible Swing look-and-feel library providing clean desktop aesthetics."],
    ["NIC", "National Identity Card: Government-issued citizen identity number validated using statutory regex formats."],
    ["MoSCoW", "Must / Should / Could / Won't: Standard requirements prioritization framework used across all functional specifications."],
    ["RTM", "Requirements Traceability Matrix: Grid mapping user requirements to system design components and verification test cases."],
    ["UAT", "User Acceptance Testing: Final operational verification conducted with actual counter cashiers and administrative staff."],
    ["POS", "Point of Sale: Counter hardware ecosystem including thermal receipt printers and barcode/QR scanners."]
]
add_styled_table(glossary_headers, glossary_rows, [1.5, 5.0])

add_custom_heading("9.2 Future Strategic Enhancements & Web/Cloud Roadmap", level=2)
add_body_p("While the present desktop release delivers comprehensive station, timing, hardware, billing, and reporting capabilities, the modular architecture was designed to accommodate future strategic expansions:")
add_body_p("Integration with cloud-hosted MySQL clusters (e.g., AWS RDS / Google Cloud SQL) enabling enterprise esports franchise owners to monitor multiple gaming branches from a central analytics dashboard.", bold_prefix="• 1. Cloud-Synchronized Multi-Branch Architecture: ")
add_body_p("A responsive web and mobile player portal allowing gamers to browse available PC/PS5 stations, check live seat availability, and book gaming slots remotely.", bold_prefix="• 2. Web & Mobile Online Seat Reservation Portal: ")
add_body_p("Integration with digital wallet platforms (LankaQR, Stripe, PayPal, eZ Cash) allowing players to pay directly from mobile apps upon token scan.", bold_prefix="• 3. Integrated Mobile Cashless QR Payments: ")
add_body_p("Automated client-side gaming station daemon that automatically syncs player Steam / Epic Games credentials, game saves, and tournament leaderboards upon session login.", bold_prefix="• 4. Steam / Epic Games Platform API Integration: ")

p_end = doc.add_paragraph()
p_end.alignment = WD_ALIGN_PARAGRAPH.CENTER
p_end.paragraph_format.space_before = Pt(40)
r_end = p_end.add_run("--- END OF SOFTWARE REQUIREMENTS SPECIFICATION DOCUMENT ---\nGaming Cafe & Esports Arena Management System • Version 1.0 • Baselined September 2026")
r_end.font.name = "Arial"
r_end.font.size = Pt(9.5)
r_end.font.bold = True
r_end.font.color.rgb = COLOR_MUTED

# Save document
output_path = "SRS_Gaming_Cafe_Management_System_Final.docx"
try:
    doc.save("SRS_Gaming_Cafe_Management_System.docx")
    print("Saved to SRS_Gaming_Cafe_Management_System.docx")
except Exception as e:
    print(f"Could not overwrite SRS_Gaming_Cafe_Management_System.docx (file open in Word): {e}")

doc.save(output_path)
print(f"Successfully generated comprehensive SRS document at: {os.path.abspath(output_path)}")
