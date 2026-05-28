package mn.edu.room.core.port.out;

import java.util.List;
import java.util.Optional;
import mn.edu.room.core.domain.Room;

public interface RoomRepository {
    Room save(Room room);

    List<Room> findAll();

    Optional<Room> findById(Long id);
}
