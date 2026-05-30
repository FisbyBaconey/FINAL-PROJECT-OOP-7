package ThreadHub.controller;

import ThreadHub.model.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class KeranjangController {
    private final List<ItemKeranjang> items = new ArrayList<>();
    private final Buyer buyer;

    public KeranjangController(Buyer buyer) {
        this.buyer = buyer;
    }

    public void tambahItem(Produk produk, int jumlah) {
        if (!produk.isAvailable()) return;
        Optional<ItemKeranjang> existing = items.stream()
                .filter(i -> i.getProduk().getId() == produk.getId())
                .findFirst();

        if (existing.isPresent()) {
            int baru = existing.get().getJumlah() + jumlah;
            existing.get().setJumlah(Math.min(baru, produk.getStok()));
        } else {
            items.add(new ItemKeranjang(produk, Math.min(jumlah, produk.getStok())));
        }
    }

    public void hapusItem(ItemKeranjang item) {
        items.remove(item);
    }

    public void kosongkanKeranjang() {
        items.clear();
    }

    public List<ItemKeranjang> getItems() {
        return items;
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public double getTotal() {
        return items.stream().mapToDouble(ItemKeranjang::getSubtotal).sum();
    }

    public String getTotalFormatted() {
        return String.format("Rp %..0f", getTotal());
    }

    public Transaksi checkout() {
        if (isEmpty()) return null;

        for (ItemKeranjang item : items) {
            if (item.getProduk().getStok() < item.getJumlah()) return null;
        }

        for (ItemKeranjang item : items) {
            Produk p = item.getProduk();
            p.setStok(p.getStok() - item.getJumlah());
        }

        int newId = DataStore.getInstance().generateTransaksiId();
        Transaksi trx = new Transaksi(newId, buyer, items);
        
        DataStore.getInstance().tambahTransaksi(trx);
        
        DataStore.getInstance().simpanDataProduk(); 
        
        kosongkanKeranjang();
        return trx;
    }
}