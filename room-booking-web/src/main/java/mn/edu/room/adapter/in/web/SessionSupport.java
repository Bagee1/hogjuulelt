package mn.edu.room.adapter.in.web;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import mn.edu.room.core.dto.UserSessionDto;

public final class SessionSupport {
    public static final String CURRENT_USER = "currentUser";

    private SessionSupport() {
    }

    public static UserSessionDto currentUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return null;
        }
        Object value = session.getAttribute(CURRENT_USER);
        return value instanceof UserSessionDto user ? user : null;
    }
}
