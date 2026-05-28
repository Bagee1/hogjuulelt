package mn.edu.room.core.domain;

public class Room {
    private final Long id;
    private final String name;
    private final int capacity;
    private final String location;

    public Room(Long id, String name, int capacity, String location) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Багтаамж эерэг тоо байх ёстой.");
        }
        this.id = id;
        this.name = requireText(name, "name");
        this.capacity = capacity;
        this.location = requireText(location, "location");
    }

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

    private static String requireText(String value, String field) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(fieldLabel(field) + " шаардлагатай.");
        }
        return value.trim();
    }

    private static String fieldLabel(String field) {
        return switch (field) {
            case "name" -> "Өрөөний нэр";
            case "location" -> "Байршил";
            default -> field;
        };
    }
}
