package ymt_odev.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import ymt_odev.AlertManager;
import ymt_odev.Domain.Reservation;
import ymt_odev.RoomState;
import ymt_odev.Services.ReservationService;
import ymt_odev.Patterns.RoomStateManager;

public class CheckinCheckoutController extends BaseController {

    @FXML private TextField customerSearchField;
    @FXML private Button checkInButton;
    @FXML private Button checkOutButton;

    // TableColumn'lar
    @FXML private TableColumn<Reservation, String> reservationIdColumn;
    @FXML private TableColumn<Reservation, String> customerNameColumn;
    @FXML private TableColumn<Reservation, String> statusColumn;
    @FXML private TableColumn<Reservation, Double> totalPriceColumn;
    @FXML private TableColumn<Reservation, String> checkInColumn;
    @FXML private TableColumn<Reservation, String> checkOutColumn;

    private Reservation currentReservation;

    @FXML
    private void searchReservation() {
        String confirmationCode = customerSearchField != null ?
                customerSearchField.getText().trim() : "";

        if (confirmationCode.isEmpty()) {
            AlertManager.Alert(Alert.AlertType.WARNING,
                    "Lütfen onay kodunu girin!", "Uyarı", "");
            return;
        }

        currentReservation = ReservationService.getReservationByConfirmationCode(confirmationCode);

        if (currentReservation == null) {
            AlertManager.Alert(Alert.AlertType.ERROR,
                    "Rezervasyon bulunamadı!", "Hata", "");
            clearReservationDetails();
            return;
        }

        displayReservationDetails();
    }

    private void displayReservationDetails() {
        if (currentReservation == null) return;

        // Buton durumlarını ayarla
        if (checkInButton != null && checkOutButton != null) {
            checkInButton.setDisable(!currentReservation.isConfirmed());
            checkOutButton.setDisable(!currentReservation.isCheckedIn());
        }

        // Rezervasyon bilgilerini göster
        AlertManager.Alert(Alert.AlertType.INFORMATION,
                "Rezervasyon Bulundu!\n\n",
                "Rezervasyon Bilgileri",
                "Onay Kodu: " + currentReservation.getConfirmationCode() + "\n" +
                        "Müşteri ID: " + currentReservation.getCustomerId() + "\n" +
                        "Oda ID: " + currentReservation.getRoomId() + "\n" +
                        "Giriş: " + currentReservation.getCheckInDate() + "\n" +
                        "Çıkış: " + currentReservation.getCheckOutDate() + "\n" +
                        "Durum: " + currentReservation.getState());
    }

    private void clearReservationDetails() {
        currentReservation = null;


        if (checkInButton != null) checkInButton.setDisable(true);
        if (checkOutButton != null) checkOutButton.setDisable(true);
    }

    @FXML
    private void performCheckIn() {
        if (currentReservation == null) {
            AlertManager.Alert(Alert.AlertType.WARNING,
                    "Lütfen önce rezervasyon arayın!", "Uyarı", "");
            return;
        }

        boolean success = ReservationService.checkIn(currentReservation.getId());

        if (success) {
            // Oda durumunu güncelle
            RoomStateManager.changeRoomState(currentReservation.getRoomId(), "OCCUPIED");

            AlertManager.Alert(Alert.AlertType.INFORMATION,
                    "Check-in işlemi başarılı!", "Başarılı", "");

            // Rezervasyonu yeniden yükle
            searchReservation();
        } else {
            AlertManager.Alert(Alert.AlertType.ERROR,
                    "Check-in işlemi başarısız!", "Hata", "");
        }
    }

    @FXML
    private void performCheckOut() {
        if (currentReservation == null) {
            AlertManager.Alert(Alert.AlertType.WARNING,
                    "Lütfen önce rezervasyon arayın!", "Uyarı", "");
            return;
        }

        boolean success = ReservationService.checkOut(currentReservation.getId());

        if (success) {
            // Oda durumunu güncelle
            RoomStateManager.changeRoomState(currentReservation.getRoomId(), RoomState.CLEANING.toString());

            AlertManager.Alert(Alert.AlertType.INFORMATION,
                    "Check-out işlemi başarılı!", "Başarılı", "");

            // Detayları temizle
            clearReservationDetails();
            if (customerSearchField != null) {
                customerSearchField.clear();
            }
        } else {
            AlertManager.Alert(Alert.AlertType.ERROR,
                    "Check-out işlemi başarısız!", "Hata", "");
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
