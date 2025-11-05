package practicum.ru.product.request;

import practicum.ru.product.dto.RequestDto;

public interface RequestService {

    RequestDto createRequest(Long userId, Long eventId);

    RequestDto cancelRequest(Long userId, Long requestId);
}
