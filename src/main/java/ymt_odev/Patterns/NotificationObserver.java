package ymt_odev.Patterns;

/**
 * Observer Pattern - Bildirim dinleyici arayüzü
 */
public interface NotificationObserver {
    void update(String title, String message, String type);
}

