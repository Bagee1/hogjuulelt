package mn.edu.room.core.observer;

import mn.edu.room.core.domain.Booking;
import mn.edu.room.core.domain.BookingStatus;

public interface BookingObserver {
    void onStatusChanged(Booking booking, BookingStatus oldStatus, BookingStatus newStatus, Long actorId);
}
