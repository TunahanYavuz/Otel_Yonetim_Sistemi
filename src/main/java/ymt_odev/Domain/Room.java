package ymt_odev.Domain;

import java.util.Objects;

/**
 * Oda domain modeli
 */
public class Room {
    private final int id;
    private final String roomNumber;
    private final String roomType;
    private final int capacity;
    private final double pricePerNight;
    private final String features;
    private final int floor;
    private final boolean hasBalcony;
    private final boolean hasSeaView;
    private final boolean hasKitchen;
    private final boolean hasJacuzzi;
    private final boolean isPetFriendly;
    private String state;
    private final String description;

    public Room(int id, String roomNumber, String roomType, int capacity,
                double pricePerNight, String features, int floor,
                boolean hasBalcony, boolean hasSeaView, boolean hasKitchen, boolean hasJacuzzi,
                boolean isPetFriendly, String state, String description) {
        this.id = id;
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.capacity = capacity;
        this.pricePerNight = pricePerNight;
        this.features = features;
        this.floor = floor;
        this.hasBalcony = hasBalcony;
        this.hasSeaView = hasSeaView;
        this.hasKitchen = hasKitchen;
        this.hasJacuzzi = hasJacuzzi; // Varsayılan değer
        this.isPetFriendly = isPetFriendly;
        this.state = state;
        this.description = description;
    }

    // Getters
    public int getId() { return id; }
    public String getRoomNumber() { return roomNumber; }
    public String getRoomType() { return roomType; }
    public int getCapacity() { return capacity; }
    public double getPricePerNight() { return pricePerNight; }
    public String getFeatures() { return features; }
    public int getFloor() { return floor; }
    public boolean hasBalcony() { return hasBalcony; }
    public boolean hasSeaView() { return hasSeaView; }
    public boolean hasKitchen() { return hasKitchen; }
    public boolean hasJacuzzi() { return hasJacuzzi; }
    public boolean isPetFriendly() { return isPetFriendly; }
    public String getState() { return state; }
    public String getDescription() { return description; }

    // Alternatif getter isimleri (JavaFX PropertyValueFactory için)
    public boolean isHasBalcony() { return hasBalcony; }
    public boolean isHasSeaView() { return hasSeaView; }
    public boolean isHasKitchen() { return hasKitchen; }
    public boolean isHasJacuzzi() { return hasJacuzzi; }

    public void setState(String state) {
        this.state = state;
    }

    public boolean isAvailable() {
        return "AVAILABLE".equals(state);
    }

    @Override
    public String toString() {
        return String.format("Oda %s - %s (%d kişilik) - %.2f TL/gece",
                roomNumber, roomType, capacity, pricePerNight);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Room)) return false;
        Room room = (Room) o;
        return id == room.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

