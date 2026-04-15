package Pertemuan4;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FormMahasiswa extends JFrame {
    public static void main(String[] args) {
        new FormMahasiswa().setVisible(true);
    }
    
    // Komponen
    JTextField txtNim, txtNama, txtJurusan, txtAlamat;
    JButton btnTambah;
    JTable table;
    DefaultTableModel model;

    public FormMahasiswa() {
        setTitle("Form Mahasiswa");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        // Label
        JLabel lblNim = new JLabel("NIM");
        lblNim.setBounds(20, 20, 80, 25);
        add(lblNim);

        JLabel lblNama = new JLabel("Nama");
        lblNama.setBounds(20, 50, 80, 25);
        add(lblNama);

        JLabel lblJurusan = new JLabel("Jurusan");
        lblJurusan.setBounds(20, 80, 80, 25);
        add(lblJurusan);

        JLabel lblAlamat = new JLabel("Alamat");
        lblAlamat.setBounds(20, 110, 80, 25);
        add(lblAlamat);

        // TextField
        txtNim = new JTextField();
        txtNim.setBounds(100, 20, 150, 25);
        add(txtNim);

        txtNama = new JTextField();
        txtNama.setBounds(100, 50, 150, 25);
        add(txtNama);

        txtJurusan = new JTextField();
        txtJurusan.setBounds(100, 80, 150, 25);
        add(txtJurusan);

        txtAlamat = new JTextField();
        txtAlamat.setBounds(100, 110, 150, 25);
        add(txtAlamat);

        // Tombol
        btnTambah = new JButton("Tambah");
        btnTambah.setBounds(100, 150, 100, 30);
        add(btnTambah);

        // JTable
        model = new DefaultTableModel();
        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(20, 200, 440, 130);
        add(scrollPane);

        // Kolom tabel (sesuai PPT)
        model.addColumn("NIM");
        model.addColumn("Nama");
        model.addColumn("Jurusan");
        model.addColumn("Alamat");

        // Event tombol
        btnTambah.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Object[] data = {
                    txtNim.getText(),
                    txtNama.getText(),
                    txtJurusan.getText(),
                    txtAlamat.getText()
                };
                model.addRow(data);

                // reset input
                txtNim.setText("");
                txtNama.setText("");
                txtJurusan.setText("");
                txtAlamat.setText("");
            }
        });
    }
}