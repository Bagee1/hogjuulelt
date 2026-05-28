package mn.edu.room.booking.adapter.in.web;

import mn.edu.room.booking.core.dto.BookingCreateRequest;
import mn.edu.room.booking.core.dto.BookingDecisionRequest;
import mn.edu.room.booking.core.dto.BookingResponse;
import mn.edu.room.booking.core.dto.BookingUpdateRequest;
import mn.edu.room.booking.core.port.in.BookingUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
public class BookingController {
    private final BookingUseCase bookingUseCase;

    public BookingController(BookingUseCase bookingUseCase) {
        this.bookingUseCase = bookingUseCase;
    }

    @GetMapping("/bookings")
    public List<BookingResponse> list(@RequestParam(required = false) Long studentId) {
        return studentId == null ? bookingUseCase.list() : bookingUseCase.listByStudent(studentId);
    }

    @GetMapping("/bookings/student/{studentId}")
    public List<BookingResponse> listByStudent(@PathVariable Long studentId) {
        return bookingUseCase.listByStudent(studentId);
    }

    @GetMapping("/bookings/{id}")
    public BookingResponse get(@PathVariable Long id) {
        try {
            return bookingUseCase.get(id);
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage(), ex);
        }
    }

    @PostMapping("/bookings")
    @ResponseStatus(HttpStatus.CREATED)
    public BookingResponse create(@RequestBody BookingCreateRequest request) {
        try {
            return bookingUseCase.create(request);
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage(), ex);
        }
    }

    @PutMapping("/bookings/{id}")
    public BookingResponse update(@PathVariable Long id, @RequestBody BookingUpdateRequest request) {
        try {
            return bookingUseCase.update(id, request);
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage(), ex);
        }
    }

    @PostMapping("/bookings/{id}/approve")
    public BookingResponse approve(@PathVariable Long id, @RequestBody BookingDecisionRequest request) {
        try {
            return bookingUseCase.approve(id, request);
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage(), ex);
        }
    }

    @PostMapping("/bookings/{id}/reject")
    public BookingResponse reject(@PathVariable Long id, @RequestBody BookingDecisionRequest request) {
        try {
            return bookingUseCase.reject(id, request);
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage(), ex);
        }
    }

    @PostMapping("/bookings/{id}/cancel")
    public BookingResponse cancel(@PathVariable Long id) {
        try {
            return bookingUseCase.cancel(id);
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage(), ex);
        }
    }

    @DeleteMapping("/bookings/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        try {
            bookingUseCase.delete(id);
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage(), ex);
        }
    }
}
