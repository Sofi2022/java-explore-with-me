package ru.practicum.server.model;

import lombok.*;


@Getter
@Setter
@ToString
@AllArgsConstructor
@RequiredArgsConstructor
public class ViewState {

    private String app;

    private String uri;

    private Long hits;
}
