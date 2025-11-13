package practicum.ru.product.request;

import practicum.ru.product.dto.EventRequestStatusUpdateRequestDto;
import practicum.ru.product.dto.EventRequestStatusUpdateResultDto;
import practicum.ru.product.dto.RequestDto;

import java.util.List;

public interface RequestService {

    RequestDto createRequest(Long userId, Long eventId);

    RequestDto cancelRequest(Long userId, Long requestId);

    EventRequestStatusUpdateResultDto updateStatus(Long userId, Long eventId,
                                                   EventRequestStatusUpdateRequestDto eventRequestStatusUpdateRequestDto);

    List<RequestDto> getRequestsByUserAndEvents(Long userId, Long eventId);

    List<RequestDto> getRequestsByUser(Long userId);
}
