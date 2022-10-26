package hub.event.events.event;

import hub.event.events.city.City;
import hub.event.events.place.Place;
import hub.event.events.type.Type;

import javax.persistence.*;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;
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
    private ZonedDateTime startDate;
    // czy to jest jednorazowe wydarzenie? czy to jest okres?
    // data zakończenia?
    private ZonedDateTime endDate;
    private String title;
    private String description;
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "event_type",
    joinColumns = @JoinColumn(name = "event_id"),
    inverseJoinColumns = @JoinColumn(name = "type_id"))
    private List<Type> types;

    public Event() {
    }

    public Event(Long id, City city, Place place, ZonedDateTime startDate, ZonedDateTime endDate, String title, String description) {
        this.id = id;
        this.city = city;
        this.place = place;
        this.startDate = startDate;
        if(!endDate.isAfter(startDate)){
            throw new IllegalArgumentException("End date must be after start date");
        }
        this.endDate = endDate;
        this.title = title;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(ZonedDateTime startDate) {
        this.startDate = startDate;
    }

    public ZonedDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(ZonedDateTime endDate) {
        this.endDate = endDate;
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

    public List<Type> getTypes() {
        return types;
    }

    public void setTypes(List<Type> types) {
        this.types = types;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return city.equals(event.city) && place.equals(event.place) && startDate.equals(event.startDate) && endDate.equals(event.endDate) && title.equals(event.title) && description.equals(event.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(city, place, startDate, endDate, title, description);
    }
}
