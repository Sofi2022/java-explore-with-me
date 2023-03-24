package ru.practicum.categories.dto;

import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Valid
@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResponseCategoryDto {

    @NotNull
    private Long id;

    @NotBlank
    private String name;
}
