package ru.practicum;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.practicum.user.NewUserRequestDto;
import ru.practicum.user.UserDto;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class MainClient {

    public static final String TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(TIME_PATTERN);


    WebClient webClient = WebClient.create("http://localhost:8080");

    public UserDto addUser(NewUserRequestDto user) {
        return this.webClient.post()
                .uri("/admin/users")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(Mono.just(user), NewUserRequestDto.class)
                .retrieve()
                .bodyToMono(UserDto.class)
                .block();
    }

    public UserDto getUser(Long userId) {
        return this.webClient.get()
                .uri("/users/{userId}", userId)
                .retrieve()
                .bodyToMono(UserDto.class)
                .block();
    }

    public List<UserDto> getUsers(List<Long> userIds, Integer from, Integer size) {
        return this.webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/users")
                        .queryParam("ids", userIds)
                        .queryParam(String.valueOf(from))
                        .queryParam(String.valueOf(size))
                        .build())
                .retrieve()
                .bodyToFlux(UserDto.class)
                .collectList()
                .block();
    }




}
