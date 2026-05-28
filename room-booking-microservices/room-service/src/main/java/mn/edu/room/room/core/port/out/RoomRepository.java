package mn.edu.room.room.core.port.out;

import mn.edu.room.room.core.domain.Room;

import java.util.List;
import java.util.Optional;

public interface RoomRepository {
    List<Room> findAll();

    Optional<Room> findById(Long id);

    Room save(Room room);
}
