package edu.uca.util;
import edu.uca.service.Audit;

/*
    Define an error with validating data
 */

public class ValidationException extends Exception {
    private static final Audit audit = new Audit();

    public ValidationException(String message) {
        super("Could not validate input: " + message);
        System.out.println(message);
        audit.add("Could not validate input: " + message);
    }
}
