package ymt_odev.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import ymt_odev.Access;
import ymt_odev.AlertManager;
import ymt_odev.Database.DBDataInsertion;
import ymt_odev.Database.DBDataSelection;
import ymt_odev.Database.DatabaseManager;
import ymt_odev.LoyaltyLevel;
import ymt_odev.Users.Admin;
import ymt_odev.Users.Customer;
import ymt_odev.Users.Staff;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class AuthController extends BaseController {

    // Login.fxml
    @FXML private ToggleGroup userTypeGroup;
    @FXML private RadioButton customerRadio;
    @FXML private RadioButton staffRadio;
    @FXML private TextField username;
    @FXML private PasswordField password;

    // Register.fxml
    @FXML private TextField name;
    @FXML private TextField surname;
    @FXML private TextField email;
    @FXML private TextField phone;
    @FXML private TextField tcKimlik;
    @FXML private PasswordField re_password;
    @FXML private PasswordField checkPassword;
    @FXML private CheckBox agreementCheck;

    @FXML
    public void switchRegister() {
        SceneController.switchScene("/register.fxml", "🏨 Otel Yönetim Sistemi - Kayıt");
    }

    @FXML
    public void switchLogin() {
        SceneController.switchScene("/login.fxml", "🏨 Otel Yönetim Sistemi - Giriş");
    }

    private boolean isValidEmail(String email) {
        return email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }

    @FXML
    public void handleRegister() {
        if (!re_password.getText().equals(checkPassword.getText())) {
            AlertManager.Alert(Alert.AlertType.ERROR, "Şifreler uyuşmuyor!", "Hata", "Lütfen şifreleri tekrar giriniz.");
            return;
        }
        if (!agreementCheck.isSelected()) {
            AlertManager.Alert(Alert.AlertType.ERROR, "Kullanım koşullarını kabul etmelisiniz!", "Hata", "Lütfen kullanım koşullarını kabul ediniz.");
            return;
        }
        if (tcKimlik.getText().length() != 11) {
            AlertManager.Alert(Alert.AlertType.ERROR, "TC kimlik numarası 11 hane olmak zorundadır", "Hata", "Lütfen TC kimlik numaranızı kontrol ediniz.");
            return;
        }
        if (name.getText().isEmpty() || surname.getText().isEmpty() || email.getText().isEmpty() || phone.getText().isEmpty() || tcKimlik.getText().isEmpty() || re_password.getText().isEmpty()) {
            AlertManager.Alert(Alert.AlertType.ERROR, "Lütfen tüm alanları doldurunuz", "Hata", "Eksik bilgi girdiniz.");
            return;
        }
        if (!isValidEmail(email.getText())) {
            AlertManager.Alert(Alert.AlertType.ERROR, "Lütfen geçerli bir e-mail giriniz", "Hata", "Lütfen e-posta adresinizi kontrol ediniz.");
            return;
        }
        if (re_password.getText().length() < 6) {
            AlertManager.Alert(Alert.AlertType.ERROR, "Şifre en az 6 karakter olmalıdır", "Hata", "Lütfen şifrenizi kontrol ediniz.");
            return;
        }

        DatabaseManager insertion = new DBDataInsertion();

        String[] columns = new String[]{
                "name", "surname", "email", "phone", "tcKimlik", "password", "loyaltyLevel", "totalBookings"
        };
        Object[] values = new Object[]{
                name.getText(),
                surname.getText(),
                email.getText(),
                phone.getText(),
                tcKimlik.getText(),
                re_password.getText(),
                LoyaltyLevel.BRONZE.toString(),
                0
        };

        boolean inserted = insertion.insertData("Customers", columns, values);

        if (inserted) {
            SceneController.switchScene("/login.fxml", "🏨 Otel Yönetim Sistemi - Giriş");
        }
    }

    private ResultSet customerLogin() {
        DatabaseManager selector = new DBDataSelection();
        try {
            String[] columns = new String[]{"*"};
            String[] inputs = new String[]{username.getText(), password.getText()};

            // Önce email ile dene
            ResultSet resultSet = selector.selectDataWithCondition(
                    "Customers",
                    columns,
                    new String[]{"email", "password"},
                    inputs
            );

            // Email ile bulunamadıysa TC ile dene
            if (resultSet == null || hasRows(resultSet)) {
                if (resultSet != null) resultSet.close();
                resultSet = selector.selectDataWithCondition(
                        "Customers",
                        columns,
                        new String[]{"tcKimlik", "password"},
                        inputs
                );
            }
            return resultSet;
        } catch (SQLException e) {
            System.out.println("Selection hatası: " + e.getMessage());
            return null;
        }
    }

    private ResultSet staffLogin() {
        DatabaseManager selector = new DBDataSelection();
        try {
            String[] columns = new String[]{"*"};
            String[] inputs = new String[]{username.getText(), password.getText()};

            // Önce email ile dene
            ResultSet resultSet = selector.selectDataWithCondition(
                    "Staff",
                    columns,
                    new String[]{"email", "password"},
                    inputs
            );

            // Email ile bulunamadıysa TC ile dene
            if (resultSet == null || hasRows(resultSet)) {
                if (resultSet != null) resultSet.close();
                resultSet = selector.selectDataWithCondition(
                        "Staff",
                        columns,
                        new String[]{"tcKimlik", "password"},
                        inputs
                );

                // TC ile de bulunamadıysa name ile dene
                if (resultSet == null || hasRows(resultSet)) {
                    if (resultSet != null) resultSet.close();
                    resultSet = selector.selectDataWithCondition(
                            "Staff",
                            columns,
                            new String[]{"userName", "password"},
                            inputs
                    );
                }
            }

            return resultSet;

        } catch (SQLException e) {
            System.out.println("Staff login hatası: " + e.getMessage());
            return null;
        }
    }

    /**
     * ResultSet'in veri içerip içermediğini kontrol eder (cursor'ı hareket ettirmeden)
     */
    private boolean hasRows(ResultSet rs) {
        try {
            // isBeforeFirst() - cursor başlangıçtaysa ve veri varsa true döner
            return rs == null || !rs.isBeforeFirst();
        } catch (SQLException e) {
            return true;
        }
    }

    @FXML
    public void handleLogin() {
        boolean isCustomerLogin = customerRadio.isSelected();
        boolean isStaffLogin = staffRadio.isSelected();

        if (!isCustomerLogin && !isStaffLogin) {
            AlertManager.Alert(
                    Alert.AlertType.WARNING,
                    "Lütfen giriş türünü seçiniz!",
                    "Giriş Türü Seçilmedi",
                    "Müşteri veya Personel girişinden birini seçmelisiniz."
            );
            return;
        }

        ResultSet resultSet = null;

        try {
            resultSet = isCustomerLogin ? customerLogin() : staffLogin();

            boolean foundUser = resultSet != null && resultSet.next();

            if (foundUser) {
                Map<String, Object> user = new HashMap<>();
                user.put("id", resultSet.getObject("id"));
                user.put("email", resultSet.getObject("email"));
                user.put("phone", resultSet.getObject("phone"));
                user.put("tcKimlik", resultSet.getObject("tcKimlik"));
                user.put("createdDate", resultSet.getObject("createdDate"));
                user.put("name", resultSet.getObject("name"));
                user.put("surname", resultSet.getObject("surname"));
                user.put("password", resultSet.getObject("password"));
                user.put("isActive", resultSet.getObject("isActive"));

                if (isCustomerLogin) {
                    user.put("loyaltyLevel", resultSet.getObject("loyaltyLevel"));
                    user.put("totalBookings", resultSet.getObject("totalBookings"));

                    SessionManager.setUser(new Customer(
                            ((Number) user.get("id")).intValue(),
                            user.get("name").toString() + " " + user.get("surname").toString(),
                            user.get("password").toString(),
                            user.get("email").toString(),
                            user.get("phone").toString(),
                            user.get("tcKimlik").toString(),
                            user.get("name").toString(),
                            user.get("surname").toString(),
                            user.get("createdDate").toString(),
                            user.get("loyaltyLevel").toString(),
                            ((Number) user.get("totalBookings")).intValue(),
                            user.get("isActive") != null && (Boolean) user.get("isActive")
                    ));

                    System.out.println("✅ Müşteri Girişi Başarılı: " + user.get("name"));
                } else {
                    user.put("department", resultSet.getObject("department"));
                    user.put("shift", resultSet.getObject("shift"));
                    user.put("userType", resultSet.getObject("userType"));
                    user.put("accessLevel", resultSet.getObject("accessLevel"));

                    String role = user.get("userType") != null ? user.get("userType").toString() : Access.STAFF.toString();
                    String userName = user.get("email").toString(); // username olarak email kullan

                    if (Access.ADMIN.toString().equals(role)) {
                        SessionManager.setUser(new Admin(
                                ((Number) user.get("id")).intValue(),
                                userName,
                                user.get("password").toString(),
                                user.get("email").toString(),
                                user.get("phone").toString(),
                                user.get("tcKimlik").toString(),
                                user.get("name").toString(),
                                user.get("surname").toString(),
                                user.get("createdDate").toString(),
                                user.get("department") != null ? user.get("department").toString() : "",
                                user.get("accessLevel") != null ? user.get("accessLevel").toString() : Access.ADMIN.toString(),
                                user.get("isActive") != null && (Boolean) user.get("isActive")

                        ));
                    } else {
                        SessionManager.setUser(new Staff(
                                ((Number) user.get("id")).intValue(),
                                userName,
                                user.get("password").toString(),
                                user.get("email").toString(),
                                user.get("phone").toString(),
                                user.get("tcKimlik").toString(),
                                user.get("name").toString(),
                                user.get("surname").toString(),
                                user.get("createdDate").toString(),
                                user.get("department") != null ? user.get("department").toString() : "",
                                user.get("shift") != null ? user.get("shift").toString() : "",
                                user.get("accessLevel") != null ? user.get("accessLevel").toString() : "STANDARD",
                                user.get("isActive") != null && (Boolean) user.get("isActive")

                        ));
                    }

                    System.out.println("✅ Personel Girişi Başarılı: " + user.get("name") + " (" + role + ")");
                }

                SceneController.switchScene("/main.fxml", "🏨 Otel Yönetim Sistemi - Ana Panel");

            } else {
                AlertManager.Alert(Alert.AlertType.ERROR, "Kullanıcı bulunamadı!", "Hata", "Lütfen kullanıcı adı veya şifrenizi kontrol ediniz.");
            }
        } catch (SQLException e) {
            System.out.println("Login hatası: " + e.getMessage());
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    System.out.println("ResultSet kapama hatası: " + e.getMessage());
                }
            }
        }
    }
}

