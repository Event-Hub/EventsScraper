package hub.event.users.user;

import hub.event.users.filter.FilterDtoMapper;
import hub.event.users.user.dto.UserDto;
import hub.event.users.user.dto.UserDtoFull;

import java.util.ArrayList;

class UserDtoMapper {

    private final FilterDtoMapper filterDtoMapper;

    UserDtoMapper() {
        filterDtoMapper = new FilterDtoMapper();
    }


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

        //TODO zadbać by w DTO nie było nigdzie encji - DONE
        // nie eksponuj encji - DONE

        dto.setFilterDtos(
                user.getFilters()
                        .stream()
                        .map(filterDtoMapper::map)
                        .toList()
        );

        return dto;
    }

    public User mapFull(UserDtoFull dto) {
        User user = new User();

        user.setUserId(dto.getId());
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setRegistrationDate(dto.getRegistrationDate());
        user.setBirthDate(dto.getBirthDate());
        user.setFilters(
                dto.getFilterDtos()
                        .stream()
                        .map(filterDtoMapper::map)
                        .toList()
        );

        return user;
    }


    public FilterDtoMapper getFilterDtoMapper() {
        return filterDtoMapper;
    }
}
