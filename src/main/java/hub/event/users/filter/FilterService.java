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

    public FilterService(FilterRepository filterRepository) {
        this.filterRepository = filterRepository;
        this.filterDtoMapper = new FilterDtoMapper();
    }

    public Optional<FilterDto> getFilterById(Long id) {
        return filterRepository.findById(id)
                .map(filterDtoMapper::map);
    }

    //TODO zapobiec nadpisywaniu obiektów przy save - DONE
    @Transactional
    public FilterDto saveFilter(FilterDto filterDto) {
        filterDto.setFilterId(null);
        Filter filterToSave = filterDtoMapper.map(filterDto);
        Filter savedFilter = filterRepository.save(filterToSave);
        return filterDtoMapper.map(savedFilter);
    }

    //
    @Transactional
    public Optional<FilterDto> updateFilter(Long id, FilterDto filterDto) {
        return filterRepository.findById(id)
            .map(target -> target.setEntityFields(filterDto))
//                .map(target -> setEntityFields(filterDto, target))
                .map(filterDtoMapper::map);
    }

    @Transactional
    public void deleteFilter(Long id) {
        filterRepository.deleteById(id);
    }

    //TODO przenieść metode do entity filter ?? - DONE
    // setEntityFields przeniesiona do Filter - DONE

    // ta metoda raczej mogła by być wewnątrz Filter
    // anemic model vs rich model


}

