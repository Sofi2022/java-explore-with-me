package ru.practicum;

import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@Valid
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ViewStateDto {

    @NotBlank
    private String app;

    @NotBlank
    private String uri;

    private Long hits;
}
