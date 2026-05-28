package mn.edu.room.booking.core.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public record BookingUpdateRequest(
        Long roomId,
        Long studentId,
        LocalDate bookingDate,
        LocalTime startTime,
        LocalTime endTime,
        String purpose
) {
}
