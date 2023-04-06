package ru.practicum;

public class RequestAlreadyExists extends RuntimeException {

    public RequestAlreadyExists(String message) {
        super(message);
    }
}
