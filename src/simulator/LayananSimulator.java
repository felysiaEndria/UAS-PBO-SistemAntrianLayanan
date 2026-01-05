//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package simulator;

import java.time.LocalDateTime;
import java.util.Timer;
import java.util.TimerTask;
import manager.AntrianManager;
import manager.FileManager;
import model.Pelanggan;
import model.Petugas;

public class LayananSimulator {
    private Petugas petugas;
    private AntrianManager antrianManager;
    private FileManager fileManager;
    private Timer timer;
    private int durasiLayanan;
    private boolean isRunning;

    public LayananSimulator(Petugas petugas, AntrianManager antrianManager, FileManager fileManager, int durasiLayanan) {
        this.petugas = petugas;
        this.antrianManager = antrianManager;
        this.fileManager = fileManager;
        this.durasiLayanan = durasiLayanan;
        this.isRunning = false;
    }

    public void mulaiSimulasi() {
        if (!this.isRunning) {
            this.isRunning = true;
            this.timer = new Timer();
            this.timer.scheduleAtFixedRate(new TimerTask() {
                public void run() {
                    LayananSimulator.this.prosesLayanan();
                }
            }, 0L, 1000L);
        }
    }

    public void hentikanSimulasi() {
        if (this.timer != null) {
            this.timer.cancel();
            this.timer = null;
        }

        this.isRunning = false;
    }

    public boolean isRunning() {
        return this.isRunning;
    }

    private void prosesLayanan() {
        if (!this.petugas.isSedangMelayani()) {
            final Pelanggan pelanggan = this.antrianManager.ambilPelangganBerikutnya();
            if (pelanggan != null) {
                this.petugas.mulaiLayanan(pelanggan);
                (new Timer()).schedule(new TimerTask() {
                    public void run() {
                        LayananSimulator.this.selesaikanLayanan(pelanggan);
                    }
                }, (long)this.durasiLayanan * 1000L);
            }
        }

    }

    private void selesaikanLayanan(Pelanggan pelanggan) {
        LocalDateTime waktuSelesai = LocalDateTime.now();
        this.petugas.selesaiLayanan();
        this.antrianManager.tambahKeRiwayat(pelanggan);
        this.fileManager.simpanLog(pelanggan, this.petugas.getNama(), waktuSelesai);
    }
}
