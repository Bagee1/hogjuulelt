package mn.edu.room.room.adapter.in.web;

import mn.edu.room.room.core.dto.RoomCreateRequest;
import mn.edu.room.room.core.dto.RoomResponse;
import mn.edu.room.room.core.port.in.RoomUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
public class RoomController {
    private final RoomUseCase roomUseCase;

    public RoomController(RoomUseCase roomUseCase) {
        this.roomUseCase = roomUseCase;
    }

    @GetMapping("/rooms")
    public List<RoomResponse> list() {
        return roomUseCase.list();
    }

    @GetMapping("/rooms/{id}")
    public RoomResponse get(@PathVariable Long id) {
        try {
            return roomUseCase.get(id);
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage(), ex);
        }
    }

    @PostMapping("/rooms")
    @ResponseStatus(HttpStatus.CREATED)
    public RoomResponse create(@RequestBody RoomCreateRequest request) {
        try {
            return roomUseCase.create(request);
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage(), ex);
        }
    }
}
