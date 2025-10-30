package edu.uca.app;

import edu.uca.model.Course;
import edu.uca.model.Student;
import edu.uca.service.Audit;
import edu.uca.util.EnrollmentInfo;
import edu.uca.util.ValidateCourse; // import course and student
import edu.uca.util.ValidateStudent;
import edu.uca.util.ValidationException;

/*
    Define what the fields of CSVs represent, create and return objects from that data
 */

public class Parser {
    private static final Audit audit = new Audit();
    private static final ValidateStudent validateStudent = new ValidateStudent();
    private static final ValidateCourse validateCourse = new ValidateCourse();

    private String[] split(String line) {
        if (line == null || line.trim().isEmpty()) return new String[0];

        // split by comma, trim whitespace around each field
        String[] parts = line.split(",");
        for (int i = 0; i < parts.length; i++) {
            parts[i] = parts[i].trim();
        }

        return parts;
    }


    public Student parseStudent(String line) {
        String[] split_line = split(line);
        String id = "";
        String name = "";
        String email = "";

        try {
            if (split_line.length < 3) {
                throw new ValidationException("Student input string too short.");
            } else if (split_line.length > 3) {
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
            if (validateStudent.ValidateEmail(split_line[2]))
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
        String code = "";
        String title = "";
        int capacity;

        try {
            if (split_line.length < 3)
                throw new ValidationException("Course input string too short.");
            else if (split_line.length > 3) {
                throw new ValidationException("Course input string too long.");
            }
        } catch (ValidationException e) {
            audit.add(e.getMessage());
            return null;
        }

        try {
            if (validateCourse.Validate_Code(split_line[0]))
                code = split_line[0];
            else
                throw new ValidationException("Failed to validate course code from CSV");

            if (validateCourse.Validate_Title(split_line[1]))
                title = split_line[1];
            else
                throw new ValidationException("Failed to validate course title from CSV");

            // capacity should be numeric
            capacity = Integer.parseInt(split_line[2]);
        } catch (ValidationException | NumberFormatException e) {
            audit.add(e.getMessage());
            return null;
        }

        return new Course(code, title, capacity);
    }

    public EnrollmentInfo parseEnrollment(String line) {
        String[] split_line = split(line);

        try {
            if (split_line.length < 3)
                throw new ValidationException("Enrollment input string too short.");
            else if (split_line.length > 3)
                throw new ValidationException("Enrollment input string too long.");
        } catch (ValidationException e) {
            audit.add(e.getMessage());
            return null;
        }

        try {
            String bannerId = split_line[0];
            String courseCode = split_line[1];
            String grade = split_line[2];
            return new EnrollmentInfo(bannerId, courseCode, grade);
        } catch (Exception e) {
            audit.add("Failed to parse enrollment: " + e.getMessage());
            return null;
        }
    }
}
