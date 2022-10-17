package hub.event.users.user;

import hub.event.users.filter.Filter;
import hub.event.users.filter.FilterDtoMapper;
import hub.event.users.filter.dto.FilterDto;
import hub.event.users.user.dto.UserDto;
import hub.event.users.user.dto.UserDtoFull;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
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
    @Order(5)
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
    @Order(6)
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
    @Order(8)
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
    @DisplayName("Test of finding user by username with filters")
    @Order(7)
//    @Disabled
    void getUserByUserNameWithFiltersTest() {
        //given
//        3,Buster Cora,buster.cora@gmail.com,2022-07-20,1968-02-24
        //        62,75,Ćmielów - Hip Hop | Rap,2020-07-13 00:46:00.000,2025-01-21 17:09:00.000,3
//        88,32,Ślesin - Rock,2020-03-05 07:09:00.000,2024-12-02 07:02:00.000,3


        List<Filter> filters = Arrays.asList(
                new Filter(62L, 75L, 3L, "Ćmielów - Hip Hop | Rap",
                        LocalDateTime.of(2020, 7, 13, 0, 46),
                        LocalDateTime.of(2025, 1, 21, 17, 9)),
                new Filter(88L, 32L, 3L, "Ślesin - Rock",
                        LocalDateTime.of(2020, 3, 5, 7, 9),
                        LocalDateTime.of(2024, 12, 2, 7, 2))
        );


        UserDtoFull given = new UserDtoFull(3L, "Buster Cora", "buster.cora@gmail.com",
                LocalDate.of(2022, 7, 20),
                LocalDate.of(1968, 2, 24),
                filters);

        //when
        Optional<UserDtoFull> resultOptional = userService.getUserByUserNameWithFilters("Buster Cora");
        UserDtoFull result = resultOptional.orElse(new UserDtoFull());

        //then
        assertEquals(given, result);

    }

    @Test
    @Order(10)
    void addFilterToUserTest() {
        //given

        //when
        Optional<UserDtoFull> userByIdWithFilters = userService.getUserByIdWithFilters(103L);

        UserDtoFull userDtoFull = userService.addFilterToUser(103L,
                new FilterDto(null, 20L, 103L, "Biecz - Jazz | Blues",
                        LocalDateTime.of(2022, 6, 25, 15, 20),
                        LocalDateTime.of(2022, 6, 26, 23, 50)));
        //then
        System.out.println("Filters of after adding");
        System.out.println(userDtoFull.getFilters());
        Optional<UserDtoFull> userByIdWithFilters1 = userService.getUserByIdWithFilters(103L);
        System.out.println("Filters from database");
        System.out.println(userByIdWithFilters1.orElse(new UserDtoFull()).getFilters());
    }

    @Test
    @DisplayName("Test of getting all users data without filters")
    @Order(3)
    void getAllTest() {
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
        assertAll(() -> assertEquals(102, totalElements),
                () -> assertEquals(11, totalPages),
                () -> assertEquals(givenUserDto, userDtos.get(6))
        );


    }

    @Test
    @Order(4)
//    @Disabled
    @DisplayName("Test of getting all users which have filters with filters data")
    void getAllWithFiltersTest() {

        //given
        List<Long> givenIds = Arrays.asList(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L);

        List<Filter> givenFiltersForIdFour = Arrays.asList(
                new Filter(24L, 70L, 4L, "Biecz - Jazz | Blues",
                        LocalDateTime.of(2020, 3, 15, 7, 51),
                        LocalDateTime.of(2024, 10, 19, 21, 38)),
                new Filter(34L, 52L, 4L, "Szczawnica - Jazz | Blues",
                        LocalDateTime.of(2020, 2, 7, 1, 20),
                        LocalDateTime.of(2024, 3, 15, 6, 36))
        );


        //when
        PageRequest pageRequest = PageRequest.of(0, 10);


        //Tested method call
        Page<UserDtoFull> resultPage = userService.getAllWithFilters(pageRequest);

        List<UserDtoFull> userDtoFulls = resultPage.toList();

        Optional<UserDtoFull> userIdFourOptional = userDtoFulls.stream().filter(userDtoFull -> userDtoFull.getId() == 4L).findFirst();
        UserDtoFull userIdFour = userIdFourOptional.orElse(new UserDtoFull());

        List<Filter> resultFiltersForUserFour = null;
        if (userIdFour.getId() == 4L) {
            resultFiltersForUserFour = userIdFour.getFilters();
        }

        UserDtoFull userIdSeven = userDtoFulls.get(6);
//        System.out.println(userIdSeven);
//        System.out.println(userIdSeven.getFilters());

        List<Filter> resultFiltersForUserSeven = null;
        if (userIdSeven.getId() == 7L) {
            resultFiltersForUserSeven = userIdSeven.getFilters();
        }

        List<Long> fistPageIds = userDtoFulls.stream().map(UserDtoFull::getId).toList();

        //then

        List<Filter> finalResultFiltersForUserFour = resultFiltersForUserFour;
        List<Filter> finalResultFiltersForUserSeven = resultFiltersForUserSeven;
        assertAll(
                //Test if first page contains User with Id 4
                () -> assertFalse(userIdFourOptional.isEmpty()),
                //Test if first page user Ids match expected Ids
                () -> assertEquals(givenIds, fistPageIds),
                //Test if User with Id 4 has expected filters
                () -> assertEquals(givenFiltersForIdFour, finalResultFiltersForUserFour),
                //Test if User with Id 7 has doesn't have filters as expected
                () -> {
                    assertNotNull(finalResultFiltersForUserSeven);
                    assertTrue(finalResultFiltersForUserSeven.isEmpty());
                }
        );
    }

    @Test
    @Order(9)
    void experimentTest() {
        Page<UserDto> all = userService.getAll(PageRequest.of(10, 10));
        all.toList().forEach(System.out::println);
    }
}
