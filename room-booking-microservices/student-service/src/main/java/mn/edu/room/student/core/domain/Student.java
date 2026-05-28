package mn.edu.room.student.core.domain;

import java.util.Objects;

public record Student(
        Long id,
        String username,
        String password,
        Role role,
        String firstName,
        String lastName,
        String email,
        String matriculationNumber
) {
    public Student {
        username = requireText(username, "username");
        password = requireText(password, "password");
        role = Objects.requireNonNull(role, "role");
        firstName = requireText(firstName, "firstName");
        lastName = requireText(lastName, "lastName");
        email = requireText(email, "email");
        matriculationNumber = matriculationNumber == null ? "" : matriculationNumber.trim();
    }

    public static Student create(
            String username,
            String password,
            String firstName,
            String lastName,
            String email,
            String matriculationNumber
    ) {
        return new Student(null, username, password, Role.STUDENT, firstName, lastName, email, matriculationNumber);
    }

    public boolean passwordMatches(String candidate) {
        return password.equals(candidate);
    }

    private static String requireText(String value, String field) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(field + " is required");
        }
        return value.trim();
    }
}
