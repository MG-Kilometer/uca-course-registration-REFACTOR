package edu.uca.util;
import edu.uca.service.Audit;

public class ValidationException extends RuntimeException {
    private static final Audit audit = new Audit();

    public ValidationException(String message) {
        super("Could not validate courses: " + message);
        audit.add("Could not validate courses: " + message);
    }
}
