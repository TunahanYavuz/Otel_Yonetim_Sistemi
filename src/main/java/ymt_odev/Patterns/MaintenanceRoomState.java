package ymt_odev.Patterns;

import ymt_odev.Domain.Room;

/**
 * State Pattern - Bakımda olan oda durumu
 */
public class MaintenanceRoomState implements RoomState {
    @Override
    public void handle(Room room) {
        room.setState("MAINTENANCE");
    }

    @Override
    public String getStateName() {
        return "MAINTENANCE";
    }

    @Override
    public boolean canBeReserved() {
        return false;
    }
}

