package mn.edu.room.student.core.dto;

import mn.edu.room.student.core.domain.Role;
import mn.edu.room.student.core.domain.Student;

public record StudentResponse(
        Long id,
        String username,
        Role role,
        String firstName,
        String lastName,
        String email,
        String matriculationNumber
) {
    public static StudentResponse from(Student student) {
        return new StudentResponse(
                student.id(),
                student.username(),
                student.role(),
                student.firstName(),
                student.lastName(),
                student.email(),
                student.matriculationNumber()
        );
    }
}
