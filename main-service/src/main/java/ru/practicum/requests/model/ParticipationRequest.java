package ru.practicum.requests.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jdk.jfr.Timestamp;
import lombok.*;
import ru.practicum.events.model.Event;
import ru.practicum.state.State;
import ru.practicum.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
//@RequiredArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "participation_requests")
public class ParticipationRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Timestamp
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime created;

    @ManyToOne
    @JoinColumn(name= "event_id")
    private Event event;

    @ManyToOne
    @JoinColumn(name= "requester_id")
    private User requester;

    @Column(name = "state")
    @Enumerated(EnumType.STRING)
    private State state;

    public ParticipationRequest(LocalDateTime created, Event event, User requester, State state) {
        this.created = created;
        this.event = event;
        this.requester = requester;
        this.state = state;
    }
}
