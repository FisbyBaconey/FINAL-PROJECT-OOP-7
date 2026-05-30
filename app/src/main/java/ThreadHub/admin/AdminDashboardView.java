package ThreadHub.admin;

import ThreadHub.controller.DataStore;
import ThreadHub.model.*;
import ThreadHub.view.*;
import javafx.collections.*;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AdminDashboardView {

    private final Stage stage;
    private final Admin admin;
    private final DataStore ds = DataStore.getInstance();

    private TableView<Produk> table;
    private ObservableList<Produk> produkData;

    public AdminDashboardView(Stage stage, Admin admin) {
        this.stage = stage;
        this.admin = admin;
    }

    public void show() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: " + StyleKit.DARK_BG + ";");

        root.setLeft(buildSidebar(root));

        if (stage.getScene() != null) {
            stage.getScene().setRoot(root);
        } else {
            Scene scene = new Scene(root, 1100, 680);
            stage.setScene(scene);
            stage.centerOnScreen();
        }

        stage.setTitle(admin.getDashboardTitle());
        stage.setResizable(true);
        stage.setMaximized(true);
        stage.show();
    }

    private VBox buildSidebar(BorderPane root) {
        VBox sidebar = new VBox(6);
        sidebar.setPrefWidth(220);
        sidebar.setPadding(new Insets(28, 14, 28, 14));
        sidebar.setStyle("-fx-background-color: " + StyleKit.SIDEBAR_BG + ";");

        Label logo = new Label("🧵 ThreadHub");
        logo.setFont(Font.font(StyleKit.FONT_FAMILY, FontWeight.BOLD, 20));
        logo.setTextFill(Color.web(StyleKit.ACCENT));
        logo.setPadding(new Insets(0, 0, 10, 6));

        Label adminLabel = StyleKit.mutedLabel("Admin: " + admin.getNama());
        adminLabel.setPadding(new Insets(0, 0, 16, 6));

        Button btnProduk    = StyleKit.sidebarButton("📦  Kelola Produk");
        Button btnPengguna  = StyleKit.sidebarButton("👥  Kelola Pengguna");
        Button btnOutfit    = StyleKit.sidebarButton("📦  Kelola Outfit");
        Button btnTransaksi = StyleKit.sidebarButton("📋  Riwayat Transaksi");

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        Button btnLogout = StyleKit.sidebarButton("🚪  Logout");
        btnLogout.setStyle(btnLogout.getStyle().replace(StyleKit.TEXT_PRIMARY, StyleKit.ACCENT));
        btnLogout.setOnAction(e -> logout());

        sidebar.getChildren().addAll(
            logo, adminLabel,
            StyleKit.hSeparator(),
            btnProduk, btnPengguna, btnOutfit, btnTransaksi,
            spacer,
            StyleKit.hSeparator(),
            btnLogout
        );

        btnProduk.setOnAction(e -> root.setCenter(buildContent()));
        btnPengguna.setOnAction(e -> root.setCenter(buildPenggunaPanel()));
        btnOutfit.setOnAction(e -> root.setCenter(buildOutfitPanel()));
        btnTransaksi.setOnAction(e -> root.setCenter(buildTransaksiPanel()));

        return sidebar;
    }

    private VBox buildContent()         { return new VBox(); }
    private VBox buildPenggunaPanel()   { return new VBox(); }
    private VBox buildOutfitPanel()     { return new VBox(); }
    private VBox buildTransaksiPanel()  { return new VBox(); }
    private void logout()               {}
}