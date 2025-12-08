package ymt_odev;

public enum Access {
    ADMIN("Admin"),
    STAFF("Personel"),
    CUSTOMER("Müşteri");

    private final String access;

    Access(String access) {
        this.access = access;
    }
    public String getAccess() {
        return access;
    }
    @Override
    public String toString() {
        return access;
    }
}
