package practicum.ru.product.event;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import practicum.ru.product.dto.*;

import java.util.List;

@RestController
public class EventController {

    private final EventService eventService;

    @Autowired
    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping("/users/{userId}/events")
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto createEvent(@PathVariable Long userId, @RequestBody @Valid NewEventDto newEventDto) {
        return eventService.createEvent(userId, newEventDto);
    }

    @GetMapping("/users/{userId}/events")
    public List<EventShortDto> getEventsByUser(@PathVariable Long userId,
                                               @RequestParam(defaultValue = "0") int from,
                                               @RequestParam(defaultValue = "10") int size) {
        return eventService.getEventsByUser(userId, from, size);
    }

    @GetMapping("/users/{userId}/events/{eventId}")
    private EventFullDto getEventByUser(@PathVariable Long userId, @PathVariable Long eventId) {
        return eventService.getEventByUser(userId, eventId);
    }

    @PatchMapping("/users/{userId}/events/{eventId}")
    private EventFullDto updateEvent(@RequestBody @Valid UpdateEventUserRequestDto updateEventUserRequestDto,
                                     @PathVariable Long userId,
                                     @PathVariable Long eventId) {
        return eventService.updateEvent(updateEventUserRequestDto, userId, eventId);
    }

    @GetMapping("/events/{id}")
    public EventFullDto getEventFullDtoById(@PathVariable Long id, HttpServletRequest request) {
        return eventService.getEventFullDtoById(id, request);
    }

    @PatchMapping("/admin/events/{eventId}")
    public EventFullDto updateEventByAdmin(@PathVariable Long eventId,
                                           @RequestBody @Valid UpdateEventAdminRequestDto updateEventAdminRequestDto) {
        return eventService.updateEventByAdmin(eventId, updateEventAdminRequestDto);
    }

    @GetMapping("/events")
    public List<EventShortDto> getEvents(@RequestParam(required = false) String text,
                                         @RequestParam(required = false) List<Long> categories,
                                         @RequestParam(required = false) Boolean paid,
                                         @RequestParam(required = false) String rangeStart,
                                         @RequestParam(required = false) String rangeEnd,
                                         @RequestParam(defaultValue = "false") Boolean onlyAvailable,
                                         @RequestParam(required = false) String sort,
                                         @RequestParam(defaultValue = "0") int from,
                                         @RequestParam(defaultValue = "10") int size,
                                         HttpServletRequest request) {
        return eventService.getEvents(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size,
                request);

    }

    @GetMapping("/admin/events")
    public List<EventFullDto> getEventsByAdmin(@RequestParam(required = false) List<Long> users,
                                        @RequestParam(required = false) List<String> states,
                                        @RequestParam(required = false) List<Long> categories,
                                        @RequestParam(required = false) String rangeStart,
                                        @RequestParam(required = false) String rangeEnd,
                                        @RequestParam(defaultValue = "0") int from,
                                        @RequestParam(defaultValue = "10") int size) {
        return eventService.getEventsByAdmin(users, states, categories, rangeStart, rangeEnd, from, size);

    }
}
