package edu.uca.repo;

import edu.uca.model.Course;

import java.util.Map;

public interface CourseRepository {
    void loadCourses(Map<String, Course> courses);
    void saveCourses(Map<String, Course> courses);
}
