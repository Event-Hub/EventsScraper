package com.event.scraper.utils.data_receiver;

import org.jsoup.nodes.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;

class PageWalkerTest {


    private PageWalker pageWalker;

    @BeforeEach
    void setUp() {
        pageWalker = new PageWalker();
    }

    @Test
    void getEventTypesTest() {
        //given
        pageWalker = new PageWalker();

        //when
        List<String> eventTypes = pageWalker.getEventTypes(PageWalker.E_BILET_MUZYKA);
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

        //when
        List<String> pagesForEventTypes = pageWalker.getPagesForEventTypes(pageWalker.getDocument(PageWalker.E_BILET_MUZYKA));

        //then
        pagesForEventTypes.forEach(System.out::println);

        assertAll(
                () -> assertFalse(pagesForEventTypes.isEmpty()),
                () -> assertTrue(pagesForEventTypes.size() > 1),
                () -> assertTrue(pagesForEventTypes.contains("https://www.ebilet.pl//muzyka/rock/"))
        );
    }
}