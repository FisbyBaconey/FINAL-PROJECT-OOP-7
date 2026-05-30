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
  private Stage stage;
  private Label errorLabel;

  public LoginView(Stage stage) {
    this.stage = stage;
  }

  public HBox buildLayout() {
    VBox brandPanel = new VBox(12);
    brandPanel.setAlignment(Pos.CENTER);
    brandPanel.setPrefWidth(400);
    brandPanel.setPadding(new Insets(60));
    brandPanel.setStyle("-fx-background-color: " + StyleKit.ACCENT + ";");

    Label logo = new Label("🧵");
    logo.setFont(Font.font(64));

    Label appName = new Label("ThreadHub");
    appName.setFont(Font.font(StyleKit.FONT_FAMILY, FontWeight.BOLD, 38));
    appName.setTextFill(Color.WHITE);

    Label tagline = new Label("Temukan gaya terbaikmu\nBelanja pakaian kualitas premium");
    tagline.setFont(Font.font(StyleKit.FONT_FAMILY, 15));
    tagline.setTextFill(Color.rgb(255, 255, 255, 0.85));
    tagline.setTextAlignment(TextAlignment.CENTER);
    tagline.setWrapText(true);

    brandPanel.getChildren().addAll(logo, appName, tagline);

    VBox formPanel = new VBox(25);
    formPanel.setAlignment(Pos.CENTER);
    formPanel.setPadding(new Insets(60, 70, 60, 70));
    formPanel.setPrefWidth(440);
    formPanel.setStyle("-fx-background-color: " + StyleKit.DARK_BG + ";");

    Label welcomeLabel = StyleKit.titleLabel("Selamat Datang!", 26);
    Label subLabel = StyleKit.mutedLabel("Masuk untuk melanjutkan ke ThreadHub");
    subLabel.setFont(Font.font(StyleKit.FONT_FAMILY, 14));

    VBox inputContainer = new VBox(14);
    inputContainer.setMaxWidth(320);
    inputContainer.setAlignment(Pos.CENTER_LEFT);

    Label usernameLabel = new Label("Username");
    usernameLabel.setTextFill(Color.web(StyleKit.TEXT_MUTED));
    usernameLabel.setFont(Font.font(StyleKit.FONT_FAMILY, 13));

    TextField usernameField = new TextField();
    usernameField.setPromptText("Masukkan username");
    usernameField.setStyle(
      "-fx-background-color: " + StyleKit.CARD_BG + ";" +
      "-fx-text-fill: " + StyleKit.TEXT_PRIMARY + ";" +
      "-fx-border-color: " + StyleKit.BORDER + ";" +
      "-fx-border-radius: 8;" +
      "-fx-background-radius: 8;" +
      "-fx-padding: 10 14;" +
      "-fx-font-size: 14px;"
    );
        
    Label passwordLabel = new Label("Password");
    passwordLabel.setTextFill(Color.web(StyleKit.TEXT_MUTED));
    passwordLabel.setFont(Font.font(StyleKit.FONT_FAMILY, 13));

    PasswordField passwordField = new PasswordField();
    passwordField.setPromptText("Masukkan password");
    passwordField.setStyle(
      "-fx-background-color: " + StyleKit.CARD_BG + ";" +
      "-fx-text-fill: " + StyleKit.TEXT_PRIMARY + ";" +
      "-fx-border-color: " + StyleKit.BORDER + ";" +
      "-fx-border-radius: 8;" +
      "-fx-background-radius: 8;" +
      "-fx-padding: 10 14;" +
      "-fx-font-size: 14px;"
    );

    errorLabel = new Label("");
    errorLabel.setTextFill(Color.web(StyleKit.ACCENT));
    errorLabel.setFont(Font.font(StyleKit.FONT_FAMILY, 13));

    Button loginBtn = StyleKit.primaryButton("Masuk");
    loginBtn.setMaxWidth(Double.MAX_VALUE);
    loginBtn.setStyle(loginBtn.getStyle() + "-fx-font-size: 15px; -fx-padding: 12 0;");
    loginBtn.setOnAction(e -> handleLogin(usernameField.getText().trim(), passwordField.getText()));

    inputContainer.getChildren().addAll(
      usernameLabel, usernameField,
      passwordLabel, passwordField,
      errorLabel,
      loginBtn
    );

    formPanel.getChildren().addAll(welcomeLabel, subLabel, inputContainer);

    HBox root = new HBox(brandPanel, formPanel);
    HBox.setHgrow(formPanel, Priority.ALWAYS);

    return root;
  }

  public Scene buildScene() {
    return new Scene(buildLayout(), 840, 560);
  }

  private void handleLogin(String username, String password) {
    if (username.isEmpty() || password.isEmpty()) {
      errorLabel.setText("Username dan password tidak boleh kosong");
      return;
    }

    User user = DataStore.getInstance().login(username, password);

    if (user == null) {
      errorLabel.setText("Username atau password salah");
      return;
    }

    errorLabel.setText("");

    if ("admin".equals(user.getRole())) {
      new ThreadHub.admin.AdminDashboardView(stage, (ThreadHub.model.Admin) user).show();
    } else {
      new ThreadHub.buyer.BuyerDashboardView(stage, (ThreadHub.model.Buyer) user).show();
    }
  }
    
  public void show() {
    stage.setTitle("ThreadHub — Login");
    stage.setScene(new Scene(buildLayout(), 840, 560));
    stage.setResizable(true);
    stage.show();
  }
}