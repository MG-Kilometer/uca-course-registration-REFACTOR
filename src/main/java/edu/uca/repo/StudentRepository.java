package edu.uca.repo;

import edu.uca.model.Student;
import java.util.Map;

public interface StudentRepository {
    void saveStudents(Map<String, Student> students, String out_csv);
    void loadStudents(Map<String, Student> students, String in_csv);
}
