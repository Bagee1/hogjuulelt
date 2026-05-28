package mn.edu.room.adapter.out.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import mn.edu.room.config.DatabaseConnection;
import mn.edu.room.core.domain.Room;
import mn.edu.room.core.port.out.RoomRepository;

public class JdbcRoomRepository implements RoomRepository {
    private final DatabaseConnection databaseConnection;

    public JdbcRoomRepository(DatabaseConnection databaseConnection) {
        this.databaseConnection = databaseConnection;
    }

    @Override
    public Room save(Room room) {
        String sql = "INSERT INTO rooms (name, capacity, location) VALUES (?, ?, ?)";
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, room.getName());
            statement.setInt(2, room.getCapacity());
            statement.setString(3, room.getLocation());
            statement.executeUpdate();
            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    return new Room(keys.getLong(1), room.getName(), room.getCapacity(), room.getLocation());
                }
                throw new IllegalStateException("Room insert did not return an id");
            }
        } catch (SQLException ex) {
            throw new IllegalStateException("Failed to save room", ex);
        }
    }

    @Override
    public List<Room> findAll() {
        String sql = "SELECT id, name, capacity, location FROM rooms ORDER BY name";
        List<Room> rooms = new ArrayList<>();
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
                rooms.add(mapRow(rs));
            }
            return rooms;
        } catch (SQLException ex) {
            throw new IllegalStateException("Failed to list rooms", ex);
        }
    }

    @Override
    public Optional<Room> findById(Long id) {
        String sql = "SELECT id, name, capacity, location FROM rooms WHERE id = ?";
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
            throw new IllegalStateException("Failed to find room by id", ex);
        }
    }

    private Room mapRow(ResultSet rs) throws SQLException {
        return new Room(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getInt("capacity"),
                rs.getString("location")
        );
    }
}
