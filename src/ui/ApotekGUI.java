package ui;

import manager.AntrianManager;
import manager.FileManager;
import model.Pelanggan;
import model.Petugas;
import simulator.LayananSimulator;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ApotekGUI extends JFrame {

    private AntrianManager antrianManager;
    private FileManager fileManager;
    private List<Petugas> daftarPetugas;
    private List<LayananSimulator> daftarSimulator;

    // UI Components
    private DefaultListModel<String> antrianListModel;
    private JTextField namaInput;
    private JTextArea logArea;
    private List<JLabel> statusPetugasLabels;
    private JLabel totalAntrianLabel;
    private JLabel totalDilayaniLabel;

    public ApotekGUI() {
        setTitle("Sistem Antrian Apotek");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        // Manager
        antrianManager = new AntrianManager();
        fileManager = new FileManager(); // constructor TANPA parameter

        daftarPetugas = new ArrayList<>();
        daftarSimulator = new ArrayList<>();

        setupPetugas();
        initComponents();

        // refresh UI tiap 500ms
        new Timer(500, e -> updateTampilan()).start();
    }

    private void setupPetugas() {
        Petugas p1 = new Petugas("Petugas 1");
        Petugas p2 = new Petugas("Petugas 2");

        daftarPetugas.add(p1);
        daftarPetugas.add(p2);

        daftarSimulator.add(new LayananSimulator(p1, antrianManager, fileManager, 10));
        daftarSimulator.add(new LayananSimulator(p2, antrianManager, fileManager, 10));
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));

        // ===== TOP =====
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        topPanel.add(new JLabel("Nama Pelanggan:"));
        namaInput = new JTextField(20);
        topPanel.add(namaInput);

        JButton btnDaftar = new JButton("Daftar Antrian");
        btnDaftar.addActionListener(e -> tambahPelanggan());
        topPanel.add(btnDaftar);

        JButton btnReset = new JButton("Reset Antrian");
        btnReset.addActionListener(e -> {
            antrianManager.clear();
            antrianListModel.clear();
            JOptionPane.showMessageDialog(this, "Antrian di-reset");
        });
        topPanel.add(btnReset);

        add(topPanel, BorderLayout.NORTH);

        // ===== CENTER =====
        antrianListModel = new DefaultListModel<>();
        JList<String> listAntrian = new JList<>(antrianListModel);

        JPanel antrianPanel = new JPanel(new BorderLayout());
        antrianPanel.setBorder(BorderFactory.createTitledBorder("Daftar Antrian"));
        antrianPanel.add(new JScrollPane(listAntrian), BorderLayout.CENTER);

        JPanel statusPanel = new JPanel();
        statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.Y_AXIS));
        statusPanel.setBorder(BorderFactory.createTitledBorder("Status Petugas"));

        statusPetugasLabels = new ArrayList<>();
        for (Petugas p : daftarPetugas) {
            JLabel lbl = new JLabel(p.getNama() + ": " + p.getStatusInfo());
            lbl.setFont(new Font("Monospaced", Font.BOLD, 14));
            statusPetugasLabels.add(lbl);
            statusPanel.add(lbl);
        }

        totalAntrianLabel = new JLabel("Total Antrian: 0");
        totalDilayaniLabel = new JLabel("Total Dilayani: 0");
        statusPanel.add(totalAntrianLabel);
        statusPanel.add(totalDilayaniLabel);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, antrianPanel, statusPanel);
        splitPane.setResizeWeight(0.5);
        add(splitPane, BorderLayout.CENTER);

        // ===== BOTTOM =====
        JPanel bottomPanel = new JPanel(new BorderLayout());

        JPanel controlPanel = new JPanel();
        JButton btnMulai = new JButton("Mulai Simulasi");
        btnMulai.addActionListener(e -> daftarSimulator.forEach(LayananSimulator::mulaiSimulasi));

        JButton btnStop = new JButton("Stop Simulasi");
        btnStop.addActionListener(e -> daftarSimulator.forEach(LayananSimulator::hentikanSimulasi));

        controlPanel.add(btnMulai);
        controlPanel.add(btnStop);

        bottomPanel.add(controlPanel, BorderLayout.NORTH);

        logArea = new JTextArea(6, 40);
        logArea.setEditable(false);
        bottomPanel.add(new JScrollPane(logArea), BorderLayout.CENTER);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void tambahPelanggan() {
        String nama = namaInput.getText().trim();
        if (!nama.isEmpty()) {
            String nomor = antrianManager.tambahPelanggan(nama);
            namaInput.setText("");
            JOptionPane.showMessageDialog(this, "Terdaftar: " + nama + " (" + nomor + ")");
        }
    }

    private void updateTampilan() {
        antrianListModel.clear();
        for (Pelanggan p : antrianManager.getAntrian()) {
            antrianListModel.addElement(p.getNomorAntrian() + " - " + p.getNama());
        }

        for (int i = 0; i < daftarPetugas.size(); i++) {
            Petugas p = daftarPetugas.get(i);
            JLabel lbl = statusPetugasLabels.get(i);
            lbl.setText(p.getNama() + ": " + p.getStatusInfo());
            lbl.setForeground(p.isSedangMelayani() ? Color.RED : new Color(0, 150, 0));
        }

        totalAntrianLabel.setText("Total Antrian: " + antrianManager.getJumlahAntrian());
        totalDilayaniLabel.setText("Total Dilayani: " + antrianManager.getRiwayatLayanan().size());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ApotekGUI().setVisible(true));
    }
}
