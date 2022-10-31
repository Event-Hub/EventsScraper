package hub.event.users.filter;

import hub.event.users.filter.dto.FilterDto;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest()
@ActiveProfiles(profiles = {"dev", "test"})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class FilterServiceTest {


    @Autowired
    private FilterRepository filterRepository;

    private FilterService filterService;

    @BeforeEach
    void setUp() {
        filterService = new FilterService(filterRepository);
    }

    @Test
    @DisplayName("Test of finding filter by Id")
    @Order(1)
    void getFilterByIdTest() {
        //given
        ZonedDateTime fromHour = ZonedDateTime.of(
                LocalDateTime.of(2021, 4, 15, 13, 29),
                ZoneId.of("Europe/Warsaw")
        );
        ZonedDateTime toHour = ZonedDateTime.of(
                LocalDateTime.of(2025, 5, 4, 16, 16),
                ZoneId.of("Europe/Warsaw")
        );
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
        ZonedDateTime fromHour = ZonedDateTime.of(
                LocalDateTime.of(2022, 2, 24, 6, 40),
                ZoneId.of("Europe/Warsaw")
        );
        ZonedDateTime toHour = ZonedDateTime.of(
                LocalDateTime.of(2022, 9, 3, 22, 4),
                ZoneId.of("Europe/Warsaw")
        );
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

        ZonedDateTime fromHour = ZonedDateTime.of(
                LocalDateTime.of(2021, 4, 15, 13, 29),
                ZoneId.of("Europe/Warsaw")
        );
        ZonedDateTime toHour = ZonedDateTime.of(
                LocalDateTime.of(2025, 5, 4, 16, 16),
                ZoneId.of("Europe/Warsaw")
        );
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