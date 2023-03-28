package ru.practicum.locations.mapper;

import org.mapstruct.Mapper;
import ru.practicum.locations.dto.LocationDto;
import ru.practicum.locations.model.Location;

@Mapper(componentModel = "spring")
public interface LocationMapper {

    Location fromDto(LocationDto locationDto);
}
