import os
import sys
from reportlab.lib import colors
from reportlab.lib.pagesizes import letter, A4
from reportlab.lib.units import inch
from reportlab.lib.styles import getSampleStyleSheet, ParagraphStyle
from reportlab.platypus import (
    SimpleDocTemplate, Paragraph, Spacer, Table, TableStyle, PageBreak, Image, KeepTogether, HRFlowable
)
from reportlab.pdfgen import canvas

class NumberedCanvas(canvas.Canvas):
    def __init__(self, *args, **kwargs):
        super(NumberedCanvas, self).__init__(*args, **kwargs)
        self._saved_page_states = []

    def showPage(self):
        self._saved_page_states.append(dict(self.__dict__))
        self._startPage()

    def save(self):
        num_pages = len(self._saved_page_states)
        for state in self._saved_page_states:
            self.__dict__.update(state)
            self.draw_page_number(num_pages)
            canvas.Canvas.showPage(self)
        canvas.Canvas.save(self)

    def draw_page_number(self, page_count):
        if self._pageNumber == 1:
            return  # Skip cover page
        
        self.saveState()
        self.setFont("Helvetica", 8)
        self.setFillColor(colors.HexColor("#64748b"))
        
        # Header text
        self.drawString(54, 795, "STANDARD IEEE 830-1998 / ISO/IEC/IEEE 29148 COMPLIANT SRS")
        self.drawRightString(558, 795, "GAMING CAFE MANAGEMENT SYSTEM")
        self.setStrokeColor(colors.HexColor("#cbd5e1"))
        self.setLineWidth(0.5)
        self.line(54, 790, 558, 790)
        
        # Footer text
        self.line(54, 45, 558, 45)
        self.drawString(54, 34, "Confidential - Baselined System Specification  •  Version 1.0")
        page_str = f"Page {self._pageNumber} of {page_count}"
        self.drawRightString(558, 34, page_str)
        self.restoreState()

def build_pdf(filename="SRS_Gaming_Cafe_Management_System.pdf"):
    doc = SimpleDocTemplate(
        filename,
        pagesize=A4,
        leftMargin=54,
        rightMargin=54,
        topMargin=54,
        bottomMargin=54
    )

    styles = getSampleStyleSheet()
    
    # Custom styles
    primary_color = colors.HexColor("#003366")
    accent_blue = colors.HexColor("#0284c7")
    dark_text = colors.HexColor("#1e293b")
    
    style_cover_badge = ParagraphStyle(
        'CoverBadge',
        fontName='Helvetica-Bold',
        fontSize=10,
        leading=14,
        textColor=accent_blue,
        alignment=1, # Center
        spaceAfter=15
    )
    
    style_cover_sub = ParagraphStyle(
        'CoverSub',
        fontName='Helvetica-Bold',
        fontSize=14,
        leading=18,
        textColor=primary_color,
        alignment=1,
        spaceAfter=25
    )
    
    style_cover_title = ParagraphStyle(
        'CoverTitle',
        fontName='Helvetica-Bold',
        fontSize=24,
        leading=30,
        textColor=primary_color,
        alignment=1,
        spaceAfter=20
    )
    
    style_cover_author = ParagraphStyle(
        'CoverAuthor',
        fontName='Helvetica',
        fontSize=12,
        leading=18,
        textColor=dark_text,
        alignment=1
    )
    
    style_h1 = ParagraphStyle(
        'SectionH1',
        fontName='Helvetica-Bold',
        fontSize=15,
        leading=19,
        textColor=primary_color,
        spaceBefore=16,
        spaceAfter=8,
        keepWithNext=True
    )
    
    style_h2 = ParagraphStyle(
        'SectionH2',
        fontName='Helvetica-Bold',
        fontSize=12,
        leading=16,
        textColor=accent_blue,
        spaceBefore=12,
        spaceAfter=6,
        keepWithNext=True
    )
    
    style_h3 = ParagraphStyle(
        'SectionH3',
        fontName='Helvetica-Bold',
        fontSize=10,
        leading=14,
        textColor=dark_text,
        spaceBefore=8,
        spaceAfter=4,
        keepWithNext=True
    )
    
    style_body = ParagraphStyle(
        'BodyDark',
        fontName='Helvetica',
        fontSize=9,
        leading=13.5,
        textColor=dark_text,
        spaceAfter=5
    )
    
    style_bullet = ParagraphStyle(
        'BulletDark',
        fontName='Helvetica',
        fontSize=9,
        leading=13.5,
        textColor=dark_text,
        leftIndent=15,
        spaceAfter=4
    )
    
    style_callout = ParagraphStyle(
        'CalloutText',
        fontName='Helvetica',
        fontSize=8.5,
        leading=12.5,
        textColor=colors.HexColor("#0c4a6e")
    )

    style_table_header = ParagraphStyle(
        'TblHdr',
        fontName='Helvetica-Bold',
        fontSize=8.5,
        leading=11,
        textColor=colors.white
    )
    
    style_table_cell = ParagraphStyle(
        'TblCell',
        fontName='Helvetica',
        fontSize=8,
        leading=10.5,
        textColor=dark_text
    )

    style_caption = ParagraphStyle(
        'ImgCaption',
        fontName='Helvetica-Oblique',
        fontSize=8,
        leading=11,
        textColor=colors.HexColor("#64748b"),
        alignment=1,
        spaceBefore=4,
        spaceAfter=10
    )

    story = []

    # =========================================================================
    # COVER PAGE
    # =========================================================================
    story.append(Spacer(1, 40))
    
    # Blue top bar box
    tbl_badge = Table(
        [[Paragraph("STANDARD IEEE 830-1998 / ISO/IEC/IEEE 29148 COMPLIANT", style_cover_badge)]],
        colWidths=[490]
    )
    tbl_badge.setStyle(TableStyle([
        ('BOX', (0,0), (-1,-1), 1, colors.HexColor("#0284c7")),
        ('BACKGROUND', (0,0), (-1,-1), colors.HexColor("#f0f9ff")),
        ('TOPPADDING', (0,0), (-1,-1), 8),
        ('BOTTOMPADDING', (0,0), (-1,-1), 8),
    ]))
    story.append(tbl_badge)
    story.append(Spacer(1, 40))
    
    story.append(Paragraph("SOFTWARE REQUIREMENTS SPECIFICATION (SRS)", style_cover_sub))
    story.append(Spacer(1, 30))
    story.append(Paragraph("GAMING CAFE & ESPORTS ARENA<br/>MANAGEMENT SYSTEM", style_cover_title))
    story.append(Spacer(1, 15))
    story.append(Paragraph("Real-Time Station Allocation, Live Countdown Billing Engine, Hardware Inventory, Defect Tracking & Multi-Dimensional PDF Reporting", ParagraphStyle('SubDesc', fontName='Helvetica-Oblique', fontSize=10, leading=15, textColor=colors.HexColor("#64748b"), alignment=1)))
    story.append(Spacer(1, 180))
    story.append(Paragraph("<b>Author / Lead Architect:</b> Damsith Dewmina<br/><b>Technology Stack:</b> Java SE (Swing / FlatLaf) & MySQL RDBMS<br/><b>Document Version:</b> 1.0 (Baselined Release)<br/><b>Date:</b> September 2026", style_cover_author))
    story.append(PageBreak())

    # =========================================================================
    # TABLE OF CONTENTS
    # =========================================================================
    story.append(Paragraph("Contents", style_h1))
    story.append(HRFlowable(width="100%", thickness=1.5, color=primary_color, spaceAfter=12))

    toc_data = [
        ("1. Introduction", "4"),
        ("    1.1 Purpose of this Document", "4"),
        ("    1.2 Document Conventions & Prioritization Rules (MoSCoW)", "4"),
        ("    1.3 Intended Audience & Reading Suggestions", "4"),
        ("    1.4 Project Background & Problem Statement", "4"),
        ("    1.5 The Engineered Solution", "5"),
        ("    1.6 Project Scope & Operational Boundaries", "5"),
        ("    1.7 Applicable Standards & Industry References", "5"),
        ("2. Software Development Life Cycle (SDLC) - Waterfall Model Application", "6"),
        ("    2.1 Justification for the Waterfall SDLC Selection", "6"),
        ("    2.2 Detailed Waterfall Phase Breakdown (Phases 1 to 6)", "6"),
        ("    2.3 Waterfall Milestones, Deliverables & Quality Gates Table", "7"),
        ("3. Overall System Description", "8"),
        ("    3.1 Product Perspective & Context", "8"),
        ("    3.2 3-Tier Layered Architecture Pattern", "8"),
        ("    3.3 User Classes, Personas & Operational Responsibilities", "9"),
        ("    3.4 Operational Environment & System Requirements", "9"),
        ("    3.5 Design & Implementation Constraints", "10"),
        ("4. System Architecture, Unified Modeling Language (UML) & Data Models", "11"),
        ("    4.1 Use Case Diagram & Actor Responsibilities", "11"),
        ("    4.2 Class Diagram & Object-Oriented Domain Model", "12"),
        ("    4.3 Sequence Diagram: Session Booking, Live Countdown & Billing Lifecycle", "13"),
        ("    4.4 Activity Diagram: Operational Control Flow & Decision Logic", "14"),
        ("    4.5 State Machine Diagram: Station Finite State Transitions", "15"),
        ("    4.6 Relational Database Schema & Data Dictionaries (Production Tables)", "16"),
        ("        Table 1: user (Staff & Admin Accounts)", "16"),
        ("        Table 2: user_logs (Staff Audit Trail)", "16"),
        ("        Table 3: pc_table (Hardware Stations Inventory)", "17"),
        ("        Table 4: gaming_sessions (Play Sessions & Time Extensions)", "17"),
        ("        Table 5: pc_maintenance (Defect & Repair Tracking)", "18"),
        ("        Table 6: invoices (Billing Invoices & Revenue Settlement)", "18"),
        ("    4.7 Real-Time Session Countdown & Dynamic Add-Time Algorithms", "19"),
        ("    4.8 Cryptographic Security Architecture & Password Lifecycle", "19"),
        ("5. Specific Functional Requirements (Modules 1 to 9)", "20"),
        ("    5.1 Module 1: Authentication & Access Control", "20"),
        ("    5.2 Module 2: Central Operational Dashboard & Real-Time KPI Telemetry", "20"),
        ("    5.3 Module 3: Live Gaming Sessions & Active Station Monitoring", "21"),
        ("    5.4 Module 4: Session Duration Allocation & Dynamic Time Extension", "22"),
        ("    5.5 Module 5: Station & Hardware Inventory Management", "22"),
        ("    5.6 Module 6: Hardware Maintenance, Defect Logging & Repair History", "23"),
        ("    5.7 Module 7: Invoicing, Billing Engine & PDF Receipt Export", "24"),
        ("    5.8 Module 8: Business Intelligence & Multi-Category PDF Reporting", "24"),
        ("    5.9 Module 9: User Administration & Security Provisioning", "25"),
        ("6. External Interface Requirements", "26"),
        ("    6.1 Graphical User Interfaces (GUI) & Dynamic Controls", "26"),
        ("    6.2 Hardware Interfaces & Token / QR Code Instant Session Check-in Scanner", "27"),
        ("    6.3 Software & Driver Interfaces (JDBC, FlatLaf, iText / Jasper PDF)", "27"),
        ("    6.4 Communications & Network Protocols", "27"),
        ("7. Non-Functional Requirements (NFR)", "28"),
        ("    7.1 Performance & Scalability Benchmarks", "28"),
        ("    7.2 Reliability, Availability & Disaster Recovery", "28"),
        ("    7.3 Information Security & Data Protection", "28"),
        ("    7.4 Software Quality Attributes", "29"),
        ("8. System Verification, Validation & Test Cases", "29"),
        ("    8.1 Requirements Traceability Matrix (RTM)", "29"),
        ("    8.2 Comprehensive Test Scenarios & Verified Results", "30"),
        ("9. Appendices & Glossary", "31"),
        ("    9.1 Glossary of Technical Terms & Acronyms", "31"),
        ("    9.2 Future Strategic Enhancements & Cloud/Web Roadmap", "32")
    ]

    for item, pg in toc_data:
        t_row = Table(
            [[Paragraph(item, style_body), Paragraph(pg, ParagraphStyle('TRight', fontName='Helvetica-Bold', fontSize=9, textColor=primary_color, alignment=2))]],
            colWidths=[450, 40]
        )
        t_row.setStyle(TableStyle([
            ('BOTTOMPADDING', (0,0), (-1,-1), 1),
            ('TOPPADDING', (0,0), (-1,-1), 1),
        ]))
        story.append(t_row)

    story.append(PageBreak())

    # =========================================================================
    # SECTION 1: INTRODUCTION
    # =========================================================================
    story.append(Paragraph("1. Introduction", style_h1))
    story.append(HRFlowable(width="100%", thickness=1, color=primary_color, spaceAfter=8))

    story.append(Paragraph("1.1 Purpose of this Document", style_h2))
    story.append(Paragraph("This Software Requirements Specification (SRS) document details the comprehensive functional, non-functional, behavioral, and architectural requirements for the <b>Gaming Cafe & Esports Arena Management System</b>. This document acts as the definitive contract between stakeholders, project evaluators, software architects, and quality assurance personnel. It adheres strictly to the <b>IEEE Std 830-1998</b> (Recommended Practice for Software Requirements Specifications) and its modern harmonized successor <b>ISO/IEC/IEEE 29148:2018</b>.", style_body))

    story.append(Paragraph("1.2 Document Conventions & Prioritization Rules", style_h2))
    story.append(Paragraph("This specification adheres to standardized typographical and structural conventions:", style_body))
    story.append(Paragraph("• <b>Requirement Identifiers:</b> Each requirement is tagged with a unique alphanumeric identifier (e.g., FR-AUTH-01, FR-SES-02, NFR-PERF-01) establishing unambiguous traceability.", style_bullet))
    story.append(Paragraph("• <b>MoSCoW Prioritization Scheme:</b>", style_bullet))
    story.append(Paragraph("&nbsp;&nbsp;&nbsp;&nbsp;o <b>MUST (High):</b> Absolute mandatory operational requirements. Non-negotiable for system deployment.", style_bullet))
    story.append(Paragraph("&nbsp;&nbsp;&nbsp;&nbsp;o <b>SHOULD (Medium):</b> Highly desirable operational features that enhance efficiency but do not prevent core operation.", style_bullet))
    story.append(Paragraph("&nbsp;&nbsp;&nbsp;&nbsp;o <b>COULD (Low):</b> Optional convenience enhancements implemented if time and resources permit.", style_bullet))
    story.append(Paragraph("• <b>Database & Code Symbols:</b> Database tables, column identifiers, classes, and SQL statements are represented in monospaced bold font (e.g., <b>gaming_sessions.add_minutes</b>, <b>pc_table.hourly_rate</b>).", style_bullet))

    story.append(Paragraph("1.3 Intended Audience & Reading Suggestions", style_h2))
    story.append(Paragraph("• <b>Academic Examiners & Evaluators:</b> Review Section 2 for the rigorous SDLC Waterfall methodology mapping, Section 4 for complete UML diagrams and relational data architecture, and Section 8 for verification test cases.", style_bullet))
    story.append(Paragraph("• <b>Software Developers & Maintainers:</b> Focus on Section 4 (Data Dictionaries, SQL relations, and security hashing) and Section 5 (Detailed Functional Specifications).", style_bullet))
    story.append(Paragraph("• <b>System Administrators & Operations Personnel:</b> Focus on Section 3 (Operational Environment), Section 5.9 (User Provisioning), and Section 7 (Security and Data Integrity).", style_bullet))

    story.append(Paragraph("1.4 Project Background & Problem Statement", style_h2))
    story.append(Paragraph("Modern gaming cafes and competitive esports arenas face significant operational bottlenecks when managing dozens of high-performance gaming stations, PlayStation 5 consoles, hourly rental rates, and concurrent customer sessions. The manual, paper-register, or uncoordinated spreadsheet systems traditionally deployed suffer from critical deficiencies:", style_body))
    story.append(Paragraph("• <b>Session Timing Inaccuracies & Revenue Leakage:</b> Manual recordkeeping makes it difficult to track remaining gameplay time precisely, resulting in unpaid overtime play and financial loss.", style_bullet))
    story.append(Paragraph("• <b>Cumbersome Mid-Session Extensions:</b> When gamers request extra time during an ongoing match, manual paper systems fail to update remaining countdowns and total bills dynamically.", style_bullet))
    story.append(Paragraph("• <b>Untracked Hardware Assets & Defect Dispatches:</b> Station hardware specifications (CPU, GPU, RAM, Motherboards) and maintenance status lack structured tracking, leading to accidental allocation of damaged PCs.", style_bullet))
    story.append(Paragraph("• <b>Security & Audit Vulnerabilities:</b> Physical logbooks lack role-based access control, cryptographic password protection, and tamper-resistant cashier shift audit trails.", style_bullet))

    # Engineered Solution Callout
    story.append(Spacer(1, 4))
    tbl_sol = Table(
        [[Paragraph("<b>The Engineered Solution</b><br/>The Gaming Cafe Management System replaces fragmented manual processes with an integrated, high-performance desktop platform built on Java Swing, FlatLaf modern UI, and MySQL relational database, delivering real-time countdown tracking, dynamic session extension, cryptographic authentication, and multi-table PDF reporting.", style_callout)]],
        colWidths=[490]
    )
    tbl_sol.setStyle(TableStyle([
        ('BOX', (0,0), (-1,-1), 1, colors.HexColor("#0284c7")),
        ('BACKGROUND', (0,0), (-1,-1), colors.HexColor("#f0f9ff")),
        ('TOPPADDING', (0,0), (-1,-1), 6),
        ('BOTTOMPADDING', (0,0), (-1,-1), 6),
        ('LEFTPADDING', (0,0), (-1,-1), 10),
    ]))
    story.append(tbl_sol)
    story.append(Spacer(1, 6))

    story.append(Paragraph("1.5 Project Scope & Operational Boundaries", style_h2))
    story.append(Paragraph("The system establishes an automated, desktop-based management ecosystem encapsulating the following major capabilities:", style_body))
    story.append(Paragraph("• <b>Secure Role-Based Authentication:</b> Segregated administrative and operational access using SHA-256 password cryptography, dynamic default password assignment, and mandatory first-time password resets.", style_bullet))
    story.append(Paragraph("• <b>Interactive Station Grid & KPI Telemetry:</b> Real-time graphical station cards (PC-01 to PC-08, PS5 consoles) with color-coded status badges (Available = Green, Occupied = Red, Repair = Orange).", style_bullet))
    story.append(Paragraph("• <b>Session Duration & Real-Time Countdown:</b> Standard duration presets (1h, 1.5h, 2h, 3h, 4h, 5h, 6h, Custom min 60m) with dynamic hourly rate calculation and per-second remaining countdown timer.", style_bullet))
    story.append(Paragraph("• <b>Dynamic In-Session Time Extension:</b> Mid-session '+ Add Time' dialog (+30m, +1h, +2h, Custom) updating the active timer and database records atomically.", style_bullet))
    story.append(Paragraph("• <b>Hardware Asset Inventory & Maintenance Tracking:</b> Complete tracking of station specifications (CPU, Motherboard, RAM, GPU, Category, Rate) and defect repair lifecycle.", style_bullet))
    story.append(Paragraph("• <b>Dedicated Invoicing & PDF Receipts:</b> Standalone BillFrame window computing cash/card payments, discounts, balance change, and generating iText PDF receipts.", style_bullet))
    story.append(Paragraph("• <b>Multi-Category PDF Reporting Suite:</b> Automated export of user activity logs, session history, billing revenue, station inventory, and maintenance logs.", style_bullet))

    story.append(Paragraph("1.6 Applicable Standards & Industry References", style_h2))
    story.append(Paragraph("1. <b>IEEE Std 830-1998:</b> IEEE Recommended Practice for Software Requirements Specifications.<br/>2. <b>ISO/IEC/IEEE 29148:2018:</b> Systems and Software Engineering — Life Cycle Processes — Requirements Engineering.<br/>3. <b>NIST FIPS 180-4:</b> Secure Hash Standard (SHS) for SHA-256 cryptographic hashing algorithms.<br/>4. <b>Oracle Java Platform, Standard Edition (Java SE):</b> Version 21 LTS specifications.<br/>5. <b>MySQL 8.0 Reference Manual:</b> Relational Database Management System (RDBMS) ACID transaction compliance.", style_body))

    story.append(PageBreak())

    # =========================================================================
    # SECTION 2: SDLC WATERFALL MODEL APPLICATION
    # =========================================================================
    story.append(Paragraph("2. Software Development Life Cycle (SDLC) - Waterfall Model Application", style_h1))
    story.append(HRFlowable(width="100%", thickness=1, color=primary_color, spaceAfter=8))

    story.append(Paragraph("2.1 Justification for the Waterfall SDLC Selection", style_h2))
    story.append(Paragraph("The classical Waterfall Model was selected as the governance lifecycle methodology for this enterprise gaming cafe management system. Unlike iterative frameworks that introduce continuous scope flux, the gaming cafe domain is characterized by well-established, non-volatile operational rules, rigid hardware relationships, and clear sequential dependencies:", style_body))
    story.append(Paragraph("• <b>Predictable, Well-Understood Problem Domain:</b> Gaming cafe workflows (station selection → duration allocation → gameplay countdown → session termination → billing settlement) are linear and deterministic.", style_bullet))
    story.append(Paragraph("• <b>Rigid Relational Dependencies:</b> Relational database schemas (foreign keys linking user, user_logs, pc_table, gaming_sessions, pc_maintenance, and invoices) must be fully modeled and normalized to Third Normal Form (3NF) before presentation forms or business validation logic can be written.", style_bullet))
    story.append(Paragraph("• <b>Quality Assurance & Verification Rigor:</b> Real-time session timing and financial billing calculation require formal algorithm verification before deployment, matching Waterfall's distinct phase-gate milestones.", style_bullet))

    story.append(Paragraph("2.2 Detailed Waterfall Phase Breakdown", style_h2))
    story.append(Paragraph("<b>Phase 1: Requirements Analysis & Elicitation:</b> Extensive domain analysis and stakeholder interviews were conducted with gaming cafe owners, counter staff, and tournament coordinators. Operational pain points were extracted and formalized into this IEEE 830 compliant SRS document.", style_body))
    story.append(Paragraph("<b>Phase 2: Architectural & Detailed System Design:</b> The architectural blueprint decomposed the application into a 3-Tier Layered Architecture (Presentation, Business Logic, and Data Access). Detailed database schemas with strict primary and foreign key constraints were designed. Form blueprints were structured in NetBeans GUI Builder XML (.form).", style_body))
    story.append(Paragraph("<b>Phase 3: Implementation, Coding & Security Engineering:</b> Physical development took place in Java utilizing NetBeans IDE and Ant Build Tooling. Clean coding standards were enforced with top-level imports only, strict exception logging, SHA-256 one-way hashing, FlatLaf modern UI integration, and parameterized JDBC PreparedStatements.", style_body))
    story.append(Paragraph("<b>Phase 4: Integration, Verification & System Testing:</b> Individual modules were integrated and subjected to unit testing (NIC regex, phone numbers, duration bounds), integration testing (timer synchronization, add-time DB updates), and security audits.", style_body))
    story.append(Paragraph("<b>Phase 5: Deployment, Configuration & UAT:</b> The software was packaged into an executable JAR archive with all bundled dependencies (flatlaf, mysql-connector-j, itextpdf). The MySQL schema was initialized, and User Acceptance Testing (UAT) was executed with counter staff.", style_body))
    story.append(Paragraph("<b>Phase 6: Maintenance, Support & Operational Evolution:</b> Corrective maintenance (bug fixing), adaptive maintenance (updating hardware tariffs or OS scaling), and perfective maintenance (cloud multi-branch sync and QR token integration).", style_body))

    story.append(Paragraph("2.3 Waterfall Milestones, Deliverables & Quality Gates Table", style_h2))
    
    def create_reportlab_table(headers, rows, widths):
        data = [[Paragraph(h, style_table_header) for h in headers]]
        for r in rows:
            data.append([Paragraph(str(c), style_table_cell) for c in r])
        t = Table(data, colWidths=widths)
        t.setStyle(TableStyle([
            ('BACKGROUND', (0,0), (-1,0), primary_color),
            ('ALIGN', (0,0), (-1,-1), 'LEFT'),
            ('VALIGN', (0,0), (-1,-1), 'TOP'),
            ('TOPPADDING', (0,0), (-1,-1), 4),
            ('BOTTOMPADDING', (0,0), (-1,-1), 4),
            ('LEFTPADDING', (0,0), (-1,-1), 5),
            ('RIGHTPADDING', (0,0), (-1,-1), 5),
            ('ROWBACKGROUNDS', (0,1), (-1,-1), [colors.HexColor("#ffffff"), colors.HexColor("#f8fafc")]),
            ('GRID', (0,0), (-1,-1), 0.5, colors.HexColor("#cbd5e1")),
        ]))
        return t

    wf_hdrs = ["Waterfall Phase", "Inputs", "Key Engineering Activities", "Outputs / Deliverables", "Quality Review Gate"]
    wf_rows_data = [
        ["Phase 1: Requirements", "Stakeholder interviews, legacy paper logs", "Problem analysis, constraint elicitation, operational modeling, MoSCoW prioritization", "Baselined SRS Document (IEEE 830), Feasibility Report", "Requirements Review Milestone (SRS Sign-off)"],
        ["Phase 2: Design", "Baselined SRS Document", "3-Tier architectural partitioning, ERD normalization (3NF), GUI wireframing", "System Design Document (SDD), Database DDL Scripts, UI Form Blueprints", "Preliminary & Critical Design Reviews (PDR/CDR)"],
        ["Phase 3: Coding", "SDD, DB DDL Scripts, UI Wireframes", "Java Swing development, FlatLaf styling, JDBC integration, SHA-256 crypto implementation", "Compiled Source Code (.java), Form Definitions (.form), Ant Build Scripts", "Code Quality & Peer Inspection Gate"],
        ["Phase 4: Testing", "Compiled System Modules, Test Plan", "Unit testing, integration testing, timer countdown stress tests, regression suites", "Test Case Results, Traceability Matrix, Bug Resolution Log", "Test Readiness & Verification Gate"],
        ["Phase 5: Deployment", "Verified Build, Production DB Schema", "JAR packaging, MySQL database installation, admin credential provisioning, UAT execution", "Deployable GamingCafe.jar, User Manual, UAT Sign-Off Certificate", "Operational Acceptance Milestone"],
        ["Phase 6: Maintenance", "User defect tickets, hardware changes", "Log monitoring, bug fixing, schema adaptation, performance tuning", "Service Patches, Version Updates, Maintenance Logs", "Post-Implementation Review Gate"]
    ]
    story.append(create_reportlab_table(wf_hdrs, wf_rows_data, [85, 95, 115, 110, 85]))

    story.append(PageBreak())

    # =========================================================================
    # SECTION 3: OVERALL SYSTEM DESCRIPTION
    # =========================================================================
    story.append(Paragraph("3. Overall System Description", style_h1))
    story.append(HRFlowable(width="100%", thickness=1, color=primary_color, spaceAfter=8))

    story.append(Paragraph("3.1 Product Perspective & Context", style_h2))
    story.append(Paragraph("The Gaming Cafe Management System is a standalone desktop application communicating with a high-performance relational database management system (MySQL) via the Java Database Connectivity (JDBC) API. It replaces fragmented paper registers and unlinked spreadsheets with an authoritative, unified transactional store. The application runs natively on client workstations deployed at the reception, dispatch, and administrative management desks.", style_body))

    story.append(Paragraph("3.2 3-Tier Layered Architecture Pattern", style_h2))
    story.append(Paragraph("To ensure high cohesion, low coupling, maintainability, and clean separation of concerns, the system is engineered around a 3-Tier Layered Architecture:", style_body))
    story.append(Paragraph("• <b>Presentation Layer (GUI Tier):</b> Built with Java Swing and styled with the FlatLaf modern UI library. Manages user visual interactions, responsive card layout transitions (CardLayout), dynamic station cards, and event listeners.", style_bullet))
    story.append(Paragraph("• <b>Business Logic Layer (Controller Tier):</b> Implements domain rules, real-time session countdown timers (javax.swing.Timer), duration and price calculations, input validation algorithms (regex for NIC, phone numbers), cryptographic password digestion (SHA-256), and role-based feature gating.", style_bullet))
    story.append(Paragraph("• <b>Data Access Layer (Persistence Tier):</b> Encapsulated by centralized database connection management (db.java) and parameterized JDBC PreparedStatement objects. Guarantees ACID transactional compliance, isolates the application from underlying database specifics, and protects against SQL injection.", style_bullet))

    if os.path.exists('srs_assets/architecture_diagram.png'):
        story.append(Spacer(1, 4))
        story.append(Image('srs_assets/architecture_diagram.png', width=6.5*inch, height=3.6*inch))
        story.append(Paragraph("Figure 3.1: 3-Tier Layered Architecture Pattern of the Gaming Cafe Platform", style_caption))

    story.append(Paragraph("3.3 User Classes, Personas & Operational Responsibilities", style_h2))
    uclass_hdrs = ["User Role", "Persona Profile", "System Capabilities & Access Rights", "Operational Security Level"]
    uclass_rows = [
        ["System Administrator", "Cafe Owner / General Manager / IT Administrator", "Full system privileges. Can access Dashboard, Live Sessions, Station Inventory Management, Maintenance, History, multi-category PDF Reporting, and exclusive User Administration (create staff/admin, reset user flags, toggle active/inactive).", "Tier 1 - Full Unrestricted Administrative Access"],
        ["Cafe Staff / Cashier", "Front-Desk Receptionist / Counter Cashier", "Operational access. Can start sessions, allocate duration, execute dynamic '+ Add Time' extensions, end sessions, generate BillFrame invoices, collect payments, and log repair requests. Blocked from User Administration and system settings.", "Tier 2 - Operational Business Access Only"],
        ["Gamer / Customer", "Enrolled Player / Esports Competitor", "Indirect user. Registration details, contact phone, allocated gaming time, and payment records are tracked. Can utilize Token/QR pass for instant station check-in.", "Tier 3 - Data Entity / Customer Resource Tier"]
    ]
    story.append(create_reportlab_table(uclass_hdrs, uclass_rows, [95, 105, 205, 85]))

    story.append(Paragraph("3.4 Operational Environment & System Requirements", style_h2))
    story.append(Paragraph("<b>Client Workstation Hardware Specifications:</b><br/>• <b>Processor:</b> Intel Core i3 (2.0 GHz) or AMD equivalent (Intel Core i5 recommended).<br/>• <b>Random Access Memory (RAM):</b> Minimum 4 GB RAM (8 GB recommended for optimal JVM performance).<br/>• <b>Hard Disk Storage:</b> Minimum 500 MB free storage for application runtime and local PDF logging.<br/>• <b>Display Resolution:</b> Minimum 1366 × 768 pixels (Full HD 1920 × 1080 recommended).<br/>• <b>Input Devices:</b> Standard QWERTY keyboard, mouse, and optional 2D barcode/QR scanner.", style_body))
    story.append(Paragraph("<b>Software & Platform Environment:</b><br/>• <b>Operating System:</b> Microsoft Windows 10 (64-bit) or Windows 11. Cross-compatible with Linux (Ubuntu 20.04+) and macOS (12+).<br/>• <b>Java Runtime Environment:</b> Oracle OpenJDK or Eclipse Temurin JRE 8, 11, 17, or 21 (LTS).<br/>• <b>Database Management System:</b> MySQL Community Server 8.0 or 8.4 LTS running on port 3306.<br/>• <b>JDBC Database Driver:</b> MySQL Connector/J 8.0.33+ included in application distribution.<br/>• <b>UI Skinning Engine:</b> FlatLaf Modern Look and Feel Version 3.5.4+.<br/>• <b>PDF Generation Engine:</b> iText PDF Library 5.5+ / JasperReports.", style_body))

    story.append(Paragraph("3.5 Design & Implementation Constraints", style_h2))
    story.append(Paragraph("1. <b>Desktop-First Native Architecture:</b> Developed as a high-responsiveness native Java desktop GUI to operate without dependencies on external cloud web servers or continuous internet connectivity.<br/>2. <b>Irreversible Password Security:</b> Passwords must never be stored in plain text. Passwords must be hashed using one-way SHA-256 with Base64 encoding.<br/>3. <b>Relational Integrity:</b> Strict Foreign Key cascades must be implemented so that deleting or updating records maintains referential integrity across user, user_logs, pc_table, gaming_sessions, pc_maintenance, and invoices.<br/>4. <b>GUI Thread Safety:</b> Long-running database operations and countdown timer ticks must update Swing components strictly within the Event Dispatch Thread (java.awt.EventQueue.invokeLater).", style_body))

    story.append(PageBreak())

    # =========================================================================
    # SECTION 4: UML DIAGRAMS, DATA MODELS & ALGORITHMS
    # =========================================================================
    story.append(Paragraph("4. System Architecture, Unified Modeling Language (UML) & Data Models", style_h1))
    story.append(HRFlowable(width="100%", thickness=1, color=primary_color, spaceAfter=8))

    story.append(Paragraph("4.1 Use Case Diagram & Actor Responsibilities", style_h2))
    story.append(Paragraph("The behavioral interactions between the primary actors (System Administrator, Cafe Staff / Cashier) and the system boundary are modeled below. System Administrator inherits all staff operational use cases with exclusive rights over user provisioning and global report compilation.", style_body))

    if os.path.exists('srs_assets/uml_use_case.png'):
        story.append(Spacer(1, 4))
        story.append(Image('srs_assets/uml_use_case.png', width=6.5*inch, height=4.5*inch))
        story.append(Paragraph("Figure 4.1: UML Use Case Diagram for Gaming Cafe Management System", style_caption))

    story.append(PageBreak())

    story.append(Paragraph("4.2 Class Diagram & Object-Oriented Domain Model", style_h2))
    story.append(Paragraph("The static structure of the Java software architecture is depicted in the Class Diagram below, showing domain entities (User, GamingStation, GamingSession, Invoice, PCMaintenance, UserLogs), utility classes (DBConnection), and GUI controllers (Dash, BillFrame).", style_body))

    if os.path.exists('srs_assets/uml_class_diagram.png'):
        story.append(Spacer(1, 4))
        story.append(Image('srs_assets/uml_class_diagram.png', width=6.5*inch, height=4.8*inch))
        story.append(Paragraph("Figure 4.2: UML Class Diagram (Domain Model, Entities & Controller Classes)", style_caption))

    story.append(PageBreak())

    story.append(Paragraph("4.3 Sequence Diagram: Session Booking, Live Countdown & Billing Lifecycle", style_h2))
    story.append(Paragraph("The dynamic runtime message interactions between Cashier, Dash Controller, SessionTimer, MySQL Database, and BillFrame during a full gaming session lifecycle are modeled below:", style_body))

    if os.path.exists('srs_assets/uml_sequence_diagram.png'):
        story.append(Spacer(1, 4))
        story.append(Image('srs_assets/uml_sequence_diagram.png', width=6.5*inch, height=4.2*inch))
        story.append(Paragraph("Figure 4.3: UML Sequence Diagram - Gaming Session Allocation, Dynamic Time Extension & Invoicing", style_caption))

    story.append(Paragraph("4.4 Activity Diagram: Operational Control Flow & Decision Logic", style_h2))
    story.append(Paragraph("The procedural workflow of session dispatching, duration selection, real-time countdown execution, dynamic mid-session time extensions (+30m, +1h, +2h, Custom), overtime handling, and PDF invoice generation is illustrated in the Activity Diagram below:", style_body))

    if os.path.exists('srs_assets/uml_activity_diagram.png'):
        story.append(Spacer(1, 4))
        story.append(Image('srs_assets/uml_activity_diagram.png', width=6.5*inch, height=4.8*inch))
        story.append(Paragraph("Figure 4.4: UML Activity Diagram - Station Dispatch, Extension Loop & Billing Flowchart", style_caption))

    story.append(PageBreak())

    story.append(Paragraph("4.5 State Machine Diagram: Station Finite State Transitions", style_h2))
    story.append(Paragraph("Each gaming station (PC or PS5) operates as a finite state-machine with three distinct operational states: Available (Green), Occupied (Red), and Under Maintenance / Repair (Orange). State transitions are triggered by cashier actions and technician repair resolutions:", style_body))

    if os.path.exists('srs_assets/uml_state_machine.png'):
        story.append(Spacer(1, 4))
        story.append(Image('srs_assets/uml_state_machine.png', width=6.5*inch, height=3.6*inch))
        story.append(Paragraph("Figure 4.5: UML State Machine Diagram - Gaming Station Finite State Lifecycle", style_caption))

    story.append(Paragraph("4.6 Relational Database Schema & Data Dictionaries", style_h2))
    story.append(Paragraph("The relational database schema is normalized to Third Normal Form (3NF), eliminating transitive and partial dependencies while enforcing domain constraints. Below is the exact production database schema diagram corresponding to the MySQL database instance:", style_body))

    if os.path.exists('srs_assets/uml_database_er.png'):
        story.append(Spacer(1, 4))
        story.append(Image('srs_assets/uml_database_er.png', width=6.5*inch, height=4.5*inch))
        story.append(Paragraph("Figure 4.6: Exact Relational Database Schema & Foreign Key Relationships", style_caption))

    story.append(PageBreak())

    # Data Dictionaries for all 6 tables from user's schema
    story.append(Paragraph("Data Dictionaries (Production MySQL Tables)", style_h2))
    
    # Table 1: user
    story.append(Paragraph("Table 1: user (User Accounts & Authentication Credentials)", style_h3))
    t1_h = ["Column Name", "Data Type", "Constraints", "Default", "Description & Validation Rules"]
    t1_r = [
        ["id", "int(3)", "PRIMARY KEY, AUTO_INC", "None", "Unique auto-generated identifier for the user account."],
        ["emp_no", "varchar(20)", "NOT NULL, UNIQUE", "None", "Employee registration number (e.g., 'EMP-001')."],
        ["f_name", "varchar(100)", "NOT NULL", "None", "First name of the staff member."],
        ["l_name", "varchar(100)", "NOT NULL", "None", "Last name / surname of the staff member."],
        ["nic", "varchar(12)", "NOT NULL, UNIQUE", "None", "National Identity Card number (validated via regex)."],
        ["email", "varchar(100)", "NULL", "None", "Email address for notifications."],
        ["phone", "varchar(10)", "NOT NULL", "None", "Mobile telephone contact number (10 digits)."],
        ["username", "varchar(50)", "NOT NULL, UNIQUE", "None", "Unique login username (3-50 characters)."],
        ["password", "varchar(255)", "NOT NULL", "None", "SHA-256 cryptographic one-way digest encoded in Base64."],
        ["role", "varchar(20)", "NOT NULL", "'Staff'", "Access permission role: 'Admin' or 'Staff'."],
        ["status", "tinyint(1)", "NOT NULL", "1", "Account operational state: 1 = Active, 0 = Inactive."],
        ["image", "varchar(255)", "NULL", "None", "Relative file path to user profile avatar."],
        ["changePass", "tinyint(4)", "NOT NULL", "1", "Flag for mandatory first-time password reset: 1 = Required, 0 = Active."],
        ["created_at", "timestamp", "DEFAULT CURRENT", "Current", "Timestamp of account registration."]
    ]
    story.append(create_reportlab_table(t1_h, t1_r, [65, 65, 115, 55, 190]))

    story.append(Spacer(1, 6))
    # Table 2: user_logs
    story.append(Paragraph("Table 2: user_logs (Staff Authentication & Shift Audit Trail)", style_h3))
    t2_h = ["Column Name", "Data Type", "Constraints", "Default", "Description & Validation Rules"]
    t2_r = [
        ["log_id", "int(11)", "PRIMARY KEY, AUTO_INC", "None", "Unique audit log record serial number."],
        ["emp_no", "varchar(50)", "FOREIGN KEY → user", "None", "Referenced employee number of authenticated user."],
        ["username", "varchar(100)", "NOT NULL", "None", "Captured username at login timestamp."],
        ["role", "varchar(50)", "NOT NULL", "None", "User role at authentication (Admin / Staff)."],
        ["login_time", "datetime", "NOT NULL", "CURRENT", "Timestamp of successful login."],
        ["logout_time", "datetime", "NULL", "None", "Timestamp of clean logout session termination."],
        ["duration", "varchar(50)", "NULL", "None", "Calculated active shift duration (e.g., '04h 25m 10s')."],
        ["status", "varchar(50)", "NOT NULL", "'Active'", "Session state: 'Active', 'Logged Out'."]
    ]
    story.append(create_reportlab_table(t2_h, t2_r, [65, 65, 115, 55, 190]))

    story.append(Spacer(1, 6))
    # Table 3: pc_table
    story.append(Paragraph("Table 3: pc_table (Gaming Stations & Hardware Inventory)", style_h3))
    t3_h = ["Column Name", "Data Type", "Constraints", "Default", "Description & Validation Rules"]
    t3_r = [
        ["pc_id", "varchar(20)", "PRIMARY KEY", "None", "Unique station identifier (e.g., 'PC-01', 'PS5-02')."],
        ["pc_name", "varchar(50)", "NOT NULL", "None", "Descriptive station alias (e.g., 'RTX 4070 Station')."],
        ["category", "varchar(20)", "NOT NULL", "'PC'", "Hardware category: 'PC', 'PS5', 'Racing Sim'."],
        ["cpu", "varchar(50)", "NOT NULL", "None", "Central Processing Unit model."],
        ["motherboard", "varchar(50)", "NOT NULL", "None", "Motherboard chipset / model."],
        ["ram_capacity", "varchar(20)", "NOT NULL", "None", "Installed RAM capacity & speed (e.g., '32GB DDR5')."],
        ["vga", "varchar(50)", "NOT NULL", "None", "Dedicated Graphics Processing Unit (e.g., 'RTX 4070 12GB')."],
        ["ip_address", "varchar(20)", "NULL", "None", "LAN IP address for remote station management."],
        ["hourly_rate", "decimal(10,2)", "NOT NULL", "200.00", "Base hourly rental tariff in LKR."],
        ["status", "varchar(20)", "NOT NULL", "'Available'", "Operational status: 'Available', 'Occupied', 'Repair'."],
        ["ip_block", "tinyint(4)", "NOT NULL", "0", "Network access block status (0 = Allowed, 1 = Blocked)."],
        ["recorded_at", "timestamp", "DEFAULT CURRENT", "Current", "Timestamp when station was registered."]
    ]
    story.append(create_reportlab_table(t3_h, t3_r, [65, 65, 115, 55, 190]))

    story.append(PageBreak())

    # Table 4: gaming_sessions
    story.append(Paragraph("Table 4: gaming_sessions (Live & Historic Play Sessions)", style_h3))
    t4_h = ["Column Name", "Data Type", "Constraints", "Default", "Description & Validation Rules"]
    t4_r = [
        ["session_id", "varchar(20)", "PRIMARY KEY", "None", "Unique session identifier (e.g., 'SES-001')."],
        ["pc_id", "varchar(20)", "FOREIGN KEY → pc_table", "None", "Referenced gaming station allocated for play."],
        ["cus_name", "varchar(100)", "NOT NULL", "'Walk-in'", "Gamer / Player customer name."],
        ["phone", "varchar(20)", "NULL", "None", "Mobile telephone contact number of customer."],
        ["start_time", "datetime", "NOT NULL", "CURRENT", "Timestamp when session gameplay commenced."],
        ["end_time", "datetime", "NULL", "None", "Timestamp when session was terminated."],
        ["add_minutes", "int(11)", "NOT NULL", "0", "Cumulative extra time added during session in minutes."],
        ["price_per_adding", "decimal(10,2)", "NOT NULL", "0.00", "Additional charge incurred via '+ Add Time'."],
        ["total_minutes", "int(11)", "NOT NULL", "60", "Total allocated playing duration (Initial + Add Time)."],
        ["total_amount", "decimal(10,2)", "NOT NULL", "0.00", "Total calculated session charge before discounts."],
        ["status", "varchar(20)", "NOT NULL", "'Ongoing'", "Session state: 'Ongoing', 'Completed', 'Cancelled'."]
    ]
    story.append(create_reportlab_table(t4_h, t4_r, [65, 65, 115, 55, 190]))

    story.append(Spacer(1, 6))
    # Table 5: pc_maintenance
    story.append(Paragraph("Table 5: pc_maintenance (Defect Logging & Repair Records)", style_h3))
    t5_h = ["Column Name", "Data Type", "Constraints", "Default", "Description & Validation Rules"]
    t5_r = [
        ["repair_id", "int(11)", "PRIMARY KEY, AUTO_INC", "None", "Unique defect repair ticket serial number."],
        ["pc_id", "varchar(20)", "FOREIGN KEY → pc_table", "None", "Station currently under hardware repair."],
        ["issue_description", "varchar(255)", "NOT NULL", "None", "Reported hardware failure description."],
        ["cost", "decimal(10,2)", "NOT NULL", "0.00", "Total maintenance / replacement cost incurred in LKR."],
        ["repair_date", "datetime", "NOT NULL", "CURRENT", "Timestamp when repair was logged."]
    ]
    story.append(create_reportlab_table(t5_h, t5_r, [65, 65, 115, 55, 190]))

    story.append(Spacer(1, 6))
    # Table 6: invoices
    story.append(Paragraph("Table 6: invoices (Financial Invoicing & Payment Records)", style_h3))
    t6_h = ["Column Name", "Data Type", "Constraints", "Default", "Description & Validation Rules"]
    t6_r = [
        ["invoice_no", "varchar(20)", "PRIMARY KEY", "None", "Unique invoice serial number (e.g., 'INV-0104')."],
        ["session_id", "varchar(20)", "FOREIGN KEY → gaming_sessions", "None", "Referenced session record billed."],
        ["emp_no", "varchar(20)", "FOREIGN KEY → user", "None", "Employee / Cashier who finalized transaction."],
        ["sub_total", "decimal(10,2)", "NOT NULL", "0.00", "Subtotal amount before promotional discounts."],
        ["discount", "decimal(10,2)", "NOT NULL", "0.00", "Promotional discount deducted from bill."],
        ["net_total", "decimal(10,2)", "NOT NULL", "0.00", "Final net amount payable by customer."],
        ["payment_method", "varchar(20)", "NOT NULL", "'Cash'", "Payment method utilized: 'Cash', 'Card', 'QR Token'."],
        ["invoice_date", "datetime", "DEFAULT CURRENT", "Current", "Timestamp of invoice settlement."]
    ]
    story.append(create_reportlab_table(t6_h, t6_r, [65, 65, 115, 55, 190]))

    story.append(Spacer(1, 10))
    story.append(Paragraph("4.7 Real-Time Session Countdown & Dynamic Add-Time Algorithms", style_h2))
    story.append(Paragraph("The system executes a real-time countdown algorithm updating per-second timestamps via <b>javax.swing.Timer</b>. When a player requests additional time (+30m, +1h, +2h, Custom), the system executes an atomic parameterized transaction:", style_body))

    tbl_code = Table(
        [[Paragraph("<b>-- Atomic Time Extension Query Executed via PreparedStatement:</b><br/>UPDATE gaming_sessions<br/>SET add_minutes = add_minutes + ?,<br/>&nbsp;&nbsp;&nbsp;&nbsp;total_minutes = total_minutes + ?,<br/>&nbsp;&nbsp;&nbsp;&nbsp;price_per_adding = ?,<br/>&nbsp;&nbsp;&nbsp;&nbsp;total_amount = total_amount + ?<br/>WHERE session_id = ? AND status = 'Ongoing';", ParagraphStyle('MonoCode', fontName='Courier', fontSize=8, leading=11, textColor=colors.HexColor("#0f172a")))]],
        colWidths=[490]
    )
    tbl_code.setStyle(TableStyle([
        ('BOX', (0,0), (-1,-1), 1, colors.HexColor("#cbd5e1")),
        ('BACKGROUND', (0,0), (-1,-1), colors.HexColor("#f8fafc")),
        ('TOPPADDING', (0,0), (-1,-1), 6),
        ('BOTTOMPADDING', (0,0), (-1,-1), 6),
        ('LEFTPADDING', (0,0), (-1,-1), 10),
    ]))
    story.append(tbl_code)

    story.append(Paragraph("4.8 Cryptographic Security Architecture & Password Lifecycle", style_h2))
    story.append(Paragraph("User credentials are protected using NIST FIPS 180-4 compliant SHA-256 one-way hashing. If changePass == 1, the user is intercepted upon login and redirected to ChangeUserDetails.java before dashboard access is permitted. Administrators can reset any user's credentials back to the default password via the btnResetUser action button in User Administration.", style_body))

    story.append(PageBreak())

    # =========================================================================
    # SECTION 5: SPECIFIC FUNCTIONAL REQUIREMENTS (MODULES 1 TO 9)
    # =========================================================================
    story.append(Paragraph("5. Specific Functional Requirements (Modules 1 to 9)", style_h1))
    story.append(HRFlowable(width="100%", thickness=1, color=primary_color, spaceAfter=8))

    def make_fr_table(rows):
        h = ["Req ID", "Requirement Title", "Priority", "Detailed Functional Specification"]
        return create_reportlab_table(h, rows, [65, 110, 55, 260])

    story.append(Paragraph("5.1 Module 1: Authentication & Access Control", style_h2))
    m1_data = [
        ["FR-AUTH-01", "Credential Hashing Authentication", "MUST", "The system shall authenticate usernames/NIC and passwords by digesting input via SHA-256 and matching digests against the user table."],
        ["FR-AUTH-02", "First-Time Login Interception", "MUST", "If a logging-in user has changePass = 1, the system shall intercept navigation and display ChangeUserDetails.java."],
        ["FR-AUTH-03", "Mandatory Password Reset", "MUST", "ChangeUserDetails form shall require entering new password and confirmation, validate minimum 4 characters, hash with SHA-256, set changePass = 0, and advance to Dashboard."],
        ["FR-AUTH-04", "Role-Based UI Gating", "MUST", "Upon login, the system shall evaluate user role. If role is 'Staff', administrative navigation buttons (btnAdmin, User Management, Global Settings) shall be hidden."],
        ["FR-AUTH-05", "User Activity Shift Logging", "MUST", "Upon every successful authentication, the system shall insert a record into user_logs capturing emp_no, username, role, and login_time."],
        ["FR-AUTH-06", "Secure Session Logout", "MUST", "The system shall allow users to log out, updating logout_time and duration in user_logs, terminating active dashboard windows, and returning to LoginFrame."]
    ]
    story.append(make_fr_table(m1_data))

    story.append(Spacer(1, 8))
    story.append(Paragraph("5.2 Module 2: Central Operational Dashboard & Real-Time KPI Telemetry", style_h2))
    m2_data = [
        ["FR-DASH-01", "Live Station KPI Telemetry", "MUST", "The dashboard shall execute aggregation queries to compute and render Total Stations, Active Playing Sessions, Available PCs, and Stations in Repair in real-time summary cards."],
        ["FR-DASH-02", "Interactive Station Grid Rendering", "MUST", "The dashboard shall dynamically render interactive cards for all stations (PC-01 to PC-08, PS5 consoles) with color-coded status badges (Available = Green, Occupied = Red, Repair = Orange)."],
        ["FR-DASH-03", "One-Click Station Selection", "MUST", "Clicking any station card in the grid shall immediately populate the Session Control side-panel with station name, category, hourly rate, and active session telemetry."],
        ["FR-DASH-04", "Digital Clock & Date Header", "MUST", "The system shall display live synchronized system date and digital time in the top application header."],
        ["FR-DASH-05", "Collapsible Hamburger Navigation", "SHOULD", "The system shall support toggling the sidebar navigation menu via btnHam to maximize workspace area on compact displays."]
    ]
    story.append(make_fr_table(m2_data))

    story.append(PageBreak())

    story.append(Paragraph("5.3 Module 3: Live Gaming Sessions & Active Station Monitoring", style_h2))
    m3_data = [
        ["FR-LIVE-01", "Live Sessions Tabular View", "MUST", "The system shall provide a dedicated Live Sessions table (jTableLive) displaying all active ongoing sessions with Customer Name, Phone, Station, Start Time, Duration, and Live Timer."],
        ["FR-LIVE-02", "Real-time Live Table Tick Refresh", "MUST", "The Live Sessions table shall update every second via javax.swing.Timer to show synchronized countdown timestamps across all concurrent players."],
        ["FR-LIVE-03", "Live Search & Customer Filtering", "MUST", "The system shall filter the live sessions table dynamically as characters are typed into txtLiveSearch by customer name, phone number, or PC ID."],
        ["FR-LIVE-04", "One-Click Session End from Live Table", "MUST", "Double-clicking or selecting an active row in the live table shall allow instant session termination and invoice generation."],
        ["FR-LIVE-05", "Overtime Visual Highlighting", "MUST", "Active sessions exceeding their allocated duration shall be highlighted in high-contrast red font with overtime surcharge notification."]
    ]
    story.append(make_fr_table(m3_data))

    story.append(Spacer(1, 8))
    story.append(Paragraph("5.4 Module 4: Session Duration Allocation & Dynamic Time Extension", style_h2))
    m4_data = [
        ["FR-SES-01", "Standard Duration Presets", "MUST", "The system shall provide a duration selection ComboBox (cmbSessionDuration) with options: 1h (60m), 1.5h (90m), 2h (120m), 3h (180m), 4h (240m), 5h (300m), 6h (360m), and Custom Minutes."],
        ["FR-SES-02", "Minimum 1-Hour Duration Validation", "MUST", "The system shall enforce a minimum session duration of 60 minutes (1 Hour) for both standard presets and custom minute entries."],
        ["FR-SES-03", "Real-time Estimated Charge Calculation", "MUST", "Selecting any duration preset shall instantly calculate and render the estimated session price: (Allocated_Minutes / 60.0) * Station_Hourly_Rate."],
        ["FR-SES-04", "Dynamic In-Session '+ Add Time'", "MUST", "The system shall provide an '⏱ + Add Time' button during active sessions allowing staff to add +30m, +1h, +2h, or Custom minutes, updating the active countdown and database record immediately."],
        ["FR-SES-05", "Session Start Validation & Dispatch", "MUST", "Clicking '▶ Start Session' shall validate customer name, verify station availability, insert record into gaming_sessions, set station status to 'Occupied', and start the countdown timer."]
    ]
    story.append(make_fr_table(m4_data))

    story.append(Spacer(1, 8))
    story.append(Paragraph("5.5 Module 5: Station & Hardware Inventory Management", style_h2))
    m5_data = [
        ["FR-PC-01", "Hardware Specification Logging", "MUST", "The system shall record station hardware specifications: Station ID, Name, Category (PC/PS5), Hourly Rate, CPU, Motherboard, RAM, and Dedicated Graphics Card (VGA)."],
        ["FR-PC-02", "Station Inventory Tabular View", "MUST", "The system shall display all registered stations in a styled table with real-time status badges and hardware component summaries."],
        ["FR-PC-03", "Hardware Specification Editing", "MUST", "Authorized administrators shall be able to update station hardware specifications, component upgrades, and hourly rental tariffs."],
        ["FR-PC-04", "Station Decommissioning & Removal", "MUST", "The system shall permit removing retired stations upon administrative confirmation while preserving historic session logs."],
        ["FR-PC-05", "Hardware Category Filtering", "SHOULD", "The system shall support filtering stations by hardware category (PC High-Tier, PC Mid-Tier, PS5 Console) for rapid dispatch."]
    ]
    story.append(make_fr_table(m5_data))

    story.append(PageBreak())

    story.append(Paragraph("5.6 Module 6: Hardware Maintenance, Defect Logging & Repair History", style_h2))
    m6_data = [
        ["FR-REP-01", "Station Defect Dispatch", "MUST", "Staff shall be able to mark any malfunctioning station as 'Repair', entering a detailed defect reason (e.g., 'GPU Artifacts', 'RAM Failure') and taking the station off-line."],
        ["FR-REP-02", "Active Repairs Management Table", "MUST", "The system shall maintain an active repair table (jTable2) listing all stations currently under maintenance with reported issues and dates."],
        ["FR-REP-03", "Repair Resolution & Return to Service", "MUST", "Upon technician completion, staff shall log the repair cost, restoring station status to 'Available' in pc_table and archiving the record to pc_maintenance."],
        ["FR-REP-04", "Maintenance History & Cost Auditing", "MUST", "The system shall display historical maintenance records in pc_maintenance table to audit total hardware repair expenditures over time."],
        ["FR-REP-05", "Maintenance PDF Report Export", "SHOULD", "Administrators shall be able to compile and export the complete hardware maintenance and repair expenditure log as a formatted PDF document."]
    ]
    story.append(make_fr_table(m6_data))

    story.append(Spacer(1, 8))
    story.append(Paragraph("5.7 Module 7: Invoicing, Billing Engine & PDF Receipt Export", style_h2))
    m7_data = [
        ["FR-BILL-01", "Dedicated BillFrame Presentation", "MUST", "Clicking '⏹ End Session & Bill' shall terminate the active session, calculate total charges, and open a dedicated, modern BillFrame window."],
        ["FR-BILL-02", "Itemized Invoice Breakdown", "MUST", "BillFrame shall display invoice serial number, station name, category, customer name, phone, start/end timestamps, total duration, hourly rate, and subtotal amount."],
        ["FR-BILL-03", "Promotional Discount Calculation", "MUST", "BillFrame shall support applying special promotional discounts, recalculating Net Payable Amount in real-time."],
        ["FR-BILL-04", "Cash & Change Due Calculator", "MUST", "Entering cash received from customer shall automatically compute and display the change due (Balance = Cash_Received - Net_Amount)."],
        ["FR-BILL-05", "Payment Method Selection", "MUST", "Staff shall record the payment method (Cash / Card / QR Token) before finalizing the invoice transaction."],
        ["FR-BILL-06", "iText PDF Receipt Export & Print", "MUST", "Clicking 'Print / Save PDF Bill' shall compile and export an official PDF invoice receipt and prompt the user to immediately view/print the document."]
    ]
    story.append(make_fr_table(m7_data))

    story.append(Spacer(1, 8))
    story.append(Paragraph("5.8 Module 8: Business Intelligence & Multi-Category PDF Reporting", style_h2))
    m8_data = [
        ["FR-REP-01", "Multi-Category Report Generation Dialog", "MUST", "The system shall provide an interactive option dialog (showReportGenerationDialog) allowing administrators to generate 5 distinct PDF reports."],
        ["FR-REP-02", "User Activity Logs & Attendance Report", "MUST", "The system shall compile and export all staff login, logout, and shift duration records into a structured 'User Activity Logs Report' PDF."],
        ["FR-REP-03", "Session History & Usage Utilization Report", "MUST", "The system shall compile and export all historic gaming sessions into a structured 'PC Session History Report' PDF."],
        ["FR-REP-04", "Invoices & Revenue Billing Report", "MUST", "The system shall compile and export all finalized customer invoices, payment methods, discounts, and total earnings into an 'Invoices Billing Report' PDF."],
        ["FR-REP-05", "Station Hardware Inventory Report", "MUST", "The system shall compile and export all registered stations, hardware specs, and hourly rates into a 'PC Inventory Report' PDF."],
        ["FR-REP-06", "Universal PDF Exporter Engine", "MUST", "The exportTableToPdf engine shall automatically extract JTable column models, apply high-contrast header styling, alternate row colors, and save files to user home directory."]
    ]
    story.append(make_fr_table(m8_data))

    story.append(PageBreak())

    story.append(Paragraph("5.9 Module 9: User Administration & Security Provisioning", style_h2))
    m9_data = [
        ["FR-USR-01", "Staff Account Provisioning", "MUST", "Administrators shall create new staff accounts specifying First Name, Last Name, NIC, Phone, Email, Username, Role (Staff/Admin), and Password."],
        ["FR-USR-02", "Automatic Default Password Assignment", "MUST", "If password fields are left blank during account provisioning, the system shall assign the default password ('1234') hashed via SHA-256 with changePass = 1."],
        ["FR-USR-03", "Administrative Credential Reset", "MUST", "The system shall provide a 'Reset User' action button (btnResetUser) allowing administrators to restore any user's credentials to the default password and reset changePass flag."],
        ["FR-USR-04", "Account Inactivation & Activation", "MUST", "Administrators shall be able to toggle staff account operational states between Active (1) and Inactive (0) to prevent unauthorized shift logins."],
        ["FR-USR-05", "Dynamic Action Button Visibility", "MUST", "Action buttons (btnUpdateUser, btnDeleteUser, btnClearUser, btnResetUser) shall remain hidden until a specific record is selected in tableUser."],
        ["FR-USR-06", "Self-Deletion Prevention", "MUST", "The system shall block administrators from deleting or inactivating their own currently authenticated user account, preventing system lockout."]
    ]
    story.append(make_fr_table(m9_data))

    story.append(PageBreak())

    # =========================================================================
    # SECTION 6: EXTERNAL INTERFACES & HARDWARE
    # =========================================================================
    story.append(Paragraph("6. External Interface Requirements", style_h1))
    story.append(HRFlowable(width="100%", thickness=1, color=primary_color, spaceAfter=8))

    story.append(Paragraph("6.1 Graphical User Interfaces (GUI) & Dynamic Controls", style_h2))
    story.append(Paragraph("The visual presentation is engineered natively in Java Swing with FlatLaf vector styling. Below are the operational blueprints of the main system interfaces:", style_body))

    if os.path.exists('srs_assets/ui_dash_mockup.png'):
        story.append(Spacer(1, 4))
        story.append(Image('srs_assets/ui_dash_mockup.png', width=6.2*inch, height=3.6*inch))
        story.append(Paragraph("Figure 6.1: Central Operational Dashboard with Live Stations Grid, KPI Cards, and Session Control Panel", style_caption))

    if os.path.exists('srs_assets/ui_bill_mockup.png'):
        story.append(Spacer(1, 4))
        story.append(Image('srs_assets/ui_bill_mockup.png', width=5.5*inch, height=4.2*inch))
        story.append(Paragraph("Figure 6.2: Dedicated Invoicing & Bill Receipt Generation Window (BillFrame)", style_caption))

    story.append(PageBreak())

    if os.path.exists('srs_assets/ui_reports_mockup.png'):
        story.append(Spacer(1, 4))
        story.append(Image('srs_assets/ui_reports_mockup.png', width=5.5*inch, height=3.8*inch))
        story.append(Paragraph("Figure 6.3: Multi-Dimensional PDF Business Intelligence & Audit Reporting Dialog", style_caption))

    story.append(Paragraph("6.2 Hardware Interfaces & Token / QR Code Instant Session Check-In Scanner", style_h2))
    story.append(Paragraph("The system is engineered to interface seamlessly with standard USB / Bluetooth 2D barcode and QR code scanners acting as Human Interface Devices (HID keyboard wedge). When a customer presents an official membership token or QR pass, the scanner transmits the player token string into the active station dispatch listener, triggering automated station unlocking and session initialization:", style_body))

    if os.path.exists('srs_assets/qr_token_workflow.png'):
        story.append(Spacer(1, 4))
        story.append(Image('srs_assets/qr_token_workflow.png', width=6.5*inch, height=3.2*inch))
        story.append(Paragraph("Figure 6.4: Token & QR Scanner Automated Session Check-In and Station Allocation Workflow", style_caption))

    story.append(Paragraph("6.3 Software & Driver Interfaces", style_h2))
    story.append(Paragraph("• <b>Java Database Connectivity (JDBC):</b> com.mysql.cj.jdbc.Driver establishes connection pooling with the MySQL database instance on port 3306.<br/>• <b>FlatLaf Look-and-Feel Engine:</b> com.formdev.flatlaf.FlatIntelliJLaf provides high-DPI scaling and vector styling.<br/>• <b>iText PDF Library / JasperReports:</b> Generates official PDF receipts and multi-table business audit reports.", style_body))

    story.append(Paragraph("6.4 Communications & Network Protocols", style_h2))
    story.append(Paragraph("The system communicates with the MySQL database instance over TCP/IP socket connections (default port 3306). Localhost loopback sockets (127.0.0.1) are utilized for single-workstation standalone deployments, while standard Local Area Network (LAN) intranet IP addresses (e.g., 192.168.1.100) are fully supported for multi-counter cafe setups.", style_body))

    story.append(PageBreak())

    # =========================================================================
    # SECTION 7: NON-FUNCTIONAL REQUIREMENTS
    # =========================================================================
    story.append(Paragraph("7. Non-Functional Requirements (NFR)", style_h1))
    story.append(HRFlowable(width="100%", thickness=1, color=primary_color, spaceAfter=8))

    story.append(Paragraph("7.1 Performance & Scalability Benchmarks", style_h2))
    nfr_h = ["NFR ID", "Quality Characteristic", "Measurable Target / Benchmark"]
    nfr_r = [
        ["NFR-PERF-01", "Query Response Time", "Station grid and session data queries shall execute and render in < 250 ms under standard database loads (≤ 50,000 records)."],
        ["NFR-PERF-02", "Authentication Latency", "SHA-256 cryptographic password hashing and credential verification shall complete in < 200 ms."],
        ["NFR-PERF-03", "Countdown Timer Accuracy", "Per-second session countdown timer ticks shall deviate by less than ±50 milliseconds per hour of operation."],
        ["NFR-PERF-04", "PDF Compilation Throughput", "Compiling and exporting a 100-row tabular PDF report via iText shall complete in < 1.5 seconds."]
    ]
    story.append(create_reportlab_table(nfr_h, nfr_r, [85, 120, 285]))

    story.append(Paragraph("7.2 Reliability, Availability & Disaster Recovery", style_h2))
    story.append(Paragraph("• <b>ACID Transaction Compliance:</b> All session creations, time extensions, and invoice settlements execute within strict database transaction boundaries. In the event of a runtime SQL exception or disconnection, uncommitted changes rollback automatically.<br/>• <b>Database Reconnection Resilience:</b> The connection manager checks whether the connection is closed (con.isClosed()), seamlessly attempting re-establishment before throwing fatal errors.<br/>• <b>Exception Logging:</b> All caught runtime errors are logged via java.util.logging.Logger for post-incident diagnostics.", style_body))

    story.append(Paragraph("7.3 Information Security & Data Protection", style_h2))
    story.append(Paragraph("• <b>Cryptographic Password Integrity:</b> Passwords must never be stored, transmitted, or logged in plain text. SHA-256 hashing is enforced across all registration and change workflows.<br/>• <b>SQL Injection Elimination:</b> Dynamic string-concatenated SQL queries are strictly prohibited. All queries with external inputs use parameterized PreparedStatement objects.<br/>• <b>Privilege Separation:</b> Staff accounts are strictly prevented from viewing or modifying user accounts, default passwords, or deleting operational logs.", style_body))

    story.append(Paragraph("7.4 Software Quality Attributes", style_h2))
    story.append(Paragraph("• <b>Usability:</b> Intuitive navigation with consistent iconography, informative popups (JOptionPane), and auto-selection of table records.<br/>• <b>Maintainability:</b> Clean source code structure with zero inline fully-qualified package namespaces (all imports placed at top of files), modular MVC separation, and comprehensive English comments.<br/>• <b>Portability:</b> Standard Java bytecode executes reliably across Windows, macOS, and Linux without recompilation.", style_body))

    story.append(PageBreak())

    # =========================================================================
    # SECTION 8: VERIFICATION & TEST CASES
    # =========================================================================
    story.append(Paragraph("8. System Verification, Validation & Test Cases", style_h1))
    story.append(HRFlowable(width="100%", thickness=1, color=primary_color, spaceAfter=8))

    story.append(Paragraph("8.1 Requirements Traceability Matrix (RTM)", style_h2))
    rtm_h = ["Requirement ID", "Module / Feature Description", "Test Case ID(s)", "Verification Status"]
    rtm_r = [
        ["FR-AUTH-01", "User Authentication & SHA-256 Verification", "TC-AUTH-01, TC-AUTH-02", "VERIFIED / PASS"],
        ["FR-AUTH-02, 03", "First-Time Login Mandatory Password Reset", "TC-AUTH-03", "VERIFIED / PASS"],
        ["FR-DASH-01..03", "Live Station Grid & Real-time KPI Telemetry", "TC-DASH-01, TC-DASH-02", "VERIFIED / PASS"],
        ["FR-SES-01..03", "Duration Allocation & Auto Price Calculation", "TC-SES-01, TC-SES-02", "VERIFIED / PASS"],
        ["FR-SES-04", "Dynamic in-session '+ Add Time' Extension", "TC-EXT-01, TC-EXT-02", "VERIFIED / PASS"],
        ["FR-BILL-01..06", "Dedicated BillFrame & iText PDF Receipt Export", "TC-BILL-01, TC-BILL-02", "VERIFIED / PASS"],
        ["FR-REP-01..04", "Station Defect Dispatch & Repair Resolution", "TC-REP-01, TC-REP-02", "VERIFIED / PASS"],
        ["FR-USR-01..06", "Staff User Provisioning & Credential Reset", "TC-USR-01, TC-USR-02", "VERIFIED / PASS"]
    ]
    story.append(create_reportlab_table(rtm_h, rtm_r, [85, 205, 115, 85]))

    story.append(Paragraph("8.2 Comprehensive Test Scenarios & Verified Results", style_h2))
    story.append(Paragraph("<b>Test Case TC-AUTH-01: Valid Staff / Admin Authentication:</b><br/>• <b>Preconditions:</b> Database contains active user record (username: 'admin', role: 'Admin').<br/>• <b>Test Input:</b> Username: 'admin', Password: 'correct_password'.<br/>• <b>Expected Result:</b> System hashes password via SHA-256, matches digest, shows 'Login Successful' dialog, records login in user_logs, and opens Dashboard.<br/>• <b>Actual Result:</b> <font color='#059669'><b>PASS</b></font> - Credentials validated, shift audit log created, Dashboard opened.", style_body))

    story.append(Paragraph("<b>Test Case TC-SES-01: Duration Selection & Real-Time Estimated Price Calculation:</b><br/>• <b>Preconditions:</b> Station 'PC-01' is 'Available' with hourly rate of Rs. 200.00.<br/>• <b>Test Input:</b> Select 'PC-01', choose duration '2 Hours (120 mins)'.<br/>• <b>Expected Result:</b> System calculates estimated charge: (120/60.0)*200 = Rs. 400.00, sets timer to '02:00:00', and reveals '▶ Start Session' button.<br/>• <b>Actual Result:</b> <font color='#059669'><b>PASS</b></font> - Price displayed as 'Rs. 400.00', timer initialized to '02:00:00'.", style_body))

    story.append(Paragraph("<b>Test Case TC-EXT-01: Dynamic '+ Add Time' During Ongoing Match:</b><br/>• <b>Preconditions:</b> Station 'PC-01' is 'Occupied' with active session SES-001 (allocated 60 mins).<br/>• <b>Test Input:</b> Click '⏱ + Add Time', select '+1 Hour (60 mins)'.<br/>• <b>Expected Result:</b> System updates database (add_minutes = 60, total_minutes = 120, total_amount = Rs. 400.00), extends countdown timer by +3600 seconds, and refreshes live view.<br/>• <b>Actual Result:</b> <font color='#059669'><b>PASS</b></font> - Additional 60 minutes added atomically, countdown timer extended.", style_body))

    story.append(Paragraph("<b>Test Case TC-BILL-01: Session Termination & Dedicated BillFrame Invoicing:</b><br/>• <b>Preconditions:</b> Station 'PC-01' session completed; player approaches checkout.<br/>• <b>Test Input:</b> Click '⏹ End Session & Bill' in Dashboard, enter cash received in BillFrame, click 'Print / Save PDF Bill'.<br/>• <b>Expected Result:</b> System terminates session, sets station status to 'Available', opens BillFrame, inserts invoice record, compiles official PDF receipt, and prompts user.<br/>• <b>Actual Result:</b> <font color='#059669'><b>PASS</b></font> - BillFrame displayed, station marked available, PDF invoice exported successfully.", style_body))

    story.append(PageBreak())

    # =========================================================================
    # SECTION 9: APPENDICES, GLOSSARY & ROADMAP
    # =========================================================================
    story.append(Paragraph("9. Appendices & Glossary", style_h1))
    story.append(HRFlowable(width="100%", thickness=1, color=primary_color, spaceAfter=8))

    story.append(Paragraph("9.1 Glossary of Technical Terms & Acronyms", style_h2))
    gl_h = ["Term / Acronym", "Full Definition & Context in this System"]
    gl_r = [
        ["SRS", "Software Requirements Specification: Formal document specifying all functional, operational, behavioral, and quality requirements."],
        ["SDLC", "Software Development Life Cycle: The structured framework (Waterfall Model) governing the engineering of this system."],
        ["GUI", "Graphical User Interface: Visual interactive front-end constructed via Java Swing and NetBeans Form Builder."],
        ["JDBC", "Java Database Connectivity: Industry standard Java API for executing SQL queries and managing relational database transactions."],
        ["SHA-256", "Secure Hash Algorithm 256-bit: Cryptographic one-way hash function used to digest and secure passwords."],
        ["FlatLaf", "Flat Look and Feel: Modern, high-DPI compatible Swing look-and-feel library providing clean, professional desktop aesthetics."],
        ["NIC", "National Identity Card: Government-issued citizen identity number validated using statutory regex formats."],
        ["MoSCoW", "Must / Should / Could / Won't: Standard requirements prioritization framework used across all functional specifications."],
        ["RTM", "Requirements Traceability Matrix: Grid mapping user requirements to system design components and verification test cases."],
        ["UAT", "User Acceptance Testing: Final operational verification conducted with actual counter receptionists and administrative staff."],
        ["POS", "Point of Sale: Counter hardware ecosystem including thermal receipt printers and barcode/QR scanners."]
    ]
    story.append(create_reportlab_table(gl_h, gl_r, [95, 395]))

    story.append(Paragraph("9.2 Future Strategic Enhancements & Web/Cloud Roadmap", style_h2))
    story.append(Paragraph("While the present release satisfies all baseline desktop operational requirements, the system architecture was engineered to accommodate future modular extensions:", style_body))
    story.append(Paragraph("• <b>Cloud Synchronized Multi-Branch Database:</b> Migration of the local MySQL instance to a managed cloud database (e.g., AWS RDS / Google Cloud SQL) for multi-branch esports franchises.", style_bullet))
    story.append(Paragraph("• <b>Web & Mobile Online Seat Reservation Portal:</b> Lightweight web frontend enabling gamers to check station availability and book gaming slots remotely.", style_bullet))
    story.append(Paragraph("• <b>Online Cashless Payment Gateway Integration:</b> LankaQR / Stripe / IPG integration for processing card and mobile wallet payments upon QR token scan.", style_bullet))
    story.append(Paragraph("• <b>Steam / Epic Games Platform API Integration:</b> Automated client-side background service to sync player tournament statistics and save games upon login.", style_bullet))

    story.append(Spacer(1, 25))
    story.append(HRFlowable(width="100%", thickness=0.5, color=colors.HexColor("#cbd5e1"), spaceAfter=15))
    story.append(Paragraph("--- END OF SOFTWARE REQUIREMENTS SPECIFICATION DOCUMENT ---<br/>Gaming Cafe & Esports Arena Management System • Version 1.0 • Baselined September 2026", ParagraphStyle('EndDoc', fontName='Helvetica-Bold', fontSize=9, leading=13, textColor=colors.HexColor("#64748b"), alignment=1)))

    doc.build(story, canvasmaker=NumberedCanvas)
    print(f"Successfully compiled professional PDF at: {os.path.abspath(filename)}")

if __name__ == '__main__':
    build_pdf()
