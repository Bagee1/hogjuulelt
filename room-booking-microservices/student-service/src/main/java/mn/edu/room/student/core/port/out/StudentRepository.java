package mn.edu.room.student.core.port.out;

import mn.edu.room.student.core.domain.Student;

import java.util.List;
import java.util.Optional;

public interface StudentRepository {
    List<Student> findAll();

    Optional<Student> findById(Long id);

    Optional<Student> findByUsername(String username);

    Student save(Student student);
}
