package mn.edu.room.booking.core.dto;

import mn.edu.room.booking.core.domain.Booking;
import mn.edu.room.booking.core.domain.BookingStatus;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;

public record BookingResponse(
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
    public static BookingResponse from(Booking booking) {
        return new BookingResponse(
                booking.id(),
                booking.roomId(),
                booking.studentId(),
                booking.bookingDate(),
                booking.startTime(),
                booking.endTime(),
                booking.purpose(),
                booking.status(),
                booking.rejectReason(),
                booking.createdAt()
        );
    }
}
