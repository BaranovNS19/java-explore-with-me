package practicum.ru.product.user;

import practicum.ru.product.dto.UserDto;

import java.util.List;

public interface UserService {
    User createUser(UserDto userDto);

    void deleteUser(Long id);

    List<User> getUsers(List<Long> ids, int from, int size);

    User getUserById(Long id);
}
