package ymt_odev.Patterns;

import ymt_odev.Domain.Room;

/**
 * State Pattern - Temizlik durumunda oda
 */
public class CleaningRoomState implements RoomState {
    @Override
    public void handle(Room room) {
        room.setState(ymt_odev.RoomState.CLEANING.toString());
    }

    @Override
    public String getStateName() {
        return ymt_odev.RoomState.CLEANING.toString();
    }

    @Override
    public boolean canBeReserved() {
        return ymt_odev.RoomState.CLEANING.canBeReserved();
    }
}

