package ru.practicum.server.model;

import jdk.jfr.Timestamp;
import lombok.*;
import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
@Entity
@Table(name = "endpoint_hits")
public class EndpointHit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String app;

    private String uri;

    private String ip;

    @Timestamp
    private LocalDateTime timestamp;

}
