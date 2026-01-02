package model;

public class Pelanggan {

    private String nama;
    private String nomorAntrian;
    private String status;

    public Pelanggan(String nama) {
        this.nama = nama;
        this.status = "Menunggu";
    }

    public String getNama() {
        return nama;
    }

    public String getNomorAntrian() {
        return nomorAntrian;
    }

    public void setNomorAntrian(String nomorAntrian) {
        this.nomorAntrian = nomorAntrian;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
