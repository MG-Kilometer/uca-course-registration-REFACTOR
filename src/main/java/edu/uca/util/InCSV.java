package edu.uca.util;

/*
    Create an object to store the file names of input CSVs.
 */

public record InCSV(String student_csv, String course_csv, String enrollment_csv) {
}
