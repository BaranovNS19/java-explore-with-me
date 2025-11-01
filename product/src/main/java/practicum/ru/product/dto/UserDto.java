package practicum.ru.product.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private Long id;
    @NotBlank
    @Email
    @Size(min = 6, max = 254, message = "Длина email должна быть от {min} до {max} символов")
    private String email;
    @NotBlank
    @Size(min = 2, max = 250, message = "Длина имени должна быть от {min} до {max} символов")
    private String name;
}
