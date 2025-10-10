package edu.uca.app;

import edu.uca.util.ValidationException;
import edu.uca.model.*; // import course and student

// take in CSV, return split values
public class Parser {
    private String[] split(String line) {
        final String CSV_DELIMITER = ",";
        return line.split(CSV_DELIMITER, -1);
    }
    public Student parseStudent(String line) {
        String[] split_line = split(line);

        if (split_line.length != 3) {
            throw new ValidationException("Student input string too long.");
        }

        String id = split_line[0];
        String name = split_line[1];
        String email = split_line[2];

        return new Student(id, name, email);
    }

    public Course parseCourse(String line) {
        String[] split_line = split(line);

        if (split_line.length != 3) {
            throw new ValidationException("Course input string too long.");
        }

        String code = split_line[0];
        String title = split_line[1];
        int cap = Integer.parseInt(split_line[2]);

        return new Course(code, title, cap);
    }

    public String[] parseEnrollment(String line) {
        String[] split_line = split(line);
        if (split_line.length != 3) {
            throw new ValidationException("Enrollment input string too long.");
        }
        return split_line;
    }
}
