package hub.event.users.user;

// Auth comment left in comments to prevent auto delete during imports optimization
//import hub.event.auth.AuthService;

import hub.event.users.filter.Filter;
import hub.event.users.filter.FilterDtoMapper;
import hub.event.users.filter.FilterService;
import hub.event.users.filter.dto.FilterDto;
import hub.event.users.user.dto.UserDto;
import hub.event.users.user.dto.UserDtoFull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserDtoMapper userDtoMapper;

    private final FilterDtoMapper filterDtoMapper;

    private final FilterService filterService;

    public UserService(UserRepository userRepository, FilterService filterService) {
        this.userRepository = userRepository;
        this.userDtoMapper = new UserDtoMapper();
        this.filterDtoMapper = new FilterDtoMapper();
        this.filterService = filterService;
    }

    //Tested
    public Optional<UserDto> getUserById(Long id) {
        return userRepository.findById(id)
                .map(userDtoMapper::map);
    }

    public Optional<UserDtoFull> getUserByIdWithFilters(Long id) {
        return userRepository.findById(id)
                .map(userDtoMapper::mapFull);
    }

    //tested
    public Optional<UserDto> getUserByUserName(String username) {
        return userRepository.findByUsername(username)
                .map(userDtoMapper::map);
    }

    @Transactional
    public Optional<UserDtoFull> getUserByUserNameWithFilters(String username) {
        return userRepository.findByUsername(username)
                .map(userDtoMapper::mapFull);
    }
    //tested
    public UserDto saveUser(UserDto userDto) {
        User userToSave = userDtoMapper.map(userDto);
        userToSave.setRegistrationDate(LocalDate.now());
        User savedUser = userRepository.save(userToSave);
        return userDtoMapper.map(savedUser);
    }


    //TODO Add validation that use_id in filter is OK - DONE
    @Transactional
    public UserDtoFull addFilterToUser(Long userID, FilterDto filterDto) {
        User userToSave = userRepository.findById(userID).orElseThrow();

        //UserID in fileterDtos validation
        if(filterDto.getUserId() != userID) {
            filterDto.setUserId(userID);
        }

        //TODO zadbać o inicjalizację listy filtrów - DONE
        //Initialize filters table
        userToSave.getFilters().size();

        //other way to do initialization
        //Hibernate.initialize(userToSave.getFilters());
        List<Filter> filters = userToSave.getFilters();

        FilterDto filterDtoSaved = filterService.saveFilter(filterDto);
        Filter filter = filterDtoMapper.map(filterDtoSaved);

        filters.add(filter);
        userToSave.setFilters(filters);

        User savedUser = userRepository.save(userToSave);
        return userDtoMapper.mapFull(savedUser);
    }

    //Tested
    @Transactional
    public Optional<UserDto> updateUser(Long id, UserDto userDto) {
        return userRepository.findById(id)
                .map(target -> setEntityFields(userDto, target))
                .map(userDtoMapper::map);
    }

    //Tested
    @Transactional
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    //Tested
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

    private User setEntityFieldsWithFilters(UserDtoFull source, User target) {

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

        if (source.getFilterDtos() != null && !source.getFilterDtos().isEmpty()) {
            target.setFilters(
                    source.getFilterDtos()
                            .stream()
                            .map(filterDtoMapper::map)
                            .toList()
            );
        }

        return target;
    }
    public Page<UserDto> getAll(Pageable pageable) {
        Page<User> allUsers = userRepository.findAll(pageable);
        return allUsers.map(userDtoMapper::map);
    }

    public Page<UserDtoFull> getAllWithFilters(Pageable pageable) {
        Page<User> allUsers = userRepository.findAllWithFilters(pageable);
        return allUsers.map(userDtoMapper::mapFull);
    }

    public UserDtoMapper getUserDtoMapper() {
        return userDtoMapper;
    }

    public FilterDtoMapper getFilterDtoMapper() {
        return filterDtoMapper;
    }
}
