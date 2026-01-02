public class Pelanggan {

    private String nama;
    private String nomorAntrian;

    // Constructor
    public Pelanggan(String nama) {
        this.nama = nama;
    }

    // Getter nama
    public String getNama() {
        return nama;
    }

    // Setter nomor antrian
    public void setNomorAntrian(String nomorAntrian) {
        this.nomorAntrian = nomorAntrian;
    }

    // Getter nomor antrian
    public String getNomorAntrian() {
        return nomorAntrian;
    }
}
