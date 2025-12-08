package ymt_odev.Patterns;

import ymt_odev.Domain.Room;

/**
 * State Pattern - Temizlik durumunda oda
 */
public class CleaningRoomState implements RoomState {
    @Override
    public void handle(Room room) {
        room.setState("CLEANING");
    }

    @Override
    public String getStateName() {
        return "CLEANING";
    }

    @Override
    public boolean canBeReserved() {
        return false;
    }
}

