package ru.practicum.events;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import ru.practicum.categories.CategoryDto;
import ru.practicum.user.UserShortDto;

import javax.validation.Valid;
import java.time.LocalDateTime;

@Valid
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class EventShortDto {

    private Long id;

    private String annotation;

    private CategoryDto category;

    private Long confirmedRequests;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;

    private UserShortDto initiator;

    private Boolean paid;

    private String title;

    private Long views;
}
