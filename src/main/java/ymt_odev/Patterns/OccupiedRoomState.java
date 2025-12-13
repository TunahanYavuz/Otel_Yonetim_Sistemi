package ymt_odev.Patterns;

import ymt_odev.Domain.Room;

/**
 * State Pattern - Dolu oda durumu
 */
public class OccupiedRoomState implements RoomState {
    @Override
    public void handle(Room room) {
        room.setState(ymt_odev.RoomState.OCCUPIED.toString());
    }

    @Override
    public String getStateName() {
        return ymt_odev.RoomState.OCCUPIED.toString();
    }

    @Override
    public boolean canBeReserved() {
        return ymt_odev.RoomState.OCCUPIED.canBeReserved();
    }
}

