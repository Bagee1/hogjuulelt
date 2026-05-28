package mn.edu.room.booking.core.port.out;

import mn.edu.room.booking.core.dto.RoomClientResponse;

import java.util.Optional;

public interface RoomClient {
    Optional<RoomClientResponse> findById(Long id);
}
