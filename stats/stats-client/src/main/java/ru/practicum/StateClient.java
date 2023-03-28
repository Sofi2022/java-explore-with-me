package ru.practicum;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class StateClient {

    protected final WebClient webClient;
    public static final String TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(TIME_PATTERN);

    public StateClient(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("${stat-server.url}").build();
    }

    public void postHit(String app,
                        String uri,
                        String ip,
                        String timestamp) {
        EndpointHitDto endpointHitDTO = EndpointHitDto.builder()
                .app(app)
                .uri(uri)
                .ip(ip)
                .timestamp(LocalDateTime.parse(timestamp, formatter))
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
                                       boolean unique) {
        return this.webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/stats")
                        .queryParam(String.valueOf(start))
                        .queryParam(String.valueOf(end))
                        .queryParam(String.valueOf(unique))
                        .queryParam("uris", String.join(",", uris))
                        .build())
                .retrieve()
                .bodyToFlux(ViewStateDto.class)
                .collectList()
                .block();
    }
}
