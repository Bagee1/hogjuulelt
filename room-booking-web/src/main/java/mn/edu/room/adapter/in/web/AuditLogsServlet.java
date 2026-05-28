package mn.edu.room.adapter.in.web;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mn.edu.room.core.port.in.AuditLogUseCase;
import mn.edu.room.factory.ServiceFactory;

@WebServlet("/admin/audit-logs")
public class AuditLogsServlet extends HttpServlet {
    private AuditLogUseCase auditLogUseCase;

    @Override
    public void init() {
        auditLogUseCase = ServiceFactory.auditLogUseCase();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("currentUser", SessionSupport.currentUser(request));
        request.setAttribute("logs", auditLogUseCase.listAuditLogs());
        request.getRequestDispatcher(WebPaths.view("audit-logs")).forward(request, response);
    }
}
