package mn.edu.room.room.adapter.out.jdbc;

import mn.edu.room.room.core.domain.Room;
import mn.edu.room.room.core.port.out.RoomRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class JdbcRoomRepository implements RoomRepository {
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Room> mapper = (rs, rowNum) -> new Room(
            rs.getLong("id"),
            rs.getString("name"),
            rs.getInt("capacity"),
            rs.getString("location")
    );

    public JdbcRoomRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Room> findAll() {
        return jdbcTemplate.query("""
                SELECT id, name, capacity, location
                FROM rooms
                ORDER BY id
                """, mapper);
    }

    @Override
    public Optional<Room> findById(Long id) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject("""
                    SELECT id, name, capacity, location
                    FROM rooms
                    WHERE id = ?
                    """, mapper, id));
        } catch (EmptyResultDataAccessException ex) {
            return Optional.empty();
        }
    }

    @Override
    public Room save(Room room) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement("""
                    INSERT INTO rooms (name, capacity, location)
                    VALUES (?, ?, ?)
                    """, new String[]{"id"});
            ps.setString(1, room.name());
            ps.setInt(2, room.capacity());
            ps.setString(3, room.location());
            return ps;
        }, keyHolder);
        Long id = Objects.requireNonNull(keyHolder.getKey()).longValue();
        return new Room(id, room.name(), room.capacity(), room.location());
    }
}
