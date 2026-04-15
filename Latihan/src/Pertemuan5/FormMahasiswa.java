package Pertemuan5;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;

public class FormMahasiswa extends JFrame {

    public static void main(String[] args) {
        new FormMahasiswa().setVisible(true);
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
    JTextField txtNim, txtNama, txtSemester, txtKelas;
    JButton btnTambah, btnTampil;
    JTable table;
    DefaultTableModel model;

    public FormMahasiswa() {
        setTitle("Form Mahasiswa - Pertemuan 5");
        setSize(550, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        // ── Label ──────────────────────────────────────────
        JLabel lblNim = new JLabel("NIM");
        lblNim.setBounds(20, 20, 80, 25);
        add(lblNim);

        JLabel lblNama = new JLabel("Nama");
        lblNama.setBounds(20, 50, 80, 25);
        add(lblNama);

        JLabel lblSemester = new JLabel("Semester");
        lblSemester.setBounds(20, 80, 80, 25);
        add(lblSemester);

        JLabel lblKelas = new JLabel("Kelas");
        lblKelas.setBounds(20, 110, 80, 25);
        add(lblKelas);

        // ── TextField ──────────────────────────────────────
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

        // ── Tombol ─────────────────────────────────────────
        btnTambah = new JButton("Tambah");
        btnTambah.setBounds(110, 150, 100, 30);
        add(btnTambah);

        btnTampil = new JButton("Tampil");
        btnTampil.setBounds(220, 150, 100, 30);
        add(btnTampil);

        // ── JTable ─────────────────────────────────────────
        model = new DefaultTableModel();
        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(20, 200, 500, 180);
        add(scrollPane);

        model.addColumn("NIM");
        model.addColumn("Nama");
        model.addColumn("Semester");
        model.addColumn("Kelas");

        // ── Test koneksi saat startup ──────────────────────
//        try (Connection test = getConnection()) {
//            JOptionPane.showMessageDialog(this, "Koneksi database berhasil!");
//        } catch (SQLException e) {
//            JOptionPane.showMessageDialog(this,
//                "Gagal koneksi database!\n" + e.getMessage(),
//                "Error", JOptionPane.ERROR_MESSAGE);
//        }

        // ── Event Tombol ───────────────────────────────────
        btnTambah.addActionListener(e -> simpanData());
        btnTampil.addActionListener(e -> tampilData());
    }

    // ── Method: Simpan Data ───────────────────────────────
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

        try (Connection koneksi = getConnection();
             PreparedStatement pStat = koneksi.prepareStatement(sql)) {

            pStat.setString(1, nim);
            pStat.setString(2, nama);
            pStat.setString(3, semester);
            pStat.setString(4, kelas);
            pStat.executeUpdate();

            JOptionPane.showMessageDialog(this, "Data berhasil disimpan!");
            txtNim.setText("");
            txtNama.setText("");
            txtSemester.setText("");
            txtKelas.setText("");
            tampilData();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                "Gagal menyimpan data!\n" + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ── Method: Tampil Data ───────────────────────────────
    void tampilData() {
        String sql = "SELECT * FROM datamhs";

        try (Connection koneksi = getConnection();
             PreparedStatement pStat = koneksi.prepareStatement(sql);
             ResultSet rs = pStat.executeQuery()) {

            model.setRowCount(0); // bersihkan tabel dulu

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
                "Gagal menampilkan data!\n" + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}