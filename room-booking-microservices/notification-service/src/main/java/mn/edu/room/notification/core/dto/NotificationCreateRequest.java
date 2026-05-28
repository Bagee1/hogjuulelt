package mn.edu.room.notification.core.dto;

public record NotificationCreateRequest(Long bookingId, Long studentId, String eventType, String message) {
}
