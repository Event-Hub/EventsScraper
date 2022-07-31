package com.event.scraper.utils.data_receiver;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class PageWalker {

    private static final Logger logger = LogManager.getLogger(PageWalker.class);
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

        if (firstPage!=null){
            return  Arrays.stream(firstPage.getElementsByTag("h2").toArray())
                    .map(element -> (Element) element)
                    .map(Element::text)
                    .filter(eventType -> !eventType.equals("Najpopularniejsze"))
                    .toList();
        }
        else {
         return new ArrayList<String>();
        }
    }

    public List<String> getPagesForEventTypes(Document firstPage) {
       List<String> eventTypes = getEventTypes(PageWalker.E_BILET_MUZYKA);

        return eventTypes.stream()
                .map(eventType -> firstPage.select("h2:containsOwn(" + eventType + ")"))
                .map(Elements::first)
                .map(element -> element != null ? element.parent() : null)
                .filter(Objects::nonNull)
                .map(parent -> parent.attr("href"))
                .map(relativeCategoryLink -> PageWalker.E_BILET + relativeCategoryLink)
                .filter(link -> !link.equals(PageWalker.E_BILET))
                .toList();
    }

}
//DONE Get page object, Get Event Types, Get Event Types links
//TODO Add code for getting event
//TODO Add interface as prototype for Scrapers