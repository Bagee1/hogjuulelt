package mn.edu.room.adapter.in.web;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mn.edu.room.core.dto.LoginDto;
import mn.edu.room.core.dto.UserSessionDto;
import mn.edu.room.core.port.in.AuthUseCase;
import mn.edu.room.factory.ServiceFactory;

@WebServlet({"/login", "/student-login", "/admin-login"})
public class LoginServlet extends HttpServlet {
    private static final String ADMIN_MODE = "admin";
    private static final String STUDENT_MODE = "student";

    private AuthUseCase authUseCase;

    @Override
    public void init() {
        authUseCase = ServiceFactory.authUseCase();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if ("/login".equals(request.getServletPath())) {
            response.sendRedirect(request.getContextPath() + "/student-login");
            return;
        }
        UserSessionDto currentUser = SessionSupport.currentUser(request);
        if (currentUser != null) {
            redirectToWorkspace(request, response, currentUser);
            return;
        }
        prepareLoginPage(request);
        request.getRequestDispatcher(WebPaths.view("login")).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String loginMode = resolveLoginMode(request);
        try {
            LoginDto loginDto = new LoginDto(request.getParameter("username"), request.getParameter("password"));
            UserSessionDto user = authUseCase.login(loginDto);
            validateRole(loginMode, user);
            request.getSession(true).setAttribute(SessionSupport.CURRENT_USER, user);
            redirectToWorkspace(request, response, user);
        } catch (RuntimeException ex) {
            request.setAttribute("error", ex.getMessage());
            prepareLoginPage(request);
            request.getRequestDispatcher(WebPaths.view("login")).forward(request, response);
        }
    }

    private static void prepareLoginPage(HttpServletRequest request) {
        String loginMode = resolveLoginMode(request);
        boolean adminMode = ADMIN_MODE.equals(loginMode);
        request.setAttribute("loginMode", loginMode);
        request.setAttribute("loginPath", adminMode ? "/admin-login" : "/student-login");
        request.setAttribute("alternateLoginPath", adminMode ? "/student-login" : "/admin-login");
        request.setAttribute("alternateLoginLabel", adminMode ? "Оюутнаар нэвтрэх" : "Админаар нэвтрэх");
        request.setAttribute("loginKicker", adminMode ? "Админ нэвтрэлт" : "Оюутны нэвтрэлт");
        request.setAttribute("loginTitle", adminMode ? "Захиалгын хүсэлтүүдийг хянана." : "Өрөө захиалах хүсэлтээ илгээнэ.");
        request.setAttribute("loginSubtitle", adminMode
                ? "Админ эрхээр орж хүсэлтийг зөвшөөрөх, татгалзах, үйлдлийн түүхийг харах боломжтой."
                : "Оюутан эрхээр орж өрөө сонгоод цагийн хүсэлтээ илгээж, төлөвөө хянана.");
        request.setAttribute("loginHeading", adminMode ? "Админ нэвтрэх" : "Оюутан нэвтрэх");
        request.setAttribute("demoTitle", adminMode ? "Админы туршилтын эрх" : "Оюутны туршилтын эрх");
        request.setAttribute("demoCredentials", adminMode ? "admin / 123" : "student / 123");
        request.setAttribute("usernameHint", adminMode ? "admin" : "student");
    }

    private static String resolveLoginMode(HttpServletRequest request) {
        return "/admin-login".equals(request.getServletPath()) ? ADMIN_MODE : STUDENT_MODE;
    }

    private static void validateRole(String loginMode, UserSessionDto user) {
        if (ADMIN_MODE.equals(loginMode) && !user.isAdmin()) {
            throw new IllegalArgumentException("Энэ хэсэгт зөвхөн админ эрхээр нэвтэрнэ.");
        }
        if (STUDENT_MODE.equals(loginMode) && user.isAdmin()) {
            throw new IllegalArgumentException("Админ хэрэглэгч /admin-login хэсгээр нэвтэрнэ.");
        }
    }

    private static void redirectToWorkspace(HttpServletRequest request, HttpServletResponse response, UserSessionDto user)
            throws IOException {
        String path = user.isAdmin() ? "/admin/bookings" : "/dashboard";
        response.sendRedirect(request.getContextPath() + path);
    }
}
