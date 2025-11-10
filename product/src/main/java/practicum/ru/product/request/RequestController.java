package practicum.ru.product.request;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import practicum.ru.product.dto.EventRequestStatusUpdateRequestDto;
import practicum.ru.product.dto.EventRequestStatusUpdateResultDto;
import practicum.ru.product.dto.RequestDto;

import java.util.List;

@RestController
public class RequestController {

    private final RequestService requestService;

    @Autowired
    public RequestController(RequestService requestService) {
        this.requestService = requestService;
    }

    @PostMapping("/users/{userId}/requests")
    @ResponseStatus(HttpStatus.CREATED)
    public RequestDto createRequest(@PathVariable Long userId, @RequestParam Long eventId) {
        return requestService.createRequest(userId, eventId);
    }

    @PatchMapping("/users/{userId}/requests/{requestId}/cancel")
    public RequestDto cancelRequest(@PathVariable Long userId, @PathVariable Long requestId) {
        return requestService.cancelRequest(userId, requestId);
    }

    @PatchMapping("/users/{userId}/events/{eventId}/requests")
    public EventRequestStatusUpdateResultDto updateStatus(@PathVariable Long userId, @PathVariable Long eventId,
                                                          @RequestBody @Valid EventRequestStatusUpdateRequestDto eventRequestStatusUpdateRequestDto) {
        return requestService.updateStatus(userId, eventId, eventRequestStatusUpdateRequestDto);
    }

    @GetMapping("/users/{userId}/events/{eventId}/requests")
    public List<RequestDto> getRequestsByUserAndEvents(@PathVariable Long userId, @PathVariable Long eventId) {
        return requestService.getRequestsByUserAndEvents(userId, eventId);
    }

    @GetMapping("/users/{userId}/requests")
    public List<RequestDto> getRequestsByUser(@PathVariable Long userId) {
        return requestService.getRequestsByUser(userId);
    }
}
