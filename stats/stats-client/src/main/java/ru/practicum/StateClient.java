package ru.practicum;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;
import ru.practicum.user.NewUserRequestDto;
import ru.practicum.user.UserDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Service
public class StateClient {

    protected final WebClient webClient;
    private final String baseUrl;

    public static final String TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(TIME_PATTERN);

    private static final String STAT_SERVER_URL = "http://localhost:9090";
    public StateClient(WebClient.Builder webClientBuilder, @Value(STAT_SERVER_URL) String baseUrl) {
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
                        .pathSegment("stats")
                        .queryParam("start",  start.format(formatter))
                        .queryParam("end", end.format(formatter))
                        .queryParam("uris", uris.toArray(new String[0]))
                        .queryParam("unique", unique)
                        .build()
                        //.encode()
                        .toUriString())
                .retrieve()
                .bodyToFlux(ViewStateDto.class)
                .collectList()
                .block();
    }
                        //.toUriString()

//                        uriBuilder -> uriBuilder
//                        .path("/stats")
//                        .queryParam("start", String.valueOf(start))
//                        .queryParam("end", String.valueOf(end))
//                        .queryParam(String.valueOf(unique))
//                        .queryParam("uris", String.join(",", uris))
//                        .build())
    //}
}
