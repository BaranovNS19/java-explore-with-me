package practicum.ru.product.event;

import jakarta.servlet.http.HttpServletRequest;
import practicum.ru.product.dto.*;

import java.util.List;

public interface EventService {
    EventFullDto createEvent(Long userId, NewEventDto newEventDto);

    List<EventShortDto> getEventsByUser(Long userId, int from, int size);

    EventFullDto getEventByUser(Long userId, Long eventId);

    Event getEventById(Long id);

    EventFullDto updateEvent(UpdateEventUserRequestDto updateEventUserRequestDto, Long userId, Long eventId);

    EventFullDto getEventFullDtoById(Long id, HttpServletRequest request);

    EventFullDto updateEventByAdmin(Long eventId, UpdateEventAdminRequestDto updateEventAdminRequestDto);

    List<EventShortDto> getEvents(String text,
                                  List<Long> categories,
                                  Boolean paid,
                                  String rangeStart,
                                  String rangeEnd,
                                  Boolean onlyAvailable,
                                  String sort,
                                  int from,
                                  int size,
                                  HttpServletRequest request);

    List<EventFullDto> getEventsByAdmin(List<Long> users,
                                 List<String> states,
                                 List<Long> categories,
                                 String rangeStart,
                                 String rangeEnd,
                                 int from,
                                 int size);
}
