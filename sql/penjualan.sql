-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Apr 12, 2026 at 05:03 PM
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
-- Database: `penjualan`
--

-- --------------------------------------------------------

--
-- Table structure for table `pelanggan`
--

CREATE TABLE `pelanggan` (
  `id` varchar(10) NOT NULL,
  `nmplgn` varchar(25) NOT NULL,
  `jenis` varchar(25) NOT NULL,
  `telepon` varchar(25) NOT NULL,
  `alamat` varchar(300) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `pelanggan`
--

INSERT INTO `pelanggan` (`id`, `nmplgn`, `jenis`, `telepon`, `alamat`) VALUES
('ID001', 'Budi Santoso', 'Laki - Laki', '081234567890', 'Jakarta'),
('ID002', 'Siti Rahayu', 'Perempuan', '082345678901', 'Bandung'),
('ID003', 'Ahmad Fauzi', 'Laki - Laki', '083456789012', 'Surabaya'),
('ID004', 'Dewi Anggraini', 'Perempuan', '084567890123', 'Yogyakarta'),
('ID005', 'Riko Pratama', 'Laki - Laki', '085678901234', 'Medan'),
('ID006', 'Nurul Hidayah', 'Perempuan', '086789012345', 'Semarang'),
('ID007', 'Dimas Ardiansyah', 'Laki - Laki', '087890123456', 'Makassar'),
('ID009', 'Hendra Gunawan', 'Laki - Laki', '089012345678', 'Denpasar'),
('ID010', 'Fitri Handayani', 'Perempuan', '081122334455', 'Manado'),
('ID012', 'Reisya S', 'Perempuan', '0897613481201', 'Bogor'),
('ID013', 'Ridwan Nugraha', 'Laki - Laki', '08172639121219', 'Bekasi'),
('ID014', 'Raffi', 'Laki - Laki', '098761252318', 'Jakarta');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `pelanggan`
--
ALTER TABLE `pelanggan`
  ADD PRIMARY KEY (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
