package ru.practicum.compilations.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import ru.practicum.events.model.Event;

import javax.validation.Valid;
import java.util.List;
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

    private List<Event> events;
}
