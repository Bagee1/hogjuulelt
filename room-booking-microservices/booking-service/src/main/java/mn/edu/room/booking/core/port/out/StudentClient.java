package mn.edu.room.booking.core.port.out;

import mn.edu.room.booking.core.dto.StudentClientResponse;

import java.util.Optional;

public interface StudentClient {
    Optional<StudentClientResponse> findById(Long id);
}
