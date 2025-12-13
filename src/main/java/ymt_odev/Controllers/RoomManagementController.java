package ymt_odev.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import ymt_odev.AlertManager;
import ymt_odev.Database.DBDataInsertion;
import ymt_odev.Database.DBDataUpdater;
import ymt_odev.Database.DBDataDeleter;
import ymt_odev.Database.DatabaseManager;
import ymt_odev.Domain.Room;
import ymt_odev.RoomState;
import ymt_odev.Services.RoomService;

import java.util.List;

public class RoomManagementController extends BaseController {

    @FXML private TableView<Room> roomResultsTable;
    @FXML private TableColumn<Room, String> roomNumberColumn;
    @FXML private TableColumn<Room, String> roomTypeColumn;
    @FXML private TableColumn<Room, Integer> capacityColumn;
    @FXML private TableColumn<Room, Double> priceColumn;
    @FXML private TableColumn<Room, String> featuresColumn;
    @FXML private TableColumn<Room, String> roomStatusColumn;
    @FXML private TableColumn<Room, Void> actionsColumn;
    @FXML private ComboBox<String> roomTypeCombo;
    @FXML private Text availableRoomsCount;
    @FXML private Text totalRoomsCount;
    @FXML private Text cleaningRoomsCount;
    @FXML private Text maintenanceRoomsCount;
    @FXML private ComboBox<String> roomStatusFilter;
    @FXML private TextField roomNumberSearchField;
    @FXML private ComboBox<String> featureFilter;

    @Override
    protected void initialize() {
        super.initialize();

        if (roomNumberColumn != null) {
            roomNumberColumn.setCellValueFactory(new PropertyValueFactory<>("roomNumber"));
            roomTypeColumn.setCellValueFactory(new PropertyValueFactory<>("roomType"));
            capacityColumn.setCellValueFactory(new PropertyValueFactory<>("capacity"));
            priceColumn.setCellValueFactory(new PropertyValueFactory<>("pricePerNight"));
            featuresColumn.setCellValueFactory(new PropertyValueFactory<>("features"));
            roomStatusColumn.setCellValueFactory(new PropertyValueFactory<>("state"));

            // İşlemler sütunu
            if (actionsColumn != null) {
                actionsColumn.setCellFactory(param -> new TableCell<>() {
                    private final Button editBtn = new Button("✏️");
                    private final Button deleteBtn = new Button("🗑️");
                    private final Button statusBtn = new Button("🔄");
                    private final HBox buttons = new HBox(5, editBtn, statusBtn, deleteBtn);

                    {
                        editBtn.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-cursor: hand; -fx-font-size: 10px;");
                        statusBtn.setStyle("-fx-background-color: #f39c12; -fx-text-fill: white; -fx-cursor: hand; -fx-font-size: 10px;");
                        deleteBtn.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-cursor: hand; -fx-font-size: 10px;");

                        editBtn.setOnAction(e -> {
                            Room room = getTableView().getItems().get(getIndex());
                            editRoom(room);
                        });

                        statusBtn.setOnAction(e -> {
                            Room room = getTableView().getItems().get(getIndex());
                            changeRoomStatus(room);
                        });

                        deleteBtn.setOnAction(e -> {
                            Room room = getTableView().getItems().get(getIndex());
                            deleteRoom(room);
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

        loadAllRooms();
    }

    private void editRoom(Room room) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Oda Düzenle");
        dialog.setHeaderText("Oda bilgilerini düzenleyin");

        ButtonType saveButton = new ButtonType("Kaydet", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButton, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        TextField priceField = new TextField(String.valueOf(room.getPricePerNight()));
        TextField featuresField = new TextField(room.getFeatures());
        TextField descriptionField = new TextField(room.getDescription());

        grid.add(new Label("Fiyat (₺/Gece):"), 0, 0);
        grid.add(priceField, 1, 0);
        grid.add(new Label("Özellikler:"), 0, 1);
        grid.add(featuresField, 1, 1);
        grid.add(new Label("Açıklama:"), 0, 2);
        grid.add(descriptionField, 1, 2);

        dialog.getDialogPane().setContent(grid);

        dialog.showAndWait().ifPresent(response -> {
            if (response == saveButton) {
                try {
                    DBDataUpdater updater = new DBDataUpdater();
                    String[] columns = new String[]{"pricePerNight", "features", "description"};
                    String[] values = new String[]{priceField.getText(), featuresField.getText(), descriptionField.getText()};
                    String[] whereClause = new String[]{"id"};
                    Object[] whereParams = new Object[]{room.getId()};

                    boolean success = updater.updateDataWithCondition("Rooms", columns, columns, values, whereClause, whereParams);

                    if (success) {
                        AlertManager.Alert(Alert.AlertType.INFORMATION, "Oda başarıyla güncellendi!", "Başarılı", "");
                        loadAllRooms();
                    } else {
                        AlertManager.Alert(Alert.AlertType.ERROR, "Oda güncellenemedi!", "Hata", "");
                    }
                } catch (Exception ex) {
                    AlertManager.Alert(Alert.AlertType.ERROR, "Hata: " + ex.getMessage(), "Hata", "");
                }
            }
        });
    }

    private void changeRoomStatus(Room room) {
        ChoiceDialog<String> dialog = new ChoiceDialog<>(room.getState(),
                RoomState.AVAILABLE.toString(),
                RoomState.OCCUPIED.toString(),
                RoomState.CLEANING.toString(),
                RoomState.MAINTENANCE.toString());
        dialog.setTitle("Oda Durumu Değiştir");
        dialog.setHeaderText("Yeni durum seçin");
        dialog.setContentText("Durum:");

        dialog.showAndWait().ifPresent(newStatus -> {
            ymt_odev.Patterns.RoomStateManager.changeRoomState(room.getId(), newStatus);
            AlertManager.Alert(Alert.AlertType.INFORMATION, "Oda durumu güncellendi!", "Başarılı", "");
            loadAllRooms();
        });
    }

    private void deleteRoom(Room room) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Silme Onayı");
        confirm.setHeaderText("Odayı silmek istediğinize emin misiniz?");
        confirm.setContentText("Bu işlem geri alınamaz!");

        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                DBDataDeleter deleter = new DBDataDeleter();
                boolean success = deleter.deleteData("Rooms", "id = ?", new Object[]{room.getId()});

                if (success) {
                    AlertManager.Alert(Alert.AlertType.INFORMATION, "Oda başarıyla silindi!", "Başarılı", "");
                    loadAllRooms();
                } else {
                    AlertManager.Alert(Alert.AlertType.ERROR, "Oda silinemedi!", "Hata", "");
                }
            }
        });
    }

    private void loadAllRooms() {
        List<Room> rooms = RoomService.getAllRooms();

        if (roomResultsTable != null) {
            roomResultsTable.getItems().clear();
            roomResultsTable.getItems().addAll(rooms);
        }

        // İstatistikleri veritabanından çekilen verilere göre güncelle
        updateRoomStatistics(rooms);
    }

    private void updateRoomStatistics(List<Room> rooms) {
        // Toplam oda sayısı
        if (totalRoomsCount != null) {
            totalRoomsCount.setText(String.valueOf(rooms.size()));
        }

        // Müsait oda sayısı
        if (availableRoomsCount != null) {
            long available = rooms.stream()
                    .filter(room -> RoomState.AVAILABLE.toString().equalsIgnoreCase(room.getState()))
                    .count();
            availableRoomsCount.setText(String.valueOf(available));
        }

        // Temizlikte olan oda sayısı
        if (cleaningRoomsCount != null) {
            long cleaning = rooms.stream()
                    .filter(room -> RoomState.CLEANING.toString().equalsIgnoreCase(room.getState()))
                    .count();
            cleaningRoomsCount.setText(String.valueOf(cleaning));
        }

        // Bakımda olan oda sayısı
        if (maintenanceRoomsCount != null) {
            long maintenance = rooms.stream()
                    .filter(room -> RoomState.MAINTENANCE.toString().equalsIgnoreCase(room.getState()))
                    .count();
            maintenanceRoomsCount.setText(String.valueOf(maintenance));
        }
    }

    @FXML
    private void addRoom() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Yeni Oda Ekle");
        dialog.setHeaderText("Oda bilgilerini girin");

        ButtonType saveButton = new ButtonType("Kaydet", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButton, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        TextField roomNumberField = new TextField();
        ComboBox<String> roomTypeCombo = new ComboBox<>();
        roomTypeCombo.getItems().addAll("Standart", "Deluxe", "Suite", "Aile", "Penthouse");
        TextField capacityField = new TextField();
        Label priceLabel = new Label("0.0 ₺");
        priceLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
        TextField featuresField = new TextField();
        TextField floorField = new TextField();
        CheckBox balconyCheck = new CheckBox();
        CheckBox seaViewCheck = new CheckBox();
        CheckBox kitchenCheck = new CheckBox();
        CheckBox jacuzziCheck = new CheckBox();

        // Fiyat otomatik hesaplama için config'i yükle
        ymt_odev.Utils.ConfigManager.PricingConfig pricingConfig = ymt_odev.Utils.ConfigManager.loadPricingConfig();

        // Fiyat hesaplama fonksiyonu
        Runnable updatePrice = () -> {
            String roomType = roomTypeCombo.getValue();
            if (roomType != null) {
                double price = pricingConfig.calculatePrice(
                    roomType,
                    seaViewCheck.isSelected(),
                    balconyCheck.isSelected(),
                    jacuzziCheck.isSelected(),
                    kitchenCheck.isSelected()
                );
                priceLabel.setText(String.format("%.2f ₺", price));
            }
        };

        // Listener'ları ekle
        roomTypeCombo.setOnAction(e -> updatePrice.run());
        balconyCheck.setOnAction(e -> updatePrice.run());
        seaViewCheck.setOnAction(e -> updatePrice.run());
        kitchenCheck.setOnAction(e -> updatePrice.run());
        jacuzziCheck.setOnAction(e -> updatePrice.run());

        grid.add(new Label("Oda No:"), 0, 0);
        grid.add(roomNumberField, 1, 0);
        grid.add(new Label("Oda Tipi:"), 0, 1);
        grid.add(roomTypeCombo, 1, 1);
        grid.add(new Label("Kapasite:"), 0, 2);
        grid.add(capacityField, 1, 2);
        grid.add(new Label("Fiyat (Otomatik):"), 0, 3);
        grid.add(priceLabel, 1, 3);
        grid.add(new Label("Özellikler:"), 0, 4);
        grid.add(featuresField, 1, 4);
        grid.add(new Label("Kat:"), 0, 5);
        grid.add(floorField, 1, 5);
        grid.add(new Label("Balkon:"), 0, 6);
        grid.add(balconyCheck, 1, 6);
        grid.add(new Label("Deniz Manzaralı:"), 0, 7);
        grid.add(seaViewCheck, 1, 7);
        grid.add(new Label("Mutfaklı:"), 0, 8);
        grid.add(kitchenCheck, 1, 8);
        grid.add(new Label("Jakuzili:"), 0, 9);
        grid.add(jacuzziCheck, 1, 9);

        dialog.getDialogPane().setContent(grid);

        dialog.showAndWait().ifPresent(response -> {
            if (response == saveButton) {
                try {
                    // Fiyatı hesapla
                    String roomType = roomTypeCombo.getValue();
                    if (roomType == null) {
                        AlertManager.Alert(Alert.AlertType.ERROR,
                                "Lütfen oda tipini seçin!", "Hata", "");
                        return;
                    }

                    double calculatedPrice = pricingConfig.calculatePrice(
                        roomType,
                        seaViewCheck.isSelected(),
                        balconyCheck.isSelected(),
                        jacuzziCheck.isSelected(),
                        kitchenCheck.isSelected()
                    );

                    DatabaseManager inserter = new DBDataInsertion();

                    String[] columns = new String[]{
                            "roomNumber", "roomType", "capacity", "pricePerNight",
                            "features", "floor", "hasBalcony", "hasSeaView", "hasKitchen", "state"
                    };

                    Object[] values = new Object[]{
                            roomNumberField.getText(),
                            roomType,
                            Integer.parseInt(capacityField.getText()),
                            calculatedPrice,
                            featuresField.getText(),
                            Integer.parseInt(floorField.getText()),
                            balconyCheck.isSelected(),
                            seaViewCheck.isSelected(),
                            kitchenCheck.isSelected(),
                            RoomState.AVAILABLE.toString()
                    };

                    boolean success = inserter.insertData("Rooms", columns, values);

                    if (success) {
                        AlertManager.Alert(Alert.AlertType.INFORMATION,
                                "Oda başarıyla eklendi!\nFiyat: " + String.format("%.2f ₺", calculatedPrice), "Başarılı", "");
                        loadAllRooms();
                    } else {
                        AlertManager.Alert(Alert.AlertType.ERROR,
                                "Oda eklenemedi!", "Hata", "");
                    }
                } catch (NumberFormatException e) {
                    AlertManager.Alert(Alert.AlertType.ERROR,
                            "Lütfen geçerli sayısal değerler girin!", "Hata", "");
                }
            }
        });
    }

    @FXML
    private void filterRooms() {
        String selectedType = roomTypeCombo != null ? roomTypeCombo.getValue() : null;
        String selectedStatus = roomStatusFilter != null ? roomStatusFilter.getValue() : null;
        String selectedFeature = featureFilter != null ? featureFilter.getValue() : null;

        Integer roomNumber = null;
        if (roomNumberSearchField != null && !roomNumberSearchField.getText().isEmpty()) {
            try {
                roomNumber = Integer.parseInt(roomNumberSearchField.getText());
            } catch (NumberFormatException e) {
                // Geçersiz sayı, null olarak kalacak
            }
        }

        List<Room> rooms = RoomService.searchRooms(roomNumber, selectedStatus, selectedFeature, selectedType);

        if (roomResultsTable != null) {
            roomResultsTable.getItems().clear();
            roomResultsTable.getItems().addAll(rooms);
        }
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
