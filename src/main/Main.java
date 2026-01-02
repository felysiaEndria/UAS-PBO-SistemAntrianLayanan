package main;

import manager.AntrianManager;
import model.Pelanggan;

public class Main {
    public static void main(String[] args) {

        AntrianManager manager = new AntrianManager();

        System.out.println("Tambah pelanggan:");
        System.out.println(manager.tambahPelanggan("Budi"));
        System.out.println(manager.tambahPelanggan("Siti"));
        System.out.println(manager.tambahPelanggan("Andi"));

        System.out.println("\nLayani pelanggan:");
        Pelanggan p1 = manager.ambilPelangganBerikutnya();
        manager.tambahKeRiwayat(p1);
        System.out.println(p1.getNomorAntrian() + " - " + p1.getNama());

        Pelanggan p2 = manager.ambilPelangganBerikutnya();
        manager.tambahKeRiwayat(p2);
        System.out.println(p2.getNomorAntrian() + " - " + p2.getNama());

        System.out.println("\nStatistik:");
        System.out.println("Sisa antrian: " + manager.getJumlahAntrian());
        System.out.println("Total dilayani: " + manager.getRiwayatLayanan().size());
    }
}
