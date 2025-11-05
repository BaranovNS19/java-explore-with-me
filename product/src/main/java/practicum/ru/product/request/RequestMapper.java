package practicum.ru.product.request;

import org.springframework.stereotype.Component;
import practicum.ru.product.dto.RequestDto;

@Component
public class RequestMapper {

    public RequestDto toRequestDto(Request request) {
        RequestDto requestDto = new RequestDto();
        requestDto.setRequester(request.getRequester().getId());
        requestDto.setId(request.getId());
        requestDto.setCreated(request.getCreated());
        requestDto.setEvent(request.getEvent().getId());
        requestDto.setStatus(request.getStatus());
        return requestDto;
    }
}
