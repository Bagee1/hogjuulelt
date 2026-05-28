package mn.edu.room.notification.core.port.in;

import mn.edu.room.notification.core.dto.NotificationCreateRequest;
import mn.edu.room.notification.core.dto.NotificationResponse;

import java.util.List;

public interface NotificationUseCase {
    NotificationResponse send(NotificationCreateRequest request);

    List<NotificationResponse> list(Long studentId);
}
