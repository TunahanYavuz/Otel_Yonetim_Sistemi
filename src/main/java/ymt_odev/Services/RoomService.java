package ymt_odev.Services;

import ymt_odev.Database.DBDataSelection;
import ymt_odev.Database.DatabaseManager;
import ymt_odev.Domain.Room;
import ymt_odev.Patterns.RoomState;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Oda arama ve yönetim servisi
 */
public class RoomService {

    /**
     * Müsait odaları arar
     */
    public static List<Room> searchAvailableRooms(LocalDate checkIn, LocalDate checkOut,
                                                   Integer guestCount, String roomType) {
        List<Room> rooms = new ArrayList<>();
        DatabaseManager selector = new DBDataSelection();

        try {
            // Önce tüm odaları al
            ResultSet rs = selector.selectData("Rooms", new String[]{"*"});

            while (rs != null && rs.next()) {
                int id = rs.getInt("id");
                String state = rs.getString("state");
                int capacity = rs.getInt("capacity");
                String type = rs.getString("roomType");

                // Kapasite ve tip kontrolü
                if (guestCount != null && capacity < guestCount) continue;
                if (roomType != null && !roomType.isEmpty() && !type.equals(roomType)) continue;
                if (!"AVAILABLE".equals(state)) continue;

                // Çakışan rezervasyon var mı kontrol et
                if (checkIn != null && checkOut != null) {
                    if (hasConflictingReservation(id, checkIn, checkOut)) {
                        continue;
                    }
                }

                Room room = new Room(
                        id,
                        rs.getString("roomNumber"),
                        type,
                        capacity,
                        rs.getDouble("pricePerNight"),
                        rs.getString("features"),
                        rs.getInt("floor"),
                        rs.getBoolean("hasBalcony"),
                        rs.getBoolean("hasSeaView"),
                        rs.getBoolean("hasKitchen"),
                        rs.getBoolean("hasJacuzzi"),
                        rs.getBoolean("isPetFriendly"),
                        state,
                        rs.getString("description")
                );
                rooms.add(room);
            }

            if (rs != null) rs.close();
        } catch (SQLException e) {
            System.err.println("Oda arama hatası: " + e.getMessage());
        }

        return rooms;
    }
    public static List<Room> searchRooms(Integer roomNumber, String state,
                                                  String feature, String roomType) {
        List<Room> rooms = new ArrayList<>();
        DatabaseManager selector = new DBDataSelection();

        try {
            // Önce tüm odaları al
            ResultSet rs = selector.selectData("Rooms", new String[]{"*"});
            boolean hasFeature;
            if (feature == null) feature = "Tümü";

            switch (state) {
                case "Müsait":
                    state = "AVAILABLE";
                    break;
                case "Dolu":
                    state = "OCCUPIED";
                    break;
                case "Temizlikte":
                    state = "CLEANING";
                    break;
                case "Bakımda":
                    state = "MAINTENANCE";
                    break;
            }
            while (rs != null && rs.next()) {
                hasFeature = switch (feature) {
                    case "Tümü" -> true;
                    case "Mutfaklı" -> rs.getBoolean("hasKitchen");
                    case "Balkonlu" -> rs.getBoolean("hasBalcony");
                    case "Deniz Manzaralı" -> rs.getBoolean("hasSeaView");
                    case "Jakuzili" -> rs.getBoolean("hasJacuzzi");
                    default -> throw new IllegalStateException("Unexpected value: " + feature);
                };
                int id = rs.getInt("id");
                String rsState = rs.getString("state");
                int capacity = rs.getInt("capacity");
                String type = rs.getString("roomType");

                if (roomType != null && !roomType.isEmpty() && (!type.equals(roomType) && !roomType.equalsIgnoreCase("Tümü"))) continue;
                if (!state.equals(rsState)&&!state.equalsIgnoreCase("Tümü")) continue;

                if (roomNumber != null && !roomNumber.equals(rs.getInt("roomNumber"))) continue;
                if (!hasFeature) continue;


                Room room = new Room(
                        id,
                        rs.getString("roomNumber"),
                        type,
                        capacity,
                        rs.getDouble("pricePerNight"),
                        rs.getString("features"),
                        rs.getInt("floor"),
                        rs.getBoolean("hasBalcony"),
                        rs.getBoolean("hasSeaView"),
                        rs.getBoolean("hasKitchen"),
                        rs.getBoolean("hasJacuzzi"),
                        rs.getBoolean("isPetFriendly"),
                        rsState,
                        rs.getString("description")
                );
                rooms.add(room);
            }

            if (rs != null) rs.close();
        } catch (SQLException e) {
            System.err.println("Oda arama hatası: " + e.getMessage());
        }

        return rooms;
    }

    /**
     * Çakışan rezervasyon var mı kontrol eder
     */
    private static boolean hasConflictingReservation(int roomId, LocalDate checkIn, LocalDate checkOut) {
        DatabaseManager selector = new DBDataSelection();

        try {
            String[] columns = new String[]{"id"};
            String[] conditions = new String[]{"roomId"};
            String[] values = new String[]{String.valueOf(roomId)};

            ResultSet rs = selector.selectDataWithCondition("Reservations", columns, conditions, values);

            while (rs != null && rs.next()) {
                // TODO: Tarih çakışma kontrolü yapılacak
                // Şimdilik basit kontrol
            }

            if (rs != null) rs.close();
            return false;

        } catch (SQLException e) {
            return false;
        }
    }

    /**
     * Oda bilgilerini ID ile getirir
     */
    public static Room getRoomById(int roomId) {
        DatabaseManager selector = new DBDataSelection();

        try {
            String[] columns = new String[]{"*"};
            String[] conditions = new String[]{"id"};
            String[] values = new String[]{String.valueOf(roomId)};

            ResultSet rs = selector.selectDataWithCondition("Rooms", columns, conditions, values);

            if (rs != null && rs.next()) {
                Room room = new Room(
                        rs.getInt("id"),
                        rs.getString("roomNumber"),
                        rs.getString("roomType"),
                        rs.getInt("capacity"),
                        rs.getDouble("pricePerNight"),
                        rs.getString("features"),
                        rs.getInt("floor"),
                        rs.getBoolean("hasBalcony"),
                        rs.getBoolean("hasSeaView"),
                        rs.getBoolean("hasKitchen"),
                        rs.getBoolean("hasJacuzzi"),
                        rs.getBoolean("isPetFriendly"),
                        rs.getString("state"),
                        rs.getString("description")
                );
                rs.close();
                return room;
            }

        } catch (SQLException e) {
            System.err.println("Oda getirme hatası: " + e.getMessage());
        }

        return null;
    }

    /**
     * Tüm odaları listeler
     */
    public static List<Room> getAllRooms() {
        return searchAvailableRooms(null, null, null, null);
    }
}

