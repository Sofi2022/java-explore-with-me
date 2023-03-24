package ru.practicum.events.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jdk.jfr.Timestamp;
import lombok.*;
import ru.practicum.Location;
import ru.practicum.categories.dto.CategoryDto;
import ru.practicum.state.State;
import ru.practicum.user.dto.UserShortDto;

import javax.validation.Valid;
import java.time.LocalDateTime;

@Valid
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Event {

    private Long id;

    private String annotation;

    private CategoryDto category;

    private Long confirmedRequests; //кол-во одобренных заявок

    @Timestamp
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdOn;

    private String description;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;

    private UserShortDto initiator;

    private Location location;

    private Boolean paid;

    private Integer participantLimit;

    @Timestamp
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime publishedOn;

    private Boolean requestModeration;

    private State state;

    private String title;

    private Long views;
}
