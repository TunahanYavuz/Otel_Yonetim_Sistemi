package ymt_odev.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import ymt_odev.AlertManager;
import ymt_odev.Domain.Room;
import ymt_odev.Services.ReservationService;
import ymt_odev.Services.RoomService;

import java.time.LocalDate;
import java.util.List;

public class RoomSearchController extends BaseController {

    @FXML private DatePicker checkInDatePicker;
    @FXML private DatePicker checkOutDatePicker;
    @FXML private javafx.scene.control.Spinner<Integer> guestCountSpinner;
    @FXML private ComboBox<String> roomTypeCombo;
    @FXML private TableView<Room> roomResultsTable;
    @FXML private TableColumn<Room, String> roomNumberColumn;
    @FXML private TableColumn<Room, String> roomTypeColumn;
    @FXML private TableColumn<Room, Integer> capacityColumn;
    @FXML private TableColumn<Room, Double> priceColumn;
    @FXML private TableColumn<Room, String> featuresColumn;
    @FXML private TableColumn<Room, String> statusColumn;
    @FXML private TableColumn<Room, Void> actionsColumn;
    @FXML private javafx.scene.control.CheckBox balconyCheck;
    @FXML private javafx.scene.control.CheckBox seaViewCheck;
    @FXML private javafx.scene.control.CheckBox jacuzziCheck;
    @FXML private javafx.scene.control.CheckBox kitchenCheck;
    @FXML private javafx.scene.layout.VBox searchResultsSection;
    @FXML private javafx.scene.layout.VBox selectedRoomDetails;
    @FXML private javafx.scene.text.Text selectedRoomInfo;
    @FXML private javafx.scene.text.Text selectedRoomPrice;
    @FXML private javafx.scene.text.Text selectedRoomFeatures;
    @FXML private javafx.scene.text.Text totalPriceText;
    @FXML private ComboBox<String> paymentMethodCombo;
    @FXML private javafx.scene.control.TextArea specialRequestsArea;

    private Room selectedRoom;

    @Override
    protected void initialize() {
        super.initialize();

        // Oda tipi seçeneklerini ekle
        if (roomTypeCombo != null) {
            roomTypeCombo.getItems().addAll("Tümü", "Standart", "Deluxe", "Suite", "Aile", "Penthouse");
            roomTypeCombo.setValue("Tümü");
        }

        // TableView kolonlarını ayarla
        if (roomNumberColumn != null) {
            roomNumberColumn.setCellValueFactory(new PropertyValueFactory<>("roomNumber"));
            roomTypeColumn.setCellValueFactory(new PropertyValueFactory<>("roomType"));
            capacityColumn.setCellValueFactory(new PropertyValueFactory<>("capacity"));
            priceColumn.setCellValueFactory(new PropertyValueFactory<>("pricePerNight"));
            featuresColumn.setCellValueFactory(new PropertyValueFactory<>("features"));

            if (statusColumn != null) {
                statusColumn.setCellValueFactory(new PropertyValueFactory<>("state"));
            }

            // İşlemler sütunu
            if (actionsColumn != null) {
                actionsColumn.setCellFactory(param -> new TableCell<>() {
                    private final Button selectBtn = new Button("Seç");
                    private final Button detailBtn = new Button("Detay");
                    private final HBox buttons = new HBox(5, selectBtn, detailBtn);

                    {
                        selectBtn.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-cursor: hand; -fx-font-size: 11px;");
                        detailBtn.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-cursor: hand; -fx-font-size: 11px;");

                        selectBtn.setOnAction(e -> {
                            Room room = getTableView().getItems().get(getIndex());
                            selectRoom(room);
                        });

                        detailBtn.setOnAction(e -> {
                            Room room = getTableView().getItems().get(getIndex());
                            showRoomDetails(room);
                        });
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        setGraphic(empty ? null : buttons);
                    }
                });
            }

            // Tablo seçim listener'ı
            roomResultsTable.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
                if (newVal != null) {
                    selectRoom(newVal);
                }
            });
        }
    }

    private void selectRoom(Room room) {
        selectedRoom = room;

        if (selectedRoomDetails != null) {
            selectedRoomDetails.setVisible(true);
        }

        if (selectedRoomInfo != null) {
            selectedRoomInfo.setText("Oda: " + room.getRoomNumber() + " - " + room.getRoomType() +
                    " (" + room.getCapacity() + " kişilik)");
        }

        if (selectedRoomPrice != null) {
            selectedRoomPrice.setText("Fiyat: " + room.getPricePerNight() + " TL/gece");
        }

        if (selectedRoomFeatures != null) {
            selectedRoomFeatures.setText("Özellikler: " + room.getFeatures());
        }

        // Toplam fiyat hesapla
        updateTotalPrice();
    }

    private void updateTotalPrice() {
        if (totalPriceText != null && selectedRoom != null &&
            checkInDatePicker.getValue() != null && checkOutDatePicker.getValue() != null) {

            long nights = java.time.temporal.ChronoUnit.DAYS.between(
                    checkInDatePicker.getValue(), checkOutDatePicker.getValue());
            double total = nights * selectedRoom.getPricePerNight();
            totalPriceText.setText("Toplam: " + total + " TL (" + nights + " gece)");
        }
    }

    private void showRoomDetails(Room room) {
        String details = "Oda No: " + room.getRoomNumber();

        AlertManager.Alert(Alert.AlertType.INFORMATION, details, "Oda Detayları - " + room.getRoomNumber(),
                "Tip: " + room.getRoomType() + "\n" +
                        "Kapasite: " + room.getCapacity() + " kişi\n" +
                        "Fiyat: " + room.getPricePerNight() + " TL/gece\n" +
                        "Kat: " + room.getFloor() + "\n" +
                        "Özellikler: " + room.getFeatures() + "\n" +
                        "Balkon: " + (room.hasBalcony() ? "Var" : "Yok") + "\n" +
                        "Deniz Manzarası: " + (room.hasSeaView() ? "Var" : "Yok") + "\n" +
                        "Mutfak: " + (room.hasKitchen() ? "Var" : "Yok") + "\n" +
                        "Evcil Hayvan: " + (room.isPetFriendly() ? "Kabul" : "Kabul Edilmez") + "\n" +
                        "Açıklama: " + room.getDescription());
    }

    @FXML
    private void searchRooms() {
        LocalDate checkIn = checkInDatePicker != null ? checkInDatePicker.getValue() : null;
        LocalDate checkOut = checkOutDatePicker != null ? checkOutDatePicker.getValue() : null;

        if (checkIn == null || checkOut == null) {
            AlertManager.Alert(
                    Alert.AlertType.WARNING,
                    "Lütfen giriş ve çıkış tarihlerini seçin!",
                    "Eksik Bilgi",
                    ""
            );
            return;
        }

        if (checkOut.isBefore(checkIn) || checkOut.isEqual(checkIn)) {
            AlertManager.Alert(
                    Alert.AlertType.ERROR,
                    "Çıkış tarihi giriş tarihinden sonra olmalıdır!",
                    "Geçersiz Tarih",
                    ""
            );
            return;
        }

        Integer guestCount = null;
        if (guestCountSpinner != null) {
            guestCount = guestCountSpinner.getValue();
        }

        String roomType = roomTypeCombo != null && roomTypeCombo.getValue() != null
                ? roomTypeCombo.getValue() : null;
        if ("Tümü".equals(roomType)) {
            roomType = null;
        }

        List<Room> rooms = RoomService.searchAvailableRooms(checkIn, checkOut, guestCount, roomType);

        // Checkbox filtreleri uygula
        if (balconyCheck != null && balconyCheck.isSelected()) {
            rooms = rooms.stream().filter(Room::hasBalcony).toList();
        }
        if (seaViewCheck != null && seaViewCheck.isSelected()) {
            rooms = rooms.stream().filter(Room::hasSeaView).toList();
        }
        if (jacuzziCheck != null && jacuzziCheck.isSelected()) {
            rooms = rooms.stream().filter(Room::hasJacuzzi).toList();
        }
        if (kitchenCheck != null && kitchenCheck.isSelected()) {
            rooms = rooms.stream().filter(Room::hasKitchen).toList();
        }

        if (roomResultsTable != null) {
            roomResultsTable.getItems().clear();
            roomResultsTable.getItems().addAll(rooms);
        }

        // Sonuçlar bölümünü göster
        if (searchResultsSection != null) {
            searchResultsSection.setVisible(true);
        }

        // Seçili oda detaylarını gizle
        if (selectedRoomDetails != null) {
            selectedRoomDetails.setVisible(false);
        }
        selectedRoom = null;

        if (rooms.isEmpty()) {
            AlertManager.Alert(
                    Alert.AlertType.INFORMATION,
                    "Seçtiğiniz kriterlere uygun oda bulunamadı!",
                    "Sonuç Yok",
                    ""
            );
        }
    }

    @FXML
    private void clearSearchForm() {
        if (checkInDatePicker != null) checkInDatePicker.setValue(null);
        if (checkOutDatePicker != null) checkOutDatePicker.setValue(null);
        if (guestCountSpinner != null) guestCountSpinner.getValueFactory().setValue(2);
        if (roomTypeCombo != null) roomTypeCombo.setValue("Tümü");
        if (balconyCheck != null) balconyCheck.setSelected(false);
        if (seaViewCheck != null) seaViewCheck.setSelected(false);
        if (jacuzziCheck != null) jacuzziCheck.setSelected(false);
        if (kitchenCheck != null) kitchenCheck.setSelected(false);
        if (roomResultsTable != null) roomResultsTable.getItems().clear();
        if (searchResultsSection != null) searchResultsSection.setVisible(false);
        if (paymentMethodCombo != null) paymentMethodCombo.setValue(null);
        if (specialRequestsArea != null) specialRequestsArea.clear();
        clearSelection();
    }

    @FXML
    private void clearSelection() {
        if (selectedRoomDetails != null) selectedRoomDetails.setVisible(false);
        if (paymentMethodCombo != null) paymentMethodCombo.setValue(null);
        if (specialRequestsArea != null) specialRequestsArea.clear();
        selectedRoom = null;
    }

    @FXML
    private void makeReservation() {
        // Önce selectedRoom'u kontrol et, sonra tablodan seçili olanı
        Room roomToBook = selectedRoom;
        if (roomToBook == null && roomResultsTable != null) {
            roomToBook = roomResultsTable.getSelectionModel().getSelectedItem();
        }

        if (roomToBook == null) {
            AlertManager.Alert(
                    Alert.AlertType.WARNING,
                    "Lütfen bir oda seçin!",
                    "Oda Seçilmedi",
                    ""
            );
            return;
        }

        LocalDate checkIn = checkInDatePicker != null ? checkInDatePicker.getValue() : null;
        LocalDate checkOut = checkOutDatePicker != null ? checkOutDatePicker.getValue() : null;

        if (checkIn == null || checkOut == null) {
            AlertManager.Alert(
                    Alert.AlertType.WARNING,
                    "Lütfen giriş ve çıkış tarihlerini seçin!",
                    "Eksik Bilgi",
                    ""
            );
            return;
        }

        // Ödeme yöntemi kontrolü
        String paymentMethod = paymentMethodCombo != null ? paymentMethodCombo.getValue() : null;
        if (paymentMethod == null || paymentMethod.isEmpty()) {
            AlertManager.Alert(
                    Alert.AlertType.WARNING,
                    "Lütfen ödeme yöntemi seçin!",
                    "Eksik Bilgi",
                    ""
            );
            return;
        }

        // Özel istekler / notlar
        String specialRequests = specialRequestsArea != null ? specialRequestsArea.getText() : "";

        Integer guestCount = guestCountSpinner != null ? guestCountSpinner.getValue() : 1;

        long nights = java.time.temporal.ChronoUnit.DAYS.between(checkIn, checkOut);
        double totalPrice = nights * roomToBook.getPricePerNight();

        String confirmationCode = ReservationService.createReservation(
                SessionManager.getUser().getId(),
                roomToBook.getId(),
                checkIn,
                checkOut,
                guestCount,
                totalPrice,
                specialRequests,
                paymentMethod
        );

        if (confirmationCode != null) {
            AlertManager.Alert(
                    Alert.AlertType.INFORMATION,
                    "Rezervasyonunuz oluşturuldu!\n\n",
                    "Rezervasyon Başarılı",

                    "Onay Kodu: " + confirmationCode + "\n" +
                            "Oda: " + roomToBook.getRoomNumber() + "\n" +
                            "Ödeme Yöntemi: " + paymentMethod + "\n" +
                            "Toplam Tutar: " + totalPrice + " TL"
            );
            clearSearchForm();
        } else {
            AlertManager.Alert(
                    Alert.AlertType.ERROR,
                    "Rezervasyon oluşturulurken bir hata oluştu!",
                    "Hata",
                    ""
            );
        }
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
