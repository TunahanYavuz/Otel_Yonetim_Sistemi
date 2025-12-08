package ymt_odev.Patterns;

import ymt_odev.Domain.Room;

/**
 * State Pattern - Müsait oda durumu
 */
public class AvailableRoomState implements RoomState {
    @Override
    public void handle(Room room) {
        room.setState("AVAILABLE");
    }

    @Override
    public String getStateName() {
        return "AVAILABLE";
    }

    @Override
    public boolean canBeReserved() {
        return true;
    }
}

