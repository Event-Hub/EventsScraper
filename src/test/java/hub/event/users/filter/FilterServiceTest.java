package hub.event.users.filter;

import hub.event.users.filter.dto.FilterDto;
import hub.event.users.user.UserService;
import hub.event.users.user.dto.UserDto;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest()
@ActiveProfiles(profiles = {"dev", "test"})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class FilterServiceTest {

    @Autowired
    private FilterDtoMapper filterDtoMapper;

    @Autowired
    private FilterRepository filterRepository;

    private FilterService filterService;

    @BeforeEach
    void setUp() {
        filterService = new FilterService(filterRepository, filterDtoMapper);
    }

    @Test
    @DisplayName("Test of finding filter by Id")
    @Order(1)
    void getFilterByIdTest() {
        //given
        LocalDateTime fromHour = LocalDateTime.of(2021, 4, 15, 11, 29);
        LocalDateTime toHour = LocalDateTime.of(2025, 5, 4, 14, 16);
        FilterDto givenDto = new FilterDto(1L, 14L, 31L, "Biała Piska - Etno", fromHour, toHour);

        //when
        Optional<FilterDto> filterByIdOpt = filterService.getFilterById(1L);
        FilterDto actualDto = filterByIdOpt.orElse(new FilterDto());

        //then
        assertEquals(givenDto, actualDto);
    }

    @Test
    @DisplayName("Test of saving filter to database")
    @Order(2)
    void saveFilterTest() {
        //given
        LocalDateTime fromHour = LocalDateTime.of(2022, 2, 24, 6, 40);
        LocalDateTime toHour = LocalDateTime.of(2022, 9, 3, 21, 4);
        FilterDto givenFilterDto = new FilterDto(null, 30L, 1L, "Łódź Rock", fromHour, toHour);


        //when
        FilterDto savedFilterDto = filterService.saveFilter(givenFilterDto);

        //then
        assertAll(
                () -> assertEquals(102,savedFilterDto.getFilterId()),
                () -> assertEquals(givenFilterDto.getCityId(),savedFilterDto.getCityId()),
                () -> assertEquals(givenFilterDto.getUserId(),savedFilterDto.getUserId()),
                () -> assertEquals(givenFilterDto.getName(),savedFilterDto.getName()),
                () -> assertEquals(givenFilterDto.getFromHour(),savedFilterDto.getFromHour()),
                () -> assertEquals(givenFilterDto.getToHour(),savedFilterDto.getToHour())
        );

    }

    @Test
    @DisplayName("Test of updating filter")
    @Order(3)
    void updateFilterTest() {
        //given

        LocalDateTime fromHour = LocalDateTime.of(2021, 4, 15, 11, 29);
        LocalDateTime toHour = LocalDateTime.of(2025, 5, 4, 14, 16);
        FilterDto givenFilterDto = new FilterDto(1L, 20L, 31L, "Zakopane classic", fromHour, toHour);


        //when

        Optional<FilterDto> filterDtoOpt = filterService.updateFilter(1L,
                new FilterDto(null, 20L, null, "Zakopane classic", null, null));
        FilterDto actualFilterDto = filterDtoOpt.orElse(new FilterDto());

        //then
        assertEquals(givenFilterDto, actualFilterDto);

    }

    @Test
    @DisplayName("Test of deleting filter")
    @Order(5)
    void deleteFilterTest() {
        //given

        //when
        filterService.deleteFilter(2L);
        Optional<FilterDto> filterById = filterService.getFilterById(2L);

        //then
        assertTrue(filterById.isEmpty());

    }
}