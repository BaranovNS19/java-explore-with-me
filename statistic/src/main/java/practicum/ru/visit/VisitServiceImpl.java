package practicum.ru.visit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import practicum.ru.visit.dto.VisitCountDto;
import practicum.ru.visit.dto.VisitGetResponseDto;
import practicum.ru.visit.dto.VisitPostRequestDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class VisitServiceImpl implements VisitService {
    private final VisitMapper visitMapper;
    private final VisitRepository visitRepository;

    @Autowired
    public VisitServiceImpl(VisitMapper visitMapper, VisitRepository visitRepository) {
        this.visitMapper = visitMapper;
        this.visitRepository = visitRepository;
    }

    @Override
    public Visit addVisit(VisitPostRequestDto visitPostRequestDto) {
        return visitRepository.save(visitMapper.toVisit(visitPostRequestDto));
    }

    @Override
    public List<VisitGetResponseDto> getVisits(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {
        List<VisitCountDto> visits;
        if (unique && (uris == null || uris.isEmpty())) {
            visits = visitRepository.findUniqueUrisWithVisitStats(start, end);
        } else if (unique && uris != null) {
            visits = visitRepository.findUniqueUrisByUrisWithVisitCount(start, end, uris);
        } else {
            visits = visitRepository.findUniqueUrisWithVisitCount(start, end);
        }
        List<VisitGetResponseDto> result = new ArrayList<>();
        for (VisitCountDto v : visits) {
            result.add(visitMapper.toVisitGetResponseDto(v));
        }
        return result;
    }

}
