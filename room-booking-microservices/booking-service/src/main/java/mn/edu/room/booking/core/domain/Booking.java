package mn.edu.room.booking.core.domain;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

public record Booking(
        Long id,
        Long roomId,
        Long studentId,
        LocalDate bookingDate,
        LocalTime startTime,
        LocalTime endTime,
        String purpose,
        BookingStatus status,
        String rejectReason,
        Instant createdAt
) {
    public Booking {
        roomId = requirePositive(roomId, "roomId");
        studentId = requirePositive(studentId, "studentId");
        bookingDate = Objects.requireNonNull(bookingDate, "bookingDate");
        startTime = Objects.requireNonNull(startTime, "startTime");
        endTime = Objects.requireNonNull(endTime, "endTime");
        purpose = requireText(purpose, "purpose");
        status = Objects.requireNonNull(status, "status");
        rejectReason = rejectReason == null || rejectReason.isBlank() ? null : rejectReason.trim();
        createdAt = createdAt == null ? Instant.now() : createdAt;
        if (!endTime.isAfter(startTime)) {
            throw new IllegalArgumentException("endTime must be after startTime");
        }
    }

    public static Booking pending(
            Long roomId,
            Long studentId,
            LocalDate bookingDate,
            LocalTime startTime,
            LocalTime endTime,
            String purpose
    ) {
        return new Booking(null, roomId, studentId, bookingDate, startTime, endTime, purpose, BookingStatus.PENDING, null, Instant.now());
    }

    public Booking withDetails(Long newRoomId, LocalDate newDate, LocalTime newStart, LocalTime newEnd, String newPurpose) {
        return new Booking(id, newRoomId, studentId, newDate, newStart, newEnd, newPurpose, BookingStatus.PENDING, null, createdAt);
    }

    public Booking withStatus(BookingStatus newStatus, String newRejectReason) {
        return new Booking(id, roomId, studentId, bookingDate, startTime, endTime, purpose, newStatus, newRejectReason, createdAt);
    }

    public boolean blocksRoom() {
        return status == BookingStatus.PENDING || status == BookingStatus.APPROVED;
    }

    private static Long requirePositive(Long value, String field) {
        if (value == null || value <= 0) {
            throw new IllegalArgumentException(field + " must be positive");
        }
        return value;
    }

    private static String requireText(String value, String field) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(field + " is required");
        }
        return value.trim();
    }
}
