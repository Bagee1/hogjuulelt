package mn.edu.room.core.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import mn.edu.room.core.domain.Booking;
import mn.edu.room.core.domain.BookingStatus;
import mn.edu.room.core.domain.Role;
import mn.edu.room.core.domain.Room;
import mn.edu.room.core.domain.User;
import mn.edu.room.core.dto.BookingCreateDto;
import mn.edu.room.core.dto.BookingDecisionDto;
import mn.edu.room.core.port.out.BookingRepository;
import mn.edu.room.core.port.out.RoomRepository;
import mn.edu.room.core.port.out.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BookingServiceTest {
    private FakeBookingRepository bookingRepository;
    private BookingService service;

    @BeforeEach
    void setUp() {
        bookingRepository = new FakeBookingRepository();
        RoomRepository roomRepository = new FakeRoomRepository();
        UserRepository userRepository = new FakeUserRepository();
        service = new BookingService(bookingRepository, roomRepository, userRepository, List.of());
    }

    @Test
    void createBookingRejectsEndTimeBeforeStartTime() {
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        BookingCreateDto dto = new BookingCreateDto(1L, 1L, tomorrow.toString(), "12:00", "10:00", "Team meeting");

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> service.createBooking(dto));

        assertEquals("Дуусах цаг эхлэх цагаас хойш байх ёстой.", ex.getMessage());
    }

    @Test
    void approveRejectsOverlappingApprovedBooking() {
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        bookingRepository.save(new Booking(null, 1L, 1L, tomorrow, LocalTime.of(9, 0), LocalTime.of(10, 0), "Existing", BookingStatus.APPROVED, null));
        Booking pending = bookingRepository.save(new Booking(null, 1L, 1L, tomorrow, LocalTime.of(9, 30), LocalTime.of(10, 30), "Pending", BookingStatus.PENDING, null));

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> service.approve(new BookingDecisionDto(pending.getId(), 2L, null))
        );

        assertEquals("Энэ өрөө тухайн цагт аль хэдийн зөвшөөрөгдсөн захиалгатай байна.", ex.getMessage());
    }

    @Test
    void rejectRequiresReason() {
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        Booking pending = bookingRepository.save(new Booking(null, 1L, 1L, tomorrow, LocalTime.of(11, 0), LocalTime.of(12, 0), "Pending", BookingStatus.PENDING, null));

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> service.reject(new BookingDecisionDto(pending.getId(), 2L, " "))
        );

        assertEquals("Татгалзах шалтгаанаа оруулна уу.", ex.getMessage());
    }

    private static class FakeBookingRepository implements BookingRepository {
        private final List<Booking> bookings = new ArrayList<>();
        private long nextId = 1;

        @Override
        public Booking save(Booking booking) {
            Booking saved = new Booking(
                    nextId++,
                    booking.getRoomId(),
                    booking.getUserId(),
                    booking.getBookingDate(),
                    booking.getStartTime(),
                    booking.getEndTime(),
                    booking.getPurpose(),
                    booking.getStatus(),
                    booking.getRejectReason()
            );
            bookings.add(saved);
            return saved;
        }

        @Override
        public void updateDetails(Booking booking) {
            Booking existing = findById(booking.getId()).orElseThrow();
            bookings.remove(existing);
            bookings.add(booking);
        }

        @Override
        public void updateStatus(Long bookingId, BookingStatus status, String rejectReason) {
            Booking existing = findById(bookingId).orElseThrow();
            bookings.remove(existing);
            bookings.add(existing.withStatus(status, rejectReason));
        }

        @Override
        public void deleteById(Long bookingId) {
            findById(bookingId).ifPresent(bookings::remove);
        }

        @Override
        public Optional<Booking> findById(Long id) {
            return bookings.stream().filter(booking -> booking.getId().equals(id)).findFirst();
        }

        @Override
        public List<Booking> findByUserId(Long userId) {
            return bookings.stream().filter(booking -> booking.getUserId().equals(userId)).toList();
        }

        @Override
        public List<Booking> findAll() {
            return List.copyOf(bookings);
        }

        @Override
        public boolean hasApprovedOverlap(Long roomId, LocalDate bookingDate, LocalTime startTime, LocalTime endTime, Long excludedBookingId) {
            return bookings.stream()
                    .filter(booking -> booking.getStatus() == BookingStatus.APPROVED)
                    .filter(booking -> booking.getRoomId().equals(roomId))
                    .filter(booking -> excludedBookingId == null || !booking.getId().equals(excludedBookingId))
                    .anyMatch(booking -> booking.overlaps(bookingDate, startTime, endTime));
        }
    }

    private static class FakeRoomRepository implements RoomRepository {
        @Override
        public Room save(Room room) {
            return new Room(2L, room.getName(), room.getCapacity(), room.getLocation());
        }

        @Override
        public List<Room> findAll() {
            return List.of(new Room(1L, "Lab 201", 30, "Main"));
        }

        @Override
        public Optional<Room> findById(Long id) {
            return id.equals(1L) ? Optional.of(new Room(1L, "Lab 201", 30, "Main")) : Optional.empty();
        }
    }

    private static class FakeUserRepository implements UserRepository {
        @Override
        public User save(User user) {
            return new User(3L, user.getUsername(), user.getPassword(), user.getRole());
        }

        @Override
        public Optional<User> findByUsername(String username) {
            return Optional.empty();
        }

        @Override
        public Optional<User> findById(Long id) {
            if (id.equals(1L)) {
                return Optional.of(new User(1L, "student", "123", Role.STUDENT));
            }
            if (id.equals(2L)) {
                return Optional.of(new User(2L, "admin", "123", Role.ADMIN));
            }
            return Optional.empty();
        }
    }
}
