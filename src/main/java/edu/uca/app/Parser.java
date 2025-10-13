package edu.uca.app;

import edu.uca.util.EnrollmentInfo;
import edu.uca.util.ValidationException;
import edu.uca.model.*; // import course and student
import edu.uca.service.Audit;

/*
    Define what the fields of CSVs represent, create and return objects from that data
 */

public class Parser {
    private static final Audit audit = new Audit();

    private String[] split(String line) {
        final String CSV_DELIMITER = ",";
        return line.split(CSV_DELIMITER, -1);
    }
    public Student parseStudent(String line) {
        String[] split_line = split(line);

        try {
            if (split_line.length != 3) {
                throw new ValidationException("Student input string too long.");
            }
        } catch (ValidationException e) {
            audit.add(e.getMessage());
            return null;
        }

        BannerID id = new BannerID(split_line[0]);
        Name name = new Name(split_line[1]);
        Email email = new Email(split_line[2]);

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
