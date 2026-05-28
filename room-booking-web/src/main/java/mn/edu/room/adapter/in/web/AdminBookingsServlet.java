package mn.edu.room.adapter.in.web;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mn.edu.room.core.dto.BookingDecisionDto;
import mn.edu.room.core.dto.BookingUpdateDto;
import mn.edu.room.core.dto.UserSessionDto;
import mn.edu.room.core.port.in.BookingUseCase;
import mn.edu.room.core.port.in.RoomUseCase;
import mn.edu.room.factory.ServiceFactory;

@WebServlet("/admin/bookings")
public class AdminBookingsServlet extends HttpServlet {
    private BookingUseCase bookingUseCase;
    private RoomUseCase roomUseCase;

    @Override
    public void init() {
        bookingUseCase = ServiceFactory.bookingUseCase();
        roomUseCase = ServiceFactory.roomUseCase();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("currentUser", SessionSupport.currentUser(request));
        request.setAttribute("bookings", bookingUseCase.listAllBookings());
        request.setAttribute("rooms", roomUseCase.listRooms());
        request.getRequestDispatcher(WebPaths.view("admin-bookings")).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        UserSessionDto user = SessionSupport.currentUser(request);
        String action = request.getParameter("action");
        try {
            BookingDecisionDto dto = new BookingDecisionDto(
                    parseLong(request.getParameter("bookingId")),
                    user.id(),
                    request.getParameter("rejectReason")
            );
            if ("approve".equals(action)) {
                bookingUseCase.approve(dto);
                response.sendRedirect(request.getContextPath() + "/admin/bookings?success=approved");
            } else if ("reject".equals(action)) {
                bookingUseCase.reject(dto);
                response.sendRedirect(request.getContextPath() + "/admin/bookings?success=rejected");
            } else if ("cancel".equals(action)) {
                bookingUseCase.cancel(new BookingDecisionDto(dto.bookingId(), user.id(), request.getParameter("cancelReason")));
                response.sendRedirect(request.getContextPath() + "/admin/bookings?success=cancelled");
            } else if ("delete".equals(action)) {
                bookingUseCase.delete(new BookingDecisionDto(dto.bookingId(), user.id(), null));
                response.sendRedirect(request.getContextPath() + "/admin/bookings?success=deleted");
            } else if ("update".equals(action)) {
                BookingUpdateDto updateDto = new BookingUpdateDto(
                        dto.bookingId(),
                        parseLong(request.getParameter("roomId")),
                        user.id(),
                        request.getParameter("bookingDate"),
                        request.getParameter("startTime"),
                        request.getParameter("endTime"),
                        request.getParameter("purpose")
                );
                bookingUseCase.updateByAdmin(updateDto);
                response.sendRedirect(request.getContextPath() + "/admin/bookings?success=updated");
            } else {
                throw new IllegalArgumentException("Тодорхойгүй үйлдэл байна.");
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
            throw new IllegalArgumentException("Захиалга зөв сонгоно уу.");
        }
    }
}
