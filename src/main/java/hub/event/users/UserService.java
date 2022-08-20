package hub.event.users;

// Auth comment left in comments to prevent auto delete during imports optimization
//import hub.event.auth.AuthService;

import hub.event.auth.AuthService;
import hub.event.users.dto.UserDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserDtoMapper userDtoMapper;

    public UserService(UserRepository userRepository, UserDtoMapper userDtoMapper) {
        this.userRepository = userRepository;
        this.userDtoMapper = userDtoMapper;

    }

    public Optional<UserDto> getUserById(Long id) {
        return userRepository.findById(id)
                .map(userDtoMapper::map);
    }

    public Optional<UserDto> getUserByUserName(String username) {
        return userRepository.findByUsername(username)
                .map(userDtoMapper::map);
    }

    public UserDto saveUser(UserDto userDto) {
        User userToSave = userDtoMapper.map(userDto);
        userToSave.setRegistrationDate(LocalDate.now());
        User savedUser = userRepository.save(userToSave);
        return userDtoMapper.map(savedUser);
    }

    @Transactional
    public Optional<UserDto> updateUser(Long id, UserDto userDto) {
        return userRepository.findById(id)
                .map(target -> setEntityFields(userDto, target))
                .map(userDtoMapper::map);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    private User setEntityFields(UserDto source, User target) {

        if (source.getUsername() != null) {
            target.setUsername(source.getUsername());
        }
        if (source.getEmail() != null) {
            target.setEmail(source.getEmail());
        }
        if (source.getRegistrationDate() != null) {
            target.setRegistrationDate(source.getRegistrationDate());
        }
        if (source.getBirthDate() != null) {
            target.setBirthDate(source.getBirthDate());
        }

        return target;
    }

}
