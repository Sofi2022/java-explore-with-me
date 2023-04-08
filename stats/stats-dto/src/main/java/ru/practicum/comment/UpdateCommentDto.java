package ru.practicum.comment;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import ru.practicum.events.EventShortDto;
import ru.practicum.user.UserDto;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Valid
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UpdateCommentDto {


    @NotBlank
    private String text;

    private UserDto author;

    private EventShortDto event;

    @CreationTimestamp
    private LocalDateTime createdOn;
}
