package mn.edu.room.core.domain;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

public class Booking {
    private final Long id;
    private final Long roomId;
    private final Long userId;
    private final LocalDate bookingDate;
    private final LocalTime startTime;
    private final LocalTime endTime;
    private final String purpose;
    private final BookingStatus status;
    private final String rejectReason;

    public Booking(
            Long id,
            Long roomId,
            Long userId,
            LocalDate bookingDate,
            LocalTime startTime,
            LocalTime endTime,
            String purpose,
            BookingStatus status,
            String rejectReason
    ) {
        this.id = id;
        this.roomId = requirePositive(roomId, "roomId");
        this.userId = requirePositive(userId, "userId");
        this.bookingDate = Objects.requireNonNull(bookingDate, "bookingDate");
        this.startTime = Objects.requireNonNull(startTime, "startTime");
        this.endTime = Objects.requireNonNull(endTime, "endTime");
        this.purpose = requireText(purpose, "purpose");
        this.status = Objects.requireNonNull(status, "status");
        this.rejectReason = rejectReason == null ? null : rejectReason.trim();
        validateTimeRange(startTime, endTime);
    }

    public static Booking pending(Long roomId, Long userId, LocalDate bookingDate, LocalTime startTime, LocalTime endTime, String purpose) {
        return new Booking(null, roomId, userId, bookingDate, startTime, endTime, purpose, BookingStatus.PENDING, null);
    }

    public Booking withStatus(BookingStatus newStatus, String newRejectReason) {
        return new Booking(id, roomId, userId, bookingDate, startTime, endTime, purpose, newStatus, newRejectReason);
    }

    public Booking withDetails(
            Long newRoomId,
            LocalDate newBookingDate,
            LocalTime newStartTime,
            LocalTime newEndTime,
            String newPurpose,
            BookingStatus newStatus,
            String newRejectReason
    ) {
        return new Booking(id, newRoomId, userId, newBookingDate, newStartTime, newEndTime, newPurpose, newStatus, newRejectReason);
    }

    public boolean overlaps(LocalDate otherDate, LocalTime otherStart, LocalTime otherEnd) {
        if (!bookingDate.equals(otherDate)) {
            return false;
        }
        return startTime.isBefore(otherEnd) && endTime.isAfter(otherStart);
    }

    public Long getId() {
        return id;
    }

    public Long getRoomId() {
        return roomId;
    }

    public Long getUserId() {
        return userId;
    }

    public LocalDate getBookingDate() {
        return bookingDate;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public String getPurpose() {
        return purpose;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public String getRejectReason() {
        return rejectReason;
    }

    private static Long requirePositive(Long value, String field) {
        if (value == null || value <= 0) {
            throw new IllegalArgumentException(fieldLabel(field) + " зөв утгатай байх ёстой.");
        }
        return value;
    }

    private static String requireText(String value, String field) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(fieldLabel(field) + " шаардлагатай.");
        }
        return value.trim();
    }

    private static void validateTimeRange(LocalTime startTime, LocalTime endTime) {
        if (!endTime.isAfter(startTime)) {
            throw new IllegalArgumentException("Дуусах цаг эхлэх цагаас хойш байх ёстой.");
        }
    }

    private static String fieldLabel(String field) {
        return switch (field) {
            case "roomId" -> "Өрөө";
            case "userId" -> "Хэрэглэгч";
            case "purpose" -> "Зорилго";
            default -> field;
        };
    }
}
