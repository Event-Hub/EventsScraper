package hub.event.users.user;

import hub.event.users.user.dto.UserDto;
import org.springframework.stereotype.Service;

@Service
class UserDtoMapper {


    UserDto map(User user) {
        UserDto dto = new UserDto();

        dto.setId(user.getUserId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setRegistrationDate(user.getRegistrationDate());
        dto.setBirthDate(user.getBirthDate());

        return dto;
    }

    User map(UserDto dto) {
        User user = new User();

        user.setUserId(dto.getId());
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setRegistrationDate(dto.getRegistrationDate());
        user.setBirthDate(dto.getBirthDate());

        return user;
    }
}
