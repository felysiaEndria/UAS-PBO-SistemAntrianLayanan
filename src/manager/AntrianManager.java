package manager;

import model.Pelanggan;
import java.util.LinkedList;
import java.util.Queue;
import java.util.List;
import java.util.ArrayList;


public class AntrianManager {
    private Queue<Pelanggan> antrian;
    private int nomorAntrianCounter;
    private List<Pelanggan> riwayatLayanan;

    public AntrianManager() {
        this.antrian = new LinkedList<>();
        this.nomorAntrianCounter = 1;
        this.riwayatLayanan = new ArrayList<>();
    }

    public synchronized String tambahPelanggan(String nama) {
        Pelanggan pelanggan = new Pelanggan(nama);
        String nomor = generateNomorAntrian();
        pelanggan.setNomorAntrian(nomor);
        antrian.add(pelanggan);
        return nomor;
    }

    public synchronized Pelanggan ambilPelangganBerikutnya() {
        return antrian.poll();
    }

    public synchronized List<Pelanggan> getAntrian() {
        return new ArrayList<>(antrian);
    }

    public synchronized int getJumlahAntrian() {
        return antrian.size();
    }

    public synchronized List<Pelanggan> getRiwayatLayanan() {
        return riwayatLayanan;
    }

    public synchronized void tambahKeRiwayat(Pelanggan pelanggan) {
        riwayatLayanan.add(pelanggan);
    }

    public synchronized void clear() {
        antrian.clear();
        nomorAntrianCounter = 1;
    }

    private String generateNomorAntrian() {
        String formatted = String.format("A%03d", nomorAntrianCounter);
        nomorAntrianCounter++;
        return formatted;
    }
}
