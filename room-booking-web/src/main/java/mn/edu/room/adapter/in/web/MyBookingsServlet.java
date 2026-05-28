package mn.edu.room.adapter.in.web;

import java.io.IOException;
import java.time.LocalDate;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mn.edu.room.core.dto.BookingCreateDto;
import mn.edu.room.core.dto.BookingDecisionDto;
import mn.edu.room.core.dto.BookingUpdateDto;
import mn.edu.room.core.dto.UserSessionDto;
import mn.edu.room.core.port.in.BookingUseCase;
import mn.edu.room.core.port.in.RoomUseCase;
import mn.edu.room.factory.ServiceFactory;

@WebServlet("/bookings")
public class MyBookingsServlet extends HttpServlet {
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
        request.setAttribute("currentUser", user);
        request.setAttribute("rooms", roomUseCase.listRooms());
        request.setAttribute("bookings", bookingUseCase.listMyBookings(user.id()));
        request.setAttribute("minBookingDate", LocalDate.now().toString());
        request.setAttribute("defaultBookingDate", LocalDate.now().plusDays(1).toString());
        request.setAttribute("defaultStartTime", "09:00");
        request.setAttribute("defaultEndTime", "10:00");
        request.getRequestDispatcher(WebPaths.view("my-bookings")).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        UserSessionDto user = SessionSupport.currentUser(request);
        String action = request.getParameter("action");
        try {
            if ("update".equals(action)) {
                BookingUpdateDto dto = new BookingUpdateDto(
                        parseLong(request.getParameter("bookingId")),
                        parseLong(request.getParameter("roomId")),
                        user.id(),
                        request.getParameter("bookingDate"),
                        request.getParameter("startTime"),
                        request.getParameter("endTime"),
                        request.getParameter("purpose")
                );
                bookingUseCase.updateByStudent(dto);
                response.sendRedirect(request.getContextPath() + "/bookings?success=updated");
            } else if ("cancel".equals(action)) {
                BookingDecisionDto dto = new BookingDecisionDto(
                        parseLong(request.getParameter("bookingId")),
                        user.id(),
                        request.getParameter("cancelReason")
                );
                bookingUseCase.cancel(dto);
                response.sendRedirect(request.getContextPath() + "/bookings?success=cancelled");
            } else {
                BookingCreateDto dto = new BookingCreateDto(
                        parseLong(request.getParameter("roomId")),
                        user.id(),
                        request.getParameter("bookingDate"),
                        request.getParameter("startTime"),
                        request.getParameter("endTime"),
                        request.getParameter("purpose")
                );
                bookingUseCase.createBooking(dto);
                response.sendRedirect(request.getContextPath() + "/bookings?success=created");
            }
        } catch (RuntimeException ex) {
            request.setAttribute("error", ex.getMessage());
            doGet(request, response);
        }
    }

    private Long parseLong(String value) {
        try {
            return Long.valueOf(value);
        } catch (RuntimeException ex) {
            throw new IllegalArgumentException("Өрөө зөв сонгоно уу.");
        }
    }
}
