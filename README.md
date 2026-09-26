# 🎮 Gaming Cafe Management System

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Java Swing](https://img.shields.io/badge/Java_Swing-GUI-0466c8?style=for-the-badge)
![MySQL](https://img.shields.io/badge/MySQL-005C84?style=for-the-badge&logo=mysql&logoColor=white)
![NetBeans](https://img.shields.io/badge/Apache_NetBeans-1B6AC6?style=for-the-badge&logo=apachenetbeanside&logoColor=white)

A modern, full-featured desktop application built with **Java Swing** and **MySQL** designed to streamline daily operations, PC station tracking, and staff management in a Gaming Cafe environment.

---

## ✨ Key Features

* **🖥️ Live PC & Station Management:**
  * Track real-time PC availability and live gaming sessions.
  * **Smart Live Search:** Instantaneous table filtering by PC ID or Name with automatic numeric extraction and ordered results (`ASC`).
* **📱 Automated QR Code Generation:**
  * Dynamically generates unique QR codes for PCs/sessions.
  * Cross-platform file handling that automatically creates and stores QR images inside the user's `Downloads/GamingCafe_QRs` directory.
* **🛡️ Role-Based Access Control (RBAC):**
  * Secure authentication flow with dynamic UI rendering.
  * Restricts sensitive modules (such as the **Admin** panel) exclusively to Administrator accounts while hiding them from standard staff.
* **👤 Comprehensive User & Staff Profiles:**
  * Manage employee and user data including Employee No, First/Last Name, Username, NIC, Email, and Phone Number[cite: 2].
  * **Database Image Handling:** Loads user profile pictures directly from database `BLOB` (`byte[]`) storage with smooth automatic scaling to fit UI components[cite: 2].
* **🎨 Modern Custom UI & Interactive Settings:**
  * Sleek dark-mode interface accented with Royal Blue (`#0466c8`) and Metallic Gold (`#D4AF37`).
  * Dedicated navigation sidebar featuring **Dashboard**, **Live**, **Pc**, **Reports**, **Admin**, **Settings**, and **LogOut** modules.
  * Interactive Settings panel with real-time component state control (e.g., checkbox-driven toggle button states).

---

## 🛠️ Tech Stack & Tools

* **Language:** Java (JDK 8+)
* **Frontend / GUI:** Java Swing (Custom styled `JFrame`, `JPanel`, `JTable`, `JToggleButton`)
* **Database:** MySQL (via JDBC `PreparedStatement` & `ResultSet`)
* **IDE:** Apache NetBeans

---

## 📸 Screenshots

> *(Add your project screenshots here)*
> 
> `![Dashboard Screenshot](screenshots/dashboard.png)`
> `![Settings & Profile](screenshots/settings.png)`

---

## 🚀 Getting Started (Installation)

Follow these steps to run the project locally on your machine:

### 1. Prerequisites
* Java Development Kit (JDK) installed.
* MySQL Server (XAMPP / WAMP / MySQL Workbench).
* Apache NetBeans IDE.

### 2. Setup Steps
1. **Clone the repository:**
   ```bash
   git clone [https://github.com/your-username/gaming-cafe-management.git](https://github.com/your-username/gaming-cafe-management.git)
