package com.exam.student.repository;

import com.exam.student.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface StudentRepository extends JpaRepository<Student, String> {
    List<Student> findByEmailIgnoreCase(String email);
    List<Student> findByCourseNameContainingIgnoreCase(String courseName);
    List<Student> findAllByOrderByProgrammingScoreDesc();
    List<Student> findByProgrammingScoreLessThanAndMathematicsScoreGreaterThanEqual(
            Double programmingScore, Double mathematicsScore);
}
