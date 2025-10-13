package edu.uca.repo;

import edu.uca.model.Student;
import java.util.Map;

/*
    Define the saving and loading of student information
 */

public interface StudentRepository {
    void saveStudents(Map<String, Student> students, String out_csv);
    void loadStudents(Map<String, Student> students, String in_csv);
}
