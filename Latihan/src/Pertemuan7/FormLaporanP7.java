package Pertemuan7;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.view.JasperViewer;

import javax.swing.*;
import java.io.File;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class FormLaporanP7 extends JFrame {

    public static void main(String[] args) {
        new FormLaporanP7().setVisible(true);
    }

    // setting db
    private static final String DB_USER = "root";
    private static final String DB_PASS = "sa";
    private static final String DB_HOST = "localhost";
    private static final String DB_NAME = "pemrograman2";
    private static final String DB_URL  = "jdbc:mariadb://" + DB_HOST + ":3306/" + DB_NAME
            + "?useSSL=false&restrictedAuth=mysql_native_password";

    private Connection getConnection() throws SQLException {
        try {
            Class.forName("org.mariadb.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver tidak ditemukan: " + e.getMessage());
        }
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
    }

    // UI
    JButton btnCetak;
    JLabel  lblStatus;

    public FormLaporanP7() {
        setTitle("Laporan Mahasiswa - Pertemuan 7");
        setSize(350, 180);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        JLabel lblJudul = new JLabel("Laporan Data Mahasiswa");
        lblJudul.setBounds(70, 20, 220, 25);
        lblJudul.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 14));
        add(lblJudul);

        btnCetak = new JButton("Cetak Laporan");
        btnCetak.setBounds(100, 60, 140, 35);
        add(btnCetak);

        lblStatus = new JLabel("");
        lblStatus.setBounds(20, 105, 310, 25);
        lblStatus.setHorizontalAlignment(SwingConstants.CENTER);
        add(lblStatus);

        btnCetak.addActionListener(e -> cetakLaporan());
    }

    // Cetak
    void cetakLaporan() {
        lblStatus.setText("Memproses laporan...");
        btnCetak.setEnabled(false);

        // Lokasi file .jrxml
        // dirr = absolute path FOLDER paling atas. contoh punya gua ada di: Latihan, jadi :
        // Latihan/src/laporan/LaporanMahasiswa.jrxml
        // ini bebas kalau mau beda, disesuain aja
        File dir1   = new File(".");
        String dirr = dir1.getAbsolutePath();
        String reportPath = dirr + File.separator + "src"
                + File.separator + "laporan"
                + File.separator + "LaporanMahasiswa.jrxml";

        try (Connection koneksi = getConnection()) {

            // Compile .jrxml menjadi .jasper
            JasperReport jasperReport = JasperCompileManager.compileReport(reportPath);

            // Parmeter
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("judul", "Daftar Data Mahasiswa");

            // Fill isi report nya
            JasperPrint jasperPrint = JasperFillManager.fillReport(
                jasperReport, parameters, koneksi
            );

            // View - JasperViewer
            JasperViewer.viewReport(jasperPrint, false);

            lblStatus.setText("Laporan berhasil ditampilkan.");

        } catch (JRException e) {
            JOptionPane.showMessageDialog(this,
                "Gagal memproses laporan!\n" + e.getMessage(),
                "Error JasperReports", JOptionPane.ERROR_MESSAGE);
            lblStatus.setText("Gagal memproses laporan.");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                "Gagal koneksi database!\n" + e.getMessage(),
                "Error Database", JOptionPane.ERROR_MESSAGE);
            lblStatus.setText("Gagal koneksi database.");
        } finally {
            btnCetak.setEnabled(true);
        }
    }
}