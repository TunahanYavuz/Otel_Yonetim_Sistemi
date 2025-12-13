package ymt_odev.Controllers;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ymt_odev.Database.DatabaseConnection;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        SceneController.setPrimaryStage(primaryStage);

        Parent root = FXMLLoader.load(SceneController.class.getResource("/login.fxml"));
        Scene scene = new Scene(root, 900, 650);
        primaryStage.setTitle("🏨 Otel Yönetim Sistemi - Giriş");
        primaryStage.setScene(scene);
        primaryStage.setResizable(true);
        primaryStage.setFullScreen(true);

        // Merkezi konumlandır
        primaryStage.centerOnScreen();

        // Uygulama kapandığında veritabanı bağlantısını kapat
        primaryStage.setOnCloseRequest(event -> {
            System.out.println("🔒 Uygulama kapatılıyor...");
            DatabaseConnection.getInstance().shutdown();
        });

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
