package com.event.scraper.scraperengine.proanima;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

class SingleEventScraper {

  void scrap() {

  }

  String title() {
    return null;
  }

  String description() {
    return null;
  }

  LocalDateTime dateFrom(){
    return LocalDateTime.now();
  }

  LocalDateTime dateTo(){
    return LocalDateTime.now();
  }

  String sourceLink(){
    return null;
  }

  Map<String, String> metadata(){
    return new HashMap<>();
  }

  String city(){
    return null;
  }
}
