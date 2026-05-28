package mn.edu.room.notification.adapter.out.jdbc;

import mn.edu.room.notification.core.domain.NotificationLog;
import mn.edu.room.notification.core.port.out.NotificationRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;

@Repository
public class JdbcNotificationRepository implements NotificationRepository {
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<NotificationLog> mapper = (rs, rowNum) -> new NotificationLog(
            rs.getLong("id"),
            rs.getLong("booking_id"),
            rs.getLong("student_id"),
            rs.getString("event_type"),
            rs.getString("message"),
            rs.getTimestamp("created_at").toInstant()
    );

    public JdbcNotificationRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public NotificationLog save(NotificationLog log) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement("""
                    INSERT INTO notification_logs (booking_id, student_id, event_type, message, created_at)
                    VALUES (?, ?, ?, ?, ?)
                    """, new String[]{"id"});
            ps.setLong(1, log.bookingId());
            ps.setLong(2, log.studentId());
            ps.setString(3, log.eventType());
            ps.setString(4, log.message());
            ps.setTimestamp(5, Timestamp.from(log.createdAt()));
            return ps;
        }, keyHolder);
        Long id = Objects.requireNonNull(keyHolder.getKey()).longValue();
        return new NotificationLog(id, log.bookingId(), log.studentId(), log.eventType(), log.message(), log.createdAt());
    }

    @Override
    public List<NotificationLog> findAll() {
        return jdbcTemplate.query("""
                SELECT id, booking_id, student_id, event_type, message, created_at
                FROM notification_logs
                ORDER BY id DESC
                """, mapper);
    }

    @Override
    public List<NotificationLog> findByStudentId(Long studentId) {
        return jdbcTemplate.query("""
                SELECT id, booking_id, student_id, event_type, message, created_at
                FROM notification_logs
                WHERE student_id = ?
                ORDER BY id DESC
                """, mapper, studentId);
    }
}
