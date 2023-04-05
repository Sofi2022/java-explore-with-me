package ru.practicum.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Date;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class ErrorResponse {

    private Date date;

    private int value;

    private String reasonPhrase;

    private String message;
}
