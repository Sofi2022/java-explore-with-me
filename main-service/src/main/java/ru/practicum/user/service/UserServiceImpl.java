package ru.practicum.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.user.dto.NewUserRequestDto;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.exception.NotFoundException;
import ru.practicum.user.mapper.UserMapper;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    @Override
    public UserDto addUser(NewUserRequestDto user) {
        User savedUser = userRepository.save(userMapper.toUser(user));
        return userMapper.toDto(savedUser);
    }


    @Override
    public List<UserDto> getUsers(List<Long> userIds, Integer from, Integer size) {
        if (size != 0) {
            return getUsersWithPage(userIds, from, size);
        }
        List<User> users = userRepository.findAllByIdContains(userIds);
        return userMapper.toListDto(users);
    }

    @Override
    public List<UserDto> getUsersWithPage(List<Long> userIds, Integer from, Integer size) {
        int page = from / size;
        final PageRequest pageRequest = PageRequest.of(page, size);
        Page<User> result = userRepository.findAllByIdContainsWithPage(pageRequest, userIds);
        return userMapper.toListDto(result.getContent());
    }


    @Override
    public void deleteUser(Long id) {
        userRepository.findById(id).orElseThrow(() -> new NotFoundException("Такого пользователя нет " + id));
        userRepository.deleteById(id);
    }
}
