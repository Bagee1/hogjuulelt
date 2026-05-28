package mn.edu.room.booking.core.port.out;

import mn.edu.room.booking.core.domain.Booking;
import mn.edu.room.booking.core.domain.BookingStatus;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository {
    List<Booking> findAll();

    List<Booking> findByStudentId(Long studentId);

    Optional<Booking> findById(Long id);

    List<Booking> findOverlapping(Long roomId, LocalDate date, LocalTime startTime, LocalTime endTime, Long excludeBookingId);

    Booking save(Booking booking);

    Booking updateDetails(Booking booking);

    Booking updateStatus(Long id, BookingStatus status, String rejectReason);

    void deleteById(Long id);
}
