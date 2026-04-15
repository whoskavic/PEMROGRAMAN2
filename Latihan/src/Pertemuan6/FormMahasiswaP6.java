package Pertemuan6;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;

public class FormMahasiswaP6 extends JFrame {

    public static void main(String[] args) {
        new FormMahasiswaP6().setVisible(true);
    }

    // ── Konfigurasi Database ───────────────────────────────
    private static final String DB_URL  = "jdbc:mariadb://localhost:3306/pemrograman2"
            + "?useSSL=false"
            + "&restrictedAuth=mysql_native_password";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "sa";

    private Connection getConnection() throws SQLException {
        try {
            Class.forName("org.mariadb.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver tidak ditemukan: " + e.getMessage());
        }
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
    }

    // ── Komponen UI ───────────────────────────────────────
    JTextField txtNim, txtNama, txtSemester, txtKelas, txtCari;
    JButton btnTambah, btnTampil, btnCari, btnUpdate, btnHapus, btnBersihkan;
    JTable table;
    DefaultTableModel model;

    public FormMahasiswaP6() {
        setTitle("Form Mahasiswa - Pertemuan 6 (CRUD)");
        setSize(620, 530);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        // ── Label & TextField Input ────────────────────────
        String[] labels = {"NIM", "Nama", "Semester", "Kelas"};
        int y = 20;
        for (String lbl : labels) {
            JLabel l = new JLabel(lbl);
            l.setBounds(20, y, 80, 25);
            add(l);
            y += 30;
        }

        txtNim = new JTextField();
        txtNim.setBounds(110, 20, 150, 25);
        add(txtNim);

        txtNama = new JTextField();
        txtNama.setBounds(110, 50, 150, 25);
        add(txtNama);

        txtSemester = new JTextField();
        txtSemester.setBounds(110, 80, 150, 25);
        add(txtSemester);

        txtKelas = new JTextField();
        txtKelas.setBounds(110, 110, 150, 25);
        add(txtKelas);

        // ── Area Pencarian ─────────────────────────────────
        JLabel lblCari = new JLabel("Cari Nama:");
        lblCari.setBounds(290, 20, 80, 25);
        add(lblCari);

        txtCari = new JTextField();
        txtCari.setBounds(375, 20, 200, 25);
        add(txtCari);

        btnCari = new JButton("Cari");
        btnCari.setBounds(375, 50, 90, 25);
        add(btnCari);

        // ── Tombol CRUD ────────────────────────────────────
        btnTambah     = new JButton("Tambah");
        btnUpdate     = new JButton("Update");
        btnHapus      = new JButton("Hapus");
        btnTampil     = new JButton("Tampil Semua");
        btnBersihkan  = new JButton("Bersihkan");

        btnTambah.setBounds(20,  150, 100, 30);
        btnUpdate.setBounds(130, 150, 100, 30);
        btnHapus.setBounds(240,  150, 100, 30);
        btnTampil.setBounds(350, 150, 120, 30);
        btnBersihkan.setBounds(480, 150, 110, 30);

        add(btnTambah);
        add(btnUpdate);
        add(btnHapus);
        add(btnTampil);
        add(btnBersihkan);

        // ── JTable ─────────────────────────────────────────
        model = new DefaultTableModel(
            new String[]{"NIM", "Nama", "Semester", "Kelas"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };
        table = new JTable(model);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(20, 200, 565, 270);
        add(scrollPane);

        // ── Klik baris tabel → isi form ───────────────────
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = table.getSelectedRow();
                if (row >= 0) {
                    txtNim.setText(model.getValueAt(row, 0).toString());
                    txtNama.setText(model.getValueAt(row, 1).toString());
                    txtSemester.setText(model.getValueAt(row, 2).toString());
                    txtKelas.setText(model.getValueAt(row, 3).toString());
                }
            }
        });

        // ── Events ────────────────────────────────────────
        btnTambah.addActionListener(e -> simpanData());
        btnTampil.addActionListener(e -> tampilData());
        btnCari.addActionListener(e -> cariData());
        btnUpdate.addActionListener(e -> updateData());
        btnHapus.addActionListener(e -> hapusData());
        btnBersihkan.addActionListener(e -> bersihkanForm());

        // ── Load data awal ─────────────────────────────────
        tampilData();
    }

    // ── Method: Simpan / INSERT ───────────────────────────
    void simpanData() {
        String nim      = txtNim.getText().trim();
        String nama     = txtNama.getText().trim();
        String semester = txtSemester.getText().trim();
        String kelas    = txtKelas.getText().trim();

        if (nim.isEmpty() || nama.isEmpty()) {
            JOptionPane.showMessageDialog(this, "NIM dan Nama tidak boleh kosong!");
            return;
        }

        String sql = "INSERT INTO datamhs (nim, nama, semester, kelas) VALUES (?, ?, ?, ?)";
        try (Connection kon = getConnection();
             PreparedStatement ps = kon.prepareStatement(sql)) {

            ps.setString(1, nim);
            ps.setString(2, nama);
            ps.setString(3, semester);
            ps.setString(4, kelas);
            ps.executeUpdate();

            JOptionPane.showMessageDialog(this, "Data berhasil disimpan!");
            bersihkanForm();
            tampilData();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                "Gagal menyimpan!\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ── Method: Tampil Semua / SELECT * ──────────────────
    void tampilData() {
        String sql = "SELECT * FROM datamhs";
        try (Connection kon = getConnection();
             Statement st   = kon.createStatement();
             ResultSet rs   = st.executeQuery(sql)) {

            model.setRowCount(0);
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("nim"),
                    rs.getString("nama"),
                    rs.getString("semester"),
                    rs.getString("kelas")
                });
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                "Gagal menampilkan!\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ── Method: Cari Data / SELECT WHERE LIKE ────────────
    // Menggunakan Statement (tanpa parameter) sesuai materi PPT slide 4-5
    void cariData() {
        String keyword = txtCari.getText().trim();
        if (keyword.isEmpty()) {
            tampilData();
            return;
        }

        // Field sebagian: LIKE '%keyword%'
        String sql = "SELECT * FROM datamhs WHERE nama LIKE ('%" + keyword + "%')";
        try (Connection kon = getConnection();
             Statement st   = kon.createStatement();
             ResultSet rs   = st.executeQuery(sql)) {

            model.setRowCount(0);
            boolean ada = false;

            while (rs.next()) {
                ada = true;
                model.addRow(new Object[]{
                    rs.getString("nim"),
                    rs.getString("nama"),
                    rs.getString("semester"),
                    rs.getString("kelas")
                });
            }

            if (!ada) {
                JOptionPane.showMessageDialog(this,
                    "Data tidak ada / Salah", "Informasi", JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                "Gagal mencari!\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ── Method: Update Data / UPDATE SET WHERE ────────────
    void updateData() {
        String nim      = txtNim.getText().trim();
        String nama     = txtNama.getText().trim();
        String semester = txtSemester.getText().trim();
        String kelas    = txtKelas.getText().trim();

        if (nim.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Pilih data dari tabel atau isi NIM terlebih dahulu!");
            return;
        }

        String sql = "UPDATE datamhs SET nama=?, semester=?, kelas=? WHERE nim=?";
        try (Connection kon = getConnection();
             PreparedStatement ps = kon.prepareStatement(sql)) {

            ps.setString(1, nama);
            ps.setString(2, semester);
            ps.setString(3, kelas);
            ps.setString(4, nim);
            int affected = ps.executeUpdate();

            if (affected > 0) {
                JOptionPane.showMessageDialog(this, "Data berhasil diupdate!");
                bersihkanForm();
                tampilData();
            } else {
                JOptionPane.showMessageDialog(this, "NIM tidak ditemukan!");
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                "Gagal update!\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ── Method: Hapus Data / DELETE WHERE ────────────────
    void hapusData() {
        String nim = txtNim.getText().trim();

        if (nim.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Pilih data dari tabel atau isi NIM terlebih dahulu!");
            return;
        }

        int konfirmasi = JOptionPane.showConfirmDialog(this,
            "Yakin ingin menghapus data NIM: " + nim + "?",
            "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION);

        if (konfirmasi != JOptionPane.YES_OPTION) return;

        String sql = "DELETE FROM datamhs WHERE nim=?";
        try (Connection kon = getConnection();
             PreparedStatement ps = kon.prepareStatement(sql)) {

            ps.setString(1, nim);
            int affected = ps.executeUpdate();

            if (affected > 0) {
                JOptionPane.showMessageDialog(this, "Data berhasil dihapus!");
                bersihkanForm();
                tampilData();
            } else {
                JOptionPane.showMessageDialog(this, "NIM tidak ditemukan!");
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                "Gagal menghapus!\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ── Method: Reset Form ────────────────────────────────
    void bersihkanForm() {
        txtNim.setText("");
        txtNama.setText("");
        txtSemester.setText("");
        txtKelas.setText("");
        txtCari.setText("");
        table.clearSelection();
    }
}