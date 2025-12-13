package ymt_odev.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import ymt_odev.AlertManager;
import ymt_odev.Domain.Reservation;
import ymt_odev.ReservationState;
import ymt_odev.RoomState;
import ymt_odev.Services.ReservationService;

import java.util.List;
import java.util.Objects;

public class ReservationsController extends BaseController {

    @FXML private TableView<Reservation> reservationsTable;
    @FXML private TableColumn<Reservation, String> reservationIdColumn;
    @FXML private TableColumn<Reservation, String> customerNameColumn;
    @FXML private TableColumn<Reservation, String> roomNumberColumn;
    @FXML private TableColumn<Reservation, String> checkInColumn;
    @FXML private TableColumn<Reservation, String> checkOutColumn;
    @FXML private TableColumn<Reservation, Integer> guestCountColumn;
    @FXML private TableColumn<Reservation, Double> totalPriceColumn;
    @FXML private TableColumn<Reservation, String> statusColumn;
    @FXML private TableColumn<Reservation, Void> actionsColumn;
    @FXML private javafx.scene.control.DatePicker filterStartDate;
    @FXML private javafx.scene.control.DatePicker filterEndDate;
    @FXML private TextField customerSearchField;
    @FXML private ComboBox<String> reservationStatusFilter;
    @FXML private javafx.scene.layout.VBox reservationDetailsPanel;
    @FXML private javafx.scene.text.Text detailReservationId;
    @FXML private javafx.scene.text.Text detailCustomerName;
    @FXML private javafx.scene.text.Text detailRoomInfo;
    @FXML private javafx.scene.text.Text detailDates;
    @FXML private javafx.scene.text.Text detailGuestCount;
    @FXML private javafx.scene.text.Text detailTotalPrice;
    @FXML private javafx.scene.text.Text detailDepositAmount;
    @FXML private javafx.scene.text.Text detailRemainingAmount;
    @FXML private javafx.scene.text.Text detailPaymentMethod;
    @FXML private javafx.scene.text.Text detailPaymentStatus;
    @FXML private javafx.scene.control.TextArea specialRequestsArea;
    @FXML private javafx.scene.control.Button checkInButton;
    @FXML private javafx.scene.control.Button checkOutButton;
    @FXML private javafx.scene.control.Button cancelButton;
    @FXML private javafx.scene.control.Button editButton;
    @FXML private javafx.scene.text.Text reservationCountText;
    @FXML private javafx.scene.text.Text selectedReservationText;

    private Reservation selectedReservation;

    @Override
    protected void initialize() {
        super.initialize();

        if (reservationIdColumn != null) {
            reservationIdColumn.setCellValueFactory(new PropertyValueFactory<>("confirmationCode"));
            customerNameColumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));
            roomNumberColumn.setCellValueFactory(new PropertyValueFactory<>("roomNumber"));
            checkInColumn.setCellValueFactory(new PropertyValueFactory<>("checkInDate"));
            checkOutColumn.setCellValueFactory(new PropertyValueFactory<>("checkOutDate"));
            guestCountColumn.setCellValueFactory(new PropertyValueFactory<>("guestCount"));
            totalPriceColumn.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));
            statusColumn.setCellValueFactory(new PropertyValueFactory<>("state"));

            // İşlemler sütunu
            if (actionsColumn != null) {
                actionsColumn.setCellFactory(param -> new TableCell<>() {
                    private final Button viewBtn = new Button("👁️");
                    private final Button checkInBtn = new Button("✅");
                    private final Button checkOutBtn = new Button("🚪");
                    private final HBox buttons = new HBox(5, viewBtn, checkInBtn, checkOutBtn);

                    {
                        viewBtn.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-cursor: hand; -fx-font-size: 10px;");
                        checkInBtn.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-cursor: hand; -fx-font-size: 10px;");
                        checkOutBtn.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-cursor: hand; -fx-font-size: 10px;");

                        viewBtn.setOnAction(e -> {
                            Reservation reservation = getTableView().getItems().get(getIndex());
                            selectReservation(reservation);
                        });

                        checkInBtn.setOnAction(e -> {
                            Reservation reservation = getTableView().getItems().get(getIndex());
                            doCheckIn(reservation);
                        });

                        checkOutBtn.setOnAction(e -> {
                            Reservation reservation = getTableView().getItems().get(getIndex());
                            doCheckOut(reservation);
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

        // Tablo seçim listener'ı
        if (reservationsTable != null) {
            reservationsTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
                if (newSelection != null) {
                    selectReservation(newSelection);
                }
            });
        }

        // Status filter için değerler ekle
        if (reservationStatusFilter != null) {
            reservationStatusFilter.getItems().addAll("Tüm Durumlar", "PENDING", "CONFIRMED", "CHECKED_IN", "CHECKED_OUT", "CANCELLED");
            reservationStatusFilter.setValue("Tüm Durumlar");
        }

        loadAllReservations();
    }

    private void selectReservation(Reservation reservation) {
        selectedReservation = reservation;
        showReservationDetails(reservation);
        selectedReservationText.setText("Seçili Rezervasyon: " + selectedReservation.getConfirmationCode() + " - " + selectedReservation.getState());
    }

    private void doCheckIn(Reservation reservation) {
        if (reservation == null) return;

        boolean success = ReservationService.checkIn(reservation.getId());

        if (success) {
            ymt_odev.Patterns.RoomStateManager.changeRoomState(reservation.getRoomId(), RoomState.OCCUPIED.toString());
            AlertManager.Alert(Alert.AlertType.INFORMATION, "Check-in işlemi tamamlandı!", "Başarılı", "");
            loadAllReservations();
        } else {
            AlertManager.Alert(Alert.AlertType.ERROR, "Check-in başarısız!", "Hata", "");
        }
    }

    private void doCheckOut(Reservation reservation) {
        if (reservation == null) return;

        boolean success = ReservationService.checkOut(reservation.getId());

        if (success) {
            ymt_odev.Patterns.RoomStateManager.changeRoomState(reservation.getRoomId(), RoomState.CLEANING.toString());
            AlertManager.Alert(Alert.AlertType.INFORMATION, "Check-out işlemi tamamlandı!", "Başarılı", "");
            loadAllReservations();
        } else {
            AlertManager.Alert(Alert.AlertType.ERROR, "Check-out başarısız!", "Hata", "");
        }
    }

    private void loadAllReservations() {
        List<Reservation> reservations = ReservationService.getAllReservations();

        if (reservationsTable != null) {
            reservationsTable.getItems().clear();
            reservationsTable.getItems().addAll(reservations);
        }
        reservationCountText.setText("Toplam: " + reservations.size() + " rezervasyon bulunmaktadır.");
    }

    @FXML
    private void searchReservations() {

    }

    @FXML
    private void filterByStatus() {
        // TODO: Durum filtreleme
        loadAllReservations();
    }

    @FXML
    private void viewDetails() {
        Reservation selected = reservationsTable != null ?
                reservationsTable.getSelectionModel().getSelectedItem() : null;

        if (selected == null) {
            AlertManager.Alert(
                    Alert.AlertType.WARNING,
                    "Lütfen bir rezervasyon seçin!",
                    "Seçim Yok",
                    ""
            );
            return;
        }

        // Detay görünümü göster
        showReservationDetails(selected);
    }

    private void showReservationDetails(Reservation reservation) {
        if (detailReservationId != null)
            detailReservationId.setText(reservation.getConfirmationCode());
        if (detailCustomerName != null)
            detailCustomerName.setText(reservation.getCustomerName());
        if (detailRoomInfo != null)
            detailRoomInfo.setText("Oda No: " + reservation.getRoomNumber());
        if (detailDates != null)
            detailDates.setText("Giriş: " + reservation.getCheckInDate() + "\nÇıkış: " + reservation.getCheckOutDate());
        if (detailGuestCount != null)
            detailGuestCount.setText(String.valueOf(reservation.getGuestCount()));
        if (detailTotalPrice != null)
            detailTotalPrice.setText(String.format("%.2f TL", reservation.getTotalPrice()));
        if (detailDepositAmount != null)
            detailDepositAmount.setText(String.format("%.2f TL", reservation.getDepositAmount()));
        if (detailRemainingAmount != null)
            detailRemainingAmount.setText(String.format("%.2f TL", reservation.getRemainingAmount()));
        if (detailPaymentMethod != null)
            detailPaymentMethod.setText(reservation.getPaymentMethod() != null ? reservation.getPaymentMethod() : "Belirtilmedi");
        if (detailPaymentStatus != null)
            detailPaymentStatus.setText(reservation.getPaymentStatus());
        if (specialRequestsArea != null)
            specialRequestsArea.setText(reservation.getSpecialRequests() != null ? reservation.getSpecialRequests() : "");

        if (reservationDetailsPanel != null)
            reservationDetailsPanel.setVisible(true);
    }

    @FXML
    private void confirmReservation() {
        Reservation selected = reservationsTable != null ?
                reservationsTable.getSelectionModel().getSelectedItem() : null;

        if (selected == null) {
            AlertManager.Alert(Alert.AlertType.WARNING, "Lütfen bir rezervasyon seçin!", "Seçim Yok", "");
            return;
        }

        boolean success = ReservationService.updateReservationState(selected.getId(), ReservationState.CONFIRMED.toString());

        if (success) {
            AlertManager.Alert(Alert.AlertType.INFORMATION, "Rezervasyon onaylandı!", "Başarılı", "");
            loadAllReservations();
        } else {
            AlertManager.Alert(Alert.AlertType.ERROR, "Onaylama başarısız!", "Hata", "");
        }
    }

    @FXML
    private void cancelReservation() {
        Reservation selected = selectedReservation != null ? selectedReservation :
                (reservationsTable != null ? reservationsTable.getSelectionModel().getSelectedItem() : null);

        if (selected == null) {
            AlertManager.Alert(Alert.AlertType.WARNING, "Lütfen bir rezervasyon seçin!", "Seçim Yok", "");
            return;
        }

        boolean success = ReservationService.cancelReservation(selected.getId());

        if (success) {
            AlertManager.Alert(Alert.AlertType.INFORMATION, "Rezervasyon iptal edildi!", "Başarılı", "");
            loadAllReservations();
        } else {
            AlertManager.Alert(Alert.AlertType.ERROR, "İptal başarısız!", "Hata", "");
        }
    }

    @FXML
    private void performCheckIn() {
        Reservation selected = selectedReservation != null ? selectedReservation :
                (reservationsTable != null ? reservationsTable.getSelectionModel().getSelectedItem() : null);

        if (selected == null) {
            AlertManager.Alert(Alert.AlertType.WARNING, "Lütfen bir rezervasyon seçin!", "Seçim Yok", "");
            return;
        }

        doCheckIn(selected);
    }

    @FXML
    private void performCheckOut() {
        Reservation selected = selectedReservation != null ? selectedReservation :
                (reservationsTable != null ? reservationsTable.getSelectionModel().getSelectedItem() : null);

        if (selected == null) {
            AlertManager.Alert(Alert.AlertType.WARNING, "Lütfen bir rezervasyon seçin!", "Seçim Yok", "");
            return;
        }

        doCheckOut(selected);
    }

    @FXML
    private void editReservation() {
        AlertManager.Alert(Alert.AlertType.INFORMATION,
                "Rezervasyon düzenleme özelliği yakında eklenecek!", "Bilgi", "");
    }

    @FXML
    private void clearFilters() {
        if (filterStartDate != null) filterStartDate.setValue(null);
        if (filterEndDate != null) filterEndDate.setValue(null);
        if (customerSearchField != null) customerSearchField.clear();
        if (reservationStatusFilter != null) reservationStatusFilter.setValue("Tüm Durumlar");
        loadAllReservations();
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

    @FXML
    private void showNewReservation() {
        SceneController.switchScene("/room-search.fxml", "🏨 Yeni Rezervasyon");
    }

    @FXML
    private void generateReport() {
        AlertManager.Alert(Alert.AlertType.INFORMATION,
                "Rapor oluşturma özelliği yakında eklenecek!", "Bilgi", "");
    }

    @FXML
    private void exportReservations() {
        AlertManager.Alert(Alert.AlertType.INFORMATION,
                "Dışa aktarma özelliği yakında eklenecek!", "Bilgi", "");
    }

    @FXML
    private void filterReservations() {
        String searchTerm = customerSearchField != null ? customerSearchField.getText() : "";
        String allStates = "Tüm Durumlar";
        List<Reservation> reservations;
        if (!searchTerm.isEmpty()){
            reservations = ReservationService.getReservationCustomerName(searchTerm);
        }else {
            reservations = ReservationService.getAllReservations();
        }
        if (reservationStatusFilter != null && !reservationStatusFilter.getValue().equalsIgnoreCase(allStates) && reservations != null) {
            reservations = reservations.stream().filter(r -> Objects.equals(r.getState(), reservationStatusFilter.getValue())).toList();
        }

        if (reservationsTable != null) {
            reservationsTable.getItems().clear();
            if (reservations != null) {
                reservationsTable.getItems().addAll(reservations);
            }
        }
    }
}
