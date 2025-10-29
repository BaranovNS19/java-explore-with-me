package practicum.ru.visit;

import practicum.ru.visit.dto.VisitGetResponseDto;
import practicum.ru.visit.dto.VisitPostRequestDto;

import java.time.LocalDateTime;
import java.util.List;

public interface VisitService {
    Visit addVisit(VisitPostRequestDto visit);

    List<VisitGetResponseDto> getVisits(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique);
}
