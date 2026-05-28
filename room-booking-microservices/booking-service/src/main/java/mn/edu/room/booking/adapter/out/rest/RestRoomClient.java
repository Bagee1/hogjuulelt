package mn.edu.room.booking.adapter.out.rest;

import mn.edu.room.booking.core.dto.RoomClientResponse;
import mn.edu.room.booking.core.port.out.RoomClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Component
public class RestRoomClient implements RoomClient {
    private final RestTemplate restTemplate;
    private final String roomServiceUrl;

    public RestRoomClient(RestTemplate restTemplate, @Value("${services.room-url}") String roomServiceUrl) {
        this.restTemplate = restTemplate;
        this.roomServiceUrl = roomServiceUrl;
    }

    @Override
    public Optional<RoomClientResponse> findById(Long id) {
        try {
            return Optional.ofNullable(restTemplate.getForObject(roomServiceUrl + "/rooms/{id}", RoomClientResponse.class, id));
        } catch (HttpStatusCodeException ex) {
            if (ex.getStatusCode() == HttpStatus.NOT_FOUND) {
                return Optional.empty();
            }
            throw ex;
        }
    }
}
