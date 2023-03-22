package ru.practicum.server.mapper;

import org.mapstruct.Mapper;
import ru.practicum.ViewStateDto;
import ru.practicum.server.model.ViewState;

@Mapper(componentModel = "spring")
public interface ViewStateMapper {

    ViewStateDto toDto(ViewState state);

    ViewState fromDto(ViewStateDto state);
}
