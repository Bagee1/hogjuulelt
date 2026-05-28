package mn.edu.room.adapter.in.web;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mn.edu.room.core.dto.RegisterDto;
import mn.edu.room.core.dto.UserSessionDto;
import mn.edu.room.core.port.in.AuthUseCase;
import mn.edu.room.factory.ServiceFactory;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    private AuthUseCase authUseCase;

    @Override
    public void init() {
        authUseCase = ServiceFactory.authUseCase();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UserSessionDto currentUser = SessionSupport.currentUser(request);
        if (currentUser != null) {
            response.sendRedirect(request.getContextPath() + (currentUser.isAdmin() ? "/admin/bookings" : "/dashboard"));
            return;
        }
        request.getRequestDispatcher(WebPaths.view("register")).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            RegisterDto dto = new RegisterDto(
                    request.getParameter("username"),
                    request.getParameter("password"),
                    request.getParameter("confirmPassword")
            );
            UserSessionDto user = authUseCase.registerStudent(dto);
            request.getSession(true).setAttribute(SessionSupport.CURRENT_USER, user);
            response.sendRedirect(request.getContextPath() + "/dashboard");
        } catch (RuntimeException ex) {
            request.setAttribute("error", ex.getMessage());
            request.getRequestDispatcher(WebPaths.view("register")).forward(request, response);
        }
    }
}
