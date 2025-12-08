package ymt_odev;

import javafx.scene.control.Alert;

public class AlertManager {
    public static void Alert(Alert.AlertType type, String title, String header, String content){
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();

    }
}
