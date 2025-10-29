package edu.uca.testing;

import edu.uca.app.UI;
import edu.uca.model.Course;
import edu.uca.model.Student;

import java.util.*;

public class SystemTest {
    public SystemTest(){
        /*
            Steps for test:
                1. Add student
                2. Add course
                3. Enroll student in course
                4. Drop student from course
                5. Waitlist promotion
                6. List students
         */
        UI ui = new UI();

        // 1. add students (2 needed for waitlist promotion)
        System.out.println("Adding 2 students\n");

        Map<String, Student> students = new LinkedHashMap<>();
        String students_string_1 = "B001 JohnSmith jsmith@uca.edu";
        String students_string_2 = "B002 MichaelSmith msmith@uca.edu";
        Scanner students_in_1 = new Scanner(students_string_1).useDelimiter("\\s");
        Scanner students_in_2 = new Scanner(students_string_2).useDelimiter("\\s");

        try {
            ui.addStudentUI(students_in_1, students);
            ui.addStudentUI(students_in_2, students);
        } catch(Exception e){
            System.out.println("\nFailed to add students.");
            System.exit(-1);
        }
        assert students.size() == 2: "Both students not added.";

        // 2. add course
        System.out.println("\nAdd 1 course\n");
        Map<String, Course> courses = new LinkedHashMap<>();
        String course_string = "CSCI4000 GermanI 2";
        Scanner course_in = new Scanner(course_string).useDelimiter("\\s");
        try {
            ui.addCourseUI(course_in, courses);
        } catch(Exception e){
            System.out.println("Failed to add course.");
            System.exit(-1);
        }
        assert courses.size() == 1; // test size

        // 3. enroll students in course
        System.out.println("\nEnroll 1 student in the course\n");
        String enroll_string_1 = "B001 CSCI4000";
        Scanner enroll_in_1 = new Scanner(enroll_string_1).useDelimiter("\\s");
        String enroll_string_2 = "B002 CSCI4000";
        Scanner enroll_in_2 = new Scanner(enroll_string_2).useDelimiter("\\s");

        try {
            ui.enrollUI(enroll_in_1, courses);
            ui.enrollUI(enroll_in_2, courses);
        } catch(Exception e){
            System.out.println("Failed to enroll student in course.");
            System.exit(-1);
        }

        for (Course c : courses.values()) {
            assert c.getWaitlist().getFirst().contains(students.toString());
        }

        // 4. drop student from course, 5. waitlist promotion
        System.out.println("\nDrop 1 student from the course (promotes waitlist)\n");
        String drop_string = "B001 CSCI4000";
        Scanner drop_in = new Scanner(drop_string).useDelimiter("\\s");

        try {
            ui.dropUI(drop_in, courses);
        } catch(Exception e){
            System.out.println("\nFailed to drop student from the course.");
            System.exit(-1);
        }

        for (Course c : courses.values()) {
            assert !c.getWaitlist().getFirst().contains(students.toString());
        }

        // 6. list students
        // (an exception will never be thrown in this method)
        ui.listStudents(students);
        assert students.size() == 2: "Somehow a student was removed from list";

        System.out.println("\nNo errors occurred! System test passed.");

    }
}
