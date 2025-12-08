package ymt_odev.Patterns;

/**
 * State Pattern - Oda durum yöneticisi
 * Factory Pattern kullanarak duruma göre RoomState nesnesi oluşturur
 */
public class RoomStateManager {

    /**
     * Factory method - Durum adına göre uygun RoomState nesnesi döndürür
     */
    public static RoomState createState(String stateName) {
        switch (stateName.toUpperCase()) {
            case "AVAILABLE":
                return new AvailableRoomState();
            case "RESERVED":
                return new ReservedRoomState();
            case "OCCUPIED":
                return new OccupiedRoomState();
            case "CLEANING":
                return new CleaningRoomState();
            case "MAINTENANCE":
                return new MaintenanceRoomState();
            default:
                return new AvailableRoomState();
        }
    }

    /**
     * Oda durumunu değiştirir ve veritabanını günceller
     */
    public static boolean changeRoomState(int roomId, String newStateName) {
        RoomState state = createState(newStateName);

        ymt_odev.Database.DatabaseManager updater = new ymt_odev.Database.DBDataUpdater();

        boolean success = updater.updateDataWithCondition(
                "Rooms",
                new String[]{"state"},
                new String[]{"state"},
                new String[]{state.getStateName()},
                new String[]{"id"},
                new Object[]{roomId}
        );

        if (success) {
            // Observer pattern ile bildirim gönder
            NotificationManager.getInstance().notifyRoomStateChanged(
                    String.valueOf(roomId),
                    state.getStateName()
            );
        }

        return success;
    }
}

