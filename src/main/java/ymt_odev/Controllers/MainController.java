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
            loadContent("/dashboard.fxml");
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

    public void loadContent(String fxmlPath) {
        if (contentArea == null) {
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(SceneController.class.getResource(fxmlPath));
            Node content = loader.load();
            contentArea.getChildren().setAll(content);
        } catch (IOException e) {
            System.err.println("❌ FXML yükleme hatası: " + fxmlPath);
            e.printStackTrace();
        }
    }

    @FXML private void showDashboard() { loadContent("/dashboard.fxml"); }
    @FXML private void switchToRoomSearch() { loadContent("/room-search.fxml"); }
    @FXML private void showMyReservations() { loadContent("/my-reservations.fxml"); }
    @FXML private void switchToProfile() { loadContent("/profile.fxml"); }
    @FXML private void showCustomerManagement() { loadContent("/customer-management.fxml"); }
    @FXML private void showRoomManagement() { loadContent("/room-management.fxml"); }
    @FXML private void switchToReservationManagement() { loadContent("/reservation-management.fxml"); }
    @FXML private void showCheckinCheckout() { loadContent("/checkin-checkout.fxml"); }
    @FXML private void showUserManagement() { loadContent("/user-management.fxml"); }
    @FXML private void showReports() { loadContent("/reports.fxml"); }
    @FXML private void showSettings() { loadContent("/settings.fxml"); }

    @FXML
    private void handleLogout() {
        SessionManager.clearUser();
        SceneController.switchScene("/login.fxml", "🏨 Otel Yönetim Sistemi - Giriş");
    }

    @FXML
    private void showChangePassword() {
        Dialog<String> dialog = new Dialog<>();
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
        loadContent("/dashboard.fxml");
    }
}
