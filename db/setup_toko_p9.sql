-- ============================================================
-- Setup Database Pertemuan 9 - Aplikasi Penjualan Barang
-- M. Fikri Avishena Parinduri - NIM: 231011401029
-- Jalankan di MariaDB sebelum run aplikasi
-- ============================================================

CREATE DATABASE IF NOT EXISTS toko_p9;
USE toko_p9;

-- Tabel Supplier
CREATE TABLE IF NOT EXISTS supplier (
    id_supplier   VARCHAR(10)  NOT NULL PRIMARY KEY,
    nama_supplier VARCHAR(50),
    alamat        VARCHAR(100),
    telepon       VARCHAR(20)
);

-- Tabel Barang
CREATE TABLE IF NOT EXISTS barang (
    kode_barang VARCHAR(10)  NOT NULL PRIMARY KEY,
    nama_barang VARCHAR(50),
    harga_beli  DOUBLE       DEFAULT 0,
    harga_jual  DOUBLE       DEFAULT 0,
    stok        INT          DEFAULT 0,
    id_supplier VARCHAR(10),
    FOREIGN KEY (id_supplier) REFERENCES supplier(id_supplier)
);

-- Tabel Customer
CREATE TABLE IF NOT EXISTS customer (
    id_customer   VARCHAR(10)  NOT NULL PRIMARY KEY,
    nama_customer VARCHAR(50),
    alamat        VARCHAR(100),
    telepon       VARCHAR(20)
);

-- Tabel Transaksi
CREATE TABLE IF NOT EXISTS transaksi (
    no_transaksi VARCHAR(15)  NOT NULL PRIMARY KEY,
    tanggal      DATE,
    id_customer  VARCHAR(10),
    kode_barang  VARCHAR(10),
    jumlah       INT          DEFAULT 1,
    total_harga  DOUBLE       DEFAULT 0,
    FOREIGN KEY (id_customer) REFERENCES customer(id_customer),
    FOREIGN KEY (kode_barang) REFERENCES barang(kode_barang)
);

-- ── Data Sample ──────────────────────────────────────────
INSERT IGNORE INTO supplier VALUES
('SUP001', 'PT Fikri',  'Medan',  '061-1234567'),
('SUP002', 'PT Avishena', 'Pekanbaru',  '0761-9876543');

INSERT IGNORE INTO barang VALUES
('BRG001', 'Laptop Asus',    5000000, 6500000, 10, 'SUP001'),
('BRG002', 'Mouse Wireless', 80000,   120000,  25, 'SUP001'),
('BRG003', 'Keyboard USB',   65000,   95000,   8,  'SUP002'),
('BRG004', 'Monitor 24"',    1500000, 1900000, 5,  'SUP002');

INSERT IGNORE INTO customer VALUES
('CST001', 'Parinduri',  'Depok',    '08111111111'),
('CST002', 'Avishena',   'Bekasi',   '08222222222'),
('CST003', 'Fikri',   'Tangerang','08333333333');

SELECT 'Database toko_p9 berhasil dibuat!' AS info;
SHOW TABLES;
