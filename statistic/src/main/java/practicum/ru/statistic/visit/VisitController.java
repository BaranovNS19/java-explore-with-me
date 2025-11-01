package practicum.ru.statistic.visit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import practicum.ru.statistic.visit.dto.VisitGetResponseDto;
import practicum.ru.statistic.visit.dto.VisitPostRequestDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
public class VisitController {
    private final VisitService visitService;

    @Autowired
    public VisitController(VisitService visitService) {
        this.visitService = visitService;
    }

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public Visit addVisit(@RequestBody VisitPostRequestDto visit) {
        return visitService.addVisit(visit);
    }

    @GetMapping("/stats")
    public List<VisitGetResponseDto> getVisits(@RequestParam String start,
                                               @RequestParam String end,
                                               @RequestParam(required = false) List<String> uris,
                                               @RequestParam(required = false, defaultValue = "false") boolean unique) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime startDate = LocalDateTime.parse(start, formatter);
        LocalDateTime endDate = LocalDateTime.parse(end, formatter);
        return visitService.getVisits(startDate, endDate, uris, unique);
    }
}
