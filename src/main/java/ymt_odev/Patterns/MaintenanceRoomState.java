package ymt_odev.Patterns;

import ymt_odev.Domain.Room;

/**
 * State Pattern - Bakımda olan oda durumu
 */
public class MaintenanceRoomState implements RoomState {
    @Override
    public void handle(Room room) {
        room.setState(ymt_odev.RoomState.MAINTENANCE.toString());
    }

    @Override
    public String getStateName() {
        return ymt_odev.RoomState.MAINTENANCE.toString();
    }

    @Override
    public boolean canBeReserved() {
        return ymt_odev.RoomState.MAINTENANCE.canBeReserved();
    }
}

