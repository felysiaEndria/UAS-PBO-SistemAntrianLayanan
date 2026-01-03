package manager;

import model.Pelanggan;
import java.io.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;

public class FileManager {
    private String filename;
    private static final DateTimeFormatter DATES_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public FileManager(String filename) {
        this.filename = filename;
    }

    public void simpanLog(Pelanggan pelanggan, String namaPetugas, LocalDateTime waktuSelesai) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename, true))) {
            StringBuilder sb = new StringBuilder();
            sb.append(pelanggan.getNomorAntrian()).append(",");
            sb.append(pelanggan.getNama()).append(",");
            sb.append(namaPetugas).append(",");
            sb.append(pelanggan.getWaktuDaftar().format(DATES_FORMAT)).append(",");
            sb.append(waktuSelesai.format(DATES_FORMAT));
            writer.println(sb.toString());
        } catch (IOException e) {
            System.err.println("Gagal menyimpan log: " + e.getMessage());
        }
    }

    public List<String> bacaLog() {
        List<String> logs = new ArrayList<>();
        File file = new File(filename);
        if (!file.exists()) {
            return logs;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                logs.add(line);
            }
        } catch (IOException e) {
            System.err.println("Gagal membaca log: " + e.getMessage());
        }
        return logs;
    }
}
