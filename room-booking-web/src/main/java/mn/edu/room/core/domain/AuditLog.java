package mn.edu.room.core.domain;

import java.time.LocalDateTime;

public class AuditLog {
    private final Long id;
    private final Long bookingId;
    private final String message;
    private final LocalDateTime createdAt;

    public AuditLog(Long id, Long bookingId, String message, LocalDateTime createdAt) {
        this.id = id;
        this.bookingId = bookingId;
        if (message == null || message.isBlank()) {
            throw new IllegalArgumentException("message is required");
        }
        this.message = message.trim();
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public Long getBookingId() {
        return bookingId;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
