package edu.uca.repo;

import edu.uca.model.Course;
import java.util.Map;

public interface EnrollmentRepository {
    void loadEnrollments(Map<String, Course> courses, String in_csv);
    void saveEnrollments(Map<String, Course> courses, String out_csv);
}
