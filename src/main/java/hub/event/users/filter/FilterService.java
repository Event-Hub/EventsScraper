package hub.event.users.filter;

// Auth comment left in comments to prevent auto delete during imports optimization
//import hub.event.auth.AuthService;

import hub.event.users.filter.dto.FilterDto;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;

@Service
public class FilterService {

    private final FilterRepository filterRepository;
    private final FilterDtoMapper filterDtoMapper;

    public FilterService(FilterRepository filterRepository, FilterDtoMapper filterDtoMapper) {
        this.filterRepository = filterRepository;
        this.filterDtoMapper = filterDtoMapper;
    }

    public Optional<FilterDto> getFilterById(Long id) {
        return filterRepository.findById(id)
                .map(filterDtoMapper::map);
    }

    @Transactional
    public FilterDto saveFilter(FilterDto filterDto) {
        // filterDto.setFilterId(null);
        Filter filterToSave = filterDtoMapper.map(filterDto);
        Filter savedFilter = filterRepository.save(filterToSave);
        return filterDtoMapper.map(savedFilter);
    }

    @Transactional
    public Optional<FilterDto> updateFilter(Long id, FilterDto filterDto) {
        return filterRepository.findById(id)
//            .map(target -> target.setEntityFields(filterDto))
                .map(target -> setEntityFields(filterDto, target))
                .map(filterDtoMapper::map);
    }

    @Transactional
    public void deleteFilter(Long id) {
        filterRepository.deleteById(id);
    }

    // ta metoda raczej mogła by być wewnątrz Filter
    // anemic model vs rich model
    private Filter setEntityFields(FilterDto source, Filter target) {

        if (source.getCityId() != null) {
            target.setCityId(source.getCityId());
        }
        if (source.getUserId() != null) {
            target.setUserId(source.getUserId());
        }
        if (source.getName() != null) {
            target.setName(source.getName());
        }
        if (source.getFromHour() != null) {
            target.setFromHour(source.getFromHour());
        }
        if (source.getToHour() != null) {
            target.setToHour(source.getToHour());
        }

        return target;
    }

}

