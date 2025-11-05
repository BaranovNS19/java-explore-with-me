package practicum.ru.product.request;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import practicum.ru.product.dto.RequestDto;
import practicum.ru.product.event.Event;
import practicum.ru.product.event.EventService;
import practicum.ru.product.event.Status;
import practicum.ru.product.exception.NotFoundException;
import practicum.ru.product.user.User;
import practicum.ru.product.user.UserService;

import java.time.LocalDateTime;

@Service
public class RequestServiceImpl implements RequestService {

    private static final Logger log = LoggerFactory.getLogger(RequestServiceImpl.class);
    private final RequestRepository requestRepository;
    private final UserService userService;
    private final EventService eventService;
    private final RequestMapper requestMapper;

    @Autowired
    public RequestServiceImpl(RequestRepository requestRepository, UserService userService, EventService eventService, RequestMapper requestMapper) {
        this.requestRepository = requestRepository;
        this.userService = userService;
        this.eventService = eventService;
        this.requestMapper = requestMapper;
    }

    @Override
    public RequestDto createRequest(Long userId, Long eventId) {
        User user = userService.getUserById(userId);
        Event event = eventService.getEventById(eventId);
        Request request = new Request();
        request.setRequester(user);
        request.setCreated(LocalDateTime.now());
        request.setEvent(event);
        request.setStatus(Status.PENDING);
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
        request.setStatus(Status.CANCELED);
        requestRepository.save(request);
        log.info("!!!!Дата после {}", request.getCreated());
        return requestMapper.toRequestDto(request);
    }

    public Request getRequestById(Long id) {
        return requestRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Запрос с id [" + id + "] не найден"));
    }
}
