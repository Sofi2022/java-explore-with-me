package ru.practicum.events.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.events.dto.Event;
import ru.practicum.events.dto.NewEventDto;
import ru.practicum.user.mapper.UserMapper;

@Mapper(componentModel = "spring", uses = UserMapper.class)
public interface EventsMapper {


    @Mapping(target = "category.id", source = "category")
    @Mapping(target = "initiator.id", source = "userId")
    Event fromDto(NewEventDto event, Long userId);

    //Event toDto(ru.practicum.events.Event event);
}
