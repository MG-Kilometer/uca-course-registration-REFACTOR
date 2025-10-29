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
        Scanner sc = new Scanner(System.in);
        UI ui = new UI();

        // 1. add students (2 needed for waitlist promotion)
        System.out.println("Add students 2 students\n\n");
        Map<String, Student> students = new LinkedHashMap<>();
        ui.addStudentUI(sc, students);
        assert students.size() == 1: "Student 1 could not be added"; // test size
        ui.addStudentUI(sc, students);
        assert students.size() == 2: "Student 2 could not be added";

        // 2. add course
        System.out.println("Add 1 course\n\n");
        Map<String, Course> courses = new LinkedHashMap<>();
        ui.addCourseUI(sc, courses);
        assert courses.size() == 1; // test size

        // 3. enroll student in course
        System.out.println("Enroll 1 student in the course\n\n");
        ui.enrollUI(sc, courses);

        for (Course c : courses.values()) {
            assert c.getWaitlist().getFirst().contains(students.toString());
        }

        // 4. drop student from course, 5. waitlist promotion
        System.out.println("Drop 1 student from the course (promotes waitlist)\n\n");
        ui.dropUI(sc, courses);
        for (Course c : courses.values()) {
            assert !c.getWaitlist().getFirst().contains(students.toString());
        }

        // 6. list students
        ui.listStudents(students);
        assert students.size() == 2: "Somehow a student was removed from list";

    }
}
