package com.event.scraper.scraperengine;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Service
public class ScraperProanimaExperimental {

  private static final String PAGE_URL = "https://proanima.pl/wydarzenia/";
  Logger log = LogManager.getLogger(ScraperProanimaExperimental.class);

  @PostConstruct
  void init() {
    scrap();
  }

  void scrap() {
    try {
      Document document = Jsoup.connect(PAGE_URL).get();
      Element eventSection = document.getElementById("events");

      Objects.requireNonNull(eventSection);
      Elements eventCarts = eventSection.getElementsByClass("card");

      Objects.requireNonNull(eventCarts);
      List<String> eventLinkList = eventCarts.stream()
          .map(this::getEventLinkAndExtractHrefAttribute)
          .toList();

//https://proanima.pl/wydarzenia/10-tenorow-koncerty-miasta-bilety-2022/
      if (log.isInfoEnabled()) {
        log.info(String.format("Document title %s", document.title()));
        log.info(eventLinkList.size());
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

  }

  private String getEventLinkAndExtractHrefAttribute(Element eventCart) {
    return eventCart.getElementsByClass("card-thumb-wrapper").attr("href");
  }
}
