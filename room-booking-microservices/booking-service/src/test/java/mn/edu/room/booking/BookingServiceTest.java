package mn.edu.room.booking;

import mn.edu.room.booking.core.domain.Booking;
import mn.edu.room.booking.core.domain.BookingStatus;
import mn.edu.room.booking.core.dto.BookingCreateRequest;
import mn.edu.room.booking.core.dto.NotificationRequest;
import mn.edu.room.booking.core.dto.RoomClientResponse;
import mn.edu.room.booking.core.dto.StudentClientResponse;
import mn.edu.room.booking.core.port.out.BookingRepository;
import mn.edu.room.booking.core.port.out.NotificationClient;
import mn.edu.room.booking.core.port.out.RoomClient;
import mn.edu.room.booking.core.port.out.StudentClient;
import mn.edu.room.booking.core.service.BookingService;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BookingServiceTest {
    @Test
    void createsBookingAfterCheckingStudentAndRoom() {
        FakeBookingRepository bookings = new FakeBookingRepository();
        FakeNotificationClient notifications = new FakeNotificationClient();
        BookingService service = new BookingService(
                bookings,
                id -> Optional.of(new StudentClientResponse(id, "student", "STUDENT", "Demo", "Student", "student@university.edu", "S-1001")),
                id -> Optional.of(new RoomClientResponse(id, "A-101", 30, "Main")),
                notifications
        );

        var response = service.create(new BookingCreateRequest(
                1L,
                1L,
                LocalDate.of(2026, 6, 1),
                LocalTime.of(9, 0),
                LocalTime.of(10, 0),
                "Seminar"
        ));

        assertEquals(BookingStatus.PENDING, response.status());
        assertEquals(1, bookings.findAll().size());
        assertEquals(1, notifications.sent.size());
        assertEquals("BOOKING_CREATED", notifications.sent.get(0).eventType());
    }

    @Test
    void rejectsBookingWhenStudentDoesNotExist() {
        BookingService service = new BookingService(
                new FakeBookingRepository(),
                id -> Optional.empty(),
                id -> Optional.of(new RoomClientResponse(id, "A-101", 30, "Main")),
                request -> {
                }
        );

        assertThrows(IllegalArgumentException.class, () -> service.create(new BookingCreateRequest(
                1L,
                99L,
                LocalDate.of(2026, 6, 1),
                LocalTime.of(9, 0),
                LocalTime.of(10, 0),
                "Seminar"
        )));
    }

    private static class FakeBookingRepository implements BookingRepository {
        private final AtomicLong sequence = new AtomicLong(1);
        private final List<Booking> bookings = new ArrayList<>();

        @Override
        public List<Booking> findAll() {
            return List.copyOf(bookings);
        }

        @Override
        public List<Booking> findByStudentId(Long studentId) {
            return bookings.stream().filter(booking -> booking.studentId().equals(studentId)).toList();
        }

        @Override
        public Optional<Booking> findById(Long id) {
            return bookings.stream().filter(booking -> booking.id().equals(id)).findFirst();
        }

        @Override
        public List<Booking> findOverlapping(Long roomId, LocalDate date, LocalTime startTime, LocalTime endTime, Long excludeBookingId) {
            return bookings.stream()
                    .filter(booking -> booking.roomId().equals(roomId))
                    .filter(booking -> booking.bookingDate().equals(date))
                    .filter(booking -> excludeBookingId == null || !booking.id().equals(excludeBookingId))
                    .filter(booking -> booking.startTime().isBefore(endTime) && booking.endTime().isAfter(startTime))
                    .toList();
        }

        @Override
        public Booking save(Booking booking) {
            Booking saved = new Booking(
                    sequence.getAndIncrement(),
                    booking.roomId(),
                    booking.studentId(),
                    booking.bookingDate(),
                    booking.startTime(),
                    booking.endTime(),
                    booking.purpose(),
                    booking.status(),
                    booking.rejectReason(),
                    Instant.now()
            );
            bookings.add(saved);
            return saved;
        }

        @Override
        public Booking updateDetails(Booking booking) {
            deleteById(booking.id());
            bookings.add(booking);
            return booking;
        }

        @Override
        public Booking updateStatus(Long id, BookingStatus status, String rejectReason) {
            Booking existing = findById(id).orElseThrow();
            Booking updated = existing.withStatus(status, rejectReason);
            deleteById(id);
            bookings.add(updated);
            return updated;
        }

        @Override
        public void deleteById(Long id) {
            bookings.removeIf(booking -> booking.id().equals(id));
        }
    }

    private static class FakeNotificationClient implements NotificationClient {
        private final List<NotificationRequest> sent = new ArrayList<>();

        @Override
        public void send(NotificationRequest request) {
            sent.add(request);
        }
    }
}
