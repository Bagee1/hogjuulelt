package mn.edu.room.student.core.service;

import mn.edu.room.student.core.domain.Student;
import mn.edu.room.student.core.dto.LoginRequest;
import mn.edu.room.student.core.dto.StudentCreateRequest;
import mn.edu.room.student.core.dto.StudentResponse;
import mn.edu.room.student.core.port.in.StudentUseCase;
import mn.edu.room.student.core.port.out.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService implements StudentUseCase {
    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public List<StudentResponse> list() {
        return studentRepository.findAll().stream()
                .map(StudentResponse::from)
                .toList();
    }

    @Override
    public StudentResponse get(Long id) {
        return studentRepository.findById(id)
                .map(StudentResponse::from)
                .orElseThrow(() -> new IllegalArgumentException("Student not found: " + id));
    }

    @Override
    public StudentResponse create(StudentCreateRequest request) {
        studentRepository.findByUsername(request.username()).ifPresent(existing -> {
            throw new IllegalArgumentException("Username already exists");
        });
        Student saved = studentRepository.save(Student.create(
                request.username(),
                request.password(),
                request.firstName(),
                request.lastName(),
                request.email(),
                request.matriculationNumber()
        ));
        return StudentResponse.from(saved);
    }

    @Override
    public StudentResponse login(LoginRequest request) {
        Student student = studentRepository.findByUsername(request.username())
                .orElseThrow(() -> new IllegalArgumentException("Invalid username or password"));
        if (!student.passwordMatches(request.password())) {
            throw new IllegalArgumentException("Invalid username or password");
        }
        return StudentResponse.from(student);
    }
}
