package hub.event.users.filter;

import hub.event.users.filter.dto.FilterDto;

//TODO Przerobić mappery na POJO - DONE
// do przerobienia na POJO - DONE
public class FilterDtoMapper {

    public FilterDto map(Filter filter) {
        FilterDto dto = new FilterDto();

        dto.setFilterId(filter.getFilterId());
        dto.setCityId(filter.getCityId());
        dto.setUserId(filter.getUserId());
        dto.setName(filter.getName());
        dto.setFromHour(filter.getFromHour());
        dto.setToHour(filter.getToHour());

        return dto;
    }

    public Filter map(FilterDto dto) {
        Filter filter = new Filter();

        filter.setFilterId(dto.getFilterId());
        filter.setCityId(dto.getCityId());
        filter.setUserId(dto.getUserId());
        filter.setName(dto.getName());
        filter.setFromHour(dto.getFromHour());
        filter.setToHour(dto.getToHour());

        return filter;
    }
}
