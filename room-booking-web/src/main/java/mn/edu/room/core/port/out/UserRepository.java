package mn.edu.room.core.port.out;

import java.util.Optional;
import mn.edu.room.core.domain.User;

public interface UserRepository {
    User save(User user);

    Optional<User> findByUsername(String username);

    Optional<User> findById(Long id);
}
