package ymt_odev.Patterns;

import ymt_odev.Domain.Room;

/**
 * State Pattern - Müsait oda durumu
 */
public class AvailableRoomState implements RoomState {
    @Override
    public void handle(Room room) {
        room.setState(ymt_odev.RoomState.AVAILABLE.toString());
    }

    @Override
    public String getStateName() {
        return ymt_odev.RoomState.AVAILABLE.toString();
    }

    @Override
    public boolean canBeReserved() {
        return ymt_odev.RoomState.AVAILABLE.canBeReserved();
    }
}

