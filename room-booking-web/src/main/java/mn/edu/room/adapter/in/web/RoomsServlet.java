package mn.edu.room.adapter.in.web;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mn.edu.room.core.dto.RoomCreateDto;
import mn.edu.room.core.dto.UserSessionDto;
import mn.edu.room.core.port.in.RoomUseCase;
import mn.edu.room.factory.ServiceFactory;

@WebServlet("/rooms")
public class RoomsServlet extends HttpServlet {
    private RoomUseCase roomUseCase;

    @Override
    public void init() {
        roomUseCase = ServiceFactory.roomUseCase();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("currentUser", SessionSupport.currentUser(request));
        request.setAttribute("rooms", roomUseCase.listRooms());
        request.getRequestDispatcher(WebPaths.view("rooms")).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        UserSessionDto user = SessionSupport.currentUser(request);
        if (user == null || !user.isAdmin()) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
        try {
            RoomCreateDto dto = new RoomCreateDto(
                    request.getParameter("name"),
                    parseInt(request.getParameter("capacity")),
                    request.getParameter("location")
            );
            roomUseCase.addRoom(dto);
            response.sendRedirect(request.getContextPath() + "/rooms?success=created");
        } catch (RuntimeException ex) {
            request.setAttribute("error", ex.getMessage());
            doGet(request, response);
        }
    }

    private int parseInt(String value) {
        try {
            return Integer.parseInt(value);
        } catch (RuntimeException ex) {
            throw new IllegalArgumentException("Багтаамж зөв тоо байх ёстой.");
        }
    }
}
