package practicum.ru.product.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import practicum.ru.product.request.StatusRequest;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventRequestStatusUpdateRequestDto {
    private Set<Long> requestIds;
    private StatusRequest status;
}
