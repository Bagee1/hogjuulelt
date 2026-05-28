package mn.edu.room.booking.core.dto;

public record StudentClientResponse(
        Long id,
        String username,
        String role,
        String firstName,
        String lastName,
        String email,
        String matriculationNumber
) {
}
