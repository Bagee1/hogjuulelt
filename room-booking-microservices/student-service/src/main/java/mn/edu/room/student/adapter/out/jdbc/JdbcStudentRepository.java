package mn.edu.room.student.adapter.out.jdbc;

import mn.edu.room.student.core.domain.Role;
import mn.edu.room.student.core.domain.Student;
import mn.edu.room.student.core.port.out.StudentRepository;
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
public class JdbcStudentRepository implements StudentRepository {
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Student> mapper = (rs, rowNum) -> new Student(
            rs.getLong("id"),
            rs.getString("username"),
            rs.getString("password_hash"),
            Role.valueOf(rs.getString("role")),
            rs.getString("first_name"),
            rs.getString("last_name"),
            rs.getString("email"),
            rs.getString("matriculation_number")
    );

    public JdbcStudentRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Student> findAll() {
        return jdbcTemplate.query("""
                SELECT id, username, password_hash, role, first_name, last_name, email, matriculation_number
                FROM students
                ORDER BY id
                """, mapper);
    }

    @Override
    public Optional<Student> findById(Long id) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject("""
                    SELECT id, username, password_hash, role, first_name, last_name, email, matriculation_number
                    FROM students
                    WHERE id = ?
                    """, mapper, id));
        } catch (EmptyResultDataAccessException ex) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Student> findByUsername(String username) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject("""
                    SELECT id, username, password_hash, role, first_name, last_name, email, matriculation_number
                    FROM students
                    WHERE username = ?
                    """, mapper, username));
        } catch (EmptyResultDataAccessException ex) {
            return Optional.empty();
        }
    }

    @Override
    public Student save(Student student) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement("""
                    INSERT INTO students (username, password_hash, role, first_name, last_name, email, matriculation_number)
                    VALUES (?, ?, ?, ?, ?, ?, ?)
                    """, new String[]{"id"});
            ps.setString(1, student.username());
            ps.setString(2, student.password());
            ps.setString(3, student.role().name());
            ps.setString(4, student.firstName());
            ps.setString(5, student.lastName());
            ps.setString(6, student.email());
            ps.setString(7, student.matriculationNumber());
            return ps;
        }, keyHolder);
        Long id = Objects.requireNonNull(keyHolder.getKey()).longValue();
        return new Student(
                id,
                student.username(),
                student.password(),
                student.role(),
                student.firstName(),
                student.lastName(),
                student.email(),
                student.matriculationNumber()
        );
    }
}
