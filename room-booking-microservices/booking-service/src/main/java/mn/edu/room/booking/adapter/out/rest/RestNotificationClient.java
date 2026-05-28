package mn.edu.room.booking.adapter.out.rest;

import mn.edu.room.booking.core.dto.NotificationRequest;
import mn.edu.room.booking.core.port.out.NotificationClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class RestNotificationClient implements NotificationClient {
    private final RestTemplate restTemplate;
    private final String notificationServiceUrl;

    public RestNotificationClient(RestTemplate restTemplate, @Value("${services.notification-url}") String notificationServiceUrl) {
        this.restTemplate = restTemplate;
        this.notificationServiceUrl = notificationServiceUrl;
    }

    @Override
    public void send(NotificationRequest request) {
        restTemplate.postForEntity(notificationServiceUrl + "/notifications", request, Void.class);
    }
}
