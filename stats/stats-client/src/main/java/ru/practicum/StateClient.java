package ru.practicum;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.stat.EndpointHitDto;
import ru.practicum.stat.ViewStateDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static ru.practicum.common.CommonConstant.TIME_PATTERN;

@Service
public class StateClient extends BaseClient {

    private final ObjectMapper mapper = new ObjectMapper();
    private final TypeReference<List<ViewStateDto>> mapType = new TypeReference<>() {};

    @Autowired
    public StateClient(@Value("${stats-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> postHit(String app,
                        String uri,
                        String ip,
                        LocalDateTime timestamp) {
        EndpointHitDto endpointHitDto = new EndpointHitDto(app, uri, ip, timestamp);
        return post("/hit", endpointHitDto);
    }

    public ResponseEntity<Object> getStats(String start, String end, String uris, Boolean unique) {
        Map<String, Object> parameters = Map.of(
                "start", start,
                "end", end,
                "uris", uris,
                "unique", unique
        );
        return get("/stats?start={start}&end={end}&uris={uris}&unique={unique}", parameters);
    }

    public Long getViewsByEventId(Long eventId) {
        Map<String, Object> parameters = Map.of(
                "start", LocalDateTime.now().minusYears(1000).format(DateTimeFormatter.ofPattern(TIME_PATTERN)),
                "end", LocalDateTime.now().plusYears(1000).format(DateTimeFormatter.ofPattern(TIME_PATTERN)),
                "uris", toString(List.of("/events/" + eventId)),
                "unique", Boolean.FALSE
        );
        ResponseEntity<Object> response = get("/stats?start={start}&end={end}&uris={uris}&unique={unique}", parameters);

        List<ViewStateDto> viewStatsList = response.hasBody() ? mapper.convertValue(response.getBody(), mapType) : Collections.emptyList();
        return viewStatsList != null && !viewStatsList.isEmpty() ? viewStatsList.get(0).getHits() : 0L;
    }

    public Map<Long, Long> getSetViewsByEventId(Set<Long> eventIds) {
        Map<String, Object> parameters = Map.of(
                "start", LocalDateTime.now().minusYears(1000).format(DateTimeFormatter.ofPattern(TIME_PATTERN)),
                "end", LocalDateTime.now().plusYears(1000).format(DateTimeFormatter.ofPattern(TIME_PATTERN)),
                "uris", (eventIds.stream().map(id -> "/events/" + id).collect(Collectors.toList())),
                "unique", Boolean.FALSE
        );
        ResponseEntity<Object> response = get("/stats?start={start}&end={end}&uris={uris}&unique={unique}", parameters);

        return response.hasBody() ? mapper.convertValue(response.getBody(), mapType).stream()
                .collect(Collectors.toMap(
                        this::getEventIdFromURI, ViewStateDto::getHits))
                : Collections.emptyMap();
    }

    private Long getEventIdFromURI(ViewStateDto e) {
        return Long.parseLong(e.getUri().substring(e.getUri().lastIndexOf("/") + 1));
    }

    private String toString(List<String> strings) {
        return Arrays.toString(strings.toArray()).replace("[", "").replace("]", "");
    }
}
