package mn.edu.room.core.observer;

import java.time.LocalDateTime;
import mn.edu.room.core.domain.AuditLog;
import mn.edu.room.core.domain.Booking;
import mn.edu.room.core.domain.BookingStatus;
import mn.edu.room.core.port.out.AuditLogRepository;

public class AuditLogObserver implements BookingObserver {
    private final AuditLogRepository auditLogRepository;

    public AuditLogObserver(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    @Override
    public void onStatusChanged(Booking booking, BookingStatus oldStatus, BookingStatus newStatus, Long actorId) {
        String message = "Захиалга #" + booking.getId() + " төлөв "
                + statusLabel(oldStatus) + "-аас " + statusLabel(newStatus)
                + " болж өөрчлөгдлөө. Үйлдэл хийсэн хэрэглэгч #" + actorId;
        auditLogRepository.save(new AuditLog(null, booking.getId(), message, LocalDateTime.now()));
    }

    private static String statusLabel(BookingStatus status) {
        return switch (status) {
            case PENDING -> "Хүлээгдэж байна";
            case APPROVED -> "Зөвшөөрсөн";
            case REJECTED -> "Татгалзсан";
            case CANCELLED -> "Цуцалсан";
        };
    }
}
