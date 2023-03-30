package ru.practicum.compilations.dto;

import lombok.*;
import ru.practicum.events.model.Event;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Set;

@Valid
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class NewCompilationDto {

    private Boolean pinned;

    @NotBlank
    private String title;

    private List<Long> events;
}
