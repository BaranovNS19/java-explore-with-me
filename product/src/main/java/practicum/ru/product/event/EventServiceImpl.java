package practicum.ru.product.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import practicum.ru.product.category.Category;
import practicum.ru.product.category.CategoryService;
import practicum.ru.product.dto.*;
import practicum.ru.product.exception.NotFoundException;
import practicum.ru.product.user.User;
import practicum.ru.product.user.UserMapper;
import practicum.ru.product.user.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final CategoryService categoryService;
    private final EventMapper eventMapper;
    private final UserService userService;
    private final UserMapper userMapper;

    @Autowired
    public EventServiceImpl(EventRepository eventRepository, CategoryService categoryService, EventMapper eventMapper,
                            UserService userService, UserMapper userMapper) {
        this.eventRepository = eventRepository;
        this.categoryService = categoryService;
        this.eventMapper = eventMapper;
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @Override
    public EventFullDto createEvent(Long userId, NewEventDto newEventDto) {
        Category category = categoryService.getCategoryById(newEventDto.getCategory());
        User user = userService.getUserById(userId);
        Event event = eventRepository.save(eventMapper.toEvent(newEventDto, category, user, Status.PENDING));
        return eventMapper.toEventFullDto(event, userMapper.toUserShortDto(user),
                eventMapper.toLocationDto(event.getLocation()));
    }

    @Override
    public List<EventShortDto> getEventsByUser(Long userId, int from, int size) {
        userService.getUserById(userId);
        List<Event> events = eventRepository.findByInitiatorId(userId);
        List<EventShortDto> result = new ArrayList<>();
        for (Event e : events) {
            result.add(eventMapper.toEventShortDto(e));
        }
        return result.stream()
                .skip(from)
                .limit(size)
                .collect(Collectors.toList());
    }

    @Override
    public EventFullDto getEventByUser(Long userId, Long eventId) {
        User user = userService.getUserById(userId);
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Событие с id [" + eventId + "] не найдено"));
        return eventMapper.toEventFullDto(event, userMapper.toUserShortDto(user),
                eventMapper.toLocationDto(event.getLocation()));
    }

    @Override
    public Event getEventById(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Событие с id [" + id + "] не найдено"));
    }

    @Override
    public EventFullDto updateEvent(UpdateEventUserRequestDto updateEventUserRequestDto, Long userId, Long eventId) {
        Event event = getEventById(eventId);
        getEventByUser(userId, eventId);

        if (updateEventUserRequestDto.getAnnotation() != null) {
            event.setAnnotation(updateEventUserRequestDto.getAnnotation());
        }
        if (updateEventUserRequestDto.getCategory() != null) {
            event.setCategory(categoryService.getCategoryById(updateEventUserRequestDto.getCategory()));
        }
        if (updateEventUserRequestDto.getDescription() != null) {
            event.setDescription(updateEventUserRequestDto.getDescription());
        }
        if (updateEventUserRequestDto.getEventDate() != null) {
            event.setEventDate(LocalDateTime.parse(updateEventUserRequestDto.getEventDate()));
        }
        if (updateEventUserRequestDto.getLocation() != null) {
            event.setLocation(updateEventUserRequestDto.getLocation());
        }
        if (updateEventUserRequestDto.getPaid() != null) {
            event.setPaid(updateEventUserRequestDto.getPaid());
        }
        if (updateEventUserRequestDto.getParticipantLimit() != null) {
            event.setParticipantLimit(updateEventUserRequestDto.getParticipantLimit());
        }
        if (updateEventUserRequestDto.getRequestModeration() != null) {
            event.setRequestModeration(updateEventUserRequestDto.getRequestModeration());
        }
        if (updateEventUserRequestDto.getTitle() != null) {
            event.setTitle(updateEventUserRequestDto.getTitle());
        }
        if (updateEventUserRequestDto.getStateAction() != null) {
            if (updateEventUserRequestDto.getStateAction().equals(StateAction.SEND_TO_REVIEW)) {
                event.setState(Status.PENDING);
            } else {
                event.setState(Status.CANCELED);
            }

        }
        eventRepository.save(event);
        return eventMapper.toEventFullDto(event, userMapper.toUserShortDto(userService.getUserById(userId)),
                eventMapper.toLocationDto(event.getLocation()));
    }

    @Override
    public EventFullDto getEventFullDtoById(Long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Событие с id [" + id + "] не найдено"));
        return eventMapper.toEventFullDto(event, userMapper.toUserShortDto(event.getInitiator()),
                eventMapper.toLocationDto(event.getLocation()));
    }

    @Override
    public EventFullDto updateEventByAdmin(Long eventId, UpdateEventAdminRequestDto updateEventAdminRequestDto) {
        Event event = getEventById(eventId);

        if (updateEventAdminRequestDto.getAnnotation() != null) {
            event.setAnnotation(updateEventAdminRequestDto.getAnnotation());
        }
        if (updateEventAdminRequestDto.getCategory() != null) {
            event.setCategory(categoryService.getCategoryById(updateEventAdminRequestDto.getCategory()));
        }
        if (updateEventAdminRequestDto.getDescription() != null) {
            event.setDescription(updateEventAdminRequestDto.getDescription());
        }
        if (updateEventAdminRequestDto.getEventDate() != null) {
            event.setEventDate(updateEventAdminRequestDto.getEventDate());
        }
        if (updateEventAdminRequestDto.getLocation() != null) {
            event.setLocation(updateEventAdminRequestDto.getLocation());
        }
        if (updateEventAdminRequestDto.getPaid() != null) {
            event.setPaid(updateEventAdminRequestDto.getPaid());
        }
        if (updateEventAdminRequestDto.getParticipantLimit() != null) {
            event.setParticipantLimit(updateEventAdminRequestDto.getParticipantLimit());
        }
        if (updateEventAdminRequestDto.getRequestModeration() != null) {
            event.setRequestModeration(updateEventAdminRequestDto.getRequestModeration());
        }
        if (updateEventAdminRequestDto.getTitle() != null) {
            event.setTitle(updateEventAdminRequestDto.getTitle());
        }
        eventRepository.save(event);
        return eventMapper.toEventFullDto(event, userMapper.toUserShortDto(event.getInitiator()),
                eventMapper.toLocationDto(event.getLocation()));
    }
}
