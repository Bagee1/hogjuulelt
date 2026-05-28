package mn.edu.room.core.dto;

public record BookingUpdateDto(
        Long bookingId,
        Long roomId,
        Long actorId,
        String bookingDate,
        String startTime,
        String endTime,
        String purpose
) {
}
