package ymt_odev.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.text.Text;
import ymt_odev.ReservationState;
import ymt_odev.Services.CustomerService;
import ymt_odev.Services.ReservationService;
import ymt_odev.Services.RoomService;
import ymt_odev.Domain.Room;
import ymt_odev.Users.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class DashboardController extends BaseController {

    @FXML private Text welcomeMessage;
    @FXML private Text availableRoomsCount;
    @FXML private Text activeReservationsCount;
    @FXML private Text totalCustomersCount;
    @FXML private TableView<Object> recentActivitiesTable;
    @FXML private TableColumn<Object, String> timeColumn;
    @FXML private TableColumn<Object, String> actionColumn;
    @FXML private TableColumn<Object, String> descriptionColumn;
    @FXML private TableColumn<Object, String> statusColumn;
    @FXML private Text currentDateTime;

    @Override
    protected void initialize() {
        super.initialize();
        loadDashboardData();
    }

    private void loadDashboardData() {
        // Hoş geldiniz mesajı
        User currentUser = SessionManager.getUser();
        if (welcomeMessage != null && currentUser != null) {
            welcomeMessage.setText("Hoş Geldiniz, " + currentUser.getDisplayName() + "!");
        }

        // Tarih ve saat
        if (currentDateTime != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            currentDateTime.setText(LocalDateTime.now().format(formatter));
        }

        // Müsait oda sayısı
        if (availableRoomsCount != null) {
            List<Room> rooms = RoomService.getAllRooms();
            long availableCount = rooms.stream().filter(Room::isAvailable).count();
            availableRoomsCount.setText(String.valueOf(availableCount));
        }

        // Aktif rezervasyon sayısı
        if (activeReservationsCount != null) {
            activeReservationsCount.setText(String.valueOf(
                    ReservationService.getAllReservations().stream()
                            .filter(r -> ReservationState.CONFIRMED.toString().equals(r.getState()) ||
                                        ReservationState.CHECKED_IN.toString().equals(r.getState()))
                            .count()
            ));
        }

        // Toplam müşteri sayısı
        if (totalCustomersCount != null) {
            totalCustomersCount.setText(String.valueOf(CustomerService.getAllCustomers().size()));
        }
    }

    @FXML
    private void showNewReservation() {
        SceneController.switchScene("/room-search.fxml", "🏨 Yeni Rezervasyon");
    }

    @FXML
    private void switchToRoomSearch() {
        SceneController.switchScene("/room-search.fxml", "🏨 Oda Arama");
    }

    @FXML
    private void showCheckin() {
        SceneController.switchScene("/checkin-checkout.fxml", "🏨 Check-in");
    }

    @FXML
    private void showCheckout() {
        SceneController.switchScene("/checkin-checkout.fxml", "🏨 Check-out");
    }
}
