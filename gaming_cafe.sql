-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Sep 24, 2026 at 12:27 PM
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
  `start_time` datetime NOT NULL,
  `end_time` datetime DEFAULT NULL,
  `add_minutes` int(11) NOT NULL DEFAULT 0,
  `price_per_adding` decimal(10,2) DEFAULT NULL,
  `total_minutes` int(11) DEFAULT 0,
  `total_amount` decimal(10,2) DEFAULT 0.00,
  `status` varchar(20) NOT NULL DEFAULT 'Ongoing'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

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
  `status` varchar(20) NOT NULL DEFAULT 'Available'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `pc_table`
--

INSERT INTO `pc_table` (`pc_id`, `pc_name`, `category`, `cpu`, `motherboard`, `ram_capacity`, `vga`, `ip_address`, `hourly_rate`, `status`) VALUES
('PC-GG-1001', 'PC-01', 'PC', 'Intel Core i5-12400F', 'MSI PRO B660M-A', '16GB DDR4', 'NVIDIA RTX 3060 12GB', '192.168.1.101', 120.00, 'Available'),
('PC-GG-1002', 'PC-02', 'PC', 'Intel Core i5-12400F', 'MSI PRO B660M-A', '16GB DDR4', 'NVIDIA RTX 3060 12GB', '192.168.1.102', 120.00, 'Available'),
('PC-GG-1003', 'PC-03', 'PC', 'AMD Ryzen 5 5600X', 'ASUS TUF B550M-PLUS', '16GB DDR4', 'NVIDIA RTX 4060 8GB', '192.168.1.103', 150.00, 'Occupied'),
('PC-GG-1004', 'PC-04', 'PC', 'AMD Ryzen 5 5600X', 'ASUS TUF B550M-PLUS', '16GB DDR4', 'NVIDIA RTX 4060 8GB', '192.168.1.104', 150.00, 'Available'),
('PC-GG-1005', 'PC-05', 'PC', 'Intel Core i5-13400F', 'Gigabyte B760M DS3H', '32GB DDR5', 'NVIDIA RTX 4060 Ti 8GB', '192.168.1.105', 180.00, 'Available'),
('PC-GG-1006', 'PC-06', 'PC', 'Intel Core i7-13700K', 'ASUS ROG Strix Z790-F', '32GB DDR5', 'NVIDIA RTX 4070 Ti 12GB', '192.168.1.106', 250.00, 'Available'),
('PC-GG-1007', 'PC-07', 'PC', 'AMD Ryzen 7 7800X3D', 'MSI MAG X670E TOMAHAWK', '32GB DDR5', 'NVIDIA RTX 4080 Super 16GB', '192.168.1.107', 300.00, 'Available'),
('PC-GG-1008', 'PC-08', 'PC', 'AMD Ryzen 5 3600', 'ASRock B450 Pro4', '16GB DDR4', 'NVIDIA GTX 1660 Super 6GB', '192.168.1.108', 100.00, 'Repair'),
('PC-GG-6238', 'PC-09', 'PC', 'fcsdfsd', 'fsdfsdf', '32', '16', '192.168.3.6', 520.00, 'Available'),
('PC-GG-9156', 'PC-10', 'PC', 'rwef', 'wefew', '12', '4', '192.168.12.3', 45.00, 'Available'),
('PS5-GG-2001', 'PS5-01', 'PS5', 'Custom AMD Zen 2 8-core', 'Sony Custom Oberon', '16GB GDDR6', 'Custom RDNA 2 10.3 TFLOPS', '192.168.1.151', 200.00, 'Available'),
('PS5-GG-2002', 'PS5-02', 'PS5', 'Custom AMD Zen 2 8-core', 'Sony Custom Oberon', '16GB GDDR6', 'Custom RDNA 2 10.3 TFLOPS', '192.168.1.152', 200.00, 'Available'),
('PS5-GG-2003', 'PS5-03', 'PS5', 'Custom AMD Zen 2 8-core', 'Sony Custom Oberon', '16GB GDDR6', 'Custom RDNA 2 10.3 TFLOPS', '192.168.1.153', 200.00, 'Occupied'),
('PS5-GG-2004', 'PS5-04', 'PS5', 'Custom AMD Zen 2 8-core', 'Sony Custom Oberon', '16GB GDDR6', 'Custom RDNA 2 10.3 TFLOPS', '192.168.1.154', 200.00, 'Available'),
('PS5-GG-3710', 'PS5-505', 'PS5', 'rdwer', 'rwerwe', '15', '26', '192.168.5.6', 23.00, 'Available');

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
(1, 'GG_1234', 'Damsith', 'Dewmina', '200524402775', 'dama@gmail.com', '0701052405', 'dama', 'dama123', 'Admin', 1, NULL, 0, '2026-09-20 11:04:07'),
(2, 'USR-GG-6699', 'dammmm', 'sasdefe', '200524402775', 'da@gmail.com', '0701052405', '200524402775', 'c77f5926f7529ac2114ffc2aa4933deb7eeb1bc17f93d2b3eadadb74cfc9822d', 'Admin', 1, NULL, 1, '2026-09-20 08:01:38'),
(3, 'USR-GG-5352', 'chamika', 'sandeepa', '200625103468', 'chiki@gmail.com', '0701052405', '200625103468', '8aed01af125c7e65971764d6d38daf45cfd96f91ccf0dbdd18bdecfe0836be3d', 'User', 1, NULL, 1, '2026-09-20 11:01:44');

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
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `pc_maintenance`
--
ALTER TABLE `pc_maintenance`
  MODIFY `repair_id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `user`
--
ALTER TABLE `user`
  MODIFY `id` int(3) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

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
