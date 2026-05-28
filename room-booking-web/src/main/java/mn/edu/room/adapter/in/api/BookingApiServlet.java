package mn.edu.room.adapter.in.api;

import java.io.IOException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mn.edu.room.adapter.in.web.SessionSupport;
import mn.edu.room.core.dto.BookingCreateDto;
import mn.edu.room.core.dto.UserSessionDto;
import mn.edu.room.core.port.in.BookingUseCase;
import mn.edu.room.factory.ServiceFactory;

@WebServlet("/api/bookings")
public class BookingApiServlet extends HttpServlet {
    private BookingUseCase bookingUseCase;
    private ObjectMapper objectMapper;

    @Override
    public void init() {
        bookingUseCase = ServiceFactory.bookingUseCase();
        objectMapper = new ObjectMapper();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        UserSessionDto user = SessionSupport.currentUser(request);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        if (user.isAdmin()) {
            objectMapper.writeValue(response.getWriter(), bookingUseCase.listAllBookings());
        } else {
            objectMapper.writeValue(response.getWriter(), bookingUseCase.listMyBookings(user.id()));
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        UserSessionDto user = SessionSupport.currentUser(request);
        try {
            BookingCreateDto body = objectMapper.readValue(request.getInputStream(), BookingCreateDto.class);
            BookingCreateDto dto = new BookingCreateDto(
                    body.roomId(),
                    user.id(),
                    body.bookingDate(),
                    body.startTime(),
                    body.endTime(),
                    body.purpose()
            );
            response.setStatus(HttpServletResponse.SC_CREATED);
            objectMapper.writeValue(response.getWriter(), bookingUseCase.createBooking(dto));
        } catch (RuntimeException ex) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            objectMapper.writeValue(response.getWriter(), new ApiError(ex.getMessage()));
        }
    }

    private record ApiError(String message) {
    }
}
