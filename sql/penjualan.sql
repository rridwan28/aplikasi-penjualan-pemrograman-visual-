-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: May 30, 2026 at 04:18 PM
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
-- Table structure for table `isi`
--

CREATE TABLE `isi` (
  `idnota` varchar(20) NOT NULL,
  `kd_barang` varchar(20) NOT NULL,
  `harga_beli` int(11) NOT NULL,
  `harga_jual` int(11) NOT NULL,
  `qty` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `isi`
--

INSERT INTO `isi` (`idnota`, `kd_barang`, `harga_beli`, `harga_jual`, `qty`) VALUES
('IN0001', 'B001', 5000, 6000, 2),
('IN0001', 'B004', 6000, 7500, 12),
('IN0002', 'B001', 5000, 6000, 4),
('IN0002', 'B003', 2200, 3500, 3),
('IN0003', 'B003', 2200, 3500, 2),
('IN0003', 'B004', 6000, 7500, 12),
('IN0004', 'B003', 2200, 3500, 12),
('IN0004', 'B004', 6000, 7500, 11),
('IN0005', 'B001', 5000, 6000, 12),
('IN0005', 'B003', 2200, 3500, 5),
('IN0006', 'B003', 2200, 3500, 12),
('IN0006', 'B004', 6000, 7500, 4);

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
-- Table structure for table `nota`
--

CREATE TABLE `nota` (
  `idnota` varchar(20) NOT NULL,
  `tgl_nota` date NOT NULL,
  `id_pelanggan` varchar(20) NOT NULL,
  `total_harga` int(11) NOT NULL,
  `id_kasir` varchar(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `nota`
--

INSERT INTO `nota` (`idnota`, `tgl_nota`, `id_pelanggan`, `total_harga`, `id_kasir`) VALUES
('IN0001', '2026-05-30', 'ID001', 102000, ''),
('IN0002', '2026-05-30', 'ID001', 34500, 'K001'),
('IN0003', '2026-05-30', 'ID004', 97000, 'K001'),
('IN0004', '2026-05-30', 'ID002', 124500, 'K001'),
('IN0005', '2026-05-30', 'ID002', 89500, 'K001'),
('IN0006', '2026-05-30', 'ID001', 72000, 'K001');

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
-- Indexes for table `isi`
--
ALTER TABLE `isi`
  ADD PRIMARY KEY (`idnota`,`kd_barang`);

--
-- Indexes for table `kasir`
--
ALTER TABLE `kasir`
  ADD PRIMARY KEY (`id_kasir`);

--
-- Indexes for table `nota`
--
ALTER TABLE `nota`
  ADD PRIMARY KEY (`idnota`);

--
-- Indexes for table `pelanggan`
--
ALTER TABLE `pelanggan`
  ADD PRIMARY KEY (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
