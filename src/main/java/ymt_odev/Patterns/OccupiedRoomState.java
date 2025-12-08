package ymt_odev.Patterns;

import ymt_odev.Domain.Room;

/**
 * State Pattern - Dolu oda durumu
 */
public class OccupiedRoomState implements RoomState {
    @Override
    public void handle(Room room) {
        room.setState("OCCUPIED");
    }

    @Override
    public String getStateName() {
        return "OCCUPIED";
    }

    @Override
    public boolean canBeReserved() {
        return false;
    }
}

