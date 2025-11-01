package practicum.ru.product.user;

import org.springframework.stereotype.Component;
import practicum.ru.product.dto.UserDto;

@Component
public class UserMapper {

    public User toUser(UserDto userDto) {
        User user = new User();
        user.setId(userDto.getId());
        user.setEmail(userDto.getEmail());
        user.setName(userDto.getName());
        return user;
    }
}
