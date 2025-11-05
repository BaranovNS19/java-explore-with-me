package practicum.ru.product.user;

import org.springframework.stereotype.Service;
import practicum.ru.product.dto.UserDto;
import practicum.ru.product.exception.NotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public User createUser(UserDto userDto) {
        return userRepository.save(userMapper.toUser(userDto));
    }

    @Override
    public void deleteUser(Long id) {
        getUserById(id);
        userRepository.deleteById(id);
    }

    @Override
    public List<User> getUsers(List<Long> ids, int from, int size) {
        List<User> users;
        if (ids.isEmpty()) {
            users = userRepository.findAll();
        } else {
            users = userRepository.findAllById(ids);
        }
        return users.stream()
                .skip(from)
                .limit(size)
                .collect(Collectors.toList());
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь с id [" + id + "] не найден"));
    }
}
