package ru.practicum.user.dto;

import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Valid
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserShortDto {

    @NotNull
    private Long id;

    @NotBlank
    private String name;
}
