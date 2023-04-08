package ru.practicum.categories;

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
public class ResponseCategoryDto {

    @NotNull
    private Long id;

    @NotBlank
    private String name;
}
