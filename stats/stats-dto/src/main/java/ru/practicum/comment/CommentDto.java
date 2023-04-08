package ru.practicum.comment;


import lombok.*;
import ru.practicum.events.EventShortDto;
import ru.practicum.user.UserDto;

import javax.validation.Valid;
import java.time.LocalDateTime;

@Valid
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CommentDto {

    private Long id;

    private String text;

    private UserDto author;

    private EventShortDto event;

    private LocalDateTime createdOn;
}
