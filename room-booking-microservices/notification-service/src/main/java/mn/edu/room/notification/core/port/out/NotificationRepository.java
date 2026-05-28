package mn.edu.room.notification.core.port.out;

import mn.edu.room.notification.core.domain.NotificationLog;

import java.util.List;

public interface NotificationRepository {
    NotificationLog save(NotificationLog log);

    List<NotificationLog> findAll();

    List<NotificationLog> findByStudentId(Long studentId);
}
