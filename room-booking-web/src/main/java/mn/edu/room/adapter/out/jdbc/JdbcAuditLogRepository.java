package mn.edu.room.adapter.out.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import mn.edu.room.config.DatabaseConnection;
import mn.edu.room.core.domain.AuditLog;
import mn.edu.room.core.port.out.AuditLogRepository;

public class JdbcAuditLogRepository implements AuditLogRepository {
    private final DatabaseConnection databaseConnection;

    public JdbcAuditLogRepository(DatabaseConnection databaseConnection) {
        this.databaseConnection = databaseConnection;
    }

    @Override
    public void save(AuditLog auditLog) {
        String sql = "INSERT INTO audit_logs (booking_id, message, created_at) VALUES (?, ?, ?)";
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            if (auditLog.getBookingId() == null) {
                statement.setObject(1, null);
            } else {
                statement.setLong(1, auditLog.getBookingId());
            }
            statement.setString(2, auditLog.getMessage());
            statement.setTimestamp(3, Timestamp.valueOf(auditLog.getCreatedAt()));
            statement.executeUpdate();
        } catch (SQLException ex) {
            throw new IllegalStateException("Failed to save audit log", ex);
        }
    }

    @Override
    public List<AuditLog> findAll() {
        String sql = "SELECT id, booking_id, message, created_at FROM audit_logs ORDER BY created_at DESC, id DESC";
        List<AuditLog> logs = new ArrayList<>();
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
                logs.add(mapRow(rs));
            }
            return logs;
        } catch (SQLException ex) {
            throw new IllegalStateException("Failed to list audit logs", ex);
        }
    }

    private AuditLog mapRow(ResultSet rs) throws SQLException {
        long bookingId = rs.getLong("booking_id");
        Long nullableBookingId = rs.wasNull() ? null : bookingId;
        Timestamp createdAt = rs.getTimestamp("created_at");
        return new AuditLog(
                rs.getLong("id"),
                nullableBookingId,
                rs.getString("message"),
                createdAt == null ? null : createdAt.toLocalDateTime()
        );
    }
}
