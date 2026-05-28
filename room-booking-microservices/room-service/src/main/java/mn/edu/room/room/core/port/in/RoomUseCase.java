package mn.edu.room.room.core.port.in;

import mn.edu.room.room.core.dto.RoomCreateRequest;
import mn.edu.room.room.core.dto.RoomResponse;

import java.util.List;

public interface RoomUseCase {
    List<RoomResponse> list();

    RoomResponse get(Long id);

    RoomResponse create(RoomCreateRequest request);
}
