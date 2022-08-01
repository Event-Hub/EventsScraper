package com.event.scraper.scraperengine.proanima;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

class EventLinkExtractor {
  List<String> extractAvailableEventLinks(List<Document> eventListDocuments) {
    return eventListDocuments.stream()
        .map(this::extractEventLinksFromDocument)
        .flatMap(Collection::stream)
        .toList();
  }

  private List<String> extractEventLinksFromDocument(Document document) {
    Element eventSection = document.getElementById("events");

    Objects.requireNonNull(eventSection);
    Elements eventCarts = eventSection.getElementsByClass("card");

    Objects.requireNonNull(eventCarts);
    return eventCarts.stream()
        .map(this::getEventLinkAndExtractHrefAttribute)
        .toList();
  }

  private String getEventLinkAndExtractHrefAttribute(Element eventCart) {
    return eventCart.getElementsByClass("card-thumb-wrapper").attr("href");
  }
}
