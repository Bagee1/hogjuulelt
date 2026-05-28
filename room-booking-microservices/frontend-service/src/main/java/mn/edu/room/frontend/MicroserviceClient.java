package mn.edu.room.frontend;

import mn.edu.room.frontend.Dto.BookingCreateRequest;
import mn.edu.room.frontend.Dto.BookingDecisionRequest;
import mn.edu.room.frontend.Dto.BookingResponse;
import mn.edu.room.frontend.Dto.NotificationResponse;
import mn.edu.room.frontend.Dto.RoomCreateRequest;
import mn.edu.room.frontend.Dto.RoomResponse;
import mn.edu.room.frontend.Dto.StudentResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
public class MicroserviceClient {
    private final RestTemplate restTemplate;
    private final String studentUrl;
    private final String roomUrl;
    private final String bookingUrl;
    private final String notificationUrl;

    public MicroserviceClient(
            RestTemplate restTemplate,
            @Value("${services.student-url}") String studentUrl,
            @Value("${services.room-url}") String roomUrl,
            @Value("${services.booking-url}") String bookingUrl,
            @Value("${services.notification-url}") String notificationUrl
    ) {
        this.restTemplate = restTemplate;
        this.studentUrl = studentUrl;
        this.roomUrl = roomUrl;
        this.bookingUrl = bookingUrl;
        this.notificationUrl = notificationUrl;
    }

    public List<StudentResponse> students() {
        return getList(studentUrl + "/students", new ParameterizedTypeReference<>() {
        });
    }

    public List<RoomResponse> rooms() {
        return getList(roomUrl + "/rooms", new ParameterizedTypeReference<>() {
        });
    }

    public List<BookingResponse> bookings() {
        return getList(bookingUrl + "/bookings", new ParameterizedTypeReference<>() {
        });
    }

    public List<NotificationResponse> notifications() {
        return getList(notificationUrl + "/notifications", new ParameterizedTypeReference<>() {
        });
    }

    public void createBooking(BookingCreateRequest request) {
        restTemplate.postForEntity(bookingUrl + "/bookings", request, BookingResponse.class);
    }

    public void approveBooking(Long bookingId, Long adminId) {
        restTemplate.postForEntity(bookingUrl + "/bookings/" + bookingId + "/approve",
                new BookingDecisionRequest(adminId, null),
                BookingResponse.class);
    }

    public void rejectBooking(Long bookingId, Long adminId, String rejectReason) {
        restTemplate.postForEntity(bookingUrl + "/bookings/" + bookingId + "/reject",
                new BookingDecisionRequest(adminId, rejectReason),
                BookingResponse.class);
    }

    public void cancelBooking(Long bookingId) {
        restTemplate.postForEntity(bookingUrl + "/bookings/" + bookingId + "/cancel", null, BookingResponse.class);
    }

    public void deleteBooking(Long bookingId) {
        restTemplate.delete(bookingUrl + "/bookings/" + bookingId);
    }

    public void createRoom(RoomCreateRequest request) {
        restTemplate.postForEntity(roomUrl + "/rooms", request, RoomResponse.class);
    }

    public String messageFrom(Throwable throwable) {
        if (throwable instanceof RestClientResponseException ex && !ex.getResponseBodyAsString().isBlank()) {
            return ex.getStatusCode() + ": " + ex.getResponseBodyAsString();
        }
        return throwable.getMessage() == null ? "Үйлдэл амжилтгүй боллоо." : throwable.getMessage();
    }

    private <T> List<T> getList(String url, ParameterizedTypeReference<List<T>> type) {
        return restTemplate.exchange(url, HttpMethod.GET, HttpEntity.EMPTY, type).getBody();
    }
}
