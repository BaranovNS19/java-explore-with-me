package practicum.ru.product.user;

import org.springframework.stereotype.Service;
import practicum.ru.product.dto.UserDto;

@Service
public class UserServiceImpl implements UserService{
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
}
