package ru.practicum.exception;

public class RequestAlreadyExists extends RuntimeException {

    public RequestAlreadyExists(String message) {
        super(message);
    }
}
