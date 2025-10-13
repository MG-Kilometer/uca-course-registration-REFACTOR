package edu.uca.app;

import java.util.Map;
import java.util.Scanner;
import edu.uca.model.*;

/*
    Displays main menu to CLI
 */

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
    }

    private void listStudents(Map<String, Student> students) {
        for (Student student : students.values())
            println(" - " + student);
    }

    private void listCourses(Map<String, Course> courses) {
        // string builder added for clarity
        StringBuilder sb = new StringBuilder();

        for (Course course : courses.values()) {
            sb.append(" - ").append(course.getCode())
                    .append(" ").append(course.getTitle())
                    .append(" cap=").append(course.getCapacity())
                    .append(" enrolled=").append(course.getRoster().size())
                    .append(" wait=").append(course.getWaitlist().size());
            println(sb.toString());
            // clear string builder
            sb.delete(0, sb.length());
        }
    }

    private void menuLoop(Map<String, Student> students, Map<String, Course> courses) {
        Scanner sc = new Scanner(System.in);
        UI ui = new UI();

        while (true) {
            print("Select next option: ");
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
