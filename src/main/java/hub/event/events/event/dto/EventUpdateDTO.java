package hub.event.events.event.dto;

import hub.event.events.city.City;
import hub.event.events.place.Place;

import java.time.LocalDate;

public record EventUpdateDTO(City city, Place place, LocalDate date, String title, String description) {

}
