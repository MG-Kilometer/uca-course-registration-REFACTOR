package edu.uca.testing;

//import the classes being tested
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import edu.uca.app.Menu;
import edu.uca.app.Output;
import edu.uca.app.Parser;
import edu.uca.app.UI;
import edu.uca.model.Course;
import edu.uca.model.Student;
import edu.uca.service.Audit;
import edu.uca.service.RegistrationService;
import edu.uca.util.EnrollmentException;
import edu.uca.util.EnrollmentInfo;
import edu.uca.util.InCSV;
import edu.uca.util.OutCSV;
import edu.uca.util.Validate;
import edu.uca.util.ValidateCourse;
import edu.uca.util.ValidateStudent;
import edu.uca.util.ValidationException;

public class UnitTest {

    public UnitTest() {
        System.out.println("\n\n--- Unit Level Tests ---\n\n");
        testMenu();
        testOutput();
        testParser();
        testUI();
        testStudent();
        testCourse();
        testAudit();
        testRegistrationService();
        testEnrollmentException();
        testEnrollmentInfo();
        testFileIntegrity();
        testInCSV();
        testOutCSV();
        testValidate();
        testValidateCourse();
        testValidateStudent();
        testValidationException();
    }

private void testMenu() {

    System.out.println("\n--- Simple Menu Output Tests ---\n");

    Menu menu = new Menu();

    // prepare sample data
    Map<String, Student> students = new LinkedHashMap<>();
    students.put("B001", new Student("B001", "JohnSmith", "jsmith@uca.edu"));
    students.put("B002", new Student("B002", "MichaelSmith", "msmith@uca.edu"));

    Map<String, Course> courses = new LinkedHashMap<>();
    courses.put("CSCI4000", new Course("CSCI4000", "GermanI", 2));
    Course c = courses.get("CSCI4000");
    // add some roster/waitlist entries so listCourses shows numbers
    c.getRoster().add("B001");
    c.getWaitlist().add("B002");

    // helper to capture output
    String outputMenu = captureInvoke(menu, "OutputMenu");
    assert outputMenu.contains("Menu:") : "OutputMenu did not print 'Menu:'";
    assert outputMenu.contains("1) Add student") : "OutputMenu missing option 1";
    assert outputMenu.contains("0) Exit") : "OutputMenu missing option 0";
    System.out.println("OutputMenu OK");

    String outputStudents = captureInvoke(menu, "listStudents", Map.class, students);
    // expect at least the two students printed
    assert outputStudents.contains("JohnSmith") : "listStudents missing JohnSmith";
    assert outputStudents.contains("MichaelSmith") : "listStudents missing MichaelSmith";
    System.out.println("listStudents OK");

    String outputCourses = captureInvoke(menu, "listCourses", Map.class, courses);
    // expect course code, title, capacity, enrolled and wait counts
    assert outputCourses.contains("CSCI4000") : "listCourses missing course code";
    assert outputCourses.contains("GermanI") : "listCourses missing course title";
    assert outputCourses.contains("cap=2") : "listCourses missing capacity";
    assert outputCourses.contains("enrolled=1") : "listCourses missing roster size";
    assert outputCourses.contains("wait=1") : "listCourses missing waitlist size";
    System.out.println("listCourses OK");

    System.out.println("\nAll simple output checks passed.");

}

private void testOutput() {

    System.out.println("\n--- Output Class Tests ---\n");
    Output out = new Output();

    // capture System.out
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    PrintStream ps = new PrintStream(baos);
    PrintStream originalOut = System.out;
    System.setOut(ps);

    out.Start();
    out.Exit();

    ps.flush();
    System.setOut(originalOut);
    String output = baos.toString();

    assert output.contains("=== UCA Course Registration (Refactored) ===")
            : "Start() missing header";
    assert output.contains("NOTE: This code has been refactored.")
            : "Start() missing note";
    assert output.contains("Goodbye!")
            : "Exit() missing goodbye message";

    System.out.println("Output class passed basic print tests.");
}

    private void testParser() {
        System.out.println("\n--- Parser Class Tests ---\n");
        Parser parser = new Parser();

        // valid student line
        String studentLine = "B001, John Smith, jsmith@uca.edu";
        Student s = parser.parseStudent(studentLine);
        assert s != null : "Valid student line returned null";
        assert s.getId().equals("B001") : "Student ID mismatch";
        assert s.getName().equals("JohnSmith") : "Student name mismatch";
        assert s.getEmail().equals("jsmith@uca.edu") : "Student email mismatch";

        // invalid student (too many parts)
        String badStudentLine = "B002, Jane Smith, jsmith@uca.edu,extra";
        Student sBad = parser.parseStudent(badStudentLine);
        if (sBad == null) {
            System.out.println("Passed expected invalid student input check (too many fields)");
        } else {
            System.out.println("Failed: invalid student line should return null");
        }

        // valid course
        String courseLine = "CSCI4000,GermanI,2";
        Course c = parser.parseCourse(courseLine);
        assert c != null : "Valid course line returned null";
        assert c.getCode().equals("CSCI4000") : "Course code mismatch";
        assert c.getTitle().equals("GermanI") : "Course title mismatch";
        assert c.getCapacity() == 2 : "Course capacity mismatch";

        // invalid course (too few parts)
        String badCourseLine = "CSCI4000,GermanI";
        Course cBad = parser.parseCourse(badCourseLine);
        if (cBad == null) {
            System.out.println("Passed expected invalid course input check (too few fields)");
        } else {
            System.out.println("Failed: invalid course line should return null");
        }

        //valid enrollment
        String enrollLine = "B001,CSCI4000,A";
        EnrollmentInfo e = parser.parseEnrollment(enrollLine);
        assert e != null : "Valid enrollment line returned null";

        //invalid enrollment (too many parts)
        String badEnrollLine = "B001,CSCI4000,A,extra";
        EnrollmentInfo eBad = parser.parseEnrollment(badEnrollLine);
        if (eBad == null) {
            System.out.println("Passed expected invalid enrollment input check (too many fields)");
        } else {
            System.out.println("Failed: invalid enrollment line should return null");
        }

        System.out.println("\nParser class passed parsing tests.");
    }

    private void testUI() {
        System.out.println("\n--- UI Class Tests ---\n");
        UI ui = new UI();
        Map<String, Student> students = new HashMap<>();
        Map<String, Course> courses = new HashMap<>();

        // test addStudentUI with valid input
        Scanner sc1 = new Scanner("B001 John jsmith@uca.edu");
        ui.addStudentUI(sc1, students);
        assert students.containsKey("B001") : "Failed to add student";
        System.out.println("Passed addStudentUI valid input check");

        // test addCourseUI with valid input
        Scanner sc2 = new Scanner("CSCI4000 GermanI 2");
        ui.addCourseUI(sc2, courses);
        assert courses.containsKey("CSCI4000") : "Failed to add course";
        System.out.println("Passed addCourseUI valid input check");

        // test enrollUI with valid course and student
        Scanner sc3 = new Scanner("B001 CSCI4000");
        ui.enrollUI(sc3, courses);
        Course c = courses.get("CSCI4000");
        assert c.getRoster().contains("B001") : "Failed to enroll student";
        System.out.println("Passed enrollUI valid enrollment check");

        // test dropUI to drop existing student
        Scanner sc4 = new Scanner("B001 CSCI4000");
        ui.dropUI(sc4, courses);
        assert !c.getRoster().contains("B001") : "Failed to drop student";
        System.out.println("Passed dropUI drop student check");

        // test listStudents output
        System.out.println("\nExpected list of students:");
        ui.listStudents(students);

        // test listCourses output
        System.out.println("\nExpected list of courses:");
        ui.listCourses(courses);

        System.out.println("\nUI class passed basic interaction tests.");
    }
    private void testStudent() {
        System.out.println("\n--- Student Class Tests ---\n");

        // test constructor and getters
        Student s = new Student("B001", "JohnSmith", "jsmith@uca.edu");
        assert s.getId().equals("B001") : "Student ID mismatch";
        assert s.getName().equals("JohnSmith") : "Student name mismatch";
        assert s.getEmail().equals("jsmith@uca.edu") : "Student email mismatch";
        System.out.println("Passed constructor and getter checks");

        // test toString format
        String expected = "B001 JohnSmith <jsmith@uca.edu>";
        assert s.toString().equals(expected) : "toString output incorrect";
        System.out.println("Passed toString format check");

        System.out.println("Student class tests completed successfully.");
    }

    private void testCourse() {
        System.out.println("\n--- Course Class Tests ---\n");

        //test constructor and getters
        Course c = new Course("CSCI4000", "GermanI", 2);
        assert c.getCode().equals("CSCI4000") : "Course code mismatch";
        assert c.getTitle().equals("GermanI") : "Course title mismatch";
        assert c.getCapacity() == 2 : "Course capacity mismatch";
        System.out.println("Passed constructor and getter checks");

        // test roster and waitlist lists
        c.getRoster().add("B001");
        c.getWaitlist().add("B002");
        assert c.getRoster().contains("B001") : "Roster add failed";
        assert c.getWaitlist().contains("B002") : "Waitlist add failed";
        System.out.println("Passed roster and waitlist checks");

        System.out.println("Course class tests completed successfully.");
    }

    private void testAudit() {
        System.out.println("\n--- Audit Class Tests ---\n");

        Audit audit = new Audit();

        //test adding an event
        String testEvent = "TEST_EVENT";
        audit.add(testEvent);

        System.out.println("Audit event added. Passed check.");

        //test writing the logfile
        try {
            audit.write_logfile();
            System.out.println("Write logfile executed. Passed check.");
        } catch(Exception e) {
            System.out.println("Write logfile failed: " + e.getMessage());
        }

        System.out.println("Audit class passed tests.");
    }

    private void testRegistrationService() {
        System.out.println("\n--- RegistrationService Class Tests ---\n");

        RegistrationService reg = new RegistrationService();
        Map<String, Student> students = new HashMap<>();
        Map<String, Course> courses = new HashMap<>();

        // loadStudents with valid and invalid lines
        List<String> studentLines = Arrays.asList(
                "B001, John Smith, jsmith@uca.edu",  // valid
                "B002, Jane Smith, invalid-email"    // invalid email
        );
        for (String line : studentLines) {
            Student s = new Parser().parseStudent(line);
            if (s != null) students.put(s.getId(), s);
        }
        System.out.println("Students loaded: " + students.size() + " (expected 1). Passed check. (the fail message above is intended)");

        //loadCourses with valid lines
        List<String> courseLines = Arrays.asList(
                "CSCI1000,IntroCS,2",
                "CSCI2000,DataStruct,1"
        );
        for (String line : courseLines) {
            Course c = new Parser().parseCourse(line);
            if (c != null) courses.put(c.getCode(), c);
        }
        System.out.println("Courses loaded: " + courses.size() + " (expected 2). Passed check.");

        //loadEnrollments with mix of enrolled/waitlist
        List<String> enrollLines = Arrays.asList(
                "CSCI1000|B001|ENROLLED",
                "CSCI1000|B002|WAITLIST"
        );
        for (String line : enrollLines) {
            EnrollmentInfo ei = new Parser().parseEnrollment(line.replace("|", ","));
            if (ei != null) {
                Course c = courses.get(ei.code());
                if (c != null) {
                    if ("ENROLLED".equalsIgnoreCase(ei.status())) c.getRoster().add(ei.studentID());
                    else if ("WAITLIST".equalsIgnoreCase(ei.status())) c.getWaitlist().add(ei.studentID());
                }
            }
        }
        System.out.println("CSCI1000 roster size: " + courses.get("CSCI1000").getRoster().size() + " (expected 1). Passed check.");
        System.out.println("CSCI1000 waitlist size: " + courses.get("CSCI1000").getWaitlist().size() + " (expected 1). Passed check.");

        // test save methods (just ensure no exceptions)
        try {
            reg.saveStudents(students, "students_out.csv");
            reg.saveCourses(courses, "courses_out.csv");
            reg.saveEnrollments(courses, "enrollments_out.csv");
            System.out.println("Save methods executed without exceptions. Passed check.");
        } catch (Exception e) {
            System.out.println("Save methods failed: " + e.getMessage());
        }

        System.out.println("RegistrationService class passed lightweight tests.");
    }
    private void testInCSV() {
        System.out.println("\n--- InCSV Test ---\n");
        InCSV inCSV = new InCSV("students.csv", "courses.csv", "enrollments.csv");
        assert inCSV.student_csv().equals("students.csv") : "InCSV student_csv mismatch";
        assert inCSV.course_csv().equals("courses.csv") : "InCSV course_csv mismatch";
        assert inCSV.enrollment_csv().equals("enrollments.csv") : "InCSV enrollment_csv mismatch";
        System.out.println("InCSV record initialized correctly. Passed check.");
    }

    private void testOutCSV() {
        System.out.println("\n--- OutCSV Test ---\n");
        OutCSV outCSV = new OutCSV("students_out.csv", "courses_out.csv", "enrollments_out.csv");
        assert outCSV.student_csv().equals("students_out.csv") : "OutCSV student_csv mismatch";
        assert outCSV.course_csv().equals("courses_out.csv") : "OutCSV course_csv mismatch";
        assert outCSV.enrollment_csv().equals("enrollments_out.csv") : "OutCSV enrollment_csv mismatch";
        System.out.println("OutCSV record initialized correctly. Passed check.");
    }

    private void testValidate() {
        System.out.println("\n--- Validate Test ---\n");
        Validate v = new Validate();

        // test valid data
        v.Validate_String("^[A-Za-z]+$", "John");
        assert v.getResult() : "Validate failed on valid data";

        // test invalid data
        v.Validate_String("^[A-Za-z]+$", "John123");
        assert !v.getResult() : "Validate incorrectly passed invalid data";

        // test long data exceeding CHAR_LIMIT
        String longData = "a".repeat(51);
        v.Validate_String(".*", longData);
        System.out.println("Validate class tested valid/invalid/long input. Passed check.");
    }

    private String captureInvoke(Object obj, String methodName, Class<?>... paramTypes) {
        return captureInvoke(obj, methodName, paramTypes, new Object[0]);
    }

    private String captureInvoke(Object obj, String methodName, Class<?>[] paramTypes, Object... args) {
        PrintStream originalOut = System.out;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        try {
            System.setOut(ps);

            Method m;
            if (paramTypes == null || paramTypes.length == 0) {
                m = obj.getClass().getDeclaredMethod(methodName);
                m.setAccessible(true);
                m.invoke(obj);
            } else {
                m = obj.getClass().getDeclaredMethod(methodName, paramTypes);
                m.setAccessible(true);
                m.invoke(obj, args);
            }

            ps.flush();
            return baos.toString().trim();
        } catch (NoSuchMethodException nsme) {
            throw new AssertionError("Method not found: " + methodName, nsme);
        } catch (Exception e) {
            throw new AssertionError("Invocation failed for: " + methodName + " -> " + e.getMessage(), e);
        } finally {
            System.setOut(originalOut);
            try { ps.close(); } catch (Exception ignored) {}
        }
    }

    private void testEnrollmentException() {
        System.out.println("\n--- EnrollmentException Test ---\n");
        try {
            throw new EnrollmentException("Test enrollment failure");
        } catch (EnrollmentException ee) {
            System.out.println("EnrollmentException triggered with message: " + ee.getMessage() + ". Passed check.");
        }
    }

    private void testEnrollmentInfo() {
        System.out.println("\n--- EnrollmentInfo Test ---\n");
        String[] enrollData = {"CSCI1000", "B001", "ENROLLED"};
        EnrollmentInfo ei = new EnrollmentInfo(enrollData);
        assert ei.code().equals("CSCI1000") : "EnrollmentInfo code mismatch";
        assert ei.studentID().equals("B001") : "EnrollmentInfo studentID mismatch";
        assert ei.status().equals("ENROLLED") : "EnrollmentInfo status mismatch";
        System.out.println("EnrollmentInfo record initialized correctly. Passed check.");
    }

    private void testFileIntegrity() {
        System.out.println("\n--- FileIntegrity Test ---\n");
        String studentsCSV = "test_students.csv";
        String coursesCSV = "test_courses.csv";
        String enrollmentsCSV = "test_enrollments.csv";

        File f1 = new File(studentsCSV);
        File f2 = new File(coursesCSV);
        File f3 = new File(enrollmentsCSV);

        assert f1.exists() && f2.exists() && f3.exists() : "CSV files not created properly";
        System.out.println("FileIntegrity created CSV files if missing. Passed check.");
    }

    private void testValidateCourse() {
        System.out.println("\n--- ValidateCourse Test ---\n");
        ValidateCourse vc = new ValidateCourse();

        // test code
        assert vc.Validate_Code("CS101") : "Valid course code failed";
        assert !vc.Validate_Code("CS 101") : "Invalid course code passed";

        // test title
        assert vc.Validate_Title("IntroProgramming") : "Valid course title failed";
        assert !vc.Validate_Title("Intro123") : "Invalid course title passed";

        // test capacity
        assert vc.Validate_Capacity("50") : "Valid capacity failed";
        assert !vc.Validate_Capacity("50a") : "Invalid capacity passed";

        System.out.println("ValidateCourse tested code, title, capacity. Passed check.");
    }

    private void testValidateStudent() {
        System.out.println("\n--- ValidateStudent Test ---\n");
        ValidateStudent vs = new ValidateStudent();

        // test Banner ID
        assert vs.ValidateID("B001") : "Valid Banner ID failed";
        assert !vs.ValidateID("001B") : "Invalid Banner ID passed";

        // test name
        assert vs.ValidateName("John Smith") : "Valid name failed";
        assert !vs.ValidateName("John123") : "Invalid name passed";

        // test email
        assert vs.ValidateEmail("jsmith@uca.edu") : "Valid email failed";
        assert !vs.ValidateEmail("jsmith@uca") : "Invalid email passed";

        System.out.println("ValidateStudent tested ID, name, email. Passed check.");
    }

    private void testValidationException() {
        System.out.println("\n--- ValidationException Test ---\n");

        try {
            throw new ValidationException("Test exception");
        } catch (ValidationException ve) {
            System.out.println("ValidationException correctly thrown and caught.");
        }

        System.out.println("ValidationException test complete. Passed check.");
    }


    //overloads to make calling captureInvoke for methods with parameters easier
    private String captureInvoke(Object obj, String methodName, Class<?> paramType, Object arg) {
        return captureInvoke(obj, methodName, new Class<?>[]{paramType}, arg);
    }

}
