package edu.uca.util;

/*
    Keep track of the headers of the Enrollment CSV
 */

public record EnrollmentInfo(String code, String studentID, String status) {
    public EnrollmentInfo(String[] lines) {
        this(lines[0], lines[1], lines[2]);
    }
}
