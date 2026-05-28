package mn.edu.room.frontend;

import mn.edu.room.frontend.Dto.BookingCreateRequest;
import mn.edu.room.frontend.Dto.RoomCreateRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.stream.Collectors;

@Controller
public class DashboardController {
    private static final Long DEMO_ADMIN_ID = 2L;

    private final MicroserviceClient client;

    public DashboardController(MicroserviceClient client) {
        this.client = client;
    }

    @GetMapping("/")
    public String dashboard(Model model) {
        var students = client.students();
        var rooms = client.rooms();
        var bookings = client.bookings();
        var notifications = client.notifications();
        model.addAttribute("students", students);
        model.addAttribute("rooms", rooms);
        model.addAttribute("bookings", bookings);
        model.addAttribute("notifications", notifications);
        model.addAttribute("studentNames", students.stream()
                .collect(Collectors.toMap(student -> student.id(), student -> student.firstName() + " " + student.lastName())));
        model.addAttribute("roomNames", rooms.stream()
                .collect(Collectors.toMap(room -> room.id(), room -> room.name())));
        model.addAttribute("pendingCount", bookings.stream().filter(booking -> booking.status() == BookingStatus.PENDING).count());
        model.addAttribute("approvedCount", bookings.stream().filter(booking -> booking.status() == BookingStatus.APPROVED).count());
        model.addAttribute("defaultDate", LocalDate.now().plusDays(1));
        model.addAttribute("defaultStart", "09:00");
        model.addAttribute("defaultEnd", "10:00");
        return "dashboard";
    }

    @PostMapping("/bookings")
    public String createBooking(
            @RequestParam Long studentId,
            @RequestParam Long roomId,
            @RequestParam LocalDate bookingDate,
            @RequestParam String startTime,
            @RequestParam String endTime,
            @RequestParam String purpose,
            RedirectAttributes redirectAttributes
    ) {
        try {
            client.createBooking(new BookingCreateRequest(
                    roomId,
                    studentId,
                    bookingDate,
                    LocalTime.parse(normalizeTime(startTime)),
                    LocalTime.parse(normalizeTime(endTime)),
                    purpose
            ));
            redirectAttributes.addFlashAttribute("success", "Хүсэлт амжилттай илгээгдлээ.");
        } catch (RuntimeException ex) {
            redirectAttributes.addFlashAttribute("error", client.messageFrom(ex));
        }
        return "redirect:/";
    }

    @PostMapping("/bookings/{id}/approve")
    public String approve(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            client.approveBooking(id, DEMO_ADMIN_ID);
            redirectAttributes.addFlashAttribute("success", "Хүсэлт батлагдлаа.");
        } catch (RuntimeException ex) {
            redirectAttributes.addFlashAttribute("error", client.messageFrom(ex));
        }
        return "redirect:/#requests";
    }

    @PostMapping("/bookings/{id}/reject")
    public String reject(@PathVariable Long id, @RequestParam String rejectReason, RedirectAttributes redirectAttributes) {
        try {
            client.rejectBooking(id, DEMO_ADMIN_ID, rejectReason);
            redirectAttributes.addFlashAttribute("success", "Хүсэлт татгалзагдлаа.");
        } catch (RuntimeException ex) {
            redirectAttributes.addFlashAttribute("error", client.messageFrom(ex));
        }
        return "redirect:/#requests";
    }

    @PostMapping("/bookings/{id}/cancel")
    public String cancel(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            client.cancelBooking(id);
            redirectAttributes.addFlashAttribute("success", "Хүсэлт цуцлагдлаа.");
        } catch (RuntimeException ex) {
            redirectAttributes.addFlashAttribute("error", client.messageFrom(ex));
        }
        return "redirect:/#requests";
    }

    @PostMapping("/bookings/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            client.deleteBooking(id);
            redirectAttributes.addFlashAttribute("success", "Хүсэлт устгагдлаа.");
        } catch (RuntimeException ex) {
            redirectAttributes.addFlashAttribute("error", client.messageFrom(ex));
        }
        return "redirect:/#requests";
    }

    @PostMapping("/rooms")
    public String createRoom(
            @RequestParam String name,
            @RequestParam int capacity,
            @RequestParam String location,
            RedirectAttributes redirectAttributes
    ) {
        try {
            client.createRoom(new RoomCreateRequest(name, capacity, location));
            redirectAttributes.addFlashAttribute("success", "Өрөө нэмэгдлээ.");
        } catch (RuntimeException ex) {
            redirectAttributes.addFlashAttribute("error", client.messageFrom(ex));
        }
        return "redirect:/#rooms";
    }

    private String normalizeTime(String value) {
        return value.length() == 5 ? value + ":00" : value;
    }
}
