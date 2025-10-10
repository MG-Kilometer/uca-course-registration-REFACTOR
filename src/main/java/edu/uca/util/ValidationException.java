package edu.uca.util;

public class ValidationException extends RuntimeException {
    public ValidationException(String message) {
        super("Could not validate courses: " + message);
    }
}
