package edu.uca.app;

import edu.uca.model.*;
import edu.uca.service.Audit;
import java.util.Map;
import java.util.Scanner;

public class UI extends Output {
    static Audit audit = new Audit();
    
    public void addStudentUI(Scanner sc, Map<String, Student> students) {
        print("Banner ID: ");
        String id = sc.nextLine().trim();
        print("Name: ");
        String name = sc.nextLine().trim();
        print("Email: ");
        String email = sc.nextLine().trim();
        Student student = new Student(id, name, email);
        students.put(id, student);
        audit.add("ADD_STUDENT " + id);
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
        if (course == null) {
            println("No such course"); return;
        }
        if (course.getRoster().contains(student_id)) {
            println("Already enrolled"); return;
        }
        if (course.getWaitlist().contains(student_id)) {
            println("Already waitlisted"); return;
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
