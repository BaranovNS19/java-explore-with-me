package practicum.ru.product.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewEventDto {
    @NotBlank
    @Size(min = 20, max = 2000, message = "Длина краткого описания должна быть от {min} до {max} символов")
    private String annotation;
    @NotNull
    private Long category;
    @NotBlank
    @Size(min = 20, max = 7000, message = "Длина описания должна быть от {min} до {max} символов")
    private String description;
    @NotBlank
    private String eventDate;
    @NotNull
    private LocationDto location;
    private boolean paid;
    @Min(value = 0, message = "Лимит участников не может быть отрицательным")
    private int participantLimit;
    private boolean requestModeration = true;
    @Size(min = 3, max = 120, message = "Длина заголовка должна быть от {min} до {max} символов")
    private String title;

}
