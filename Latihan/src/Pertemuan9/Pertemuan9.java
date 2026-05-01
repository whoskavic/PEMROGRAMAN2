package Pertemuan9;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Pertemuan9 extends JFrame {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Pertemuan9().setVisible(true));
    }

    // color tone
    static final Color THEME      = new Color(145, 190, 201);
    static final Color THEME_DARK = new Color(100, 155, 168);
    static final Color THEME_LITE = new Color(210, 235, 240);
    static final Color WHITE      = Color.WHITE;
    static final Font  FONT_LBL   = new Font("Arial", Font.PLAIN, 12);
    static final Font  FONT_BOLD  = new Font("Arial", Font.BOLD, 12);
    static final Font  FONT_TITLE = new Font("Arial", Font.BOLD, 14);

    // koneksi db
    static final String DB_URL  = "jdbc:mariadb://localhost:3306/toko_p9"
            + "?useSSL=false&restrictedAuth=mysql_native_password";
    static final String DB_USER = "root";
    static final String DB_PASS = "sa";

    static Connection getConnection() throws SQLException {
        try { Class.forName("org.mariadb.jdbc.Driver"); }
        catch (ClassNotFoundException e) { throw new SQLException("Driver tidak ditemukan"); }
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
    }

    // ctor
    public Pertemuan9() {
        setTitle("Aplikasi Penjualan Barang - M. Fikri Avishena Parinduri (231011401029)");
        setSize(850, 620);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Header
        JPanel header = new JPanel(null);
        header.setBackground(THEME_DARK);
        header.setPreferredSize(new Dimension(850, 55));

        JLabel lblApp = new JLabel("Aplikasi Penjualan Barang");
        lblApp.setFont(new Font("Arial", Font.BOLD, 18));
        lblApp.setForeground(WHITE);
        lblApp.setBounds(20, 8, 400, 25);
        header.add(lblApp);

        JLabel lblInfo = new JLabel("M. Fikri Avishena Parinduri  |  NIM: 231011401029");
        lblInfo.setFont(new Font("Arial", Font.PLAIN, 11));
        lblInfo.setForeground(THEME_LITE);
        lblInfo.setBounds(20, 32, 500, 18);
        header.add(lblInfo);

        add(header, BorderLayout.NORTH);

        // Tabs
        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(FONT_BOLD);
        tabs.setBackground(THEME_LITE);

        tabs.addTab("Dashboard",    new PanelDashboard());
        tabs.addTab("Barang",       new PanelBarang());
        tabs.addTab("Customer",     new PanelCustomer());
        tabs.addTab("Supplier",     new PanelSupplier());
        tabs.addTab("Transaksi",    new PanelTransaksi());
        tabs.addTab("Inventory",    new PanelInventory());
        tabs.addTab("Laporan",     new PanelLaporan());

        add(tabs, BorderLayout.CENTER);
    }

    // ══════════════════════════════════════════════════════
    // HELPER: buat JLabel + JTextField dalam satu baris
    // ══════════════════════════════════════════════════════
    static JTextField addField(JPanel p, String label, int x, int y, int w) {
        JLabel lbl = new JLabel(label);
        lbl.setFont(FONT_LBL);
        lbl.setBounds(x, y, 90, 25);
        p.add(lbl);
        JTextField tf = new JTextField();
        tf.setBounds(x + 95, y, w, 25);
        p.add(tf);
        return tf;
    }

    static JButton makeBtn(String text) {
        JButton b = new JButton(text);
        b.setBackground(THEME);
        b.setForeground(WHITE);
        b.setFont(FONT_BOLD);
        b.setFocusPainted(false);
        b.setBorder(BorderFactory.createEmptyBorder(4, 10, 4, 10));
        return b;
    }

    static DefaultTableModel makeModel(String[] cols) {
        return new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
    }

    static JScrollPane makeTable(JTable t, int x, int y, int w, int h) {
        t.setSelectionBackground(THEME);
        t.setSelectionForeground(WHITE);
        t.getTableHeader().setBackground(THEME_DARK);
        t.getTableHeader().setForeground(WHITE);
        t.getTableHeader().setFont(FONT_BOLD);
        t.setFont(FONT_LBL);
        t.setRowHeight(22);
        JScrollPane sp = new JScrollPane(t);
        sp.setBounds(x, y, w, h);
        return sp;
    }

    // ══════════════════════════════════════════════════════
    // DASHBOARD
    // ══════════════════════════════════════════════════════
    static class PanelDashboard extends JPanel {
        JLabel lblBarang, lblCustomer, lblSupplier, lblTransaksi;

        PanelDashboard() {
            setLayout(null);
            setBackground(THEME_LITE);

            JLabel title = new JLabel("Dashboard Ringkasan", SwingConstants.CENTER);
            title.setFont(new Font("Arial", Font.BOLD, 16));
            title.setBounds(0, 20, 830, 30);
            add(title);

            lblBarang    = addCard("Total Barang",    "0", 30,  80);
            lblCustomer  = addCard("Total Customer",  "0", 230, 80);
            lblSupplier  = addCard("Total Supplier",  "0", 430, 80);
            lblTransaksi = addCard("Total Transaksi", "0", 630, 80);

            JButton btnRefresh = makeBtn("Refresh");
            btnRefresh.setBounds(350, 200, 130, 35);
            btnRefresh.addActionListener(e -> refresh());
            add(btnRefresh);

            refresh();
        }

        JLabel addCard(String title, String val, int x, int y) {
            JPanel card = new JPanel(null);
            card.setBounds(x, y, 175, 90);
            card.setBackground(THEME_DARK);
            card.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));

            JLabel lTitle = new JLabel(title, SwingConstants.CENTER);
            lTitle.setFont(new Font("Arial", Font.BOLD, 11));
            lTitle.setForeground(THEME_LITE);
            lTitle.setBounds(0, 10, 175, 20);
            card.add(lTitle);

            JLabel lVal = new JLabel(val, SwingConstants.CENTER);
            lVal.setFont(new Font("Arial", Font.BOLD, 28));
            lVal.setForeground(WHITE);
            lVal.setBounds(0, 35, 175, 40);
            card.add(lVal);

            add(card);
            return lVal;
        }

        void refresh() {
            String[][] queries = {
                {"SELECT COUNT(*) FROM barang",    null},
                {"SELECT COUNT(*) FROM customer",  null},
                {"SELECT COUNT(*) FROM supplier",  null},
                {"SELECT COUNT(*) FROM transaksi", null}
            };
            JLabel[] labels = {lblBarang, lblCustomer, lblSupplier, lblTransaksi};
            try (Connection k = getConnection()) {
                for (int i = 0; i < queries.length; i++) {
                    try (Statement st = k.createStatement();
                         ResultSet rs = st.executeQuery(queries[i][0])) {
                        if (rs.next()) labels[i].setText(rs.getString(1));
                    } catch (SQLException ignored) {
                        labels[i].setText("?");
                    }
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Gagal koneksi DB: " + e.getMessage());
            }
        }
    }

    // ══════════════════════════════════════════════════════
    // PANEL BARANG
    // ══════════════════════════════════════════════════════
    static class PanelBarang extends JPanel {
        JTextField txtKode, txtNama, txtHargaBeli, txtHargaJual, txtStok;
        JTextField txtIdSupplier;
        DefaultTableModel model;
        JTable table;

        PanelBarang() {
            setLayout(null);
            setBackground(WHITE);

            // Form input
            JPanel form = new JPanel(null);
            form.setBounds(10, 10, 400, 210);
            form.setBackground(THEME_LITE);
            form.setBorder(BorderFactory.createTitledBorder("Input Data Barang"));

            txtKode       = addField(form, "Kode Barang",  10, 25,  180);
            txtNama       = addField(form, "Nama Barang",  10, 55,  180);
            txtHargaBeli  = addField(form, "Harga Beli",   10, 85,  180);
            txtHargaJual  = addField(form, "Harga Jual",   10, 115, 180);
            txtStok       = addField(form, "Stok Awal",    10, 145, 180);
            txtIdSupplier = addField(form, "ID Supplier",  10, 175, 180);

            add(form);

            // Tombol
            JButton btnSimpan   = makeBtn("Simpan");
            JButton btnUpdate   = makeBtn("Update");
            JButton btnHapus    = makeBtn("Hapus");
            JButton btnBersih   = makeBtn("Bersihkan");

            btnSimpan.setBounds(10,  225, 110, 30);
            btnUpdate.setBounds(130, 225, 110, 30);
            btnHapus.setBounds(250,  225, 110, 30);
            btnBersih.setBounds(10,  260, 110, 30);

            btnSimpan.addActionListener(e -> simpan());
            btnUpdate.addActionListener(e -> update());
            btnHapus.addActionListener(e -> hapus());
            btnBersih.addActionListener(e -> bersih());

            add(btnSimpan); add(btnUpdate); add(btnHapus); add(btnBersih);

            // Tabel
            model = makeModel(new String[]{"Kode", "Nama", "Harga Beli", "Harga Jual", "Stok", "ID Supplier"});
            table = new JTable(model);
            add(makeTable(table, 10, 300, 800, 220));

            table.getSelectionModel().addListSelectionListener(e -> {
                if (!e.getValueIsAdjusting() && table.getSelectedRow() >= 0) {
                    int r = table.getSelectedRow();
                    txtKode.setText(model.getValueAt(r, 0).toString());
                    txtNama.setText(model.getValueAt(r, 1).toString());
                    txtHargaBeli.setText(model.getValueAt(r, 2).toString());
                    txtHargaJual.setText(model.getValueAt(r, 3).toString());
                    txtStok.setText(model.getValueAt(r, 4).toString());
                    txtIdSupplier.setText(model.getValueAt(r, 5).toString());
                }
            });

            tampil();
        }

        void simpan() {
            try (Connection k = getConnection();
                 PreparedStatement ps = k.prepareStatement(
                     "INSERT INTO barang(kode_barang,nama_barang,harga_beli,harga_jual,stok,id_supplier) VALUES(?,?,?,?,?,?)")) {
                ps.setString(1, txtKode.getText().trim());
                ps.setString(2, txtNama.getText().trim());
                ps.setDouble(3, Double.parseDouble(txtHargaBeli.getText().trim()));
                ps.setDouble(4, Double.parseDouble(txtHargaJual.getText().trim()));
                ps.setInt(5, Integer.parseInt(txtStok.getText().trim()));
                ps.setString(6, txtIdSupplier.getText().trim());
                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, "Barang berhasil disimpan!");
                bersih(); tampil();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
            }
        }

        void update() {
            try (Connection k = getConnection();
                 PreparedStatement ps = k.prepareStatement(
                     "UPDATE barang SET nama_barang=?,harga_beli=?,harga_jual=?,stok=?,id_supplier=? WHERE kode_barang=?")) {
                ps.setString(1, txtNama.getText().trim());
                ps.setDouble(2, Double.parseDouble(txtHargaBeli.getText().trim()));
                ps.setDouble(3, Double.parseDouble(txtHargaJual.getText().trim()));
                ps.setInt(4, Integer.parseInt(txtStok.getText().trim()));
                ps.setString(5, txtIdSupplier.getText().trim());
                ps.setString(6, txtKode.getText().trim());
                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, "Barang berhasil diupdate!");
                bersih(); tampil();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
            }
        }

        void hapus() {
            if (txtKode.getText().isBlank()) return;
            int c = JOptionPane.showConfirmDialog(this, "Yakin hapus barang ini?");
            if (c != JOptionPane.YES_OPTION) return;
            try (Connection k = getConnection();
                 PreparedStatement ps = k.prepareStatement("DELETE FROM barang WHERE kode_barang=?")) {
                ps.setString(1, txtKode.getText().trim());
                ps.executeUpdate();
                bersih(); tampil();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
            }
        }

        void tampil() {
            model.setRowCount(0);
            try (Connection k = getConnection();
                 Statement st = k.createStatement();
                 ResultSet rs = st.executeQuery("SELECT * FROM barang")) {
                while (rs.next()) {
                    model.addRow(new Object[]{
                        rs.getString("kode_barang"), rs.getString("nama_barang"),
                        rs.getDouble("harga_beli"),  rs.getDouble("harga_jual"),
                        rs.getInt("stok"),           rs.getString("id_supplier")
                    });
                }
            } catch (Exception e) { /* ignore jika tabel belum ada */ }
        }

        void bersih() {
            txtKode.setText(""); txtNama.setText(""); txtHargaBeli.setText("");
            txtHargaJual.setText(""); txtStok.setText(""); txtIdSupplier.setText("");
            table.clearSelection();
        }
    }

    // ══════════════════════════════════════════════════════
    // PANEL CUSTOMER
    // ══════════════════════════════════════════════════════
    static class PanelCustomer extends JPanel {
        JTextField txtId, txtNama, txtAlamat, txtTelp;
        DefaultTableModel model;
        JTable table;

        PanelCustomer() {
            setLayout(null);
            setBackground(WHITE);

            JPanel form = new JPanel(null);
            form.setBounds(10, 10, 400, 160);
            form.setBackground(THEME_LITE);
            form.setBorder(BorderFactory.createTitledBorder("Input Data Customer"));

            txtId     = addField(form, "ID Customer", 10, 25, 180);
            txtNama   = addField(form, "Nama",        10, 55, 180);
            txtAlamat = addField(form, "Alamat",      10, 85, 180);
            txtTelp   = addField(form, "Telepon",     10, 115, 180);
            add(form);

            JButton btnSimpan = makeBtn("Simpan");
            JButton btnUpdate = makeBtn("Update");
            JButton btnHapus  = makeBtn("Hapus");
            JButton btnBersih = makeBtn("Bersihkan");

            btnSimpan.setBounds(10, 178, 110, 30);
            btnUpdate.setBounds(130, 178, 110, 30);
            btnHapus.setBounds(250, 178, 110, 30);
            btnBersih.setBounds(370, 178, 110, 30);

            btnSimpan.addActionListener(e -> simpan());
            btnUpdate.addActionListener(e -> update());
            btnHapus.addActionListener(e -> hapus());
            btnBersih.addActionListener(e -> bersih());

            add(btnSimpan); add(btnUpdate); add(btnHapus); add(btnBersih);

            model = makeModel(new String[]{"ID", "Nama", "Alamat", "Telepon"});
            table = new JTable(model);
            add(makeTable(table, 10, 215, 800, 300));

            table.getSelectionModel().addListSelectionListener(e -> {
                if (!e.getValueIsAdjusting() && table.getSelectedRow() >= 0) {
                    int r = table.getSelectedRow();
                    txtId.setText(model.getValueAt(r, 0).toString());
                    txtNama.setText(model.getValueAt(r, 1).toString());
                    txtAlamat.setText(model.getValueAt(r, 2).toString());
                    txtTelp.setText(model.getValueAt(r, 3).toString());
                }
            });

            tampil();
        }

        void simpan() {
            try (Connection k = getConnection();
                 PreparedStatement ps = k.prepareStatement(
                     "INSERT INTO customer(id_customer,nama_customer,alamat,telepon) VALUES(?,?,?,?)")) {
                ps.setString(1, txtId.getText().trim());
                ps.setString(2, txtNama.getText().trim());
                ps.setString(3, txtAlamat.getText().trim());
                ps.setString(4, txtTelp.getText().trim());
                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, "Customer berhasil disimpan!");
                bersih(); tampil();
            } catch (Exception e) { JOptionPane.showMessageDialog(this, "Error: " + e.getMessage()); }
        }

        void update() {
            try (Connection k = getConnection();
                 PreparedStatement ps = k.prepareStatement(
                     "UPDATE customer SET nama_customer=?,alamat=?,telepon=? WHERE id_customer=?")) {
                ps.setString(1, txtNama.getText().trim());
                ps.setString(2, txtAlamat.getText().trim());
                ps.setString(3, txtTelp.getText().trim());
                ps.setString(4, txtId.getText().trim());
                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, "Customer berhasil diupdate!");
                bersih(); tampil();
            } catch (Exception e) { JOptionPane.showMessageDialog(this, "Error: " + e.getMessage()); }
        }

        void hapus() {
            if (txtId.getText().isBlank()) return;
            if (JOptionPane.showConfirmDialog(this, "Yakin hapus customer ini?") != JOptionPane.YES_OPTION) return;
            try (Connection k = getConnection();
                 PreparedStatement ps = k.prepareStatement("DELETE FROM customer WHERE id_customer=?")) {
                ps.setString(1, txtId.getText().trim());
                ps.executeUpdate();
                bersih(); tampil();
            } catch (Exception e) { JOptionPane.showMessageDialog(this, "Error: " + e.getMessage()); }
        }

        void tampil() {
            model.setRowCount(0);
            try (Connection k = getConnection();
                 Statement st = k.createStatement();
                 ResultSet rs = st.executeQuery("SELECT * FROM customer")) {
                while (rs.next()) {
                    model.addRow(new Object[]{
                        rs.getString("id_customer"), rs.getString("nama_customer"),
                        rs.getString("alamat"),      rs.getString("telepon")
                    });
                }
            } catch (Exception ignored) {}
        }

        void bersih() {
            txtId.setText(""); txtNama.setText(""); txtAlamat.setText(""); txtTelp.setText("");
            table.clearSelection();
        }
    }

    // ══════════════════════════════════════════════════════
    // PANEL SUPPLIER
    // ══════════════════════════════════════════════════════
    static class PanelSupplier extends JPanel {
        JTextField txtId, txtNama, txtAlamat, txtTelp;
        DefaultTableModel model;
        JTable table;

        PanelSupplier() {
            setLayout(null);
            setBackground(WHITE);

            JPanel form = new JPanel(null);
            form.setBounds(10, 10, 400, 160);
            form.setBackground(THEME_LITE);
            form.setBorder(BorderFactory.createTitledBorder("Input Data Supplier"));

            txtId     = addField(form, "ID Supplier", 10, 25,  180);
            txtNama   = addField(form, "Nama",        10, 55,  180);
            txtAlamat = addField(form, "Alamat",      10, 85,  180);
            txtTelp   = addField(form, "Telepon",     10, 115, 180);
            add(form);

            JButton btnSimpan = makeBtn("Simpan");
            JButton btnUpdate = makeBtn("Update");
            JButton btnHapus  = makeBtn("Hapus");
            JButton btnBersih = makeBtn("Bersihkan");

            btnSimpan.setBounds(10, 178, 110, 30);
            btnUpdate.setBounds(130, 178, 110, 30);
            btnHapus.setBounds(250, 178, 110, 30);
            btnBersih.setBounds(370, 178, 110, 30);

            btnSimpan.addActionListener(e -> simpan());
            btnUpdate.addActionListener(e -> update());
            btnHapus.addActionListener(e -> hapus());
            btnBersih.addActionListener(e -> bersih());

            add(btnSimpan); add(btnUpdate); add(btnHapus); add(btnBersih);

            model = makeModel(new String[]{"ID", "Nama", "Alamat", "Telepon"});
            table = new JTable(model);
            add(makeTable(table, 10, 215, 800, 300));

            table.getSelectionModel().addListSelectionListener(e -> {
                if (!e.getValueIsAdjusting() && table.getSelectedRow() >= 0) {
                    int r = table.getSelectedRow();
                    txtId.setText(model.getValueAt(r, 0).toString());
                    txtNama.setText(model.getValueAt(r, 1).toString());
                    txtAlamat.setText(model.getValueAt(r, 2).toString());
                    txtTelp.setText(model.getValueAt(r, 3).toString());
                }
            });

            tampil();
        }

        void simpan() {
            try (Connection k = getConnection();
                 PreparedStatement ps = k.prepareStatement(
                     "INSERT INTO supplier(id_supplier,nama_supplier,alamat,telepon) VALUES(?,?,?,?)")) {
                ps.setString(1, txtId.getText().trim());
                ps.setString(2, txtNama.getText().trim());
                ps.setString(3, txtAlamat.getText().trim());
                ps.setString(4, txtTelp.getText().trim());
                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, "Supplier berhasil disimpan!");
                bersih(); tampil();
            } catch (Exception e) { JOptionPane.showMessageDialog(this, "Error: " + e.getMessage()); }
        }

        void update() {
            try (Connection k = getConnection();
                 PreparedStatement ps = k.prepareStatement(
                     "UPDATE supplier SET nama_supplier=?,alamat=?,telepon=? WHERE id_supplier=?")) {
                ps.setString(1, txtNama.getText().trim());
                ps.setString(2, txtAlamat.getText().trim());
                ps.setString(3, txtTelp.getText().trim());
                ps.setString(4, txtId.getText().trim());
                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, "Supplier berhasil diupdate!");
                bersih(); tampil();
            } catch (Exception e) { JOptionPane.showMessageDialog(this, "Error: " + e.getMessage()); }
        }

        void hapus() {
            if (txtId.getText().isBlank()) return;
            if (JOptionPane.showConfirmDialog(this, "Yakin hapus supplier ini?") != JOptionPane.YES_OPTION) return;
            try (Connection k = getConnection();
                 PreparedStatement ps = k.prepareStatement("DELETE FROM supplier WHERE id_supplier=?")) {
                ps.setString(1, txtId.getText().trim());
                ps.executeUpdate();
                bersih(); tampil();
            } catch (Exception e) { JOptionPane.showMessageDialog(this, "Error: " + e.getMessage()); }
        }

        void tampil() {
            model.setRowCount(0);
            try (Connection k = getConnection();
                 Statement st = k.createStatement();
                 ResultSet rs = st.executeQuery("SELECT * FROM supplier")) {
                while (rs.next()) {
                    model.addRow(new Object[]{
                        rs.getString("id_supplier"), rs.getString("nama_supplier"),
                        rs.getString("alamat"),      rs.getString("telepon")
                    });
                }
            } catch (Exception ignored) {}
        }

        void bersih() {
            txtId.setText(""); txtNama.setText(""); txtAlamat.setText(""); txtTelp.setText("");
            table.clearSelection();
        }
    }

    // ══════════════════════════════════════════════════════
    // PANEL TRANSAKSI
    // ══════════════════════════════════════════════════════
    static class PanelTransaksi extends JPanel {
        JTextField txtNoTransaksi, txtTanggal, txtIdCustomer, txtKodeBarang, txtJumlah;
        JLabel lblNamaBarang, lblHarga, lblTotal;
        DefaultTableModel model;
        JTable table;
        double hargaJual = 0;

        PanelTransaksi() {
            setLayout(null);
            setBackground(WHITE);

            JPanel form = new JPanel(null);
            form.setBounds(10, 10, 810, 220);
            form.setBackground(THEME_LITE);
            form.setBorder(BorderFactory.createTitledBorder("Input Transaksi Penjualan"));

            txtNoTransaksi = addField(form, "No. Transaksi", 10, 25, 160);
            txtTanggal     = addField(form, "Tanggal",       10, 55, 160);
            txtIdCustomer  = addField(form, "ID Customer",   10, 85, 160);

            // Auto tanggal hari ini
            txtTanggal.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));

            txtKodeBarang = addField(form, "Kode Barang", 310, 25, 120);
            txtJumlah     = addField(form, "Jumlah",      310, 55, 160);

            JButton btnCekBarang = makeBtn("Cek");
            btnCekBarang.setBounds(535, 25, 60, 25);
            form.add(btnCekBarang);

            lblNamaBarang = new JLabel("-");
            lblNamaBarang.setFont(FONT_LBL);
            lblNamaBarang.setBounds(310, 85, 260, 25);
            form.add(lblNamaBarang);

            lblHarga = new JLabel("Harga: Rp 0");
            lblHarga.setFont(FONT_LBL);
            lblHarga.setBounds(310, 110, 260, 25);
            form.add(lblHarga);

            lblTotal = new JLabel("TOTAL: Rp 0", SwingConstants.RIGHT);
            lblTotal.setFont(new Font("Arial", Font.BOLD, 14));
            lblTotal.setForeground(THEME_DARK);
            lblTotal.setBounds(500, 155, 290, 30);
            form.add(lblTotal);

            add(form);

            btnCekBarang.addActionListener(e -> cekBarang());
            txtJumlah.addActionListener(e -> hitungTotal());

            JButton btnSimpan = makeBtn("Simpan Transaksi");
            JButton btnBersih = makeBtn("Bersihkan");

            btnSimpan.setBounds(10, 238, 180, 32);
            btnBersih.setBounds(200, 238, 130, 32);

            btnSimpan.addActionListener(e -> simpan());
            btnBersih.addActionListener(e -> bersih());

            add(btnSimpan); add(btnBersih);

            model = makeModel(new String[]{"No. Transaksi", "Tanggal", "ID Customer", "Kode Barang", "Jumlah", "Total Harga"});
            table = new JTable(model);
            add(makeTable(table, 10, 278, 810, 235));

            tampil();
        }

        void cekBarang() {
            String kode = txtKodeBarang.getText().trim();
            if (kode.isBlank()) return;
            try (Connection k = getConnection();
                 PreparedStatement ps = k.prepareStatement("SELECT * FROM barang WHERE kode_barang=?")) {
                ps.setString(1, kode);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    lblNamaBarang.setText(rs.getString("nama_barang"));
                    hargaJual = rs.getDouble("harga_jual");
                    lblHarga.setText("Harga: Rp " + (long) hargaJual);
                    hitungTotal();
                } else {
                    lblNamaBarang.setText("Barang tidak ditemukan!");
                    hargaJual = 0;
                }
            } catch (Exception e) { JOptionPane.showMessageDialog(this, "Error: " + e.getMessage()); }
        }

        void hitungTotal() {
            try {
                int qty = Integer.parseInt(txtJumlah.getText().trim());
                lblTotal.setText("TOTAL: Rp " + (long)(qty * hargaJual));
            } catch (NumberFormatException ignored) {}
        }

        void simpan() {
            try {
                int qty = Integer.parseInt(txtJumlah.getText().trim());
                double total = qty * hargaJual;

                try (Connection k = getConnection()) {
                    // Cek stok
                    PreparedStatement cek = k.prepareStatement("SELECT stok FROM barang WHERE kode_barang=?");
                    cek.setString(1, txtKodeBarang.getText().trim());
                    ResultSet rs = cek.executeQuery();
                    if (rs.next() && rs.getInt("stok") < qty) {
                        JOptionPane.showMessageDialog(this, "Stok tidak cukup! Stok tersedia: " + rs.getInt("stok"));
                        return;
                    }

                    // Insert transaksi
                    PreparedStatement ps = k.prepareStatement(
                        "INSERT INTO transaksi(no_transaksi,tanggal,id_customer,kode_barang,jumlah,total_harga) VALUES(?,?,?,?,?,?)");
                    ps.setString(1, txtNoTransaksi.getText().trim());
                    ps.setString(2, txtTanggal.getText().trim());
                    ps.setString(3, txtIdCustomer.getText().trim());
                    ps.setString(4, txtKodeBarang.getText().trim());
                    ps.setInt(5, qty);
                    ps.setDouble(6, total);
                    ps.executeUpdate();

                    // Kurangi stok
                    PreparedStatement upd = k.prepareStatement(
                        "UPDATE barang SET stok = stok - ? WHERE kode_barang=?");
                    upd.setInt(1, qty);
                    upd.setString(2, txtKodeBarang.getText().trim());
                    upd.executeUpdate();

                    JOptionPane.showMessageDialog(this, "Transaksi berhasil disimpan!");
                    bersih(); tampil();
                }
            } catch (Exception e) { JOptionPane.showMessageDialog(this, "Error: " + e.getMessage()); }
        }

        void tampil() {
            model.setRowCount(0);
            try (Connection k = getConnection();
                 Statement st = k.createStatement();
                 ResultSet rs = st.executeQuery("SELECT * FROM transaksi ORDER BY tanggal DESC")) {
                while (rs.next()) {
                    model.addRow(new Object[]{
                        rs.getString("no_transaksi"), rs.getString("tanggal"),
                        rs.getString("id_customer"),  rs.getString("kode_barang"),
                        rs.getInt("jumlah"),          "Rp " + (long) rs.getDouble("total_harga")
                    });
                }
            } catch (Exception ignored) {}
        }

        void bersih() {
            txtNoTransaksi.setText(""); txtIdCustomer.setText("");
            txtKodeBarang.setText(""); txtJumlah.setText("");
            txtTanggal.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
            lblNamaBarang.setText("-"); lblHarga.setText("Harga: Rp 0");
            lblTotal.setText("TOTAL: Rp 0"); hargaJual = 0;
            table.clearSelection();
        }
    }

    // ══════════════════════════════════════════════════════
    // PANEL INVENTORY
    // ══════════════════════════════════════════════════════
    static class PanelInventory extends JPanel {
        DefaultTableModel model;

        PanelInventory() {
            setLayout(null);
            setBackground(WHITE);

            JLabel title = new JLabel("Status Inventory Barang", SwingConstants.LEFT);
            title.setFont(FONT_TITLE);
            title.setBounds(10, 10, 400, 25);
            add(title);

            JButton btnRefresh = makeBtn("Refresh");
            btnRefresh.setBounds(10, 40, 120, 30);
            btnRefresh.addActionListener(e -> tampil());
            add(btnRefresh);

            model = makeModel(new String[]{"Kode", "Nama Barang", "Harga Beli", "Harga Jual", "Stok", "Status"});
            JTable table = new JTable(model);

            // Warnai baris stok rendah
            table.setDefaultRenderer(Object.class, new javax.swing.table.DefaultTableCellRenderer() {
                public Component getTableCellRendererComponent(JTable t, Object val,
                        boolean sel, boolean foc, int row, int col) {
                    super.getTableCellRendererComponent(t, val, sel, foc, row, col);
                    if (!sel) {
                        Object stok = t.getModel().getValueAt(row, 4);
                        try {
                            int s = Integer.parseInt(stok.toString());
                            setBackground(s <= 5 ? new Color(255, 200, 200) :
                                          s <= 10 ? new Color(255, 240, 180) : WHITE);
                        } catch (Exception ex) { setBackground(WHITE); }
                    }
                    return this;
                }
            });

            add(makeTable(table, 10, 80, 810, 430));

            tampil();
        }

        void tampil() {
            model.setRowCount(0);
            try (Connection k = getConnection();
                 Statement st = k.createStatement();
                 ResultSet rs = st.executeQuery("SELECT * FROM barang ORDER BY stok ASC")) {
                while (rs.next()) {
                    int stok = rs.getInt("stok");
                    String status = stok <= 5 ? "KRITIS" : stok <= 10 ? "Menipis" : "Normal";
                    model.addRow(new Object[]{
                        rs.getString("kode_barang"), rs.getString("nama_barang"),
                        "Rp " + (long) rs.getDouble("harga_beli"),
                        "Rp " + (long) rs.getDouble("harga_jual"),
                        stok, status
                    });
                }
            } catch (Exception ignored) {}
        }
    }

    // ══════════════════════════════════════════════════════
    // PANEL LAPORAN
    // ══════════════════════════════════════════════════════
    static class PanelLaporan extends JPanel {
        PanelLaporan() {
            setLayout(null);
            setBackground(WHITE);

            JLabel title = new JLabel("Cetak Laporan", SwingConstants.LEFT);
            title.setFont(FONT_TITLE);
            title.setBounds(10, 10, 300, 25);
            add(title);

            // Card Laporan Transaksi
            JPanel card1 = new JPanel(null);
            card1.setBounds(30, 60, 350, 120);
            card1.setBackground(THEME_LITE);
            card1.setBorder(BorderFactory.createLineBorder(THEME_DARK, 1));

            JLabel lbl1 = new JLabel("Laporan Transaksi Penjualan", SwingConstants.CENTER);
            lbl1.setFont(FONT_BOLD);
            lbl1.setBounds(0, 15, 350, 20);
            card1.add(lbl1);

            JLabel sub1 = new JLabel("Seluruh data transaksi penjualan", SwingConstants.CENTER);
            sub1.setFont(new Font("Arial", Font.ITALIC, 10));
            sub1.setBounds(0, 35, 350, 18);
            card1.add(sub1);

            JButton btnTransaksi = makeBtn("Cetak");
            btnTransaksi.setBounds(115, 68, 120, 32);
            btnTransaksi.addActionListener(e -> cetak("laporan/LaporanTransaksi.jrxml", "Laporan Transaksi"));
            card1.add(btnTransaksi);
            add(card1);

            // Card Laporan Inventory
            JPanel card2 = new JPanel(null);
            card2.setBounds(430, 60, 350, 120);
            card2.setBackground(THEME_LITE);
            card2.setBorder(BorderFactory.createLineBorder(THEME_DARK, 1));

            JLabel lbl2 = new JLabel("Laporan Inventory Barang", SwingConstants.CENTER);
            lbl2.setFont(FONT_BOLD);
            lbl2.setBounds(0, 15, 350, 20);
            card2.add(lbl2);

            JLabel sub2 = new JLabel("Status stok seluruh barang", SwingConstants.CENTER);
            sub2.setFont(new Font("Arial", Font.ITALIC, 10));
            sub2.setBounds(0, 35, 350, 18);
            card2.add(sub2);

            JButton btnInventory = makeBtn("Cetak");
            btnInventory.setBounds(115, 68, 120, 32);
            btnInventory.addActionListener(e -> cetak("laporan/LaporanInventory.jrxml", "Laporan Inventory"));
            card2.add(btnInventory);
            add(card2);

            JLabel note = new JLabel("Pastikan file .jrxml ada di folder src/laporan/ dalam project.");
            note.setFont(new Font("Arial", Font.ITALIC, 10));
            note.setBounds(30, 210, 600, 18);
            add(note);
        }

        void cetak(String jrxmlRelPath, String judul) {
            try {
                String path = new java.io.File(".").getAbsolutePath()
                        + java.io.File.separator + "src"
                        + java.io.File.separator + jrxmlRelPath.replace("/", java.io.File.separator);

                net.sf.jasperreports.engine.JasperReport jr =
                    net.sf.jasperreports.engine.JasperCompileManager.compileReport(path);

                java.util.Map<String, Object> params = new java.util.HashMap<>();
                params.put("judul", judul);

                try (Connection k = getConnection()) {
                    net.sf.jasperreports.engine.JasperPrint jp =
                        net.sf.jasperreports.engine.JasperFillManager.fillReport(jr, params, k);
                    net.sf.jasperreports.view.JasperViewer.viewReport(jp, false);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Gagal cetak laporan:\n" + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}