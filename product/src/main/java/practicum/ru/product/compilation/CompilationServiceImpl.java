package practicum.ru.product.compilation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import practicum.ru.product.dto.CompilationDto;
import practicum.ru.product.dto.NewCompilationDto;
import practicum.ru.product.event.Event;
import practicum.ru.product.event.EventService;
import practicum.ru.product.exception.NotFoundException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CompilationServiceImpl implements CompilationService {

    private final CompilationRepository compilationRepository;
    private final EventService eventService;
    private final CompilationMapper compilationMapper;

    @Autowired
    public CompilationServiceImpl(CompilationRepository compilationRepository, EventService eventService,
                                  CompilationMapper compilationMapper) {
        this.compilationRepository = compilationRepository;
        this.eventService = eventService;
        this.compilationMapper = compilationMapper;
    }

    @Override
    public CompilationDto createCompilation(NewCompilationDto newCompilationDto) {
        Compilation compilation = compilationRepository.save(compilationMapper.toCompilation(newCompilationDto,
                getEventsByIds(newCompilationDto.getEvents())));
        return compilationMapper.toCompilationDto(compilation);
    }

    @Override
    public List<CompilationDto> getCompilations(Boolean pinned, int from, int size) {
        List<Compilation> compilations;
        if (pinned == null) {
            compilations = compilationRepository.findAll();
        } else {
            compilations = compilationRepository.findByPinned(pinned);
        }
        List<CompilationDto> result = new ArrayList<>();
        for (Compilation c : compilations) {
            result.add(compilationMapper.toCompilationDto(c));
        }
        return result.stream()
                .skip(from)
                .limit(size)
                .collect(Collectors.toList());
    }

    @Override
    public CompilationDto getCompilationById(Long compId) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Подборка с id [" + compId + "] не найден"));
        return compilationMapper.toCompilationDto(compilation);
    }

    @Override
    public void deleteCompilation(Long compId) {
        getCompilationById(compId);
        compilationRepository.deleteById(compId);
    }

    private Set<Event> getEventsByIds(Set<Long> ids) {
        Set<Event> result = new HashSet<>();
        for (Long id : ids) {
            Event event = eventService.getEventById(id);
            result.add(event);
        }
        return result;
    }
}
