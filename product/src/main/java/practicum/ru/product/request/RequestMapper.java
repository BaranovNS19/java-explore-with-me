package practicum.ru.product.request;

import org.springframework.stereotype.Component;
import practicum.ru.product.dto.EventRequestStatusUpdateResultDto;
import practicum.ru.product.dto.ParticipationRequestDto;
import practicum.ru.product.dto.RequestDto;

import java.util.ArrayList;
import java.util.List;

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

    public EventRequestStatusUpdateResultDto toEventRequestStatusUpdateResultDto(List<Request> requests) {
        EventRequestStatusUpdateResultDto result = new EventRequestStatusUpdateResultDto();
        List<ParticipationRequestDto> confirmed = new ArrayList<>();
        List<ParticipationRequestDto> rejected = new ArrayList<>();
        for (Request r : requests) {
            if (r.getStatus().equals(StatusRequest.CONFIRMED)) {
                confirmed.add(toParticipationRequestDto(r));
            } else {
                rejected.add(toParticipationRequestDto(r));
            }
        }
        result.setConfirmedRequests(confirmed);
        result.setRejectedRequests(rejected);
        return result;
    }

    public ParticipationRequestDto toParticipationRequestDto(Request request) {
        ParticipationRequestDto participationRequestDto = new ParticipationRequestDto();
        participationRequestDto.setCreated(request.getCreated());
        participationRequestDto.setEvent(request.getEvent().getId());
        participationRequestDto.setId(request.getId());
        participationRequestDto.setRequester(request.getRequester().getId());
        participationRequestDto.setStatus(request.getStatus());
        return participationRequestDto;
    }
}
