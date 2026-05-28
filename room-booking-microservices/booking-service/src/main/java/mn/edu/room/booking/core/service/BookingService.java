package mn.edu.room.booking.core.service;

import mn.edu.room.booking.core.domain.Booking;
import mn.edu.room.booking.core.domain.BookingStatus;
import mn.edu.room.booking.core.dto.BookingCreateRequest;
import mn.edu.room.booking.core.dto.BookingDecisionRequest;
import mn.edu.room.booking.core.dto.BookingResponse;
import mn.edu.room.booking.core.dto.BookingUpdateRequest;
import mn.edu.room.booking.core.dto.NotificationRequest;
import mn.edu.room.booking.core.dto.RoomClientResponse;
import mn.edu.room.booking.core.dto.StudentClientResponse;
import mn.edu.room.booking.core.port.in.BookingUseCase;
import mn.edu.room.booking.core.port.out.BookingRepository;
import mn.edu.room.booking.core.port.out.NotificationClient;
import mn.edu.room.booking.core.port.out.RoomClient;
import mn.edu.room.booking.core.port.out.StudentClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BookingService implements BookingUseCase {
    private static final Logger log = LoggerFactory.getLogger(BookingService.class);

    private final BookingRepository bookingRepository;
    private final StudentClient studentClient;
    private final RoomClient roomClient;
    private final NotificationClient notificationClient;

    public BookingService(
            BookingRepository bookingRepository,
            StudentClient studentClient,
            RoomClient roomClient,
            NotificationClient notificationClient
    ) {
        this.bookingRepository = bookingRepository;
        this.studentClient = studentClient;
        this.roomClient = roomClient;
        this.notificationClient = notificationClient;
    }

    @Override
    public List<BookingResponse> list() {
        return bookingRepository.findAll().stream()
                .map(BookingResponse::from)
                .toList();
    }

    @Override
    public List<BookingResponse> listByStudent(Long studentId) {
        ensureStudent(studentId);
        return bookingRepository.findByStudentId(studentId).stream()
                .map(BookingResponse::from)
                .toList();
    }

    @Override
    public BookingResponse get(Long id) {
        return BookingResponse.from(findBooking(id));
    }

    @Override
    @Transactional
    public BookingResponse create(BookingCreateRequest request) {
        StudentClientResponse student = ensureStudent(request.studentId());
        RoomClientResponse room = ensureRoom(request.roomId());
        Booking booking = Booking.pending(
                room.id(),
                student.id(),
                request.bookingDate(),
                request.startTime(),
                request.endTime(),
                request.purpose()
        );
        ensureNoOverlap(booking, null);
        Booking saved = bookingRepository.save(booking);
        notifySafely(saved, "BOOKING_CREATED", "Booking request created for room " + room.name());
        return BookingResponse.from(saved);
    }

    @Override
    @Transactional
    public BookingResponse update(Long id, BookingUpdateRequest request) {
        Booking existing = findBooking(id);
        if (!existing.studentId().equals(request.studentId())) {
            throw new IllegalArgumentException("Only the owner student can resubmit this booking");
        }
        ensureStudent(request.studentId());
        ensureRoom(request.roomId());
        Booking updated = existing.withDetails(
                request.roomId(),
                request.bookingDate(),
                request.startTime(),
                request.endTime(),
                request.purpose()
        );
        ensureNoOverlap(updated, existing.id());
        Booking saved = bookingRepository.updateDetails(updated);
        notifySafely(saved, "BOOKING_RESUBMITTED", "Booking request was updated and is pending review again");
        return BookingResponse.from(saved);
    }

    @Override
    @Transactional
    public BookingResponse approve(Long id, BookingDecisionRequest request) {
        ensureAdmin(request.adminId());
        Booking booking = findBooking(id);
        Booking saved = bookingRepository.updateStatus(booking.id(), BookingStatus.APPROVED, null);
        notifySafely(saved, "BOOKING_APPROVED", "Your room booking was approved");
        return BookingResponse.from(saved);
    }

    @Override
    @Transactional
    public BookingResponse reject(Long id, BookingDecisionRequest request) {
        ensureAdmin(request.adminId());
        if (request.rejectReason() == null || request.rejectReason().isBlank()) {
            throw new IllegalArgumentException("rejectReason is required");
        }
        Booking booking = findBooking(id);
        Booking saved = bookingRepository.updateStatus(booking.id(), BookingStatus.REJECTED, request.rejectReason());
        notifySafely(saved, "BOOKING_REJECTED", "Your room booking was rejected: " + request.rejectReason());
        return BookingResponse.from(saved);
    }

    @Override
    @Transactional
    public BookingResponse cancel(Long id) {
        Booking booking = findBooking(id);
        Booking saved = bookingRepository.updateStatus(booking.id(), BookingStatus.CANCELLED, null);
        notifySafely(saved, "BOOKING_CANCELLED", "Your room booking was cancelled");
        return BookingResponse.from(saved);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        findBooking(id);
        bookingRepository.deleteById(id);
    }

    private Booking findBooking(Long id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found: " + id));
    }

    private StudentClientResponse ensureStudent(Long studentId) {
        return studentClient.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found in student-service: " + studentId));
    }

    private void ensureAdmin(Long adminId) {
        StudentClientResponse admin = ensureStudent(adminId);
        if (!"ADMIN".equals(admin.role())) {
            throw new IllegalArgumentException("Admin role is required");
        }
    }

    private RoomClientResponse ensureRoom(Long roomId) {
        return roomClient.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Room not found in room-service: " + roomId));
    }

    private void ensureNoOverlap(Booking candidate, Long excludeBookingId) {
        boolean overlapping = bookingRepository.findOverlapping(
                candidate.roomId(),
                candidate.bookingDate(),
                candidate.startTime(),
                candidate.endTime(),
                excludeBookingId
        ).stream().anyMatch(Booking::blocksRoom);
        if (overlapping) {
            throw new IllegalArgumentException("Room is already booked for that time");
        }
    }

    private void notifySafely(Booking booking, String eventType, String message) {
        try {
            notificationClient.send(new NotificationRequest(booking.id(), booking.studentId(), eventType, message));
        } catch (RuntimeException ex) {
            log.warn("Notification service failed for booking {}", booking.id(), ex);
        }
    }
}
