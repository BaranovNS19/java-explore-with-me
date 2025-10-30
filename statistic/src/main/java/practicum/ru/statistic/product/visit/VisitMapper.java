package practicum.ru.statistic.product.visit;

import org.springframework.stereotype.Component;
import practicum.ru.statistic.product.visit.dto.VisitCountDto;
import practicum.ru.statistic.product.visit.dto.VisitGetResponseDto;
import practicum.ru.statistic.product.visit.dto.VisitPostRequestDto;

@Component
public class VisitMapper {

    public Visit toVisit(VisitPostRequestDto visitPostRequestDto) {
        Visit visit = new Visit();
        visit.setId(visitPostRequestDto.getId());
        visit.setApp(visitPostRequestDto.getApp());
        visit.setUri(visitPostRequestDto.getUri());
        visit.setIp(visitPostRequestDto.getIp());
        visit.setTimestamp(visitPostRequestDto.getTimestamp());
        return visit;
    }

    public VisitGetResponseDto toVisitGetResponseDto(VisitCountDto visitCountDto) {
        VisitGetResponseDto visitGetResponseDto = new VisitGetResponseDto();
        visitGetResponseDto.setApp(visitCountDto.getApp());
        visitGetResponseDto.setUri(visitCountDto.getUri());
        visitGetResponseDto.setHits(visitCountDto.getVisitCount());
        return visitGetResponseDto;
    }
}
