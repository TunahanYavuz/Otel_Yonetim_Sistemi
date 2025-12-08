package ymt_odev.Patterns;

import ymt_odev.Domain.Room;

/**
 * State Pattern - Rezerve edilmiş oda durumu
 */
public class ReservedRoomState implements RoomState {
    @Override
    public void handle(Room room) {
        room.setState("RESERVED");
    }

    @Override
    public String getStateName() {
        return "RESERVED";
    }

    @Override
    public boolean canBeReserved() {
        return false;
    }
}

