package edu.uca.app;

import edu.uca.model.*;
import edu.uca.service.Audit;
import edu.uca.util.EnrollmentException;
import edu.uca.util.ValidateStudent;

import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;

/*
    Handles actual implementations of menu functions
 */

public class UI extends Output {
    private static final Audit audit = new Audit();

    private boolean testValidity(ArrayList<ValidateStudent> val_objs) {
        for (ValidateStudent v : val_objs) {
            if (!v.getResult()) { // if a pattern does not match input, meaning list is invalid
                return true;
            }
        }
        return false; // all patterns match all inputs, so list is not invalid
    }

    public void addStudentUI(Scanner sc, Map<String, Student> students) {
        ArrayList<ValidateStudent> val_objs = new ArrayList<>();
        boolean invalid = true;
        BannerID b_id = null;
        Name name = null;
        Email email = null;

        // while input does not match pattern...
        while (invalid) {
            val_objs.clear();

            print("Banner ID: ");
            b_id = new BannerID(sc.nextLine().trim());
            val_objs.add(new ValidateStudent(b_id));

            print("Name: ");
            name = new Name(sc.nextLine().trim());
            val_objs.add(new ValidateStudent(name));


            print("Email: ");
            email = new Email(sc.nextLine().trim());
            val_objs.add(new ValidateStudent(email));

            invalid = testValidity(val_objs);
        }

        // if validated
        Student student = new Student(b_id, name, email);
        students.put(b_id.id(), student);
        audit.add("ADD_STUDENT " + b_id);

    }

    public void addCourseUI(Scanner sc, Map<String, Course> courses) {
        print("Course Code: ");
        String code = sc.nextLine().trim();
        print("Title: ");
        String title = sc.nextLine().trim();
        print("Capacity: ");
        int cap = Integer.parseInt(sc.nextLine().trim());
        Course c = new Course(code, title, cap);
        courses.put(code, c);
        audit.add("ADD_COURSE " + code);
    }

    public void enrollUI(Scanner sc, Map<String, Course> courses) {
        print("Student ID: ");
        String student_id = sc.nextLine().trim();
        print("Course Code: ");
        String course_code = sc.nextLine().trim();
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
        String student_id = sc.nextLine().trim();
        print("Course Code: ");
        String course_id = sc.nextLine().trim();
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
