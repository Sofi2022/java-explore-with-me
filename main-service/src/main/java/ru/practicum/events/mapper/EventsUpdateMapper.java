package ru.practicum.events.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ru.practicum.categories.mapper.CategoriesMapper;
import ru.practicum.events.dto.UpdateEventAdminRequest;
import ru.practicum.events.dto.UpdateEventUserRequest;
import ru.practicum.events.model.Event;
import ru.practicum.locations.mapper.LocationMapper;
import ru.practicum.user.mapper.UserMapper;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = { UserMapper.class, LocationMapper.class, CategoriesMapper.class})
public interface EventsUpdateMapper {

    @Mapping(target = "category.id", source = "eventUpdateDto.category")
    Event updateEventByAdmin(UpdateEventAdminRequest eventUpdateDto, @MappingTarget Event event);

    @Mapping(target = "category.id", source = "eventUpdateDto.category")
    Event updateEventByUser(UpdateEventUserRequest eventUpdateDto, @MappingTarget Event event);
}
