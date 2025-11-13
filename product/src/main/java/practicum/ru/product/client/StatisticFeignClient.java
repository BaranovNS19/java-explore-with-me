package practicum.ru.product.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import practicum.ru.product.dto.Visit;
import practicum.ru.product.dto.VisitGetResponseDto;
import practicum.ru.product.dto.VisitPostRequestDto;

import java.util.List;

@FeignClient(
        name = "statisticsService",
        url = "${statistic-server.url}"
)
public interface StatisticFeignClient {

    @PostMapping("/hit")
    Visit addVisit(@RequestBody VisitPostRequestDto visit);

    @GetMapping("/stats")
    List<VisitGetResponseDto> getVisits(@RequestParam String start,
                                        @RequestParam String end,
                                        @RequestParam(required = false) List<String> uris,
                                        @RequestParam(required = false, defaultValue = "false") boolean unique);
}
