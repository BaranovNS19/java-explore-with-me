package practicum.ru.visit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import practicum.ru.visit.dto.VisitGetResponseDto;
import practicum.ru.visit.dto.VisitPostRequestDto;

import java.time.LocalDateTime;
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
    public List<VisitGetResponseDto> getVisits(@RequestParam LocalDateTime start,
                                               @RequestParam LocalDateTime end,
                                               @RequestParam(required = false) List<String> uris,
                                               @RequestParam(required = false, defaultValue = "false") boolean unique){
        return visitService.getVisits(start, end, uris, unique);
    }
}
