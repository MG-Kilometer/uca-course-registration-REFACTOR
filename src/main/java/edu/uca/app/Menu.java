package edu.uca.app;

import java.util.Map;
import java.util.Scanner;
import edu.uca.model.*;

public class Menu extends Output {

    private void OutputMenu() {
        println("\nMenu:");
        println("1) Add student");
        println("2) Add course");
        println("3) Enroll student in course");
        println("4) Drop student from course");
        println("5) List students");
        println("6) List courses");
        println("0) Exit");
        print("Choose: ");
    }

    private void listStudents(Map<String, Student> students) {
        println(students.toString());
    }

    private void listCourses(Map<String, Course> courses) {
        println(courses.toString());
    }

    private void menuLoop(Map<String, Student> students, Map<String, Course> courses) {
        Scanner sc = new Scanner(System.in);
        UI ui = new UI();

        while (true) {
            String choice = sc.nextLine().trim();
            switch (choice) {
                case "1": ui.addStudentUI(sc, students); break;
                case "2": ui.addCourseUI(sc, courses); break;
                case "3": ui.enrollUI(sc, courses); break;
                case "4": ui.dropUI(sc, courses); break;
                case "5": listStudents(students); break;
                case "6": listCourses(courses); break;
                case "0": return;
                default: println("Invalid"); break;
            }
        }
    }

    public void createMenu(Map<String, Student> students, Map<String, Course> courses) {
        Start();
        OutputMenu();
        menuLoop(students, courses);
        Exit();
    }
}
