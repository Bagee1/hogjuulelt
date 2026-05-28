package mn.edu.room.booking.core.dto;

public record NotificationRequest(Long bookingId, Long studentId, String eventType, String message) {
}
