package ru.practicum.events.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.categories.mapper.CategoriesMapper;
import ru.practicum.events.EventShortDto;
import ru.practicum.events.model.Event;
import ru.practicum.events.EventFullDto;
import ru.practicum.events.NewEventDto;
import ru.practicum.locations.mapper.LocationMapper;
import ru.practicum.user.mapper.UserMapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = { UserMapper.class, LocationMapper.class, CategoriesMapper.class})
public interface EventsMapper {


    @Mapping(target = "category.id", source = "event.category")
    @Mapping(target = "initiator.id", source = "userId")
    Event fromDto(NewEventDto event, Long userId);

    EventFullDto toDto(Event event);

    List<EventFullDto> toListFullDto(List<Event> events);

    EventShortDto toShortDto(Event event);

    List<EventShortDto> toListShortDto(List<Event> events);

    List<Event> toEventList(List<EventFullDto> events);
}
