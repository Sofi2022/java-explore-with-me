package ru.practicum.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.exception.NotFoundException;
import ru.practicum.exception.RequestAlreadyExists;
import ru.practicum.user.NewUserRequestDto;
import ru.practicum.user.UserDto;
import ru.practicum.user.mapper.UserMapper;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;


    @Transactional
    @Override
    public UserDto addUser(NewUserRequestDto user) {
        Optional<List<User>> userWithSameName = userRepository.findByName(user.getName());
        if (userWithSameName.get().size() != 0) {
            throw new RequestAlreadyExists("Пользователь с таким именем уже существует: " + user.getName());
        }
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


    @Transactional
    @Override
    public void deleteUser(Long id) {
        userRepository.findById(id).orElseThrow(() -> new NotFoundException("Такого пользователя нет " + id));
        userRepository.deleteById(id);
    }
}
