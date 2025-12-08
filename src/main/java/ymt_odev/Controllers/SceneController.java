package ymt_odev.Controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;

public class SceneController {
    private static Stage primaryStage;

    public static void setPrimaryStage(Stage stage) {
        primaryStage = stage;
    }

    public static void switchScene(String fxmlFile, String title) {
        try {
            double currentHeight = primaryStage.getScene().getHeight();
            double currentWidth = primaryStage.getScene().getWidth();
            boolean maximized = primaryStage.isMaximized();
            Parent root = FXMLLoader.load(SceneController.class.getResource(fxmlFile));
            Scene scene = new Scene(root, currentWidth, currentHeight);
            primaryStage.setTitle(title);
            primaryStage.setScene(scene);
            primaryStage.setMaximized(maximized);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Belirtilen FXML dosyasını bir StackPane içine yükler
     * @param pane Hedef StackPane
     * @param fxmlPath FXML dosya yolu
     */
    public static void loadIntoPane(StackPane pane, String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(SceneController.class.getResource(fxmlPath));
            Node content = loader.load();
            pane.getChildren().setAll(content);

        } catch (IOException e) {
            System.err.println("❌ FXML yükleme hatası: " + fxmlPath);
            e.printStackTrace();
        }
    }
}
