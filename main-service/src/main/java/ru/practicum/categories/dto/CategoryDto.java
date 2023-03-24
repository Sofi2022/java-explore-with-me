package ru.practicum.categories.dto;

import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@Valid
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryDto {

    @NotBlank
    private String name;
}
