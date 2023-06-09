package ru.practicum.events;

import com.fasterxml.jackson.annotation.JsonFormat;
import jdk.jfr.Timestamp;
import lombok.*;
import ru.practicum.categories.CategoryDto;
import ru.practicum.locations.LocationDto;
import ru.practicum.state.State;
import ru.practicum.user.UserShortDto;

import javax.validation.Valid;
import java.time.LocalDateTime;

@Valid
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class EventFullDto {

    private Long id;

    private String annotation;

    private CategoryDto category;

    private Long confirmedRequests;

    @Timestamp
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdOn;

    private String description;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;

    private UserShortDto initiator;

    private LocationDto location;

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
