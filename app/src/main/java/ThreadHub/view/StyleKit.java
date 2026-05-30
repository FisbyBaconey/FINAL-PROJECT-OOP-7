package ThreadHub.view;

import javafx.scene.control.Button;

public class StyleKit {
  public static final String DARK_BG      = "#1a1a2e";
  public static final String CARD_BG      = "#16213e";   
  public static final String ACCENT       = "#e94560";   
  public static final String ACCENT_HOVER = "#c73652";
  public static final String SUCCESS      = "#0f9b58";
  public static final String WARNING      = "#f39c12";
  public static final String TEXT_PRIMARY = "#eaeaea";
  public static final String TEXT_MUTED   = "#8892a4";
  public static final String BORDER       = "#0f3460";
  public static final String SIDEBAR_BG   = "#0f3460";
  public static final String TEXT_CONTRAST = "#ffffff";
  public static final String FONT_FAMILY  = "Segoe UI";
  public static final double RADIUS       = 10.0;

  public static Button primaryButton(String text) {
    Button btn = new Button(text);
    btn.setStyle(
      "-fx-background-color: " + ACCENT + ";" +
      "-fx-text-fill: white;" +
      "-fx-font-size: 13px;" +
      "-fx-font-weight: bold;" +
      "-fx-padding: 10 24;" +
      "-fx-background-radius: 8;" +
      "-fx-cursor: hand;"
    );
    btn.setOnMouseEntered(e -> btn.setStyle(
      "-fx-background-color: " + ACCENT_HOVER + ";" +
      "-fx-text-fill: white;" +
      "-fx-font-size: 13px;" +
      "-fx-font-weight: bold;" +
      "-fx-padding: 10 24;" +
      "-fx-background-radius: 8;" +
      "-fx-cursor: hand;"
    ));
    btn.setOnMouseExited(e -> btn.setStyle(
      "-fx-background-color: " + ACCENT + ";" +
      "-fx-text-fill: white;" +
      "-fx-font-size: 13px;" +
      "-fx-font-weight: bold;" +
      "-fx-padding: 10 24;" +
      "-fx-background-radius: 8;" +
      "-fx-cursor: hand;"
    ));
    return btn;
  }
  public static Button outlineButton(String text) {
    Button btn = new Button(text);
    btn.setStyle(
      "-fx-background-color: transparent;" +
      "-fx-text-fill: " + ACCENT + ";" +
      "-fx-border-color: " + ACCENT + ";" +
      "-fx-border-radius: 8;" +
      "-fx-background-radius: 8;" +
      "-fx-font-size: 13px;" +
      "-fx-padding: 9 22;" +
      "-fx-cursor: hand;"
    );
    return btn;
  }
}