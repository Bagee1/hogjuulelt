package mn.edu.room.adapter.in.web;

import java.io.IOException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import mn.edu.room.core.dto.UserSessionDto;

@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        UserSessionDto user = SessionSupport.currentUser(request);
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        String loginPath = user != null && user.isAdmin() ? "/admin-login" : "/student-login";
        response.sendRedirect(request.getContextPath() + loginPath);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        doPost(request, response);
    }
}
