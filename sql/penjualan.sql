-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Apr 25, 2026 at 05:32 PM
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
-- Table structure for table `barang`
--

CREATE TABLE `barang` (
  `kd_barang` varchar(20) NOT NULL,
  `nm_brg` varchar(40) NOT NULL,
  `jenis` enum('Makanan','Minuman','','') NOT NULL,
  `hargabeli` decimal(10,0) NOT NULL,
  `hargajual` decimal(10,0) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `barang`
--

INSERT INTO `barang` (`kd_barang`, `nm_brg`, `jenis`, `hargabeli`, `hargajual`) VALUES
('B001', 'Coca Cola', 'Minuman', 5000, 6000),
('B003', 'Mie Goreng', 'Makanan', 2200, 3500),
('B004', 'Milo', 'Minuman', 6000, 7500);

-- --------------------------------------------------------

--
-- Table structure for table `kasir`
--

CREATE TABLE `kasir` (
  `id_kasir` varchar(10) NOT NULL,
  `nm_kasir` varchar(25) NOT NULL,
  `jenis_kelamin` varchar(25) NOT NULL,
  `no_telepon` varchar(25) NOT NULL,
  `agama` varchar(25) NOT NULL,
  `alamat` varchar(25) NOT NULL,
  `password` varchar(15) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `kasir`
--

INSERT INTO `kasir` (`id_kasir`, `nm_kasir`, `jenis_kelamin`, `no_telepon`, `agama`, `alamat`, `password`) VALUES
('K001', 'Ridwan', 'Laki-Laki', '0812345678', 'Islam', 'Kota Bekasi', 'ridwan123');

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
('ID013', 'Ridwan Nugraha', 'Laki - Laki', '08172639121219', 'Bekasi');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `barang`
--
ALTER TABLE `barang`
  ADD PRIMARY KEY (`kd_barang`);

--
-- Indexes for table `kasir`
--
ALTER TABLE `kasir`
  ADD PRIMARY KEY (`id_kasir`);

--
-- Indexes for table `pelanggan`
--
ALTER TABLE `pelanggan`
  ADD PRIMARY KEY (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
