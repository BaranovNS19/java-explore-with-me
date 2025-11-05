package practicum.ru.product.event;

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
    public EventFullDto getEventFullDtoById(@PathVariable Long id) {
        return eventService.getEventFullDtoById(id);
    }

    @PatchMapping("/admin/events/{eventId}")
    public EventFullDto updateEventByAdmin(@PathVariable Long eventId,
                                           @RequestBody @Valid UpdateEventAdminRequestDto updateEventAdminRequestDto) {
        return eventService.updateEventByAdmin(eventId, updateEventAdminRequestDto);
    }
}
