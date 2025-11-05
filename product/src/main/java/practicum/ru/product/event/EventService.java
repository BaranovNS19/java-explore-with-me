package practicum.ru.product.event;

import practicum.ru.product.dto.*;

import java.util.List;

public interface EventService {
    EventFullDto createEvent(Long userId, NewEventDto newEventDto);

    List<EventShortDto> getEventsByUser(Long userId, int from, int size);

    EventFullDto getEventByUser(Long userId, Long eventId);

    Event getEventById(Long id);

    EventFullDto updateEvent(UpdateEventUserRequestDto updateEventUserRequestDto, Long userId, Long eventId);

    EventFullDto getEventFullDtoById(Long id);

    EventFullDto updateEventByAdmin(Long eventId, UpdateEventAdminRequestDto updateEventAdminRequestDto);
}
