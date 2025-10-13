package edu.uca.service;

import edu.uca.model.*; // add course and student classes
import edu.uca.repo.*; // course, enrollment, and student repository
import edu.uca.util.*; // exceptions
import edu.uca.app.Parser;

import java.io.*;
import java.util.Map;

/*
    Service to register students. Implements all repository interfaces.+
 */

public class RegistrationService implements StudentRepository, CourseRepository, EnrollmentRepository {
    private static final Audit audit = new Audit();
    private static final Parser parser = new Parser();

    public void loadAll(Map<String, Student> students, Map<String, Course> courses, InCSV inCSV) {
        loadStudents(students, inCSV.student_csv());
        loadCourses(courses, inCSV.course_csv());
        loadEnrollments(courses, inCSV.enrollment_csv());
    }

    public void saveAll(Map<String, Student> students, Map<String, Course> courses, OutCSV outCSV) {
        saveStudents(students, outCSV.student_csv());
        saveCourses(courses, outCSV.course_csv());
        saveEnrollments(courses, outCSV.enrollment_csv());
    }

    public void loadStudents(Map<String, Student> students, String in_csv) {
        File f = new File(in_csv);
        if (!f.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                Student student = parser.parseStudent(line);
                try {
                    if (student == null) {
                        audit.add("Invalid CSV input");
                    }
                } catch(NullPointerException e) {
                    audit.add("Invalid CSV input");
                    return;
                }

                assert student != null;
                students.put(student.getName().name(), student);
            }
            audit.add("LOAD students=" + students.size());
        } catch (Exception e) {
            throw new EnrollmentException("Failed load students");
        }
    }

    public void saveStudents(Map<String, Student> students, String out_csv) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(out_csv))) {
            for (Student s : students.values()) {
                pw.println(s.getId() + "," + s.getName() + "," + s.getEmail());
            }
        } catch (Exception e) {
            throw new EnrollmentException("Failed save students");
        }
    }

    public void loadCourses(Map<String, Course> courses, String in_csv) {
        File f = new File(in_csv);
        if (!f.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                Course course = parser.parseCourse(line);

                // catch null value
                try {
                    if (course == null) {
                        audit.add("Invalid CSV input");
                    }
                } catch(NullPointerException e) {
                    audit.add("Invalid CSV input");
                    return;
                }

                assert course != null;
                courses.put(course.getCode(), course);
            }

            audit.add("LOAD courses=" + courses.size());
        } catch (Exception e) {
            throw new EnrollmentException("Failed load courses");
        }
    }

    public void saveCourses(Map<String, Course> courses, String out_csv) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(out_csv))) {
            for (Course c : courses.values()) {
                pw.println(c.getCode() + "," + c.getTitle() + "," + c.getCapacity());
            }
        } catch (Exception e) {
            throw new EnrollmentException("Failed save courses");
        }
    }

    public void loadEnrollments(Map<String, Course> courses, String in_csv) {
        File f = new File(in_csv);
        if (!f.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;

            while ((line = br.readLine()) != null) {
                // Format: courseCode|studentId|ENROLLED or WAITLIST
                EnrollmentInfo enrollment_info = parser.parseEnrollment(line);

                try {
                    if (enrollment_info == null) {
                        audit.add("Invalid CSV input (enrollment info is nullpointer).");
                    }
                } catch(NullPointerException e) {
                    audit.add("Invalid CSV input: " + e.getMessage());
                    return;
                }
                assert enrollment_info != null;

                String code = enrollment_info.code();
                String student_id = enrollment_info.studentID(), status = enrollment_info.status();
                Course course = courses.get(code);

                if (course == null)
                    continue;
                if ("ENROLLED".equalsIgnoreCase(status)) {
                    if (!course.getRoster().contains(student_id))
                        course.getRoster().add(student_id);
                } else if ("WAITLIST".equalsIgnoreCase(status)) {
                    if (!course.getWaitlist().contains(student_id))
                        course.getWaitlist().add(student_id);
                }
            }
            audit.add("LOAD enrollments");

        } catch (Exception e) {
            audit.add("Failed load enrollments");
            return;
        }
    }

    public void saveEnrollments(Map<String, Course> courses, String out_csv) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(out_csv))) {
            for (Course course : courses.values()) {
                for (String student_id : course.getRoster())
                    pw.println(course.getCode() + "|" + student_id + "|ENROLLED");
                for (String student_id : course.getWaitlist())
                    pw.println(course.getCode() + "|" + student_id + "|WAITLIST");
            }
        } catch (Exception e) {
            // TODO: add to log?
            audit.add("Failed save enrollments: " + e.getLocalizedMessage());
            return;
        }
    }
}
