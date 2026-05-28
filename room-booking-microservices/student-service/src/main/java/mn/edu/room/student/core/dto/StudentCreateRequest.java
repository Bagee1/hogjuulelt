package mn.edu.room.student.core.dto;

public record StudentCreateRequest(
        String username,
        String password,
        String firstName,
        String lastName,
        String email,
        String matriculationNumber
) {
}
