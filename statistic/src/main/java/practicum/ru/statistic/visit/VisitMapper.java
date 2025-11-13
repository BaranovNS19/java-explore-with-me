package practicum.ru.statistic.visit;

import org.springframework.stereotype.Component;
import practicum.ru.statistic.visit.dto.VisitCountDto;
import practicum.ru.statistic.visit.dto.VisitGetResponseDto;
import practicum.ru.statistic.visit.dto.VisitPostRequestDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class VisitMapper {

    public Visit toVisit(VisitPostRequestDto visitPostRequestDto) {
        Visit visit = new Visit();
        visit.setId(visitPostRequestDto.getId());
        visit.setApp(visitPostRequestDto.getApp());
        visit.setUri(visitPostRequestDto.getUri());
        visit.setIp(visitPostRequestDto.getIp());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        visit.setTimestamp(LocalDateTime.parse(visitPostRequestDto.getTimestamp(), formatter));
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
