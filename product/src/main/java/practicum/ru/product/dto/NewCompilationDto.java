package practicum.ru.product.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewCompilationDto {
    private Long id;
    private Set<Long> events;
    private boolean pinned;
    @NotBlank
    @Size(min = 1, max = 50, message = "Длина заголовка должна быть от {min} до {max} символов")
    private String title;
}
