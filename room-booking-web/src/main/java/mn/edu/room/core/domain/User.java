package mn.edu.room.core.domain;

import java.util.Objects;

public class User {
    private final Long id;
    private final String username;
    private final String password;
    private final Role role;

    public User(Long id, String username, String password, Role role) {
        this.id = id;
        this.username = requireText(username, "username");
        this.password = requireText(password, "password");
        this.role = Objects.requireNonNull(role, "role");
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Role getRole() {
        return role;
    }

    public boolean hasRole(Role expectedRole) {
        return role == expectedRole;
    }

    private static String requireText(String value, String field) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(field + " is required");
        }
        return value.trim();
    }
}
