package edu.uca.util;

/*
    Create an object to store the file names of output CSVs
 */

public record OutCSV(String student_csv, String course_csv, String enrollment_csv) {
}
