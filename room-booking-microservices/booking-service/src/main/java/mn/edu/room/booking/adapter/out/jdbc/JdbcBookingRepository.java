package mn.edu.room.booking.adapter.out.jdbc;

import mn.edu.room.booking.core.domain.Booking;
import mn.edu.room.booking.core.domain.BookingStatus;
import mn.edu.room.booking.core.port.out.BookingRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class JdbcBookingRepository implements BookingRepository {
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Booking> mapper = (rs, rowNum) -> new Booking(
            rs.getLong("id"),
            rs.getLong("room_id"),
            rs.getLong("student_id"),
            rs.getDate("booking_date").toLocalDate(),
            rs.getTime("start_time").toLocalTime(),
            rs.getTime("end_time").toLocalTime(),
            rs.getString("purpose"),
            BookingStatus.valueOf(rs.getString("status")),
            rs.getString("reject_reason"),
            rs.getTimestamp("created_at").toInstant()
    );

    public JdbcBookingRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Booking> findAll() {
        return jdbcTemplate.query("""
                SELECT id, room_id, student_id, booking_date, start_time, end_time, purpose, status, reject_reason, created_at
                FROM bookings
                ORDER BY id DESC
                """, mapper);
    }

    @Override
    public List<Booking> findByStudentId(Long studentId) {
        return jdbcTemplate.query("""
                SELECT id, room_id, student_id, booking_date, start_time, end_time, purpose, status, reject_reason, created_at
                FROM bookings
                WHERE student_id = ?
                ORDER BY id DESC
                """, mapper, studentId);
    }

    @Override
    public Optional<Booking> findById(Long id) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject("""
                    SELECT id, room_id, student_id, booking_date, start_time, end_time, purpose, status, reject_reason, created_at
                    FROM bookings
                    WHERE id = ?
                    """, mapper, id));
        } catch (EmptyResultDataAccessException ex) {
            return Optional.empty();
        }
    }

    @Override
    public List<Booking> findOverlapping(Long roomId, LocalDate date, LocalTime startTime, LocalTime endTime, Long excludeBookingId) {
        if (excludeBookingId == null) {
            return jdbcTemplate.query("""
                    SELECT id, room_id, student_id, booking_date, start_time, end_time, purpose, status, reject_reason, created_at
                    FROM bookings
                    WHERE room_id = ?
                      AND booking_date = ?
                      AND status IN ('PENDING', 'APPROVED')
                      AND start_time < ?
                      AND end_time > ?
                    """, mapper, roomId, Date.valueOf(date), Time.valueOf(endTime), Time.valueOf(startTime));
        }
        return jdbcTemplate.query("""
                SELECT id, room_id, student_id, booking_date, start_time, end_time, purpose, status, reject_reason, created_at
                FROM bookings
                WHERE room_id = ?
                  AND booking_date = ?
                  AND id <> ?
                  AND status IN ('PENDING', 'APPROVED')
                  AND start_time < ?
                  AND end_time > ?
                """, mapper, roomId, Date.valueOf(date), excludeBookingId, Time.valueOf(endTime), Time.valueOf(startTime));
    }

    @Override
    public Booking save(Booking booking) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement("""
                    INSERT INTO bookings (room_id, student_id, booking_date, start_time, end_time, purpose, status, reject_reason)
                    VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                    """, new String[]{"id"});
            bindBooking(ps, booking);
            return ps;
        }, keyHolder);
        Long id = Objects.requireNonNull(keyHolder.getKey()).longValue();
        return findById(id).orElseThrow();
    }

    @Override
    public Booking updateDetails(Booking booking) {
        int updated = jdbcTemplate.update("""
                UPDATE bookings
                SET room_id = ?,
                    booking_date = ?,
                    start_time = ?,
                    end_time = ?,
                    purpose = ?,
                    status = ?,
                    reject_reason = ?
                WHERE id = ?
                """,
                booking.roomId(),
                Date.valueOf(booking.bookingDate()),
                Time.valueOf(booking.startTime()),
                Time.valueOf(booking.endTime()),
                booking.purpose(),
                booking.status().name(),
                booking.rejectReason(),
                booking.id());
        if (updated == 0) {
            throw new IllegalArgumentException("Booking not found: " + booking.id());
        }
        return findById(booking.id()).orElseThrow();
    }

    @Override
    public Booking updateStatus(Long id, BookingStatus status, String rejectReason) {
        int updated = jdbcTemplate.update("""
                UPDATE bookings
                SET status = ?, reject_reason = ?
                WHERE id = ?
                """, status.name(), rejectReason, id);
        if (updated == 0) {
            throw new IllegalArgumentException("Booking not found: " + id);
        }
        return findById(id).orElseThrow();
    }

    @Override
    public void deleteById(Long id) {
        jdbcTemplate.update("DELETE FROM bookings WHERE id = ?", id);
    }

    private void bindBooking(PreparedStatement ps, Booking booking) throws java.sql.SQLException {
        ps.setLong(1, booking.roomId());
        ps.setLong(2, booking.studentId());
        ps.setDate(3, Date.valueOf(booking.bookingDate()));
        ps.setTime(4, Time.valueOf(booking.startTime()));
        ps.setTime(5, Time.valueOf(booking.endTime()));
        ps.setString(6, booking.purpose());
        ps.setString(7, booking.status().name());
        ps.setString(8, booking.rejectReason());
    }
}
