package ymt_odev;

/**
 * Müşteri sadakat seviyelerini temsil eden enum
 */
public enum LoyaltyLevel {
    BRONZE("Bronze"),
    SILVER("Silver"),
    GOLD("Gold"),
    PLATINUM("Platinum");

    private final String displayName;

    LoyaltyLevel(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }

    /**
     * String değerden enum'a dönüştürür
     */
    public static LoyaltyLevel fromString(String value) {
        if (value == null) return BRONZE;

        try {
            return valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            return BRONZE;
        }
    }
}

