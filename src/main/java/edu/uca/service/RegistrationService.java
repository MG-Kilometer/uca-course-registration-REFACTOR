package edu.uca.service;

import edu.uca.model.Course;
import edu.uca.model.Student;
import edu.uca.repo.*; // course, enrollment, and student repository
import edu.uca.service.Audit;
import edu.uca.util.*; // exceptions

import java.io.*;
import java.util.Map;

public class RegistrationService implements StudentRepository, CourseRepository, EnrollmentRepository {
    // TODO: I don't think these CSVs should be here
    static final String STUDENTS_CSV = "students.csv";
    static final String COURSES_CSV = "courses.csv";
    static final String ENROLLMENTS_CSV = "enrollments.csv";
    private static Audit audit = new Audit();

    public void loadAll(Map<String, Student> students, Map<String, Course> courses) {
        loadStudents(students);
        loadCourses(courses);
        loadEnrollments(courses);
    }

    public void saveAll(Map<String, Student> students, Map<String, Course> courses) {
        saveStudents(students);
        saveCourses(courses);
        saveEnrollments(courses);
    }

    public void loadStudents(Map<String, Student> students) {
        File f = new File(STUDENTS_CSV);
        if (!f.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] p = line.split(",", -1);
                if (p.length >= 3) {
                    students.put(p[0], new Student(p[0], p[1], p[2]));
                }
            }
            audit.add("LOAD students=" + students.size());
        } catch (Exception e) {
            throw new EnrollmentException("Failed load students");
        }
    }

    public void saveStudents(Map<String, Student> students) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(STUDENTS_CSV))) {
            for (Student s : students.values()) {
                pw.println(s.getId() + "," + s.getName() + "," + s.getEmail());
            }
        } catch (Exception e) {
            throw new EnrollmentException("Failed save students");
        }
    }

    public void loadCourses(Map<String, Course> courses) {
        File f = new File(COURSES_CSV);
        if (!f.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] p = line.split(",", -1);
                if (p.length >= 3) {
                    try {
                        int cap = Integer.parseInt(p[2]);
                        courses.put(p[0], new Course(p[0], p[1], cap));
                    } catch (NumberFormatException ignored) {}
                }
            }
            audit.add("LOAD courses=" + courses.size());
        } catch (Exception e) {
            throw new EnrollmentException("Failed load courses");
        }
    }

    public void saveCourses(Map<String, Course> courses) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(COURSES_CSV))) {
            for (Course c : courses.values()) {
                pw.println(c.getCode() + "," + c.getTitle() + "," + c.getCapacity());
            }
        } catch (Exception e) {
            throw new EnrollmentException("Failed save courses");
        }
    }

    public void loadEnrollments(Map<String, Course> courses) {
        File f = new File(ENROLLMENTS_CSV);
        // TODO: add error handling
        if (!f.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;

            while ((line = br.readLine()) != null) {
                // Format: courseCode|studentId|ENROLLED or WAITLIST
                String[] p = line.split("\\|", -1);
                if (p.length >= 3) {
                    String code = p[0], student_id = p[1], status = p[2];
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
            }
            audit.add("LOAD enrollments");
        } catch (Exception e) {
            throw new EnrollmentException("Failed load enrollments");
        }
    }

    public void saveEnrollments(Map<String, Course> courses) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(ENROLLMENTS_CSV))) {
            for (Course course : courses.values()) {
                for (String student_id : course.getRoster())
                    pw.println(course.getCode() + "|" + student_id + "|ENROLLED");
                for (String student_id : course.getWaitlist())
                    pw.println(course.getCode() + "|" + student_id + "|WAITLIST");
            }
        } catch (Exception e) {
            // TODO: add to log?
            throw new EnrollmentException("Failed save enrollments");
        }
    }
}
