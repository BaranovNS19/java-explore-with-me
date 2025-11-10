package practicum.ru.product.request;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import practicum.ru.product.dto.EventRequestStatusUpdateRequestDto;
import practicum.ru.product.dto.EventRequestStatusUpdateResultDto;
import practicum.ru.product.dto.RequestDto;
import practicum.ru.product.event.Event;
import practicum.ru.product.event.EventRepository;
import practicum.ru.product.event.EventService;
import practicum.ru.product.event.Status;
import practicum.ru.product.exception.BusinessException;
import practicum.ru.product.exception.ConflictException;
import practicum.ru.product.exception.NotFoundException;
import practicum.ru.product.user.User;
import practicum.ru.product.user.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class RequestServiceImpl implements RequestService {

    private static final Logger log = LoggerFactory.getLogger(RequestServiceImpl.class);
    private final RequestRepository requestRepository;
    private final UserService userService;
    private final EventService eventService;
    private final RequestMapper requestMapper;
    private final EventRepository eventRepository;

    @Autowired
    public RequestServiceImpl(RequestRepository requestRepository, UserService userService, EventService eventService, RequestMapper requestMapper, EventRepository eventRepository) {
        this.requestRepository = requestRepository;
        this.userService = userService;
        this.eventService = eventService;
        this.requestMapper = requestMapper;
        this.eventRepository = eventRepository;
    }

    @Override
    public RequestDto createRequest(Long userId, Long eventId) {
        User user = userService.getUserById(userId);
        Event event = eventService.getEventById(eventId);
        if (requestRepository.findByEventIdAndRequesterId(eventId, userId) != null) {
            throw new ConflictException("У пользователя [" + userId + "] уже существует заявка на участие в событиии [" + eventId + "]");
        }
        if (Objects.equals(event.getInitiator().getId(), userId)) {
            throw new ConflictException("Пользователь [" + userId + "] является владельцем события [" + eventId + "]");
        }
        if (!event.getState().equals(Status.PUBLISHED)) {
            throw new ConflictException("Событие [" + eventId + "] не опубликовано");
        }
        if (event.getParticipantLimit() != 0) {
            if (event.getParticipantLimit() == event.getConfirmedRequests()) {
                throw new ConflictException("исчерпан лимит заявок на событие [" + eventId + "]");
            }
        }
        Request request = new Request();
        request.setRequester(user);
        request.setCreated(LocalDateTime.now());
        request.setEvent(event);

        if (!event.isRequestModeration() || event.getParticipantLimit() == 0) {
            request.setStatus(StatusRequest.CONFIRMED);
            event.setConfirmedRequests(event.getConfirmedRequests() + 1);
        } else {
            request.setStatus(StatusRequest.PENDING);
        }
        Request result = requestRepository.save(request);
        return requestMapper.toRequestDto(result);
    }

    @Override
    public RequestDto cancelRequest(Long userId, Long requestId) {
        getRequestById(requestId);
        userService.getUserById(userId);
        Request request = requestRepository.findByIdAndRequesterId(requestId, userId)
                .orElseThrow(() -> new NotFoundException("Запрос с id [" + requestId + "] не найден у пользователя с id [" + userId + "]"));
        log.info("!!!!Дата до {}", request.getCreated());
        request.setStatus(StatusRequest.CANCELED);
        requestRepository.save(request);
        log.info("!!!!Дата после {}", request.getCreated());
        return requestMapper.toRequestDto(request);
    }

    @Override
    public EventRequestStatusUpdateResultDto updateStatus(Long userId,
                                                          Long eventId,
                                                          EventRequestStatusUpdateRequestDto eventRequestStatusUpdateRequestDto) {
        Event event = eventService.getEventById(eventId);
        if (!event.getInitiator().getId().equals(userId)) {
            throw new BusinessException("Пользователь [" + userId + "] не является владельцем события [" + event + "]");
        }
        List<Request> requests = new ArrayList<>();
        for (Long requestId : eventRequestStatusUpdateRequestDto.getRequestIds()) {
            Request request = getRequestById(requestId);
            if (request.getStatus().equals(StatusRequest.CONFIRMED) &&
                    eventRequestStatusUpdateRequestDto.getStatus().equals(StatusRequest.REJECTED)) {
                throw new ConflictException("невозможно отменить подтвержденный запрос");
            }
            request.setStatus(eventRequestStatusUpdateRequestDto.getStatus());
            if (eventRequestStatusUpdateRequestDto.getStatus().equals(StatusRequest.CONFIRMED)) {
                if (event.getParticipantLimit() > 0 &&
                        event.getConfirmedRequests() >= event.getParticipantLimit()) {
                    throw new ConflictException("Достигнут лимит участников события");
                }
                event.setConfirmedRequests(event.getConfirmedRequests() + 1);
                eventRepository.save(event);
            }
            requestRepository.save(request);
            requests.add(getRequestById(requestId));
        }
        return requestMapper.toEventRequestStatusUpdateResultDto(requests);
    }

    @Override
    public List<RequestDto> getRequestsByUserAndEvents(Long userId, Long eventId) {
        Event event = eventService.getEventById(eventId);
        if (!event.getInitiator().getId().equals(userId)) {
            throw new BusinessException("Пользователь [" + userId + "] не является владельцем события [" + event + "]");
        }
        List<Request> requests = requestRepository.findByEventId(eventId);
        List<RequestDto> result = new ArrayList<>();
        for (Request r : requests) {
            result.add(requestMapper.toRequestDto(r));
        }
        return result;
    }

    @Override
    public List<RequestDto> getRequestsByUser(Long userId) {
        List<Request> requests = requestRepository.findByRequesterId(userId);
        List<RequestDto> result = new ArrayList<>();
        for (Request r : requests) {
            result.add(requestMapper.toRequestDto(r));
        }
        return result;
    }

    public Request getRequestById(Long id) {
        return requestRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Запрос с id [" + id + "] не найден"));
    }
}
