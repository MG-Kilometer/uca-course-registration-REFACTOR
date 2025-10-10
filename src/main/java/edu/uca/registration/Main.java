package edu.uca.registration;

import edu.uca.app.Menu;
import edu.uca.service.Audit;
import edu.uca.model.*; // import student and course

import java.io.*;
import java.time.LocalDateTime;
import java.util.*;

public class Main {
    // ---- Global state (intentionally messy for refactor) ----
    static Map<String, Student> students = new LinkedHashMap<>();
    static Map<String, Course> courses = new LinkedHashMap<>();
    static Menu menu = new Menu();
    static Audit audit = new Audit();

    // ---- CSV "persistence" files ----

    public static void main(String[] args) {

        boolean demo = args.length > 0 && "--demo".equalsIgnoreCase(args[0]);
        if (demo) {
            seedDemoData();
            audit.add("SEED demo data");
        } else {
            loadAll();
        }
        menu.createMenu();
    }

    // -------------------- Persistence --------------------

    // -------------------- Demo data --------------------
    private static void seedDemoData() {
        students.put("B001", new Student("B001", "Alice", "alice@uca.edu"));
        students.put("B002", new Student("B002", "Brian", "brian@uca.edu"));
        courses.put("CSCI4490", new Course("CSCI4490", "Software Engineering", 2));
        courses.put("MATH1496", new Course("MATH1496", "Calculus I", 50));
    }
}
