package Pertemuan5;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class JualRumah extends JFrame {
    // ===================== KONEKSI DB =====================
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
     
    // ===================== KOMPONEN UI =====================
    private JTextField txtNamaPemesan, txtHargaTanah, txtLuasTanahAsli;
    private JTextField txtLuasTanahTersedia, txtHargaBangunan;
    private JTextField txtHarga, txtDP, txtLamaCicilan;
    private JTextField txtPPN, txtCicilanPerBulan;
    private JComboBox<String> cmbArea;
    private ButtonGroup tipeGroup;
    private JRadioButton rbTipe36, rbTipe45, rbTipe54;
    private JCheckBox chkSetuju;
    private JButton btnProses, btnSimpan, btnClear;

    // Tabel Tampil Data
    private JTable tabelData;
    private DefaultTableModel tableModel;

    // ===================== CONSTRUCTOR =====================
    public JualRumah() {
        setTitle("Form Penjualan Rumah - Pemrograman 2");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 700);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        add(buildFormPanel(), BorderLayout.NORTH);
        add(buildTablePanel(), BorderLayout.CENTER);

        loadData();
    }

    // ===================== BUILD FORM =====================
    private JPanel buildFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Form Penjualan Rumah"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 8, 5, 8);
        gbc.anchor = GridBagConstraints.WEST;

        // --- Kolom Kiri ---
        // Nama Pemesan
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Nama Pemesan :"), gbc);
        txtNamaPemesan = new JTextField(18);
        gbc.gridx = 1;
        panel.add(txtNamaPemesan, gbc);

        // Harga Tanah/m2
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Harga Tanah/m2 :"), gbc);
        txtHargaTanah = new JTextField(18);
        gbc.gridx = 1;
        panel.add(txtHargaTanah, gbc);

        // Area
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Area :"), gbc);
        cmbArea = new JComboBox<>(new String[]{"Fikri", "Avishena", "Parinduri"});
        cmbArea.setPreferredSize(new Dimension(180, 25));
        gbc.gridx = 1;
        panel.add(cmbArea, gbc);

        // Tipe Rumah
        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Tipe Rumah :"), gbc);
        JPanel tipePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        tipeGroup = new ButtonGroup();
        rbTipe36 = new JRadioButton("Tipe-36", true);
        rbTipe45 = new JRadioButton("Tipe-45");
        rbTipe54 = new JRadioButton("Tipe-54");
        tipeGroup.add(rbTipe36);
        tipeGroup.add(rbTipe45);
        tipeGroup.add(rbTipe54);
        tipePanel.add(rbTipe36);
        tipePanel.add(rbTipe45);
        tipePanel.add(rbTipe54);
        gbc.gridx = 1;
        panel.add(tipePanel, gbc);

        // Luas Tanah Asli
        gbc.gridx = 0; gbc.gridy = 4;
        panel.add(new JLabel("Luas Tanah Asli :"), gbc);
        txtLuasTanahAsli = new JTextField(18);
        gbc.gridx = 1;
        panel.add(txtLuasTanahAsli, gbc);

        // Luas Tanah Tersedia
        gbc.gridx = 0; gbc.gridy = 5;
        panel.add(new JLabel("Luas Tanah Tersedia :"), gbc);
        txtLuasTanahTersedia = new JTextField(18);
        gbc.gridx = 1;
        panel.add(txtLuasTanahTersedia, gbc);

        // --- Kolom Kanan ---
        // Harga Bangunan
        gbc.gridx = 2; gbc.gridy = 0;
        panel.add(new JLabel("Harga Bangunan :"), gbc);
        txtHargaBangunan = new JTextField(18);
        gbc.gridx = 3;
        panel.add(txtHargaBangunan, gbc);

        // Harga (output)
        gbc.gridx = 2; gbc.gridy = 1;
        panel.add(new JLabel("Harga :"), gbc);
        txtHarga = new JTextField(18);
        txtHarga.setEditable(false);
        txtHarga.setBackground(new Color(230, 230, 230));
        gbc.gridx = 3;
        panel.add(txtHarga, gbc);

        // DP (input manual)
        gbc.gridx = 2; gbc.gridy = 2;
        panel.add(new JLabel("DP :"), gbc);
        txtDP = new JTextField(18);
        gbc.gridx = 3;
        panel.add(txtDP, gbc);

        // Lama Cicilan (input manual)
        gbc.gridx = 2; gbc.gridy = 3;
        panel.add(new JLabel("Lama Cicilan :"), gbc);
        JPanel cicilanPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        txtLamaCicilan = new JTextField(10);
        cicilanPanel.add(txtLamaCicilan);
        cicilanPanel.add(new JLabel("  Bulan"));
        gbc.gridx = 3;
        panel.add(cicilanPanel, gbc);

        // PPN (output)
        gbc.gridx = 2; gbc.gridy = 4;
        panel.add(new JLabel("PPN (10%) :"), gbc);
        txtPPN = new JTextField(18);
        txtPPN.setEditable(false);
        txtPPN.setBackground(new Color(230, 230, 230));
        gbc.gridx = 3;
        panel.add(txtPPN, gbc);

        // Cicilan/bln (output) + Setuju
        gbc.gridx = 2; gbc.gridy = 5;
        panel.add(new JLabel("Cicilan/bln :"), gbc);
        JPanel cicilanBlnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        txtCicilanPerBulan = new JTextField(12);
        txtCicilanPerBulan.setEditable(false);
        txtCicilanPerBulan.setBackground(new Color(230, 230, 230));
        chkSetuju = new JCheckBox("Setuju");
        cicilanBlnPanel.add(txtCicilanPerBulan);
        cicilanBlnPanel.add(chkSetuju);
        gbc.gridx = 3;
        panel.add(cicilanBlnPanel, gbc);

        // --- Tombol ---
        gbc.gridx = 0; gbc.gridy = 6;
        gbc.gridwidth = 4;
        gbc.anchor = GridBagConstraints.CENTER;
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        btnProses = new JButton("PROSES");
        btnSimpan = new JButton("SIMPAN");
        btnClear  = new JButton("CLEAR");

        btnProses.setPreferredSize(new Dimension(100, 30));
        btnSimpan.setPreferredSize(new Dimension(100, 30));
        btnClear.setPreferredSize(new Dimension(100, 30));

        btnPanel.add(btnProses);
        btnPanel.add(btnSimpan);
        btnPanel.add(btnClear);
        panel.add(btnPanel, gbc);

        // ===================== EVENT LISTENER =====================
        btnProses.addActionListener(e -> proses());
        btnSimpan.addActionListener(e -> simpan());
        btnClear.addActionListener(e -> clear());

        return panel;
    }

    // ===================== BUILD TABLE =====================
    private JPanel buildTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Tampil Data Penjualan"));

        String[] kolom = {
            "ID", "Nama Pemesan", "Area", "Tipe",
            "Harga Tanah/m2", "Luas Asli", "Luas Tersedia",
            "Harga Bangunan", "Harga", "DP",
            "Cicilan (bln)", "PPN", "Cicilan/bln", "Setuju"
        };

        tableModel = new DefaultTableModel(kolom, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };

        tabelData = new JTable(tableModel);
        tabelData.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tabelData.getTableHeader().setBackground(new Color(70, 130, 180));
        tabelData.getTableHeader().setForeground(Color.WHITE);
        tabelData.setRowHeight(22);

        JScrollPane scroll = new JScrollPane(tabelData,
            JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
            JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scroll.setPreferredSize(new Dimension(860, 200));

        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }

    // ===================== LOGIKA KALKULASI =====================
    private void proses() {
        try {
            double hargaTanah    = Double.parseDouble(txtHargaTanah.getText());
            double luasTersedia  = Double.parseDouble(txtLuasTanahTersedia.getText());
            double hargaBangunan = Double.parseDouble(txtHargaBangunan.getText());

            double harga = (hargaTanah * luasTersedia) + hargaBangunan;
            double ppn   = harga * 0.10;

            txtHarga.setText(String.format("%.2f", harga));
            txtPPN.setText(String.format("%.2f", ppn));

            // Hitung cicilan kalau DP dan lama cicilan sudah diisi
            String dpStr   = txtDP.getText().trim();
            String lamaStr = txtLamaCicilan.getText().trim();
            if (!dpStr.isEmpty() && !lamaStr.isEmpty()) {
                double dp = Double.parseDouble(dpStr);
                int lama  = Integer.parseInt(lamaStr);
                if (lama > 0) {
                    double cicilan = (harga - dp) / lama;
                    txtCicilanPerBulan.setText(String.format("%.2f", cicilan));
                }
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                "Pastikan semua field angka terisi dengan benar!",
                "Kesalahan Input", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ===================== SIMPAN KE DATABASE =====================
    private void simpan() {
        if (txtNamaPemesan.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Nama Pemesan tidak boleh kosong!",
                "Kesalahan", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (txtHarga.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Lakukan PROSES terlebih dahulu sebelum menyimpan!",
                "Kesalahan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!chkSetuju.isSelected()) {
            JOptionPane.showMessageDialog(this,
                "Centang Setuju terlebih dahulu!",
                "Kesalahan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try (Connection conn = getConnection()) {
            String sql = "INSERT INTO jual_rumah (nama_pemesan, area, tipe_rumah, " +
                         "harga_tanah_m2, luas_tanah_asli, luas_tanah_tersedia, harga_bangunan, " +
                         "harga, dp, lama_cicilan, ppn, cicilan_per_bulan, setuju) " +
                         "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, txtNamaPemesan.getText());
            ps.setString(2, cmbArea.getSelectedItem().toString());
            ps.setString(3, getTipeRumah());
            ps.setDouble(4, Double.parseDouble(txtHargaTanah.getText()));
            ps.setDouble(5, Double.parseDouble(txtLuasTanahAsli.getText()));
            ps.setDouble(6, Double.parseDouble(txtLuasTanahTersedia.getText()));
            ps.setDouble(7, Double.parseDouble(txtHargaBangunan.getText()));
            ps.setDouble(8, Double.parseDouble(txtHarga.getText()));
            ps.setDouble(9, Double.parseDouble(txtDP.getText()));
            ps.setInt(10, Integer.parseInt(txtLamaCicilan.getText()));
            ps.setDouble(11, Double.parseDouble(txtPPN.getText()));
            ps.setDouble(12, Double.parseDouble(txtCicilanPerBulan.getText()));
            ps.setBoolean(13, chkSetuju.isSelected());

            int result = ps.executeUpdate();
            if (result > 0) {
                JOptionPane.showMessageDialog(this,
                    "Data berhasil disimpan!",
                    "Sukses", JOptionPane.INFORMATION_MESSAGE);
                loadData();
                clear();
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                "Gagal menyimpan data:\n" + e.getMessage(),
                "Kesalahan Database", JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                "Pastikan semua field angka terisi dengan benar!",
                "Kesalahan Input", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ===================== LOAD DATA KE TABEL =====================
    private void loadData() {
        tableModel.setRowCount(0);
        try (Connection conn = getConnection()) {
            String sql = "SELECT * FROM jual_rumah ORDER BY id DESC";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
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
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                "Gagal memuat data:\n" + e.getMessage(),
                "Kesalahan Database", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ===================== CLEAR FORM =====================
    private void clear() {
        txtNamaPemesan.setText("");
        txtHargaTanah.setText("");
        txtLuasTanahAsli.setText("");
        txtLuasTanahTersedia.setText("");
        txtHargaBangunan.setText("");
        txtHarga.setText("");
        txtDP.setText("");
        txtLamaCicilan.setText("");
        txtPPN.setText("");
        txtCicilanPerBulan.setText("");
        cmbArea.setSelectedIndex(0);
        rbTipe36.setSelected(true);
        chkSetuju.setSelected(false);
    }

    // ===================== HELPER =====================
    private String getTipeRumah() {
        if (rbTipe45.isSelected()) return "Tipe-45";
        if (rbTipe54.isSelected()) return "Tipe-54";
        return "Tipe-36";
    }

    // ===================== MAIN =====================
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new JualRumah().setVisible(true);
        });
    }
}