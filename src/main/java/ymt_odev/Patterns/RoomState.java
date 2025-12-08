package ymt_odev.Patterns;

import ymt_odev.Domain.Room;

/**
 * State Pattern - Oda durumu arayüzü
 */
public interface RoomState {
    void handle(Room room);
    String getStateName();
    boolean canBeReserved();
}

