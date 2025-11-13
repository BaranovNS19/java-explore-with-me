package practicum.ru.product.event;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import practicum.ru.product.category.Category;
import practicum.ru.product.category.CategoryService;
import practicum.ru.product.client.StatisticFeignClient;
import practicum.ru.product.dto.*;
import practicum.ru.product.exception.BadRequestException;
import practicum.ru.product.exception.ConflictException;
import practicum.ru.product.exception.NotFoundException;
import practicum.ru.product.user.User;
import practicum.ru.product.user.UserMapper;
import practicum.ru.product.user.UserService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final CategoryService categoryService;
    private final EventMapper eventMapper;
    private final UserService userService;
    private final UserMapper userMapper;
    private final StatisticFeignClient statisticFeignClient;

    @Autowired
    public EventServiceImpl(EventRepository eventRepository, CategoryService categoryService, EventMapper eventMapper,
                            UserService userService, UserMapper userMapper, StatisticFeignClient statisticFeignClient) {
        this.eventRepository = eventRepository;
        this.categoryService = categoryService;
        this.eventMapper = eventMapper;
        this.userService = userService;
        this.userMapper = userMapper;
        this.statisticFeignClient = statisticFeignClient;
    }

    @Override
    public EventFullDto createEvent(Long userId, NewEventDto newEventDto) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        Category category = categoryService.getCategoryById(newEventDto.getCategory());
        User user = userService.getUserById(userId);
        if (LocalDateTime.parse(newEventDto.getEventDate(), formatter).isBefore(LocalDateTime.now())) {
            throw new BadRequestException("Дата события не может быть раньше текущей");
        }
        Event event = eventRepository.save(eventMapper.toEvent(newEventDto, category, user, Status.PENDING));
        return eventMapper.toEventFullDto(event, userMapper.toUserShortDto(user),
                eventMapper.toLocationDto(event.getLocation()), getViewByEvent(event.getId()));
    }

    @Override
    public List<EventShortDto> getEventsByUser(Long userId, int from, int size) {
        userService.getUserById(userId);
        List<Event> events = eventRepository.findByInitiatorId(userId);
        List<EventShortDto> result = new ArrayList<>();
        for (Event e : events) {
            result.add(eventMapper.toEventShortDto(e, getViewByEvent(e.getId())));
        }
        return result.stream()
                .skip(from)
                .limit(size)
                .collect(Collectors.toList());
    }

    @Override
    public EventFullDto getEventByUser(Long userId, Long eventId) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        User user = userService.getUserById(userId);
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Событие с id [" + eventId + "] не найдено"));
        return eventMapper.toEventFullDto(event, userMapper.toUserShortDto(user),
                eventMapper.toLocationDto(event.getLocation()), getViewByEvent(event.getId()));
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

        if (event.getState().equals(Status.PUBLISHED)) {
            throw new ConflictException("нельзя обновлять опубликованное событие");
        }

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
            if (updateEventUserRequestDto.getEventDate().isBefore(LocalDateTime.now())) {
                throw new BadRequestException("Дата события не может быть раньше текущей даты");
            }
            event.setEventDate(updateEventUserRequestDto.getEventDate());
        }
        if (updateEventUserRequestDto.getLocation() != null) {
            event.setLocation(eventMapper.toLocation(updateEventUserRequestDto.getLocation()));
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
                eventMapper.toLocationDto(event.getLocation()), getViewByEvent(event.getId()));
    }

    @Override
    public EventFullDto getEventFullDtoById(Long id, HttpServletRequest request) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Событие с id [" + id + "] не найдено"));
        if (!event.getState().equals(Status.PUBLISHED)) {
            throw new NotFoundException("Событие с id [" + id + "] не найдено");
        }
        VisitPostRequestDto visitPostRequestDto = new VisitPostRequestDto();
        visitPostRequestDto.setApp("ewm-main-service");
        visitPostRequestDto.setIp(request.getRemoteAddr());
        visitPostRequestDto.setUri(request.getRequestURI());
        visitPostRequestDto.setTimestamp(LocalDateTime.now());
        statisticFeignClient.addVisit(visitPostRequestDto);
        eventRepository.save(event);
        return eventMapper.toEventFullDto(event, userMapper.toUserShortDto(event.getInitiator()),
                eventMapper.toLocationDto(event.getLocation()), getViewByEvent(event.getId()));
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
            if (updateEventAdminRequestDto.getEventDate().isBefore(LocalDateTime.now())) {
                throw new BadRequestException("Дата события не может быть раньше текущей даты");
            }
            event.setEventDate(updateEventAdminRequestDto.getEventDate());
        }
        if (updateEventAdminRequestDto.getLocation() != null) {
            event.setLocation(eventMapper.toLocation(updateEventAdminRequestDto.getLocation()));
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
        if (updateEventAdminRequestDto.getStateAction() != null) {
            if (event.getState().equals(Status.PUBLISHED) &&
                    updateEventAdminRequestDto.getStateAction().equals(StateAction.REJECT_EVENT)) {
                throw new ConflictException("Нельзя отменять опубликованое событие");
            }
            if (event.getState().equals(Status.PUBLISHED) &&
                    updateEventAdminRequestDto.getStateAction().equals(StateAction.PUBLISH_EVENT)) {
                throw new ConflictException("Событие уже опубликовано");
            }
            if (event.getState().equals(Status.CANCELED) &&
                    updateEventAdminRequestDto.getStateAction().equals(StateAction.PUBLISH_EVENT)) {
                throw new ConflictException("Событие уже опубликовано");
            }
            if (updateEventAdminRequestDto.getStateAction().equals(StateAction.PUBLISH_EVENT)) {
                event.setState(Status.PUBLISHED);
                event.setPublishedOn(LocalDateTime.now());
            }
            if (updateEventAdminRequestDto.getStateAction().equals(StateAction.REJECT_EVENT)) {
                event.setState(Status.CANCELED);
            }
        }
        eventRepository.save(event);
        return eventMapper.toEventFullDto(event, userMapper.toUserShortDto(event.getInitiator()),
                eventMapper.toLocationDto(event.getLocation()), getViewByEvent(event.getId()));
    }

    @Override
    public List<EventShortDto> getEvents(String text, List<Long> categories, Boolean paid, String rangeStart,
                                         String rangeEnd, Boolean onlyAvailable, String sort, int from, int size,
                                         HttpServletRequest request) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        VisitPostRequestDto visitPostRequestDto = new VisitPostRequestDto();
        visitPostRequestDto.setApp("ewm-main-service");
        visitPostRequestDto.setIp(request.getRemoteAddr());
        visitPostRequestDto.setUri(request.getRequestURI());
        visitPostRequestDto.setTimestamp(LocalDateTime.now());
        statisticFeignClient.addVisit(visitPostRequestDto);
        log.info("Запрос в сервис статистики {} {} {}", request.getServerName(), request.getServerPort(), request.getRequestURI());
        LocalDateTime start;
        LocalDateTime end;
        if (rangeStart != null) {
            start = LocalDateTime.parse(rangeStart, formatter);
        } else {
            start = null;
        }
        if (rangeEnd != null) {
            end = LocalDateTime.parse(rangeEnd, formatter);
        } else {
            end = null;
        }
        if (rangeStart != null && rangeEnd != null) {
            if (end.isBefore(start)) {
                throw new BadRequestException("Дата окончания не может быть раньше даты начала");
            }
        }
        List<Event> events = eventRepository.findEvents(text, categories, paid, start, end, onlyAvailable);
        List<EventShortDto> result = new ArrayList<>();
        for (Event e : events) {
            result.add(eventMapper.toEventShortDto(e, getViewByEvent(e.getId())));
        }
        return result.stream()
                .skip(from)
                .limit(size)
                .collect(Collectors.toList());
    }

    @Override
    public List<EventFullDto> getEventsByAdmin(List<Long> users, List<String> states, List<Long> categories, String rangeStart,
                                               String rangeEnd, int from, int size) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        LocalDateTime startDateTime;
        LocalDateTime endDateTime;
        if (rangeStart != null) {
            startDateTime = LocalDateTime.parse(rangeStart, formatter);
        } else {
            startDateTime = null;
        }
        if (rangeEnd != null) {
            endDateTime = LocalDateTime.parse(rangeEnd, formatter);
        } else {
            endDateTime = null;
        }
        List<Status> statusList = new ArrayList<>();
        if (states != null && !states.isEmpty()) {
            for (String s : states) {
                statusList.add(Status.valueOf(s.toUpperCase()));
            }
        }
        List<Event> events = eventRepository.findEventsByAdmin(users, statusList, categories,
                startDateTime, endDateTime);
        List<EventFullDto> result = new ArrayList<>();
        for (Event e : events) {
            result.add(eventMapper.toEventFullDto(e, userMapper.toUserShortDto(e.getInitiator()),
                    eventMapper.toLocationDto(e.getLocation()), getViewByEvent(e.getId())));
        }
        return result.stream()
                .skip(from)
                .limit(size)
                .collect(Collectors.toList());
    }

    @Override
    public int getViewByEvent(Long id) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        List<String> uris = new ArrayList<>();
        uris.add("/events/" + id);
        String startDate = LocalDateTime.now().minusDays(1).format(formatter);
        String endDate = LocalDateTime.now().plusDays(1).format(formatter);
        List<VisitGetResponseDto> views = statisticFeignClient.getVisits(startDate, endDate, uris, true);
        if (views.isEmpty()) {
            return 0;
        }
        return Math.toIntExact(views.getFirst().getHits());
    }
}
