package ru.practicum.user.service;

import ru.practicum.user.NewUserRequestDto;
import ru.practicum.user.UserDto;

import java.util.List;

public interface UserService {

    UserDto addUser(NewUserRequestDto user);

    List<UserDto> getUsers(List<Long> userIds, Integer from, Integer size);

    List<UserDto> getUsersWithPage(List<Long> userIds, Integer from, Integer size);

    void deleteUser(Long id);
}
