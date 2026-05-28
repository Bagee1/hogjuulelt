package mn.edu.room.adapter.out.jdbc;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import mn.edu.room.config.DatabaseConnection;
import mn.edu.room.core.domain.Booking;
import mn.edu.room.core.domain.BookingStatus;
import mn.edu.room.core.port.out.BookingRepository;

public class JdbcBookingRepository implements BookingRepository {
    private final DatabaseConnection databaseConnection;

    public JdbcBookingRepository(DatabaseConnection databaseConnection) {
        this.databaseConnection = databaseConnection;
    }

    @Override
    public Booking save(Booking booking) {
        String sql = """
                INSERT INTO bookings (room_id, user_id, booking_date, start_time, end_time, purpose, status, reject_reason)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                """;
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            bindBooking(statement, booking);
            statement.executeUpdate();
            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    return new Booking(
                            keys.getLong(1),
                            booking.getRoomId(),
                            booking.getUserId(),
                            booking.getBookingDate(),
                            booking.getStartTime(),
                            booking.getEndTime(),
                            booking.getPurpose(),
                            booking.getStatus(),
                            booking.getRejectReason()
                    );
                }
                throw new IllegalStateException("Booking insert did not return an id");
            }
        } catch (SQLException ex) {
            throw new IllegalStateException("Failed to save booking", ex);
        }
    }

    @Override
    public void updateDetails(Booking booking) {
        String sql = """
                UPDATE bookings
                SET room_id = ?, booking_date = ?, start_time = ?, end_time = ?, purpose = ?, status = ?, reject_reason = ?
                WHERE id = ?
                """;
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, booking.getRoomId());
            statement.setDate(2, Date.valueOf(booking.getBookingDate()));
            statement.setTime(3, Time.valueOf(booking.getStartTime()));
            statement.setTime(4, Time.valueOf(booking.getEndTime()));
            statement.setString(5, booking.getPurpose());
            statement.setString(6, booking.getStatus().name());
            statement.setString(7, booking.getRejectReason());
            statement.setLong(8, booking.getId());
            statement.executeUpdate();
        } catch (SQLException ex) {
            throw new IllegalStateException("Failed to update booking details", ex);
        }
    }

    @Override
    public void updateStatus(Long bookingId, BookingStatus status, String rejectReason) {
        String sql = "UPDATE bookings SET status = ?, reject_reason = ? WHERE id = ?";
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, status.name());
            statement.setString(2, rejectReason);
            statement.setLong(3, bookingId);
            statement.executeUpdate();
        } catch (SQLException ex) {
            throw new IllegalStateException("Failed to update booking status", ex);
        }
    }

    @Override
    public void deleteById(Long bookingId) {
        String deleteLogsSql = "DELETE FROM audit_logs WHERE booking_id = ?";
        String deleteBookingSql = "DELETE FROM bookings WHERE id = ?";
        try (Connection connection = databaseConnection.getConnection()) {
            boolean autoCommit = connection.getAutoCommit();
            connection.setAutoCommit(false);
            try (PreparedStatement logStatement = connection.prepareStatement(deleteLogsSql);
                 PreparedStatement bookingStatement = connection.prepareStatement(deleteBookingSql)) {
                logStatement.setLong(1, bookingId);
                logStatement.executeUpdate();
                bookingStatement.setLong(1, bookingId);
                bookingStatement.executeUpdate();
                connection.commit();
            } catch (SQLException ex) {
                connection.rollback();
                throw ex;
            } finally {
                connection.setAutoCommit(autoCommit);
            }
        } catch (SQLException ex) {
            throw new IllegalStateException("Failed to delete booking", ex);
        }
    }

    @Override
    public Optional<Booking> findById(Long id) {
        String sql = baseSelect() + " WHERE id = ?";
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
                return Optional.empty();
            }
        } catch (SQLException ex) {
            throw new IllegalStateException("Failed to find booking by id", ex);
        }
    }

    @Override
    public List<Booking> findByUserId(Long userId) {
        String sql = baseSelect() + " WHERE user_id = ? ORDER BY booking_date DESC, start_time DESC";
        List<Booking> bookings = new ArrayList<>();
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, userId);
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    bookings.add(mapRow(rs));
                }
            }
            return bookings;
        } catch (SQLException ex) {
            throw new IllegalStateException("Failed to list user bookings", ex);
        }
    }

    @Override
    public List<Booking> findAll() {
        String sql = baseSelect() + " ORDER BY booking_date DESC, start_time DESC, id DESC";
        List<Booking> bookings = new ArrayList<>();
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
                bookings.add(mapRow(rs));
            }
            return bookings;
        } catch (SQLException ex) {
            throw new IllegalStateException("Failed to list bookings", ex);
        }
    }

    @Override
    public boolean hasApprovedOverlap(Long roomId, LocalDate bookingDate, LocalTime startTime, LocalTime endTime, Long excludedBookingId) {
        String sql = """
                SELECT COUNT(*) FROM bookings
                WHERE room_id = ?
                  AND booking_date = ?
                  AND status = 'APPROVED'
                  AND start_time < ?
                  AND end_time > ?
                  AND (? IS NULL OR id <> ?)
                """;
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, roomId);
            statement.setDate(2, Date.valueOf(bookingDate));
            statement.setTime(3, Time.valueOf(endTime));
            statement.setTime(4, Time.valueOf(startTime));
            if (excludedBookingId == null) {
                statement.setObject(5, null);
                statement.setObject(6, null);
            } else {
                statement.setLong(5, excludedBookingId);
                statement.setLong(6, excludedBookingId);
            }
            try (ResultSet rs = statement.executeQuery()) {
                return rs.next() && rs.getLong(1) > 0;
            }
        } catch (SQLException ex) {
            throw new IllegalStateException("Failed to check booking overlap", ex);
        }
    }

    private String baseSelect() {
        return "SELECT id, room_id, user_id, booking_date, start_time, end_time, purpose, status, reject_reason FROM bookings";
    }

    private void bindBooking(PreparedStatement statement, Booking booking) throws SQLException {
        statement.setLong(1, booking.getRoomId());
        statement.setLong(2, booking.getUserId());
        statement.setDate(3, Date.valueOf(booking.getBookingDate()));
        statement.setTime(4, Time.valueOf(booking.getStartTime()));
        statement.setTime(5, Time.valueOf(booking.getEndTime()));
        statement.setString(6, booking.getPurpose());
        statement.setString(7, booking.getStatus().name());
        statement.setString(8, booking.getRejectReason());
    }

    private Booking mapRow(ResultSet rs) throws SQLException {
        return new Booking(
                rs.getLong("id"),
                rs.getLong("room_id"),
                rs.getLong("user_id"),
                rs.getDate("booking_date").toLocalDate(),
                rs.getTime("start_time").toLocalTime(),
                rs.getTime("end_time").toLocalTime(),
                rs.getString("purpose"),
                BookingStatus.valueOf(rs.getString("status")),
                rs.getString("reject_reason")
        );
    }
}
