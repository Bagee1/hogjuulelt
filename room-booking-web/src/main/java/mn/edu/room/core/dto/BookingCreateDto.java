package mn.edu.room.core.dto;

public record BookingCreateDto(
        Long roomId,
        Long userId,
        String bookingDate,
        String startTime,
        String endTime,
        String purpose
) {
}
