package mn.edu.room.booking.core.port.out;

import mn.edu.room.booking.core.dto.NotificationRequest;

public interface NotificationClient {
    void send(NotificationRequest request);
}
