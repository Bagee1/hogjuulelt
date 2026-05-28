package mn.edu.room.notification.core.domain;

import java.time.Instant;

public record NotificationLog(
        Long id,
        Long bookingId,
        Long studentId,
        String eventType,
        String message,
        Instant createdAt
) {
    public NotificationLog {
        bookingId = requirePositive(bookingId, "bookingId");
        studentId = requirePositive(studentId, "studentId");
        eventType = requireText(eventType, "eventType");
        message = requireText(message, "message");
        createdAt = createdAt == null ? Instant.now() : createdAt;
    }

    public static NotificationLog create(Long bookingId, Long studentId, String eventType, String message) {
        return new NotificationLog(null, bookingId, studentId, eventType, message, Instant.now());
    }

    private static Long requirePositive(Long value, String field) {
        if (value == null || value <= 0) {
            throw new IllegalArgumentException(field + " must be positive");
        }
        return value;
    }

    private static String requireText(String value, String field) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(field + " is required");
        }
        return value.trim();
    }
}
