package Pertemuan6;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class CariRumah extends JFrame {

    private JTextField txtCari;
    private JComboBox<String> cmbKriteria;
    private JButton btnCari;
    private JTable tabelData;
    private DefaultTableModel tableModel;

    private static final String DB_URL = "jdbc:mariadb://localhost:3306/pemrograman2"
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

    public CariRumah() {
        setTitle("Cari Data Penjualan Rumah");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 450);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Panel Pencarian
        JPanel panelCari = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panelCari.setBorder(BorderFactory.createTitledBorder("Pencarian"));

        panelCari.add(new JLabel("Cari Berdasarkan :"));
        cmbKriteria = new JComboBox<>(new String[]{"Nama Pembeli", "Nama Area"});
        panelCari.add(cmbKriteria);

        panelCari.add(new JLabel("Kata Kunci :"));
        txtCari = new JTextField(20);
        panelCari.add(txtCari);

        btnCari = new JButton("CARI");
        panelCari.add(btnCari);

        add(panelCari, BorderLayout.NORTH);

        // Panel Tabel
        JPanel panelTabel = new JPanel(new BorderLayout());
        panelTabel.setBorder(BorderFactory.createTitledBorder("Hasil Pencarian"));

        String[] kolom = {
            "ID", "Nama Pembeli", "Area", "Tipe",
            "Harga Tanah/m2", "Luas Asli", "Luas Tersedia",
            "Harga Bangunan", "Harga", "DP",
            "Cicilan (bln)", "PPN", "Cicilan/bln", "Setuju"
        };

        tableModel = new DefaultTableModel(kolom, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };

        tabelData = new JTable(tableModel);
        tabelData.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tabelData.setRowHeight(22);

        JScrollPane scroll = new JScrollPane(tabelData,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        panelTabel.add(scroll, BorderLayout.CENTER);
        add(panelTabel, BorderLayout.CENTER);

        // Event
        btnCari.addActionListener(e -> cariData());
        txtCari.addActionListener(e -> cariData()); // bisa pencet Enter juga
    }

    private void cariData() {
        String keyword = txtCari.getText().trim();
        String kriteria = cmbKriteria.getSelectedItem().toString();

        if (keyword.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Kata kunci tidak boleh kosong!",
                    "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        tableModel.setRowCount(0);

        String field = kriteria.equals("Nama Pembeli") ? "nama_pemesan" : "area";
        String sql = "SELECT * FROM jual_rumah WHERE " + field + " LIKE '%" + keyword + "%'";

        try (Connection conn = getConnection()) {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);

            boolean adaData = false;
            while (rs.next()) {
                adaData = true;
                tableModel.addRow(new Object[]{
                    rs.getInt("id"),
                    rs.getString("nama_pemesan"),
                    rs.getString("area"),
                    rs.getString("tipe_rumah"),
                    rs.getDouble("harga_tanah_m2"),
                    rs.getDouble("luas_tanah_asli"),
                    rs.getDouble("luas_tanah_tersedia"),
                    rs.getDouble("harga_bangunan"),
                    rs.getDouble("harga"),
                    rs.getDouble("dp"),
                    rs.getInt("lama_cicilan"),
                    rs.getDouble("ppn"),
                    rs.getDouble("cicilan_per_bulan"),
                    rs.getBoolean("setuju") ? "Ya" : "Tidak"
                });
            }

            if (!adaData) {
                JOptionPane.showMessageDialog(this,
                        "Data tidak ada/Salah",
                        "Informasi", JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Gagal mengambil data:\n" + e.getMessage(),
                    "Kesalahan Database", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String args[]) {
        SwingUtilities.invokeLater(() -> {
            new CariRumah().setVisible(true);
        });
    }
}