package ru.practicum.compilations.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.categories.mapper.CategoriesMapper;
import ru.practicum.compilations.dto.CompilationDto;
import ru.practicum.compilations.dto.NewCompilationDto;
import ru.practicum.compilations.dto.UpdateCompilationRequest;
import ru.practicum.compilations.model.Compilation;
import ru.practicum.events.mapper.EventsMapper;
import ru.practicum.events.model.Event;
import ru.practicum.locations.mapper.LocationMapper;
import ru.practicum.user.mapper.UserMapper;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring", uses = { UserMapper.class, LocationMapper.class, CategoriesMapper.class, EventsMapper.class})
public interface CompilationsMapper {

    CompilationDto toDto(Compilation compilation);

    @Mapping(target = "events", source = "eventList")
    Compilation toModel(NewCompilationDto compilation, Set<Event> eventList);

    List<CompilationDto> toDtoList(List<Compilation> compils);
}
