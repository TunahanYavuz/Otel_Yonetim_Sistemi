package ymt_odev.Controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import ymt_odev.AlertManager;
import ymt_odev.Database.DBDataDeleter;
import ymt_odev.Database.DBDataInsertion;
import ymt_odev.Database.DBDataSelection;
import ymt_odev.Database.DBDataUpdater;
import ymt_odev.Database.DatabaseManager;
import ymt_odev.Users.Admin;
import ymt_odev.Users.Customer;
import ymt_odev.Users.Staff;
import ymt_odev.Users.User;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserManagementController extends BaseController {

    @FXML private TextField userSearchField;
    @FXML private TableView<User> usersTable;
    @FXML private ComboBox<String> roleBox;
    @FXML private ComboBox<String> statusBox;

    // İstatistik alanları
    @FXML private Text totalUserCount;
    @FXML private Text adminCount;
    @FXML private Text staffCount;
    @FXML private Text activeUserCount;

    private ObservableList<User> userList = FXCollections.observableArrayList();

    @Override
    protected void initialize() {
        super.initialize();
        loadUsers();
        setupTableColumns();
        updateStatistics();
    }

    /**
     * Kullanıcı istatistiklerini veritabanından çeker ve günceller
     */
    private void updateStatistics() {
        int totalUsers = userList.size();
        int admins = 0;
        int staffMembers = 0;
        int activeUsers = 0;

        for (User user : userList) {
            if (user instanceof Admin) {
                admins++;
            } else if (user instanceof Staff) {
                staffMembers++;
            }

            // Aktif kullanıcı kontrolü
            if (user instanceof Customer) {
                if (user.isActive()) {
                    activeUsers++;
                }
            } else if (user instanceof Staff) {
                if (user.isActive()) {
                    activeUsers++;
                }
            } else if (user instanceof Admin) {
                if (user.isActive()) {
                    activeUsers++;
                }
            }
        }

        // UI'ı güncelle
        if (totalUserCount != null) totalUserCount.setText(String.valueOf(totalUsers));
        if (adminCount != null) adminCount.setText(String.valueOf(admins));
        if (staffCount != null) staffCount.setText(String.valueOf(staffMembers));
        if (activeUserCount != null) activeUserCount.setText(String.valueOf(activeUsers));
    }

    private void setupTableColumns() {
        if (usersTable.getColumns().isEmpty()) return;

        TableColumn<User, Integer> idCol = (TableColumn<User, Integer>) usersTable.getColumns().get(0);
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<User, String> usernameCol = (TableColumn<User, String>) usersTable.getColumns().get(1);
        usernameCol.setCellValueFactory(new PropertyValueFactory<>("username"));

        TableColumn<User, String> nameCol = (TableColumn<User, String>) usersTable.getColumns().get(2);
        nameCol.setCellValueFactory(cellData -> {
            User user = cellData.getValue();
            return new SimpleStringProperty(user.getFirstName() + " " + user.getLastName());
        });

        TableColumn<User, String> emailCol = (TableColumn<User, String>) usersTable.getColumns().get(3);
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));

        TableColumn<User, String> roleCol = (TableColumn<User, String>) usersTable.getColumns().get(4);
        roleCol.setCellValueFactory(cellData -> {
            User user = cellData.getValue();

            return new SimpleStringProperty(user.getRole());
        });

        TableColumn<User, String> statusCol = (TableColumn<User, String>) usersTable.getColumns().get(5);
        statusCol.setCellValueFactory(cellData -> {
            User user = cellData.getValue();
            if (user instanceof Customer) {
                return new SimpleStringProperty(((Customer) user).getLoyaltyLevel());
            }
            return new SimpleStringProperty("Aktif");
        });

        TableColumn<User, String> lastLoginCol = (TableColumn<User, String>) usersTable.getColumns().get(6);
        lastLoginCol.setCellValueFactory(new PropertyValueFactory<>("memberSince"));

        // İşlemler sütunu
        TableColumn<User, Void> actionsCol = (TableColumn<User, Void>) usersTable.getColumns().get(7);
        actionsCol.setCellFactory(param -> new TableCell<>() {
            private final Button editBtn = new Button("✏️");
            private final Button deleteBtn = new Button("🗑️");
            private final HBox buttons = new HBox(5, editBtn, deleteBtn);

            {
                editBtn.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-cursor: hand;");
                deleteBtn.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-cursor: hand;");

                editBtn.setOnAction(e -> {
                    User user = getTableView().getItems().get(getIndex());
                    if (user instanceof Staff) {
                        editUser((Staff) user);
                    } else if (user instanceof Customer) {
                        editCustomer((Customer) user);
                    } else if (user instanceof Admin) {
                        editAdmin((Admin) user);
                    }
                });

                deleteBtn.setOnAction(e -> {
                    User user = getTableView().getItems().get(getIndex());
                    if (user instanceof Staff) {
                        deleteUser((Staff) user);
                    } else if (user instanceof Customer) {
                        deleteCustomer((Customer) user);
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : buttons);
            }
        });

        //usersTable.setItems(userList);
    }

    private void loadUsers() {
        userList.clear();
        DBDataSelection selector = new DBDataSelection();

        // Müşterileri yükle
        try {
            ResultSet rs = selector.selectData("Customers", new String[]{"*"});
            if (rs != null) {
                while (rs.next()) {
                    Customer customer = new Customer(
                            rs.getInt("id"),
                            rs.getString("name") + " " + rs.getString("surname"),
                            rs.getString("password"),
                            rs.getString("email"),
                            rs.getString("phone"),
                            rs.getString("tcKimlik"),
                            rs.getString("name"),
                            rs.getString("surname"),
                            rs.getString("createdDate") != null ? rs.getString("createdDate") : "",
                            rs.getString("loyaltyLevel") != null ? rs.getString("loyaltyLevel") : "Bronze",
                            rs.getInt("totalBookings"),
                            rs.getBoolean("isActive")
                    );
                    userList.add(customer);
                }
                rs.close();
            }
        } catch (SQLException e) {
            System.err.println("Müşteri listesi yükleme hatası: " + e.getMessage());
        }

        // Personeli yükle
        try {
            ResultSet rs = selector.selectData("Staff", new String[]{"*"});
            if (rs != null) {
                while (rs.next()) {
                    if (rs.getString("accessLevel").equalsIgnoreCase("Admin")){
                        Admin admin = new Admin(
                                rs.getInt("id"),
                                rs.getString("userName"),
                                rs.getString("password"),
                                rs.getString("email"),
                                rs.getString("phone"),
                                rs.getString("tcKimlik"),
                                rs.getString("name"),
                                rs.getString("surname") != null ? rs.getString("surname") : "",
                                rs.getString("createdDate") != null ? rs.getString("createdDate") : "",
                                rs.getString("department"),
                                rs.getString("accessLevel"),
                                rs.getBoolean("isActive")
                        );
                        userList.add(admin);

                    }else {
                        Staff staff = new Staff(
                                rs.getInt("id"),
                                rs.getString("userName"),
                                rs.getString("password"),
                                rs.getString("email"),
                                rs.getString("phone"),
                                rs.getString("tcKimlik"),
                                rs.getString("name"),
                                rs.getString("surname") != null ? rs.getString("surname") : "",
                                rs.getString("createdDate") != null ? rs.getString("createdDate") : "",
                                rs.getString("department"),
                                rs.getString("shift") != null ? rs.getString("shift") : "Gündüz",
                                rs.getString("accessLevel"),
                                rs.getBoolean("isActive")
                        );
                        userList.add(staff);
                    }
                }
                rs.close();
            }
        } catch (SQLException e) {
            System.err.println("Personel listesi yükleme hatası: " + e.getMessage());
        }
        usersTable.setItems(userList);
        setupTableColumns();
        updateStatistics();
    }

    @FXML
    private void searchUsers() {
        String searchText = userSearchField.getText().trim().toLowerCase();

        if (searchText.isEmpty() && roleBox.getValue() == null && statusBox.getValue() == null) {
            loadUsers();
            setupTableColumns();
            return;
        }
        boolean selectedActivity;
        try {
            selectedActivity = switch (statusBox.getValue()) {
                case "Pasif" -> false;
                case null, default -> true;
            };
        }catch (NullPointerException e){
            selectedActivity = true;
        }


        ObservableList<User> filteredList = FXCollections.observableArrayList();
        for (User user : userList) {
            if ((!user.getRole().equalsIgnoreCase(roleBox.getValue()) && !roleBox.getValue().equalsIgnoreCase("Tümü"))) continue;
            if (user.isActive() != selectedActivity) continue;
            if (user.getFirstName().toLowerCase().contains(searchText) ||
                user.getLastName().toLowerCase().contains(searchText) ||
                user.getEmail().toLowerCase().contains(searchText) ||
                user.getUsername().toLowerCase().contains(searchText)) {
                filteredList.add(user);
            }
        }
        usersTable.setItems(filteredList);
        setupTableColumns();
    }

    private void editUser(Staff staff) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Personel Düzenle");
        dialog.setHeaderText("Personel bilgilerini düzenleyin");

        ButtonType saveButton = new ButtonType("Kaydet", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButton, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        TextField nameField = new TextField(staff.getFirstName());
        TextField emailField = new TextField(staff.getEmail());
        TextField phoneField = new TextField(staff.getPhone());
        ComboBox<String> departmentCombo = new ComboBox<>();
        departmentCombo.getItems().addAll("Resepsiyon", "Temizlik", "Muhasebe", "IT", "Yönetim");
        departmentCombo.setValue(staff.getDepartment());
        ComboBox<String> accessLevelCombo = new ComboBox<>();
        accessLevelCombo.getItems().addAll("Personel", "Admin");
        accessLevelCombo.setValue(staff.getAccessLevel());

        grid.add(new Label("Ad Soyad:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("E-posta:"), 0, 1);
        grid.add(emailField, 1, 1);
        grid.add(new Label("Telefon:"), 0, 2);
        grid.add(phoneField, 1, 2);
        grid.add(new Label("Departman:"), 0, 3);
        grid.add(departmentCombo, 1, 3);
        grid.add(new Label("Yetki:"), 0, 4);
        grid.add(accessLevelCombo, 1, 4);

        dialog.getDialogPane().setContent(grid);

        dialog.showAndWait().ifPresent(response -> {
            if (response == saveButton) {
                DatabaseManager updater = new DBDataUpdater();

                String[] columns = new String[]{"name", "email", "phone", "department", "accessLevel"};
                String[] values = new String[]{
                        nameField.getText(),
                        emailField.getText(),
                        phoneField.getText(),
                        departmentCombo.getValue(),
                        accessLevelCombo.getValue()
                };
                String[] whereClause = new String[]{"id"};
                Object[] whereParams = new Object[]{staff.getId()};

                boolean success = updater.updateDataWithCondition("Staff", columns, columns, values, whereClause, whereParams);

                if (success) {
                    AlertManager.Alert(Alert.AlertType.INFORMATION, "Personel başarıyla güncellendi!", "Başarılı", "");
                    loadUsers();
                } else {
                    AlertManager.Alert(Alert.AlertType.ERROR, "Personel güncellenemedi!", "Hata", "");
                }
            }
        });
    }
    private void editAdmin(Admin admin) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Personel Düzenle");
        dialog.setHeaderText("Personel bilgilerini düzenleyin");

        ButtonType saveButton = new ButtonType("Kaydet", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButton, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        TextField nameField = new TextField(admin.getFirstName());
        TextField emailField = new TextField(admin.getEmail());
        TextField phoneField = new TextField(admin.getPhone());
        ComboBox<String> departmentCombo = new ComboBox<>();
        departmentCombo.getItems().addAll("Resepsiyon", "Temizlik", "Muhasebe", "IT", "Yönetim");
        departmentCombo.setValue(admin.getDepartment());
        ComboBox<String> accessLevelCombo = new ComboBox<>();
        accessLevelCombo.getItems().addAll("Personel", "Admin");
        accessLevelCombo.setValue(admin.getAccessLevel());

        grid.add(new Label("Ad Soyad:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("E-posta:"), 0, 1);
        grid.add(emailField, 1, 1);
        grid.add(new Label("Telefon:"), 0, 2);
        grid.add(phoneField, 1, 2);
        grid.add(new Label("Departman:"), 0, 3);
        grid.add(departmentCombo, 1, 3);
        grid.add(new Label("Yetki:"), 0, 4);
        grid.add(accessLevelCombo, 1, 4);

        dialog.getDialogPane().setContent(grid);

        dialog.showAndWait().ifPresent(response -> {
            if (response == saveButton) {
                DatabaseManager updater = new DBDataUpdater();

                String[] columns = new String[]{"name", "email", "phone", "department", "accessLevel"};
                String[] values = new String[]{
                        nameField.getText(),
                        emailField.getText(),
                        phoneField.getText(),
                        departmentCombo.getValue(),
                        accessLevelCombo.getValue()
                };
                String[] whereClause = new String[]{"id"};
                Object[] whereParams = new Object[]{admin.getId()};

                boolean success = updater.updateDataWithCondition("Staff", columns, columns, values, whereClause, whereParams);

                if (success) {
                    AlertManager.Alert(Alert.AlertType.INFORMATION, "Personel başarıyla güncellendi!", "Başarılı", "");
                    loadUsers();
                } else {
                    AlertManager.Alert(Alert.AlertType.ERROR, "Personel güncellenemedi!", "Hata", "");
                }
            }
        });
    }

    private void deleteUser(Staff staff) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Silme Onayı");
        confirm.setHeaderText("Personeli silmek istediğinize emin misiniz?");
        confirm.setContentText("Bu işlem geri alınamaz!");

        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                DBDataDeleter deleter = new DBDataDeleter();
                boolean success = deleter.deleteData("Staff", "id = ?", new Object[]{staff.getId()});

                if (success) {
                    AlertManager.Alert(Alert.AlertType.INFORMATION, "Personel başarıyla silindi!", "Başarılı", "");
                    loadUsers();
                } else {
                    AlertManager.Alert(Alert.AlertType.ERROR, "Personel silinemedi!", "Hata", "");
                }
            }
        });
    }

    private void editCustomer(Customer customer) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Müşteri Düzenle");
        dialog.setHeaderText("Müşteri bilgilerini düzenleyin");

        ButtonType saveButton = new ButtonType("Kaydet", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButton, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        TextField nameField = new TextField(customer.getFirstName());
        TextField surnameField = new TextField(customer.getLastName());
        TextField emailField = new TextField(customer.getEmail());
        TextField phoneField = new TextField(customer.getPhone());
        TextField tcField = new TextField(customer.getNationalId());
        ComboBox<String> loyaltyCombo = new ComboBox<>();
        loyaltyCombo.getItems().addAll("Bronze", "Silver", "Gold", "Platinum");
        loyaltyCombo.setValue(customer.getLoyaltyLevel());

        grid.add(new Label("Ad:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Soyad:"), 0, 1);
        grid.add(surnameField, 1, 1);
        grid.add(new Label("E-posta:"), 0, 2);
        grid.add(emailField, 1, 2);
        grid.add(new Label("Telefon:"), 0, 3);
        grid.add(phoneField, 1, 3);
        grid.add(new Label("TC Kimlik:"), 0, 4);
        grid.add(tcField, 1, 4);
        grid.add(new Label("Sadakat Seviyesi:"), 0, 5);
        grid.add(loyaltyCombo, 1, 5);

        dialog.getDialogPane().setContent(grid);

        dialog.showAndWait().ifPresent(response -> {
            if (response == saveButton) {
                DatabaseManager updater = new DBDataUpdater();

                String[] columns = new String[]{"name", "surname", "email", "phone", "tcKimlik", "loyaltyLevel"};
                String[] values = new String[]{
                        nameField.getText(),
                        surnameField.getText(),
                        emailField.getText(),
                        phoneField.getText(),
                        tcField.getText(),
                        loyaltyCombo.getValue()
                };
                String[] whereClause = new String[]{"id"};
                Object[] whereParams = new Object[]{customer.getId()};

                boolean success = updater.updateDataWithCondition("Customers", columns, columns, values, whereClause, whereParams);

                if (success) {
                    AlertManager.Alert(Alert.AlertType.INFORMATION, "Müşteri başarıyla güncellendi!", "Başarılı", "");
                    loadUsers();
                } else {
                    AlertManager.Alert(Alert.AlertType.ERROR, "Müşteri güncellenemedi!", "Hata", "");
                }
            }
        });
    }

    private void deleteCustomer(Customer customer) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Silme Onayı");
        confirm.setHeaderText("Müşteriyi silmek istediğinize emin misiniz?");
        confirm.setContentText("Bu işlem geri alınamaz!");

        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                DBDataDeleter deleter = new DBDataDeleter();
                boolean success = deleter.deleteData("Customers", "id = ?", new Object[]{customer.getId()});

                if (success) {
                    AlertManager.Alert(Alert.AlertType.INFORMATION, "Müşteri başarıyla silindi!", "Başarılı", "");
                    loadUsers();
                } else {
                    AlertManager.Alert(Alert.AlertType.ERROR, "Müşteri silinemedi!", "Hata", "");
                }
            }
        });
    }

    @FXML
    private void addUser() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Yeni Personel Ekle");
        dialog.setHeaderText("Personel bilgilerini girin");

        ButtonType saveButton = new ButtonType("Kaydet", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButton, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        TextField nameField = new TextField();
        TextField emailField = new TextField();
        TextField phoneField = new TextField();
        TextField tcField = new TextField();
        PasswordField passwordField = new PasswordField();
        ComboBox<String> departmentCombo = new ComboBox<>();
        departmentCombo.getItems().addAll("Resepsiyon", "Temizlik", "Muhasebe", "IT", "Yönetim");
        ComboBox<String> userTypeCombo = new ComboBox<>();
        userTypeCombo.getItems().addAll("STAFF", "ADMIN");

        grid.add(new Label("Ad Soyad:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("E-posta:"), 0, 1);
        grid.add(emailField, 1, 1);
        grid.add(new Label("Telefon:"), 0, 2);
        grid.add(phoneField, 1, 2);
        grid.add(new Label("TC Kimlik:"), 0, 3);
        grid.add(tcField, 1, 3);
        grid.add(new Label("Şifre:"), 0, 4);
        grid.add(passwordField, 1, 4);
        grid.add(new Label("Departman:"), 0, 5);
        grid.add(departmentCombo, 1, 5);
        grid.add(new Label("Yetki:"), 0, 6);
        grid.add(userTypeCombo, 1, 6);

        dialog.getDialogPane().setContent(grid);

        dialog.showAndWait().ifPresent(response -> {
            if (response == saveButton) {
                DatabaseManager inserter = new DBDataInsertion();

                String[] columns = new String[]{
                        "name", "email", "phone", "tcKimlik", "password",
                        "department", "userType", "accessLevel"
                };

                String userType = userTypeCombo.getValue();
                String accessLevel = "ADMIN".equals(userType) ? "ADMIN" : "STANDARD";

                Object[] values = new Object[]{
                        nameField.getText(),
                        emailField.getText(),
                        phoneField.getText(),
                        tcField.getText(),
                        passwordField.getText(),
                        departmentCombo.getValue(),
                        userType,
                        accessLevel
                };

                boolean success = inserter.insertData("Staff", columns, values);

                if (success) {
                    AlertManager.Alert(Alert.AlertType.INFORMATION,
                            "Personel başarıyla eklendi!", "Başarılı", "");
                    loadUsers();
                } else {
                    AlertManager.Alert(Alert.AlertType.ERROR,
                            "Personel eklenemedi!", "Hata", "");
                }
            }
        });
    }

    @FXML
    private void goToMainMenu() {
        try {
            SceneController.switchScene("/main.fxml", "Otel Yönetim Sistemi");
        } catch (Exception e) {
            AlertManager.Alert(Alert.AlertType.ERROR,
                    "Ana menüye geçiş yapılamadı!", "Hata", e.getMessage());
        }
    }
}
