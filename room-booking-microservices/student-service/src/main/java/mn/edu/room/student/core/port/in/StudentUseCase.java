package mn.edu.room.student.core.port.in;

import mn.edu.room.student.core.dto.LoginRequest;
import mn.edu.room.student.core.dto.StudentCreateRequest;
import mn.edu.room.student.core.dto.StudentResponse;

import java.util.List;

public interface StudentUseCase {
    List<StudentResponse> list();

    StudentResponse get(Long id);

    StudentResponse create(StudentCreateRequest request);

    StudentResponse login(LoginRequest request);
}
