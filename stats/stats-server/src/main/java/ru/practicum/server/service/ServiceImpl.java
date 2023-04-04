package ru.practicum.server.service;

import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import ru.practicum.EndpointHitDto;
import ru.practicum.ViewStateDto;
import ru.practicum.server.mapper.StatMapper;
import ru.practicum.server.mapper.ViewStateMapper;
import ru.practicum.server.model.EndpointHit;
import ru.practicum.server.model.ViewState;
import ru.practicum.server.repository.StatRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class ServiceImpl implements StatService {

    private final StatRepository statRepository;

    private static final String DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATETIME_PATTERN);

    private StatMapper statMapper = Mappers.getMapper(StatMapper.class);

    private ViewStateMapper viewStateMapper = Mappers.getMapper(ViewStateMapper.class);

    @Override
    public EndpointHitDto createEndpoint(EndpointHitDto endpointHitDto) {
        EndpointHit endpointHit = statMapper.fromDto(endpointHitDto);
        return statMapper.toDto(statRepository.save(endpointHit));
    }

    @Override
    public List<ViewStateDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        if (unique) {

          List<ViewState> uniqueStates =  statRepository.findAllByTimestampBetweenUnique(start, end, uris);
          return uniqueStates.stream().map(viewState -> viewStateMapper.toDto(viewState)).collect(Collectors.toList());
        }
        List<ViewState> states = statRepository.findAllByTimestampBetween(start, end, uris);
        return states.stream().map(viewState -> viewStateMapper.toDto(viewState)).collect(Collectors.toList());
    }
}
