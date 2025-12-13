package ymt_odev.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import ymt_odev.AlertManager;
import ymt_odev.Services.CustomerService;
import ymt_odev.Users.Customer;

import java.util.List;

public class CustomerManagementController extends BaseController {

    @FXML private TextField customerSearchField;
    @FXML private TableView<Customer> customersTable;
    @FXML private TableColumn<Customer, Integer> customerIdColumn;
    @FXML private TableColumn<Customer, String> customerNameColumn;
    @FXML private TableColumn<Customer, String> emailColumn;
    @FXML private TableColumn<Customer, String> phoneColumn;
    @FXML private TableColumn<Customer, String> tcColumn;
    @FXML private TableColumn<Customer, String> loyaltyColumn;
    @FXML private TableColumn<Customer, Integer> totalBookingsColumn;
    @FXML private TableColumn<Customer, Void> actionsColumn;
    @FXML private Text totalCustomersCount;
    @FXML private ComboBox<String> loyaltyBox;

    @Override
    protected void initialize() {
        super.initialize();

        if (customerIdColumn != null) {
            customerIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
            customerNameColumn.setCellValueFactory(data ->
                    new javafx.beans.property.SimpleStringProperty(data.getValue().getDisplayName()));
            emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
            phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
            tcColumn.setCellValueFactory(new PropertyValueFactory<>("nationalId"));
            loyaltyColumn.setCellValueFactory(new PropertyValueFactory<>("loyaltyLevel"));
            totalBookingsColumn.setCellValueFactory(new PropertyValueFactory<>("totalBookings"));

            // İşlemler sütunu
            if (actionsColumn != null) {
                actionsColumn.setCellFactory(param -> new TableCell<>() {
                    private final Button viewBtn = new Button("👁️");
                    private final Button editBtn = new Button("✏️");
                    private final javafx.scene.layout.HBox buttons = new javafx.scene.layout.HBox(5, viewBtn, editBtn);

                    {
                        viewBtn.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-cursor: hand;");
                        editBtn.setStyle("-fx-background-color: #f39c12; -fx-text-fill: white; -fx-cursor: hand;");

                        viewBtn.setOnAction(e -> {
                            Customer customer = getTableView().getItems().get(getIndex());
                            viewCustomerDetails(customer);
                        });

                        editBtn.setOnAction(e -> {
                            Customer customer = getTableView().getItems().get(getIndex());
                            editCustomer(customer);
                        });
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        setGraphic(empty ? null : buttons);
                    }
                });
            }
        }

        loadAllCustomers();
    }

    private void viewCustomerDetails(Customer customer) {
        AlertManager.Alert(Alert.AlertType.INFORMATION,
                "Müşteri Detayları",
                customer.getDisplayName(),
                "ID: " + customer.getId() + "\n" +
                "E-posta: " + customer.getEmail() + "\n" +
                "Telefon: " + customer.getPhone() + "\n" +
                "TC Kimlik: " + customer.getNationalId() + "\n" +
                "Sadakat: " + customer.getLoyaltyLevel() + "\n" +
                "Toplam Rezervasyon: " + customer.getTotalBookings());
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

        TextField firstNameField = new TextField(customer.getFirstName());
        TextField lastNameField = new TextField(customer.getLastName());
        TextField emailField = new TextField(customer.getEmail());
        TextField phoneField = new TextField(customer.getPhone());
        TextField tcField = new TextField(customer.getNationalId());

        grid.add(new Label("Ad:"), 0, 0);
        grid.add(firstNameField, 1, 0);
        grid.add(new Label("Soyad:"), 0, 1);
        grid.add(lastNameField, 1, 1);
        grid.add(new Label("E-posta:"), 0, 2);
        grid.add(emailField, 1, 2);
        grid.add(new Label("Telefon:"), 0, 3);
        grid.add(phoneField, 1, 3);
        grid.add(new Label("TC Kimlik:"), 0, 4);
        grid.add(tcField, 1, 4);

        dialog.getDialogPane().setContent(grid);

        dialog.showAndWait().ifPresent(response -> {
            if (response == saveButton) {
                boolean success = CustomerService.updateCustomer(
                        customer.getId(),
                        firstNameField.getText(),
                        lastNameField.getText(),
                        emailField.getText(),
                        phoneField.getText(),
                        tcField.getText()
                );

                if (success) {
                    AlertManager.Alert(Alert.AlertType.INFORMATION, "Müşteri başarıyla güncellendi!", "Başarılı", "");
                    loadAllCustomers();
                } else {
                    AlertManager.Alert(Alert.AlertType.ERROR, "Müşteri güncellenemedi!", "Hata", "");
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

    private void loadAllCustomers() {
        List<Customer> customers = CustomerService.getAllCustomers();

        if (customersTable != null) {
            customersTable.getItems().clear();
            customersTable.getItems().addAll(customers);
        }

        if (totalCustomersCount != null) {
            totalCustomersCount.setText(String.valueOf(customers.size()));
        }
    }

    @FXML
    private void searchCustomers() {
        String searchTerm = customerSearchField != null ? customerSearchField.getText() : "";
        List<Customer> customers;
        if (searchTerm.isEmpty()) {
            customers = CustomerService.getAllCustomers();
        }else {
            customers = CustomerService.searchCustomers(searchTerm);
        }
        if (loyaltyBox != null && loyaltyBox.getValue() != null) {
            String selectedLoyalty = loyaltyBox.getValue();
            if (!selectedLoyalty.equals("Tümü")) {
                customers = customers.stream()
                        .filter(c -> c.getLoyaltyLevel().equals(selectedLoyalty))
                        .toList();
            }
        }

        if (customersTable != null) {
            customersTable.getItems().clear();
            customersTable.getItems().addAll(customers);
        }

        if (customers.isEmpty()) {
            AlertManager.Alert(
                    Alert.AlertType.INFORMATION,
                    "Müşteri bulunamadı!",
                    "Sonuç Yok",
                    ""
            );
        }
    }

    @FXML
    private void addCustomer() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Yeni Müşteri Ekle");
        dialog.setHeaderText("Müşteri bilgilerini girin");

        ButtonType saveButton = new ButtonType("Kaydet", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButton, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        TextField firstNameField = new TextField();
        TextField lastNameField = new TextField();
        TextField emailField = new TextField();
        TextField phoneField = new TextField();
        TextField tcField = new TextField();
        PasswordField passwordField = new PasswordField();

        grid.add(new Label("Ad:"), 0, 0);
        grid.add(firstNameField, 1, 0);
        grid.add(new Label("Soyad:"), 0, 1);
        grid.add(lastNameField, 1, 1);
        grid.add(new Label("E-posta:"), 0, 2);
        grid.add(emailField, 1, 2);
        grid.add(new Label("Telefon:"), 0, 3);
        grid.add(phoneField, 1, 3);
        grid.add(new Label("TC Kimlik:"), 0, 4);
        grid.add(tcField, 1, 4);
        grid.add(new Label("Şifre:"), 0, 5);
        grid.add(passwordField, 1, 5);

        dialog.getDialogPane().setContent(grid);

        dialog.showAndWait().ifPresent(response -> {
            if (response == saveButton) {
                boolean success = CustomerService.registerCustomer(
                        firstNameField.getText(),
                        lastNameField.getText(),
                        emailField.getText(),
                        phoneField.getText(),
                        tcField.getText(),
                        passwordField.getText()
                );

                if (success) {
                    AlertManager.Alert(Alert.AlertType.INFORMATION, "Müşteri başarıyla eklendi!", "Başarılı", "");
                    loadAllCustomers();
                } else {
                    AlertManager.Alert(Alert.AlertType.ERROR, "Müşteri eklenemedi!", "Hata", "");
                }
            }
        });
    }
}
