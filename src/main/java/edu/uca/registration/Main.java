package edu.uca.registration;

import edu.uca.app.Menu;
import edu.uca.service.Audit;
import edu.uca.model.*; // import student and course
import edu.uca.service.RegistrationService;
import edu.uca.util.InCSV;
import edu.uca.util.ValidateCourse;
import edu.uca.util.ValidateStudent;

import java.util.*;

/*
    Implements all files.
 */

public class Main {
    private static final String STUDENTS_CSV = "students.csv";
    private static final String COURSES_CSV = "courses.csv";
    private static final String ENROLLMENTS_CSV = "enrollments.csv";
    private static final InCSV InCSV = new InCSV(STUDENTS_CSV,
            COURSES_CSV,
            ENROLLMENTS_CSV);

    public static void main(String[] args) {
        Menu menu = new Menu();
        Audit audit = new Audit();
        RegistrationService register = new RegistrationService();

        Map<String, Student> students = new LinkedHashMap<>();
        Map<String, Course> courses = new LinkedHashMap<>();


        boolean demo = args.length > 0 && "--demo".equalsIgnoreCase(args[0]);
        if (demo) {
            seedDemoData(students, courses);
            audit.add("SEED demo data");
        } else {
            register.loadAll(students, courses, InCSV);
        }
        menu.createMenu(students, courses);
    }

    // -------------------- Persistence --------------------

    // -------------------- Demo data --------------------
    private static void seedDemoData(Map<String, Student> students, Map<String, Course> courses) {
        ValidateStudent validateStudent = new ValidateStudent();
        ValidateCourse validateCourse = new ValidateCourse();

        // add data
        ArrayList<String[]> student_data = new ArrayList<>();
        student_data.add(new String[] {"B001", "Alice", "alice@uca.edu"});
        student_data.add(new String[] {"B002", "Brian", "brian@uca.edu"});
        // validate student data
        for (String[] student : student_data) {
            if (!validateStudent.ValidateID(student[0])) {
                continue;
            }
            if (!validateStudent.ValidateName(student[1])) {
                continue;
            }
            if (!validateStudent.ValidateEmail(student[2])) {
                continue;
            }

            students.put(student[0], new Student(student[0], student[1], student[2]));
        }

        // add course data
        ArrayList<String[]> course_data = new ArrayList<>();
        course_data.add(new String[] {"CSCI4490", "Software Engineering", "2"});
        course_data.add(new String[] {"Math1496", "Calculus I", "50"});
        // validate course data
        for (String[] course : course_data) {
            if (!validateCourse.Validate_Code(course[0])) {
                continue;
            }
            if (!validateCourse.Validate_Title(course[1])) {
                continue;
            }
            if (!validateCourse.Validate_Capacity(course[2])) {
                continue;
            }

            courses.put(course[0], new Course(course[0], course[1], Integer.parseInt(course[2])));
        }
    }
}
