import java.util.Queue;
import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;
/**
 * AntrianManager adalah class business logic utama
 * yang mengelola sistem antrian apotek menggunakan
 * metode FIFO (First In First Out).
 *
 * Class ini bertugas untuk:
 * - Menambahkan pelanggan ke antrian
 * - Mengambil pelanggan berikutnya
 * - Menyimpan riwayat layanan
 * - Menyediakan statistik antrian
 *
 * Class ini digunakan oleh:
 * - LayananSimulator
 * - Antarmuka pengguna (GUI)
 */

public class AntrianManager {

    // Queue FIFO untuk antrian aktif
    private Queue<String> antrian;

    // Riwayat pelanggan yang sudah dilayani
    private List<String> riwayatLayanan;

    // Counter nomor antrian
    private int nomorAntrian;

    // Statistik
    private int totalDilayani;

    public AntrianManager() {
        antrian = new LinkedList<>();
        riwayatLayanan = new ArrayList<>();
        nomorAntrian = 1;
        totalDilayani = 0;
    }

    // Generate nomor antrian: A001, A002, ...
    private String generateNomorAntrian() {
        return String.format("A%03d", nomorAntrian++);
    }

    // Tambah pelanggan ke antrian
    /**
     * Menambahkan pelanggan baru ke dalam antrian
     * dan menghasilkan nomor antrian secara otomatis.
     *
     * @param nama nama pelanggan
     * @return nomor antrian (contoh: A001)
     */
    public String tambahPelanggan(String nama) {
        String nomor = generateNomorAntrian();
        String data = nomor + " - " + nama;
        antrian.add(data);
        return nomor;
    }

    // Ambil pelanggan berikutnya (FIFO)
    /**
     * Mengambil pelanggan berikutnya dari antrian
     * berdasarkan prinsip FIFO.
     *
     * @return data pelanggan atau null jika antrian kosong
     */
    public String ambilPelangganBerikutnya() {
        if (antrian.isEmpty()) {
            return null;
        }
        String pelanggan = antrian.poll();
        riwayatLayanan.add(pelanggan);
        totalDilayani++;
        return pelanggan;
    }

    public int getJumlahAntrian() {
        return antrian.size();
    }

    public int getTotalDilayani() {
        return totalDilayani;
    }

    public List<String> getRiwayatLayanan() {
        return riwayatLayanan;
    }
}
