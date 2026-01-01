package simulator;

import manager.AntrianManager;
import model.Pelanggan;
import model.Petugas;

/**
 * LayananSimulator
 * mengatur simulasi pelayanan otomatis menggunakan timer
 */

public class LayananSimulator {

    private AntrianManager antrianManager;
    private Timer timer;
    private boolean isRunning;


    /**
     * constructor
     *
     * @param antrianManager manager antrian
     * @param durasiLayanan durasi layanan (detik)
     */

                            int durasiLayanan) {
        this.antrianManager = antrianManager;
        this.durasiLayanan = durasiLayanan;
        this.isRunning = false;
    }
    /**
     * Memulai simulasi pelayanan
     */

            return;
        isRunning = true;
        timer = new Timer();

        // cek kondisi setiap 1 detik
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

            timer.cancel();
        isRunning = false;

    }

    /**
     * Proses utama pelayanan
     * petugas tersedia -> ambil pelanggan -> Layani -> selesai
     */


                petugas.mulaiLayanan(pelanggan);


                    @Override
                    public void run() {
                    }
            }
        }
    }

    /**
     * Mengatur durasi Layanan (detik)
     */

    }
}
