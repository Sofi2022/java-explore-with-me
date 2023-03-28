package ru.practicum.categories.dto;

import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@Valid
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NewCategoryDto {

    @NotBlank
    private String name;
}
