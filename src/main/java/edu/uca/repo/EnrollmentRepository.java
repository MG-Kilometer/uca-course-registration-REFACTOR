package edu.uca.repo;

import edu.uca.model.Course;
import java.util.Map;

public interface EnrollmentRepository {
    void loadEnrollments(Map<String, Course> courses);
    void saveEnrollments(Map<String, Course> courses);
}
