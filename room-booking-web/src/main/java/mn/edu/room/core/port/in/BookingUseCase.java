package mn.edu.room.core.port.in;

import java.util.List;
import mn.edu.room.core.dto.BookingCreateDto;
import mn.edu.room.core.dto.BookingDecisionDto;
import mn.edu.room.core.dto.BookingResponseDto;
import mn.edu.room.core.dto.BookingUpdateDto;

public interface BookingUseCase {
    BookingResponseDto createBooking(BookingCreateDto dto);

    List<BookingResponseDto> listMyBookings(Long userId);

    List<BookingResponseDto> listAllBookings();

    void approve(BookingDecisionDto dto);

    void reject(BookingDecisionDto dto);

    void updateByStudent(BookingUpdateDto dto);

    void updateByAdmin(BookingUpdateDto dto);

    void cancel(BookingDecisionDto dto);

    void delete(BookingDecisionDto dto);
}
