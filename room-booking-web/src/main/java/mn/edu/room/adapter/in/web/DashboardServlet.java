package mn.edu.room.adapter.in.web;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mn.edu.room.core.dto.BookingResponseDto;
import mn.edu.room.core.dto.UserSessionDto;
import mn.edu.room.core.port.in.BookingUseCase;
import mn.edu.room.core.port.in.RoomUseCase;
import mn.edu.room.factory.ServiceFactory;

@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {
    private BookingUseCase bookingUseCase;
    private RoomUseCase roomUseCase;

    @Override
    public void init() {
        bookingUseCase = ServiceFactory.bookingUseCase();
        roomUseCase = ServiceFactory.roomUseCase();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UserSessionDto user = SessionSupport.currentUser(request);
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/student-login");
            return;
        }
        var bookings = user.isAdmin() ? bookingUseCase.listAllBookings() : bookingUseCase.listMyBookings(user.id());
        long pendingCount = bookings.stream()
                .filter(booking -> "PENDING".equals(booking.status()))
                .count();

        request.setAttribute("currentUser", user);
        request.setAttribute("roomCount", roomUseCase.listRooms().size());
        request.setAttribute("bookingCount", bookings.size());
        request.setAttribute("pendingCount", pendingCount);
        request.setAttribute("recentBookings", bookings.stream().limit(5).toList());
        request.getRequestDispatcher(WebPaths.view("dashboard")).forward(request, response);
    }
}
