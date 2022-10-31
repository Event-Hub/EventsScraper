package hub.event.users.user;

import hub.event.users.filter.Filter;
import hub.event.users.filter.FilterDtoMapper;
import hub.event.users.filter.FilterService;
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
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest()
@ActiveProfiles(profiles = {"dev", "test"})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserServiceTest {


    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FilterService filterService;

    @BeforeEach
    void setUp() {
        userService = new UserService(userRepository, filterService);


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



        LocalDateTime fromHourFilter1 = LocalDateTime.of(2020, 7, 13, 2, 46);
        LocalDateTime toHourFilter1 = LocalDateTime.of(2025, 1, 21, 18, 9);
        LocalDateTime fromHourFilter2 = LocalDateTime.of(2020, 3, 5, 8, 9);
        LocalDateTime toHourFilter2 = LocalDateTime.of(2024, 12, 2, 8, 2);

        //ManytoOne spowodowało by problem z ustawieniem filtru -> z userem -> filtrami -> userami ...
        List<Filter> filters = Arrays.asList(
                new Filter(62L, 75L, 3L, "Ćmielów - Hip Hop | Rap",
                        ZonedDateTime.of(fromHourFilter1, ZoneId.of("Europe/Warsaw")),
                        ZonedDateTime.of(toHourFilter1, ZoneId.of("Europe/Warsaw"))
                ),
                new Filter(88L, 32L, 3L, "Ślesin - Rock",
                        ZonedDateTime.of(fromHourFilter2, ZoneId.of("Europe/Warsaw")),
                        ZonedDateTime.of(toHourFilter2, ZoneId.of("Europe/Warsaw"))
                )
        );


        FilterDtoMapper filterDtoMapper = userService.getFilterDtoMapper();

        UserDtoFull given = new UserDtoFull(3L, "Buster Cora", "buster.cora@gmail.com",
                LocalDate.of(2022, 7, 20),
                LocalDate.of(1968, 2, 24),
                filters.stream().map(filterDtoMapper::map).toList());

        //when
        Optional<UserDtoFull> resultOptional = userService.getUserByUserNameWithFilters("Buster Cora");
        UserDtoFull result = resultOptional.orElse(new UserDtoFull());

        System.out.println("Given:");
        System.out.println(given);
        given.getFilterDtos().forEach(System.out::println);
        System.out.println("Result:");
        System.out.println(result);
        result.getFilterDtos().forEach(System.out::println);

        //then
        assertEquals(given, result);

    }

    @Test
    @DisplayName("Test of adding filter to user")
    @Order(9)
    void addFilterToUserTest() {
        //given


        //when

        UserDtoFull userDtoFull = userService.addFilterToUser(103L,
                new FilterDto(null, 20L, null, "Biecz - Jazz | Blues",
                        ZonedDateTime.of(
                                LocalDateTime.of(2022, 6, 25, 15, 20),
                                ZoneId.of("UTC")
                        ),
                        ZonedDateTime.of(
                                LocalDateTime.of(2022, 6, 26, 23, 50),
                                ZoneId.of("UTC")
                        )
                )
        );
        //then

        Optional<UserDtoFull> userByIdWithFiltersAfterTest = userService.getUserByIdWithFilters(103L);

        assertIterableEquals(userDtoFull.getFilterDtos(),userByIdWithFiltersAfterTest.orElse(new UserDtoFull()).getFilterDtos());
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

        //ManytoOne spowodowało by problem z ustawieniem filtru -> z userem -> filtrami -> userami ...
        List<Filter> givenFiltersForIdFour = Arrays.asList(
                new Filter(24L, 70L, 4L, "Biecz - Jazz | Blues",
                        ZonedDateTime.of(
                                LocalDateTime.of(2020, 3, 15, 8, 51),
                                ZoneId.of("Europe/Warsaw")
                        ),
                        ZonedDateTime.of(
                                LocalDateTime.of(2024, 10, 19, 23, 38),
                                ZoneId.of("Europe/Warsaw")
                        )
                ),
                new Filter(34L, 52L, 4L, "Szczawnica - Jazz | Blues",
                        ZonedDateTime.of(
                                LocalDateTime.of(2020, 2, 7, 2, 20),
                                ZoneId.of("Europe/Warsaw")
                        ),
                        ZonedDateTime.of(
                                LocalDateTime.of(2024, 3, 15, 7, 36),
                                ZoneId.of("Europe/Warsaw")
                        )
                )
        );

        List<FilterDto> givenFilterDtosForIdFour = givenFiltersForIdFour.stream().map(userService.getFilterDtoMapper()::map).toList();


        //when
        PageRequest pageRequest = PageRequest.of(0, 10);


        //Tested method call
        Page<UserDtoFull> resultPage = userService.getAllWithFilters(pageRequest);

        List<UserDtoFull> userDtoFulls = resultPage.toList();

        Optional<UserDtoFull> userIdFourOptional = userDtoFulls.stream().filter(userDtoFull -> userDtoFull.getId() == 4L).findFirst();
        UserDtoFull userIdFour = userIdFourOptional.orElse(new UserDtoFull());

        List<FilterDto> resultFilterDtosForUserFour = null;
        if (userIdFour.getId() == 4L) {
            resultFilterDtosForUserFour = userIdFour.getFilterDtos();
        }

        UserDtoFull userIdSeven = userDtoFulls.get(6);


        List<FilterDto> resultFilterDtosForUserSeven = null;
        if (userIdSeven.getId() == 7L) {
            resultFilterDtosForUserSeven = userIdSeven.getFilterDtos();
        }

        List<Long> fistPageIds = userDtoFulls.stream().map(UserDtoFull::getId).toList();

        //then

        List<FilterDto> finalResultFilterDtosForUserFour = resultFilterDtosForUserFour;
        List<FilterDto> finalResultFilterDtosForUserSeven = resultFilterDtosForUserSeven;
        assertAll(
                //Test if first page contains User with Id 4
                () -> assertFalse(userIdFourOptional.isEmpty()),
                //Test if first page user Ids match expected Ids
                () -> assertEquals(givenIds, fistPageIds),
                //Test if User with Id 4 has expected filters
                () -> assertEquals(givenFilterDtosForIdFour, finalResultFilterDtosForUserFour),
                //Test if User with Id 7 has doesn't have filters as expected
                () -> {
                    assertNotNull(finalResultFilterDtosForUserSeven);
                    assertTrue(finalResultFilterDtosForUserSeven.isEmpty());
                }
        );
    }
}