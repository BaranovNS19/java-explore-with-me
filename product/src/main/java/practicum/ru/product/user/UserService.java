package practicum.ru.product.user;

import practicum.ru.product.dto.UserDto;

public interface UserService {
    User createUser(UserDto userDto);
}
