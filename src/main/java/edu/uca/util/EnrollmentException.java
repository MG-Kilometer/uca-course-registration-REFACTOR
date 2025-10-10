package edu.uca.util;

public class EnrollmentException extends RuntimeException {
    public EnrollmentException(String message) {
        super("An error with enrollments has occurred: " + message);
    }
}
