package ru.practicum.user.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.user.NewUserRequestDto;
import ru.practicum.user.UserDto;
import ru.practicum.user.UserShortDto;
import ru.practicum.user.model.User;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toUser(NewUserRequestDto user);

    UserDto toDto(User user);

    @Mapping(target = "user.email", ignore = true)
    UserShortDto toShortUser(User user);

    List<UserDto> toListDto(List<User> users);
}
