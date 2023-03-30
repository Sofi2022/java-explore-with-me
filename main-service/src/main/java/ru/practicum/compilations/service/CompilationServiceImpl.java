package ru.practicum.compilations.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.compilations.mapper.CompilationsMapper;
import ru.practicum.compilations.dto.CompilationDto;
import ru.practicum.compilations.dto.NewCompilationDto;
import ru.practicum.compilations.dto.UpdateCompilationRequest;
import ru.practicum.compilations.model.Compilation;
import ru.practicum.compilations.repository.CompilationsRepository;
import ru.practicum.events.mapper.EventsMapper;
import ru.practicum.events.model.Event;
import ru.practicum.events.service.EventService;
import ru.practicum.user.exception.NotFoundException;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
            Set<Long> eventIds = compilation.getEvents();
            Set<Event> events = eventIds.stream().map(eventService::getEventById).collect(Collectors.toSet());
            Compilation fromDto = compilationsMapper.toModel(compilation, events);
            Compilation savedCompil = compilationsRepository.save(fromDto);
            CompilationDto result = compilationsMapper.toDto(savedCompil);
            return result;
        }
        Compilation fromDto = compilationsMapper.toModel(compilation, new HashSet<>());
        Compilation savedCompil = compilationsRepository.save(fromDto);
        CompilationDto result = compilationsMapper.toDto(savedCompil);
        return result;
    }


    @Override
    public CompilationDto getCompilById(Long compId) {
        Compilation compilation = compilationsRepository.findById(compId).orElseThrow(() ->
                new NotFoundException("Такой подборки нет " + compId));
        //List<Long> events = ;
        CompilationDto compilationDto = compilationsMapper.toDto(compilation);
        return compilationDto;
    }


    @Override
    public List<CompilationDto> getCompilsWithParams(Boolean pinned, Integer from, Integer size) {
        int page = from / size;
        final PageRequest pageRequest = PageRequest.of(page, size);
        List<Compilation> compils = compilationsRepository.findAllByPinned(pinned, pageRequest);
        return compilationsMapper.toDtoList(compils);
    }


    @Override
    public void deleteCompilation(Long compId) {
        compilationsRepository.findById(compId).orElseThrow(() ->
                new NotFoundException("Такой подборки нет " + compId));
        compilationsRepository.deleteById(compId);
    }


    @Override
    public CompilationDto updateCompilation(Long compId, UpdateCompilationRequest compil) {
        Compilation compilationFromDb = compilationsRepository.findById(compId).orElseThrow(() ->
                new NotFoundException("Такой подборки нет " + compId));
        if(compil.getEvents().size() != 0) {
            Set<Long> eventIds = compil.getEvents();
            Set<Event> events = eventIds.stream().map(eventService::getEventById).collect(Collectors.toSet());
            compilationFromDb.setEvents(events);
        }
        if (compil.getPinned() != null) {
            compilationFromDb.setPinned(compil.getPinned());
        }
        if (compil.getTitle() != null) {
            compilationFromDb.setTitle(compil.getTitle());
        }
        Compilation updated = compilationsRepository.save(compilationFromDb);
        return compilationsMapper.toDto(updated);
    }
}
