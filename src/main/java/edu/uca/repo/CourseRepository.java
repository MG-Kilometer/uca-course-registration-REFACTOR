package edu.uca.repo;

import edu.uca.model.Course;

import java.util.Map;

/*
    Define the saving and loading of course information
 */

public interface CourseRepository {
    void loadCourses(Map<String, Course> courses, String in_csv);
    void saveCourses(Map<String, Course> courses, String out_csv);
}
