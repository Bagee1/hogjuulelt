package mn.edu.room.core.dto;

public record AuditLogResponseDto(Long id, Long bookingId, String message, String createdAt) {
    public Long getId() {
        return id;
    }

    public Long getBookingId() {
        return bookingId;
    }

    public String getMessage() {
        return message;
    }

    public String getCreatedAt() {
        return createdAt;
    }
}
