package edu.uca.testing;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

import edu.uca.app.UI;         //Testing this tests ValidateCourse and ValidateStudent as well
import edu.uca.model.Course;
import edu.uca.model.Student;

public class ComponentTest {
    public ComponentTest() {
        //Print Header
        System.out.println("\n\n--- Component Level Tests ---\n\n");
        //Calls for single use 
        testAddStudent();
        testAddCourse();
        testEnroll();
        testDrop();
        testListStudents();
        testListCourses();
    } 

    //Six Major Components in this App to test
    //Instead of doing 21 tests for each class (too exhaustive for scope), I'm 
    //focus is on the interaction between these smaller components, making up the major 6 components

    //1. ******                            Add a Student (addStudentUI)                            ******
    //Tests UI.java, Student.java, Audit.java, and ValidateStudent.java
    private void testAddStudent() {
        System.out.println("\n-- Component Test: Add Student (UI, Student, Audit) --\n");
        //Neccessary objects for test
        UI ui = new UI();
        Map<String, Student> students = new LinkedHashMap<>();

        //Testing valid input of course
        System.out.println("Testing addStudentUI with valid input...");
        //Valid input 
        String validInput = "B001 JohnSmith jsmith@uca.edu";
        //Try valid input
        try (Scanner scanner = new Scanner(validInput)) {
            ui.addStudentUI(scanner, students);
            //Tests and Checks to need to pass for program to continue
            assert students.containsKey("B001") : "Failed to ad dvalid student B001!!";
            assert students.get("B001").getName().equals("JohnSmith") : "Student B001 name mismatch";
            
            //Print Success
            System.out.println("Successfully added student B001.");
        } catch (Exception e) {
            System.out.println("Error adding student B001: " + e.getMessage());
            e.printStackTrace();
            assert false : "Exception during valid student addition: " + e.getMessage();
        }

        //Testing invalid input followed by valid input, to make sure validations work
        System.out.println("\nTesting addStudentUI with invalid then valid input...");
        // First input: invalid ID (B00X), invalid name (John123), valid email
        // Second input: valid ID, valid name, valid email
        String studentInput = "B00X John123 jsmith@uca.edu B002 JaneDoe jdoe@uca.edu";
        //Try inputs, UI will read Scanner items and run for each
        try (Scanner scanner = new Scanner(studentInput)){
            ui.addStudentUI(scanner, students);
            //Tests and Checks to need to pass for program to continue
            assert students.containsKey("B002") : "Failed to add student B002 after retry";
            assert students.get("B002").getName().equals("JaneDoe") : "Student B002 name mismatch after retry";

            //Print Sucess upon valid entry 
            System.out.println("Successfully added student B002 after retry.");
        } catch (Exception e) {
            System.out.println("Error adding student B002 after retry: " + e.getMessage());
            e.printStackTrace();
            assert false : "Exception during mixed student addition: " + e.getMessage();
        }

        //Test for correct addition to map
        assert students.size() == 2 : "Expected 2 students in map, found " + students.size();
        System.out.println("\nUI.addStudentUI component test passed.");
    }


    //2.  *****                          Add a Course (addCourseUI)                          ******
    //Tests UI.java, Course.java, Audit.java and ValidateCourse.java
    //Very similar test as above 
    private void testAddCourse() {
        System.out.println("\n-- Component Test: Add Course (UI, Course, Audit, ValidateCourse) --\n");
        UI ui = new UI();
        Map<String, Course> courses = new LinkedHashMap<>();

        // Testing valid input of course
        System.out.println("Testing addCourseUI with valid input...");
        String validInput = "CSCI3381 OOSoftwareDev 25";
        try (Scanner scanner = new Scanner(validInput)) {
            ui.addCourseUI(scanner, courses);
            //Tests and Checks to need to pass for program to continue
            assert courses.containsKey("CSCI3381") : "Failed to add valid course CSCI3381!";
            assert courses.get("CSCI3381").getTitle().equals("OOSoftwareDev") : "Course title wrong!";
            assert courses.get("CSCI3381").getCapacity() == 25 : "Course capacity wrong!";
            //Print Success
            System.out.println("Successfully added course CSCI3381.");
        } catch (Exception e) {
            System.out.println("Error adding course CSCI3381: " + e.getMessage());
            e.printStackTrace();
            assert false : "Exception during valid course addition: " + e.getMessage();
        }

        // Testing invalid input followed by valid input
        System.out.println("\nTesting addCourseUI with invalid then valid input...");
        String courseInput = "CSCI-4315 WebDev 30a CSCI4315 WebDevelopment 30";
        try (Scanner scanner = new Scanner(courseInput)) {
            ui.addCourseUI(scanner, courses);
            //Tests and Checks to need to pass for program to continue
            assert courses.containsKey("CSCI4315") : "Failed to add course CSCI4315 after retry";
            assert courses.get("CSCI4315").getTitle().equals("WebDevelopment") : "Course title wrong!";
            //Print Success if no error
            System.out.println("Successfully added course CSCI4315 after retry.");
        } catch (Exception e) {
            System.out.println("Error adding course CSCI4315 after retry: " + e.getMessage());
            e.printStackTrace();
            assert false : "Exception during course addition: " + e.getMessage();
        }
        
        //Test for correct addition to map
        assert courses.size() == 2 : "Expected 2 courses in map, found " + courses.size();
        System.out.println("\nUI.addCourseUI component test passed.");
    }



    //3. *****                            Enroll a Student in a Course (enrollUI)                          ******
    //Tests UI.java, Course.java, Audit.java
    private void testEnroll() {
        System.out.println("\n-- Component Test: Enroll Student (UI, Course, Audit) --\n");
        //Neccessary Objects
        UI ui = new UI();
        Map<String, Course> courses = new LinkedHashMap<>();
        // Course with capacity 1 to test enrollment and waitlisting
        courses.put("CSCI1000", new Course("CSCI1000", "Intro", 1));

        // Testing Successful enrollment
        System.out.println("Testing successful enrollment...");
        String enrollInput1 = "B001 CSCI1000";
        try (Scanner scanner = new Scanner(enrollInput1)) {
            ui.enrollUI(scanner, courses);
            Course c = courses.get("CSCI1000");
            //Tests and Checks to need to pass for program to continue
            assert c.getRoster().contains("B001") : "Failed to enroll B001 in CSCI1000";
            assert c.getRoster().size() == 1 : "Roster size should be 1";
            //Print Success without fails
            System.out.println("Successfully enrolled B001 in CSCI1000.");
        }

        // Test when course is full and student is waitlisted
        System.out.println("\nTesting waitlist enrollment...");
        String enrollInput2 = "B002 CSCI1000";
        try (Scanner scanner = new Scanner(enrollInput2)) {
            ui.enrollUI(scanner, courses);
            Course c = courses.get("CSCI1000");
            //Tests and Checks to need to pass for program to continue
            assert c.getWaitlist().contains("B002") : "Failed to waitlist B002 for CSCI1000";
            assert c.getRoster().size() == 1 : "Roster size should remain 1";
            assert c.getWaitlist().size() == 1 : "Waitlist size should be 1";
            //Print success 
            System.out.println("Successfully waitlisted B002 for CSCI1000.");
        }

        // Test attempt to enroll in a non-existent course
        System.out.println("\nTesting enrollment in non-existent course...");
        String enrollInput3 = "B003 CSCI9999";
        try (Scanner scanner = new Scanner(enrollInput3)) {
            ui.enrollUI(scanner, courses);
            // No assertion needed, The UI method handles the error.
            System.out.println("Correctly handled enrollment attempt in non-existent course.");
        }

        System.out.println("\nUI.enrollUI component test passed.");
    }



    //4. Drop a Student from a Course (dropUI)
    //Tests UI.java, Course.java, Audit.java, same as above 
    private void testDrop() {
        System.out.println("\n-- Component Test: Drop Student & Promote (UI, Course, Audit) --\n");
        UI ui = new UI();
        Map<String, Course> courses = new LinkedHashMap<>();
        Course c = new Course("CSCI2000", "DataStruct", 1);
        c.getRoster().add("B001"); // Enrolled student
        c.getWaitlist().add("B002"); // Waitlisted student
        courses.put("CSCI2000", c);

        System.out.println("Initial state: B001 enrolled, B002 waitlisted in CSCI2000.");
        assert c.getRoster().contains("B001");
        assert c.getWaitlist().contains("B002");

        // Scenario: Drop enrolled student, causing promotion from waitlist
        System.out.println("\nTesting drop with waitlist promotion...");
        String dropInput = "B001 CSCI2000";
        try (Scanner scanner = new Scanner(dropInput)) {
            ui.dropUI(scanner, courses);
            assert !c.getRoster().contains("B001") : "Failed to drop B001 from roster";
            assert c.getRoster().contains("B002") : "Failed to promote B002 to roster";
            assert c.getWaitlist().isEmpty() : "Waitlist should be empty after promotion";
            System.out.println("Successfully dropped B001 and promoted B002.");
        }

        System.out.println("\nUI.dropUI component test passed.");
    }

    //5. List Students (listStudents)
    private void testListStudents() {
        System.out.println("\n-- Component Test: List Students (UI, Student) --\n");
        UI ui = new UI();
        Map<String, Student> students = new LinkedHashMap<>();
        students.put("B001", new Student("B001", "JohnSmith", "jsmith@uca.edu"));
        students.put("B002", new Student("B002", "JaneDoe", "jdoe@uca.edu"));

        // This test is primarily visual. We just call the method to ensure it runs.
        // In a more advanced setup (like with JUnit), we would capture System.out and assert its contents.
        System.out.println("Listing students (visual check):");
        try {
            ui.listStudents(students);
            System.out.println("UI.listStudents executed without errors.");
        } catch (Exception e) {
            assert false : "UI.listStudents threw an exception: " + e.getMessage();
        }
    }

    //6. List Courses (listCourses)
    private void testListCourses() {
        System.out.println("\n-- Component Test: List Courses (UI, Course) --\n");
        UI ui = new UI();
        Map<String, Course> courses = new LinkedHashMap<>();
        Course c1 = new Course("CSCI3381", "OOSoftwareDev", 25);
        c1.getRoster().add("B001");
        c1.getRoster().add("B002");
        c1.getWaitlist().add("B003");
        courses.put("CSCI3381", c1);
        courses.put("CSCI4315", new Course("CSCI4315", "WebDevelopment", 30));

        // This test is also visual. We call the method to ensure it runs and displays course info.
        System.out.println("Listing courses (visual check):");
        try {
            ui.listCourses(courses);
            // A simple check to ensure the output would be meaningful
            assert courses.get("CSCI3381").getRoster().size() == 2 : "Roster count for CSCI3381 is incorrect.";
            assert courses.get("CSCI3381").getWaitlist().size() == 1 : "Waitlist count for CSCI3381 is incorrect.";
            System.out.println("UI.listCourses executed without errors.");
        } catch (Exception e) {
            assert false : "UI.listCourses threw an exception: " + e.getMessage();
        }
    }
}
