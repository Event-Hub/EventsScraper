package hub.event.events.event;

import hub.event.events.city.City;
import hub.event.events.place.Place;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "events")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_id")
    private Long id;
    @ManyToOne
    @JoinColumn(name = "city_id")
    private City city;
    @ManyToOne
    @JoinColumn(name = "place_id")
    private Place place;
    private LocalDate eventDate;
    private String title;
    private String description;

    public Event() {
    }

    public Event(Long id, City city, Place place, LocalDate eventDate, String title, String description) {
        this.id = id;
        this.city = city;
        this.place = place;
        this.eventDate = eventDate;
        this.title = title;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return eventDate;
    }

    public void setDate(LocalDate date) {
        this.eventDate = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public Place getPlace() {
        return place;
    }

    public void setPlace(Place place) {
        this.place = place;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return Objects.equals(city, event.city) && Objects.equals(place, event.place) && Objects.equals(eventDate, event.eventDate) && Objects.equals(title, event.title) && Objects.equals(description, event.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(city, place, eventDate, title, description);
    }
}
