package edu.uca.app;

import edu.uca.util.EnrollmentInfo;
import edu.uca.util.Validate;
import edu.uca.util.ValidateStudent;
import edu.uca.util.ValidationException;
import edu.uca.model.*; // import course and student
import edu.uca.service.Audit;

/*
    Define what the fields of CSVs represent, create and return objects from that data
 */

public class Parser {
    private static final Audit audit = new Audit();
    private static final ValidateStudent validateStudent = new ValidateStudent();

    private String[] split(String line) {
        final String CSV_DELIMITER = ",";
        return line.split(CSV_DELIMITER, -1);
    }
    public Student parseStudent(String line) {
        String[] split_line = split(line);
        String id = "";
        String name = "";
        String email = "";

        try {
            if (split_line.length != 3) {
                throw new ValidationException("Student input string too long.");
            }
        } catch (ValidationException e) {
            audit.add(e.getMessage());
            return null;
        }

        // validate
        try {
            if (validateStudent.ValidateID(split_line[0]))
                id = split_line[0];
            else
                throw new ValidationException("Failed to validate id from CSV");
            if (validateStudent.ValidateName(split_line[1]))
                name = split_line[1];
            else
                throw new ValidationException("Failed to validate name from CSV");
            if (validateStudent.ValidateName(split_line[2]))
                email = split_line[2];
            else
                throw new ValidationException("Failed to validate email from CSV");
        } catch(ValidationException e) {
            audit.add(e.getMessage());
            return null;
        }
        return new Student(id, name, email);
    }

    public Course parseCourse(String line) {
        String[] split_line = split(line);

        try {
            if (split_line.length != 3) {
                throw new ValidationException("Course input string too long.");
            }
        } catch (ValidationException e) {
            audit.add(e.getMessage());
            return null;
        }

        String code = split_line[0];
        String title = split_line[1];
        int cap = Integer.parseInt(split_line[2]);

        return new Course(code, title, cap);
    }

    public EnrollmentInfo parseEnrollment(String line) {
        String[] split_line = split(line);
        try {
            if (split_line.length != EnrollmentInfo.class.getConstructors()[0].getParameterCount()) {
                throw new ValidationException("Enrollment input string too long.");
            }
        } catch (ValidationException e) {
            audit.add(e.getMessage());
            return null;
        }

        return new EnrollmentInfo(split_line);
    }
}
