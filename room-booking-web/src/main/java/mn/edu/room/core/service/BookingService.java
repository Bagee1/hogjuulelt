package mn.edu.room.core.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import mn.edu.room.core.domain.Booking;
import mn.edu.room.core.domain.BookingStatus;
import mn.edu.room.core.domain.Role;
import mn.edu.room.core.domain.Room;
import mn.edu.room.core.domain.User;
import mn.edu.room.core.dto.BookingCreateDto;
import mn.edu.room.core.dto.BookingDecisionDto;
import mn.edu.room.core.dto.BookingResponseDto;
import mn.edu.room.core.dto.BookingUpdateDto;
import mn.edu.room.core.observer.BookingObserver;
import mn.edu.room.core.port.in.BookingUseCase;
import mn.edu.room.core.port.out.BookingRepository;
import mn.edu.room.core.port.out.RoomRepository;
import mn.edu.room.core.port.out.UserRepository;

public class BookingService implements BookingUseCase {
    private final BookingRepository bookingRepository;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;
    private final List<BookingObserver> observers;

    public BookingService(
            BookingRepository bookingRepository,
            RoomRepository roomRepository,
            UserRepository userRepository,
            List<BookingObserver> observers
    ) {
        this.bookingRepository = bookingRepository;
        this.roomRepository = roomRepository;
        this.userRepository = userRepository;
        this.observers = List.copyOf(observers);
    }

    @Override
    public BookingResponseDto createBooking(BookingCreateDto dto) {
        if (dto == null) {
            throw new IllegalArgumentException("Захиалгын мэдээлэл шаардлагатай.");
        }
        roomRepository.findById(dto.roomId()).orElseThrow(() -> new IllegalArgumentException("Өрөө олдсонгүй."));
        userRepository.findById(dto.userId()).orElseThrow(() -> new IllegalArgumentException("Хэрэглэгч олдсонгүй."));

        LocalDate date = parseDate(dto.bookingDate());
        LocalTime start = parseTime(dto.startTime());
        LocalTime end = parseTime(dto.endTime());
        validateNotPast(date, start);

        Booking booking = Booking.pending(dto.roomId(), dto.userId(), date, start, end, dto.purpose());
        return toDto(bookingRepository.save(booking));
    }

    @Override
    public List<BookingResponseDto> listMyBookings(Long userId) {
        return bookingRepository.findByUserId(userId).stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public List<BookingResponseDto> listAllBookings() {
        return bookingRepository.findAll().stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public void approve(BookingDecisionDto dto) {
        Booking booking = findBooking(dto.bookingId());
        if (booking.getStatus() != BookingStatus.PENDING) {
            throw new IllegalArgumentException("Зөвхөн хүлээгдэж буй захиалгыг зөвшөөрнө.");
        }
        boolean overlap = bookingRepository.hasApprovedOverlap(
                booking.getRoomId(),
                booking.getBookingDate(),
                booking.getStartTime(),
                booking.getEndTime(),
                booking.getId()
        );
        if (overlap) {
            throw new IllegalArgumentException("Энэ өрөө тухайн цагт аль хэдийн зөвшөөрөгдсөн захиалгатай байна.");
        }
        bookingRepository.updateStatus(booking.getId(), BookingStatus.APPROVED, null);
        notifyObservers(booking.withStatus(BookingStatus.APPROVED, null), booking.getStatus(), BookingStatus.APPROVED, dto.adminId());
    }

    @Override
    public void reject(BookingDecisionDto dto) {
        Booking booking = findBooking(dto.bookingId());
        if (booking.getStatus() != BookingStatus.PENDING) {
            throw new IllegalArgumentException("Зөвхөн хүлээгдэж буй захиалгыг татгалзана.");
        }
        if (dto.rejectReason() == null || dto.rejectReason().isBlank()) {
            throw new IllegalArgumentException("Татгалзах шалтгаанаа оруулна уу.");
        }
        String reason = dto.rejectReason().trim();
        bookingRepository.updateStatus(booking.getId(), BookingStatus.REJECTED, reason);
        notifyObservers(booking.withStatus(BookingStatus.REJECTED, reason), booking.getStatus(), BookingStatus.REJECTED, dto.adminId());
    }

    @Override
    public void updateByStudent(BookingUpdateDto dto) {
        Booking booking = findBooking(dto.bookingId());
        if (!booking.getUserId().equals(dto.actorId())) {
            throw new IllegalArgumentException("Зөвхөн өөрийн захиалгыг засах боломжтой.");
        }
        if (booking.getStatus() == BookingStatus.APPROVED) {
            throw new IllegalArgumentException("Зөвшөөрөгдсөн захиалгыг оюутан засахгүй, эхлээд цуцална уу.");
        }
        Booking updated = buildUpdatedBooking(booking, dto, BookingStatus.PENDING, null);
        bookingRepository.updateDetails(updated);
        if (booking.getStatus() != BookingStatus.PENDING) {
            notifyObservers(updated, booking.getStatus(), BookingStatus.PENDING, dto.actorId());
        }
    }

    @Override
    public void updateByAdmin(BookingUpdateDto dto) {
        requireAdmin(dto.actorId());
        Booking booking = findBooking(dto.bookingId());
        Booking updated = buildUpdatedBooking(booking, dto, booking.getStatus(), booking.getRejectReason());
        if (updated.getStatus() == BookingStatus.APPROVED) {
            ensureNoApprovedOverlap(updated);
        }
        bookingRepository.updateDetails(updated);
    }

    @Override
    public void cancel(BookingDecisionDto dto) {
        Booking booking = findBooking(dto.bookingId());
        User actor = findUser(dto.adminId());
        if (!actor.hasRole(Role.ADMIN) && !booking.getUserId().equals(actor.getId())) {
            throw new IllegalArgumentException("Зөвхөн өөрийн захиалга эсвэл админ цуцлах боломжтой.");
        }
        if (booking.getStatus() == BookingStatus.CANCELLED) {
            throw new IllegalArgumentException("Энэ захиалга аль хэдийн цуцлагдсан байна.");
        }
        String reason = dto.rejectReason() == null || dto.rejectReason().isBlank()
                ? "Цуцалсан"
                : dto.rejectReason().trim();
        bookingRepository.updateStatus(booking.getId(), BookingStatus.CANCELLED, reason);
        notifyObservers(booking.withStatus(BookingStatus.CANCELLED, reason), booking.getStatus(), BookingStatus.CANCELLED, actor.getId());
    }

    @Override
    public void delete(BookingDecisionDto dto) {
        requireAdmin(dto.adminId());
        findBooking(dto.bookingId());
        bookingRepository.deleteById(dto.bookingId());
    }

    private Booking findBooking(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Захиалгын дугаар шаардлагатай.");
        }
        return bookingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Захиалга олдсонгүй."));
    }

    private Booking buildUpdatedBooking(Booking booking, BookingUpdateDto dto, BookingStatus status, String rejectReason) {
        roomRepository.findById(dto.roomId()).orElseThrow(() -> new IllegalArgumentException("Өрөө олдсонгүй."));
        LocalDate date = parseDate(dto.bookingDate());
        LocalTime start = parseTime(dto.startTime());
        LocalTime end = parseTime(dto.endTime());
        validateNotPast(date, start);
        return booking.withDetails(dto.roomId(), date, start, end, dto.purpose(), status, rejectReason);
    }

    private void ensureNoApprovedOverlap(Booking booking) {
        boolean overlap = bookingRepository.hasApprovedOverlap(
                booking.getRoomId(),
                booking.getBookingDate(),
                booking.getStartTime(),
                booking.getEndTime(),
                booking.getId()
        );
        if (overlap) {
            throw new IllegalArgumentException("Энэ өрөө тухайн цагт аль хэдийн зөвшөөрөгдсөн захиалгатай байна.");
        }
    }

    private User findUser(Long userId) {
        if (userId == null || userId <= 0) {
            throw new IllegalArgumentException("Хэрэглэгчийн дугаар шаардлагатай.");
        }
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Хэрэглэгч олдсонгүй."));
    }

    private void requireAdmin(Long userId) {
        User user = findUser(userId);
        if (!user.hasRole(Role.ADMIN)) {
            throw new IllegalArgumentException("Энэ үйлдэл зөвхөн админ эрхтэй.");
        }
    }

    private BookingResponseDto toDto(Booking booking) {
        Room room = roomRepository.findById(booking.getRoomId()).orElse(null);
        User user = userRepository.findById(booking.getUserId()).orElse(null);
        String roomName = room == null ? "Тодорхойгүй өрөө" : room.getName();
        String username = user == null ? "Тодорхойгүй хэрэглэгч" : user.getUsername();
        return new BookingResponseDto(
                booking.getId(),
                booking.getRoomId(),
                roomName,
                booking.getUserId(),
                username,
                booking.getBookingDate().toString(),
                booking.getStartTime().toString(),
                booking.getEndTime().toString(),
                booking.getPurpose(),
                booking.getStatus().name(),
                booking.getRejectReason()
        );
    }

    private void notifyObservers(Booking booking, BookingStatus oldStatus, BookingStatus newStatus, Long actorId) {
        for (BookingObserver observer : observers) {
            observer.onStatusChanged(booking, oldStatus, newStatus, actorId);
        }
    }

    private static LocalDate parseDate(String value) {
        try {
            return LocalDate.parse(value);
        } catch (RuntimeException ex) {
            throw new IllegalArgumentException("Захиалах огноо буруу байна.");
        }
    }

    private static LocalTime parseTime(String value) {
        try {
            return LocalTime.parse(value);
        } catch (RuntimeException ex) {
            throw new IllegalArgumentException("Захиалах цаг буруу байна.");
        }
    }

    private static void validateNotPast(LocalDate date, LocalTime start) {
        LocalDate today = LocalDate.now();
        if (date.isBefore(today) || (date.equals(today) && start.isBefore(LocalTime.now()))) {
            throw new IllegalArgumentException("Захиалах цаг одоо цагаас хойш байх ёстой.");
        }
    }
}
