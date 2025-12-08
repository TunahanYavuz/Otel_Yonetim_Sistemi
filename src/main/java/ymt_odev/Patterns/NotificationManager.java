package ymt_odev.Patterns;

import ymt_odev.Database.DBDataInsertion;
import ymt_odev.Database.DatabaseManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Observer Pattern - Bildirim yöneticisi
 * Rezervasyon, oda durumu vb. değişikliklerde ilgilenen taraflara bildirim gönderir
 */
public class NotificationManager {
    private static NotificationManager instance;
    private final List<NotificationObserver> observers = new ArrayList<>();

    private NotificationManager() {}

    public static synchronized NotificationManager getInstance() {
        if (instance == null) {
            instance = new NotificationManager();
        }
        return instance;
    }

    /**
     * Observer ekler
     */
    public void addObserver(NotificationObserver observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
        }
    }

    /**
     * Observer kaldırır
     */
    public void removeObserver(NotificationObserver observer) {
        observers.remove(observer);
    }

    /**
     * Tüm observer'lara bildirim gönderir
     */
    public void notifyObservers(String title, String message, String type) {
        for (NotificationObserver observer : observers) {
            observer.update(title, message, type);
        }
    }

    /**
     * Veritabanına bildirim kaydeder
     */
    public void sendNotification(Integer userId, String userType, String title,
                                  String message, String notificationType) {
        DatabaseManager inserter = new DBDataInsertion();

        String[] columns = new String[]{
                "userId", "userType", "title", "message", "notificationType", "isRead"
        };

        Object[] values = new Object[]{
                userId, userType, title, message, notificationType, false
        };

        inserter.insertData("Notifications", columns, values);

        // Observer'lara da bildir
        notifyObservers(title, message, notificationType);
    }

    /**
     * Rezervasyon oluşturma bildirimi
     */
    public void notifyReservationCreated(int customerId, String confirmationCode) {
        sendNotification(
                customerId,
                "CUSTOMER",
                "Rezervasyon Oluşturuldu",
                "Rezervasyon kodunuz: " + confirmationCode,
                "RESERVATION"
        );

        // Personele de bildir
        sendNotification(
                null,
                "STAFF",
                "Yeni Rezervasyon",
                "Yeni rezervasyon oluşturuldu: " + confirmationCode,
                "SYSTEM"
        );
    }

    /**
     * Rezervasyon iptal bildirimi
     */
    public void notifyReservationCancelled(int customerId, String confirmationCode) {
        sendNotification(
                customerId,
                "CUSTOMER",
                "Rezervasyon İptal Edildi",
                "Rezervasyonunuz iptal edildi: " + confirmationCode,
                "RESERVATION"
        );
    }

    /**
     * Oda durumu değişikliği bildirimi
     */
    public void notifyRoomStateChanged(String roomNumber, String newState) {
        sendNotification(
                null,
                "STAFF",
                "Oda Durumu Değişti",
                "Oda " + roomNumber + " durumu: " + newState,
                "ROOM"
        );
    }
}

