package mn.edu.room.room.core.service;

import mn.edu.room.room.core.domain.Room;
import mn.edu.room.room.core.dto.RoomCreateRequest;
import mn.edu.room.room.core.dto.RoomResponse;
import mn.edu.room.room.core.port.in.RoomUseCase;
import mn.edu.room.room.core.port.out.RoomRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomService implements RoomUseCase {
    private final RoomRepository roomRepository;

    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    @Override
    public List<RoomResponse> list() {
        return roomRepository.findAll().stream()
                .map(RoomResponse::from)
                .toList();
    }

    @Override
    public RoomResponse get(Long id) {
        return roomRepository.findById(id)
                .map(RoomResponse::from)
                .orElseThrow(() -> new IllegalArgumentException("Room not found: " + id));
    }

    @Override
    public RoomResponse create(RoomCreateRequest request) {
        Room saved = roomRepository.save(Room.create(request.name(), request.capacity(), request.location()));
        return RoomResponse.from(saved);
    }
}
