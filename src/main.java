public class main {
    public static void main(String[] args) {

        AntrianManager manager = new AntrianManager();

        System.out.println("Tambah pelanggan:");
        System.out.println(manager.tambahPelanggan("Budi"));
        System.out.println(manager.tambahPelanggan("Siti"));
        System.out.println(manager.tambahPelanggan("Andi"));

        System.out.println("\nLayani pelanggan:");
        System.out.println(manager.ambilPelangganBerikutnya());
        System.out.println(manager.ambilPelangganBerikutnya());

        System.out.println("\nStatistik:");
        System.out.println("Sisa antrian: " + manager.getJumlahAntrian());
        System.out.println("Total dilayani: " + manager.getTotalDilayani());
    }
}
