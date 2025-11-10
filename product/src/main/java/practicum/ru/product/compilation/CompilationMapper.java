package practicum.ru.product.compilation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import practicum.ru.product.dto.CompilationDto;
import practicum.ru.product.dto.EventShortDto;
import practicum.ru.product.dto.NewCompilationDto;
import practicum.ru.product.event.Event;
import practicum.ru.product.event.EventMapper;

import java.util.HashSet;
import java.util.Set;

@Component
public class CompilationMapper {

    private final EventMapper eventMapper;

    @Autowired
    public CompilationMapper(EventMapper eventMapper) {
        this.eventMapper = eventMapper;
    }

    public Compilation toCompilation(NewCompilationDto newCompilationDto, Set<Event> events) {
        Compilation compilation = new Compilation();
        compilation.setId(newCompilationDto.getId());
        compilation.setEvents(events);
        compilation.setPinned(newCompilationDto.isPinned());
        compilation.setTitle(newCompilationDto.getTitle());
        return compilation;
    }

    public CompilationDto toCompilationDto(Compilation compilation) {
        CompilationDto compilationDto = new CompilationDto();
        compilationDto.setId(compilation.getId());
        compilationDto.setPinned(compilation.isPinned());
        compilationDto.setTitle(compilation.getTitle());
        Set<Event> events = compilation.getEvents();
        Set<EventShortDto> eventsShort = new HashSet<>();
        if (events != null) {
            for (Event e : events) {
                eventsShort.add(eventMapper.toEventShortDto(e));
            }
        }
        compilationDto.setEvents(eventsShort);
        return compilationDto;
    }
}
