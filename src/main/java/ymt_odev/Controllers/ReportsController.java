package ymt_odev.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import ymt_odev.AlertManager;
import ymt_odev.Services.CustomerService;
import ymt_odev.Services.ReservationService;
import ymt_odev.Services.RoomService;
import ymt_odev.Domain.Reservation;
import ymt_odev.Domain.Room;

import java.time.LocalDate;
import java.util.List;

public class ReportsController extends BaseController {

    @FXML private DatePicker startDatePicker;
    @FXML private DatePicker endDatePicker;
    @FXML private ComboBox<String> reportTypeCombo;
    @FXML private TextArea reportResultArea;

    // İstatistik kartları
    @FXML private Text totalRevenueText;
    @FXML private Text totalReservationsText;
    @FXML private Text occupancyRateText;
    @FXML private Text availableRoomsText;

    @Override
    protected void initialize() {
        super.initialize();

        // Varsayılan rapor tipi seçimi
        if (reportTypeCombo != null && reportTypeCombo.getItems().isEmpty()) {
            reportTypeCombo.getItems().addAll(
                    "Gelir Raporu",
                    "Doluluk Raporu",
                    "Müşteri Raporu",
                    "Oda Performans"
            );
        }

        // Sayfa yüklendiğinde istatistikleri veritabanından çek ve göster
        loadStatisticsFromDatabase();
    }

    /**
     * Veritabanından istatistikleri çeker ve kartlara yazar
     */
    private void loadStatisticsFromDatabase() {
        try {
            List<Reservation> reservations = ReservationService.getAllReservations();
            List<Room> rooms = RoomService.getAllRooms();

            // Toplam gelir hesapla (ödenen rezervasyonlar)
            double totalRevenue = reservations.stream()
                    .filter(Reservation::isPaid)
                    .mapToDouble(Reservation::getTotalPrice)
                    .sum();

            // Toplam rezervasyon sayısı
            int totalReservations = reservations.size();

            // Müsait oda sayısı
            long availableRooms = rooms.stream().filter(Room::isAvailable).count();

            // Doluluk oranı
            double occupancyRate = rooms.isEmpty() ? 0 :
                    ((double) (rooms.size() - availableRooms) / rooms.size()) * 100;

            // UI'ı güncelle
            if (totalRevenueText != null) {
                totalRevenueText.setText(String.format("%.2f ₺", totalRevenue));
            }
            if (totalReservationsText != null) {
                totalReservationsText.setText(String.valueOf(totalReservations));
            }
            if (occupancyRateText != null) {
                occupancyRateText.setText(String.format("%.1f%%", occupancyRate));
            }
            if (availableRoomsText != null) {
                availableRoomsText.setText(String.valueOf(availableRooms));
            }

        } catch (Exception e) {
            System.err.println("İstatistik yükleme hatası: " + e.getMessage());
        }
    }

    @FXML
    private void generateReport() {
        String reportType = reportTypeCombo != null ? reportTypeCombo.getValue() : null;

        if (reportType == null) {
            AlertManager.Alert(Alert.AlertType.WARNING,
                    "Lütfen rapor türü seçin!", "Uyarı", "");
            return;
        }

        StringBuilder report = new StringBuilder();
        report.append("========================================\n");
        report.append(reportType).append("\n");
        report.append("========================================\n\n");

        LocalDate startDate = startDatePicker != null ? startDatePicker.getValue() : null;
        LocalDate endDate = endDatePicker != null ? endDatePicker.getValue() : null;

        if (startDate != null && endDate != null) {
            report.append("Tarih Aralığı: ").append(startDate).append(" - ").append(endDate).append("\n\n");
        }

        // İstatistikler
        List<Reservation> reservations = ReservationService.getAllReservations();
        List<Room> rooms = RoomService.getAllRooms();
        int customerCount = CustomerService.getAllCustomers().size();

        long activeReservations = reservations.stream()
                .filter(r -> "CONFIRMED".equals(r.getState()) || "CHECKED_IN".equals(r.getState()))
                .count();

        long availableRooms = rooms.stream().filter(Room::isAvailable).count();

        double totalRevenue = reservations.stream()
                .filter(Reservation::isPaid)
                .mapToDouble(Reservation::getTotalPrice)
                .sum();

        double occupancyRate = rooms.isEmpty() ? 0 :
                ((double) (rooms.size() - availableRooms) / rooms.size()) * 100;

        report.append("📊 GENEL İSTATİSTİKLER\n");
        report.append("─────────────────────────────\n");
        report.append(String.format("Toplam Müşteri: %d\n", customerCount));
        report.append(String.format("Toplam Oda: %d\n", rooms.size()));
        report.append(String.format("Müsait Oda: %d\n", availableRooms));
        report.append(String.format("Aktif Rezervasyon: %d\n", activeReservations));
        report.append(String.format("Doluluk Oranı: %.1f%%\n", occupancyRate));
        report.append(String.format("Toplam Gelir: %.2f TL\n\n", totalRevenue));

        report.append("📋 REZERVASYON DETAYLARI\n");
        report.append("─────────────────────────────\n");

        for (Reservation res : reservations) {
            if (startDate != null && endDate != null) {
                if (res.getCheckInDate().isBefore(startDate) ||
                    res.getCheckInDate().isAfter(endDate)) {
                    continue;
                }
            }

            report.append(String.format("• %s - %s (%s) - %.2f TL\n",
                    res.getConfirmationCode(),
                    res.getCheckInDate(),
                    res.getState(),
                    res.getTotalPrice()));
        }

        if (reportResultArea != null) {
            reportResultArea.setText(report.toString());
        }

        // İstatistik kartlarını güncelle
        if (totalRevenueText != null) {
            totalRevenueText.setText(String.format("%.2f ₺", totalRevenue));
        }

        if (totalReservationsText != null) {
            totalReservationsText.setText(String.valueOf(reservations.size()));
        }

        if (occupancyRateText != null) {
            occupancyRateText.setText(String.format("%.1f%%", occupancyRate));
        }

        if (availableRoomsText != null) {
            availableRoomsText.setText(String.valueOf(availableRooms));
        }
    }
}
