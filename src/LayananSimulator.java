package simulator;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import manager.AntrianManager;
import model.Pelanggan;
import model.Petugas;

/**
 * LayananSimulator
 * mengatur simulasi pelayanan otomatis menggunakan timer
 */

public class LayananSimulator {

    private AntrianManager antrianManager;
    private List<Petugas> daftarPetugas;

    private Timer timer;
    private boolean isRunning;

    // durasi pelayanan dalam detik
    private int durasiLayanan;

    /**
     * constructor
     *
     * @param antrianManager manager antrian
     * @param daftarPetugas list petugas
     * @param durasiLayanan durasi layanan (detik)
     */

    public LayananSimulator(AntrianManager antrianManager,
                            List<Petugas> daftarPetugas,
                            int durasiLayanan) {
        this.antrianManager = antrianManager;
        this.daftarPetugas = daftarPetugas;
        this.durasiLayanan = durasiLayanan;
        this.isRunning = false;
    }
    /**
     * Memulai simulasi pelayanan
     */

    public void start() {
        if (isRunning) {
            return;
        }

        isRunning = true;
        timer = new Timer();

        // cek kondisi setiap 1 detik
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                prosesLayanan();
            }
        }, 0, 1000);

        System.out.println("[SIMULATOR] Simulasi dimulai");
    }

    /**
     * Menghentikan simulasi pelayanan
     */

    public void stop() {
        if (!isRunning) {
            return;
        }

        timer.cancel();
        isRunning = false;

        System.out.println("[SIMULATOR] Simulasi dihentikan");
    }

    /**
     * Proses utama pelayanan
     * petugas tersedia -> ambil pelanggan -> Layani -> selesai
     */

    private synchronized void prosesLayanan() {

        // jika antrian kosong, tidak lakukan apa-apa
        if (antrianManager.isAntrianKosong()) {
            return;
        }

        for (Petugas petugas : daftarPetugas) {

            // cek petugas tersedia & antrian tidak kosong
            if (petugas.isTersedia() && !antrianManager.isAntrianKosong()) {

                Pelanggan pelanggan = antrianManager.ambilAntrian();

                if (pelanggan == null) {
                    return;
                }

                // mulai layanan
                petugas.mulaiLayanan(pelanggan);

                System.out.println("[LAYANAN] "
                        + petugas.getNama()
                        + " melayani "
                        + pelanggan.getNama()
                        + " (" + pelanggan.getNomorAntrian() + ")");

                // timer untuk menyelesaikan layanan
                Timer selesaiTimer = new Timer();
                selesaiTimer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        petugas.selesaiLayanan();
                        antrianManager.tambahRiwayat(pelanggan, petugas);

                        System.out.println("[SELESAI] "
                                + pelanggan.getNama()
                                + " selesai dilayani oleh "
                                + petugas.getNama());
                    }
                }, durasiLayanan * 1000);
            }
        }
    }

    /**
     * Mengatur durasi Layanan (detik)
     */

    public void setDurasiLayanan(int durasiLayanan) {
        this.durasiLayanan = durasiLayanan;
    }

    /**
     * Mengambil durasi Layanan
     */

    public int getDurasiLayanan() {
        return durasiLayanan;
    }

    /**
     * Mengecek apakah simulasi sedang berjalan
     */

    public boolean isRunning() {
        return isRunning;
    }



}
