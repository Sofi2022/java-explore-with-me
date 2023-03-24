package ru.practicum.events.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import ru.practicum.Location;

import javax.validation.Valid;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Valid
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NewEventDto {

    @Size(min = 20, max = 200)
    private String annotation;

    private Integer category;

    @Size(min = 20, max = 7000)
    private String description;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;

    private Location location;

    private Boolean paid;

    private Integer participantLimit;

    private Boolean requestModeration;

    @Size(min = 3, max = 120)
    private String title;
}
