package Pertemuan3;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class KalkulatorGUI extends JFrame {

    JLabel lblAngka1, lblAngka2, lblHasil;
    JTextField txtAngka1, txtAngka2, txtHasil;
    JButton btnTambah, btnHapus, btnExit;

    public KalkulatorGUI() {

        setTitle("M. Fikri Avishena Parinduri");
        setSize(420,250);
        setLayout(null);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Label
        lblAngka1 = new JLabel("Angka Pertama");
        lblAngka1.setBounds(50,40,120,25);
        add(lblAngka1);

        lblAngka2 = new JLabel("Angka Kedua");
        lblAngka2.setBounds(50,80,120,25);
        add(lblAngka2);

        lblHasil = new JLabel("Hasil");
        lblHasil.setBounds(50,120,120,25);
        add(lblHasil);

        // TextField
        txtAngka1 = new JTextField();
        txtAngka1.setBounds(180,40,150,25);
        add(txtAngka1);

        txtAngka2 = new JTextField();
        txtAngka2.setBounds(180,80,150,25);
        add(txtAngka2);

        txtHasil = new JTextField();
        txtHasil.setBounds(180,120,150,25);
        txtHasil.setEditable(false);
        add(txtHasil);

        // Button
        btnTambah = new JButton("Tambah");
        btnTambah.setBounds(70,170,80,30);
        add(btnTambah);

        btnHapus = new JButton("Hapus");
        btnHapus.setBounds(160,170,80,30);
        add(btnHapus);

        btnExit = new JButton("Exit");
        btnExit.setBounds(250,170,80,30);
        add(btnExit);

        // Event Tambah
        btnTambah.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    int angka1 = Integer.parseInt(txtAngka1.getText());
                    int angka2 = Integer.parseInt(txtAngka2.getText());

                    int hasil = angka1 + angka2;
                    txtHasil.setText(String.valueOf(hasil));
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Input harus angka!");
                }
            }
        });

        // Event Hapus
        btnHapus.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                txtAngka1.setText("");
                txtAngka2.setText("");
                txtHasil.setText("");
            }
        });

        // Event Exit
        btnExit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
    }

    public static void main(String[] args) {
        new KalkulatorGUI().setVisible(true);
    }
}
