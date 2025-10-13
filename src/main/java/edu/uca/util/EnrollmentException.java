package edu.uca.util;

import edu.uca.service.Audit;

/*
    Define what an enrollment exception looks like (any time a student cannot be added to a course
    or course cannot be dropped, etc).
 */

public class EnrollmentException extends RuntimeException {
    private static final Audit audit = new Audit();

    public EnrollmentException(String message) {
        super("An error with enrollments has occurred: " + message);
        audit.add("An error with enrollments has occurred: " + message);
    }
}
