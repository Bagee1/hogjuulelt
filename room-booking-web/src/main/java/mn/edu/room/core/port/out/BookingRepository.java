package mn.edu.room.core.port.out;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import mn.edu.room.core.domain.Booking;

public interface BookingRepository {
    Booking save(Booking booking);

    void updateDetails(Booking booking);

    void updateStatus(Long bookingId, mn.edu.room.core.domain.BookingStatus status, String rejectReason);

    void deleteById(Long bookingId);

    Optional<Booking> findById(Long id);

    List<Booking> findByUserId(Long userId);

    List<Booking> findAll();

    boolean hasApprovedOverlap(Long roomId, LocalDate bookingDate, LocalTime startTime, LocalTime endTime, Long excludedBookingId);
}
