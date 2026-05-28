package mn.edu.room.core.dto;

public record BookingResponseDto(
        Long id,
        Long roomId,
        String roomName,
        Long userId,
        String studentUsername,
        String bookingDate,
        String startTime,
        String endTime,
        String purpose,
        String status,
        String rejectReason
) {
    public Long getId() {
        return id;
    }

    public Long getRoomId() {
        return roomId;
    }

    public String getRoomName() {
        return roomName;
    }

    public Long getUserId() {
        return userId;
    }

    public String getStudentUsername() {
        return studentUsername;
    }

    public String getBookingDate() {
        return bookingDate;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public String getPurpose() {
        return purpose;
    }

    public String getStatus() {
        return status;
    }

    public String getStatusLabel() {
        return switch (status) {
            case "PENDING" -> "Хүлээгдэж байна";
            case "APPROVED" -> "Зөвшөөрсөн";
            case "REJECTED" -> "Татгалзсан";
            case "CANCELLED" -> "Цуцалсан";
            default -> status;
        };
    }

    public String getRejectReason() {
        return rejectReason;
    }

    public String getRejectReasonText() {
        return rejectReason == null || rejectReason.isBlank() ? "-" : rejectReason;
    }
}
