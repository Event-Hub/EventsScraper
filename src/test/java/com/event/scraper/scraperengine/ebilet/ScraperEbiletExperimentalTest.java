package com.event.scraper.scraperengine.ebilet;

import com.event.scraper.event.Event;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ScraperEbiletExperimentalTest {

    private ScraperEbiletExperimental scraperEbiletExperimental;

    @BeforeEach
    void setUp() {
        scraperEbiletExperimental = new ScraperEbiletExperimental();
    }

    @Test
    void getEventTypesTest() {
        //given
        scraperEbiletExperimental = new ScraperEbiletExperimental();

        //when
        List<String> eventTypes = scraperEbiletExperimental.getEventTypes(ScraperEbiletExperimental.E_BILET_MUZYKA);
        //then

        System.out.println("Event Types");
        eventTypes.forEach(System.out::println);

        assertAll(
                () -> assertFalse(eventTypes.isEmpty()),
                () -> assertTrue(eventTypes.size() > 1),
                () -> assertTrue(eventTypes.contains("Rock"))
        );

    }

    @Test
    void getPagesForEventTypesTest() {
        //given
        List<String> eventTypes = scraperEbiletExperimental.getEventTypes(ScraperEbiletExperimental.E_BILET_MUZYKA);

        //when
        List<String> pagesForEventTypes = scraperEbiletExperimental.getPagesForEventTypes(eventTypes);

        //then
        pagesForEventTypes.forEach(System.out::println);

        assertAll(
                () -> assertFalse(pagesForEventTypes.isEmpty()),
                () -> assertTrue(pagesForEventTypes.size() > 1),
                () -> assertTrue(pagesForEventTypes.contains("https://www.ebilet.pl//muzyka/rock/"))
        );
    }

    @Test
    void getEventsForTypeTest() {
        //given
        String eventTypeName = "Rock";

        //when
        List<Event> eventsForType = scraperEbiletExperimental.getEventsForType(eventTypeName);

        //then
        eventsForType.forEach(System.out::println);
    }

    @Test
    void getDescriptionForElementTest() {
        Elements select = scraperEbiletExperimental.getDocument("https://www.ebilet.pl/muzyka/rock/").select("a.cube");
        String descriptionForElement = scraperEbiletExperimental.getDescriptionForElement(select.get(0));
        System.out.println(descriptionForElement);
    }

    @Test
    void getAddressTest() {
        Elements select = scraperEbiletExperimental.getDocument("https://www.ebilet.pl/muzyka/rock/").select("a.cube");
        String descriptionForElement = scraperEbiletExperimental.getAddress(select.get(0));
        System.out.println(descriptionForElement);
    }

    @Test
    void getDateTest() {
        Elements select = scraperEbiletExperimental.getDocument("https://www.ebilet.pl/muzyka/rock/").select("a.cube");
        LocalDate date = scraperEbiletExperimental.getDate(select.get(1));
        System.out.println(date);
    }

    @Test
    void getTimeTest() {
        scraperEbiletExperimental.getTime();

    }
}
//TODO not finished