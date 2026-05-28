package mn.edu.room.factory;

import java.util.List;
import mn.edu.room.core.observer.AuditLogObserver;
import mn.edu.room.core.observer.BookingObserver;
import mn.edu.room.core.port.in.AuditLogUseCase;
import mn.edu.room.core.port.in.AuthUseCase;
import mn.edu.room.core.port.in.BookingUseCase;
import mn.edu.room.core.port.in.RoomUseCase;
import mn.edu.room.core.service.AuditLogService;
import mn.edu.room.core.service.AuthService;
import mn.edu.room.core.service.BookingService;
import mn.edu.room.core.service.RoomService;

public final class ServiceFactory {
    private static final AuthUseCase AUTH_USE_CASE = new AuthService(RepositoryFactory.userRepository());
    private static final RoomUseCase ROOM_USE_CASE = new RoomService(RepositoryFactory.roomRepository());
    private static final AuditLogUseCase AUDIT_LOG_USE_CASE = new AuditLogService(RepositoryFactory.auditLogRepository());
    private static final BookingObserver AUDIT_LOG_OBSERVER = new AuditLogObserver(RepositoryFactory.auditLogRepository());
    private static final BookingUseCase BOOKING_USE_CASE = new BookingService(
            RepositoryFactory.bookingRepository(),
            RepositoryFactory.roomRepository(),
            RepositoryFactory.userRepository(),
            List.of(AUDIT_LOG_OBSERVER)
    );

    private ServiceFactory() {
    }

    public static AuthUseCase authUseCase() {
        return AUTH_USE_CASE;
    }

    public static RoomUseCase roomUseCase() {
        return ROOM_USE_CASE;
    }

    public static BookingUseCase bookingUseCase() {
        return BOOKING_USE_CASE;
    }

    public static AuditLogUseCase auditLogUseCase() {
        return AUDIT_LOG_USE_CASE;
    }
}
