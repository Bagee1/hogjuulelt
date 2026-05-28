package mn.edu.room.core.dto;

public record RoomResponseDto(Long id, String name, int capacity, String location) {
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getCapacity() {
        return capacity;
    }

    public String getLocation() {
        return location;
    }
}
