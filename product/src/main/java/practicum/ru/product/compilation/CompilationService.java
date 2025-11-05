package practicum.ru.product.compilation;

import practicum.ru.product.dto.CompilationDto;
import practicum.ru.product.dto.NewCompilationDto;

import java.util.List;

public interface CompilationService {
    CompilationDto createCompilation(NewCompilationDto newCompilationDto);

    List<CompilationDto> getCompilations(Boolean pinned, int from, int size);

    CompilationDto getCompilationById(Long compId);

    void deleteCompilation(Long compId);
}
