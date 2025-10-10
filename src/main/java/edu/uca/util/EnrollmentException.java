package edu.uca.util;

import edu.uca.service.Audit;

public class EnrollmentException extends RuntimeException {
    private static final Audit audit = new Audit();

    public EnrollmentException(String message) {
        super("An error with enrollments has occurred: " + message);
        audit.add("An error with enrollments has occurred: " + message);
    }
}
