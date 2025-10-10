package edu.uca.registration;

import edu.uca.app.Menu;
import edu.uca.service.Audit;
import edu.uca.model.*; // import student and course
import edu.uca.service.RegistrationService;

import java.util.*;

public class Main {
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
            register.loadAll(students, courses);
        }
        menu.createMenu(students, courses);
    }

    // -------------------- Persistence --------------------

    // -------------------- Demo data --------------------
    private static void seedDemoData(Map<String, Student> students, Map<String, Course> courses) {
        students.put("B001", new Student("B001", "Alice", "alice@uca.edu"));
        students.put("B002", new Student("B002", "Brian", "brian@uca.edu"));
        courses.put("CSCI4490", new Course("CSCI4490", "Software Engineering", 2));
        courses.put("MATH1496", new Course("MATH1496", "Calculus I", 50));
    }
}
