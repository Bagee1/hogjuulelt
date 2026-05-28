package mn.edu.room.booking.core.port.in;

import mn.edu.room.booking.core.dto.BookingCreateRequest;
import mn.edu.room.booking.core.dto.BookingDecisionRequest;
import mn.edu.room.booking.core.dto.BookingResponse;
import mn.edu.room.booking.core.dto.BookingUpdateRequest;

import java.util.List;

public interface BookingUseCase {
    List<BookingResponse> list();

    List<BookingResponse> listByStudent(Long studentId);

    BookingResponse get(Long id);

    BookingResponse create(BookingCreateRequest request);

    BookingResponse update(Long id, BookingUpdateRequest request);

    BookingResponse approve(Long id, BookingDecisionRequest request);

    BookingResponse reject(Long id, BookingDecisionRequest request);

    BookingResponse cancel(Long id);

    void delete(Long id);
}
