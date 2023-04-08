package ru.practicum.compilations;

import lombok.*;
import ru.practicum.events.EventShortDto;

import javax.validation.Valid;
import java.util.Set;

@Valid
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CompilationDto {

    private Long id;

    private Boolean pinned;

    private String title;

    private Set<EventShortDto> events;
}
