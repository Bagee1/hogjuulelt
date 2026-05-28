package mn.edu.room.factory;

import mn.edu.room.adapter.out.jdbc.JdbcAuditLogRepository;
import mn.edu.room.adapter.out.jdbc.JdbcBookingRepository;
import mn.edu.room.adapter.out.jdbc.JdbcRoomRepository;
import mn.edu.room.adapter.out.jdbc.JdbcUserRepository;
import mn.edu.room.config.DatabaseConnection;
import mn.edu.room.core.port.out.AuditLogRepository;
import mn.edu.room.core.port.out.BookingRepository;
import mn.edu.room.core.port.out.RoomRepository;
import mn.edu.room.core.port.out.UserRepository;

public final class RepositoryFactory {
    private static final DatabaseConnection DATABASE_CONNECTION = DatabaseConnection.getInstance();
    private static final UserRepository USER_REPOSITORY = new JdbcUserRepository(DATABASE_CONNECTION);
    private static final RoomRepository ROOM_REPOSITORY = new JdbcRoomRepository(DATABASE_CONNECTION);
    private static final BookingRepository BOOKING_REPOSITORY = new JdbcBookingRepository(DATABASE_CONNECTION);
    private static final AuditLogRepository AUDIT_LOG_REPOSITORY = new JdbcAuditLogRepository(DATABASE_CONNECTION);

    private RepositoryFactory() {
    }

    public static UserRepository userRepository() {
        return USER_REPOSITORY;
    }

    public static RoomRepository roomRepository() {
        return ROOM_REPOSITORY;
    }

    public static BookingRepository bookingRepository() {
        return BOOKING_REPOSITORY;
    }

    public static AuditLogRepository auditLogRepository() {
        return AUDIT_LOG_REPOSITORY;
    }
}
