package ru.practicum;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;
import ru.practicum.stat.EndpointHitDto;
import ru.practicum.stat.ViewStateDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class StateClient {

    protected final WebClient webClient;
    private final String baseUrl;

    public static final String TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(TIME_PATTERN);


    public StateClient(@Value("${stats-server.url}") String baseUrl, WebClient.Builder webClientBuilder) {
        this.baseUrl = baseUrl;
        this.webClient = webClientBuilder.baseUrl(baseUrl).build();
    }

    public void postHit(String app,
                        String uri,
                        String ip,
                        LocalDateTime timestamp) {
        EndpointHitDto endpointHitDTO = EndpointHitDto.builder()
                .app(app)
                .uri(uri)
                .ip(ip)
                .timestamp(timestamp)
                .build();

        webClient.post()
                .uri("/hit")
                .body(Mono.just(endpointHitDTO), EndpointHitDto.class)
                .retrieve()
                .bodyToMono(EndpointHitDto.class)
                .block();
    }

    public List<ViewStateDto> getStats(LocalDateTime start,
                                       LocalDateTime end,
                                       List<String> uris,
                                       Boolean unique) {
        return this.webClient.get()
                .uri(UriComponentsBuilder.fromHttpUrl("http://localhost:9090")
                        .pathSegment("/stats")
                        .queryParam("start",  start.format(formatter))
                        .queryParam("end", end.format(formatter))
                        .queryParam("uris", String.join(",", uris))
                        .queryParam("unique", unique)
                        .build()
                        .toUriString())
                .retrieve()
                .bodyToFlux(ViewStateDto.class)
                .collectList()
                .block();
    }
}
