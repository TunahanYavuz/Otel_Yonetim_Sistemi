package ymt_odev.Services;

import ymt_odev.Database.DBDataInsertion;
import ymt_odev.Database.DBDataSelection;
import ymt_odev.Database.DBDataUpdater;
import ymt_odev.Database.DatabaseManager;
import ymt_odev.Domain.Reservation;
import ymt_odev.Patterns.NotificationManager;
import ymt_odev.ReservationState;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Rezervasyon yönetim servisi
 */
public class ReservationService {

    /**
     * Yeni rezervasyon oluşturur
     */
    public static String createReservation(int customerId, int roomId, LocalDate checkIn,
                                           LocalDate checkOut, int guestCount, double totalPrice,
                                           String specialRequests, String paymentMethod) {
        DatabaseManager inserter = new DBDataInsertion();

        String confirmationCode = generateConfirmationCode();

        // SQL Server uyumlu tarih formatı: yyyy-MM-dd
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        String[] columns = new String[]{
                "customerId", "roomId", "checkInDate", "checkOutDate",
                "guestCount", "totalPrice", "state", "confirmationCode", "specialRequests", "paymentMethod"
        };

        Object[] values = new Object[]{
                customerId, roomId, checkIn.format(dateFormatter), checkOut.format(dateFormatter),
                guestCount, totalPrice, "PENDING", confirmationCode, specialRequests, paymentMethod
        };

        boolean success = inserter.insertData("Reservations", columns, values);

        if (success) {
            // Observer pattern ile bildirim gönder
            NotificationManager.getInstance().notifyReservationCreated(customerId, confirmationCode);
        }

        return success ? confirmationCode : null;
    }

    /**
     * Yeni rezervasyon oluşturur (ödeme yöntemi olmadan - eski uyumluluk için)
     */
    public static String createReservation(int customerId, int roomId, LocalDate checkIn,
                                           LocalDate checkOut, int guestCount, double totalPrice,
                                           String specialRequests) {
        return createReservation(customerId, roomId, checkIn, checkOut, guestCount, totalPrice, specialRequests, null);
    }

    /**
     * Müşteriye ait rezervasyonları getirir
     */
    public static List<Reservation> getCustomerReservations(int customerId) {
        List<Reservation> reservations = new ArrayList<>();
        DatabaseManager selector = new DBDataSelection();

        try {
            String[] columns = new String[]{"*"};
            String[] conditions = new String[]{"customerId"};
            String[] values = new String[]{String.valueOf(customerId)};

            ResultSet rs = selector.selectDataWithCondition("Reservations", columns, conditions, values);

            while (rs != null && rs.next()) {
                reservations.add(mapResultSetToReservation(rs));
            }

            if (rs != null) rs.close();

        } catch (SQLException e) {
            System.err.println("Rezervasyon getirme hatası: " + e.getMessage());
        }

        return reservations;
    }

    /**
     * Rezervasyon durumunu günceller
     */
    public static boolean updateReservationState(int reservationId, String newState) {
        DatabaseManager updater = new DBDataUpdater();

        String[] columns = new String[]{"state"};
        String[] values = new String[]{newState};
        String[] whereClause = new String[]{"id"};
        Object[] whereParams = new Object[]{reservationId};

        return updater.updateDataWithCondition(
                "Reservations",
                columns,
                columns,
                values,
                whereClause,
                whereParams
        );
    }

    /**
     * Rezervasyonu iptal eder
     */
    public static boolean cancelReservation(int reservationId) {
        boolean success = updateReservationState(reservationId, "CANCELLED");

        if (success) {
            // TODO: Müşteri ID'sini ve confirmation code'u al, bildirim gönder
        }

        return success;
    }

    /**
     * Ödeme onaylama işlemi - isPaid değerini true yapar
     */
    public static boolean confirmPayment(int reservationId) {
        DatabaseManager updater = new DBDataUpdater();

        String[] columns = new String[]{"isPaid"};
        String[] values = new String[]{"1"}; // SQL Server'da BIT için 1 = true
        String[] whereClause = new String[]{"id"};
        Object[] whereParams = new Object[]{reservationId};

        boolean success = updater.updateDataWithCondition(
                "Reservations",
                columns,
                columns,
                values,
                whereClause,
                whereParams
        );

        if (success) {
            // Ödeme onaylandığında durum da CONFIRMED yapılabilir
            updateReservationState(reservationId, "CONFIRMED");
        }

        return success;
    }

    /**
     * Check-in işlemi yapar
     */
    public static boolean checkIn(int reservationId) {
        DatabaseManager updater = new DBDataUpdater();

        // SQL Server uyumlu tarih formatı: yyyy-MM-dd HH:mm:ss
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String checkInTime = LocalDateTime.now().format(formatter);

        String[] columns = new String[]{"state", "checkInTime"};
        String[] values = new String[]{"CHECKED_IN", checkInTime};
        String[] whereClause = new String[]{"id"};
        Object[] whereParams = new Object[]{reservationId};

        return updater.updateDataWithCondition(
                "Reservations",
                columns,
                columns,
                values,
                whereClause,
                whereParams
        );
    }

    /**
     * Check-out işlemi yapar
     */
    public static boolean checkOut(int reservationId) {
        DatabaseManager updater = new DBDataUpdater();

        // SQL Server uyumlu tarih formatı: yyyy-MM-dd HH:mm:ss
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String checkOutTime = LocalDateTime.now().format(formatter);

        String[] columns = new String[]{"state", "checkOutTime"};
        String[] values = new String[]{ReservationState.CHECKED_OUT.toString(), checkOutTime};
        String[] whereClause = new String[]{"id"};
        Object[] whereParams = new Object[]{reservationId};

        return updater.updateDataWithCondition(
                "Reservations",
                columns,
                columns,
                values,
                whereClause,
                whereParams
        );
    }

    /**
     * Tüm rezervasyonları listeler
     */
    public static List<Reservation> getAllReservations() {
        List<Reservation> reservations = new ArrayList<>();
        DatabaseManager selector = new DBDataSelection();

        try {
            ResultSet rs = selector.selectData("Reservations", new String[]{"*"});

            while (rs != null && rs.next()) {
                reservations.add(mapResultSetToReservation(rs));
            }

            if (rs != null) rs.close();

        } catch (SQLException e) {
            System.err.println("Rezervasyon listesi hatası: " + e.getMessage());
        }
        return reservations;
    }

    /**
     * Tarih aralığına göre rezervasyonları filtreler
     * @param startDate Başlangıç tarihi (check-in tarihi bu tarihten sonra veya eşit olmalı)
     * @param endDate Bitiş tarihi (check-out tarihi bu tarihten önce veya eşit olmalı)
     * @param status Durum filtresi (null ise tüm durumlar)
     */
    public static List<Reservation> getReservationsByDateRange(LocalDate startDate, LocalDate endDate, String status) {
        List<Reservation> reservations = new ArrayList<>();

        try {
            java.sql.Connection conn = ymt_odev.Database.DatabaseConnection.getInstance().getConnection();

            StringBuilder sql = new StringBuilder();
            sql.append("SELECT * FROM Reservations WHERE 1=1");

            if (startDate != null) {
                sql.append(" AND checkInDate >= ?");
            }
            if (endDate != null) {
                sql.append(" AND checkOutDate <= ?");
            }
            if (status != null && !status.isEmpty() && !status.equalsIgnoreCase("Tüm Durumlar")) {
                sql.append(" AND state = ?");
            }

            sql.append(" ORDER BY checkInDate DESC");

            java.sql.PreparedStatement stmt = conn.prepareStatement(sql.toString());

            int paramIndex = 1;
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            if (startDate != null) {
                stmt.setString(paramIndex++, startDate.format(dateFormatter));
            }
            if (endDate != null) {
                stmt.setString(paramIndex++, endDate.format(dateFormatter));
            }
            if (status != null && !status.isEmpty() && !status.equalsIgnoreCase("Tüm Durumlar")) {
                stmt.setString(paramIndex++, status);
            }

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                reservations.add(mapResultSetToReservation(rs));
            }

            rs.close();
            stmt.close();

        } catch (SQLException e) {
            System.err.println("Tarih filtreli rezervasyon listesi hatası: " + e.getMessage());
        }

        return reservations;
    }

    /**
     * Onay koduna göre rezervasyon arar
     */
    public static Reservation getReservationByConfirmationCode(String confirmationCode) {
        DatabaseManager selector = new DBDataSelection();

        try {
            String[] columns = new String[]{"*"};
            String[] conditions = new String[]{"confirmationCode"};
            String[] values = new String[]{confirmationCode};

            ResultSet rs = selector.selectDataWithCondition("Reservations", columns, conditions, values);

            if (rs != null && rs.next()) {
                Reservation reservation = mapResultSetToReservation(rs);
                rs.close();
                return reservation;
            }

        } catch (SQLException e) {
            System.err.println("Rezervasyon arama hatası: " + e.getMessage());
        }

        return null;
    }
    public static List<Reservation> getReservationCustomerName(String customerName){
        DatabaseManager selector = new DBDataSelection();
        List<Reservation> reservations = new ArrayList<>();
        try {

            ResultSet rs = selector.selectDataWithProximity("Reservations", "name", customerName);

            while (rs != null && rs.next()) {
                Reservation reservation = mapResultSetToReservation(rs);
                reservations.add(reservation);
            }
            return reservations;

        } catch (SQLException e) {
            System.err.println("Rezervasyon arama hatası: " + e.getMessage());
        }

        return null;
    }

    /**
     * ResultSet'ten Reservation nesnesine dönüştürme
     */
    private static Reservation mapResultSetToReservation(ResultSet rs) throws SQLException {
        // Date alanlarını doğrudan getDate() ile al
        LocalDate checkIn = rs.getDate("checkInDate").toLocalDate();
        LocalDate checkOut = rs.getDate("checkOutDate").toLocalDate();

        // DateTime alanları için güvenli parse
        LocalDateTime checkInTime = null;
        LocalDateTime checkOutTime = null;
        LocalDateTime createdAt;

        try {
            String checkInTimeStr = rs.getString("checkInTime");
            if (checkInTimeStr != null && !checkInTimeStr.isEmpty()) {
                checkInTime = rs.getTimestamp("checkInTime").toLocalDateTime();
            }
        } catch (Exception e) {
            // checkInTime null kalacak
        }

        try {
            String checkOutTimeStr = rs.getString("checkOutTime");
            if (checkOutTimeStr != null && !checkOutTimeStr.isEmpty()) {
                checkOutTime = rs.getTimestamp("checkOutTime").toLocalDateTime();
            }
        } catch (Exception e) {
            // checkOutTime null kalacak
        }

        try {
            createdAt = rs.getTimestamp("createdAt").toLocalDateTime();
        } catch (Exception e) {
            createdAt = LocalDateTime.now(); // Fallback
        }

        return new Reservation(
                rs.getInt("id"),
                rs.getInt("customerId"),
                rs.getInt("roomId"),
                checkIn,
                checkOut,
                checkInTime,
                checkOutTime,
                rs.getInt("guestCount"),
                rs.getDouble("totalPrice"),
                rs.getDouble("depositAmount"),
                rs.getBoolean("isPaid"),
                rs.getString("paymentMethod"),
                rs.getString("state"),
                rs.getString("confirmationCode"),
                rs.getString("specialRequests"),
                rs.getString("notes"),
                createdAt
        );
    }

    /**
     * Benzersiz onay kodu üretir (max 15 karakter)
     */
    private static String generateConfirmationCode() {
        Random random = new Random();
        // Son 6 hane timestamp + 6 haneli random = 12 karakter + HTL = 15 karakter
        long timestamp = System.currentTimeMillis() % 1000000;
        int randomNum = random.nextInt(1000000);
        return String.format("HTL%06d%06d", timestamp, randomNum);
    }
}
