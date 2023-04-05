package ru.practicum.requests;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import jdk.jfr.Timestamp;
import lombok.*;
import ru.practicum.state.State;

import javax.validation.Valid;

@Valid
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ParticipationRequestDto {

    private Long id;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Timestamp
    private LocalDateTime created;

    private Long event;

    private Long requester;

    private State status;
}
