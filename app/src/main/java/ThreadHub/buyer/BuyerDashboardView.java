package ThreadHub.buyer;

import ThreadHub.controller.*;
import ThreadHub.model.*;
import ThreadHub.view.*;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.stage.Stage;

public class BuyerDashboardView {

    private final Stage stage;
    private final Buyer buyer;
    private final KeranjangController keranjang;
    private BorderPane root;
    private Label cartBadge;
    private TextField searchBar; 

    public BuyerDashboardView(Stage stage, Buyer buyer) {
        this.stage     = stage;
        this.buyer     = buyer;
        this.keranjang = new KeranjangController(buyer);
    }

    public void show() {
        root = new BorderPane();
        root.setStyle("-fx-background-color: #FFFFFF;"); 

        root.setTop(buildZaloraHeader());
        showProdukView(); 

        Scene scene = new Scene(root, 1200, 720);
        stage.setTitle("ThreadHub — Belanja");
        stage.setScene(scene);
        stage.setResizable(true);
        stage.centerOnScreen();
        stage.show();
    }

    private VBox buildZaloraHeader() {
        VBox header = new VBox();
        header.setStyle(
            "-fx-background-color: " + StyleKit.CARD_BG + ";" + 
            "-fx-border-color: transparent transparent " + StyleKit.BORDER + " transparent;" +
            "-fx-border-width: 1;"
        );

        // 1. Baris Atas (Logo, Search Bar, User Actions)
        HBox topRow = new HBox(20);
        topRow.setAlignment(Pos.CENTER_LEFT); 
        topRow.setPadding(new Insets(15, 40, 15, 40));

        Label logo = new Label("THREADHUB");
        logo.setFont(Font.font(StyleKit.FONT_FAMILY, FontWeight.EXTRA_BOLD, 26)); 
        logo.setTextFill(Color.web(StyleKit.ACCENT)); 
        logo.setTranslateY(2); // Menjaga kelurusan vertikal agar center
        logo.setOnMouseClicked(e -> {
            searchBar.clear();
            showProdukView();
        });
        logo.setCursor(Cursor.HAND);

        Region spacerLeft = new Region();
        HBox.setHgrow(spacerLeft, Priority.ALWAYS);

        // Search Bar
        searchBar = new TextField();
        searchBar.setPromptText("Cari produk atau kategori... (Tekan Enter)");
        searchBar.setPrefWidth(450);
        searchBar.setStyle(
            "-fx-background-radius: 20; -fx-border-radius: 20; " +
            "-fx-padding: 8 15 8 15; -fx-border-color: #FFFFFF; " + 
            "-fx-background-color: transparent; -fx-text-fill: #FFFFFF; " + 
            "-fx-prompt-text-fill: #AAAAAA;" 
        );
        searchBar.setOnAction(e -> {
            String keyword = searchBar.getText().trim();
            showProdukView("SEMUA", keyword); 
        });

        Region spacerRight = new Region();
        HBox.setHgrow(spacerRight, Priority.ALWAYS);

        HBox userActions = new HBox(20);
        userActions.setAlignment(Pos.CENTER_RIGHT);

        Label greeting = new Label("Hi, " + buyer.getNama());
        greeting.setFont(Font.font(StyleKit.FONT_FAMILY, FontWeight.SEMI_BOLD, 13));
        greeting.setTextFill(Color.WHITE); 
        
        Button btnRiwayat = createFlatButton("Riwayat");
        btnRiwayat.setOnAction(e -> showRiwayatView());

        Button btnLogout = createFlatButton("Logout");
        btnLogout.setOnAction(e -> new LoginView(stage).show());

        StackPane cartIconContainer = buildCartIcon();

        userActions.getChildren().addAll(greeting, btnRiwayat, cartIconContainer, btnLogout);
        topRow.getChildren().addAll(logo, spacerLeft, searchBar, spacerRight, userActions);

        // 2. Baris Bawah (Kategori Menu Navigasi)
        HBox categoryRow = new HBox(35);
        categoryRow.setAlignment(Pos.CENTER);
        categoryRow.setPadding(new Insets(0, 0, 15, 0));

        // PENAMBAHAN MENU OUTFIT DI SINI
        String[] categories = {"PAKAIAN", "WANITA", "PRIA", "ANAK-ANAK", "✨ INSPIRASI OUTFIT"};
        for (String cat : categories) {
            Label catLabel = new Label(cat);
            catLabel.setFont(Font.font(StyleKit.FONT_FAMILY, FontWeight.BOLD, 12));
            catLabel.setCursor(Cursor.HAND);
            catLabel.setTextFill(Color.WHITE); 

            catLabel.setOnMouseEntered(e -> catLabel.setTextFill(Color.web(StyleKit.ACCENT)));
            catLabel.setOnMouseExited(e -> catLabel.setTextFill(Color.WHITE)); 
            
            catLabel.setOnMouseClicked(e -> {
                searchBar.clear(); 
                
                // LOGIKA NAVIGASI BARU
                if (cat.equals("✨ INSPIRASI OUTFIT")) {
                    showOutfitView();
                } else {
                    String targetGender = cat.equals("PAKAIAN") ? "SEMUA" : cat;
                    showProdukView(targetGender);
                }
            }); 
            categoryRow.getChildren().add(catLabel);
        }

        header.getChildren().addAll(topRow, categoryRow);
        return header;
    }

    private StackPane buildCartIcon() {
        StackPane stack = new StackPane();
        stack.setAlignment(Pos.TOP_RIGHT);
        stack.setCursor(Cursor.HAND);

        Button btnKeranjang = new Button("🛒");
        btnKeranjang.setStyle("-fx-background-color: transparent; -fx-font-size: 18px; -fx-padding: 0; -fx-text-fill: white;");
        btnKeranjang.setMouseTransparent(true); 
        
        cartBadge = new Label("0");
        cartBadge.setStyle(
            "-fx-background-color: red; -fx-text-fill: white; -fx-font-size: 10px; " +
            "-fx-font-weight: bold; -fx-background-radius: 10; -fx-padding: 1 5 1 5;"
        );
        cartBadge.setTranslateX(5); 
        cartBadge.setTranslateY(-5);
        cartBadge.setVisible(false);

        stack.getChildren().addAll(btnKeranjang, cartBadge);
        stack.setOnMouseClicked(e -> showIntegrasiKeranjang());

        return stack;
    }

    private void showIntegrasiKeranjang() {
        showKeranjangView();
    }

    private Button createFlatButton(String text) {
        Button btn = new Button(text);
        btn.setStyle("-fx-background-color: transparent; -fx-cursor: hand; -fx-text-fill: white;"); 
        btn.setFont(Font.font(StyleKit.FONT_FAMILY, FontWeight.SEMI_BOLD, 13));
        btn.setOnMouseEntered(e -> btn.setUnderline(true));
        btn.setOnMouseExited(e -> btn.setUnderline(false));
        return btn;
    }

    // ── Navigation helpers ────────────────────────────────────────────────────

    public void showProdukView() {
        showProdukView("SEMUA", "");
    }

    public void showProdukView(String gender) {
        showProdukView(gender, "");
    }

    public void showProdukView(String gender, String keyword) {
        root.setCenter(new ProdukListView(this, buyer, keranjang, gender, keyword).build());
    }

    public void showKeranjangView() {
        root.setCenter(new KeranjangView(this, keranjang).build());
    }

    public void showRiwayatView() {
        root.setCenter(new RiwayatView(buyer).build());
    }

    public void showDetailView(Produk p) {
        root.setCenter(new ProdukDetailView(this, p, keranjang).build());
    }

    // FUNGSI UNTUK MENAMPILKAN HALAMAN OUTFIT
    public void showOutfitView() {
        root.setCenter(new OutfitView(this, keranjang).build());
    }

    public void updateCartBadge() {
        int total = keranjang.getItems().stream()
                .mapToInt(app.model.ItemKeranjang::getJumlah).sum();
        cartBadge.setText(String.valueOf(total));
        cartBadge.setVisible(total > 0);
    }
}