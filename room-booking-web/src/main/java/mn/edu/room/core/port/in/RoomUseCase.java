package mn.edu.room.core.port.in;

import java.util.List;
import mn.edu.room.core.dto.RoomCreateDto;
import mn.edu.room.core.dto.RoomResponseDto;

public interface RoomUseCase {
    List<RoomResponseDto> listRooms();

    RoomResponseDto addRoom(RoomCreateDto dto);
}
