package ymt_odev.Patterns;

import ymt_odev.Domain.Room;

/**
 * State Pattern - Rezerve edilmiş oda durumu
 */
public class ReservedRoomState implements RoomState {
    @Override
    public void handle(Room room) {
        room.setState(ymt_odev.RoomState.RESERVED.toString());
    }

    @Override
    public String getStateName() {
        return ymt_odev.RoomState.RESERVED.toString();
    }

    @Override
    public boolean canBeReserved() {
        return ymt_odev.RoomState.RESERVED.canBeReserved();
    }
}

