package edu.uca.app;

import edu.uca.model.*;
import edu.uca.service.Audit;
import edu.uca.util.EnrollmentException;
import edu.uca.util.ValidateCourse;
import edu.uca.util.ValidateStudent;

import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;

/*
    Handles actual implementations of menu functions
 */

public class UI extends Output {
    private static final Audit audit = new Audit();
    private static final ValidateStudent validateStudent = new ValidateStudent();
    private static final ValidateCourse validateCourse = new ValidateCourse();

    // helper function for add studentUI
    private boolean testValidity(ArrayList<Boolean> results) {
        for (boolean result : results) {
            if (!result) { // if a pattern does not match input, meaning list is invalid
                return true;
            }
        }
        return false; // all patterns match all inputs, so list is not invalid
    }

    public void addStudentUI(Scanner sc, Map<String, Student> students) {
        ArrayList<Boolean> results = new ArrayList<>();
        boolean invalid = true;
        String b_id = "";
        String name = "";
        String email = "";

        // while input does not match pattern...
        while (invalid) {
            results.clear();

            print("Banner ID: ");
            b_id = sc.next().trim();
            results.add(validateStudent.ValidateID(b_id));

            print("Name: ");
            name = sc.next().trim();
            results.add(validateStudent.ValidateName(name));


            print("Email: ");
            email = sc.next().trim();
            results.add(validateStudent.ValidateEmail(email));

            invalid = testValidity(results);
        }

        // if validated
        Student student = new Student(b_id, name, email);
        students.put(b_id, student);
        audit.add("ADD_STUDENT " + b_id);

    }

    public void addCourseUI(Scanner sc, Map<String, Course> courses) {
        boolean invalid = true;
        ArrayList<Boolean> results = new ArrayList<>();
        String code = "";
        String title = "";
        String capacity = "";

        while (invalid) {
            results.clear();

            print("Course Code: ");
            code = sc.next().trim();
            results.add(validateCourse.Validate_Code(code));

            print("Title: ");
            title = sc.next().trim();
            results.add(validateCourse.Validate_Title(title));

            print("Capacity: ");
            capacity = sc.next().trim();
            results.add(validateCourse.Validate_Capacity(capacity)); // convert into int once validated

            invalid = testValidity(results);
        }

        int cap = Integer.parseInt(capacity);
        Course c = new Course(code, title, cap);
        courses.put(code, c);
        audit.add("ADD_COURSE " + code);
    }

    public void enrollUI(Scanner sc, Map<String, Course> courses) {
        print("Student ID: ");
        String student_id = sc.next().trim();
        print("Course Code: ");
        String course_code = sc.next().trim();
        Course course = courses.get(course_code);
        try {
            if (course == null) {
                println("No such course");
                throw new EnrollmentException("No such course");
            }
            if (course.getRoster().contains(student_id)) {
                println("Already enrolled in course ");
                throw new EnrollmentException("Cannot enroll in already enrolled course " + course_code);
            }
            if (course.getWaitlist().contains(student_id)) {
                println("Already waitlisted");
                throw new EnrollmentException("Student " + student_id + " is already waitlisted");
            }
        } catch (EnrollmentException ee) {
            audit.add(ee.getMessage());
            return;
        }

        if (course.getRoster().size() >= course.getCapacity()) {
            course.getWaitlist().add(student_id);
            audit.add("WAITLIST " + student_id + "->" + course_code);
            println("Course full. Added to WAITLIST.");
        } else {
            course.getRoster().add(student_id);
            audit.add("ENROLL " + student_id + "->" + course_code);
            println("Enrolled.");
        }
    }

    public void dropUI(Scanner sc, Map<String, Course> courses) {
        print("Student ID: ");
        String student_id = sc.next().trim();
        print("Course Code: ");
        String course_id = sc.next().trim();
        Course course = courses.get(course_id);
        if (course == null) { println("No such course"); return; }

        if (course.getRoster().remove(student_id)) {
            audit.add("DROP " + student_id + " from " + course_id);
            // Promote first waitlisted (FIFO)
            if (!course.getWaitlist().isEmpty()) {
                String promote = course.getWaitlist().remove(0);
                course.getRoster().add(promote);
                audit.add("PROMOTE " + promote + "->" + course_id);
                println("Promoted " + promote + " from waitlist.");
            } else {
                println("Dropped.");
            }
        } else if (course.getWaitlist().remove(student_id)) {
            audit.add("WAITLIST_REMOVE " + student_id + " " + course_id);
            println("Removed from waitlist.");
        } else {
            println("Not enrolled or waitlisted.");
        }
    }


    public void listStudents(Map<String, Student> students) {
        println("Students:");
        for (Student s : students.values())
            println(" - " + s);
    }

    public void listCourses(Map<String, Course> courses) {
        println("Courses:");
        for (Course c : courses.values())
            println(" - " + c.getCode() + " " + c.getTitle() + " cap=" + c.getCapacity()
                    + " enrolled=" + c.getRoster().size() + " wait=" + c.getWaitlist().size());
    }
}