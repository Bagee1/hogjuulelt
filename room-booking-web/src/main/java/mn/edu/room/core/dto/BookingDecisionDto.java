package mn.edu.room.core.dto;

public record BookingDecisionDto(Long bookingId, Long adminId, String rejectReason) {
}
