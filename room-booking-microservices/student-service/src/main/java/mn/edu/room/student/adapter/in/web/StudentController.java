package mn.edu.room.student.adapter.in.web;

import mn.edu.room.student.core.dto.LoginRequest;
import mn.edu.room.student.core.dto.StudentCreateRequest;
import mn.edu.room.student.core.dto.StudentResponse;
import mn.edu.room.student.core.port.in.StudentUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
public class StudentController {
    private final StudentUseCase studentUseCase;

    public StudentController(StudentUseCase studentUseCase) {
        this.studentUseCase = studentUseCase;
    }

    @GetMapping("/students")
    public List<StudentResponse> list() {
        return studentUseCase.list();
    }

    @GetMapping("/students/{id}")
    public StudentResponse get(@PathVariable Long id) {
        try {
            return studentUseCase.get(id);
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage(), ex);
        }
    }

    @PostMapping("/students")
    @ResponseStatus(HttpStatus.CREATED)
    public StudentResponse create(@RequestBody StudentCreateRequest request) {
        try {
            return studentUseCase.create(request);
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage(), ex);
        }
    }

    @PostMapping("/auth/login")
    public StudentResponse login(@RequestBody LoginRequest request) {
        try {
            return studentUseCase.login(request);
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, ex.getMessage(), ex);
        }
    }
}
