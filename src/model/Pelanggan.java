package model;

import java.time.LocalDateTime;
import java.util.UUID;

public class Pelanggan {
    private String id;
    private String nama;
    private String nomorAntrian;
    private LocalDateTime waktuDaftar;
    private String status; // "Menunggu", "Dilayani", "Selesai"

    public Pelanggan(String nama) {
        this.id = UUID.randomUUID().toString();
        this.nama = nama;
        this.waktuDaftar = LocalDateTime.now();
        this.status = "Menunggu";
    }

    public String getId() {
        return id;
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

    public LocalDateTime getWaktuDaftar() {
        return waktuDaftar;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return nama + " (" + nomorAntrian + ")";
    }
}