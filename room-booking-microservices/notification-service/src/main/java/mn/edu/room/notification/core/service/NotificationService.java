package mn.edu.room.notification.core.service;

import mn.edu.room.notification.core.domain.NotificationLog;
import mn.edu.room.notification.core.dto.NotificationCreateRequest;
import mn.edu.room.notification.core.dto.NotificationResponse;
import mn.edu.room.notification.core.port.in.NotificationUseCase;
import mn.edu.room.notification.core.port.out.NotificationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService implements NotificationUseCase {
    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @Override
    public NotificationResponse send(NotificationCreateRequest request) {
        NotificationLog saved = notificationRepository.save(NotificationLog.create(
                request.bookingId(),
                request.studentId(),
                request.eventType(),
                request.message()
        ));
        return NotificationResponse.from(saved);
    }

    @Override
    public List<NotificationResponse> list(Long studentId) {
        List<NotificationLog> logs = studentId == null
                ? notificationRepository.findAll()
                : notificationRepository.findByStudentId(studentId);
        return logs.stream().map(NotificationResponse::from).toList();
    }
}
