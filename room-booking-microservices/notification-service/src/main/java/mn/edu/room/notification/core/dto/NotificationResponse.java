package mn.edu.room.notification.core.dto;

import mn.edu.room.notification.core.domain.NotificationLog;

import java.time.Instant;

public record NotificationResponse(
        Long id,
        Long bookingId,
        Long studentId,
        String eventType,
        String message,
        Instant createdAt
) {
    public static NotificationResponse from(NotificationLog log) {
        return new NotificationResponse(
                log.id(),
                log.bookingId(),
                log.studentId(),
                log.eventType(),
                log.message(),
                log.createdAt()
        );
    }
}
