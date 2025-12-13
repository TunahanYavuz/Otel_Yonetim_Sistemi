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
        ymt_odev.RoomState roomState = ymt_odev.RoomState.fromString(stateName);
        return switch (roomState) {
            case RESERVED -> new ReservedRoomState();
            case OCCUPIED -> new OccupiedRoomState();
            case CLEANING -> new CleaningRoomState();
            case MAINTENANCE -> new MaintenanceRoomState();
            default -> new AvailableRoomState();
        };
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

