package mn.edu.room.core.dto;

public record UserSessionDto(Long id, String username, String role) {
    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getRole() {
        return role;
    }

    public boolean isAdmin() {
        return "ADMIN".equals(role);
    }

    public String getRoleLabel() {
        return isAdmin() ? "Админ" : "Оюутан";
    }
}
