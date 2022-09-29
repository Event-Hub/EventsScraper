package hub.event.users.user;

import hub.event.users.filter.FilterDtoMapper;
import hub.event.users.user.dto.UserDto;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest()
@ActiveProfiles(profiles = {"dev", "test"})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserServiceTest {


    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserDtoMapper userDtoMapper;

    @Autowired
    private FilterDtoMapper filterDtoMapper;

    @BeforeEach
    void setUp() {
        userService = new UserService(userRepository, userDtoMapper, filterDtoMapper);
    }

    @Test
    @DisplayName("Test of finding user by Id")
    @Order(1)
    void getUserByIdTest() {
        //given

        UserDto givenUser = new UserDto(1L, "testUser1", "testUser1@gmail.com", LocalDate.of(2022, 7, 8), LocalDate.of(1999, 7, 8));
        UserDto givenUser2 = new UserDto(2L, "testUser2", "testUser2@poczta.onet.pl", LocalDate.of(2022, 8, 17), LocalDate.of(2000, 6, 5));


        //when
        Optional<UserDto> userById = userService.getUserById(1L);
        Optional<UserDto> userById2 = userService.getUserById(2L);

        UserDto actualUser = userById.orElse(new UserDto());
        UserDto actualUser2 = userById2.orElse(new UserDto());


        //then
        assertAll(
                () -> assertEquals(givenUser, actualUser),
                () -> assertEquals(givenUser2, actualUser2)
        );
    }


    @Test
    @DisplayName("Test of finding user by username")
    @Order(2)
    void getUserByUserNameTest() {
        //given
        UserDto givenUser = new UserDto(1L, "testUser1", "testUser1@gmail.com", LocalDate.of(2022, 7, 8), LocalDate.of(1999, 7, 8));

        //when
        Optional<UserDto> userById = userService.getUserByUserName("testUser1");
        UserDto actualUser = userById.orElse(new UserDto());

        //then
        assertEquals(givenUser, actualUser);
    }

    @Test
    @DisplayName("Test of saving in database")
    @Order(3)
//    @Disabled
    void saveUserTest() {
        //given
        UserDto givenUserDto = new UserDto();
        givenUserDto.setUsername("TestUser");
        givenUserDto.setEmail("test.user@gmail.com");
        givenUserDto.setBirthDate(LocalDate.of(1989, 3, 12));


        //when
        UserDto userDtoSaved = userService.saveUser(givenUserDto);

        //then
        assertAll(
                () -> assertEquals(givenUserDto.getUsername(), userDtoSaved.getUsername()),
                () -> assertEquals(givenUserDto.getEmail(), userDtoSaved.getEmail()),
                () -> assertEquals(givenUserDto.getBirthDate(), userDtoSaved.getBirthDate()),
                () -> assertNotNull(userDtoSaved.getRegistrationDate()),
                () -> assertNotNull(userDtoSaved.getId())
        );

    }

    @Test
    @Order(4)
    @Transactional
    @DisplayName("Test of updating user")
    void updateUserTest() {
        //given
        UserDto givenUser = new UserDto(1L, "UpdatedName", "testUser1@gmail.com", LocalDate.of(2022, 7, 8), LocalDate.of(1999, 7, 8));
        //when
        userService.updateUser(1L, new UserDto(null, "UpdatedName", null, null, null));
        Optional<UserDto> userById = userService.getUserById(1L);
        UserDto actualUser = userById.orElse(new UserDto());

        //then
        assertEquals(givenUser, actualUser);

    }

    @Test
    @DisplayName("Test of deleting user")
    @Order(5)
//    @Disabled
    void deleteUserTest() {
        //given

        //when
        userService.deleteUser(2L);

        Optional<UserDto> userById = userService.getUserById(2L);

        //then
        assertTrue(userById.isEmpty());
    }

    @Test
    @Disabled
    void getUserByUserNameWithFiltersTest(){
        fail("Not implemented");
    }

    @Test
    @Disabled
    void addFilterToUserTest(){
        fail("Not implemented");
    }

    @Test
    @DisplayName("Test of getting all users data without filters")
    @Order(6)
    void getAllTest(){
        //given
        UserDto givenUserDto = new UserDto(7L, "Lorina Anjanette", "lorina.anjanette@gmail.com",
                LocalDate.of(2022, 3, 22), LocalDate.of(1979, 9, 18));

        //when
        PageRequest pageRequest = PageRequest.of(0, 10);

        Page<UserDto> resultPage = userService.getAll(pageRequest);

        long totalElements = resultPage.getTotalElements();
        List<UserDto> userDtos = resultPage.toList();
        int totalPages = resultPage.getTotalPages();

        //then
        assertAll(()->assertEquals(102,totalElements),
                ()->assertEquals(11,totalPages),
                ()->assertEquals(userDtos.get(5),givenUserDto)
        );
    }

    @Test
    @Disabled
    void getAllWithFiltersTest(){
        fail("Not implemented");
    }
}
