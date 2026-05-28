package ThreadHub.buyer;

import ThreadHub.controller.DataStore;
import ThreadHub.controller.KeranjangController;
import ThreadHub.model.*;
import ThreadHub.view.StyleKit;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;

import java.util.List;

public class OutfitView {

    private final BuyerDashboardView dashboard;
    private final KeranjangController keranjang;
    private final DataStore ds = DataStore.getInstance();
    private FlowPane cardsContainer;

    public OutfitView(BuyerDashboardView dashboard, KeranjangController keranjang) {
        this.dashboard = dashboard;
        this.keranjang = keranjang;
    }

    public VBox build() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(30));
        content.setStyle("-fx-background-color: #F8F9FA;");

        Label title = new Label("✨ Inspirasi Outfit");
        title.setStyle("-fx-text-fill: #222222; -fx-font-weight: bold; -fx-font-size: 24px;");

        HBox filterBox = new HBox(10);
        filterBox.setAlignment(Pos.CENTER_LEFT);
        String[] styles = {"SEMUA", "STREETWEAR", "CASUAL", "FORMAL", "SPORTY"};
        
        for (String style : styles) {
            Button btnFilter = new Button(style);
            btnFilter.setStyle(
                "-fx-background-color: white; -fx-border-color: #DDDDDD; -fx-border-radius: 20; " +
                "-fx-background-radius: 20; -fx-text-fill: #555555; -fx-font-weight: bold; -fx-cursor: hand;"
            );
            btnFilter.setOnAction(e -> muatDaftarOutfit(style));
            filterBox.getChildren().add(btnFilter);
        }

        cardsContainer = new FlowPane(Orientation.HORIZONTAL, 25, 25);
        cardsContainer.setPadding(new Insets(10, 0, 10, 0));

        ScrollPane scroll = new ScrollPane(cardsContainer);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background: #F8F9FA; -fx-border-color: transparent; -fx-background-color: transparent;");
        VBox.setVgrow(scroll, Priority.ALWAYS);

        muatDaftarOutfit("SEMUA");

        content.getChildren().addAll(title, filterBox, scroll);
        return content;
    }

    private void muatDaftarOutfit(String filter) {
        cardsContainer.getChildren().clear();
        
        List<OutfitBundle> list = filter.equals("SEMUA") 
                                  ? ds.getAllOutfit() 
                                  : ds.getOutfitByStyle(filter);

        if (list.isEmpty()) {
            Label kosong = new Label("Belum ada outfit untuk kategori ini.");
            kosong.setStyle("-fx-text-fill: gray; -fx-font-size: 14px;");
            cardsContainer.getChildren().add(kosong);
            return;
        }

        for (OutfitBundle ob : list) {
            cardsContainer.getChildren().add(buildOutfitCard(ob));
        }
    }

    private VBox buildOutfitCard(OutfitBundle ob) {
        VBox card = new VBox(12);
        
        // 1. LEBAR KARTU DISAMAKAN DENGAN GAMBAR (250)
        card.setPrefWidth(250); 
        card.setStyle(
            "-fx-background-color: white; -fx-background-radius: 12; " +
            "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.08), 10, 0, 0, 5);"
        );

        // --- BOKS UNTUK GAMBAR ---
        VBox imageBox = new VBox();
        imageBox.setAlignment(Pos.CENTER);
        imageBox.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 12 12 0 0;");
        
        // 2. TINGGI BOKS GAMBAR
        imageBox.setPrefHeight(280); 
        
        if (ob.getImagePath() != null && !ob.getImagePath().isEmpty()) {
            try {
                ImageView imgView = new ImageView(new Image(ob.getImagePath()));
                
                // 3. UKURAN GAMBAR (Pas dengan kartu)
                imgView.setFitWidth(250);
                imgView.setFitHeight(280);
                imgView.setPreserveRatio(false); 
                
                // 4. POTONGAN SUDUT MELENGKUNG (Sesuai ukuran gambar)
                javafx.scene.shape.Rectangle clip = new javafx.scene.shape.Rectangle(250, 280);
                clip.setArcWidth(24);
                clip.setArcHeight(24);
                imgView.setClip(clip);
                
                imageBox.getChildren().add(imgView);
            } catch (Exception e) {
                Label lblImgError = new Label("📷\nGambar tidak ditemukan");
                lblImgError.setTextAlignment(TextAlignment.CENTER);
                lblImgError.setTextFill(Color.GRAY);
                imageBox.getChildren().add(lblImgError);
            }
        } else {
            Label lblNoImg = new Label("📷\nBelum ada gambar");
            lblNoImg.setTextAlignment(TextAlignment.CENTER);
            lblNoImg.setTextFill(Color.GRAY);
            imageBox.getChildren().add(lblNoImg);
        }
        // ------------------------

        // --- BOKS UNTUK KONTEN (Teks & Tombol) ---
        VBox contentBox = new VBox(10);
        contentBox.setPadding(new Insets(15));

        Label lblStyle = new Label(ob.getKategoriStyle());
        lblStyle.setStyle("-fx-background-color: #222222; -fx-text-fill: white; -fx-font-size: 10px; -fx-font-weight: bold; -fx-padding: 3 8; -fx-background-radius: 5;");
        
        Label lblNama = new Label(ob.getNamaPaket());
        lblNama.setStyle("-fx-text-fill: #111111; -fx-font-weight: bold; -fx-font-size: 18px;");
        lblNama.setWrapText(true);

        Label lblDeskripsi = new Label(ob.getDeskripsi());
        lblDeskripsi.setStyle("-fx-text-fill: #666666; -fx-font-size: 13px;");
        lblDeskripsi.setWrapText(true);

        VBox itemsBox = new VBox(5);
        itemsBox.setStyle("-fx-background-color: #F9F9F9; -fx-padding: 10; -fx-background-radius: 8;");
        Label lblIsi = new Label("Isi Paket:");
        lblIsi.setStyle("-fx-font-weight: bold; -fx-text-fill: #444444; -fx-font-size: 12px;");
        itemsBox.getChildren().add(lblIsi);
        
        for (Produk p : ob.getListProduk()) {
            Label lblItem = new Label("• " + p.getNama());
            lblItem.setStyle("-fx-text-fill: #555555; -fx-font-size: 13px;");
            itemsBox.getChildren().add(lblItem);
        }

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        Label lblTotal = new Label(ob.getTotalHargaFormatted());
        lblTotal.setStyle("-fx-text-fill: " + StyleKit.ACCENT + "; -fx-font-weight: bold; -fx-font-size: 20px;");

        Button btnBeliSet = StyleKit.primaryButton("🛒 Beli 1 Set");
        btnBeliSet.setMaxWidth(Double.MAX_VALUE);
        btnBeliSet.setOnAction(e -> {
            for (Produk p : ob.getListProduk()) {
                keranjang.tambahItem(p, 1);
            }
            dashboard.updateCartBadge();
            
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Berhasil");
            alert.setHeaderText(null);
            alert.setContentText("Paket '" + ob.getNamaPaket() + "' berhasil ditambahkan ke keranjang!");
            alert.showAndWait();
        });

        contentBox.getChildren().addAll(lblStyle, lblNama, lblDeskripsi, itemsBox, spacer, lblTotal, btnBeliSet);
        
        // Satukan gambar dan konten ke dalam kartu
        card.getChildren().addAll(imageBox, contentBox);
        return card;
    }
}