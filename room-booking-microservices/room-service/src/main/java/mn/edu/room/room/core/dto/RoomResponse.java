package mn.edu.room.room.core.dto;

import mn.edu.room.room.core.domain.Room;

public record RoomResponse(Long id, String name, int capacity, String location) {
    public static RoomResponse from(Room room) {
        return new RoomResponse(room.id(), room.name(), room.capacity(), room.location());
    }
}
