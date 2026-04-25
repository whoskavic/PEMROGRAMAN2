package launcher;

import javax.swing.*;
import java.awt.*;

public class MainLauncher extends JFrame {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainLauncher().setVisible(true));
    }

    public MainLauncher() {
        setTitle("Pemrograman 2 - Pilih Pertemuan");
        setSize(350, 420);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);
        setResizable(false);

        JLabel lblJudul = new JLabel("Pemrograman 2", SwingConstants.CENTER);
        lblJudul.setFont(new Font("Arial", Font.BOLD, 18));
        lblJudul.setBounds(0, 20, 350, 30);
        add(lblJudul);

        JLabel lblSub = new JLabel("Pilih Pertemuan", SwingConstants.CENTER);
        lblSub.setFont(new Font("Arial", Font.PLAIN, 12));
        lblSub.setBounds(0, 50, 350, 20);
        add(lblSub);

        JSeparator sep = new JSeparator();
        sep.setBounds(30, 78, 290, 2);
        add(sep);

        String[] labels = {
            "Pertemuan 3 - Kalkulator GUI", // 0
            "Pertemuan 4 - Form Mahasiswa (Swing)", // 1
            "Pertemuan 5 - Form Mahasiswa (DB)", // 2
            // "Pertemuan 5 - Jual Rumah",
            "Pertemuan 6 - Form Mahasiswa CRUD", // 3
            // "Pertemuan 6 - Cari Rumah",
            "Pertemuan 7 - Laporan JasperReports", // 4
        };

        int y = 90;
        for (int i = 0; i < labels.length; i++) {
            JButton btn = new JButton(labels[i]);
            btn.setBounds(30, y, 290, 35);
            btn.setFont(new Font("Arial", Font.PLAIN, 11));
            final int idx = i;
            btn.addActionListener(e -> bukaPertemuan(idx));
            add(btn);
            y += 42;
        }
    }

    void bukaPertemuan(int idx) {
        try {
            switch (idx) {
                case 0 -> new Pertemuan3.KalkulatorGUI().setVisible(true);
                case 1 -> new Pertemuan4.FormMahasiswa().setVisible(true);
                case 2 -> new Pertemuan5.FormMahasiswa().setVisible(true);
                // case 3 -> new Pertemuan5.JualRumah().setVisible(true);
                case 3 -> new Pertemuan6.FormMahasiswaP6().setVisible(true);
                // case 5 -> new Pertemuan6.CariRumah().setVisible(true);
                case 4 -> new Pertemuan7.FormLaporanP7().setVisible(true);
                default -> JOptionPane.showMessageDialog(this, "Form tidak ditemukan.");
            }
        } catch (HeadlessException e) {
            JOptionPane.showMessageDialog(this,
                "Gagal membuka form:\n" + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}