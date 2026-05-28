package mn.edu.room.adapter.in.web;

import java.io.IOException;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mn.edu.room.core.dto.UserSessionDto;

@WebFilter(urlPatterns = {"/dashboard", "/rooms", "/bookings", "/admin/*", "/api/*"})
public class AuthFilter extends HttpFilter implements Filter {
    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        UserSessionDto user = SessionSupport.currentUser(request);
        if (user == null) {
            String loginPath = request.getServletPath().startsWith("/admin") ? "/admin-login" : "/student-login";
            response.sendRedirect(request.getContextPath() + loginPath);
            return;
        }
        String path = request.getServletPath();
        if (path.startsWith("/admin") && !user.isAdmin()) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
        chain.doFilter(request, response);
    }
}
