package hub.event.users.filter;

import javax.persistence.*;
import java.time.ZonedDateTime;
import java.util.Objects;

@Entity
@SequenceGenerator(name = "FILTER_ID_SEQ", allocationSize = 1, initialValue = 1)
public class Filter {
    @Id
    @GeneratedValue(generator = "FILTER_ID_SEQ", strategy = GenerationType.SEQUENCE)
    @Column(nullable = false)
    private Long filterId;
    private Long cityId;

    //TODO Dodaj relacje dwukierunkowe
    // @ManyToOne
    private Long userId;
    private String name;
//    private LocalDateTime fromHour;
    private ZonedDateTime fromHour;
    private ZonedDateTime toHour;


    public Filter() {
    }

    public Filter(Long filterId, Long cityId, Long userId, String name, ZonedDateTime fromHour, ZonedDateTime toHour) {
        this.filterId = filterId;
        this.cityId = cityId;
        this.userId = userId;
        this.name = name;
        this.fromHour = fromHour;
        this.toHour = toHour;
    }

    public Long getFilterId() {
        return filterId;
    }

    public void setFilterId(Long filterId) {
        this.filterId = filterId;
    }

    public Long getCityId() {
        return cityId;
    }

    public void setCityId(Long cityId) {
        this.cityId = cityId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ZonedDateTime getFromHour() {
        return fromHour;
    }

    public void setFromHour(ZonedDateTime fromHour) {
        this.fromHour = fromHour;
    }

    public ZonedDateTime getToHour() {
        return toHour;
    }

    public void setToHour(ZonedDateTime toHour) {
        this.toHour = toHour;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Filter filter = (Filter) o;
        return Objects.equals(filterId, filter.filterId) && Objects.equals(cityId, filter.cityId) && Objects.equals(userId, filter.userId) && Objects.equals(name, filter.name) && Objects.equals(fromHour, filter.fromHour) && Objects.equals(toHour, filter.toHour);
    }

    @Override
    public int hashCode() {
        return Objects.hash(filterId, cityId, userId, name, fromHour, toHour);
    }

    @Override
    public String toString() {
        return "Filter{" +
                "filterId=" + filterId +
                ", cityId=" + cityId +
                ", userId=" + userId +
                ", name='" + name + '\'' +
                ", fromHour=" + fromHour +
                ", toHour=" + toHour +
                '}';
    }
}

//TODO modify all LocalDateTime in entities to ZonedDateTime - DONE