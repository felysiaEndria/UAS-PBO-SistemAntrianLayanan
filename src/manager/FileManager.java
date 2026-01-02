package manager;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

public class FileManager {

    private static final String FILE_LOG = "log_layanan.txt";

    public void simpanLog(String pesan) {
        try (FileWriter writer = new FileWriter(FILE_LOG, true)) {
            writer.write(LocalDateTime.now() + " - " + pesan + "\n");
        } catch (IOException e) {
            System.out.println("Gagal menyimpan log: " + e.getMessage());
        }
    }
}
