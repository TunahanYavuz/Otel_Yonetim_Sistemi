package ymt_odev;

/**
 * Oda durumlarını temsil eden enum
 */
public enum RoomState {
    AVAILABLE("Müsait"),
    RESERVED("Rezerve"),
    OCCUPIED("Dolu"),
    CLEANING("Temizlikte"),
    MAINTENANCE("Bakımda"),
    OUT_OF_ORDER("Kullanım Dışı");

    private final String displayName;

    RoomState(String displayName) {
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
    public static RoomState fromString(String value) {
        if (value == null) return AVAILABLE;

        try {
            return valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            return switch (value.toLowerCase()) {
                case "müsait", "musait" -> AVAILABLE;
                case "rezerve" -> RESERVED;
                case "dolu" -> OCCUPIED;
                case "temizlikte" -> CLEANING;
                case "bakımda", "bakimda" -> MAINTENANCE;
                case "kullanım dışı", "kullanim disi" -> OUT_OF_ORDER;
                default -> AVAILABLE;
            };
        }
    }

    /**
     * Oda rezerve edilebilir mi?
     */
    public boolean canBeReserved() {
        return this == AVAILABLE;
    }
}
