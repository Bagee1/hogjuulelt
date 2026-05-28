package mn.edu.room.adapter.out.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;
import mn.edu.room.config.DatabaseConnection;
import mn.edu.room.core.domain.Role;
import mn.edu.room.core.domain.User;
import mn.edu.room.core.port.out.UserRepository;

public class JdbcUserRepository implements UserRepository {
    private final DatabaseConnection databaseConnection;

    public JdbcUserRepository(DatabaseConnection databaseConnection) {
        this.databaseConnection = databaseConnection;
    }

    @Override
    public User save(User user) {
        String sql = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getRole().name());
            statement.executeUpdate();
            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    return new User(keys.getLong(1), user.getUsername(), user.getPassword(), user.getRole());
                }
                throw new IllegalStateException("Бүртгэлийн дугаар үүссэнгүй.");
            }
        } catch (SQLException ex) {
            throw new IllegalStateException("Бүртгэл хадгалахад алдаа гарлаа: " + ex.getMessage(), ex);
        }
    }

    @Override
    public Optional<User> findByUsername(String username) {
        String sql = "SELECT id, username, password, role FROM users WHERE username = ?";
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
                return Optional.empty();
            }
        } catch (SQLException ex) {
            throw new IllegalStateException("Failed to find user by username: " + ex.getMessage(), ex);
        }
    }

    @Override
    public Optional<User> findById(Long id) {
        String sql = "SELECT id, username, password, role FROM users WHERE id = ?";
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
            throw new IllegalStateException("Failed to find user by id: " + ex.getMessage(), ex);
        }
    }

    private User mapRow(ResultSet rs) throws SQLException {
        return new User(
                rs.getLong("id"),
                rs.getString("username"),
                rs.getString("password"),
                Role.valueOf(rs.getString("role"))
        );
    }
}
