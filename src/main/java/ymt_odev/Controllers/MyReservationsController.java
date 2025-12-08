package ymt_odev.Controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import ymt_odev.AlertManager;
import ymt_odev.Domain.Reservation;
import ymt_odev.Services.ReservationService;
import ymt_odev.Users.Customer;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class MyReservationsController extends BaseController {

    @FXML private ComboBox<String> reservationStatusFilter;
    @FXML private TableView<Reservation> reservationsTable;
    @FXML private TableColumn<Reservation, String> reservationIdColumn;
    @FXML private TableColumn<Reservation, String> roomColumn;
    @FXML private TableColumn<Reservation, String> checkInColumn;
    @FXML private TableColumn<Reservation, String> checkOutColumn;
    @FXML private TableColumn<Reservation, Integer> guestCountColumn;
    @FXML private TableColumn<Reservation, Double> totalPriceColumn;
    @FXML private TableColumn<Reservation, String> statusColumn;
    @FXML private TableColumn<Reservation, Void> actionsColumn;

    @FXML private DatePicker filterStartDate;
    @FXML private DatePicker filterEndDate;
    @FXML private Text reservationCountText;

    // Detay paneli
    @FXML private VBox reservationDetailsPanel;
    @FXML private Text detailReservationId;
    @FXML private Text detailRoomInfo;
    @FXML private Text detailDates;
    @FXML private Text detailGuestCount;
    @FXML private Text detailTotalPrice;
    @FXML private Text detailPaymentMethod;
    @FXML private Text detailPaymentStatus;
    @FXML private Text detailStatus;
    @FXML private TextArea specialRequestsArea;
    @FXML private Button cancelButton;

    private List<Reservation> allReservations;
    private Reservation selectedReservation;

    @Override
    protected void initialize() {
        super.initialize();

        // Durum filtresi varsayılan değer
        if (reservationStatusFilter != null) {
            reservationStatusFilter.setValue("Tümü");
        }

        // TableView kolonlarını ayarla
        if (reservationIdColumn != null) {
            reservationIdColumn.setCellValueFactory(new PropertyValueFactory<>("confirmationCode"));

            roomColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty("Oda " + cellData.getValue().getRoomId()));

            checkInColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getCheckInDate().toString()));

            checkOutColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getCheckOutDate().toString()));

            guestCountColumn.setCellValueFactory(new PropertyValueFactory<>("guestCount"));
            totalPriceColumn.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));

            statusColumn.setCellValueFactory(cellData -> {
                String state = cellData.getValue().getState();
                String displayState = switch (state) {
                    case "PENDING" -> "Beklemede";
                    case "CONFIRMED" -> "Onaylandı";
                    case "CHECKED_IN" -> "Giriş Yapıldı";
                    case "CHECKED_OUT" -> "Tamamlandı";
                    case "CANCELLED" -> "İptal";
                    default -> state;
                };
                return new SimpleStringProperty(displayState);
            });

            // İşlemler sütunu
            if (actionsColumn != null) {
                actionsColumn.setCellFactory(param -> new TableCell<>() {
                    private final Button viewBtn = new Button("👁️");
                    private final Button cancelBtn = new Button("❌");
                    private final HBox buttons = new HBox(5, viewBtn, cancelBtn);

                    {
                        viewBtn.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-cursor: hand; -fx-font-size: 10px;");
                        cancelBtn.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-cursor: hand; -fx-font-size: 10px;");

                        viewBtn.setOnAction(e -> {
                            Reservation reservation = getTableView().getItems().get(getIndex());
                            showReservationDetails(reservation);
                        });

                        cancelBtn.setOnAction(e -> {
                            Reservation reservation = getTableView().getItems().get(getIndex());
                            cancelSelectedReservation(reservation);
                        });
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            Reservation reservation = getTableView().getItems().get(getIndex());
                            // İptal edilmiş veya tamamlanmış rezervasyonlar için iptal butonunu gizle
                            cancelBtn.setVisible(!reservation.isCancelled() && !reservation.isCheckedOut());
                            setGraphic(buttons);
                        }
                    }
                });
            }
        }

        // Tablo seçim listener'ı
        if (reservationsTable != null) {
            reservationsTable.getSelectionModel().selectedItemProperty().addListener(
                    (obs, oldSelection, newSelection) -> {
                        if (newSelection != null) {
                            showReservationDetails(newSelection);
                        }
                    }
            );
        }

        loadReservations();
    }

    private void loadReservations() {
        Customer customer = SessionManager.getCustomer();
        if (customer == null) {
            System.err.println("Müşteri oturumu bulunamadı!");
            return;
        }

        allReservations = ReservationService.getCustomerReservations(customer.getId());

        if (reservationsTable != null) {
            reservationsTable.getItems().clear();
            reservationsTable.getItems().addAll(allReservations);
        }

        updateReservationCount(allReservations.size());
    }

    private void updateReservationCount(int count) {
        if (reservationCountText != null) {
            reservationCountText.setText(count + " rezervasyon");
        }
    }

    private void showReservationDetails(Reservation reservation) {
        selectedReservation = reservation;

        if (reservationDetailsPanel != null) {
            reservationDetailsPanel.setVisible(true);
        }

        if (detailReservationId != null) {
            detailReservationId.setText(reservation.getConfirmationCode());
        }
        if (detailRoomInfo != null) {
            detailRoomInfo.setText("Oda " + reservation.getRoomId());
        }
        if (detailDates != null) {
            detailDates.setText(reservation.getCheckInDate() + " - " + reservation.getCheckOutDate());
        }
        if (detailGuestCount != null) {
            detailGuestCount.setText(String.valueOf(reservation.getGuestCount()) + " kişi");
        }
        if (detailTotalPrice != null) {
            detailTotalPrice.setText(String.format("%.2f TL", reservation.getTotalPrice()));
        }
        if (detailPaymentMethod != null) {
            String method = reservation.getPaymentMethod();
            detailPaymentMethod.setText(method != null && !method.isEmpty() ? method : "Belirtilmedi");
        }
        if (detailPaymentStatus != null) {
            detailPaymentStatus.setText(reservation.isPaid() ? "✅ Ödendi" : "⏳ Ödenmedi");
        }
        if (detailStatus != null) {
            String state = reservation.getState();
            String displayState = switch (state) {
                case "PENDING" -> "⏳ Beklemede";
                case "CONFIRMED" -> "✅ Onaylandı";
                case "CHECKED_IN" -> "🏨 Giriş Yapıldı";
                case "CHECKED_OUT" -> "✔️ Tamamlandı";
                case "CANCELLED" -> "❌ İptal Edildi";
                default -> state;
            };
            detailStatus.setText(displayState);
        }
        if (specialRequestsArea != null) {
            String requests = reservation.getSpecialRequests();
            specialRequestsArea.setText(requests != null && !requests.isEmpty() ? requests : "Özel istek belirtilmedi.");
        }

        // İptal butonu görünürlüğü
        if (cancelButton != null) {
            cancelButton.setVisible(!reservation.isCancelled() && !reservation.isCheckedOut());
        }
    }

    @FXML
    private void filterReservations() {
        if (allReservations == null) return;

        List<Reservation> filtered = allReservations;

        // Durum filtresi
        String statusFilter = reservationStatusFilter != null ? reservationStatusFilter.getValue() : "Tümü";
        if (statusFilter != null && !"Tümü".equals(statusFilter)) {
            String stateCode = switch (statusFilter) {
                case "Beklemede" -> "PENDING";
                case "Onaylandı" -> "CONFIRMED";
                case "Giriş Yapıldı" -> "CHECKED_IN";
                case "Tamamlandı" -> "CHECKED_OUT";
                case "İptal Edildi" -> "CANCELLED";
                default -> null;
            };
            if (stateCode != null) {
                filtered = filtered.stream()
                        .filter(r -> stateCode.equals(r.getState()))
                        .collect(Collectors.toList());
            }
        }

        // Tarih filtresi
        LocalDate startDate = filterStartDate != null ? filterStartDate.getValue() : null;
        LocalDate endDate = filterEndDate != null ? filterEndDate.getValue() : null;

        if (startDate != null) {
            filtered = filtered.stream()
                    .filter(r -> !r.getCheckInDate().isBefore(startDate))
                    .collect(Collectors.toList());
        }

        if (endDate != null) {
            filtered = filtered.stream()
                    .filter(r -> !r.getCheckOutDate().isAfter(endDate))
                    .collect(Collectors.toList());
        }

        if (reservationsTable != null) {
            reservationsTable.getItems().clear();
            reservationsTable.getItems().addAll(filtered);
        }

        updateReservationCount(filtered.size());
    }

    @FXML
    private void clearFilters() {
        if (reservationStatusFilter != null) reservationStatusFilter.setValue("Tümü");
        if (filterStartDate != null) filterStartDate.setValue(null);
        if (filterEndDate != null) filterEndDate.setValue(null);

        if (reservationsTable != null && allReservations != null) {
            reservationsTable.getItems().clear();
            reservationsTable.getItems().addAll(allReservations);
            updateReservationCount(allReservations.size());
        }

        if (reservationDetailsPanel != null) {
            reservationDetailsPanel.setVisible(false);
        }
        selectedReservation = null;
    }

    @FXML
    private void cancelReservation() {
        if (selectedReservation == null) {
            AlertManager.Alert(
                    Alert.AlertType.WARNING,
                    "Lütfen iptal etmek istediğiniz rezervasyonu seçin!",
                    "Seçim Yok",
                    ""
            );
            return;
        }
        cancelSelectedReservation(selectedReservation);
    }

    private void cancelSelectedReservation(Reservation reservation) {
        if (reservation.isCancelled() || reservation.isCheckedOut()) {
            AlertManager.Alert(
                    Alert.AlertType.WARNING,
                    "Bu rezervasyon zaten iptal edilmiş veya tamamlanmış!",
                    "İşlem Yapılamaz",
                    ""
            );
            return;
        }

        // Onay dialogu
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Rezervasyon İptali");
        confirm.setHeaderText("Rezervasyonu iptal etmek istediğinize emin misiniz?");
        confirm.setContentText("Onay Kodu: " + reservation.getConfirmationCode());

        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                boolean success = ReservationService.cancelReservation(reservation.getId());

                if (success) {
                    AlertManager.Alert(
                            Alert.AlertType.INFORMATION,
                            "Rezervasyonunuz başarıyla iptal edildi.",
                            "İptal Başarılı",
                            ""
                    );
                    loadReservations();
                    if (reservationDetailsPanel != null) {
                        reservationDetailsPanel.setVisible(false);
                    }
                    selectedReservation = null;
                } else {
                    AlertManager.Alert(
                            Alert.AlertType.ERROR,
                            "Rezervasyon iptal edilirken bir hata oluştu!",
                            "Hata",
                            ""
                    );
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
