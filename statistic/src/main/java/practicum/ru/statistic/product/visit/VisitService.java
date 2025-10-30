package practicum.ru.statistic.product.visit;

import practicum.ru.statistic.product.visit.dto.VisitGetResponseDto;
import practicum.ru.statistic.product.visit.dto.VisitPostRequestDto;

import java.time.LocalDateTime;
import java.util.List;

public interface VisitService {
    Visit addVisit(VisitPostRequestDto visit);

    List<VisitGetResponseDto> getVisits(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique);
}
