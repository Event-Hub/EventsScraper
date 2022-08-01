package com.event.scraper.scraperengine.proanima;

import org.assertj.core.util.Files;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class EventLinkExtractorTest {
  private final EventLinkExtractor eventLinkExtractor = new EventLinkExtractor();

  @Test
  void whenDocumentListEmptyThenReturnEmptyListAndNotThrown() {
    List<Document> list = new ArrayList<>();

    List<String> eventLinks = eventLinkExtractor.extractAvailableEventLinks(list);

    assertThat(eventLinks).isNotNull().isEmpty();

  }

  @Test
  void whenDocumentListNotEmptyThenReturnLinksAndNotThrown() throws IOException {
    //given
    File inputPage = new File("src/test/resources/scraper/proanima_2022_08_01.html");
    Document document = Jsoup.parse(inputPage, StandardCharsets.UTF_8.name(), "https://proanima.pl/wydarzenia/");
    List<Document> list = List.of(document);

    //then
    List<String> eventLinks = eventLinkExtractor.extractAvailableEventLinks(list);

    assertThat(eventLinks).isNotNull()
        .hasSize(6)
        .doesNotContainNull()
        .containsExactlyInAnyOrder(
            "https://proanima.pl/wydarzenia/10-tenorow-koncerty-miasta-bilety-2022/",
            "https://proanima.pl/wydarzenia/summer-contrast-festival-2022-swidwin/",
            "https://proanima.pl/wydarzenia/tribalanga-festival-2022-podlasie/",
            "https://proanima.pl/wydarzenia/xxii-dni-pawlowa-2022/",
            "https://proanima.pl/wydarzenia/od-nowa-festiwal-2022/",
            "https://proanima.pl/wydarzenia/new-date-rave-internat-open-air-w-kliment-dnaprocess-twardogora-wroclaw/"
        );

  }
}