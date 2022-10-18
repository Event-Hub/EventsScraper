package hub.event.users.filter;

import hub.event.users.filter.dto.FilterDto;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
    private LocalDateTime fromHour;
    private LocalDateTime toHour;


    public Filter() {
    }

    public Filter(Long filterId, Long cityId, Long userId, String name, LocalDateTime fromHour, LocalDateTime toHour) {
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

    public LocalDateTime getFromHour() {
        return fromHour;
    }

    public void setFromHour(LocalDateTime fromHour) {
        this.fromHour = fromHour;
    }

    public LocalDateTime getToHour() {
        return toHour;
    }

    public void setToHour(LocalDateTime toHour) {
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
