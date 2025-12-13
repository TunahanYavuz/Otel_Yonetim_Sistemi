package ymt_odev;

/**
 * Rezervasyon durumlarını temsil eden enum
 */
public enum ReservationState {
    PENDING("Beklemede"),
    CONFIRMED("Onaylandı"),
    CHECKED_IN("Giriş Yapıldı"),
    CHECKED_OUT("Çıkış Yapıldı"),
    CANCELLED("İptal Edildi"),
    NO_SHOW("Gelmedi");

    private final String displayName;

    ReservationState(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return name();
    }

    /**
     * String değerden enum'a dönüştürür
     */
    public static ReservationState fromString(String value) {
        if (value == null) return PENDING;

        try {
            return valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            return PENDING;
        }
    }
}

