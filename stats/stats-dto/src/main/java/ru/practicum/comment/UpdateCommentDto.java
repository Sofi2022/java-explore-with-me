package ru.practicum.comment;

import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Valid
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UpdateCommentDto {

    @NotBlank
    @Size(min = 20, max = 7000)
    private String text;
}
