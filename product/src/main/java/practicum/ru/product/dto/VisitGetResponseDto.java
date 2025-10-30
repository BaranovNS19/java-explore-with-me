package practicum.ru.product.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VisitGetResponseDto {
    private String app;
    private String uri;
    private Long hits;
}
