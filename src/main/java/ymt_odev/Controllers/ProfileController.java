package ymt_odev.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import ymt_odev.AlertManager;
import ymt_odev.Users.Customer;
import ymt_odev.Users.User;

public class ProfileController extends BaseController {

    @FXML private Text userTypeLabel;
    @FXML private Text userNameTitle;
    @FXML private Text userEmailTitle;
    @FXML private Text memberSinceTitle;
    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private TextField emailField;
    @FXML private TextField phoneField;
    @FXML private TextField tcField;
    @FXML private Text totalReservationsText;
    @FXML private Text loyaltyLevelText;
    @FXML private VBox customerStatsSection;

    @Override
    protected void initialize() {
        super.initialize();
        User user = SessionManager.getUser();
        if (user == null) {
            return;
        }

        userTypeLabel.setText(user.getRole());
        userNameTitle.setText(user.getDisplayName());
        userEmailTitle.setText(user.getEmail());
        memberSinceTitle.setText("Üyelik Tarihi: " + user.getMemberSince());

        firstNameField.setText(user.getFirstName());
        lastNameField.setText(user.getLastName());
        emailField.setText(user.getEmail());
        phoneField.setText(user.getPhone());
        tcField.setText(user.getNationalId());

        if (user instanceof Customer customer) {
            customerStatsSection.setVisible(true);
            totalReservationsText.setText(String.valueOf(customer.getTotalBookings()));
            loyaltyLevelText.setText(customer.getLoyaltyLevel());
        } else {
            customerStatsSection.setVisible(false);
            totalReservationsText.setText("-");
            loyaltyLevelText.setText("-");
        }
    }

    @FXML
    private void saveProfile() {
        User user = SessionManager.getUser();
        if (user == null) return;

        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String email = emailField.getText();
        String phone = phoneField.getText();
        String tcKimlik = tcField.getText();

        boolean success = false;
        if (user instanceof Customer) {
            success = ymt_odev.Services.UserService.updateCustomerProfile(
                    user.getId(),
                    firstName,
                    lastName,
                    email,
                    phone,
                    tcKimlik
            );
        } else {
            success = ymt_odev.Services.UserService.updateStaffProfile(
                    user.getId(),
                    firstName,
                    lastName,
                    email,
                    phone,
                    tcKimlik
            );
        }

        if (success) {
            user.updateProfile(user.getUsername(), firstName, lastName, email, phone, tcKimlik);
            AlertManager.Alert(
                    javafx.scene.control.Alert.AlertType.INFORMATION,
                    "Profil başarıyla güncellendi!",
                    "Başarılı",
                    ""
            );
        } else {
            AlertManager.Alert(
                    javafx.scene.control.Alert.AlertType.ERROR,
                    "Profil güncellenirken bir hata oluştu!",
                    "Hata",
                    ""
            );
        }
    }

    @FXML
    private void resetProfile() {
        initialize();
    }

    @FXML private void setupTwoFactor() {}
    @FXML private void showSessionHistory() {}
    @FXML private void exportData() {}
    @FXML private void deleteAccount() {}

    @FXML
    private void showChangePassword() {
        javafx.scene.control.Dialog<String> dialog = new javafx.scene.control.Dialog<>();
        dialog.setTitle("Şifre Değiştir");
        dialog.setHeaderText("Yeni şifrenizi belirleyin");

        javafx.scene.control.ButtonType saveButton = new javafx.scene.control.ButtonType("Kaydet", javafx.scene.control.ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButton, javafx.scene.control.ButtonType.CANCEL);

        javafx.scene.layout.GridPane grid = new javafx.scene.layout.GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        javafx.scene.control.PasswordField oldPassword = new javafx.scene.control.PasswordField();
        oldPassword.setPromptText("Eski Şifre");
        javafx.scene.control.PasswordField newPassword = new javafx.scene.control.PasswordField();
        newPassword.setPromptText("Yeni Şifre");
        javafx.scene.control.PasswordField confirmPassword = new javafx.scene.control.PasswordField();
        confirmPassword.setPromptText("Yeni Şifre Tekrar");

        grid.add(new javafx.scene.control.Label("Eski Şifre:"), 0, 0);
        grid.add(oldPassword, 1, 0);
        grid.add(new javafx.scene.control.Label("Yeni Şifre:"), 0, 1);
        grid.add(newPassword, 1, 1);
        grid.add(new javafx.scene.control.Label("Yeni Şifre Tekrar:"), 0, 2);
        grid.add(confirmPassword, 1, 2);

        dialog.getDialogPane().setContent(grid);

        dialog.showAndWait().ifPresent(result -> {
            if (!newPassword.getText().equals(confirmPassword.getText())) {
                AlertManager.Alert(javafx.scene.control.Alert.AlertType.ERROR, "Şifreler uyuşmuyor!", "Hata", "Lütfen şifreleri tekrar giriniz.");
            } else if (newPassword.getText().length() < 6) {
                AlertManager.Alert(javafx.scene.control.Alert.AlertType.ERROR, "Şifre en az 6 karakter olmalıdır!", "Hata", "");
            } else {
                User user = SessionManager.getUser();
                if (user != null) {
                    boolean success = ymt_odev.Services.UserService.changePassword(
                            user,
                            oldPassword.getText(),
                            newPassword.getText()
                    );
                    if (success) {
                        AlertManager.Alert(javafx.scene.control.Alert.AlertType.INFORMATION, "Şifre başarıyla değiştirildi!", "Başarılı", "");
                    } else {
                        AlertManager.Alert(javafx.scene.control.Alert.AlertType.ERROR, "Eski şifre hatalı!", "Hata", "");
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
    private void handleLogout() {
        SessionManager.clearUser();
        SceneController.switchScene("/login.fxml", "🏨 Otel Yönetim Sistemi - Giriş");
    }
}
