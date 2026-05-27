package ThreadHub.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Transaksi {
    private static int idCounter = 1;

    private int               id;
    private Buyer             buyer;
    private List<ItemKeranjang> items;
    private double            totalHarga;
    private LocalDateTime     waktu;
    private String            status;   // "BERHASIL", "DIBATALKAN"

    public Transaksi(Buyer buyer, List<ItemKeranjang> items) {
        this.id         = idCounter++;
        this.buyer      = buyer;
        this.items      = List.copyOf(items);
        this.waktu      = LocalDateTime.now();
        this.status     = "BERHASIL";
        this.totalHarga = items.stream()
                               .mapToDouble(ItemKeranjang::getSubtotal)
                               .sum();
    }

    public int                  getId()         { return id; }
    public Buyer                getBuyer()      { return buyer; }
    public List<ItemKeranjang>  getItems()      { return items; }
    public double               getTotalHarga() { return totalHarga; }
    public LocalDateTime        getWaktu()      { return waktu; }
    public String               getStatus()     { return status; }

    public String getTotalFormatted() {
        return String.format("Rp %,.0f", totalHarga);
    }

    public String getWaktuFormatted() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm");
        return waktu.format(fmt);
    }

    public String getRingkasan() {
        return String.format("#TRX-%04d | %s | %s | %s",
                id, getWaktuFormatted(), getTotalFormatted(), status);
    }
}
