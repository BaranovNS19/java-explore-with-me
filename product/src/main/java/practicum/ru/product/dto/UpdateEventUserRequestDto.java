package practicum.ru.product.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import practicum.ru.product.event.Location;
import practicum.ru.product.event.StateAction;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateEventUserRequestDto {
    private Long id;
    @Size(min = 20, max = 2000, message = "Длина краткого описания должна быть от {min} до {max} символов")
    private String annotation;
    private Long category;
    @Size(min = 20, max = 7000, message = "Длина описания должна быть от {min} до {max} символов")
    private String description;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;
    private Location location;
    private Boolean paid;
    @Min(value = 0, message = "Лимит участников не может быть отрицательным")
    private Integer participantLimit;
    private Boolean requestModeration;
    @Size(min = 3, max = 120, message = "Длина заголовка должна быть от {min} до {max} символов")
    private String title;
    private StateAction stateAction;
}
