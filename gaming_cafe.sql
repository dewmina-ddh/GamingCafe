-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Sep 27, 2026 at 03:48 PM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `gaming_cafe`
--

-- --------------------------------------------------------

--
-- Table structure for table `gaming_sessions`
--

CREATE TABLE `gaming_sessions` (
  `session_id` varchar(20) NOT NULL,
  `pc_id` varchar(20) NOT NULL,
  `cus_name` varchar(100) NOT NULL DEFAULT 'Walk-in Customer',
  `phone` varchar(20) DEFAULT '',
  `start_time` datetime NOT NULL,
  `end_time` datetime DEFAULT NULL,
  `add_minutes` int(11) DEFAULT 0,
  `price_per_adding` decimal(10,2) DEFAULT 0.00,
  `total_minutes` int(11) DEFAULT 0,
  `total_amount` decimal(10,2) DEFAULT 0.00,
  `status` varchar(20) DEFAULT 'Ongoing'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `gaming_sessions`
--

INSERT INTO `gaming_sessions` (`session_id`, `pc_id`, `cus_name`, `phone`, `start_time`, `end_time`, `add_minutes`, `price_per_adding`, `total_minutes`, `total_amount`, `status`) VALUES
('SES-37908', 'PC-GG-5543', 'Walk-in Customer', '0701052405', '2026-09-27 18:13:57', '2026-09-27 18:19:39', 0, 0.00, 90, 78.00, 'Completed'),
('SES-815795', 'PC-GG-5543', 'Walk-in Customer (0701052405)', '0701052405', '2026-09-27 16:13:35', '2026-09-27 16:14:20', 0, 0.00, 1, 0.87, 'Completed');

-- --------------------------------------------------------

--
-- Table structure for table `invoices`
--

CREATE TABLE `invoices` (
  `invoice_no` varchar(20) NOT NULL,
  `session_id` varchar(20) NOT NULL,
  `emp_no` varchar(20) NOT NULL,
  `sub_total` decimal(10,2) NOT NULL,
  `discount` decimal(10,2) DEFAULT 0.00,
  `net_total` decimal(10,2) NOT NULL,
  `payment_method` varchar(20) DEFAULT 'Cash',
  `invoice_date` datetime DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `invoices`
--

INSERT INTO `invoices` (`invoice_no`, `session_id`, `emp_no`, `sub_total`, `discount`, `net_total`, `payment_method`, `invoice_date`) VALUES
('INV-379294', 'SES-37908', 'GG_1234', 78.00, 0.00, 78.00, 'Cash', '2026-09-27 18:19:39'),
('INV-860028', 'SES-815795', 'GG_1234', 0.87, 0.00, 0.87, 'Cash', '2026-09-27 16:14:20');

-- --------------------------------------------------------

--
-- Table structure for table `pc_maintenance`
--

CREATE TABLE `pc_maintenance` (
  `repair_id` int(11) NOT NULL,
  `pc_id` varchar(20) NOT NULL,
  `issue_description` varchar(255) NOT NULL,
  `cost` decimal(10,2) NOT NULL DEFAULT 0.00,
  `repair_date` datetime NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `pc_maintenance`
--

INSERT INTO `pc_maintenance` (`repair_id`, `pc_id`, `issue_description`, `cost`, `repair_date`) VALUES
(1, 'PC-GG-5543', 'frggfdg', 25.00, '2026-09-27 13:49:15');

-- --------------------------------------------------------

--
-- Table structure for table `pc_table`
--

CREATE TABLE `pc_table` (
  `pc_id` varchar(20) NOT NULL,
  `pc_name` varchar(50) NOT NULL,
  `category` varchar(20) DEFAULT 'PC',
  `cpu` varchar(50) DEFAULT NULL,
  `motherboard` varchar(50) DEFAULT NULL,
  `ram_capacity` varchar(20) DEFAULT NULL,
  `vga` varchar(50) DEFAULT NULL,
  `ip_address` varchar(20) DEFAULT NULL,
  `hourly_rate` decimal(10,2) NOT NULL,
  `status` varchar(20) NOT NULL DEFAULT 'Available',
  `ip_block` tinyint(4) NOT NULL DEFAULT 0,
  `recorded_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `pc_table`
--

INSERT INTO `pc_table` (`pc_id`, `pc_name`, `category`, `cpu`, `motherboard`, `ram_capacity`, `vga`, `ip_address`, `hourly_rate`, `status`, `ip_block`, `recorded_at`) VALUES
('PC-GG-5543', 'PC-02', 'PC', 'gergf', 'ergregr', '12', '4', '192.168.5.3', 52.00, 'Available', 0, '2026-09-27 12:49:39');

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

CREATE TABLE `user` (
  `id` int(3) NOT NULL,
  `emp_no` varchar(20) NOT NULL,
  `f_name` varchar(100) NOT NULL,
  `l_name` varchar(100) NOT NULL,
  `nic` varchar(12) NOT NULL,
  `email` varchar(100) NOT NULL,
  `phone` varchar(10) NOT NULL,
  `username` varchar(50) NOT NULL,
  `password` varchar(255) NOT NULL,
  `role` varchar(20) NOT NULL,
  `status` tinyint(1) NOT NULL DEFAULT 1,
  `image` varchar(255) DEFAULT NULL,
  `changePass` tinyint(4) NOT NULL DEFAULT 1,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `user`
--

INSERT INTO `user` (`id`, `emp_no`, `f_name`, `l_name`, `nic`, `email`, `phone`, `username`, `password`, `role`, `status`, `image`, `changePass`, `created_at`) VALUES
(1, 'GG_1234', 'Damsith', 'Dewmina', '200524402775', 'dama@gmail.com', '0701052405', 'dama', 'b66585d6a56de3ced2f860dc8f0c964c6ee9c016afa4ce90c27ae1f8405c3315', 'Admin', 1, NULL, 0, '2026-09-27 08:03:17'),
(3, 'USR-GG-5352', 'chamika', 'sandeepa', '200625103468', 'chiki@gmail.com', '0701052405', 'chiki', '4eff6334453a1a6cdee437d2327efa8eb6fec685ab90aa2da07958db8551d255', 'Inactive', 1, NULL, 1, '2026-09-27 12:48:46');

-- --------------------------------------------------------

--
-- Table structure for table `user_logs`
--

CREATE TABLE `user_logs` (
  `log_id` int(11) NOT NULL,
  `emp_no` varchar(50) DEFAULT NULL,
  `username` varchar(100) DEFAULT NULL,
  `role` varchar(50) DEFAULT NULL,
  `login_time` datetime DEFAULT current_timestamp(),
  `logout_time` datetime DEFAULT NULL,
  `duration` varchar(50) DEFAULT 'Active',
  `status` varchar(50) DEFAULT 'Logged In'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `user_logs`
--

INSERT INTO `user_logs` (`log_id`, `emp_no`, `username`, `role`, `login_time`, `logout_time`, `duration`, `status`) VALUES
(1, 'GG_1234', 'dama', 'Admin', '2026-09-27 15:40:51', '2026-09-27 16:04:05', '23 mins', 'Logged Out'),
(2, 'GG_1234', 'dama', 'Admin', '2026-09-27 15:47:13', '2026-09-27 15:47:50', '0 mins', 'Logged Out'),
(3, 'GG_1234', 'dama', 'Admin', '2026-09-27 15:47:55', '2026-09-27 15:48:47', '0 mins', 'Logged Out'),
(4, 'GG_1234', 'dama', 'Admin', '2026-09-27 16:07:56', '2026-09-27 16:11:43', '3 mins', 'Logged Out'),
(5, 'GG_1234', 'dama', 'Admin', '2026-09-27 16:13:21', '2026-09-27 16:23:17', '9 mins', 'Logged Out'),
(6, 'GG_1234', 'dama', 'Admin', '2026-09-27 16:25:03', '2026-09-27 16:25:57', '0 mins', 'Logged Out'),
(7, 'GG_1234', 'dama', 'Admin', '2026-09-27 16:31:37', NULL, 'Active', 'Logged In'),
(8, 'GG_1234', 'dama', 'Admin', '2026-09-27 16:40:16', '2026-09-27 16:40:38', '0 mins', 'Logged Out'),
(9, 'GG_1234', 'dama', 'Admin', '2026-09-27 16:46:49', NULL, 'Active', 'Logged In'),
(10, 'GG_1234', 'dama', 'Admin', '2026-09-27 18:13:20', '2026-09-27 18:21:48', '8 mins', 'Logged Out'),
(11, 'GG_1234', 'dama', 'Admin', '2026-09-27 18:35:04', '2026-09-27 18:43:46', '8 mins', 'Logged Out'),
(12, 'GG_1234', 'dama', 'Admin', '2026-09-27 18:46:22', '2026-09-27 18:47:49', '1 mins', 'Logged Out');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `gaming_sessions`
--
ALTER TABLE `gaming_sessions`
  ADD PRIMARY KEY (`session_id`),
  ADD KEY `pc_id` (`pc_id`);

--
-- Indexes for table `invoices`
--
ALTER TABLE `invoices`
  ADD PRIMARY KEY (`invoice_no`),
  ADD KEY `session_id` (`session_id`),
  ADD KEY `emp_no` (`emp_no`);

--
-- Indexes for table `pc_maintenance`
--
ALTER TABLE `pc_maintenance`
  ADD PRIMARY KEY (`repair_id`),
  ADD KEY `pc_id` (`pc_id`);

--
-- Indexes for table `pc_table`
--
ALTER TABLE `pc_table`
  ADD PRIMARY KEY (`pc_id`);

--
-- Indexes for table `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `emp_no` (`emp_no`);

--
-- Indexes for table `user_logs`
--
ALTER TABLE `user_logs`
  ADD PRIMARY KEY (`log_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `pc_maintenance`
--
ALTER TABLE `pc_maintenance`
  MODIFY `repair_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `user`
--
ALTER TABLE `user`
  MODIFY `id` int(3) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `user_logs`
--
ALTER TABLE `user_logs`
  MODIFY `log_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `gaming_sessions`
--
ALTER TABLE `gaming_sessions`
  ADD CONSTRAINT `gaming_sessions_ibfk_1` FOREIGN KEY (`pc_id`) REFERENCES `pc_table` (`pc_id`) ON UPDATE CASCADE;

--
-- Constraints for table `invoices`
--
ALTER TABLE `invoices`
  ADD CONSTRAINT `invoices_ibfk_1` FOREIGN KEY (`session_id`) REFERENCES `gaming_sessions` (`session_id`) ON UPDATE CASCADE,
  ADD CONSTRAINT `invoices_ibfk_2` FOREIGN KEY (`emp_no`) REFERENCES `user` (`emp_no`) ON UPDATE CASCADE;

--
-- Constraints for table `pc_maintenance`
--
ALTER TABLE `pc_maintenance`
  ADD CONSTRAINT `pc_maintenance_ibfk_1` FOREIGN KEY (`pc_id`) REFERENCES `pc_table` (`pc_id`) ON DELETE CASCADE ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
