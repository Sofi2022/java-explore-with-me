package ru.practicum.compilations.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.compilations.CompilationsMapper;
import ru.practicum.compilations.dto.CompilationDto;
import ru.practicum.compilations.dto.NewCompilationDto;
import ru.practicum.compilations.model.Compilation;
import ru.practicum.compilations.repository.CompilationsRepository;
import ru.practicum.events.mapper.EventsMapper;
import ru.practicum.events.model.Event;
import ru.practicum.events.service.EventService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {

    private final CompilationsRepository compilationsRepository;

    private final CompilationsMapper compilationsMapper;

    private final EventService eventService;

    private final EventsMapper eventsMapper;

    @Override
    public CompilationDto addCompilation(NewCompilationDto compilation) {
        if (compilation.getEvents().size() != 0) {
            List<Long> eventIds = compilation.getEvents();
            List<Event> events = eventIds.stream().map(eventService::getEventById).collect(Collectors.toList());
            Compilation fromDto = compilationsMapper.toModel(compilation, events);
            Compilation savedCompil = compilationsRepository.save(fromDto);
            CompilationDto result = compilationsMapper.toDto(savedCompil);
            return result;
        }
        Compilation fromDto = compilationsMapper.toModel(compilation, null);
        Compilation savedCompil = compilationsRepository.save(fromDto);
        CompilationDto result = compilationsMapper.toDto(savedCompil);
        return result;
    }
}
