package ui;

import manager.AntrianManager;
import manager.FileManager;
import model.Pelanggan;
import model.Petugas;
import simulator.LayananSimulator;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

public class ApotekGUI extends JFrame {
    private AntrianManager antrianManager;
    private FileManager fileManager;
    private List<Petugas> daftarPetugas;
    private List<LayananSimulator> daftarSimulator;

    // Components
    private DefaultListModel<String> antrianListModel;
    private JTextArea logArea;
    private List<JLabel> statusPetugasLabels;
    private JTextField namaInput;
    private JLabel totalAntrianLabel;
    private JLabel totalDilayaniLabel;

    public ApotekGUI() {
        setTitle("Sistem Antrian Apotek");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        // Initialize Managers
        antrianManager = new AntrianManager();
        fileManager = new FileManager("apotek_log.csv");

        // Initialize Petugas & Simulators
        daftarPetugas = new ArrayList<>();
        daftarSimulator = new ArrayList<>();
        setupPetugas();

        // Setup UI
        initComponents();

        // Timer for UI Update (every 500ms)
        new Timer(500, e -> updateTampilan()).start();
    }

    private void setupPetugas() {
        // Create 2 Petugas
        Petugas p1 = new Petugas("Petugas 1");
        Petugas p2 = new Petugas("Petugas 2");
        daftarPetugas.add(p1);
        daftarPetugas.add(p2);

        // Create Simulators (Duration 10 seconds for testing, default 30)
        daftarSimulator.add(new LayananSimulator(p1, antrianManager, fileManager, 10));
        daftarSimulator.add(new LayananSimulator(p2, antrianManager, fileManager, 10));
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));

        // TOP: Registration
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        topPanel.add(new JLabel("Nama Pelanggan:"));
        namaInput = new JTextField(20);
        topPanel.add(namaInput);
        JButton btnDaftar = new JButton("Daftar Antrian");
        btnDaftar.addActionListener(this::handleTambahPelanggan);
        topPanel.add(btnDaftar);

        JButton btnReset = new JButton("Reset Antrian");
        btnReset.addActionListener(e -> {
            antrianManager.clear();
            antrianListModel.clear();
            JOptionPane.showMessageDialog(this, "Antrian di-reset!");
        });
        topPanel.add(btnReset);

        add(topPanel, BorderLayout.NORTH);

        // CENTER: Split Pane (Queue List vs Petugas Status)
        // Left: Queue
        JPanel queuePanel = new JPanel(new BorderLayout());
        queuePanel.setBorder(BorderFactory.createTitledBorder("Daftar Antrian"));
        antrianListModel = new DefaultListModel<>();
        JList<String> antrianList = new JList<>(antrianListModel);
        queuePanel.add(new JScrollPane(antrianList), BorderLayout.CENTER);

        // Right: Petugas Status
        JPanel statusPanel = new JPanel();
        statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.Y_AXIS));
        statusPanel.setBorder(BorderFactory.createTitledBorder("Status Petugas"));

        statusPetugasLabels = new ArrayList<>();
        for (Petugas p : daftarPetugas) {
            JLabel lbl = new JLabel(p.getNama() + ": " + p.getStatusInfo());
            lbl.setBorder(new EmptyBorder(5, 5, 5, 5));
            lbl.setFont(new Font("Monospaced", Font.BOLD, 14));
            statusPetugasLabels.add(lbl);
            statusPanel.add(lbl);
        }

        // Stats
        JPanel statsPanel = new JPanel(new GridLayout(2, 1));
        statsPanel.setBorder(new EmptyBorder(10, 0, 0, 0));
        totalAntrianLabel = new JLabel("Total Antrian: 0");
        totalDilayaniLabel = new JLabel("Total Dilayani: 0");
        statsPanel.add(totalAntrianLabel);
        statsPanel.add(totalDilayaniLabel);
        statusPanel.add(statsPanel);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, queuePanel, statusPanel);
        splitPane.setResizeWeight(0.5);
        add(splitPane, BorderLayout.CENTER);

        // BOTTOM: Controls & Logs
        JPanel bottomPanel = new JPanel(new BorderLayout());

        // Controls
        JPanel controlPanel = new JPanel(new FlowLayout());
        JButton btnMulai = new JButton("Mulai Simulasi");
        btnMulai.addActionListener(e -> {
            for (LayananSimulator sim : daftarSimulator)
                sim.mulaiSimulasi();
            JOptionPane.showMessageDialog(this, "Simulasi Dimulai");
        });
        JButton btnStop = new JButton("Hentikan Simulasi");
        btnStop.addActionListener(e -> {
            for (LayananSimulator sim : daftarSimulator)
                sim.hentikanSimulasi();
            JOptionPane.showMessageDialog(this, "Simulasi Dihentikan");
        });
        JButton btnLog = new JButton("Refresh Log");
        btnLog.addActionListener(e -> loadLog());

        controlPanel.add(btnMulai);
        controlPanel.add(btnStop);
        controlPanel.add(btnLog);
        bottomPanel.add(controlPanel, BorderLayout.NORTH);

        // Log Area
        logArea = new JTextArea(8, 50);
        logArea.setEditable(false);
        bottomPanel.add(new JScrollPane(logArea), BorderLayout.CENTER);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void handleTambahPelanggan(ActionEvent e) {
        String nama = namaInput.getText().trim();
        if (!nama.isEmpty()) {
            String no = antrianManager.tambahPelanggan(nama);
            namaInput.setText("");
            // Optional: updateTampilan will handle list update
            JOptionPane.showMessageDialog(this, "Terdaftar: " + nama + " (" + no + ")");
        }
    }

    private void updateTampilan() {
        // Update Queue List
        List<Pelanggan> antrian = antrianManager.getAntrian();
        // Simple diff check could be better, but recreating for simplicity here
        if (antrianListModel.getSize() != antrian.size()) { // Basic optimization
            antrianListModel.clear();
            for (Pelanggan p : antrian) {
                antrianListModel.addElement(p.toString());
            }
        } else {
            // Check content if needed, but size check is a rough proxy
            // To be safe, just clear and re-add if not too heavy, or check first element
            antrianListModel.clear();
            for (Pelanggan p : antrian) {
                antrianListModel.addElement(p.toString());
            }
        }

        // Update Petugas Status
        for (int i = 0; i < daftarPetugas.size(); i++) {
            Petugas p = daftarPetugas.get(i);
            JLabel lbl = statusPetugasLabels.get(i);
            String status = p.getStatusInfo();
            lbl.setText(p.getNama() + ": " + status);
            if (p.isSedangMelayani()) {
                lbl.setForeground(Color.RED);
            } else {
                lbl.setForeground(new Color(0, 150, 0)); // Green
            }
        }

        // Update Stats
        totalAntrianLabel.setText("Total Antrian: " + antrianManager.getJumlahAntrian());
        totalDilayaniLabel.setText("Total Riwayat: " + antrianManager.getRiwayatLayanan().size());
    }

    private void loadLog() {
        List<String> logs = fileManager.bacaLog();
        StringBuilder sb = new StringBuilder();
        for (String line : logs) {
            sb.append(line).append("\n");
        }
        logArea.setText(sb.toString());
    }
}
