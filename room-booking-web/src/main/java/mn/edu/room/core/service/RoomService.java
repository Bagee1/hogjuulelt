package mn.edu.room.core.service;

import java.util.List;
import mn.edu.room.core.domain.Room;
import mn.edu.room.core.dto.RoomCreateDto;
import mn.edu.room.core.dto.RoomResponseDto;
import mn.edu.room.core.port.in.RoomUseCase;
import mn.edu.room.core.port.out.RoomRepository;

public class RoomService implements RoomUseCase {
    private final RoomRepository roomRepository;

    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    @Override
    public List<RoomResponseDto> listRooms() {
        return roomRepository.findAll().stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public RoomResponseDto addRoom(RoomCreateDto dto) {
        if (dto == null) {
            throw new IllegalArgumentException("Өрөөний мэдээлэл шаардлагатай.");
        }
        Room room = new Room(null, dto.name(), dto.capacity(), dto.location());
        return toDto(roomRepository.save(room));
    }

    private RoomResponseDto toDto(Room room) {
        return new RoomResponseDto(room.getId(), room.getName(), room.getCapacity(), room.getLocation());
    }
}
