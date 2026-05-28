package ThreadHub.view;

import ThreadHub.controller.DataStore;
import ThreadHub.model.*;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.stage.Stage;

public class LoginView {

    private final Stage stage;
    private Label errorLabel;

    public LoginView(Stage stage) {
        this.stage = stage;
    }

    public Scene buildScene() {
        // ── Kiri: branding panel ────────────────────────────────────────────
        VBox brandPanel = new VBox(12);
        brandPanel.setAlignment(Pos.CENTER);
        brandPanel.setPrefWidth(400);
        brandPanel.setStyle("-fx-background-color: " + StyleKit.ACCENT + ";");
        brandPanel.setPadding(new Insets(60));

        Label logo = new Label("🧵");
        logo.setFont(Font.font(64));

        Label appName = new Label("ThreadHub");
        appName.setFont(Font.font(StyleKit.FONT_FAMILY, FontWeight.BOLD, 38));
        appName.setTextFill(Color.WHITE);

        Label tagline = new Label("Temukan gaya terbaikmu.\nBelanja pakaian kualitas premium.");
        tagline.setFont(Font.font(StyleKit.FONT_FAMILY, 15));
        tagline.setTextFill(Color.rgb(255, 255, 255, 0.85));
        tagline.setTextAlignment(TextAlignment.CENTER);
        tagline.setWrapText(true);

        brandPanel.getChildren().addAll(logo, appName, tagline);

        // ── Kanan: form login ───────────────────────────────────────────────
        VBox formPanel = new VBox(20);
        formPanel.setAlignment(Pos.CENTER);
        formPanel.setPadding(new Insets(60, 70, 60, 70));
        formPanel.setStyle("-fx-background-color: " + StyleKit.DARK_BG + ";");
        formPanel.setPrefWidth(440);

        Label welcomeLabel = StyleKit.titleLabel("Selamat Datang!", 26);
        Label subLabel     = StyleKit.mutedLabel("Masuk untuk melanjutkan ke ThreadHub");
        subLabel.setFont(Font.font(StyleKit.FONT_FAMILY, 14));

        // Username field
        Label usernameLabel = new Label("Username");
        usernameLabel.setTextFill(Color.web(StyleKit.TEXT_MUTED));
        usernameLabel.setFont(Font.font(StyleKit.FONT_FAMILY, 13));
        TextField usernameField = new TextField();
        usernameField.setPromptText("Masukkan username");
        styleTextField(usernameField);

        // Password field
        Label passwordLabel = new Label("Password");
        passwordLabel.setTextFill(Color.web(StyleKit.TEXT_MUTED));
        passwordLabel.setFont(Font.font(StyleKit.FONT_FAMILY, 13));
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Masukkan password");
        styleTextField(passwordField);

        // Error label
        errorLabel = new Label("");
        errorLabel.setTextFill(Color.web(StyleKit.ACCENT));
        errorLabel.setFont(Font.font(StyleKit.FONT_FAMILY, 13));

        // Tombol login
        Button loginBtn = StyleKit.primaryButton("Masuk");
        loginBtn.setMaxWidth(Double.MAX_VALUE);
        loginBtn.setStyle(loginBtn.getStyle() + "-fx-font-size: 15px; -fx-padding: 13 0;");

        loginBtn.setOnAction(e -> handleLogin(
                usernameField.getText().trim(),
                passwordField.getText()
        ));

        // Enter key
        passwordField.setOnAction(e -> handleLogin(
                usernameField.getText().trim(),
                passwordField.getText()
        ));

        // Info akun demo
        VBox demoInfo = StyleKit.card(14);
        Label demoTitle = new Label("🔑  Akun Demo");
        demoTitle.setTextFill(Color.web(StyleKit.TEXT_MUTED));
        demoTitle.setFont(Font.font(StyleKit.FONT_FAMILY, FontWeight.BOLD, 12));
        Label demoText = new Label("Admin : admin / admin123\nPembeli: budi / budi123");
        demoText.setTextFill(Color.web(StyleKit.TEXT_MUTED));
        demoText.setFont(Font.font(StyleKit.FONT_FAMILY, 12));
        demoInfo.getChildren().addAll(demoTitle, demoText);

        formPanel.getChildren().addAll(
                welcomeLabel, subLabel,
                new Region(), // spacer
                usernameLabel, usernameField,
                passwordLabel, passwordField,
                errorLabel,
                loginBtn,
                demoInfo
        );
        VBox.setMargin(loginBtn, new Insets(4, 0, 0, 0));

        // ── Root layout ──────────────────────────────────────────────────────
        HBox root = new HBox(brandPanel, formPanel);
        HBox.setHgrow(formPanel, Priority.ALWAYS);

        return new Scene(root, 840, 560);
    }

    private void handleLogin(String username, String password) {
        if (username.isEmpty() || password.isEmpty()) {
            errorLabel.setText("⚠  Username dan password tidak boleh kosong.");
            return;
        }
        User user = DataStore.getInstance().login(username, password);
        if (user == null) {
            errorLabel.setText("⚠  Username atau password salah.");
            return;
        }
        errorLabel.setText("");
        if ("admin".equals(user.getRole())) {
            new app.admin.AdminDashboardView(stage, (app.model.Admin) user).show();
        } else {
            new app.buyer.BuyerDashboardView(stage, (app.model.Buyer) user).show();
        }
    }

    private void styleTextField(TextField tf) {
        tf.setStyle(
            "-fx-background-color: " + StyleKit.CARD_BG + ";" +
            "-fx-text-fill: " + StyleKit.TEXT_PRIMARY + ";" +
            "-fx-prompt-text-fill: " + StyleKit.TEXT_MUTED + ";" +
            "-fx-border-color: " + StyleKit.BORDER + ";" +
            "-fx-border-radius: 8;" +
            "-fx-background-radius: 8;" +
            "-fx-padding: 10 14;" +
            "-fx-font-size: 14px;"
        );
        tf.focusedProperty().addListener((obs, old, focused) -> {
            if (focused) {
                tf.setStyle(
                    "-fx-background-color: " + StyleKit.CARD_BG + ";" +
                    "-fx-text-fill: " + StyleKit.TEXT_PRIMARY + ";" +
                    "-fx-prompt-text-fill: " + StyleKit.TEXT_MUTED + ";" +
                    "-fx-border-color: " + StyleKit.ACCENT + ";" +
                    "-fx-border-radius: 8;" +
                    "-fx-background-radius: 8;" +
                    "-fx-padding: 10 14;" +
                    "-fx-font-size: 14px;"
                );
            } else {
                tf.setStyle(
                    "-fx-background-color: " + StyleKit.CARD_BG + ";" +
                    "-fx-text-fill: " + StyleKit.TEXT_PRIMARY + ";" +
                    "-fx-prompt-text-fill: " + StyleKit.TEXT_MUTED + ";" +
                    "-fx-border-color: " + StyleKit.BORDER + ";" +
                    "-fx-border-radius: 8;" +
                    "-fx-background-radius: 8;" +
                    "-fx-padding: 10 14;" +
                    "-fx-font-size: 14px;"
                );
            }
        });
    }

    public void show() {
        stage.setTitle("ThreadHub — Login");
        stage.setScene(buildScene());
        stage.setResizable(false);
        stage.centerOnScreen();
        stage.show();
    }
}
