package hub.event.users.filter.dto;

import java.time.LocalDateTime;
import java.util.Objects;

public class FilterDto {
    private Long filterId;
    private Long cityId;
    private Long userId;
    private String name;
    private LocalDateTime fromHour;
    private LocalDateTime toHour;

    public FilterDto() {
    }

    public FilterDto(Long filterId, Long cityId, Long userId, String name, LocalDateTime fromHour, LocalDateTime toHour) {
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
        FilterDto filterDto = (FilterDto) o;
        return Objects.equals(filterId, filterDto.filterId) && Objects.equals(cityId, filterDto.cityId) && Objects.equals(userId, filterDto.userId) && Objects.equals(name, filterDto.name) && Objects.equals(fromHour, filterDto.fromHour) && Objects.equals(toHour, filterDto.toHour);
    }

    @Override
    public int hashCode() {
        return Objects.hash(filterId, cityId, userId, name, fromHour, toHour);
    }

    @Override
    public String toString() {
        return "FilterDto{" +
                "filterId=" + filterId +
                ", cityId=" + cityId +
                ", userId=" + userId +
                ", name='" + name + '\'' +
                ", fromHour=" + fromHour +
                ", toHour=" + toHour +
                '}';
    }
}
