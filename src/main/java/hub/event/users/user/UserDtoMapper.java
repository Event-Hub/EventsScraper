package hub.event.users.user;

import hub.event.users.user.dto.UserDto;
import hub.event.users.user.dto.UserDtoFull;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
class UserDtoMapper {


     public UserDto map(User user) {
        UserDto dto = new UserDto();

        dto.setId(user.getUserId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setRegistrationDate(user.getRegistrationDate());
        dto.setBirthDate(user.getBirthDate());

        return dto;
    }

    public User map(UserDto dto) {
        User user = new User();

        user.setUserId(dto.getId());
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setRegistrationDate(dto.getRegistrationDate());
        user.setBirthDate(dto.getBirthDate());
        user.setFilters(new ArrayList<>());

        return user;
    }

     public UserDtoFull mapFull(User user) {
        UserDtoFull dto = new UserDtoFull();

        dto.setId(user.getUserId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setRegistrationDate(user.getRegistrationDate());
        dto.setBirthDate(user.getBirthDate());
        dto.setFilters(user.getFilters());

        return dto;
    }

    public User mapFull(UserDtoFull dto) {
        User user = new User();

        user.setUserId(dto.getId());
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setRegistrationDate(dto.getRegistrationDate());
        user.setBirthDate(dto.getBirthDate());
        user.setFilters(dto.getFilters());

        return user;
    }
}
