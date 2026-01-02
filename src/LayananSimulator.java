package simulator;

import java.time.LocalDateTime;
import java.util.Timer;
import java.util.TimerTask;

import manager.AntrianManager;
import manager.FileManager;
import model.Pelanggan;
import model.Petugas;

/**
 * LayananSimulator
 * mengatur simulasi pelayanan otomatis menggunakan timer
 */

public class LayananSimulator {

    private AntrianManager antrianManager;
    private Petugas petugas;
    private FileManager fileManager;
    private Timer timer;

    // durasi pelayanan dalam detik
    private int durasiLayanan;
    private boolean isRunning;


    public LayananSimulator(Petugas petugas, AntrianManager antrianManager, FileManager fileManager,
                            int durasiLayanan) {
        this.petugas = petugas;
        this.antrianManager = antrianManager;
        this.fileManager = fileManager;
        this.durasiLayanan = durasiLayanan;
        this.isRunning = false;
    }

    /**
     * Memulai simulasi pelayanan
     */

    public void mulaiSimulasi() {
        if (isRunning)
            return;
        isRunning = true;
        timer = new Timer();

        // Cek antrian setiap 1 detik
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                prosesLayanan();
            }
        }, 0, 1000);
    }

    /**
     * Menghentikan simulasi pelayanan
     */

    public void hentikanSimulasi() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        isRunning = false;
    }

    public boolean isRunning() {
        return isRunning;
    }


    private void prosesLayanan() {
        if (!petugas.isSedangMelayani()) {
            Pelanggan pelanggan = antrianManager.ambilPelangganBerikutnya();
            if (pelanggan != null) {
                petugas.mulaiLayanan(pelanggan);
                // System.out.println(petugas.getNama() + " mulai melayani " +
                // pelanggan.getNama());

                // Jadwalkan selesai layanan
                // Kita gunakan timer baru atau thread sleep?
                // Karena kita di dalam TimerTask, sleep akan memblokir pengecekan berikutnya
                // (yg mana tidak masalah karena petugas sibuk)
                // Tapi lebih baik gunakan Timer terpisah atau schedule one-shot task di timer
                // yg sama?
                // Jika pakai schedule di timer yg sama, queue execution.

                // Cara simple: schedule task untuk selesai
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        selesaikanLayanan(pelanggan);
                    }
                }, durasiLayanan * 1000L);
            }
        }
    }


    public void selesaikanLayanan(Pelanggan pelanggan) {
        LocalDateTime waktuSelesai = LocalDateTime.now();
        petugas.selesaiLayanan();
        antrianManager.tambahKeRiwayat(pelanggan);
        fileManager.simpanLog(pelanggan, petugas.getNama(), waktuSelesai);
        // System.out.println(petugas.getNama() + " selesai melayani " +
        // pelanggan.getNama());
    }
}