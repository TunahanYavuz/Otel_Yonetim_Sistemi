package ymt_odev.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import ymt_odev.Access;
import ymt_odev.AlertManager;
import ymt_odev.Services.UserService;
import ymt_odev.Users.User;

import java.io.IOException;
import java.util.Objects;

public class MainController extends BaseController {

    @FXML private Text userInfoText;
    @FXML private VBox customerMenu;
    @FXML private VBox staffMenu;
    @FXML private VBox adminMenu;
    @FXML private StackPane contentArea;

    @Override
    protected void initialize() {
        super.initialize();

        if (SessionManager.isUserLoggedIn()) {
            User currentUser = SessionManager.getUser();
            if (currentUser != null && userInfoText != null) {
                userInfoText.setText(currentUser.getDisplayName());
                toggleMenus(currentUser);
            }
        }

        if (contentArea != null && contentArea.getChildren().isEmpty()) {
            SceneController.loadIntoPane(contentArea ,"/dashboard.fxml");
        }
    }

    private void toggleMenus(User user) {
        if (customerMenu == null || staffMenu == null || adminMenu == null) {
            return;
        }

        customerMenu.setVisible(false);
        staffMenu.setVisible(false);
        adminMenu.setVisible(false);

        if (Objects.equals(user.getRole(), Access.ADMIN.toString())) {
            adminMenu.setVisible(true);
            staffMenu.setVisible(true);
            customerMenu.setVisible(true);
        } else if (Objects.equals(user.getRole(), Access.STAFF.toString())) {
            staffMenu.setVisible(true);
            customerMenu.setVisible(true);
        } else if (Objects.equals(user.getRole(), Access.CUSTOMER.toString())) {
            customerMenu.setVisible(true);
        }
    }


    @FXML private void showDashboard() { SceneController.loadIntoPane(contentArea ,"/dashboard.fxml"); }
    @FXML private void switchToRoomSearch() { SceneController.loadIntoPane(contentArea ,"/room-search.fxml"); }
    @FXML private void showMyReservations() { SceneController.loadIntoPane(contentArea ,"/my-reservations.fxml"); }
    @FXML private void switchToProfile() { SceneController.loadIntoPane(contentArea ,"/profile.fxml"); }
    @FXML private void showCustomerManagement() { SceneController.loadIntoPane(contentArea ,"/customer-management.fxml"); }
    @FXML private void showRoomManagement() { SceneController.loadIntoPane(contentArea ,"/room-management.fxml"); }
    @FXML private void switchToReservationManagement() { SceneController.loadIntoPane(contentArea ,"/reservation-management.fxml"); }
    @FXML private void showCheckinCheckout() { SceneController.loadIntoPane(contentArea ,"/checkin-checkout.fxml"); }
    @FXML private void showUserManagement() { SceneController.loadIntoPane(contentArea ,"/user-management.fxml"); }
    @FXML private void showReports() { SceneController.loadIntoPane(contentArea ,"/reports.fxml"); }
    @FXML private void showSettings() { SceneController.loadIntoPane(contentArea ,"/settings.fxml"); }

    @FXML
    private void handleLogout() {
        SessionManager.clearUser();
        SceneController.switchScene("/login.fxml", "🏨 Otel Yönetim Sistemi - Giriş");
    }

    @FXML
    private void showChangePassword() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Şifre Değiştir");
        dialog.setHeaderText("Yeni şifrenizi belirleyin");

        ButtonType saveButton = new ButtonType("Kaydet", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButton, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        PasswordField oldPassword = new PasswordField();
        oldPassword.setPromptText("Eski Şifre");
        PasswordField newPassword = new PasswordField();
        newPassword.setPromptText("Yeni Şifre");
        PasswordField confirmPassword = new PasswordField();
        confirmPassword.setPromptText("Yeni Şifre Tekrar");

        grid.add(new Label("Eski Şifre:"), 0, 0);
        grid.add(oldPassword, 1, 0);
        grid.add(new Label("Yeni Şifre:"), 0, 1);
        grid.add(newPassword, 1, 1);
        grid.add(new Label("Yeni Şifre Tekrar:"), 0, 2);
        grid.add(confirmPassword, 1, 2);

        dialog.getDialogPane().setContent(grid);

        dialog.showAndWait().ifPresent(result -> {
            if (result != saveButton) {
                return;
            }
            if (!newPassword.getText().equals(confirmPassword.getText())) {
                AlertManager.Alert(Alert.AlertType.ERROR, "Şifreler uyuşmuyor!", "Hata", "Lütfen şifreleri tekrar giriniz.");
            } else if (newPassword.getText().length() < 6) {
                AlertManager.Alert(Alert.AlertType.ERROR, "Şifre en az 6 karakter olmalıdır!", "Hata", "");
            } else {
                User user = SessionManager.getUser();
                if (user != null) {
                    boolean success = UserService.changePassword(
                            user,
                            oldPassword.getText(),
                            newPassword.getText()
                    );
                    if (success) {
                        AlertManager.Alert(Alert.AlertType.INFORMATION, "Şifre başarıyla değiştirildi!", "Başarılı", "");
                    } else {
                        AlertManager.Alert(Alert.AlertType.ERROR, "Eski şifre hatalı!", "Hata", "");
                    }
                }
            }
        });
    }

    @FXML
    private void switchToMain() {
       SceneController.switchScene("/main.fxml", "🏨 Otel Yönetim Sistemi - Ana Panel");
    }

    @FXML
    private void showStaffDashboard() {
        SceneController.loadIntoPane(contentArea ,"/dashboard.fxml");

    }

}
