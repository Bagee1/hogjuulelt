package mn.edu.room.room.core.domain;

public record Room(Long id, String name, int capacity, String location) {
    public Room {
        name = requireText(name, "name");
        location = requireText(location, "location");
        if (capacity <= 0) {
            throw new IllegalArgumentException("capacity must be positive");
        }
    }

    public static Room create(String name, int capacity, String location) {
        return new Room(null, name, capacity, location);
    }

    private static String requireText(String value, String field) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(field + " is required");
        }
        return value.trim();
    }
}
