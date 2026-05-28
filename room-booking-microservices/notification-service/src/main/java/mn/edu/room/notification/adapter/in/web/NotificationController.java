package mn.edu.room.notification.adapter.in.web;

import mn.edu.room.notification.core.dto.NotificationCreateRequest;
import mn.edu.room.notification.core.dto.NotificationResponse;
import mn.edu.room.notification.core.port.in.NotificationUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
public class NotificationController {
    private final NotificationUseCase notificationUseCase;

    public NotificationController(NotificationUseCase notificationUseCase) {
        this.notificationUseCase = notificationUseCase;
    }

    @PostMapping("/notifications")
    @ResponseStatus(HttpStatus.CREATED)
    public NotificationResponse send(@RequestBody NotificationCreateRequest request) {
        try {
            return notificationUseCase.send(request);
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage(), ex);
        }
    }

    @GetMapping("/notifications")
    public List<NotificationResponse> list(@RequestParam(required = false) Long studentId) {
        return notificationUseCase.list(studentId);
    }
}
