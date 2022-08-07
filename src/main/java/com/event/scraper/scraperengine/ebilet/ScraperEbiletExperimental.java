package com.event.scraper.scraperengine.ebilet;

import com.event.scraper.event.Event;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class ScraperEbiletExperimental {

    private static final Logger logger = LogManager.getLogger(ScraperEbiletExperimental.class);
    public static final String E_BILET_MUZYKA = "https://www.ebilet.pl/muzyka/";
    public static final String E_BILET = "https://www.ebilet.pl/";


    public Document getDocument(String url) {
        try {
            return Jsoup.connect(url).get();
        } catch (IOException exception) {
            logger.log(Level.ERROR, "Error while connecting to page", exception);
            return null;
        }
    }

    public List<String> getEventTypes(String firstPageUrl) {
        logger.info("Start parsing main page with events");
        Document firstPage = getDocument(firstPageUrl);

        if (firstPage != null) {
            return Arrays.stream(firstPage.getElementsByTag("h2").toArray())
                    .map(element -> (Element) element)
                    .map(Element::text)
                    .filter(eventType -> !eventType.equals("Najpopularniejsze"))
                    .toList();
        } else {
            return new ArrayList<String>();
        }
    }

    public List<String> getPagesForEventTypes(List<String> eventTypes) {
        Document firstPage = getDocument(ScraperEbiletExperimental.E_BILET_MUZYKA);

        return eventTypes.stream()
                .map(eventType -> firstPage.select("h2:containsOwn(" + eventType + ")"))
                .map(Elements::first)
                .map(element -> element != null ? element.parent() : null)
                .filter(Objects::nonNull)
                .map(parent -> parent.attr("href"))
                .map(relativeCategoryLink -> ScraperEbiletExperimental.E_BILET + relativeCategoryLink)
                .filter(link -> !link.equals(ScraperEbiletExperimental.E_BILET))
                .toList();
    }

    public List<Event> getEventsForType(String eventTypeName) {
        List<String> types = Collections.singletonList(eventTypeName);
        String eventTypeNameUrl = getPagesForEventTypes(Collections
                .singletonList(types.stream()
                        .findFirst()
                        .orElse(""))).stream().findFirst().orElse("");

        Document document = getDocument(eventTypeNameUrl);
        Elements elements = document.select("a.cube");

        List<Event> events = new ArrayList<>();
        elements.forEach(element -> {
            String title = element.attr("title").trim();
            String city = element.attr("data-overlay-location").trim();
            String link = ScraperEbiletExperimental.E_BILET.substring(0, ScraperEbiletExperimental.E_BILET.length() - 1) + element.attr("href");
            String description = getDescriptionForElement(element);
            String address = getAddress(element);
            LocalDate date = getDate(element);


            events.add(
                    new Event(city, title, description, address, date, null, link)
            );
        });

        return events;
    }

    public String getAddress(Element element) {
        return element.select("div.overlay-wrapper").attr("data-overlay-location");
    }

    public LocalDate getDate(Element element) {
        String stringDate = element.select("div.overlay-wrapper").attr("data-overlay-date");
        String correctedStringDate = stringDate.replaceFirst("od ", "");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        LocalDate date = LocalDate.parse(correctedStringDate, formatter);
        return date;
    }

    public String getDescriptionForElement(Element element) {
        return element.select("div.desc > p").text();
    }

    public void getTime(){
//        Document document = getDocument("https://www.ebilet.pl/muzyka/rock/tlove-koncert/");
        Document document = getDocument("https://www.ebilet.pl/muzyka/rock/ville-valo/");

        System.out.println(
                document.select("div.performanceList").select("div.description-inline-container").html()
        );

        System.out.println(
                document.select("div.performanceList").select("div.city-header").select("b").html()

        );
        System.out.println(
                document.select("div.performanceList").select("div.date").select("b.item-date").html()
        );


    }

}
//DONE Get page object, Get Event Types, Get Event Types links
//DONE Add code for getting event
//TODO finish getTime method
//TODO Add interface as prototype for Scrapers
//TODO get Events method don't get all events have to change for active loader like PhantomJS
