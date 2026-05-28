package mn.edu.room.frontend;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;

public final class Dto {
    private Dto() {
    }

    public record StudentResponse(
            Long id,
            String username,
            String role,
            String firstName,
            String lastName,
            String email,
            String matriculationNumber
    ) {
    }

    public record RoomResponse(Long id, String name, int capacity, String location) {
    }

    public record RoomCreateRequest(String name, int capacity, String location) {
    }

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
    }

    public record BookingCreateRequest(
            Long roomId,
            Long studentId,
            LocalDate bookingDate,
            LocalTime startTime,
            LocalTime endTime,
            String purpose
    ) {
    }

    public record BookingDecisionRequest(Long adminId, String rejectReason) {
    }

    public record NotificationResponse(
            Long id,
            Long bookingId,
            Long studentId,
            String eventType,
            String message,
            Instant createdAt
    ) {
    }
}
